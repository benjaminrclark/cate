package org.cateproject.web.form;

public enum Delimiter {
	TAB("\t", "Tab"),
	SEMICOLON(";", "Semicolon"),
	COMMA(",", "Comma"),
	SPACE(" ", "Space");
	
	private String value;
	
	private String label;
	
	private Delimiter(String value, String label) {
		this.value = value;
		this.label = label;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return label;
	}

}
