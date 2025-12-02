package cn.hex.ddp.manufacture.domain.common.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * 日期时间转换工具类
 * @Author: FengZebang
 * @Date: 2025/6/11
 */

public class DateConverterUtils {
    private static final Logger logger = Logger.getLogger(DateConverterUtils.class.getName());
    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy")
    );

    // 添加时间格式化器
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 将字符串转换为LocalDateTime，支持多种格式
     * 空字符串("")会被视为null，返回时间为一天的开始(00:00:00)
     */
    public static LocalDateTime convertToLocalDateTime(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;

        dateStr = dateStr.trim();

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, formatter).atStartOfDay();
            } catch (DateTimeParseException ignored) {}
        }

        try {
            return LocalDateTime.parse(dateStr).toLocalDate().atStartOfDay();
        } catch (DateTimeParseException e) {
            logger.warning("日期格式错误: " + dateStr);
            return null;
        }
    }

    /**
     * 将LocalDate转换为LocalDateTime（时间为00:00:00）
     */
    public static LocalDateTime convertToLocalDateTime(LocalDate localDate) {
        return localDate != null ? localDate.atStartOfDay() : null;
    }

    /**
     * 将java.util.Date转换为LocalDateTime（使用系统默认时区，时间为00:00:00）
     */
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date != null ?
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay() : null;
    }

    /**
     * 将时间戳格式化为指定格式的字符串
     * @param timeMillis 时间戳（毫秒）
     * @return 格式化后的时间字符串
     */
    public static String formatTime(long timeMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault())
                .format(DEFAULT_TIME_FORMATTER);
    }

    /**
     * 将LocalDateTime格式化为指定格式的字符串
     * @param localDateTime LocalDateTime对象
     * @return 格式化后的时间字符串
     */
    public static String formatTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DEFAULT_TIME_FORMATTER);
    }
}
