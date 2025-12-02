//package cn.hex.ddp.manufacture.infrastructure.simulator;
//
//import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
//import cn.hex.ddp.manufacture.domain.simulator.manager.EquipmentSimulatorManager;
//import cn.hex.ddp.manufacture.domain.simulator.model.EquipmentSimulatorData;
//import cn.hex.ddp.manufacture.domain.simulator.model.EquipmentVaryData;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//
///**
// * 设备仿真数据测试
// * @Author: FengZebang
// * @Date: 2025/3/10
// */
//@Slf4j
//@SpringBootTest
//public class EquipmentSimulatorDataTest {
//    @Autowired
//    private EquipmentSimulatorManager equipmentSimulatorManager;
//
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    void saveEquipmentSimulatorData() {
//        //构造数据
//        List<EquipmentVaryData> equipmentVaryDataData = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            EquipmentVaryData equipmentVaryData = new EquipmentVaryData();
//            equipmentVaryData.setId("1");
//            equipmentVaryData.setOperationStatus(EquipmentOperationStatusEnum.WORKING);
//            equipmentVaryData.setCurrentAction("1");
//        }
//
//        int threadNum = 20;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//        for (int i = 1; i <=threadNum; i++) {
//            //提交任务使用了lambda,注意捕获变量的规则
//            int finalI = i;
//            executorService.execute(() -> {
//                try {
//                    long startall = System.currentTimeMillis();
//                    for (int j = 0; j < 50; j++) {
//                        EquipmentSimulatorData equipmentSimulatorData = new EquipmentSimulatorData();
//                        equipmentSimulatorData.setMission(Long.valueOf(finalI));
//                        equipmentSimulatorData.setTick(Long.valueOf(j));
//                        equipmentSimulatorData.setData(objectMapper.writeValueAsString(equipmentVaryDataData));
//                        long start = System.currentTimeMillis();
//                        equipmentSimulatorManager.saveEquipmentRealTime(equipmentSimulatorData);
//                        long end = System.currentTimeMillis();
//                        System.out.println("第" + j + "次保存设备仿真数据耗时：" + (end - start) + "ms");
//                    }
//
//                    long endAll = System.currentTimeMillis();
//                    System.out.println("保存设备仿真数据总耗时：" + (endAll - startall) + "ms");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        executorService.shutdown();
//        while (!executorService.isTerminated()) {
//
//        }
//    }
//
//    @Test
//    public void getEquipmentRealTimeTest() {
//        int threadNum = 20;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//        for(int i = 1; i <= threadNum; i++){
//            int finalI = i;
//            executorService.execute(() -> {
//                try {
//                    long startall = System.currentTimeMillis();
//                    long tickstart=1L;
//                    long tickEnd=20L;
//                    long tickBatch=5L;
//                    while (tickstart <= tickEnd){
//                        long start =System.currentTimeMillis();
//                        List<EquipmentSimulatorData> equipmentSimulatorDataList = equipmentSimulatorManager.getEquipmentRealTimes(Long.valueOf(finalI), tickstart, tickstart + tickBatch);
//                        long end = System.currentTimeMillis();
//                        System.out.println("线程："+finalI+"查询设备仿真数据耗时：" + (end - start) + "ms");
//                        tickstart += tickBatch;
//                    }
//                    long endAll = System.currentTimeMillis();
//                    System.out.println("查询设备仿真数据总耗时：" + (endAll - startall) + "ms");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        executorService.shutdown();
//        while (!executorService.isTerminated()) {
//
//        }
//    }
//}
