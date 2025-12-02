package cn.hex.ddp.manufacture.infrastructure.influx.repository

import com.influxdb.annotations.Measurement
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*


class InfluxQueryBuilder(private val bucket: String) {

    private lateinit var start: String;
    private lateinit var stop: String;
    private var filters = mutableListOf<String>();
    private var postFilters = mutableListOf<String>()
    private var dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

    private fun localDateTime2String(time: LocalDateTime): String {
        return dtf.format(time)
    }

    fun range(start: LocalDateTime, end: LocalDateTime): InfluxQueryBuilder {
        this.start = localDateTime2String(start)
        this.stop = localDateTime2String(end)
        return this
    }

    fun range(start: Int, end: Int, unit: TimeUnit): InfluxQueryBuilder {
        if (start < 0 || end < 0) throw IllegalArgumentException("Invalid time range")
        if (start < end) throw IllegalArgumentException("Invalid time range")
        val timeSuffix = unit2Suffix(unit)
        this.start = "-$start$timeSuffix"
        this.stop = "-$end$timeSuffix"
        return this
    }

    fun range(start: Int, unit: TimeUnit): InfluxQueryBuilder {
        if (start < 0) throw IllegalArgumentException("Invalid time range")
        val timeSuffix = unit2Suffix(unit)
        this.start = "-$start$timeSuffix"
        this.stop = localDateTime2String(LocalDateTime.now())
        return this
    }

    fun filter(vararg query: String): InfluxQueryBuilder {
        filters.addAll(query.map { "r.$it" })
        return this
    }

    fun postFilter(vararg query: String): InfluxQueryBuilder {
        postFilters.addAll(query)
        return this
    }


    fun measurement(measurementClass: Class<*>): InfluxQueryBuilder {
        val annotation = measurementClass.getAnnotation(Measurement::class.java)
            ?: throw IllegalArgumentException("Invalid measurement class")
        filters.add("r._measurement == \"${annotation.name}\"")
        return this
    }


    fun build(pivot: Boolean = true): String {
        val query = StringBuilder()
        query.append("from(bucket:\"$bucket\")")
        query.append("|> range(start: $start, stop: $stop)")
        query.append("|> filter(fn: (r) =>")
        query.append(filters.joinToString(separator = " and "));
        query.append(")")
        if (pivot) {
            query.append("|> pivot(rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")")
        }
        query.append("|> filter(fn: (r) =>")
        query.append(postFilters.joinToString(separator = " and "));
        query.append(")")
        return query.toString()
    }


    private fun unit2Suffix(unit: TimeUnit): String {
        return when (unit) {
            NANOSECONDS -> "ns"
            MICROSECONDS -> "us"
            MILLISECONDS -> "ms"
            SECONDS -> "s"
            MINUTES -> "m"
            HOURS -> "h"
            DAYS -> "d"
            else -> throw IllegalArgumentException("Invalid time unit")
        }
    }

}

