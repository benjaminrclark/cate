package org.cateproject.repository.search;



public class Match {

    private String text;
        
    private Object value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
        
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Match(String text, Object value) {
        this.text = text;
        this.value = value;
    }

	public Match() {
	}
}
