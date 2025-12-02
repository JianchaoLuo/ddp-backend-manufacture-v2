package cn.hex.ddp.manufacture.infrastructure.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;


/**
 * @author 琚翔
 * @version 1.0
 * @className RedisUtil
 * @description Redis工具类
 * @since 2022/2/24 22:38
 * <p>
 * * 声明: 此工具只简单包装了redisTemplate的大部分常用的api, 没有包装redisTemplate所有的api。
 * *      如果对此工具类中的功能不太满意, 或对StringRedisTemplate提供的api不太满意，
 * *      那么可自行实现相应的{@link StringRedisTemplate}类中的对应execute方法, 以达
 * *      到自己想要的效果; 至于如何实现,则可参考源码或{@link LockOps}中的方法。
 * *
 * * 注: 此工具类依赖spring-boot-starter-data-redis类库、以及可选的lombok、fastjson
 * *
 * * 统一说明一: 方法中的key、 value都不能为null。
 * * 统一说明二: 不能跨数据类型进行操作， 否者会操作失败/操作报错。
 * *            如: 向一个String类型的做Hash操作，会失败/报错......等等
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class RedisUtil implements ApplicationContextAware {

    /**
     * 使用StringRedisTemplate(RedisTemplate的定制化升级)
     */
    private static StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RedisUtil.redisTemplate = applicationContext.getBean(StringRedisTemplate.class);
    }

    /**
     * key相关操作
     */
    public static class KeyOps {

        /**
         * 根据key, 删除redis中的对应key-value
         * <p>
         * 注: 若删除失败, 则返回false。
         * <p>
         * 若redis中，不存在该key, 那么返回的也是false。
         * 所以，不能因为返回了false,就认为redis中一定还存
         * 在该key对应的key-value。
         *
         * @param key 要删除的key
         * @return 删除是否成功
         */
        public static boolean delete(String key) {
            // 返回值只可能为true/false, 不可能为null
            Boolean result = redisTemplate.delete(key);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 根据keys, 批量删除key-value
         * <p>
         * 注: 若redis中，不存在对应的key, 那么计数不会加1, 即:
         * redis中存在的key-value里，有名为a1、a2的key，
         * 删除时，传的集合是a1、a2、a3，那么返回结果为2。
         *
         * @param keys 要删除的key集合
         * @return 删除了的key-value个数
         */
        public static long delete(Collection<String> keys) {
            Long count = redisTemplate.delete(keys);
            if (count == null) {
                throw new RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 将key对应的value值进行序列化，并返回序列化后的value值。
         * <p>
         * 注: 若不存在对应的key, 则返回null。
         * 注: dump时，并不会删除redis中的对应key-value。
         * 注: dump功能与restore相反。
         *
         * @param key 要序列化的value的key
         * @return 序列化后的value值
         */
        public static byte[] dump(String key) {
            byte[] result = redisTemplate.dump(key);
            return result;
        }

        /**
         * 将给定的value值，反序列化到redis中, 形成新的key-value。
         *
         * @param key        value对应的key
         * @param value      要反序列的value值。
         *                   注: 这个值可以由{@link this#dump(String)}获得
         * @param timeToLive 反序列化后的key-value的存活时长
         * @param unit       timeToLive的单位
         * @throws RedisSystemException 如果redis中已存在同样的key时，抛出此异常
         */
        public static void restore(String key, byte[] value, long timeToLive, TimeUnit unit) {
            restore(key, value, timeToLive, unit, false);
        }

        /**
         * 将给定的value值，反序列化到redis中, 形成新的key-value。
         *
         * @param key     value对应的key
         * @param value   要反序列的value值。
         *                注: 这个值可以由{@link this#dump(String)}获得
         * @param timeout 反序列化后的key-value的存活时长
         * @param unit    timeout的单位
         * @param replace 若redis中已经存在了相同的key, 是否替代原来的key-value
         * @throws RedisSystemException 如果redis中已存在同样的key, 且replace为false时，抛出此异常
         */
        public static void restore(String key, byte[] value, long timeout, TimeUnit unit, boolean replace) {
            redisTemplate.restore(key, value, timeout, unit, replace);
        }

        /**
         * redis中是否存在,指定key的key-value
         *
         * @param key 指定的key
         * @return 是否存在对应的key-value
         */
        public static boolean hasKey(String key) {
            Boolean result = redisTemplate.hasKey(key);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 给指定的key对应的key-value设置: 多久过时
         * <p>
         * 注:过时后，redis会自动删除对应的key-value。
         * 注:若key不存在，那么也会返回false。
         *
         * @param key     指定的key
         * @param timeout 过时时间
         * @param unit    timeout的单位
         * @return 操作是否成功
         */
        public static boolean expire(String key, long timeout, TimeUnit unit) {
            Boolean result = redisTemplate.expire(key, timeout, unit);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 给指定的key对应的key-value设置: 什么时候过时
         * <p>
         * 注:过时后，redis会自动删除对应的key-value。
         * 注:若key不存在，那么也会返回false。
         *
         * @param key  指定的key
         * @param date 啥时候过时
         * @return 操作是否成功
         */
        public static boolean expireAt(String key, Date date) {
            Boolean result = redisTemplate.expireAt(key, date);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 找到所有匹配pattern的key,并返回该key的结合.
         * <p>
         * 提示:若redis中键值对较多，此方法耗时相对较长，慎用！慎用！慎用！
         *
         * @param pattern 匹配模板。
         *                注: 常用的通配符有:
         *                ?    有且只有一个;
         *                *     >= 0 个;
         * @return 匹配pattern的key的集合。 可能为null。
         */
        public static Set<String> keys(String pattern) {
            Set<String> keys = redisTemplate.keys(pattern);
            return keys;
        }

        /**
         * 将当前数据库中的key对应的key-value,移动到对应位置的数据库中。
         * <p>
         * 注:单机版的redis,默认将存储分为16个db, index为0 到 15。
         * 注:同一个db下，key唯一； 但是在不同db中，key可以相同。
         * 注:若目标db下，已存在相同的key, 那么move会失败，返回false。
         *
         * @param key     定位要移动的key-value的key
         * @param dbIndex 要移动到哪个db
         * @return 移动是否成功。
         * 注: 若目标db下，已存在相同的key, 那么move会失败，返回false。
         */
        public static boolean move(String key, int dbIndex) {
            Boolean result = redisTemplate.move(key, dbIndex);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 移除key对应的key-value的过期时间, 使该key-value一直存在
         * <p>
         * 注: 若key对应的key-value，本身就是一直存在(无过期时间的)， 那么persist方法会返回false;
         * 若没有key对应的key-value存在，本那么persist方法会返回false;
         *
         * @param key 定位key-value的key
         * @return 操作是否成功
         */
        public static boolean persist(String key) {
            Boolean result = redisTemplate.persist(key);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 获取key对应的key-value的过期时间
         * <p>
         * 注: 若key-value永不过期， 那么返回的为-1。
         * 注: 若不存在key对应的key-value， 那么返回的为-2
         * 注:若存在零碎时间不足1 SECONDS,则(大体上)四舍五入到SECONDS级别。
         *
         * @param key 定位key-value的key
         * @return 过期时间(单位s)
         */
        public static long getExpire(String key) {
            long result = getExpire(key, TimeUnit.SECONDS);
            return result;
        }

        /**
         * 获取key对应的key-value的过期时间
         * <p>
         * 注: 若key-value永不过期， 那么返回的为-1。
         * 注: 若不存在key对应的key-value， 那么返回的为-2
         * 注:若存在零碎时间不足1 unit,则(大体上)四舍五入到unit别。
         *
         * @param key 定位key-value的key
         * @return 过期时间(单位unit)
         */
        public static long getExpire(String key, TimeUnit unit) {
            Long result = redisTemplate.getExpire(key, unit);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 从redis的所有key中，随机获取一个key
         * <p>
         * 注: 若redis中不存在任何key-value, 那么这里返回null
         *
         * @return 随机获取到的一个key
         */
        public static String randomKey() {
            String result = redisTemplate.randomKey();
            return result;
        }

        /**
         * 重命名对应的oldKey为新的newKey
         * <p>
         * 注: 若oldKey不存在， 则会抛出异常.
         * 注: 若redis中已存在与newKey一样的key,
         * 那么原key-value会被丢弃，
         * 只留下新的key，以及原来的value
         * 示例说明: 假设redis中已有 (keyAlpha, valueAlpha) 和 (keyBeta, valueBeat),
         * 在使用rename(keyAlpha, keyBeta)替换后, redis中只会剩下(keyBeta, valueAlpha)
         *
         * @param oldKey 旧的key
         * @param newKey 新的key
         * @throws RedisSystemException 若oldKey不存在时， 抛出此异常
         */
        public static void rename(String oldKey, String newKey) {
            redisTemplate.rename(oldKey, newKey);
        }

        /**
         * 当redis中不存在newKey时, 重命名对应的oldKey为新的newKey。
         * 否者不进行重命名操作。
         * <p>
         * 注: 若oldKey不存在， 则会抛出异常.
         *
         * @param oldKey 旧的key
         * @param newKey 新的key
         * @throws RedisSystemException 若oldKey不存在时， 抛出此异常
         */
        public static boolean renameIfAbsent(String oldKey, String newKey) {
            Boolean result = redisTemplate.renameIfAbsent(oldKey, newKey);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 获取key对应的value的数据类型
         * <p>
         * 注: 若redis中不存在该key对应的key-value， 那么这里返回NONE。
         *
         * @param key 用于定位的key
         * @return key对应的value的数据类型
         */
        public static DataType type(String key) {
            DataType result = redisTemplate.type(key);
            return result;
        }
    }

    /**
     * string相关操作
     */
    public static class StringOps {

        /**
         * 设置key-value
         * <p>
         * 注: 若已存在相同的key, 那么原来的key-value会被丢弃。
         *
         * @param key   key
         * @param value key对应的value
         */
        public static void set(String key, String value) {
            redisTemplate.opsForValue().set(key, value);
        }

        /**
         * 处理redis中key对应的value值, 将第offset位的值, 设置为1或0。
         * <p>
         * 说明: 在redis中，存储的字符串都是以二级制的进行存在的; 如存储的key-value里，值为abc,实际上，
         * 在redis里面存储的是011000010110001001100011,前8为对应a,中间8为对应b,后面8位对应c。
         * 示例：这里如果setBit(key, 6, true)的话，就是将索引位置6的那个数，设置值为1，值就变成
         * 了011000110110001001100011
         * 追注:offset即index,从0开始。
         * <p>
         * 注: 参数value为true, 则设置为1；参数value为false, 则设置为0。
         * <p>
         * 注: 若redis中不存在对应的key,那么会自动创建新的。
         * 注: offset可以超过value在二进制下的索引长度。
         *
         * @param key    定位value的key
         * @param offset 要改变的bit的索引
         * @param value  改为1或0, true - 改为1, false - 改为0
         * @return set是否成功
         */
        public static boolean setBit(String key, long offset, boolean value) {
            Boolean result = redisTemplate.opsForValue().setBit(key, offset, value);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 设置key-value
         * <p>
         * 注: 若已存在相同的key, 那么原来的key-value会被丢弃
         *
         * @param key     key
         * @param value   key对应的value
         * @param timeout 过时时长
         * @param unit    timeout的单位
         */
        public static void setEx(String key, String value, long timeout, TimeUnit unit) {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        }

        /**
         * 若不存在key时, 向redis中添加key-value, 返回成功/失败。
         * 若存在，则不作任何操作, 返回false。
         *
         * @param key   key
         * @param value key对应的value
         * @return set是否成功
         */
        public static boolean setIfAbsent(String key, String value) {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 若不存在key时, 向redis中添加一个(具有超时时长的)key-value, 返回成功/失败。
         * 若存在，则不作任何操作, 返回false。
         *
         * @param key     key
         * @param value   key对应的value
         * @param timeout 超时时长
         * @param unit    timeout的单位
         * @return set是否成功
         */
        public static boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 从(redis中key对应的)value的offset位置起(包含该位置),用replaceValue替换对应长度的值。
         * <p>
         * 举例说明:
         * 1.假设redis中存在key-value ("ds", "0123456789"); 调
         * 用setRange("ds", "abcdefghijk", 3)后， redis中该value值就变为了[012abcdefghijk]
         * <p>
         * 2.假设redis中存在key-value ("jd", "0123456789");调
         * 用setRange("jd", "xyz", 3)后， redis中该value值就变为了[012xyz6789]
         * <p>
         * 3.假设redis中存在key-value ("ey", "0123456789");调
         * 用setRange("ey", "qwer", 15)后， redis中该value值就变为了[0123456789     qwer]
         * 注:case3比较特殊，offset超过了原value的长度了, 中间就会有一些空格来填充，但是如果在程序
         * 中直接输出的话，中间那部分空格可能会出现乱码。
         *
         * @param key          定位key-value的key
         * @param replaceValue 要替换的值
         * @param offset       起始位置
         */
        public static void setRange(String key, String replaceValue, long offset) {
            redisTemplate.opsForValue().set(key, replaceValue, offset);
        }

        /**
         * 获取到key对应的value的长度。
         * <p>
         * 注: 长度等于{@link String#length}。
         * 注: 若redis中不存在对应的key-value, 则返回值为0.
         *
         * @param key 定位value的key
         * @return value的长度
         */
        public static long size(String key) {

            Long result = redisTemplate.opsForValue().size(key);

            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 批量设置 key-value
         * <p>
         * 注: 若存在相同的key, 则原来的key-value会被丢弃。
         *
         * @param maps key-value 集
         */
        public static void multiSet(Map<String, String> maps) {

            redisTemplate.opsForValue().multiSet(maps);
        }

        /**
         * 当redis中,不存在任何一个keys时, 才批量设置 key-value, 并返回成功/失败.
         * 否者，不进行任何操作, 并返回false。
         * <p>
         * 即: 假设调用此方法时传入的参数map是这样的: {k1=v1, k2=v2, k3=v3}
         * 那么redis中, k1、k2、k3都不存在时,才会批量设置key-value;
         * 否则不会设置任何key-value。
         * <p>
         * 注: 若存在相同的key, 则原来的key-value会被丢弃。
         * <p>
         * 注:
         *
         * @param maps key-value 集
         * @return 操作是否成功
         */
        public static boolean multiSetIfAbsent(Map<String, String> maps) {

            Boolean result = redisTemplate.opsForValue().multiSetIfAbsent(maps);

            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 增/减 整数
         * <p>
         * 注: 负数则为减。
         * 注: 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
         * 抛出org.springframework.data.redis.RedisSystemException
         *
         * @param key       用于定位value的key
         * @param increment 增加多少
         * @return 增加后的总值。
         * @throws RedisSystemException key对应的value值不支持增/减操作时
         */
        public static long incrBy(String key, long increment) {

            Long result = redisTemplate.opsForValue().increment(key, increment);

            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 增/减 浮点数
         * <p>
         * 注: 慎用浮点数，会有精度问题。
         * 如: 先 RedisUtil.StringOps.set("ds", "123");
         * 然后再RedisUtil.StringOps.incrByFloat("ds", 100.6);
         * 就会看到精度问题。
         * 注: 负数则为减。
         * 注: 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
         * 抛出org.springframework.data.redis.RedisSystemException
         *
         * @param key       用于定位value的key
         * @param increment 增加多少
         * @return 增加后的总值。
         * @throws RedisSystemException key对应的value值不支持增/减操作时
         */
        public static double incrByFloat(String key, double increment) {

            Double result = redisTemplate.opsForValue().increment(key, increment);

            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 追加值到末尾
         * <p>
         * 注: 当redis中原本不存在key时,那么（从效果上来看）此方法就等价于{@link this#set(String, String)}
         *
         * @param key   定位value的key
         * @param value 要追加的value值
         * @return 追加后， 整个value的长度
         */
        public static int append(String key, String value) {

            Integer result = redisTemplate.opsForValue().append(key, value);

            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 根据key，获取到对应的value值
         *
         * @param key key-value对应的key
         * @return 该key对应的值。
         * 注: 若key不存在， 则返回null。
         */
        public static String get(String key) {

            String result = redisTemplate.opsForValue().get(key);

            return result;
        }

        /**
         * 对(key对应的)value进行截取, 截取范围为[start, end]
         * <p>
         * 注: 若[start, end]的范围不在value的范围中，那么返回的是空字符串 ""
         * 注: 若value只有一部分在[start, end]的范围中，那么返回的是value对应部分的内容(即:不足的地方，并不会以空来填充)
         *
         * @param key   定位value的key
         * @param start 起始位置 (从0开始)
         * @param end   结尾位置 (从0开始)
         * @return 截取后的字符串
         */
        public static String getRange(String key, long start, long end) {

            String result = redisTemplate.opsForValue().get(key, start, end);

            return result;
        }

        /**
         * 给指定key设置新的value, 并返回旧的value
         * <p>
         * 注: 若redis中不存在key, 那么此操作仍然可以成功， 不过返回的旧值是null
         *
         * @param key      定位value的key
         * @param newValue 要为该key设置的新的value值
         * @return 旧的value值
         */
        public static String getAndSet(String key, String newValue) {

            String oldValue = redisTemplate.opsForValue().getAndSet(key, newValue);

            return oldValue;
        }

        /**
         * 获取(key对应的)value在二进制下，offset位置的bit值。
         * <p>
         * 注: 当offset的值在(二进制下的value的)索引范围外时, 返回的也是false。
         * <p>
         * 示例:
         * RedisUtil.StringOps.set("akey", "a");
         * 字符串a, 转换为二进制为01100001
         * 那么getBit("akey", 6)获取到的结果为false。
         *
         * @param key    定位value的key
         * @param offset 定位bit的索引
         * @return offset位置对应的bit的值(true - 1, false - 0)
         */
        public static boolean getBit(String key, long offset) {

            Boolean result = redisTemplate.opsForValue().getBit(key, offset);

            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 批量获取value值
         * <p>
         * 注: 若redis中，对应的key不存在，那么该key对应的返回的value值为null
         *
         * @param keys key集
         * @return value值集合
         */
        public static List<String> multiGet(Collection<String> keys) {

            List<String> result = redisTemplate.opsForValue().multiGet(keys);

            return result;
        }
    }

    /**
     * hash相关操作
     * <p>
     * 提示: 简单的，可以将redis中hash的数据结构看作是 Map<String, Map<HK, HV>>
     */
    public static class HashOps {

        /**
         * 向key对应的hash中，增加一个键值对entryKey-entryValue
         * <p>
         * 注: 同一个hash里面，若已存在相同的entryKey， 那么此操作将丢弃原来的entryKey-entryValue，
         * 而使用新的entryKey-entryValue。
         *
         * @param key        定位hash的key
         * @param entryKey   要向hash中增加的键值对里的 键
         * @param entryValue 要向hash中增加的键值对里的 值
         */
        public static void hPut(String key, String entryKey, String entryValue) {

            redisTemplate.opsForHash().put(key, entryKey, entryValue);
        }

        /**
         * 向key对应的hash中，增加maps(即: 批量增加entry集)
         * <p>
         * 注: 同一个hash里面，若已存在相同的entryKey， 那么此操作将丢弃原来的entryKey-entryValue，
         * 而使用新的entryKey-entryValue
         *
         * @param key  定位hash的key
         * @param maps 要向hash中增加的键值对集
         */
        public static void hPutAll(String key, Map<String, String> maps) {

            redisTemplate.opsForHash().putAll(key, maps);
        }

        /**
         * 当key对应的hash中,不存在entryKey时，才(向key对应的hash中，)增加entryKey-entryValue
         * 否者，不进行任何操作
         *
         * @param key        定位hash的key
         * @param entryKey   要向hash中增加的键值对里的 键
         * @param entryValue 要向hash中增加的键值对里的 值
         * @return 操作是否成功。
         */
        public static boolean hPutIfAbsent(String key, String entryKey, String entryValue) {
            Boolean result = redisTemplate.opsForHash().putIfAbsent(key, entryKey, entryValue);

            return result;
        }

        /**
         * 获取到key对应的hash里面的对应字段的值
         * <p>
         * 注: 若redis中不存在对应的key, 则返回null。
         * 若key对应的hash中不存在对应的entryKey, 也会返回null。
         *
         * @param key      定位hash的key
         * @param entryKey 定位hash里面的entryValue的entryKey
         * @return key对应的hash里的entryKey对应的entryValue值
         */
        public static Object hGet(String key, String entryKey) {

            Object entryValue = redisTemplate.opsForHash().get(key, entryKey);

            return entryValue;
        }

        /**
         * 获取到key对应的hash(即: 获取到key对应的Map<HK, HV>)
         * <p>
         * 注: 若redis中不存在对应的key, 则返回一个没有任何entry的空的Map(，而不是返回null)。
         *
         * @param key 定位hash的key
         * @return key对应的hash。
         */
        public static Map<Object, Object> hGetAll(String key) {

            Map<Object, Object> result = redisTemplate.opsForHash().entries(key);

            return result;
        }

        /**
         * 批量获取(key对应的)hash中的entryKey的entryValue
         * <p>
         * 注: 若hash中对应的entryKey不存在，那么返回的对应的entryValue值为null
         * 注: redis中key不存在，那么返回的List中，每个元素都为null。
         * 追注: 这个List本身不为null, size也不为0， 只是每个list中的每个元素为null而已。
         *
         * @param key       定位hash的key
         * @param entryKeys 需要获取的hash中的字段集
         * @return hash中对应entryKeys的对应entryValue集
         */
        public static List<Object> hMultiGet(String key, Collection<Object> entryKeys) {

            List<Object> entryValues = redisTemplate.opsForHash().multiGet(key, entryKeys);

            return entryValues;
        }

        /**
         * (批量)删除(key对应的)hash中的对应entryKey-entryValue
         * <p>
         * 注: 1、若redis中不存在对应的key, 则返回0;
         * 2、若要删除的entryKey，在key对应的hash中不存在，在count不会+1, 如:
         * RedisUtil.HashOps.hPut("ds", "name", "邓沙利文");
         * RedisUtil.HashOps.hPut("ds", "birthday", "1994-02-05");
         * RedisUtil.HashOps.hPut("ds", "hobby", "女");
         * 则调用RedisUtil.HashOps.hDelete("ds", "name", "birthday", "hobby", "non-exist-entryKey")
         * 的返回结果为3
         * 注: 若(key对应的)hash中的所有entry都被删除了，那么该key也会被删除
         *
         * @param key       定位hash的key
         * @param entryKeys 定位要删除的entryKey-entryValue的entryKey
         * @return 删除了对应hash中多少个entry
         */
        public static long hDelete(String key, Object... entryKeys) {

            Long count = redisTemplate.opsForHash().delete(key, entryKeys);

            return count;
        }

        /**
         * 查看(key对应的)hash中，是否存在entryKey对应的entry
         * <p>
         * 注: 若redis中不存在key,则返回false。
         * 注: 若key对应的hash中不存在对应的entryKey, 也会返回false。
         *
         * @param key      定位hash的key
         * @param entryKey 定位hash中entry的entryKey
         * @return hash中是否存在entryKey对应的entry.
         */
        public static boolean hExists(String key, String entryKey) {

            Boolean exist = redisTemplate.opsForHash().hasKey(key, entryKey);

            return exist;
        }

        /**
         * 增/减(hash中的某个entryValue值) 整数
         * <p>
         * 注: 负数则为减。
         * 注: 若key不存在，那么会自动创建对应的hash,并创建对应的entryKey、entryValue,entryValue的初始值为increment。
         * 注: 若entryKey不存在，那么会自动创建对应的entryValue,entryValue的初始值为increment。
         * 注: 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
         * 抛出org.springframework.data.redis.RedisSystemException
         *
         * @param key       用于定位hash的key
         * @param entryKey  用于定位entryValue的entryKey
         * @param increment 增加多少
         * @return 增加后的总值。
         * @throws RedisSystemException key对应的value值不支持增/减操作时
         */
        public static long hIncrBy(String key, Object entryKey, long increment) {

            Long result = redisTemplate.opsForHash().increment(key, entryKey, increment);

            return result;
        }

        /**
         * 增/减(hash中的某个entryValue值) 浮点数
         * <p>
         * 注: 负数则为减。
         * 注: 若key不存在，那么会自动创建对应的hash,并创建对应的entryKey、entryValue,entryValue的初始值为increment。
         * 注: 若entryKey不存在，那么会自动创建对应的entryValue,entryValue的初始值为increment。
         * 注: 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
         * 抛出org.springframework.data.redis.RedisSystemException
         * 注: 因为是浮点数， 所以可能会和{@link StringOps#incrByFloat(String, double)}一样， 出现精度问题。
         * 追注: 本人简单测试了几组数据，暂未出现精度问题。
         *
         * @param key       用于定位hash的key
         * @param entryKey  用于定位entryValue的entryKey
         * @param increment 增加多少
         * @return 增加后的总值。
         * @throws RedisSystemException key对应的value值不支持增/减操作时
         */
        public static double hIncrByFloat(String key, Object entryKey, double increment) {
            Double result = redisTemplate.opsForHash().increment(key, entryKey, increment);
            return result;
        }

        /**
         * 获取(key对应的)hash中的所有entryKey
         * <p>
         * 注: 若key不存在，则返回的是一个空的Set(，而不是返回null)
         *
         * @param key 定位hash的key
         * @return hash中的所有entryKey
         */
        public static Set<Object> hKeys(String key) {
            Set<Object> entryKeys = redisTemplate.opsForHash().keys(key);
            return entryKeys;
        }

        /**
         * 获取(key对应的)hash中的所有entryValue
         * <p>
         * 注: 若key不存在，则返回的是一个空的List(，而不是返回null)
         *
         * @param key 定位hash的key
         * @return hash中的所有entryValue
         */
        public static List<Object> hValues(String key) {
            List<Object> entryValues = redisTemplate.opsForHash().values(key);
            return entryValues;
        }

        /**
         * 获取(key对应的)hash中的所有entry的数量
         * <p>
         * 注: 若redis中不存在对应的key, 则返回值为0
         *
         * @param key 定位hash的key
         * @return (key对应的)hash中, entry的个数
         */
        public static long hSize(String key) {
            Long count = redisTemplate.opsForHash().size(key);
            return count;
        }

        /**
         * 根据options匹配到(key对应的)hash中的对应的entryKey, 并返回对应的entry集
         * <p>
         * <p>
         * 注: ScanOptions实例的创建方式举例:
         * 1、ScanOptions.NONE
         * 2、ScanOptions.scanOptions().match("n??e").build()
         *
         * @param key     定位hash的key
         * @param options 匹配entryKey的条件
         *                注: ScanOptions.NONE表示全部匹配。
         *                注: ScanOptions.scanOptions().match(pattern).build()表示按照pattern匹配,
         *                其中pattern中可以使用通配符 * ? 等,
         *                * 表示>=0个字符
         *                ？ 表示有且只有一个字符
         *                此处的匹配规则与{@link KeyOps#keys(String)}处的一样。
         * @return 匹配到的(key对应的)hash中的entry
         */
        public static Cursor<Entry<Object, Object>> hScan(String key, ScanOptions options) {
            Cursor<Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, options);
            return cursor;
        }
    }

    /**
     * list相关操作
     * <p>
     * 提示: 列表中的元素，可以重复。
     * <p>
     * 提示: list是有序的。
     * <p>
     * 提示: redis中的list中的索引，可分为两类,这两类都可以用来定位list中元素:
     * 类别一: 从left到right, 是从0开始依次增大:   0,  1,  2,  3...
     * 类别二: 从right到left, 是从-1开始依次减小: -1, -2, -3, -4...
     */
    public static class ListOps {

        /**
         * 从左端推入元素进列表
         * <p>
         * 注: 若redis中不存在对应的key, 那么会自动创建
         *
         * @param key  定位list的key
         * @param item 要推入list的元素
         * @return 推入后，(key对应的)list的size
         */
        public static long lLeftPush(String key, String item) {
            Long size = redisTemplate.opsForList().leftPush(key, item);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 从左端批量推入元素进列表
         * <p>
         * 注: 若redis中不存在对应的key, 那么会自动创建
         * 注: 这一批item中，先push左侧的, 后push右侧的
         *
         * @param key   定位list的key
         * @param items 要批量推入list的元素集
         * @return 推入后，(key对应的)list的size
         */
        public static long lLeftPushAll(String key, String... items) {
            Long size = redisTemplate.opsForList().leftPushAll(key, items);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 从左端批量推入元素进列表
         * <p>
         * 注: 若redis中不存在对应的key, 那么会自动创建
         * 注: 这一批item中，那个item先从Collection取出来，就先push哪个
         *
         * @param key   定位list的key
         * @param items 要批量推入list的元素集
         * @return 推入后，(key对应的)list的size
         */
        public static long lLeftPushAll(String key, Collection<String> items) {
            Long size = redisTemplate.opsForList().leftPushAll(key, items);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 如果redis中存在key, 则从左端批量推入元素进列表;
         * 否则，不进行任何操作
         *
         * @param key  定位list的key
         * @param item 要推入list的项
         * @return 推入后，(key对应的)list的size
         */
        public static long lLeftPushIfPresent(String key, String item) {
            Long size = redisTemplate.opsForList().leftPushIfPresent(key, item);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 若key对应的list中存在pivot项, 那么将item放入第一个pivot项前(即:放在第一个pivot项左边);
         * 若key对应的list中不存在pivot项, 那么不做任何操作， 直接返回-1。
         * <p>
         * 注: 若redis中不存在对应的key, 那么会自动创建
         *
         * @param key  定位list的key
         * @param item 要推入list的元素
         * @return 推入后，(key对应的)list的size
         */
        public static long lLeftPush(String key, String pivot, String item) {
            Long size = redisTemplate.opsForList().leftPush(key, pivot, item);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link ListOps#lLeftPush(String, String)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPush(String key, String item) {
            Long size = redisTemplate.opsForList().rightPush(key, item);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link ListOps#lLeftPushAll(String, String...)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPushAll(String key, String... items) {
            Long size = redisTemplate.opsForList().rightPushAll(key, items);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link ListOps#lLeftPushAll(String, Collection<String>)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPushAll(String key, Collection<String> items) {
            Long size = redisTemplate.opsForList().rightPushAll(key, items);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link ListOps#lLeftPushIfPresent(String, String)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPushIfPresent(String key, String item) {
            Long size = redisTemplate.opsForList().rightPushIfPresent(key, item);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link ListOps#lLeftPush(String, String, String)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPush(String key, String pivot, String item) {
            Long size = redisTemplate.opsForList().rightPush(key, pivot, item);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 【非阻塞队列】 从左侧移出(key对应的)list中的第一个元素, 并将该元素返回
         * <p>
         * 注: 此方法是非阻塞的， 即: 若(key对应的)list中的所有元素都被pop移出了，此时，再进行pop的话，会立即返回null
         * 注: 此方法是非阻塞的， 即: 若redis中不存在对应的key,那么会立即返回null
         * 注: 若将(key对应的)list中的所有元素都pop完了，那么该key会被删除
         *
         * @param key 定位list的key
         * @return 移出的那个元素
         */
        public static String lLeftPop(String key) {
            String item = redisTemplate.opsForList().leftPop(key);
            return item;
        }

        /**
         * 【阻塞队列】 从左侧移出(key对应的)list中的第一个元素, 并将该元素返回
         * <p>
         * 注: 此方法是阻塞的， 即: 若(key对应的)list中的所有元素都被pop移出了，此时，再进行pop的话，
         * 会阻塞timeout这么久，然后返回null
         * 注: 此方法是阻塞的， 即: 若redis中不存在对应的key,那么会阻塞timeout这么久，然后返回null
         * 注: 若将(key对应的)list中的所有元素都pop完了，那么该key会被删除
         * <p>
         * 提示: 若阻塞过程中， 目标key-list出现了，且里面有item了，那么会立马停止阻塞, 进行元素移出并返回
         *
         * @param key     定位list的key
         * @param timeout 超时时间
         * @param unit    timeout的单位
         * @return 移出的那个元素
         */
        public static String lLeftPop(String key, long timeout, TimeUnit unit) {
            String item = redisTemplate.opsForList().leftPop(key, timeout, unit);
            return item;
        }

        /**
         * 与{@link ListOps#lLeftPop(String)}类比即可， 不过是从list右侧移出元素
         */
        public static String lRightPop(String key) {
            String item = redisTemplate.opsForList().rightPop(key);
            return item;
        }

        /**
         * 与{@link ListOps#lLeftPop(String, long, TimeUnit)}类比即可， 不过是从list右侧移出元素
         */
        public static String lRightPop(String key, long timeout, TimeUnit unit) {
            String item = redisTemplate.opsForList().rightPop(key, timeout, unit);
            return item;
        }

        /**
         * 【非阻塞队列】 从sourceKey对应的sourceList右侧移出一个item, 并将这个item推
         * 入(destinationKey对应的)destinationList的左侧
         * <p>
         * 注: 若sourceKey对应的list中没有item了，则立马认为(从sourceKey对应的list中pop出来的)item为null,
         * null并不会往destinationKey对应的list中push。
         * 追注: 此时，此方法的返回值是null。
         * <p>
         * 注: 若将(sourceKey对应的)list中的所有元素都pop完了，那么该sourceKey会被删除。
         *
         * @param sourceKey      定位sourceList的key
         * @param destinationKey 定位destinationList的key
         * @return 移动的这个元素
         */
        public static String lRightPopAndLeftPush(String sourceKey, String destinationKey) {
            String item = redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
            return item;
        }

        /**
         * 【阻塞队列】 从sourceKey对应的sourceList右侧移出一个item, 并将这个item推
         * 入(destinationKey对应的)destinationList的左侧
         * <p>
         * 注: 若sourceKey对应的list中没有item了，则阻塞等待, 直到能从sourceList中移出一个非null的item(或等待时长超时);
         * case1: 等到了一个非null的item, 那么继续下面的push操作，并返回这个item。
         * case2: 超时了，还没等到非null的item, 那么pop出的结果就未null,此时并不会往destinationList进行push。
         * 此时，此方法的返回值是null。
         * <p>
         * 注: 若将(sourceKey对应的)list中的所有元素都pop完了，那么该sourceKey会被删除。
         *
         * @param sourceKey      定位sourceList的key
         * @param destinationKey 定位destinationList的key
         * @param timeout        超时时间
         * @param unit           timeout的单位
         * @return 移动的这个元素
         */
        public static String lRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout,
                                                  TimeUnit unit) {
            String item = redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
            return item;
        }

        /**
         * 设置(key对应的)list中对应索引位置index处的元素为item
         * <p>
         * 注: 若key不存在，则会抛出org.springframework.data.redis.RedisSystemException
         * 注: 若索引越界，也会抛出org.springframework.data.redis.RedisSystemException
         *
         * @param key   定位list的key
         * @param index 定位list中的元素的索引
         * @param item  要替换成的值
         */
        public static void lSet(String key, long index, String item) {
            redisTemplate.opsForList().set(key, index, item);
        }

        /**
         * 通过索引index, 获取(key对应的)list中的元素
         * <p>
         * 注: 若key不存在 或 index超出(key对应的)list的索引范围，那么返回null
         *
         * @param key   定位list的key
         * @param index 定位list中的item的索引
         * @return list中索引index对应的item
         */
        public static String lIndex(String key, long index) {
            String item = redisTemplate.opsForList().index(key, index);
            return item;
        }

        /**
         * 获取(key对应的)list中索引在[start, end]之间的item集
         * <p>
         * 注: 含start、含end。
         * 注: 当key不存在时，获取到的是空的集合。
         * 注: 当获取的范围比list的范围还要大时，获取到的是这两个范围的交集。
         * <p>
         * 提示: 可通过RedisUtil.ListOps.lRange(key, 0, -1)来获取到该key对应的整个list
         *
         * @param key   定位list的key
         * @param start 起始元素的index
         * @param end   结尾元素的index
         * @return 对应的元素集合
         */
        public static List<String> lRange(String key, long start, long end) {
            List<String> result = redisTemplate.opsForList().range(key, start, end);
            return result;
        }

        /**
         * 获取(key对应的)list
         *
         * @param key 定位list的key
         * @return (key对应的)list
         * @see ListOps#lRange(String, long, long)
         */
        public static List<String> lWholeList(String key) {
            List<String> result = redisTemplate.opsForList().range(key, 0, -1);
            return result;
        }

        /**
         * 获取(key对应的)list的size
         * <p>
         * 注: 当key不存在时，获取到的size为0.
         *
         * @param key 定位list的key
         * @return list的size。
         */
        public static long lSize(String key) {
            Long size = redisTemplate.opsForList().size(key);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 删除(key对应的)list中，前expectCount个值等于item的项
         * <p>
         * 注: 若expectCount == 0， 则表示删除list中所有的值等于item的项.
         * 注: 若expectCount > 0，  则表示删除从左往右进行
         * 注: 若expectCount < 0，  则表示删除从右往左进行
         * <p>
         * 注: 若list中,值等于item的项的个数少于expectCount时，那么会删除list中所有的值等于item的项。
         * 注: 当key不存在时, 返回0。
         * 注: 若lRemove后， 将(key对应的)list中没有任何元素了，那么该key会被删除。
         *
         * @param key         定位list的key
         * @param expectCount 要删除的item的个数
         * @param item        要删除的item
         * @return 实际删除了的item的个数
         */
        public static long lRemove(String key, long expectCount, String item) {
            Long actualCount = redisTemplate.opsForList().remove(key, expectCount, item);
            if (actualCount == null) {
                throw new RedisOpsResultIsNullException();
            }
            return actualCount;
        }

        /**
         * 裁剪(即: 对list中的元素取交集。)
         * <p>
         * 举例说明: list中的元素索引范围是[0, 8], 而这个方法传入的[start, end]为 [3, 10]，
         * 那么裁剪就是对[0, 8]和[3, 10]进行取交集， 得到[3, 8], 那么裁剪后
         * 的list中，只剩下(原来裁剪前)索引在[3, 8]之间的元素了。
         * <p>
         * 注: 若裁剪后的(key对应的)list就是空的,那么该key会被删除。
         *
         * @param key   定位list的key
         * @param start 要删除的item集的起始项的索引
         * @param end   要删除的item集的结尾项的索引
         */
        public static void lTrim(String key, long start, long end) {
            redisTemplate.opsForList().trim(key, start, end);
        }

    }

    /**
     * set相关操作
     * <p>
     * 提示: set中的元素，不可以重复。
     * 提示: set是无序的。
     */
    public static class SetOps {

        /**
         * 向(key对应的)set中添加items
         * <p>
         * 注: 若key不存在，则会自动创建。
         * 注: set中的元素会去重。
         *
         * @param key   定位set的key
         * @param items 要向(key对应的)set中添加的items
         * @return 此次添加操作, 添加到set中的元素的个数
         */
        public static long sAdd(String key, String... items) {
            Long count = redisTemplate.opsForSet().add(key, items);
            if (count == null) {
                throw new RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 从(key对应的)set中删除items
         * <p>
         * 注: 若key不存在, 则返回0。
         * 注: 若已经将(key对应的)set中的项删除完了，那么对应的key也会被删除。
         *
         * @param key   定位set的key
         * @param items 要移除的items
         * @return 实际删除了的个数
         */
        public static long sRemove(String key, Object... items) {
            Long count = redisTemplate.opsForSet().remove(key, items);
            if (count == null) {
                throw new RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 从(key对应的)set中随机移出一个item, 并返回这个item
         * <p>
         * 注: 因为set是无序的，所以移出的这个item,是随机的; 并且，哪怕
         * 是数据一样的set,多次测试移出操作,移除的元素也是随机的。
         * <p>
         * 注: 若已经将(key对应的)set中的项pop完了，那么对应的key会被删除。
         *
         * @param key 定位set的key
         * @return 移出的项
         */
        public static String sPop(String key) {
            String popItem = redisTemplate.opsForSet().pop(key);
            return popItem;
        }

        /**
         * 将(sourceKey对应的)sourceSet中的元素item, 移动到(destinationKey对应的)destinationSet中
         * <p>
         * 注: 当sourceKey不存在时， 返回false
         * 注: 当item不存在时， 返回false
         * 注: 若destinationKey不存在， 那么在移动时会自动创建
         * 注: 若已经将(sourceKey对应的)set中的项move出去完了，那么对应的sourceKey会被删除。
         *
         * @param sourceKey      定位sourceSet的key
         * @param item           要移动的项目
         * @param destinationKey 定位destinationSet的key
         * @return 移动成功与否
         */
        public static boolean sMove(String sourceKey, String item, String destinationKey) {
            Boolean result = redisTemplate.opsForSet().move(sourceKey, item, destinationKey);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 获取(key对应的)set中的元素个数
         * <p>
         * 注: 若key不存在，则返回0
         *
         * @param key 定位set的key
         * @return (key对应的)set中的元素个数
         */
        public static long sSize(String key) {
            Long size = redisTemplate.opsForSet().size(key);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 判断(key对应的)set中是否含有item
         * <p>
         * 注: 若key不存在，则返回false。
         *
         * @param key  定位set的key
         * @param item 被查找的项
         * @return (key对应的)set中是否含有item
         */
        public static boolean sIsMember(String key, Object item) {
            Boolean result = redisTemplate.opsForSet().isMember(key, item);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 获取两个(key对应的)Set的交集
         * <p>
         * 注: 若不存在任何交集，那么返回空的集合(, 而不是null)
         * 注: 若其中一个key不存在(或两个key都不存在)，那么返回空的集合(, 而不是null)
         *
         * @param key      定位其中一个set的键
         * @param otherKey 定位其中另一个set的键
         * @return item交集
         */
        public static Set<String> sIntersect(String key, String otherKey) {
            Set<String> intersectResult = redisTemplate.opsForSet().intersect(key, otherKey);
            return intersectResult;
        }

        /**
         * 获取多个(key对应的)Set的交集
         * <p>
         * 注: 若不存在任何交集，那么返回空的集合(, 而不是null)
         * 注: 若>=1个key不存在，那么返回空的集合(, 而不是null)
         *
         * @param key       定位其中一个set的键
         * @param otherKeys 定位其它set的键集
         * @return item交集
         */
        public static Set<String> sIntersect(String key, Collection<String> otherKeys) {
            Set<String> intersectResult = redisTemplate.opsForSet().intersect(key, otherKeys);
            return intersectResult;
        }

        /**
         * 获取两个(key对应的)Set的交集, 并将结果add到storeKey对应的Set中。
         * <p>
         * case1: 交集不为空, storeKey不存在， 则 会创建对应的storeKey，并将交集添加到(storeKey对应的)set中
         * case2: 交集不为空, storeKey已存在， 则 会清除原(storeKey对应的)set中所有的项，然后将交集添加到(storeKey对应的)set中
         * case3: 交集为空, 则不进行下面的操作, 直接返回0
         * <p>
         * 注: 求交集的部分，详见{@link SetOps#sIntersect(String, String)}
         *
         * @param key      定位其中一个set的键
         * @param otherKey 定位其中另一个set的键
         * @param storeKey 定位(要把交集添加到哪个)set的key
         * @return add到(storeKey对应的)Set后, 该set对应的size
         */
        public static long sIntersectAndStore(String key, String otherKey, String storeKey) {
            Long size = redisTemplate.opsForSet().intersectAndStore(key, otherKey, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 获取多个(key对应的)Set的交集, 并将结果add到storeKey对应的Set中。
         * <p>
         * case1: 交集不为空, storeKey不存在， 则 会创建对应的storeKey，并将交集添加到(storeKey对应的)set中
         * case2: 交集不为空, storeKey已存在， 则 会清除原(storeKey对应的)set中所有的项，然后将交集添加到(storeKey对应的)set中
         * case3: 交集为空, 则不进行下面的操作, 直接返回0
         * <p>
         * 注: 求交集的部分，详见{@link SetOps#sIntersect(String, Collection)}
         */
        public static long sIntersectAndStore(String key, Collection<String> otherKeys, String storeKey) {
            Long size = redisTemplate.opsForSet().intersectAndStore(key, otherKeys, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 获取两个(key对应的)Set的并集
         * <p>
         * 注: 并集中的元素也是唯一的，这是Set保证的。
         *
         * @param key      定位其中一个set的键
         * @param otherKey 定位其中另一个set的键
         * @return item并集
         */
        public static Set<String> sUnion(String key, String otherKey) {
            Set<String> unionResult = redisTemplate.opsForSet().union(key, otherKey);
            return unionResult;
        }

        /**
         * 获取两个(key对应的)Set的并集
         * <p>
         * 注: 并集中的元素也是唯一的，这是Set保证的。
         *
         * @param key       定位其中一个set的键
         * @param otherKeys 定位其它set的键集
         * @return item并集
         */
        public static Set<String> sUnion(String key, Collection<String> otherKeys) {
            Set<String> unionResult = redisTemplate.opsForSet().union(key, otherKeys);
            return unionResult;
        }

        /**
         * 获取两个(key对应的)Set的并集, 并将结果add到storeKey对应的Set中。
         * <p>
         * case1: 并集不为空, storeKey不存在， 则 会创建对应的storeKey，并将并集添加到(storeKey对应的)set中
         * case2: 并集不为空, storeKey已存在， 则 会清除原(storeKey对应的)set中所有的项，然后将并集添加到(storeKey对应的)set中
         * case3: 并集为空, 则不进行下面的操作, 直接返回0
         * <p>
         * 注: 求并集的部分，详见{@link SetOps#sUnion(String, String)}
         *
         * @param key      定位其中一个set的键
         * @param otherKey 定位其中另一个set的键
         * @param storeKey 定位(要把并集添加到哪个)set的key
         * @return add到(storeKey对应的)Set后, 该set对应的size
         */
        public static long sUnionAndStore(String key, String otherKey, String storeKey) {
            Long size = redisTemplate.opsForSet().unionAndStore(key, otherKey, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 获取两个(key对应的)Set的并集, 并将结果add到storeKey对应的Set中。
         * <p>
         * case1: 并集不为空, storeKey不存在， 则 会创建对应的storeKey，并将并集添加到(storeKey对应的)set中
         * case2: 并集不为空, storeKey已存在， 则 会清除原(storeKey对应的)set中所有的项，然后将并集添加到(storeKey对应的)set中
         * case3: 并集为空, 则不进行下面的操作, 直接返回0
         * <p>
         * 注: 求并集的部分，详见{@link SetOps#sUnion(String, Collection)}
         *
         * @param key       定位其中一个set的键
         * @param otherKeys 定位其它set的键集
         * @param storeKey  定位(要把并集添加到哪个)set的key
         * @return add到(storeKey对应的)Set后, 该set对应的size
         */
        public static long sUnionAndStore(String key, Collection<String> otherKeys, String storeKey) {
            Long size = redisTemplate.opsForSet().unionAndStore(key, otherKeys, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 获取 (key对应的)Set 减去 (otherKey对应的)Set 的差集
         * <p>
         * 注: 如果被减数key不存在， 那么结果为空的集合(，而不是null)
         * 注: 如果被减数key存在，但减数key不存在， 那么结果即为(被减数key对应的)Set
         *
         * @param key      定位"被减数set"的键
         * @param otherKey 定位"减数set"的键
         * @return item差集
         */
        public static Set<String> sDifference(String key, String otherKey) {
            Set<String> differenceResult = redisTemplate.opsForSet().difference(key, otherKey);
            return differenceResult;
        }

        /**
         * 获取 (key对应的)Set 减去 (otherKeys对应的)Sets 的差集
         * <p>
         * 注: 如果被减数key不存在， 那么结果为空的集合(，而不是null)
         * 注: 如果被减数key存在，但减数key不存在， 那么结果即为(被减数key对应的)Set
         * <p>
         * 提示: 当有多个减数时， 被减数先减去哪一个减数，后减去哪一个减数，是无所谓的，是不影响最终结果的。
         *
         * @param key       定位"被减数set"的键
         * @param otherKeys 定位"减数集sets"的键集
         * @return item差集
         */
        public static Set<String> sDifference(String key, Collection<String> otherKeys) {
            Set<String> differenceResult = redisTemplate.opsForSet().difference(key, otherKeys);
            return differenceResult;
        }

        /**
         * 获取 (key对应的)Set 减去 (otherKey对应的)Set 的差集, 并将结果add到storeKey对应的Set中。
         * <p>
         * case1: 差集不为空, storeKey不存在， 则 会创建对应的storeKey，并将差集添加到(storeKey对应的)set中
         * case2: 差集不为空, storeKey已存在， 则 会清除原(storeKey对应的)set中所有的项，然后将差集添加到(storeKey对应的)set中
         * case3: 差集为空, 则不进行下面的操作, 直接返回0
         * <p>
         * 注: 求并集的部分，详见{@link SetOps#sDifference(String, String)}
         *
         * @param key      定位"被减数set"的键
         * @param otherKey 定位"减数set"的键
         * @param storeKey 定位(要把差集添加到哪个)set的key
         * @return add到(storeKey对应的)Set后, 该set对应的size
         */
        public static long sDifferenceAndStore(String key, String otherKey, String storeKey) {
            Long size = redisTemplate.opsForSet().differenceAndStore(key, otherKey, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 获取 (key对应的)Set 减去 (otherKey对应的)Set 的差集, 并将结果add到storeKey对应的Set中。
         * <p>
         * case1: 差集不为空, storeKey不存在， 则 会创建对应的storeKey，并将差集添加到(storeKey对应的)set中
         * case2: 差集不为空, storeKey已存在， 则 会清除原(storeKey对应的)set中所有的项，然后将差集添加到(storeKey对应的)set中
         * case3: 差集为空, 则不进行下面的操作, 直接返回0
         * <p>
         * 注: 求并集的部分，详见{@link SetOps#sDifference(String, String)}
         *
         * @param key       定位"被减数set"的键
         * @param otherKeys 定位"减数集sets"的键集
         * @param storeKey  定位(要把差集添加到哪个)set的key
         * @return add到(storeKey对应的)Set后, 该set对应的size
         */
        public static long sDifferenceAndStore(String key, Collection<String> otherKeys, String storeKey) {
            Long size = redisTemplate.opsForSet().differenceAndStore(key, otherKeys, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 获取key对应的set
         * <p>
         * 注: 若key不存在, 则返回的是空的set(, 而不是null)
         *
         * @param key 定位set的key
         * @return (key对应的)set
         */
        public static Set<String> sMembers(String key) {
            Set<String> members = redisTemplate.opsForSet().members(key);
            return members;
        }

        /**
         * 从key对应的set中随机获取一项
         *
         * @param key 定位set的key
         * @return 随机获取到的项
         */
        public static String sRandomMember(String key) {
            String randomItem = redisTemplate.opsForSet().randomMember(key);
            return randomItem;
        }

        /**
         * 从key对应的set中获取count次随机项(, set中的同一个项可能被多次获取)
         * <p>
         * 注: count可大于set的size。
         * 注: 取出来的结果里可能存在相同的值。
         *
         * @param key   定位set的key
         * @param count 要取多少项
         * @return 随机获取到的项集
         */
        public static List<String> sRandomMembers(String key, long count) {
            List<String> randomItems = redisTemplate.opsForSet().randomMembers(key, count);
            return randomItems;
        }

        /**
         * 从key对应的set中随机获取count个项
         * <p>
         * 注: 若count >= set的size, 那么返回的即为这个key对应的set。
         * 注: 取出来的结果里没有重复的项。
         *
         * @param key   定位set的key
         * @param count 要取多少项
         * @return 随机获取到的项集
         */
        public static Set<String> sDistinctRandomMembers(String key, long count) {
            Set<String> distinctRandomItems = redisTemplate.opsForSet().distinctRandomMembers(key, count);
            return distinctRandomItems;
        }

        /**
         * 根据options匹配到(key对应的)set中的对应的item, 并返回对应的item集
         * <p>
         * <p>
         * 注: ScanOptions实例的创建方式举例:
         * 1、ScanOptions.NONE
         * 2、ScanOptions.scanOptions().match("n??e").build()
         *
         * @param key     定位set的key
         * @param options 匹配set中的item的条件
         *                注: ScanOptions.NONE表示全部匹配。
         *                注: ScanOptions.scanOptions().match(pattern).build()表示按照pattern匹配,
         *                其中pattern中可以使用通配符 * ? 等,
         *                * 表示>=0个字符
         *                ？ 表示有且只有一个字符
         *                此处的匹配规则与{@link KeyOps#keys(String)}处的一样。
         * @return 匹配到的(key对应的)set中的项
         */
        public static Cursor<String> sScan(String key, ScanOptions options) {
            Cursor<String> cursor = redisTemplate.opsForSet().scan(key, options);
            return cursor;
        }
    }

    /**
     * ZSet相关操作
     * <p>
     * 特别说明: ZSet是有序的,
     * 不仅体现在： redis中的存储上有序。
     * 还体现在:   此工具类ZSetOps中返回值类型为Set<?>的方法, 实际返回类型是LinkedHashSet<?>
     * <p>
     * 提示: redis中的ZSet, 一定程度等于redis中的Set + redis中的Hash的结合体。
     * 提示: redis中String的数据结构可参考resources/data-structure/ZSet(有序集合)的数据结构(示例一).png
     * redis中String的数据结构可参考resources/data-structure/ZSet(有序集合)的数据结构(示例二).png
     * 提示: ZSet中的entryKey即为成员项， entryValue即为这个成员项的分值, ZSet根据成员的分值，来堆成员进行排序。
     */
    public static class ZSetOps {

        /**
         * 向(key对应的)zset中添加(item, score)
         * <p>
         * 注: item为entryKey成员项， score为entryValue分数值。
         * <p>
         * 注: 若(key对应的)zset中已存在(与此次要添加的项)相同的item项，那么此次添加操作会失败，返回false；
         * 但是！！！ zset中原item的score会被更新为此次add的相同item项的score。
         * 所以, 也可以通过zAdd达到更新item对应score的目的。
         * <p>
         * 注: score可为正、可为负、可为0; 总之, double范围内都可以。
         * <p>
         * 注: 若score的值一样，则按照item排序。
         *
         * @param key   定位set的key
         * @param item  要往(key对应的)zset中添加的成员项
         * @param score item的分值
         * @return 是否添加成功
         */
        public static boolean zAdd(String key, String item, double score) {
            Boolean result = redisTemplate.opsForZSet().add(key, item, score);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 批量添加entry<item, score>
         * <p>
         * 注: 若entry<item, score>集中存在item相同的项(, score不一样)，那么redis在执行真正的批量add操作前,会
         * 将其中一个item过滤掉。
         * 注: 同样的，若(key对应的)zset中已存在(与此次要添加的项)相同的item项，那么此次批量添加操作中，
         * 对该item项的添加会失败，会失败，成功计数器不会加1；但是！！！ zset中原item的score会被更新为此
         * 次add的相同item项的score。所以, 也可以通过zAdd达到更新item对应score的目的。
         *
         * @param key     定位set的key
         * @param entries 要添加的entry<item, score>集
         * @return 本次添加进(key对应的)zset中的entry的个数
         */
        public static long zAdd(String key, Set<TypedTuple<String>> entries) {
            Long count = redisTemplate.opsForZSet().add(key, entries);
            if (count == null) {
                throw new RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 从(key对应的)zset中移除项
         * <p>
         * 注:若key不存在，则返回0
         *
         * @param key   定位set的key
         * @param items 要移除的项集
         * @return 实际移除了的项的个数
         */
        public static long zRemove(String key, Object... items) {
            Long count = redisTemplate.opsForZSet().remove(key, items);
            if (count == null) {
                throw new RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 移除(key对应的)zset中, 排名范围在[startIndex, endIndex]内的item
         * <p>
         * 注:默认的，按score.item升序排名， 排名从0开始
         * <p>
         * 注: 类似于List中的索引, 排名可以分为多个方式:
         * 从前到后(正向)的排名: 0、1、2...
         * 从后到前(反向)的排名: -1、-2、-3...
         * <p>
         * 注: 不论是使用正向排名，还是使用反向排名, 使用此方法时, 应保证 startRange代表的元素的位置
         * 在endRange代表的元素的位置的前面， 如:
         * 示例一: RedisUtil.ZSetOps.zRemoveRange("name", 0, 2);
         * 示例二: RedisUtil.ZSetOps.zRemoveRange("site", -2, -1);
         * 示例三: RedisUtil.ZSetOps.zRemoveRange("foo", 0, -1);
         * <p>
         * 注:若key不存在，则返回0
         *
         * @param key        定位set的key
         * @param startRange 开始项的排名
         * @param endRange   结尾项的排名
         * @return 实际移除了的项的个数
         */
        public static long zRemoveRange(String key, long startRange, long endRange) {
            Long count = redisTemplate.opsForZSet().removeRange(key, startRange, endRange);
            if (count == null) {
                throw new RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 移除(key对应的)zset中, score范围在[minScore, maxScore]内的item
         * <p>
         * 提示: 虽然删除范围包含两侧的端点(即:包含minScore和maxScore), 但是由于double存在精度问题，所以建议:
         * 设置值时，minScore应该设置得比要删除的项里，最小的score还小一点
         * maxScore应该设置得比要删除的项里，最大的score还大一点
         * 追注: 本人简单测试了几组数据，暂未出现精度问题。
         * <p>
         * 注:若key不存在，则返回0
         *
         * @param key      定位set的key
         * @param minScore score下限(含这个值)
         * @param maxScore score上限(含这个值)
         * @return 实际移除了的项的个数
         */
        public static long zRemoveRangeByScore(String key, double minScore, double maxScore) {
            Long count = redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
            if (count == null) {
                throw new RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 增/减 (key对应的zset中,)item的分数值
         *
         * @param key   定位zset的key
         * @param item  项
         * @param delta 变化量(正 - 增, 负 - 减)
         * @return 修改后的score值
         */
        public static double zIncrementScore(String key, String item, double delta) {
            Double scoreValue = redisTemplate.opsForZSet().incrementScore(key, item, delta);
            if (scoreValue == null) {
                throw new RedisOpsResultIsNullException();
            }
            return scoreValue;
        }

        /**
         * 返回item在(key对应的)zset中的(按score从小到大的)排名
         * <p>
         * 注: 排名从0开始。 即意味着，此方法等价于: 返回item在(key对应的)zset中的位置索引。
         * 注: 若key或item不存在， 返回null。
         * 注: 排序规则是score,item, 即:优先以score排序，若score相同，则再按item排序。
         *
         * @param key  定位zset的key
         * @param item 项
         * @return 排名(等价于 : 索引)
         */
        public static long zRank(String key, Object item) {
            Long rank = redisTemplate.opsForZSet().rank(key, item);
            if (rank == null) {
                throw new RedisOpsResultIsNullException();
            }
            return rank;
        }

        /**
         * 返回item在(key对应的)zset中的(按score从大到小的)排名
         * <p>
         * 注: 排名从0开始。补充: 因为是按score从大到小排序的, 所以最大score对应的item的排名为0。
         * 注: 若key或item不存在， 返回null。
         * 注: 排序规则是score,item, 即:优先以score排序，若score相同，则再按item排序。
         *
         * @param key  定位zset的key
         * @param item 项
         * @return 排名(等价于 : 索引)
         */
        public static long zReverseRank(String key, Object item) {
            Long reverseRank = redisTemplate.opsForZSet().reverseRank(key, item);
            if (reverseRank == null) {
                throw new RedisOpsResultIsNullException();
            }
            return reverseRank;
        }

        /**
         * 根据索引位置， 获取(key对应的)zset中排名处于[start, end]中的item项集
         * <p>
         * 注: 不论是使用正向排名，还是使用反向排名, 使用此方法时, 应保证 startIndex代表的元素的
         * 位置在endIndex代表的元素的位置的前面， 如:
         * 示例一: RedisUtil.ZSetOps.zRange("name", 0, 2);
         * 示例二: RedisUtil.ZSetOps.zRange("site", -2, -1);
         * 示例三: RedisUtil.ZSetOps.zRange("foo", 0, -1);
         * <p>
         * 注: 若key不存在, 则返回空的集合。
         * <p>
         * 注: 当[start, end]的范围比实际zset的范围大时, 返回范围上"交集"对应的项集合。
         *
         * @param key   定位zset的key
         * @param start 排名开始位置
         * @param end   排名结束位置
         * @return 对应的item项集
         */
        public static Set<String> zRange(String key, long start, long end) {
            Set<String> result = redisTemplate.opsForZSet().range(key, start, end);
            return result;
        }

        /**
         * 获取(key对应的)zset中的所有item项
         *
         * @param key 定位zset的键
         * @return (key对应的)zset中的所有item项
         * @date 2020/3/12 10:02:07
         * @see ZSetOps#zRange(String, long, long)
         */
        public static Set<String> zWholeZSetItem(String key) {
            Set<String> result = redisTemplate.opsForZSet().range(key, 0, -1);
            return result;
        }

        /**
         * 根据索引位置， 获取(key对应的)zset中排名处于[start, end]中的entry集
         * <p>
         * 注: 不论是使用正向排名，还是使用反向排名, 使用此方法时, 应保证 startIndex代表的元素的
         * 位置在endIndex代表的元素的位置的前面， 如:
         * 示例一: RedisUtil.ZSetOps.zRange("name", 0, 2);
         * 示例二: RedisUtil.ZSetOps.zRange("site", -2, -1);
         * 示例三: RedisUtil.ZSetOps.zRange("foo", 0, -1);
         * <p>
         * 注: 若key不存在, 则返回空的集合。
         * <p>
         * 注: 当[start, end]的范围比实际zset的范围大时, 返回范围上"交集"对应的项集合。
         * <p>
         * 注: 此方法和{@link ZSetOps#zRange(String, long, long)}类似，不过此方法返回的不是item集， 而是entry集
         *
         * @param key   定位zset的key
         * @param start 排名开始位置
         * @param end   排名结束位置
         * @return 对应的entry集
         */
        public static Set<TypedTuple<String>> zRangeWithScores(String key, long start, long end) {
            Set<TypedTuple<String>> entries = redisTemplate.opsForZSet().rangeWithScores(key, start, end);
            return entries;
        }

        /**
         * 获取(key对应的)zset中的所有entry
         *
         * @param key 定位zset的键
         * @return (key对应的)zset中的所有entry
         * @see ZSetOps#zRangeWithScores(String, long, long)
         */
        public static Set<TypedTuple<String>> zWholeZSetEntry(String key) {
            Set<TypedTuple<String>> entries = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
            return entries;
        }

        /**
         * 根据score， 获取(key对应的)zset中分数值处于[minScore, maxScore]中的item项集
         * <p>
         * 注: 若key不存在, 则返回空的集合。
         * 注: 当[minScore, maxScore]的范围比实际zset中score的范围大时, 返回范围上"交集"对应的项集合。
         * <p>
         * 提示: 虽然删除范围包含两侧的端点(即:包含minScore和maxScore), 但是由于double存在精度问题，所以建议:
         * 设置值时，minScore应该设置得比要删除的项里，最小的score还小一点
         * maxScore应该设置得比要删除的项里，最大的score还大一点
         * 追注: 本人简单测试了几组数据，暂未出现精度问题。
         *
         * @param key      定位zset的key
         * @param minScore score下限
         * @param maxScore score上限
         * @return 对应的item项集
         */
        public static Set<String> zRangeByScore(String key, double minScore, double maxScore) {
            Set<String> items = redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
            return items;
        }

        /**
         * 根据score， 获取(key对应的)zset中分数值处于[minScore, maxScore]中的, score处于[minScore,
         * 排名大于等于offset的count个item项
         * <p>
         * 特别注意: 对于不是特别熟悉redis的人来说, offset 和 count最好都使用正数， 避免引起理解上的歧义。
         * <p>
         * 注: 若key不存在, 则返回空的集合。
         * <p>
         * 提示: 虽然删除范围包含两侧的端点(即:包含minScore和maxScore), 但是由于double存在精度问题，所以建议:
         * 设置值时，minScore应该设置得比要删除的项里，最小的score还小一点
         * maxScore应该设置得比要删除的项里，最大的score还大一点
         * 追注: 本人简单测试了几组数据，暂未出现精度问题。
         *
         * @param key      定位zset的key
         * @param minScore score下限
         * @param maxScore score上限
         * @param offset   偏移量(即:排名下限)
         * @param count    期望获取到的元素个数
         * @return 对应的item项集
         */
        public static Set<String> zRangeByScore(String key, double minScore, double maxScore,
                                                long offset, long count) {
            Set<String> items = redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore, offset, count);
            return items;
        }

        /**
         * 获取(key对应的)zset中的所有score处于[minScore, maxScore]中的entry
         *
         * @param key      定位zset的键
         * @param minScore score下限
         * @param maxScore score上限
         * @return (key对应的)zset中的所有score处于[minScore, maxScore]中的entry
         * @see ZSetOps#zRangeByScore(String, double, double)
         * <p>
         * 注: 若key不存在, 则返回空的集合。
         * 注: 当[minScore, maxScore]的范围比实际zset中score的范围大时, 返回范围上"交集"对应的项集合。
         */
        public static Set<TypedTuple<String>> zRangeByScoreWithScores(String key, double minScore, double maxScore) {
            Set<TypedTuple<String>> entries = redisTemplate.opsForZSet().rangeByScoreWithScores(key, minScore, maxScore);
            return entries;
        }

        /**
         * 获取(key对应的)zset中, score处于[minScore, maxScore]里的、排名大于等于offset的count个entry
         * <p>
         * 特别注意: 对于不是特别熟悉redis的人来说, offset 和 count最好都使用正数， 避免引起理解上的歧义。
         *
         * @param key      定位zset的键
         * @param minScore score下限
         * @param maxScore score上限
         * @param offset   偏移量(即:排名下限)
         * @param count    期望获取到的元素个数
         * @return [startIndex, endIndex] & [minScore, maxScore]里的entry
         */
        public static Set<TypedTuple<String>> zRangeByScoreWithScores(String key, double minScore,
                                                                      double maxScore, long offset,
                                                                      long count) {
            Set<TypedTuple<String>> entries = redisTemplate.opsForZSet().rangeByScoreWithScores(key, minScore,
                    maxScore, offset, count);
            return entries;
        }


        /**
         * 获取时, 先按score倒序, 然后根据索引位置， 获取(key对应的)zset中排名处于[start, end]中的item项集
         *
         * @see ZSetOps#zRange(String, long, long)。 只是zReverseRange这里会提前多一个倒序。
         */
        public static Set<String> zReverseRange(String key, long start, long end) {
            Set<String> entries = redisTemplate.opsForZSet().reverseRange(key, start, end);
            return entries;
        }

        /**
         * 获取时, 先按score倒序, 然后根据索引位置， 获取(key对应的)zset中排名处于[start, end]中的entry集
         *
         * @see ZSetOps#zRangeWithScores(String, long, long)。 只是zReverseRangeWithScores这里会提前多一个倒序。
         */
        public static Set<TypedTuple<String>> zReverseRangeWithScores(String key, long start, long end) {
            Set<TypedTuple<String>> entries = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
            return entries;
        }

        /**
         * 获取时, 先按score倒序, 然后根据score， 获取(key对应的)zset中分数值处于[minScore, maxScore]中的item项集
         *
         * @see ZSetOps#zRangeByScore(String, double, double)。 只是zReverseRangeByScore这里会提前多一个倒序。
         */
        public static Set<String> zReverseRangeByScore(String key, double minScore, double maxScore) {
            Set<String> items = redisTemplate.opsForZSet().reverseRangeByScore(key, minScore, maxScore);
            return items;
        }

        /**
         * 获取时, 先按score倒序, 然后获取(key对应的)zset中的所有score处于[minScore, maxScore]中的entry
         *
         * @see ZSetOps#zRangeByScoreWithScores(String, double, double)。 只是zReverseRangeByScoreWithScores这里会提前多一个倒序。
         */
        public static Set<TypedTuple<String>> zReverseRangeByScoreWithScores(String key, double minScore, double maxScore) {
            Set<TypedTuple<String>> entries = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,
                    minScore, maxScore);
            return entries;
        }

        /**
         * 获取时, 先按score倒序, 然后根据score， 获取(key对应的)zset中分数值处于[minScore, maxScore]中的,
         * score处于[minScore,排名大于等于offset的count个item项
         *
         * @see ZSetOps#zRangeByScore(String, double, double, long, long)。 只是zReverseRangeByScore这里会提前多一个倒序。
         */
        public static Set<String> zReverseRangeByScore(String key, double minScore, double maxScore, long offset, long count) {
            Set<String> items = redisTemplate.opsForZSet().reverseRangeByScore(key, minScore, maxScore, offset, count);
            return items;
        }

        /**
         * 统计(key对应的zset中)score处于[minScore, maxScore]中的item的个数
         *
         * @param key      定位zset的key
         * @param minScore score下限
         * @param maxScore score上限
         * @return [minScore, maxScore]中item的个数
         */
        public static long zCount(String key, double minScore, double maxScore) {
            Long count = redisTemplate.opsForZSet().count(key, minScore, maxScore);
            if (count == null) {
                throw new RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 统计(key对应的)zset中item的个数
         * <p>
         * 注: 此方法等价于{@link ZSetOps#zZCard(String)}
         *
         * @param key 定位zset的key
         * @return zset中item的个数
         */
        public static long zSize(String key) {
            Long size = redisTemplate.opsForZSet().size(key);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 统计(key对应的)zset中item的个数
         * <p>
         * 注: 此方法等价于{@link ZSetOps#zSize(String)}
         *
         * @param key 定位zset的key
         * @return zset中item的个数
         */
        public static long zZCard(String key) {
            Long size = redisTemplate.opsForZSet().zCard(key);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 统计(key对应的)zset中指定item的score
         *
         * @param key  定位zset的key
         * @param item zset中的item
         * @return item的score
         */
        public static double zScore(String key, Object item) {
            Double score = redisTemplate.opsForZSet().score(key, item);
            if (score == null) {
                throw new RedisOpsResultIsNullException();
            }
            return score;
        }

        /**
         * 获取两个(key对应的)ZSet的并集, 并将结果add到storeKey对应的ZSet中。
         * <p>
         * 注: 和set一样，zset中item是唯一的， 在多个zset进行Union时, 处理相同的item时， score的值会变为对应的score之和，如：
         * RedisUtil.ZSetOps.zAdd("name1", "a", 1);和RedisUtil.ZSetOps.zAdd("name2", "a", 2);
         * 对(name1和name2对应的)zset进行zUnionAndStore之后，新的zset中的项a,对应的score值为3
         * <p>
         * case1: 交集不为空, storeKey不存在， 则 会创建对应的storeKey，并将并集添加到(storeKey对应的)ZSet中
         * case2: 交集不为空, storeKey已存在， 则 会清除原(storeKey对应的)ZSet中所有的项，然后将并集添加到(storeKey对应的)ZSet中
         * case3: 交集为空, 则不进行下面的操作, 直接返回0
         *
         * @param key      定位其中一个zset的键
         * @param otherKey 定位另外的zset的键
         * @param storeKey 定位(要把交集添加到哪个)set的key
         * @return add到(storeKey对应的)ZSet后, 该ZSet对应的size
         */
        public static long zUnionAndStore(String key, String otherKey, String storeKey) {
            Long size = redisTemplate.opsForZSet().unionAndStore(key, otherKey, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 获取两个(key对应的)ZSet的并集, 并将结果add到storeKey对应的ZSet中。
         * <p>
         * 注: 和set一样，zset中item是唯一的， 在多个zset进行Union时, 处理相同的item时， score的值会变为对应的score之和，如：
         * RedisUtil.ZSetOps.zAdd("name1", "a", 1);和RedisUtil.ZSetOps.zAdd("name2", "a", 2);
         * 对(name1和name2对应的)zset进行zUnionAndStore之后，新的zset中的项a,对应的score值为3
         * <p>
         * case1: 并集不为空, storeKey不存在， 则 会创建对应的storeKey，并将并集添加到(storeKey对应的)ZSet中
         * case2: 并集不为空, storeKey已存在， 则 会清除原(storeKey对应的)ZSet中所有的项，然后将并集添加到(storeKey对应的)ZSet中
         * case3: 并集为空, 则不进行下面的操作, 直接返回0
         *
         * @param key       定位其中一个set的键
         * @param otherKeys 定位其它set的键集
         * @param storeKey  定位(要把并集添加到哪个)set的key
         * @return add到(storeKey对应的)ZSet后, 该ZSet对应的size
         */
        public static long zUnionAndStore(String key, Collection<String> otherKeys, String storeKey) {
            Long size = redisTemplate.opsForZSet().unionAndStore(key, otherKeys, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 获取两个(key对应的)ZSet的交集, 并将结果add到storeKey对应的ZSet中。
         * <p>
         * 注: 和set一样，zset中item是唯一的， 在多个zset进行Intersect时, 处理相同的item时， score的值会变为对应的score之和，如：
         * RedisUtil.ZSetOps.zAdd("name1", "a", 1);
         * RedisUtil.ZSetOps.zAdd("name1", "b", 100);
         * 和R
         * edisUtil.ZSetOps.zAdd("name2", "a", 2);
         * edisUtil.ZSetOps.zAdd("name2", "c", 200);
         * 对(name1和name2对应的)zset进行zIntersectAndStore之后，新的zset中的项a,对应的score值为3
         * <p>
         * case1: 交集不为空, storeKey不存在， 则 会创建对应的storeKey，并将交集添加到(storeKey对应的)ZSet中
         * case2: 交集不为空, storeKey已存在， 则 会清除原(storeKey对应的)ZSet中所有的项，然后将交集添加到(storeKey对应的)ZSet中
         * case3: 交集为空, 则不进行下面的操作, 直接返回0
         *
         * @param key      定位其中一个ZSet的键
         * @param otherKey 定位其中另一个ZSet的键
         * @param storeKey 定位(要把交集添加到哪个)ZSet的key
         * @return add到(storeKey对应的)ZSet后, 该ZSet对应的size
         */
        public static long zIntersectAndStore(String key, String otherKey, String storeKey) {
            Long size = redisTemplate.opsForZSet().intersectAndStore(key, otherKey, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 获取多个(key对应的)ZSet的交集, 并将结果add到storeKey对应的ZSet中。
         * <p>
         * case1: 交集不为空, storeKey不存在， 则 会创建对应的storeKey，并将交集添加到(storeKey对应的)ZSet中
         * case2: 交集不为空, storeKey已存在， 则 会清除原(storeKey对应的)ZSet中所有的项，然后将交集添加到(storeKey对应的)ZSet中
         * case3: 交集为空, 则不进行下面的操作, 直接返回0
         *
         * @param key       定位其中一个set的键
         * @param otherKeys 定位其它set的键集
         * @param storeKey  定位(要把并集添加到哪个)set的key
         * @return add到(storeKey对应的)ZSet后, 该ZSet对应的size
         */
        public static long zIntersectAndStore(String key, Collection<String> otherKeys, String storeKey) {
            Long size = redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, storeKey);
            if (size == null) {
                throw new RedisOpsResultIsNullException();
            }
            return size;
        }
    }

    /**
     * redis分布式锁(单机版).
     * <p>
     * 使用方式(示例):
     * boolean flag = false;
     * String lockName = "sichuan:mianyang:fucheng:ds";
     * String lockValue = UUID.randomUUID().toString();
     * try {
     * //	非阻塞获取(锁的最大存活时间采用默认值)
     * flag = RedisUtil.LockOps.getLock(lockName, lockValue);
     * //	非阻塞获取e.g.
     * flag = RedisUtil.LockOps.getLock(lockName, lockValue, 3, TimeUnit.SECONDS);
     * // 阻塞获取(锁的最大存活时间采用默认值)
     * flag = RedisUtil.LockOps.getLockUntilTimeout(lockName, lockValue, 2000);
     * // 阻塞获取e.g.
     * flag = RedisUtil.LockOps.getLockUntilTimeout(lockName, lockValue, 2, TimeUnit.SECONDS, 2000);
     * if (!flag) {
     * throw new RuntimeException(" obtain redis-lock[" + lockName + "] fail");
     * }
     * //	...
     * } finally {
     * if (flag) {
     * RedisUtil.LockOps.releaseLock(lockName, lockValue);
     * }
     * }
     * <p>
     * |--------------------------------------------------------------------------------------------------------------------|
     * |单机版分布式锁、集群版分布式锁，特别说明:                                                                                 |
     * |   - 此锁是针对单机Redis的分布式锁;                                                                                    |
     * |   - 对于Redis集群而言, 此锁可能存在失效的情况。考虑如下情况:                                                              |
     * |         首先，当客户端A通过key-value(假设为key名为key123)在Master上获取到一个锁。                                        |
     * |         然后，Master试着把这个数据同步到Slave的时候突然挂了(此时Slave上没有该分布式锁的key123)。                            |
     * |         接着，Slave变成了Master。                                                                                    |
     * |         不巧的是，客户端B此时也一以相同的key去获取分布式锁；                                                              |
     * |                 因为现在的Master上没有key123代表的分布式锁，                                                            |
     * |                 所以客户端B此时再通过key123去获取分布式锁时，                                                            |
     * |                 就能获取成功。                                                                                       |
     * |         那么此时，客户端A和客户端B同时获取到了同一把分布式锁，分布式锁失效。                                                 |
     * |   - 在Redis集群模式下，如果需要严格的分布式锁的话，可使用Redlock算法来实现。Redlock算法原理简述:                              |
     * |     - 获取分布式锁：                                                                                                 |
     * |           1. 客户端获取服务器当前的的时间t0。                                                                           |
     * |           2. 使用相同的key和value依次向5个实例获取锁。                                                                  |
     * |              注:为了避免在某个redis节点耗时太久而影响到对后面的Redis节点的锁的获取;                                         |
     * |                 客户端在获取每一个Redis节点的锁的时候,自身需要设置一个较小的等待获取锁超时的时间,                             |
     * |                 一旦都在某个节点获取分布式锁的时间超过了超时时间，那么就认为在这个节点获取分布式锁失败，                        |
     * |                 （不把时间浪费在这一个节点上），继续获取下一个节点的分布式锁。                                              |
     * |           3. 客户端通过当前时间(t1)减去t0，计算(从所有redis节点)获取锁所消耗的总时间t2(注：t2=t1-t0)。                      |
     * |              只有t2小于锁本身的锁定时长(注:若锁的锁定时长是1小时， 假设下午一点开始上锁，那么锁会在下午两点                     |
     * |              的时候失效， 而你却在两点后才获取到锁，这个时候已经没意义了)，并且，客户端在至少在多半Redis                        |
     * |              节点上获取到锁, 我们才认为分布式锁获取成功。                                                                |
     * |           5. 如果锁已经获取，那么  锁的实际有效时长 = 锁的总有效时长 - 获取分布式锁所消耗的时长; 锁的实际有效时长 应保证 > 0。    |
     * |              注: 也就是说， 如果获取锁失败，那么                                                                        |
     * |                  A. 可能是   获取到的锁的个数，不满足大多数原则。                                                         |
     * |                  B. 也可能是 锁的实际有效时长不大于0。                                                                  |
     * |      - 释放分布式锁： 在每个redis节点上试着删除锁(, 不论有没有在该节点上获取到锁)。                                          |
     * |   - 集群下的分布式锁，可直接使用现有类库<a href="https://github.com/redisson/redisson"/>                                |
     * |                                                                                                                    |
     * |   注: 如果Redis集群项目能够容忍master宕机导致单机版分布式锁失效的情况的话，那么是直接使用单机版分布式锁在Redis集群的项目中的；     |
     * |       如果Redis集群项目不能容忍单机版分布式锁失效的情况的话，那么请使用基于RedLock算法的集群版分布式锁；                        |
     * |--------------------------------------------------------------------------------------------------------------------|
     */
    public static class LockOps {

        /**
         * 分布式锁默认(最大)存活时长
         */
        public static final long DEFAULT_LOCK_TIMEOUT = 3;
        /**
         * DEFAULT_LOCK_TIMEOUT的单位
         */
        public static final TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.SECONDS;
        /**
         * lua脚本, 保证 释放锁脚本 的原子性(以避免, 并发场景下, 释放了别人的锁)
         */
        private static final String RELEASE_LOCK_LUA;

        static {
            // 不论lua中0是否代表失败; 对于java的Boolean而言, 返回0, 则会被解析为false
            RELEASE_LOCK_LUA = "if redis.call('get',KEYS[1]) == ARGV[1] "
                    + "then "
                    + "    return redis.call('del',KEYS[1]) "
                    + "else "
                    + "    return 0 "
                    + "end ";
        }

        /**
         * 获取(分布式)锁.
         * <p>
         * 注: 获取结果是即时返回的、是非阻塞的。
         *
         * @see LockOps#getLock(String, String, long, TimeUnit)
         */
        public static boolean getLock(final String key, final String value) {
            return getLock(key, value, DEFAULT_LOCK_TIMEOUT, DEFAULT_TIMEOUT_UNIT);
        }

        /**
         * 获取(分布式)锁。
         * 若成功, 则直接返回;
         * 若失败, 则进行重试, 直到成功 或 超时为止。
         * <p>
         * 注: 获取结果是阻塞的， 要么成功, 要么超时, 才返回。
         *
         * @param retryTimeoutLimit 重试的超时时长(ms)
         *                          其它参数可详见:
         * @return 是否成功
         * @see LockOps#getLock(String, String, long, TimeUnit)
         */
        public static boolean getLockUntilTimeout(final String key, final String value,
                                                  final long retryTimeoutLimit) {
            return getLockUntilTimeout(key, value, DEFAULT_LOCK_TIMEOUT, DEFAULT_TIMEOUT_UNIT, retryTimeoutLimit);
        }

        /**
         * 获取(分布式)锁。
         * 若成功, 则直接返回;
         * 若失败, 则进行重试, 直到成功 或 超时为止。
         * <p>
         * 注: 获取结果是阻塞的， 要么成功, 要么超时, 才返回。
         *
         * @param retryTimeoutLimit 重试的超时时长(ms)
         *                          其它参数可详见:
         * @return 是否成功
         * @see LockOps#getLock(String, String, long, TimeUnit, boolean)
         */
        public static boolean getLockUntilTimeout(final String key, final String value,
                                                  final long timeout, final TimeUnit unit,
                                                  final long retryTimeoutLimit) {
            long startTime = Instant.now().toEpochMilli();
            long now = startTime;
            do {
                try {
                    boolean alreadyGotLock = getLock(key, value, timeout, unit, false);
                    if (alreadyGotLock) {
                        return true;
                    }
                } catch (Exception e) {
                }
                now = Instant.now().toEpochMilli();
            } while (now < startTime + retryTimeoutLimit);
            return false;
        }

        /**
         * 获取(分布式)锁
         * <p>
         * 注: 获取结果是即时返回的、是非阻塞的。
         *
         * @see LockOps#getLock(String, String, long, TimeUnit, boolean)
         */
        public static boolean getLock(final String key, final String value,
                                      final long timeout, final TimeUnit unit) {
            return getLock(key, value, timeout, unit, true);
        }

        /**
         * 获取(分布式)锁
         * <p>
         * 注: 获取结果是即时返回的、是非阻塞的。
         *
         * @param key     锁名
         * @param value   锁名对应的value
         *                注: value一般采用全局唯一的值， 如: requestId、uuid等。
         *                这样， 释放锁的时候, 可以再次验证value值,
         *                保证自己上的锁只能被自己释放, 而不会被别人释放。
         *                当然, 如果锁超时时, 会被redis自动删除释放。
         * @param timeout 锁的(最大)存活时长
         *                注: 一般的， 获取锁与释放锁 都是成对使用的, 在锁在达到(最大)存活时长之前，都会被主动释放。
         *                但是在某些情况下(如:程序获取锁后,释放锁前,崩了),锁得不到释放, 这时就需要等锁过
         *                了(最大)存活时长后，被redis自动删除清理了。这样就能保证redis中不会留下死数据。
         * @param unit    timeout的单位
         * @return 是否成功
         */
        public static boolean getLock(final String key, final String value,
                                      final long timeout, final TimeUnit unit,
                                      boolean recordLog) {
            if (recordLog) {
                log.info("getLock(...) => key -> {}, value -> {}, timeout -> {}, unit -> {}, recordLog -> {}",
                        key, value, timeout, unit, recordLog);
            }
            Boolean result = redisTemplate.execute((RedisConnection connection) ->
                    connection.set(key.getBytes(StandardCharsets.UTF_8),
                            value.getBytes(StandardCharsets.UTF_8),
                            Expiration.seconds(unit.toSeconds(timeout)),
                            RedisStringCommands.SetOption.SET_IF_ABSENT)
            );
            if (recordLog) {
                log.info("getLock(...) => result -> {}", result);
            }
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
    }

    /**
     * 释放(分布式)锁
     * <p>
     * 注: 此方式能(通过value的唯一性)保证: 自己加的锁, 只能被自己释放。
     * 注: 锁超时时, 也会被redis自动删除释放。
     *
     * @param key   锁名
     * @param value 锁名对应的value
     * @return 释放锁是否成功
     */
    public static boolean releaseLock(final String key, final String value) {
        Boolean result = redisTemplate.execute((RedisConnection connection) ->
                connection.eval(RELEASE_LOCK_LUA.getBytes(),
                        ReturnType.BOOLEAN, 1,
                        key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8))
        );
        if (result == null) {
            throw new RedisOpsResultIsNullException();
        }
        return result;
    }

    /**
     * 释放锁, 不校验该key对应的value值
     * <p>
     * 注: 此方式释放锁，可能导致: 自己加的锁, 结果被别人释放了。
     * 所以不建议使用此方式释放锁。
     *
     * @param key 锁名
     */
    @Deprecated
    public static void releaseLock(final String key) {
        KeyOps.delete(key);
    }
}

/**
 * 当使用Pipeline 或 Transaction操作redis时, (不论redis中实际操作是否成功, 这里)结果(都)会返回null。
 * 此时，如果试着将null转换为基本类型的数据时，会抛出此异常。
 * <p>
 * 即: 此工具类中的某些方法, 希望不要使用Pipeline或Transaction操作redis。
 * <p>
 * 注: Pipeline 或 Transaction默认是不启用的， 可详见源码:
 *
 * @see LettuceConnection#isPipelined()
 * @see LettuceConnection#isQueueing()
 * @see JedisConnection#isPipelined()
 * @see JedisConnection#isQueueing()
 */
public static class RedisOpsResultIsNullException extends NullPointerException {

    public RedisOpsResultIsNullException() {
        super();
    }

    public RedisOpsResultIsNullException(String message) {
        super(message);
    }
}

/**
 * 提供一些基础功能支持
 */
public static class Helper {

    /**
     * 默认拼接符
     */
    public static final String DEFAULT_SYMBOL = ":";

    /**
     * 拼接args
     *
     * @see Helper#joinBySymbol(String, String...)
     */
    public static String join(String... args) {
        return Helper.joinBySymbol(DEFAULT_SYMBOL, args);
    }

    /**
     * 使用symbol拼接args
     *
     * @param symbol 分隔符， 如: 【:】
     * @param args   要拼接的元素数组, 如: 【a b c】
     * @return 拼接后的字符串, 如  【a:b:c】
     */
    public static String joinBySymbol(String symbol, String... args) {
        if (symbol == null || symbol.trim().length() == 0) {
            throw new RuntimeException(" symbol must not be emptyResult!");
        }
        if (args == null || args.length == 0) {
            throw new RuntimeException(" args must not be emptyResult!");
        }
        StringBuilder sb = new StringBuilder(16);
        for (String arg : args) {
            sb.append(arg).append(symbol);
        }
        sb.replace(sb.length() - symbol.length(), sb.length(), "");
        return sb.toString();
    }

}
}

