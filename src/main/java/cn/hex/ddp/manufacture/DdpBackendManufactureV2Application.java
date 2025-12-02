package cn.hex.ddp.manufacture;

import com.tangzc.autotable.springboot.EnableAutoTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoTable
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DdpBackendManufactureV2Application {

    public static void main(String[] args) {
        SpringApplication.run(DdpBackendManufactureV2Application.class, args);
    }

}
