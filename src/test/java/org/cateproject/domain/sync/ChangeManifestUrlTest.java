package org.cateproject.domain.sync;

import static org.junit.Assert.*;

import java.util.SortedSet;
import java.util.TreeSet;

import org.cateproject.domain.batch.BatchLine;
import org.joda.time.DateTime;

import org.junit.Before;
import org.junit.Test;

public class ChangeManifestUrlTest {

    ChangeManifestUrl changeManifestUrl;
 
    ChangeManifestChange md;

    DateTime lastmod;

    SortedSet<BatchLine> batchLines;

    @Before
    public void setUp() throws Exception {
        md = new ChangeManifestChange();
        lastmod = new DateTime();
        batchLines = new TreeSet<BatchLine>(); 

        changeManifestUrl = new ChangeManifestUrl();
        changeManifestUrl.setMd(md);
        changeManifestUrl.setLoc("loc");
        changeManifestUrl.setLastmod(lastmod);
        changeManifestUrl.setBatchLines(batchLines);
    }

    @Test
    public void testGetMd() {
        assertEquals("md should equal md", md, changeManifestUrl.getMd());
    }

    @Test
    public void testGetLoc() {
        assertEquals("loc should equal 'loc'", "loc", changeManifestUrl.getLoc());
    }

    @Test
    public void testGetLastMod() {
        assertEquals("lastmod should equal lastmod", lastmod, changeManifestUrl.getLastmod());
    }

    @Test
    public void testGetBatchLines() {
        assertEquals("batchLines should equal batchLines", batchLines, changeManifestUrl.getBatchLines());
    }
}
