package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling;

import cn.hex.ddp.manufacture.domain.task.model.Task;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingOutPut;
import cn.hex.ddp.manufacture.infrastructure.algorithm.test.kh.SchedulingDataMoke;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.postgresql.repository.TaskPORepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TestProductionScheduling {

//    @Test
//    void testPreDoSchedulingCalculateProductTime(){
//        SchedulingDataMoke schedulingDataMoke = new SchedulingDataMoke();
//
//        SimulateInput simulateInput = schedulingDataMoke.createSimulateInput();
//        ScheduleOrderInput scheduleOrderInput = schedulingDataMoke.createScheduleOrderInput();
//
//        SchedulingOutPut schedulingOutPut = ProductionScheduling.preDoScheduling(simulateInput, scheduleOrderInput);
//        if (schedulingOutPut != null) {
//            log.warn(schedulingOutPut.toString());
//        }
//    }


}
