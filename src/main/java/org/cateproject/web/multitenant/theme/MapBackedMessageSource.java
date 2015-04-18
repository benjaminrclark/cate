package org.cateproject.web.multitenant.theme;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractMessageSource;

class MapBackedMessageSource extends AbstractMessageSource {
	private Logger logger = LoggerFactory.getLogger(MapBackedMessageSource.class);
	
    Map<String,String> messages = new HashMap<String,String>();
        
    public MapBackedMessageSource(Map<String,String> messages) {
        this.messages = new HashMap<String,String>(messages);
    }

    protected MessageFormat resolveCode(String code, Locale locale) {
    	
        MessageFormat messageFormat = null;
        if(messages.containsKey(code)) {
            messageFormat = new MessageFormat(messages.get(code),locale);
        }
        return messageFormat;
    }
}