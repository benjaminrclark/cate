package org.cateproject.domain.sync;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;

import org.cateproject.domain.batch.BatchLine;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Configurable
public class ChangeManifestUrl {

    private static Logger logger = LoggerFactory.getLogger(ChangeManifestUrl.class);
    
    @Id
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;

    private String loc;

    @Type(type="dateTimeUserType")
    private DateTime lastmod;

    @Embedded
    private ChangeManifestChange md;
 
    @OneToMany
    @OrderBy("lineNumber ASC") 
    SortedSet<BatchLine> batchLines;

    /**
     * @return the loc
     */
    public String getLoc() {
        return loc;
    }

    /**
     * @param loc the loc to set
     */
    public void setLoc(String loc) {
        this.loc = loc;
    }

    /**
     * @return the lastmod
     */
    public DateTime getLastmod() {
        return lastmod;
    }

    /**
     * @param lastmod the lastmod to set
     */
    public void setLastmod(DateTime lastmod) {
        this.lastmod = lastmod;
    }

    /**
     * @return the md
     */
    public ChangeManifestChange getMd() {
        return md;
    }

    /**
     * @param md the md to set
     */
    public void setMd(ChangeManifestChange md) {
        this.md = md;
	}

	/**
	 * @return the batchLines
	 */
	public SortedSet<BatchLine> getBatchLines() {
		return batchLines;
	}

	/**
	 * @param batchLines the batchLines to set
	 */
	public void setBatchLines(SortedSet<BatchLine> batchLines) {
		this.batchLines = batchLines;
	}
}
