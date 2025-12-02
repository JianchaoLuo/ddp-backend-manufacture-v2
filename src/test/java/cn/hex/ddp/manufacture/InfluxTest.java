package cn.hex.ddp.manufacture;

import cn.hex.ddp.manufacture.infrastructure.influx.repository.InfluxRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InfluxTest {
    @Autowired
    private InfluxRepository influxRepository;


    @Test
    public void test() {
//        InfluxQueryBuilder builder = new InfluxQueryBuilder("hex_bucket");
//        builder.range(20, TimeUnit.DAYS);
//        builder.filter(StrUtil.format("mission == \"{}\"", 3));
//        builder.measurement(CarRealTimePO.class);
//        var r = influxRepository.query(builder.build(true),
//                CarRealTimePO.class
//        );
//        log.info(r);
    }

}
