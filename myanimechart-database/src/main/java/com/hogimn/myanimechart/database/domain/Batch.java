package com.hogimn.myanimechart.database.domain;

import com.hogimn.myanimechart.database.dao.BatchDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Batch {
    private String name;
    private String cron;

    public static Batch from(BatchDao batchDao) {
        Batch batch = new Batch();
        batch.setName(batchDao.getName());
        batch.setCron(batchDao.getCron());
        return batch;
    }
}
