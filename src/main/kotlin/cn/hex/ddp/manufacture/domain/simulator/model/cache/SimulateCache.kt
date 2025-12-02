package cn.hex.ddp.manufacture.domain.simulator.model.cache

import cn.hex.ddp.manufacture.infrastructure.common.redis.RedisUtil
import java.time.LocalDateTime
import kotlin.let
import kotlin.text.toBoolean
import kotlin.text.toLong
import kotlin.toString

object SimulateCache {
    var T1: LocalDateTime? = null
        get() {
            if (field == null) {
                field = RedisUtil.StringOps.get("T1")?.let {
                    LocalDateTime.parse(it)
                }
            }
            return field
        }
        set(value: LocalDateTime?) {
            field = value
            RedisUtil.StringOps.set("T1", value.toString())
        }
    var T2: LocalDateTime? = null
        get() {
            if (field == null) {
                field = RedisUtil.StringOps.get("T2")?.let {
                    LocalDateTime.parse(it)
                }
            }
            return field
        }
        set(value: LocalDateTime?) {
            field = value
            RedisUtil.StringOps.set("T2", value.toString())
        }
    var T3: LocalDateTime? = null
        get() {
            if (field == null) {
                field = RedisUtil.StringOps.get("T3")?.let {
                    LocalDateTime.parse(it)
                }
            }
            return field
        }
        set(value) {
            field = value
            RedisUtil.StringOps.set("T3", value.toString())
        }
    var T4: LocalDateTime? = null
        get() {
            if (field == null) {
                field = RedisUtil.StringOps.get("T4")?.let {
                    LocalDateTime.parse(it)
                }
            }
            return field
        }
        set(value) {
            field = value
            RedisUtil.StringOps.set("T4", value.toString())
        }
    var T5: LocalDateTime? = null
        get() {
            if (field == null) {
                field = RedisUtil.StringOps.get("T5")?.let {
                    LocalDateTime.parse(it)
                }
            }
            return field
        }
        set(value: LocalDateTime?) {
            field = value
            RedisUtil.StringOps.set("T5", value.toString())
        }
    var deltaT: Long = 0
        get() {
            if (field == 0L) {
                field = RedisUtil.StringOps.get("deltaT")?.toLong() ?: 0
            }
            return field
        }
        set(value) {
            field = value
            RedisUtil.StringOps.set("deltaT", value.toString())
        }

    /***
     * 把这玩意设置成false，则算法和模拟器都会停止，结束运行
     */
    var running: Boolean = true
        get() {
            if (field == false) {
                field = RedisUtil.StringOps.get("running")?.toBoolean() == true
            }
            return field
        }
        set(value) {
            field = value
            RedisUtil.StringOps.set("running", value.toString())
        }

    fun reset() {
        T1 = null
        T2 = null
        T3 = null
        T4 = null
        T5 = null
        deltaT = 0
        running = true
        RedisUtil.KeyOps.delete("T1")
        RedisUtil.KeyOps.delete("T2")
        RedisUtil.KeyOps.delete("T3")
        RedisUtil.KeyOps.delete("T4")
        RedisUtil.KeyOps.delete("T5")
        RedisUtil.KeyOps.delete("deltaT")
        RedisUtil.KeyOps.delete("running")


    }

}
