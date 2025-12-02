package cn.hex.ddp.manufacture.infrastructure.influx.repository


import cn.hex.ddp.manufacture.domain.simulator.model.simulation.*
import cn.hex.ddp.manufacture.domain.simulator.model.tick.ITickData
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j.Companion.log
import cn.hex.ddp.manufacture.infrastructure.config.InfluxConfig
import com.influxdb.client.*
import com.influxdb.client.domain.DeletePredicateRequest
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import com.influxdb.query.FluxTable
import jakarta.annotation.PostConstruct
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit


@Slf4j
@Repository
open class InfluxRepository {
    @Autowired
    lateinit var influxConfig: InfluxConfig
    lateinit var client: InfluxDBClient;
    lateinit var writeApiBlocking: WriteApiBlocking

    @PostConstruct
    private fun initClient() {

        val connectTimeout: Long = 100L; // 默认连接超时时间（秒）
        val readTimeout: Long = 300L;   // 默认读取超时时间（秒）
        val writeTimeout: Long = 300L;   // 默认写入超时时间（秒）

        // 设置超时时间
        // 配置 OkHttpClient 超时时间
        val httpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)

        // 使用 InfluxDBClientOptions 配置
        val options = InfluxDBClientOptions.builder()
            .url(influxConfig.url)
            .authenticateToken(influxConfig.token.toCharArray())
            .org(influxConfig.org)
            .bucket(influxConfig.bucket)
            .okHttpClient(httpClientBuilder) // 自定义 OkHttpClient
            .build()

        // 创建 InfluxDBClient
        client = InfluxDBClientFactory.create(options)

        // 创建 WriteApi
        writeApiBlocking = client.writeApiBlocking
    }

    fun <T> writeData(data: T) {
        val writeApi: WriteApiBlocking = client.writeApiBlocking;
        writeApi.writeMeasurement(WritePrecision.NS, data)
    }

    fun <T> writeDataBatch(data: List<T>) {
        val writeApi: WriteApiBlocking = client.writeApiBlocking;
        writeApi.writeMeasurements(WritePrecision.NS, data)
    }

    fun <T> query(query: InfluxQueryBuilder, converter: (Map<String, Any>) -> T): List<T> {
        val build = query.build()
        log.info("Query: $build")
        val queryResult: List<FluxTable> = client.queryApi.query(build)
        return queryResult[0].records.map {
            converter(it.values)
        }
    }


    fun <T> query(query: String, converter: (Map<String, Any>) -> T): List<T> {
        log.info("Query: $query")
        val queryResult: List<FluxTable> = client.queryApi.query(query)
        return queryResult.map { converter(it.records[0].values) }
    }

    fun <T> query(query: String, resultClass: Class<T>): List<T> {
        log.info("Query: $query")
        val result: List<T> = client.queryApi.query(query, resultClass)
        return result
    }

    /**
     * 获取tick的数据，会group成map
     * @param mission 任务id
     * @param tickRange tick范围
     * @param timeLimit 时间范围
     * @param dbClass 数据库类
     * @param resultMap 数据转换方法
     */
    fun <T, R : ITickData> getTickData(
        mission: Long,
        tickRange: LongRange?,
        dbClass: Class<T>,
        resultMap: (T) -> R,
        timeLimit: Int = 30 * 6,
    ): Map<Long, List<R>> {
        // 记录起始时间
        val startTime = System.nanoTime()

        val builder = InfluxQueryBuilder("hex_bucket")
        builder.range(timeLimit, TimeUnit.DAYS)
        builder.filter("mission == \"$mission\"")
        if (tickRange != null) {
            builder.postFilter("r[\"tick\"] >= ${tickRange.first}", "r[\"tick\"] <= ${tickRange.last}")
        }
        builder.measurement(dbClass)
        val result = this.query(
            builder.build(true),
            dbClass
        ).map {
            resultMap.invoke(it)
        }.groupBy {
            it.tick
        }

        // 记录结束时间并计算耗时
        val endTime = System.nanoTime()
        val durationMillis = (endTime - startTime) / 1_000_000 // 转换为毫秒
        log.warn("getTickData method execution time: $durationMillis ms : class $dbClass")

        return result

    }

    //    fun getCarTickByMission(mission: Long, timeRange: Int = 20, tickRange: IntRange?): Map<Long, List<CarITickData>> {
