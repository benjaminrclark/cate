package org.cateproject.domain.batch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Configurable
public class BatchField implements Comparable<BatchField>
{

	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@ManyToOne
	@NotNull
	private BatchFile file;

	private String term;

	private Integer index;

        private boolean idField;

	private String defaultValue;

        public int compareTo(BatchField other) {
            if ( this.index != null ) {
                if ( other.index == null ) {
                    return -1;
                }  else {
                    return this.index.compareTo(other.index);
                }
            } else {
                if ( other.index == null ) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion()
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version)
	{
		this.version = version;
	}

	/**
	 * @return the file
	 */
	public BatchFile getFile()
	{
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(BatchFile file)
	{
		this.file = file;
	}

	/**
	 * @return the term
	 */
	public String getTerm()
	{
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(String term)
	{
		this.term = term;
	}

	/**
	 * @return the index
	 */
	public Integer getIndex()
	{
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index)
	{
		this.index = index;
	}

	/**
	 * @return the idField
	 */
	public boolean isIdField() {
		return idField;
	}

	/**
	 * @param idField the idField to set
	 */
	public void setIdField(boolean idField) {
		this.idField = idField;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
}
