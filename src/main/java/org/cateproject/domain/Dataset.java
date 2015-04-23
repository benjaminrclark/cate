package org.cateproject.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Audited
@Entity
public class Dataset extends NonOwnedEntity {
	
    private static Logger logger = LoggerFactory.getLogger(Dataset.class);

    @OneToMany(mappedBy = "dataset")
    private List<Node> nodes = new ArrayList<Node>();

    @ElementCollection
    private Map<String,String> parameters = new HashMap<String,String>();

    private String description;
    
    private String identifier;
    
    private String title;

    @ManyToMany
    private List<Taxon> taxa = new ArrayList<Taxon>();
    
    @Override
    public String toString() {
            return this.getClass().getSimpleName() + "<" + this.getIdentifier() + ">";
    }

    public List<Node> getNodes() {
        return this.nodes;
    }
    
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
    
    public Map<String, String> getParameters() {
        return this.parameters;
    }
    
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<Taxon> getTaxa() {
        return this.taxa;
    }
    
    public void setTaxa(List<Taxon> taxa) {
        this.taxa = taxa;
    }
}
