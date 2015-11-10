package org.cateproject.batch.multimedia;

import java.io.IOException;
import java.util.List;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.util.MultimediaFile;
import org.cateproject.file.FileTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class MultimediaFileWriter implements ItemWriter<Multimedia> {

    private static final Logger logger = LoggerFactory.getLogger(MultimediaFileWriter.class);

    @Autowired
    private FileTransferService fileTransferService;

    @Override
    public void write(final List<? extends Multimedia> multimedias) throws Exception {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter () {
            @Override
	    public void afterCommit() {
                for(Multimedia multimedia : multimedias) {
                    logger.debug("Writing {}", new Object[]{multimedia.getIdentifier()});
                    for(MultimediaFile multimediaFile : multimedia.getMultimediaFiles()) {
                        try {
                            fileTransferService.copyFileOut(multimediaFile.getFile(), multimediaFile.toString());
                        } catch(IOException ioe) {
                            throw new RuntimeException(ioe);
                        }
                    }
                }
            }
        });
    }
}
