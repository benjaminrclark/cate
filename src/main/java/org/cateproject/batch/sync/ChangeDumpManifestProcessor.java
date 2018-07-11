package org.cateproject.batch.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cateproject.domain.batch.BatchLine;
import org.cateproject.domain.sync.ChangeManifestChange;
import org.cateproject.domain.sync.ChangeManifestUrl;
import org.cateproject.repository.jpa.batch.BatchFileRepository;
import org.cateproject.repository.jpa.batch.BatchLineRepository;
import org.cateproject.repository.jpa.sync.ChangeManifestUrlRepository;
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

    @Autowired
    private ChangeManifestUrlRepository changeManifestUrlRepository;

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
            Pattern singleRowPattern = Pattern.compile("\\/(.*?)#row=(\\d+)");
            Matcher singleRowMatcher = singleRowPattern.matcher(changeManifestChange.getPath());
            if ( rangeMatcher.matches() ) {
                logger.info("Resource {} Lines {}-{}", new Object[]{ rangeMatcher.group(1), rangeMatcher.group(2), rangeMatcher.group(3) });
                SortedSet<BatchLine> batchLines = batchLineRepository.findLinesByDatasetIdentifierAndFileLocationAndLineNumbers(datasetIdentifier, rangeMatcher.group(1), Integer.parseInt(rangeMatcher.group(2)), Integer.parseInt(rangeMatcher.group(3)));
                logger.info("BatchLines {}", batchLines);
                if( batchLines.isEmpty()) {
                    throw new RuntimeException(String.format("Resources with path %s not found", changeManifestChange.getPath()));
                } else {
                    changeManifestUrl = this.changeManifestUrlRepository.save(changeManifestUrl);
                    changeManifestUrl.setBatchLines(batchLines);
                    for (BatchLine batchLine : batchLines) {
                        batchLine.setChangeManifestUrl(changeManifestUrl);
                    }
                    return changeManifestUrl;
                }
            } else if ( singleRowMatcher.matches() ) {
                logger.info("Resource {} Line {}", new Object[]{ singleRowMatcher.group(1), singleRowMatcher.group(2) });
                BatchLine batchLine = batchLineRepository.findLineByDatasetIdentifierAndFileLocationAndLineNumber(datasetIdentifier, singleRowMatcher.group(1), Integer.parseInt(singleRowMatcher.group(2)));
                SortedSet<BatchLine> batchLines = new TreeSet<BatchLine>();
                batchLines.add(batchLine);
                logger.info("BatchLines {}", batchLines);
                if( batchLines.isEmpty()) {
                    throw new RuntimeException(String.format("Resources with path %s not found", changeManifestChange.getPath()));
                } else {
                    changeManifestUrl = this.changeManifestUrlRepository.save(changeManifestUrl);
                    changeManifestUrl.setBatchLines(batchLines);
                    for (BatchLine btchLine : batchLines) {
                        btchLine.setChangeManifestUrl(changeManifestUrl);
                    }
                    return changeManifestUrl;
                }
            } else {
                throw new RuntimeException(String.format("No matchers for path %s not found", changeManifestChange.getPath()));
            }
        }
        return null;
    }
}
