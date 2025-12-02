package cn.hex.ddp.manufacture.application.configuration.converter;

import cn.hex.ddp.manufacture.api.configuration.rest.req.AddCoordinateReq;
import cn.hex.ddp.manufacture.api.configuration.rest.req.AddOrUpdateGlobalConfigurationReq;
import cn.hex.ddp.manufacture.application.configuration.dto.out.CoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.PositionDTO;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.configuration.model.GlobalConfiguration;
import cn.hex.ddp.manufacture.domain.configuration.model.Position;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 全局配置数据转换器
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
@Mapper(componentModel = "spring")
public interface ConfigurationAppConverter {
    CoordinateDTO coordinateToDTO(Coordinate coordinate);

    List<CoordinateDTO> coordinateListToDTOList(List<Coordinate> coordinates);

    Page<CoordinateDTO> coordinatePageToDTOPage(Page<Coordinate> page);

    PositionDTO positionToDTO(Position position);

    List<PositionDTO> positionListToDTOList(List<Position> positions);

    Page<PositionDTO> positionPageToDTOPage(Page<Position> page);

    GlobalConfiguration toGlobalConfiguration(AddOrUpdateGlobalConfigurationReq addOrUpdateGlobalConfigurationReq);

    Coordinate toCoordinate(AddCoordinateReq addCoordinateReq);
}
