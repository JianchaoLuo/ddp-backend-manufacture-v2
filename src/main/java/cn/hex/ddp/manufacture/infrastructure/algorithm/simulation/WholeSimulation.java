package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.simulation_v2.MouldingSimulation_2;
import cn.hex.ddp.manufacture.infrastructure.algorithm.test.OtherSimulation_2;
import cn.hex.ddp.manufacture.infrastructure.algorithm.test.OutBoundANDMoldClosing;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.CoolingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.ImitateCoolingPosition;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.MouldingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.OutBoundMouldingAndClosingBox;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox.OpenBoxGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring.PouringGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.AfterClosingBoxAllLink;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.WaitingPourGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/1 15:01
 */
@Data
@Slf4j
public class WholeSimulation {
    /**
     * 仿真处理合箱区之前输入的工具类
     */
    private HandleInput handleInput = new HandleInput();
    /**
     * 仿真处理浇筑区之后输入的工具类
     */
    private PourHandleInput pourHandleInput = new PourHandleInput();
    /**
     * 仿真工具类
     */
    private SimulationTool allTool = new SimulationTool();
    /**
     * 仿真总时间
     */
    private double totalTime = 0.0;
    /**
     * 仿真的进度时间
     */
    private double dTime = 10.0;
    /**
     * 事件最小
     */
    private double eventMinTime = 0.0;
    /**
     * 待浇筑区是否放置完砂箱，可以开始浇筑的标识
     * false表示不可以开始浇筑，true表示可以
     * 目前暂时用当订单中所有需要生成的产品全部到达待浇筑区时，可以准备开始浇筑
     */
    private boolean waitingPourFlag = false;
    /**
     * 待浇筑区的产品计数，用于判断是否达到所有需要的产品数目
     */
    private int waitingPourCount = 0;
    /**
     * 铸件计数判断
     */
    private int castingCount = 0;
    /**
     * 仿真结束判断，用于判断何时结束仿真
     */
    private int finishedCount = 0;
    /**
     * 订单数量列表
     */
    private Map<Long, Integer> orderNums = new HashMap<>();
    /**
     * 判断最小时间的列表
     */
    private Double[] timeList = new Double[160];
    /**
     * 浇筑铁水用量
     */
    private List<Double> usageDoubleList = new ArrayList<>();

    /**
     * 测试的模拟其他区域类
     */
    private OtherSimulation_2 otherSimulation_2 = new OtherSimulation_2();

