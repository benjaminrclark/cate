package org.cateproject.domain.batch;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Configurable
public class BatchFile implements Comparable<BatchFile>
{
	private static final Log logger =
		LogFactory.getLog(BatchFile.class);


	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;
        
        @NotNull
	private String location;

        @NotNull
        private boolean core;

	@ManyToOne
	private BatchDataset dataset;

	@OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
	private Set<BatchField> fields = new HashSet<BatchField>();

        @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
        private Set<BatchLine> lines = new HashSet<BatchLine>();

	private String dateFormat;

	private String encoding;

	private Character fieldsEnclosedBy;

	private String fieldsTerminatedBy;

	private Integer ignoreHeaderLines;

	private String linesTerminatedBy;

	private String rowType;

        @Transient
        public String[] getFieldNames(Integer maxColumns) {
            Integer totalColumns = 0;
            Integer maxIndex = 0;

            // Determine the max index in the metadata
            for(BatchField field : fields) {
                if(field.getIndex() != null && field.getIndex() > maxIndex) {
                        maxIndex = field.getIndex();
                }
            }
 
            // Use the max number of columns from the file if it is greater
            // We might not have field names for all columns
            if((maxIndex + 1) < maxColumns) {
               totalColumns = maxColumns;
            } else {
               totalColumns = maxIndex + 1;
            }

            List<String> names = new ArrayList<String>(totalColumns);
            for(int i = 0; i < totalColumns; i++) {
                names.add("");
            }
              
            for (BatchField field : fields) {
                if(field.getIndex() != null) {
                    names.set(field.getIndex(), field.getTerm());
                }
            }

            logger.info("Archive contains field names " + names);
            return names.toArray(new String[names.size()]);

        }

        @Transient
        public Map<String, String> getDefaultValues() {
        Map<String, String> defaultValues = new HashMap<String, String>();
        for (BatchField field : fields) {
            if (field.getDefaultValue() != null && field.getIndex() == null) {
                defaultValues.put(field.getTerm(), field.getDefaultValue());
            }
        }
        return defaultValues;
    }

        public int compareTo(BatchFile other) {
            return this.location.compareTo(other.location);
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
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}

	/**
	 * @return the core
	 */
	public boolean isCore() {
		return core;
	}

	/**
	 * @param core the core to set
	 */
	public void setCore(boolean core) {
		this.core = core;
	}

	/**
	 * @return the dataset
	 */
	public BatchDataset getDataset()
	{
		return dataset;
	}

	/**
	 * @param dataset the dataset to set
	 */
	public void setDataset(BatchDataset dataset)
	{
		this.dataset = dataset;
	}

	/**
	 * @return the fields
	 */
	public Set<BatchField> getFields()
	{
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Set<BatchField> fields)
	{
		this.fields = fields;
	}

	/**
	 * @return the dateFormat
	 */
	public String getDateFormat()
	{
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding()
	{
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	/**
	 * @return the fieldsEnclosedBy
	 */
	public Character getFieldsEnclosedBy()
	{
		return fieldsEnclosedBy;
	}

	/**
	 * @param fieldsEnclosedBy the fieldsEnclosedBy to set
	 */
	public void setFieldsEnclosedBy(Character fieldsEnclosedBy)
	{
		this.fieldsEnclosedBy = fieldsEnclosedBy;
	}

	/**
	 * @return the fieldsTerminatedBy
	 */
	public String getFieldsTerminatedBy()
	{
		return fieldsTerminatedBy;
	}

	/**
	 * @param fieldsTerminatedBy the fieldsTerminatedBy to set
	 */
	public void setFieldsTerminatedBy(String fieldsTerminatedBy)
	{
		this.fieldsTerminatedBy = fieldsTerminatedBy;
	}

	/**
	 * @return the ignoreHeaderLines
	 */
	public Integer getIgnoreHeaderLines()
	{
		return ignoreHeaderLines;
	}

	/**
	 * @param ignoreHeaderLines the ignoreHeaderLines to set
	 */
	public void setIgnoreHeaderLines(Integer ignoreHeaderLines)
	{
		this.ignoreHeaderLines = ignoreHeaderLines;
	}

	/**
	 * @return the linesTerminatedBy
	 */
	public String getLinesTerminatedBy()
	{
		return linesTerminatedBy;
	}

	/**
	 * @param linesTerminatedBy the linesTerminatedBy to set
	 */
	public void setLinesTerminatedBy(String linesTerminatedBy)
	{
		this.linesTerminatedBy = linesTerminatedBy;
	}

	/**
	 * @return the rowType
	 */
	public String getRowType()
	{
		return rowType;
	}

	/**
	 * @param rowType the rowType to set
	 */
	public void setRowType(String rowType)
	{
		this.rowType = rowType;
	}

	/**
	 * @return the lines 
	 */
	public Set<BatchLine> getLines()
	{
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(Set<BatchLine> lines)
	{
		this.lines = lines;
	}
}
