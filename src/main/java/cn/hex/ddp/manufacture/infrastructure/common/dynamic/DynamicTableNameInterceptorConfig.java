package cn.hex.ddp.manufacture.infrastructure.common.dynamic;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * 动态表名拦截器配置
 * 注意：由于MybatisPlusInterceptor是以Bean的形式注入到Spring容器中的，只能出现一次，因此分页插件也需要在这里配置。
 *
 * @author Huhaisen
 * @date 2024/12/26
 */
@Slf4j
@Configuration
public class DynamicTableNameInterceptorConfig {

    private final DynamicTableManager dynamicTableManager;

    // 使用构造器注入 TableManager
    public DynamicTableNameInterceptorConfig(DynamicTableManager dynamicTableManager) {
        this.dynamicTableManager = dynamicTableManager;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 动态表名插件
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            // 定义需要动态分表的表名集合。注意，这里传入的tableName带schema，例如：ddp_simulation_service.car_real_time
            List<String> dynamicTables = Arrays.asList(
                    "ddp_simulation_service.car_simulator_data",
                    "ddp_simulation_service.equipment_simulator_data",
                    "ddp_simulation_service.position_simulator_data",
                    "ddp_simulation_service.product_simulator_data",
                    "ddp_simulation_service.workstation_simulator_data",
                    "ddp_simulation_service.path_simulator_data"
            );

            // 如果表名在动态表集合中，动态生成表名
            if (dynamicTables.contains(tableName)) {
                Long missionId = DynamicTableNameContext.getMissionId(); // 获取当前线程的 missionId
                if (missionId != null) {
                    String dynamicTableName = tableName + "_" + missionId;
                    dynamicTableManager.ensureTableExists(dynamicTableName, tableName); // 确保表存在
                    return dynamicTableName;
                } else {
                    log.error("[DynamicTableNameInterceptorConfig] 生成动态表时候 missionId 不能为空，表名：{}", tableName);
                    throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR,
                            "生成动态表时候 missionId 不能为空，表名：" + tableName);
                }
            }

            // 如果表不需要动态分表，则直接返回原始表名
            return tableName;
        });

        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }
}

