package cn.hex.ddp.manufacture.infrastructure.common.handlers;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.technique.model.FormulaDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.List;

/**
 * 自定义类型处理器，处理JSON格式的List<FormulaDetail>类型
 *
 * @author Huhaisen
 * @date 2024/09/26
 */
public class JsonListFormulaDetailTypeHandler extends BaseTypeHandler<List<FormulaDetail>> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<FormulaDetail> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, toJson(parameter), Types.OTHER);
    }

    @Override
    public List<FormulaDetail> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toList(rs.getString(columnName));
    }

    @Override
    public List<FormulaDetail> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toList(rs.getString(columnIndex));
    }

    @Override
    public List<FormulaDetail> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toList(cs.getString(columnIndex));
    }

    private String toJson(List<FormulaDetail> parameter) {
        try {
            return OBJECT_MAPPER.writeValueAsString(parameter);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BssExType.JSON_PROCESSING_ERROR, "List<FormulaDetail> 转为 JSON 字符串失败");
        }
    }

    private List<FormulaDetail> toList(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, FormulaDetail.class));
        } catch (JsonProcessingException e) {
            throw new BusinessException(BssExType.JSON_PROCESSING_ERROR, "JSON 字符串转为 List<FormulaDetail> 失败");
        }
    }
}
