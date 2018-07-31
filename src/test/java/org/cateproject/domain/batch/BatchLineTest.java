package org.cateproject.domain.batch;

import static org.junit.Assert.*;

import org.cateproject.domain.Taxon;
import org.cateproject.domain.sync.ChangeManifestUrl;

import org.junit.Before;
import org.junit.Test;

public class BatchLineTest {

        BatchLine batchLine;

        BatchFile file;

        Taxon entity;

        ChangeManifestUrl changeManifestUrl; 

	@Before
	public void setUp() throws Exception {
            file = new BatchFile();

            entity = new Taxon();

            changeManifestUrl = new ChangeManifestUrl();

            batchLine = new BatchLine();
            batchLine.setId(1L);
            batchLine.setVersion(1);
            batchLine.setLineNumber(1);
            batchLine.setNumberOfColumns(1);
            batchLine.setLine("line");
            batchLine.setFile(file);
            batchLine.setEntity(entity);
            batchLine.setChangeManifestUrl(changeManifestUrl);
        }

	@Test
	public void testGetId() {
		assertEquals("id should equal 1", new Long(1), batchLine.getId());
	}

	@Test
	public void testGetVersion() {
		assertEquals("version should equal 1", new Integer(1), batchLine.getVersion());
	}

	@Test
	public void testGetLineNumber() {
		assertEquals("lineNumber should equal 1", new Integer(1), batchLine.getLineNumber());
	}

	@Test
	public void testGetNumberOfColumns() {
		assertEquals("numberOfColumns should equal 1", new Integer(1), batchLine.getNumberOfColumns());
	}

	@Test
	public void testGetLine() {
		assertEquals("line should equal 'line'", "line", batchLine.getLine());
	}

	@Test
	public void testGetEntity() {
		assertEquals("entity should equal entity", entity, batchLine.getEntity());
	}

	@Test
	public void testGetFile() {
		assertEquals("file should equal file", file, batchLine.getFile());
	}

	@Test
	public void testGetChangeManifestUrl() {
		assertEquals("changeManifestUrl should equal changeManifestUrl", changeManifestUrl, batchLine.getChangeManifestUrl());
	}
}
