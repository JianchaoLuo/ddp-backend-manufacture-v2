package cn.hex.ddp.manufacture.infrastructure;

import cn.hex.ddp.manufacture.domain.car.manager.CarManager;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarPathPO;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.postgresql.repository.CarPathPORepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 冯泽邦
 * @date 2025/10/20
 */
@SpringBootTest
public class Cartest {

    @Autowired
    private CarManager carManager;
    @Autowired
    CarPathPORepository carPathPORepository;

    @Test
    public void updateCarPath(){
        List<CarPathPO> newcarpath = new ArrayList<>();
        newcarpath.add(new CarPathPO( 1820801092161695745L ,1820787401567563777L));
        newcarpath.add(new CarPathPO( 1820801443195580418L ,1820787402515476481L));
        newcarpath.add(new CarPathPO( 1876526342100078594L ,1875563668201656322L));
        newcarpath.add(new CarPathPO( 2L ,1796862784780828673L));
        newcarpath.add(new CarPathPO( 1L ,1796862786970255361L));
        newcarpath.add(new CarPathPO( 4L ,1796862786970255361L));
        carPathPORepository.saveBatch(newcarpath);
    }
}
