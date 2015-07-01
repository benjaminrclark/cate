package org.cateproject.multitenant.event;

public class MultitenantEvent {
	
	public MultitenantEvent() {
		
	}
	
	public MultitenantEvent(String identifier, MultitenantEventType type) {
		this.identifier = identifier;
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public MultitenantEventType getType() {
		return type;
	}

	public void setType(MultitenantEventType type) {
		this.type = type;
	}

	private String identifier;
	
	private MultitenantEventType type;

}