//        val builder = InfluxQueryBuilder("hex_bucket")
//        builder.range(timeRange, TimeUnit.DAYS)
//        builder.filter("mission == \"$mission\"")
//        if (tickRange != null) {
//            builder.filter("tick >= ${tickRange.first}", "tick <= ${tickRange.last}")
//        }
//        builder.measurement(CarRealTimePO::class.java)
//        return this.query(
//            builder.build(true),
//            CarRealTimePO::class.java
//        ).map {
//            it.toCarTickData()
//        }.groupBy {
//            it.tick
//        }
//    }
//
//    fun getEquipmentTickByMission(
//        mission: Long,
//        timeRange: Int = 20,
//        tickRange: IntRange?
//    ): Map<Long, List<EquipmentITickData>> {
//        val builder = InfluxQueryBuilder("hex_bucket")
//        builder.range(timeRange, TimeUnit.DAYS)
//        builder.filter("mission == \"$mission\"")
//        if (tickRange != null) {
//            builder.filter("tick >= ${tickRange.first}", "tick <= ${tickRange.last}")
//        }
//        builder.measurement(EquipmentRealTimePO::class.java)
//        return this.query(
//            builder.build(true),
//            EquipmentRealTimePO::class.java
//        ).map {
//            it.toEquipmentTickData()
//        }.groupBy {
//            it.tick
//        }
//    }
    fun saveSimulateTick(
        currentTick: Long,
        missionId: Long,
        cars: List<Car>,
        equipment: List<Equipment>,
        position: Collection<PositionRealTime>,
        workstation: Collection<WorkstationRealTime>,
        path: Collection<Path>,
        product: Collection<Product>
    ) {
        val points = mutableListOf<Point>()

        cars.forEach { car ->
            val point = Point.measurement("car_real_time")
                .addField("tick", currentTick)
                .addTag("id", car.id.toString())
                .addTag("mission", missionId.toString())
                .addTag("type", car.meta.type.name)
                .addField("name", car.meta.name)
                .addField("area", car.meta.nameEnum.area.name)
                .addField("currentCoordinate", car.realTime.currentCoordinate.scaleConversionToString())
                .addField("targetCoordinate", car.realTime.targetCoordinate.scaleConversionToString())
                .addField("operationStatus", car.realTime.operationStatus.name)
                .addField("currentAction", car.realTime.currentAction)
//                .addField("locationStatus", car.realTime.locationStatus.name)
//                .addField("connectStatus", car.realTime.connectStatus.name)
            points.add(point)
        }

        equipment.forEach { eq ->
            val point = Point.measurement("equipment_real_time")
                .addField("tick", currentTick)
                .addTag("id", eq.id.toString())
                .addTag("mission", missionId.toString())
                .addField("name", eq.meta.name)
                .addField("area", eq.meta.nameEnum.area.name)
                .addField("operationStatus", eq.realTime.operationStatus.name)
                .addField("currentAction", eq.realTime.currentAction)
                .addField("moltenIronQuantity", eq.realTime.moltenIronQuantity)
            points.add(point)
        }

        position.forEach { pos ->
            val point = Point.measurement("position_real_time")
                .addField("tick", currentTick)
                .addTag("id", pos.id.toString())
                .addTag("mission", missionId.toString())
                .addField("status", pos.status.name)
                .addField("currentAction", pos.currentAction)
            points.add(point)
        }

        workstation.forEach { ws ->
            val point = Point.measurement("workstation_real_time")
                .addField("tick", currentTick)
                .addTag("id", ws.id.toString())
                .addTag("mission", missionId.toString())
                .addField("name", ws.name)
                .addField("area", ws.nameEnum.area.name)
                .addField("status", ws.status.name)
                .addField("currentAction", ws.currentAction)
            points.add(point)
        }

        path.forEach { p ->
            val point = Point.measurement("path_real_time")
                .addField("tick", currentTick)
                .addTag("id", p.id.toString())
                .addTag("mission", missionId.toString())
                .addField("name", p.name)
                .addField("length", p.length)
                .addField("startCoordinate", p.startCoordinate.scaleConversionToString())
                .addField("endCoordinate", p.endCoordinate.scaleConversionToString())
                .addField("direction", p.direction.name)
                .addField("status", p.status.name)
            points.add(point)
        }

        product.forEach { p ->
            val point = Point.measurement("product_real_time")
                .addField("tick", currentTick)
                .addTag("id", p.id.toString())
                .addTag("mission", missionId.toString())
                .addField("name", p.name)
                .addField("productType", p.productType.name)
                .addField("productAfoot", p.productAfoot.name)
            points.add(point)
        }
        // 调用InfluxDBClient的makeWriteApi()方法，将数据异步写入到InfluxDB中
        writeApiBlocking.writePoints(points)
    }

    /**
     * 根据mission和tick删除数据
     */
    fun deleteDataByMissionAndTick(mission: Long, maxTick: Long) {
        // 每次处理 200 个 tick
        val batchSize = 200L
        // 删除时间范围为过去30天
        val startTime = OffsetDateTime.now().minusDays(30)
        val endTime = OffsetDateTime.now()
        var startTick = 0L

        while (startTick <= maxTick) {
            val endTick = if (startTick + batchSize - 1 > maxTick) maxTick else startTick + batchSize - 1
            // 构建删除谓词，按照 tick 范围删除数据
            val predicateRequest = DeletePredicateRequest()
            predicateRequest.start = startTime
            predicateRequest.stop = endTime
            predicateRequest.predicate = "mission = \"$mission\" AND tick >= $startTick AND tick <= $endTick"
            try {
                // 执行删除操作
                client.deleteApi.delete(predicateRequest, influxConfig.bucket, influxConfig.org)
                log.info("Deleted data for mission: $mission and tick range: $startTick to $endTick")
            } catch (e: Exception) {
                log.error("Failed to delete data for mission: $mission and tick range: $startTick to $endTick", e)
            }

            // 更新下一个 tick 范围的起始值
            startTick += batchSize
        }
    }

    /**
     * 根据mission删除数据
     * @param [mission] 任务ID
     */
    fun deleteDataByMission(mission: Long) {
        // 删除时间范围为过去30天
        val startTime = OffsetDateTime.now().minusDays(30)
        val endTime = OffsetDateTime.now()

        // 构建删除谓词，按照 tick 范围删除数据
        val predicateRequest = DeletePredicateRequest()
        predicateRequest.start = startTime
        predicateRequest.stop = endTime
        predicateRequest.predicate = "mission = \"$mission\""
        try {
            // 执行删除操作
            client.deleteApi.delete(predicateRequest, influxConfig.bucket, influxConfig.org)
            log.info("Deleted data for mission: $mission")
        } catch (e: Exception) {
            log.error("Failed to delete data for mission: $mission", e)
        }
    }

}
