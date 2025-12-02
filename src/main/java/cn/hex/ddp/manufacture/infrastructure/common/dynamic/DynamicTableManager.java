package cn.hex.ddp.manufacture.infrastructure.common.dynamic;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 动态表名管理类
 * <p>
 * 本类用于在 PostgreSQL 数据库中动态管理表的创建和检查逻辑。
 * 它通过查询数据库中的元数据，动态生成建表 SQL，并在目标表不存在时，
 * 根据基准表的结构和元信息（包括字段、索引和字段描述）来创建新表。
 * </p>
 *
 * 通过 `@DS("simulation")` 注解指定使用仿真数据源。
 */
@Component
@DS("simulation")
public class DynamicTableManager {

    /**
     * JdbcTemplate 是 Spring 提供的用于简化数据库操作的工具类，
     * 本类通过 JdbcTemplate 与数据库进行交互，包括查询和执行 SQL。
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 确保目标表在数据库中存在。如果目标表不存在，则根据基准表的结构
     * 动态生成建表 SQL 并执行创建表操作。
     *
     * @param tableName     目标表的名称，格式为 "schema.tableName"。
     * @param baseTableName 基准表的名称，格式为 "schema.baseTableName"，用于获取基准表的结构信息。
     *
     * @throws IllegalArgumentException 如果目标表名或基准表名格式不正确，抛出该异常。
     */
    public void ensureTableExists(String tableName, String baseTableName) {
        // 解析目标表名，提取 schema 和 table 名称
        String[] tableParts = tableName.split("\\.");
        if (tableParts.length != 2) {
            throw new IllegalArgumentException("Invalid table name format. Expected 'schema.tableName'.");
        }
        String schema = tableParts[0];
        String tableNameWithoutSchema = tableParts[1];

        // 检查目标表是否存在
        String checkTableSql = String.format(
                "SELECT EXISTS ("
                        + "SELECT 1 FROM information_schema.tables "
                        + "WHERE table_schema = '%s' AND table_name = '%s' AND table_type = 'BASE TABLE'"
                        + ")",
                schema, tableNameWithoutSchema
        );

        // 执行查询，判断表是否存在
        Boolean exists = jdbcTemplate.queryForObject(checkTableSql, Boolean.class);

        // 如果表不存在，则根据基准表生成建表 SQL 并执行
        if (Boolean.FALSE.equals(exists)) {
            String createTableSql = getCreateTableSql(tableName, baseTableName);
            if (createTableSql != null) {
                // 执行生成的建表 SQL
                jdbcTemplate.execute(createTableSql);
            } else {
                throw new IllegalArgumentException("Failed to generate SQL for table creation from base table: " + baseTableName);
            }
        }
    }

    /**
     * 根据基础表生成建表 SQL
     * @param baseTableName 基础表名，格式为 schema.base_table_name
     * @param tableName 新表的表名，格式为 schema.table_name
     * @return 创建新表的 SQL 语句
     */
    private String getCreateTableSql(String tableName, String baseTableName) {
        // 使用 PostgreSQL 的 CREATE TABLE LIKE 语法
        return String.format(
                "CREATE TABLE %s (LIKE %s INCLUDING ALL);",
                tableName, baseTableName
        );
    }
}
