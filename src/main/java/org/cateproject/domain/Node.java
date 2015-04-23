package org.cateproject.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;


@Audited
@Entity
public class Node extends Base {
	
	private String identifier;

    @OneToMany(mappedBy = "parent")
    private List<org.cateproject.domain.Node> children = new ArrayList<org.cateproject.domain.Node>();

    @ManyToOne
    private org.cateproject.domain.Node parent;

    @ManyToOne
    private Term term;

    @ElementCollection
    private Map<String,String> parameters = new HashMap<String,String>();    

    @ManyToMany
    private Set<Term> inapplicableIf = new HashSet<Term>();

    @ManyToMany
    private Set<Term> applicableIf = new HashSet<Term>();
    
    @Override
    public String toString() {
            return this.getClass().getSimpleName() + "<" + this.getIdentifier() + ">";
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public List<Node> getChildren() {
        return this.children;
    }
    
    public void setChildren(List<Node> children) {
        this.children = children;
    }
    
    public Node getParent() {
        return this.parent;
    }
    
    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    public Term getTerm() {
        return this.term;
    }
    
    public void setTerm(Term term) {
        this.term = term;
    }
    
    public Map<String, String> getParameters() {
        return this.parameters;
    }
    
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public Set<Term> getInapplicableIf() {
        return this.inapplicableIf;
    }
    
    public void setInapplicableIf(Set<Term> inapplicableIf) {
        this.inapplicableIf = inapplicableIf;
    }
    
    public Set<Term> getApplicableIf() {
        return this.applicableIf;
    }
    
    public void setApplicableIf(Set<Term> applicableIf) {
        this.applicableIf = applicableIf;
    }
}
