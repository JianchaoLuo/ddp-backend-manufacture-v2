package cn.hex.ddp.manufacture

import cn.hex.ddp.manufacture.infrastructure.common.redis.RedisUtil
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KtTest {
    @Test
    fun test() {
        val get = RedisUtil.StringOps.get("MISSION_ID")
        print(get)
    }
}
