package com.hogimn.myanimechart.common.util;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

public class CronUtil {
    public static long getPeriodAsSeconds(String cron) {
        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
        Cron cronDef = parser.parse(cron);

        ExecutionTime executionTime = ExecutionTime.forCron(cronDef);
        ZonedDateTime now = DateUtil.now().atZone(ZoneOffset.systemDefault());
        ZonedDateTime nextExecution = executionTime.nextExecution(now).orElse(now);
        Optional<Duration> optional = executionTime.timeToNextExecution(nextExecution);

        if (optional.isPresent()) {
            return optional.get().toSeconds();
        }
        throw new IllegalArgumentException(cron + " is not a valid cron expression");
    }
}
