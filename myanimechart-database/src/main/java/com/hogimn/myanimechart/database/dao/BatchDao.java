package com.hogimn.myanimechart.database.dao;

import com.hogimn.myanimechart.database.domain.Batch;
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

    public static BatchDao from(Batch batch) {
        BatchDao batchDao = new BatchDao();
        batchDao.setName(batch.getName());
        batchDao.setCron(batch.getCron());
        return batchDao;
    }
}
