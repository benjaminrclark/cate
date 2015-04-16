package org.cateproject.web.view;

import org.springframework.stereotype.Component;

@Component
public class ViewUtils {
	
	public String toCommaSeparatedString(Object[] objs) {
		if(objs == null) {
			return null;
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			for(Object obj : objs) {
				if(stringBuilder.length() > 0) {
					stringBuilder.append(",");
				}
				stringBuilder.append(obj.toString());
			}
			return stringBuilder.toString();
		}
	}

}
