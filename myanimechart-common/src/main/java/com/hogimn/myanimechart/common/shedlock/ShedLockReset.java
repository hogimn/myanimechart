package com.hogimn.myanimechart.common.shedlock;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShedLockReset implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public ShedLockReset(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        jdbcTemplate.update("DELETE FROM shedlock");
        System.out.println("ShedLock reset completed!");
    }
}
