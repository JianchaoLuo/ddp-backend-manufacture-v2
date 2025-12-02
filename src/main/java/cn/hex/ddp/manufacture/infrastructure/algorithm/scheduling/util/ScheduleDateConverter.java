package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author:bobo
 * @description: 用于将时间戳类型（分钟）的时间和LocalDateTime进行转换
 * @date:2025/6/9 14:09
 */
@Slf4j
public class ScheduleDateConverter {

    /**
     * 将double类型的时间转为LocalDateTime
     * @param minute 以分钟为单位
     * @return LocalDateTime类型的时间
     */
    public static LocalDateTime minute2LocalDateTime(double minute) {
        LocalDateTime currentTime = LocalDateTime.now();
        int minutes = (int) Math.ceil(minute);
        return currentTime.plusMinutes(minutes);
    }

    /**
     * 将LocalDateTime类型的时间转为double类型的时间戳
     * @param localDateTime 输入的时间对象，要求必须在当前时间之后
     * @return 一个double类型的时间，单位为分钟
     */
    public static double localDateTime2Minute(LocalDateTime localDateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (localDateTime == null) {
            log.error("输入的时间为null，时间转换失败");
            throw new BusinessException(BssExType.TIME_CONVERSION_ERROR, "时间转换异常");
        }
        if (currentTime.isAfter(localDateTime)) {
            log.error("输入的时间:{},该事件早于当前时间，时间转换失败", localDateTime);
            throw new BusinessException(BssExType.TIME_CONVERSION_ERROR, "时间转换异常");
        }
        Duration duration = Duration.between(currentTime, localDateTime);
        long minutes = duration.toMinutes();
        return (double) minutes;
    }

    /**
     * 将以分钟为单位的时间戳转为以天为单位的时间戳
     * @param minute 分钟
     * @return 天数
     */
    public static int minutes2Days(double minute) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime inputTime = minute2LocalDateTime(minute);
        Duration duration = Duration.between(currentTime, inputTime);
        return (int) duration.toDays();
    }

    /**
     * 将以天为单位的日期转换为LocalDateTime
     * @param day 输入的时间
     * @return LocalDateTime对象
     */
    public static LocalDateTime days2LocalDateTime(int day) {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.plusDays(day);
    }
}
