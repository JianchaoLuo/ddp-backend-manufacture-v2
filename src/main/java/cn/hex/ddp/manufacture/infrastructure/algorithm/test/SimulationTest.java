package cn.hex.ddp.manufacture.infrastructure.algorithm.test;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.simulation_v2.WholeSimulation_3;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/7 23:38
 */
@Slf4j
public class SimulationTest {
    public static void main(String[] args) {
        DataTest dataTest = new DataTest();
        SimulateInput simulateInput = dataTest.createSimulateInput();
//        Long start=System.nanoTime();
//        ArrayList<Object> objects = new ArrayList<>(1000);
//        for (int i = 0; i < 1000; i++) {
//            SimulateInput simulateInput1 = Converter.INSTANCE.toSimulateInput(simulateInput);
//            objects.add(simulateInput1);
//        }
//        SimulateInput simulateInput1 = Converter.INSTANCE.toSimulateInput(simulateInput);
//        System.out.println("mapstruct");
//        System.out.println(System.nanoTime()-start);
//        Long start1 = System.nanoTime();
//        ArrayList<Object> objects1 = new ArrayList<>(1000);
//        for (int i = 0; i < 1000; i++) {
//            SimulateInput clone2 = SerializationUtils.clone(simulateInput);
//            objects1.add(clone2);
//        }
//        System.out.println("serilize");
//        System.out.println(System.nanoTime()-start1);

//        System.out.println(objects.size()+objects1.size());


        WholeSimulation_3 wholeSimulation = new WholeSimulation_3();
        SimulateResult result = wholeSimulation.simulationRun(simulateInput);
        log.info("finish");
        log.info("" + result.getGanttCharts());
    }
}
