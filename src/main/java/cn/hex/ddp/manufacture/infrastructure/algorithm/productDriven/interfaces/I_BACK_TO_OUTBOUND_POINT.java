package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.interfaces;

import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;

import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/11/22 18:11
 */
public interface I_BACK_TO_OUTBOUND_POINT extends BaseInterface{
    List<Event> gen(String name,String age);
}
