package org.cateproject.domain.audit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RevisionListener implements org.hibernate.envers.RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		RevisionInfo revisionInfo = (RevisionInfo)revisionEntity;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication != null) {
			revisionInfo.setUserName(authentication.getName());
		}
		
	}

}
