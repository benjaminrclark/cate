package org.cateproject.domain.sync;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ChangeManifestChangeTest {

    ChangeManifestChange changeManifestChange;

    @Before
    public void setUp() throws Exception {
        changeManifestChange = new ChangeManifestChange();
        changeManifestChange.setChange(ChangeManifestChangeType.created);
        changeManifestChange.setHash("hash");
        changeManifestChange.setLength(0);
        changeManifestChange.setType("type");
        changeManifestChange.setPath("path");
    }

    @Test
    public void testGetChange() {
        assertEquals("change should equal 'created'", ChangeManifestChangeType.created, changeManifestChange.getChange());
    }

    @Test
    public void testGetHash() {
        assertEquals("hash should equal 'hash'", "hash", changeManifestChange.getHash());
    }

    @Test
    public void testGetLength() {
        assertEquals("length should equal 0", new Integer(0), changeManifestChange.getLength());
    }

    @Test
    public void testGetType() {
        assertEquals("type should equal 'type'", "type", changeManifestChange.getType());
    }

    @Test
    public void testGetPath() {
        assertEquals("path should equal 'path'", "path", changeManifestChange.getPath());
    }
}
