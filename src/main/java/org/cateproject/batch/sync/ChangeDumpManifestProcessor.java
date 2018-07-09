package org.cateproject.batch.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cateproject.domain.batch.BatchLine;
import org.cateproject.domain.sync.ChangeManifestChange;
import org.cateproject.domain.sync.ChangeManifestUrl;
import org.cateproject.repository.jpa.batch.BatchFileRepository;
import org.cateproject.repository.jpa.batch.BatchLineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class ChangeDumpManifestProcessor implements ItemProcessor<ChangeManifestUrl,ChangeManifestUrl> {

    private static final Logger logger = LoggerFactory.getLogger(ChangeDumpManifestProcessor.class);

    @Autowired
    private BatchLineRepository batchLineRepository;

    @Autowired
    private BatchFileRepository batchFileRepository;

    private String datasetIdentifier;

    public ChangeDumpManifestProcessor(String datasetIdentifier) {
        this.datasetIdentifier = datasetIdentifier;
    }

    public ChangeManifestUrl process(ChangeManifestUrl changeManifestUrl) throws RuntimeException {
        logger.debug("ChangeManifestUrl {} {} {}", new Object[] {changeManifestUrl.getLoc(), changeManifestUrl.getLastmod(), changeManifestUrl.getMd()});
        ChangeManifestChange changeManifestChange = changeManifestUrl.getMd();
        if( changeManifestChange != null) {
            logger.info("ChangeManifestChange {} {} {} {} {}", new Object[] {changeManifestChange.getChange(), changeManifestChange.getLength(), changeManifestChange.getPath(), changeManifestChange.getHash(), changeManifestChange.getType()});
            Pattern rangePattern = Pattern.compile("\\/(.*?)#row=(\\d+)\\-(\\d+)");
            Matcher rangeMatcher = rangePattern.matcher(changeManifestChange.getPath());
            if( rangeMatcher.matches() ) {
                logger.info("Resource {} Lines {}-{}", new Object[]{ rangeMatcher.group(1), rangeMatcher.group(2), rangeMatcher.group(3) });
                SortedSet<BatchLine> batchLines = batchLineRepository.findLinesByDatasetIdentifierAndFileLocationAndLineNumbers(datasetIdentifier, rangeMatcher.group(1), Integer.parseInt(rangeMatcher.group(2)), Integer.parseInt(rangeMatcher.group(3)));
                logger.info("BatchLines {}", batchLines);
                if( batchLines.isEmpty()) {
                    throw new RuntimeException(String.format("Resources with path %s not found", changeManifestChange.getPath()));
                } else {
                    changeManifestUrl.setBatchLines(batchLines);
                    return changeManifestUrl;
                }
            }
        }
        return null;
    }
}
