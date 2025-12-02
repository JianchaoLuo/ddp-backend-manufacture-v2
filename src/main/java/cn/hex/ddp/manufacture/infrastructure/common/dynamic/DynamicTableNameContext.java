package cn.hex.ddp.manufacture.infrastructure.common.dynamic;

/**
 * 动态表名上下文
 * 用于存储当前线程的 missionId，用于动态生成表名
 *
 * @author Huhaisen
 * @date 2024/12/26
 */
public class DynamicTableNameContext {

    // 使用 ThreadLocal 存储 missionId，保证线程安全，避免多线程下 missionId 混乱
    private static final ThreadLocal<Long> MISSION_ID = new ThreadLocal<>();

    public static void setMissionId(Long missionId) {
        MISSION_ID.set(missionId);
    }

    public static Long getMissionId() {
        return MISSION_ID.get();
    }

    public static void clear() {
        MISSION_ID.remove();
    }
}

