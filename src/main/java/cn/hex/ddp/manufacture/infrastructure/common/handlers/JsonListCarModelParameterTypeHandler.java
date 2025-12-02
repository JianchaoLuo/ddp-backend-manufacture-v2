package cn.hex.ddp.manufacture.infrastructure.common.handlers;

import cn.hex.ddp.manufacture.domain.car.model.CarModelParameter;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.List;

/**
 * 自定义TypeHandler，用于序列化List<CarModelParameter>到JSON字符串，反序列化JSON字符串到List<CarModelParameter>
 *
 * @author Huhaisen
 * @date 2024/06/22
 */
public class JsonListCarModelParameterTypeHandler extends BaseTypeHandler<List<CarModelParameter>> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<CarModelParameter> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, toJson(parameter), Types.OTHER);
    }

    @Override
    public List<CarModelParameter> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toList(rs.getString(columnName));
    }

    @Override
    public List<CarModelParameter> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toList(rs.getString(columnIndex));
    }

    @Override
    public List<CarModelParameter> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toList(cs.getString(columnIndex));
    }

    private String toJson(List<CarModelParameter> parameter) {
        try {
            return OBJECT_MAPPER.writeValueAsString(parameter);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BssExType.JSON_PROCESSING_ERROR, "List<CarModelParameter> 转为 JSON 字符串失败");
        }
    }

    private List<CarModelParameter> toList(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, CarModelParameter.class));
        } catch (JsonProcessingException e) {
            throw new BusinessException(BssExType.JSON_PROCESSING_ERROR, "JSON 字符串转为 List<CarModelParameter> 失败");
        }
    }
}
