package com.github.code.zxs.auth.converter;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class TimeUnitConverter {
    public ChronoUnit convert(TimeUnit unit){
        return ChronoUnit.valueOf(unit.name());
    }
}
