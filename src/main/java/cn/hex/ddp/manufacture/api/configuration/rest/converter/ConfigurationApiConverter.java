package cn.hex.ddp.manufacture.api.configuration.rest.converter;

import cn.hex.ddp.manufacture.api.configuration.rest.req.UpdateCoordinateReq;
import cn.hex.ddp.manufacture.api.configuration.rest.req.UpdatePositionReq;
import cn.hex.ddp.manufacture.api.configuration.rest.vo.CoordinateVO;
import cn.hex.ddp.manufacture.api.configuration.rest.vo.GlobalConfigurationVO;
import cn.hex.ddp.manufacture.api.configuration.rest.vo.PositionVO;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdateCoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdatePositionDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.CoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.PositionDTO;
import cn.hex.ddp.manufacture.domain.configuration.model.GlobalConfiguration;
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
public interface ConfigurationApiConverter {
    CoordinateVO toCoordinateVO(CoordinateDTO coordinateDTO);

    List<CoordinateVO> toCoordinateVOList(List<CoordinateDTO> coordinateDTOList);

    Page<CoordinateVO> toCoordinateVOPage(Page<CoordinateDTO> coordinateDTOPage);

    UpdateCoordinateDTO toUpdateCoordinateDTO(Long id, UpdateCoordinateReq updateCoordinateReq);

    PositionVO toPositionVO(PositionDTO positionDTO);

    List<PositionVO> toPositionVOList(List<PositionDTO> positionDTOList);

    Page<PositionVO> toPositionVOPage(Page<PositionDTO> positionDTOPage);

    UpdatePositionDTO toUpdatePositionDTO(Long id, UpdatePositionReq updatePositionReq);

    GlobalConfigurationVO toGlobalConfigurationVO(GlobalConfiguration globalConfiguration);
}
