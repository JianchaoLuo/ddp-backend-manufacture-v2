package cn.hex.ddp.manufacture.infrastructure.common.jsckson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class LongModule extends SimpleModule {
    public LongModule() {
        super();
        addSerializer(Long.class, ToStringSerializer.instance);
        addSerializer(Long.TYPE, ToStringSerializer.instance);
    }
}
