package cn.hex.ddp.manufacture.infrastructure.configuration.managerimpl.converter;

import cn.hex.ddp.manufacture.api.configuration.rest.req.AddCoordinateReq;
import cn.hex.ddp.manufacture.api.configuration.rest.req.AddPositionReq;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdateCoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdatePositionDTO;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.configuration.model.Position;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po.CoordinatePO;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po.PositionPO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 全局配置数据转换器
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
@Mapper(componentModel = "spring")
public interface ConfigurationInfraConverter {
    Collection<CoordinatePO> toCoordinatePOList(List<AddCoordinateReq> coordinateReqList);

    Coordinate toCoordinate(CoordinatePO coordinatePO);

    List<Coordinate> toCoordinateList(List<CoordinatePO> coordinatePOList);

    Page<Coordinate> toCoordinatePage(Page<CoordinatePO> poPage);

    CoordinatePO toCoordinatePO(UpdateCoordinateDTO updateCoordinateDTO);

    CoordinatePO toCoordinatePO(Coordinate coordinate);

    PositionPO toPositionPO(AddPositionReq addPositionReq);

    PositionPO toPositionPO(UpdatePositionDTO updatePositionDTO);

    Position toPosition(PositionPO positionPO);

    List<Position> toPositionList(List<PositionPO> positionPOList);

    Page<Position> toPositionPage(Page<PositionPO> poPage);
}
