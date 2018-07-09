package org.cateproject.batch.job.darwincore;

import org.cateproject.domain.batch.BatchFile;
import org.cateproject.domain.batch.BatchLine;
import org.cateproject.repository.jpa.batch.BatchFileRepository;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchLineMapper implements LineMapper<BatchLine>, InitializingBean {

    private Long batchFileId;

    private BatchFile batchFile;

    @Autowired
    private BatchFileRepository batchFileRepository;

    public BatchLineMapper(Long batchFileId) {
        this.batchFileId = batchFileId;
    }

    public void afterPropertiesSet() {
        batchFile = batchFileRepository.findOne(batchFileId);
    }

    public BatchLine mapLine(String line, int lineNumber) {
      BatchLine batchLine = new BatchLine();
      batchLine.setFile(batchFile);
      batchLine.setLine(line);
      batchLine.setLineNumber(lineNumber);
      batchLine.setNumberOfColumns(line.split(batchFile.getFieldsTerminatedBy(), -1).length);
      return batchLine; 
    }

}
