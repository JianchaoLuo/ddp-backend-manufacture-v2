package cn.hex.ddp.manufacture;

import cn.hex.ddp.manufacture.application.simulator.SimulateService;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.postgresql.repository.CoordinatePORepository;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.postgresql.repository.PositionPORepository;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.postgresql.repository.EquipmentPORepository;
import cn.hex.ddp.manufacture.infrastructure.path.persistence.postgresql.repository.PathPORepository;
import cn.hex.ddp.manufacture.infrastructure.workstation.persistence.postgresql.repository.WorkstationPORepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DdpBackendManufactureV2ApplicationTests {

    @Autowired
    private SimulateService simulateService;
    @Autowired
    private EquipmentPORepository equipmentPORepository;
    @Autowired
    private CoordinatePORepository coordinatePORepository;
    private static Long no = 1L;
    @Autowired
    private PositionPORepository positionPORepository;
    @Autowired
    private WorkstationPORepository workstationPORepository;
    @Autowired
    private PathPORepository pathPORepository;
    @Test
    public void TestTimer() {

    }


}
