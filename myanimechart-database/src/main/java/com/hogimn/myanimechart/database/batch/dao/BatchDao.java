package com.hogimn.myanimechart.database.batch.dao;

import com.hogimn.myanimechart.database.batch.dto.BatchDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "batch")
@Data
public class BatchDao {
    @Id
    private String name;

    private String cron;

    public static BatchDao from(BatchDto batchDto) {
        BatchDao batchDao = new BatchDao();
        batchDao.setName(batchDto.getName());
        batchDao.setCron(batchDto.getCron());
        return batchDao;
    }
}
