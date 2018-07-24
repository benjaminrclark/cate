package org.cateproject.batch.sync;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.cateproject.domain.Taxon;
import org.cateproject.domain.batch.BatchLine;
import org.cateproject.domain.sync.ChangeManifestChange;
import org.cateproject.domain.sync.ChangeManifestChangeType;
import org.cateproject.domain.sync.ChangeManifestUrl;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class ChangeWriterTest {

    ChangeWriter changeWriter;

    EntityManager entityManager;

    BatchLine batchLine;

    List<BatchLine> items;

    Taxon entity;

    @Before
    public void setUp() throws Exception {
        changeWriter = new ChangeWriter();

        entityManager = EasyMock.createMock(EntityManager.class);
        changeWriter.setEntityManager(entityManager);

        batchLine = new BatchLine();
        batchLine.setChangeManifestUrl(new ChangeManifestUrl());
        batchLine.getChangeManifestUrl().setMd(new ChangeManifestChange());

        entity = new Taxon();
        entity.setTaxonId("identifier");
        batchLine.setEntity(entity);

        items = new ArrayList<BatchLine>();
        items.add(batchLine);
    }

    @Test
    public void testWriteCreated() {
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.created);

        entityManager.persist(EasyMock.eq(entity));

        EasyMock.replay(entityManager);
        changeWriter.write(items);
        EasyMock.verify(entityManager);
    }

    @Test
    public void testWriteUpdated() {
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.updated);

        EasyMock.expect(entityManager.merge(EasyMock.eq(entity))).andReturn(entity);

        EasyMock.replay(entityManager);
        changeWriter.write(items);
        EasyMock.verify(entityManager);
    }

    @Test
    public void testWriteDeleted() {
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.deleted);

        entityManager.remove(EasyMock.eq(entity));

        EasyMock.replay(entityManager);
        changeWriter.write(items);
        EasyMock.verify(entityManager);
    }

    @Test
    public void testWriteSkipped() {
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.skipped);

        EasyMock.replay(entityManager);
        changeWriter.write(items);
        EasyMock.verify(entityManager);
    }
}
