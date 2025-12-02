package cn.hex.ddp.manufacture.infrastructure.common.handlers;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.List;

/**
 * 自定义TypeHandler，用于序列化List<Long>到JSON字符串，反序列化JSON字符串到List<Long>
 *
 * @author Huhaisen
 * @date 2024/06/14
 */
public class JsonListLongTypeHandler extends BaseTypeHandler<List<Long>> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, toJson(parameter), Types.OTHER);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toList(rs.getString(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toList(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toList(cs.getString(columnIndex));
    }

    private String toJson(List<Long> parameter) {
        try {
            return OBJECT_MAPPER.writeValueAsString(parameter);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BssExType.JSON_PROCESSING_ERROR, "List<Long> 转为 JSON 字符串失败");
        }
    }

    private List<Long> toList(String content) {
        try {
            return OBJECT_MAPPER.readValue(content, OBJECT_MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, Long.class));
        } catch (JsonProcessingException e) {
            throw new BusinessException(BssExType.JSON_PROCESSING_ERROR, "JSON 字符串转为 List<Long> 失败");
        }
    }
}

