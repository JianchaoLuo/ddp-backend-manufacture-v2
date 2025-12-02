package cn.hex.ddp.manufacture.infrastructure.algorithm.test.kh;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/27 18:45
 */
@Mapper(mappingControl = DeepClone.class)
public interface Converter {
    Converter INSTANCE= Mappers.getMapper(Converter.class);

    SimulateInput toSimulateInput(SimulateInput simulateInput);
}
