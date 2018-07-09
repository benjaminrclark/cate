package org.cateproject.batch.sync;

import java.util.List;

import javax.persistence.EntityManager;

import org.cateproject.domain.batch.BatchLine;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class ChangeWriter implements ItemWriter<BatchLine> {

    @Autowired 
    EntityManager em;

    public void write(List<? extends BatchLine> items) {
        for(BatchLine b : items) {
            switch(b.getChangeManifestUrl().getMd().getChange()) {
                case created:
                    em.persist(b.getEntity());
                case updated:
                    em.merge(b.getEntity());
                case deleted:
                    em.remove(b.getEntity());
                case skipped:
                    // created and deleted within a chunk, so skip;
            }
        } 

    }
}
