package com.hogimn.myanimechart.database.batch.dto;

import com.hogimn.myanimechart.database.batch.dao.BatchDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchDto {
    private String name;
    private String cron;

    public static BatchDto from(BatchDao batchDao) {
        BatchDto batchDto = new BatchDto();
        batchDto.setName(batchDao.getName());
        batchDto.setCron(batchDao.getCron());
        return batchDto;
    }
}
