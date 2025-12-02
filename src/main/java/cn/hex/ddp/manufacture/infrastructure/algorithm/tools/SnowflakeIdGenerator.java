package cn.hex.ddp.manufacture.infrastructure.algorithm.tools;

/**
 * @ClassDescription: 雪花id生成函数
 * @Author: KangHong
 * @Created: 2024/7/7 17:03
 */
public class SnowflakeIdGenerator {
    // 起始时间戳 (2024-01-01)
    private final long twepoch = 1704067200000L;

    // 机器ID所占的位数
    private final long workerIdBits = 5L;

    // 数据中心ID所占的位数
    private final long datacenterIdBits = 5L;

    // 支持的最大机器ID，结果是31
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    // 支持的最大数据中心ID，结果是31
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    // 序列在ID中占的位数
    private final long sequenceBits = 12L;

    // 机器ID向左移12位
    private final long workerIdShift = sequenceBits;

    // 数据中心ID向左移17位
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    // 时间戳向左移22位
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    // 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    // 工作节点ID(0~31)
    private long workerId;

    // 数据中心ID(0~31)
    private long datacenterId;

    // 毫秒内序列(0~4095)
    private long sequence = 0L;

    // 上次生成ID的时间戳
    private long lastTimestamp = -1L;

    // 构造函数
    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // 生成ID
    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    // 阻塞到下一毫秒，直到获得新的时间戳
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    // 返回当前时间，以毫秒为单位
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
