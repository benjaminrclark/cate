package org.cateproject.batch.sync;

import java.util.List;

import javax.persistence.EntityManager;

import org.cateproject.domain.batch.BatchLine;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class ChangeWriter implements ItemWriter<BatchLine> {

    @Autowired 
    EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void write(List<? extends BatchLine> items) {
        for(BatchLine b : items) {
            switch(b.getChangeManifestUrl().getMd().getChange()) {
                case created:
                    em.persist(b.getEntity());
                    break;
                case updated:
                    em.merge(b.getEntity());
                    break; 
                case deleted:
                    em.remove(b.getEntity());
                    break;
                case skipped:
                    // created and deleted within a chunk, so skip;
            }
        } 

    }
}
