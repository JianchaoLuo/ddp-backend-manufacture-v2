package cn.hex.ddp.manufacture.infrastructure.algorithm.test.yongkang;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation_2.WholeSimulation_2;
import cn.hex.ddp.manufacture.infrastructure.algorithm.test.DataTest;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/23 21:39
 */
@Slf4j
public class test {
    public static void main(String[] args) {
        DataTest dataTest = new DataTest();
        SimulateInput simulateInput = dataTest.createSimulateInput();

        WholeSimulation_2 wholeSimulation = new WholeSimulation_2();
        SimulateResult result = wholeSimulation.simulationRun(simulateInput);
        log.info("finish");
//        log.info("" + result.getGanttCharts());
    }
}
