package cn.hex.ddp.manufacture.infrastructure.algorithm;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.WholeSimulation;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.simulation_v2.WholeSimulation_3;
import cn.hex.ddp.manufacture.infrastructure.algorithm.test.DataTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlgorithmEndpoint {
    /**
     * 获取算法仿真结果
     *
     * @param input
     * @return
     */
    public static SimulateResult preDoSimulate(SimulateInput input) {
        //测试数据
        DataTest dataTest = new DataTest();
        SimulateInput simulateInput = dataTest.createSimulateInput();

        //正常逻辑
        WholeSimulation_3 wholeSimulation_3 = new WholeSimulation_3();

        log.info("==================================================================================================================================================");
        for (int i = 0; i < input.getOrderInputs().size(); i++) {
            for (int j = 0; j < input.getOrderInputs().get(i).getOrderProducts().size(); j++) {
                for (int k = 0; k < input.getOrderInputs().get(i).getOrderProducts().get(j).getProducts().size(); k++) {
                    log.info("{}",input.getOrderInputs().get(i).getOrderProducts().get(j).getProducts().get(k));
                }
                log.info("{}",input.getOrderInputs().get(i).getOrderProducts().get(j));
            }
        }
        log.info("==================================================================================================================================================");

//        input.setOrderInputs(simulateInput.getOrderInputs());

//        log.info("==================================================================================================================================================");
//        log.info(simulateResult.getOrderTimes());
//        log.info("==================================================================================================================================================");

        // 初步仿真，获取仿真结果
        // todo @康弘
        return wholeSimulation_3.simulationRun(input);
    }

    public static DeployThread startDeployThread(SimulateResult simulateResult) {
        // 开启一个线程，实时与模拟器线程通信
        // todo @康弘
        DeployThread deployThread = new DeployThread();
        deployThread.setSimulateResult(simulateResult);
        deployThread.start();
        return deployThread;
    }


}

