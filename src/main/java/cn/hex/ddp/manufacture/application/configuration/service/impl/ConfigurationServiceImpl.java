package cn.hex.ddp.manufacture.application.configuration.service.impl;

import cn.hex.ddp.manufacture.api.configuration.rest.req.*;
import cn.hex.ddp.manufacture.api.configuration.rest.vo.NearPointsVO;
import cn.hex.ddp.manufacture.application.configuration.converter.ConfigurationAppConverter;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdateCoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdatePositionDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.CoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.PositionDTO;
import cn.hex.ddp.manufacture.application.configuration.service.ConfigurationService;
import cn.hex.ddp.manufacture.application.equipment.converter.EquipmentServiceConverter;
import cn.hex.ddp.manufacture.application.workstation.converter.WorkstationAppConverter;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.configuration.manager.ConfigurationManager;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.configuration.model.GlobalConfiguration;
import cn.hex.ddp.manufacture.domain.configuration.model.Position;
import cn.hex.ddp.manufacture.domain.equipment.manager.EquipmentManager;
import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import cn.hex.ddp.manufacture.domain.workstation.manager.WorkstationManager;
import cn.hex.ddp.manufacture.domain.workstation.model.Workstation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 工厂全局配置服务接口实现类
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
@Slf4j
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationManager configurationManager;

    @Autowired
    private WorkstationManager workstationManager;

    @Autowired
    private EquipmentManager equipmentManager;

    @Autowired
    private ConfigurationAppConverter configurationAppConverter;

    @Autowired
    private EquipmentServiceConverter equipmentServiceConverter;

    @Autowired
    private WorkstationAppConverter workstationAppConverter;

    @Override
    public CoordinateDTO addCoordinate(AddCoordinateReq addCoordinateReq) {
        // 判断坐标是否已存在
        Coordinate coordinate = configurationManager.getCoordinateByXAndY(
                addCoordinateReq.getX(), addCoordinateReq.getY());
        if (coordinate != null) {
            log.warn("[ConfigurationServiceImpl.addCoordinate] 坐标已存在, x: {}, y: {}", addCoordinateReq.getX(),
                    addCoordinateReq.getY());
            throw new BusinessException(BssExType.COORDINATE_REPEAT_ERROR, "x: " + addCoordinateReq.getX() + ", y: " +
                    addCoordinateReq.getY());
        }
        // 新增坐标并返回坐标信息
        configurationManager.addCoordinate(configurationAppConverter.toCoordinate(addCoordinateReq));
        Coordinate addCoordinate = configurationManager.getCoordinateByXAndY(
                addCoordinateReq.getX(), addCoordinateReq.getY());
        return configurationAppConverter.coordinateToDTO(addCoordinate);
    }

    @Override
    public void addCoordinateBatch(List<AddCoordinateReq> coordinateReqList) {
        ArrayList<String> errorInfos = new ArrayList<>();
        for (AddCoordinateReq addCoordinateReq : coordinateReqList) {
            Coordinate coordinate = configurationManager.getCoordinateByXAndY(
                    addCoordinateReq.getX(), addCoordinateReq.getY());
            if (coordinate != null) {
                errorInfos.add("(" + addCoordinateReq.getX() + ", " + addCoordinateReq.getY() + ")");
            }
        }
        if (!errorInfos.isEmpty()) {
            throw new BusinessException(BssExType.COORDINATE_REPEAT_ERROR, errorInfos.toString());
        }
        configurationManager.addCoordinateBatch(coordinateReqList);
    }

    @Override
    public CoordinateDTO getCoordinate(Long id) {
        Coordinate coordinate = configurationManager.getCoordinateById(id);
        if (coordinate == null) {
            log.warn("[ConfigurationServiceImpl.getCoordinate] 坐标不存在, id: {}", id);
            throw new NotFoundException("坐标不存在");
        }
        return configurationAppConverter.coordinateToDTO(coordinate);
    }

    @Override
    public List<CoordinateDTO> getCoordinateBatch(List<Long> ids) {
        List<Coordinate> coordinates = configurationManager.getCoordinateByIds(ids);
        return configurationAppConverter.coordinateListToDTOList(coordinates);
    }

    @Override
    public Page<CoordinateDTO> getCoordinatePage(GetCoordinatePageReq getCoordinatePageReq) {
        Page<Coordinate> page = configurationManager.getCoordinatePage(getCoordinatePageReq);
        return configurationAppConverter.coordinatePageToDTOPage(page);
    }

    @Override
    public void updateCoordinate(UpdateCoordinateDTO updateCoordinateDTO) {
        // 根据坐标id获取坐标，判断坐标是否存在
        Coordinate coordinate = configurationManager.getCoordinateById(updateCoordinateDTO.getId());
        if (coordinate == null) {
            log.warn("[ConfigurationServiceImpl.updateCoordinate] 坐标不存在, id: {}", updateCoordinateDTO.getId());
            throw new NotFoundException("坐标不存在");
        }
        // 根据x和y判断坐标是否重复
        Coordinate repeatCoordinate = configurationManager.getCoordinateByXAndY(
                updateCoordinateDTO.getX(), updateCoordinateDTO.getY());
        if (repeatCoordinate != null) {
            log.warn("[ConfigurationServiceImpl.updateCoordinate] 坐标重复, x: {}, y: {}", updateCoordinateDTO.getX(),
                    updateCoordinateDTO.getY());
            throw new BusinessException(BssExType.COORDINATE_REPEAT_ERROR, "x: " + updateCoordinateDTO.getX() + ", y: "
                    + updateCoordinateDTO.getY());
        }

        configurationManager.updateCoordinateById(updateCoordinateDTO);
    }

    @Override
    public void deleteCoordinateBatch(List<Long> ids) {
        configurationManager.deleteCoordinateBatch(ids);
    }

    @Override
    public void addPosition(AddPositionReq addPositionReq) {
        Coordinate coordinate = configurationManager.getCoordinateById(addPositionReq.getCoordinateId());
        if (coordinate == null) {
            log.warn("[ConfigurationServiceImpl.addPosition] 坐标不存在, id: {}", addPositionReq.getCoordinateId());
            throw new NotFoundException("坐标不存在");
        }
        Position positionByName = configurationManager.getPositionByName(addPositionReq.getName());
        if (positionByName != null) {
            log.warn("[ConfigurationServiceImpl.addPosition] 点位名称已存在, name: {}", addPositionReq.getName());
            throw new BusinessException(BssExType.POSITION_NAME_REPEAT_ERROR, "name: " + addPositionReq.getName());
        }

        configurationManager.addPosition(addPositionReq);
    }

    @Override
    public PositionDTO getPosition(Long id) {
        Position position = configurationManager.getPositionById(id);
        if (position == null) {
            log.warn("[ConfigurationServiceImpl.getPosition] 点位不存在, id: {}", id);
            throw new NotFoundException("点位不存在");
        }
        return configurationAppConverter.positionToDTO(position);
    }

    @Override
    public List<PositionDTO> getPositionBatch(List<Long> ids) {
        List<Position> positions = configurationManager.getPositionByIds(ids);
        return configurationAppConverter.positionListToDTOList(positions);
    }

    @Override
    public List<PositionDTO> getAllPosition() {
        List<Position> positions = configurationManager.getAllPosition();
        return configurationAppConverter.positionListToDTOList(positions);
    }

    @Override
    public Page<PositionDTO> getPositionPage(GetPositionPageReq getPositionPageReq) {
        Page<Position> page = configurationManager.getPositionPage(getPositionPageReq);
        return configurationAppConverter.positionPageToDTOPage(page);
    }

    @Override
    public void updatePosition(UpdatePositionDTO updatePositionDTO) {
        Position position = configurationManager.getPositionById(updatePositionDTO.getId());
        if (position == null) {
            log.warn("[ConfigurationServiceImpl.updatePosition] 点位不存在, id: {}", updatePositionDTO.getId());
            throw new NotFoundException("点位不存在");
        }
        Position positionByName = configurationManager.getPositionByName(updatePositionDTO.getName());
        if (positionByName != null && !positionByName.getId().equals(updatePositionDTO.getId())) {
            log.warn("[ConfigurationServiceImpl.updatePosition] 点位名称已存在, name: {}", updatePositionDTO.getName());
            throw new BusinessException(BssExType.POSITION_NAME_REPEAT_ERROR, "name: " + updatePositionDTO.getName());
        }

        if (updatePositionDTO.getCoordinateId() != null) {
            Coordinate coordinate = configurationManager.getCoordinateById(updatePositionDTO.getCoordinateId());
            if (coordinate == null) {
                log.warn("[ConfigurationServiceImpl.updatePosition] 坐标不存在, id: {}",
                        updatePositionDTO.getCoordinateId());
                throw new NotFoundException("坐标不存在");
            }
        }
        configurationManager.updatePositionById(updatePositionDTO);
    }

    @Override
    public void deletePositionBatch(List<Long> ids) {
        configurationManager.deletePositionByIds(ids);
    }

    @Override
    public void deletePosition(Long id) {
        Position position = configurationManager.getPositionById(id);
        if (position == null) {
            log.warn("[ConfigurationServiceImpl.deletePosition] 点位不存在, id: {}", id);
            throw new NotFoundException("点位不存在");
        }
        configurationManager.deletePositionById(id);
    }

    @Override
    public void addOrUpdateGlobalConfiguration(AddOrUpdateGlobalConfigurationReq addOrUpdateGlobalConfigurationReq) {
        configurationManager.addOrUpdateGlobalConfiguration(
                configurationAppConverter.toGlobalConfiguration(addOrUpdateGlobalConfigurationReq)
        );
    }

    @Override
    public GlobalConfiguration getGlobalConfiguration() {
        return configurationManager.getGlobalConfiguration();
    }

    @Override
    public NearPointsVO getNearByPoint(NearPointReq nearPointReq) {
        //查找工岗点位
        List<Workstation> nearWorkStations = workstationManager.getNearWorkStation(nearPointReq.getX(), nearPointReq.getY());

        //查找设备点位
        List<Equipment> nearEquipments = equipmentManager.getNearEquipment(nearPointReq.getX(), nearPointReq.getY());

        //查找交互点点位
        List<Position> nearPositions = configurationManager.getNearPosition(nearPointReq.getX(), nearPointReq.getY());

        //封装结果并返回
        return new NearPointsVO(
                nearWorkStations,
                nearEquipments,
                nearPositions
        );
    }
}