    public SimulateResult simulationRun(SimulateInput input){
        //创建一个甘特图列表
        List<GanttChart> charts = new ArrayList<>();
        //创建出库区的仿真
        OutBoundSimulation outBoundSimulation = new OutBoundSimulation();
        Deque<Product> productDeque = new LinkedList<>();
        //生成一个出库队列
        productDeque = handleInput.handleDeque(input, usageDoubleList);
        //创建合模区的仿真
        MoldClosingSimulation moldClosingSimulation = new MoldClosingSimulation();
        //创建造型线的仿真
        MouldingSimulation_2 mouldingSimulation_2 = new MouldingSimulation_2();
        //创建合箱区的仿真
        ClosingBoxSimulation closingBoxSimulation = new ClosingBoxSimulation();
        //创建待浇筑区的仿真
        WaitingPourSimulation waitingPourSimulation = new WaitingPourSimulation();
        //创建浇筑区的仿真
        PouringSimulation pouringSimulation = new PouringSimulation();
        //创建冷却区的仿真
        CoolingSimulation coolingSimulation = new CoolingSimulation();
        //创建开箱区的仿真
        OpenBoxSimulation openBoxSimulation = new OpenBoxSimulation();

        //初始化出库区的仿真输入
        OutBoundGanttChartLinkInput outBoundGanttChartLinkInput = handleInput.handleOutBoundInput(input, totalTime, productDeque);
        OutBoundGanttChartLinkInput outBoundOutPut;

        //初始化合模区的仿真输入
        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = handleInput.handleMoldClosingInput(input, totalTime);
        MoldClosingGanttChartLinkInput moldClosingOutPut;

        //初始化造型线的仿真输入
        MouldingGanttChartLinkInput mouldingGanttChartLinkInput = handleInput.handleMouldingInput(input, totalTime);
        MouldingGanttChartLinkInput mouldingOutPut;

        //初始化合箱区的仿真输入
        ClosingBoxGanttChartLinkInput closingBoxGanttChartLinkInput = handleInput.handleClosingBoxInput(input, totalTime);
        ClosingBoxGanttChartLinkInput closingBoxOutPut;

        //初始化待浇筑区的仿真输入
        WaitingPourGanttChartLinkInput waitingPourGanttChartLinkInput = pourHandleInput.handleWaitingPourInput(input, totalTime);
        WaitingPourGanttChartLinkInput waitingPourOutPut;

        //初始化浇筑区的仿真输入
        PouringGanttChartLinkInput pouringGanttChartLinkInput = pourHandleInput.handlePouringInput(input, totalTime);
        PouringGanttChartLinkInput pouringOutPut;

        //初始化冷却区的仿真输入
        CoolingGanttChartLinkInput coolingGanttChartLinkInput = pourHandleInput.handleCoolingInput(input, totalTime, SerializationUtils.clone(waitingPourGanttChartLinkInput.getInput().getClosingGoPourPath()));
        CoolingGanttChartLinkInput coolingOutPut;

        //初始化开箱区的仿真输入
        OpenBoxGanttChartLinkInput openBoxGanttChartLinkInput = pourHandleInput.handleOpenBoxInput(input, totalTime);
        OpenBoxGanttChartLinkInput openBoxOutPut;

        //生成模拟冷却区
        List<List<ImitateCoolingPosition>> upImitates = pourHandleInput.createImitates();
        List<List<ImitateCoolingPosition>> downImitates = pourHandleInput.createImitates();

        OutBoundANDMoldClosing outBoundANDMoldClosing = new OutBoundANDMoldClosing(outBoundGanttChartLinkInput, moldClosingGanttChartLinkInput, 0);
        OutBoundANDMoldClosing outBoundANDOutPut;

        //用于造型线仿真输入输出
        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = new OutBoundMouldingAndClosingBox(outBoundGanttChartLinkInput, mouldingGanttChartLinkInput, closingBoxGanttChartLinkInput);
        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingOutPut;

        //用于待浇筑区之后仿真输入输出
        AfterClosingBoxAllLink afterClosingBoxAllLink = new AfterClosingBoxAllLink(closingBoxGanttChartLinkInput, waitingPourGanttChartLinkInput, pouringGanttChartLinkInput,
                coolingGanttChartLinkInput, openBoxGanttChartLinkInput, outBoundGanttChartLinkInput, waitingPourCount, upImitates, downImitates);
        AfterClosingBoxAllLink afterClosingBoxAllOutPut;

        //其他区域仿真模拟
        OtherSimulationLink otherSimulationLink = new OtherSimulationLink();
        //用于测试
        int size = productDeque.size();
        //判断需要生成的产品数目
        int productNum = 0;
        for (int i = 0; i < input.getOrderInputs().size(); i++) {
            for (int j = 0; j < input.getOrderInputs().get(i).getOrderProducts().size(); j++) {
                productNum = productNum + (input.getOrderInputs().get(i).getOrderProducts().get(j).getNumber() - input.getOrderInputs().get(i).getOrderProducts().get(j).getFinishNum());
            }
        }

//        log.info(productNum + "?" + size);

        int num = 0;
        for (int i = 0; i < input.getOrderInputs().size(); i++) {
            for (int j = 0; j < input.getOrderInputs().get(i).getOrderProducts().size(); j++) {
                num = num + (input.getOrderInputs().get(i).getOrderProducts().get(j).getNumber() - input.getOrderInputs().get(i).getOrderProducts().get(j).getFinishNum());
            }

            orderNums.put(input.getOrderInputs().get(i).getId(), num);
        }

        Map<Long, Double> orderTimes = new HashMap<>();
        int oredrIndex = 0;

        boolean addEventFlag = true;

        //初始化最小时间数组
        Arrays.fill(timeList, 0.0);

        while (true){
            try {
                //获得出库区仿真结果
                //更新仿真输入
                outBoundANDOutPut = outBoundSimulation.OutBoundSimulationRun(outBoundANDMoldClosing.getOutBoundGanttChartLinkInput().getInput(),
                        outBoundANDMoldClosing.getMoldClosingGanttChartLinkInput(), outBoundANDMoldClosing.getOutBoundGanttChartLinkInput().getGanttChart(), finishedCount, timeList);
                outBoundANDMoldClosing = SerializationUtils.clone(outBoundANDOutPut);

                finishedCount = outBoundANDMoldClosing.getFinishedCount();

                //获得合模区仿真结果
                //更新仿真输入
                outBoundANDOutPut = moldClosingSimulation.MoldClosingSimulationRun(outBoundANDMoldClosing.getMoldClosingGanttChartLinkInput().getInput(),
                        outBoundANDMoldClosing.getOutBoundGanttChartLinkInput(), outBoundANDMoldClosing.getMoldClosingGanttChartLinkInput().getGanttChart(), finishedCount, timeList);
                outBoundANDMoldClosing = SerializationUtils.clone(outBoundANDOutPut);

                outBoundMouldingAndClosingBox.setOutBoundInput(outBoundANDMoldClosing.getOutBoundGanttChartLinkInput());

                //获得造型线仿真结果
                //更新仿真输入
                outBoundMouldingAndClosingOutPut = mouldingSimulation_2.MouldingSimulationRun(outBoundMouldingAndClosingBox, timeList, input.getAnalogInput());
                outBoundMouldingAndClosingBox = SerializationUtils.clone(outBoundMouldingAndClosingOutPut);

                //获得合箱区仿真结果
                //更新仿真输入
                outBoundMouldingAndClosingOutPut = closingBoxSimulation.ClosingBoxSimulationRun(outBoundMouldingAndClosingBox, timeList);
                outBoundMouldingAndClosingBox = SerializationUtils.clone(outBoundMouldingAndClosingOutPut);

                outBoundANDMoldClosing.setOutBoundGanttChartLinkInput(outBoundMouldingAndClosingBox.getOutBoundInput());
                afterClosingBoxAllLink.setClosingBoxInput(outBoundMouldingAndClosingBox.getClosingBoxInput());

                //模拟其他区域的仿真
                //更新仿真输入
                otherSimulationLink = otherSimulation_2.otherSimulationRun(outBoundANDMoldClosing.getOutBoundGanttChartLinkInput(), outBoundANDMoldClosing.getMoldClosingGanttChartLinkInput(),
                        outBoundMouldingAndClosingBox.getMouldingInput(), outBoundMouldingAndClosingBox.getClosingBoxInput(), dTime, finishedCount, input.getAnalogInput());

                outBoundANDMoldClosing = otherSimulationLink.getOutBoundANDMoldClosing();
                //更新仿真输入
                outBoundMouldingAndClosingBox.setMouldingInput(SerializationUtils.clone(otherSimulationLink.getMouldingGanttChartLinkInput()));
                outBoundMouldingAndClosingBox.setClosingBoxInput(SerializationUtils.clone(otherSimulationLink.getClosingBoxGanttChartLinkInput()));


                //获得待浇筑区仿真结果
                //更新仿真输入
                afterClosingBoxAllOutPut = waitingPourSimulation.WaitingPourSimulationRun(afterClosingBoxAllLink, timeList);
                afterClosingBoxAllLink = SerializationUtils.clone(afterClosingBoxAllOutPut);

                outBoundMouldingAndClosingBox.setClosingBoxInput(afterClosingBoxAllLink.getClosingBoxInput());

                //更新待浇筑区的产品计数
                waitingPourCount = afterClosingBoxAllLink.getWaitingPourCount();
                if (waitingPourCount == productNum){
                    waitingPourFlag = true;
                    if (addEventFlag){
                        afterClosingBoxAllLink.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(afterClosingBoxAllLink.getWaitingPourInput()
                                .getInput().getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(allTool.createFlagEvent());


                        addEventFlag = false;
                    }
                }

                //判断待浇筑是否放置完成所有订单
                if (waitingPourFlag){
                    //获得浇筑区仿真结果
                    //更新仿真输入
                    afterClosingBoxAllOutPut = pouringSimulation.PouringSimulationRun(afterClosingBoxAllLink, timeList);
                    afterClosingBoxAllLink = SerializationUtils.clone(afterClosingBoxAllOutPut);

                    //获得冷却区仿真结果
                    //更新仿真输入
                    afterClosingBoxAllOutPut = coolingSimulation.CoolingSimulationRun(afterClosingBoxAllLink, timeList);
                    afterClosingBoxAllLink = SerializationUtils.clone(afterClosingBoxAllOutPut);

                    //获得开箱区仿真结果
                    //更新仿真输入
                    afterClosingBoxAllLink.setOutBoundInput(outBoundANDMoldClosing.getOutBoundGanttChartLinkInput());
                    afterClosingBoxAllOutPut = openBoxSimulation.OpenBoxSimulationRun(afterClosingBoxAllLink, timeList);
                    afterClosingBoxAllLink = SerializationUtils.clone(afterClosingBoxAllOutPut);

                    outBoundANDMoldClosing.setOutBoundGanttChartLinkInput(afterClosingBoxAllOutPut.getOutBoundInput());

                    if (afterClosingBoxAllLink.getOpenBoxInput().getInput().getCastingShippingPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        afterClosingBoxAllLink.getOpenBoxInput().getInput().getCastingShippingPosition().setStatus(PositionStatusEnum.UNOCCUPIED);
                        Product product = afterClosingBoxAllLink.getOpenBoxInput().getInput().getCastingShippingPosition().getProduct().productDeepCopy();
                        afterClosingBoxAllLink.getOpenBoxInput().getInput().getCastingShippingPosition().setProduct(null);
                        castingCount++;

                        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
                        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
                        startPositionStatus.add(OCCUPIED);
                        endPositionStatus.add(UNOCCUPIED);

                        Event event = allTool.createEvent("100799", "position", 0.0, 0.0, 0.0, null, null,
                                false, null, null, false, null, null,
                                null,false, null, null, false, null,
                                null, null, null, afterClosingBoxAllLink.getOpenBoxInput().getInput().getCastingShippingPosition().getId(),
                                startPositionStatus, endPositionStatus, null,null, null, null, null,
                                null, afterClosingBoxAllLink.getOpenBoxInput().getInput().getCastingShippingPosition().getId(), ExecuteTypeEnum.POSITION,
                                InstructionsEnum.NOTICE, null, product);

                        afterClosingBoxAllLink.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(afterClosingBoxAllLink.getOpenBoxInput().getInput().getOpenBoxIndexInput().getPositionInGanttIndex()).getEvents().add(event);
                    }
                }

                if (finishedCount == productNum * 5){
                    break;
                }

                //更新总时间
                eventMinTime = allTool.findMinEventTimeAndUpdata(timeList);
                totalTime = totalTime + eventMinTime;
                outBoundANDMoldClosing.getOutBoundGanttChartLinkInput().getInput().setTotalTime(totalTime);
                outBoundANDMoldClosing.getMoldClosingGanttChartLinkInput().getInput().setTotalTime(totalTime);
                outBoundMouldingAndClosingBox.getMouldingInput().getInput().setTotalTime(totalTime);
                outBoundMouldingAndClosingBox.getClosingBoxInput().getInput().setTotalTime(totalTime);
                afterClosingBoxAllLink.getWaitingPourInput().getInput().setTotalTime(totalTime);
                afterClosingBoxAllLink.getPouringInput().getInput().setTotalTime(totalTime);
                afterClosingBoxAllLink.getCoolingInput().getInput().setTotalTime(totalTime);
                afterClosingBoxAllLink.getOpenBoxInput().getInput().setTotalTime(totalTime);

                if (orderNums.get(input.getOrderInputs().get(oredrIndex).getId()) != null){
                    if (orderTimes.get(input.getOrderInputs().get(oredrIndex).getId()) == null){
                        if (castingCount >= orderNums.get(input.getOrderInputs().get(oredrIndex).getId())){

                            orderTimes.put(input.getOrderInputs().get(oredrIndex).getId(), totalTime);
                            if (oredrIndex < input.getOrderInputs().size() - 1){
                                oredrIndex++;
                            }
                        }
                    }else {
                        if (oredrIndex < input.getOrderInputs().size() - 1){
                            oredrIndex++;
                        }
                    }
                }

            }catch (SimulationException e){
                // 捕获并处理自定义异常
                log.info("Caught a custom exception:");
                e.printStackTrace();
            }
        }

        charts.add(outBoundANDMoldClosing.getOutBoundGanttChartLinkInput().getGanttChart());
        charts.add(outBoundANDMoldClosing.getMoldClosingGanttChartLinkInput().getGanttChart());
        charts.add(outBoundMouldingAndClosingBox.getMouldingInput().getGanttChart());
        charts.add(outBoundMouldingAndClosingBox.getClosingBoxInput().getGanttChart());
        charts.add(afterClosingBoxAllLink.getWaitingPourInput().getGanttChart());
        charts.add(afterClosingBoxAllLink.getPouringInput().getGanttChart());
        charts.add(afterClosingBoxAllLink.getCoolingInput().getGanttChart());
        charts.add(afterClosingBoxAllLink.getOpenBoxInput().getGanttChart());

        return new SimulateResult(charts, orderTimes);
    }
}
