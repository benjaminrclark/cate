package org.cateproject.web.view;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class ViewUtils {
    public void addParameter(Map<String, List<Object>> parameters, String parameterName, Object parameterValue) {
        if(!parameters.containsKey(parameterName)) {
            parameters.put(parameterName, new ArrayList<Object>());
        }
        parameters.get(parameterName).add(parameterValue);
    }
 
    public String facetParams(HttpServletRequest request, String includeFacet, String excludeFacet, String sort, String view, Page pager, Integer page, Integer size) {
        Map<String, List<Object>> parameters = new HashMap<String, List<Object>>();
        for(Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
            String parameterName = e.nextElement();
            for(String parameterValue : request.getParameterValues(parameterName)) {
                switch(parameterName) {
                    case "filterQuery":
                      if(!ObjectUtils.nullSafeEquals(parameterValue, excludeFacet)) {
                        addParameter(parameters,parameterName,parameterValue);
                      }
                    break;
                    case "page":
                    case "size":
                    case "sort":
                    case "view":
                    break;
                    default:
                      addParameter(parameters, parameterName, parameterValue);
                    break;
                }
            }
        }
        if(!ObjectUtils.isEmpty(includeFacet)) {
            addParameter(parameters, "filterQuery", includeFacet);
        }
        if(ObjectUtils.isEmpty(page)) {
            addParameter(parameters, "page", pager.getNumber());
        } else {
            addParameter(parameters, "page", page);
        }
        if(ObjectUtils.isEmpty(size)) {
            addParameter(parameters, "size", pager.getSize());
        } else {
            addParameter(parameters, "size", size);
        }
        if(!ObjectUtils.isEmpty(sort) && !sort.equals("relevance")) {
            addParameter(parameters, "sort", sort);
        } else if(!ObjectUtils.isEmpty(request.getParameter("sort")) && ObjectUtils.isEmpty(sort)) {
            addParameter(parameters, "sort", request.getParameter("sort"));
        }
        if(!ObjectUtils.isEmpty(view)) {
            addParameter(parameters, "view", view);
        } else if(!ObjectUtils.isEmpty(request.getParameter("view"))){
            addParameter(parameters, "view", request.getParameter("view"));
        }

        boolean first = true;
        StringBuilder params = new StringBuilder();
        params.append("?");
        for(String parameter : parameters.keySet()) {
            for(Object value : parameters.get(parameter)) {
                if(!first) {
                    params.append("&");
                } else {
                    first = false;
                } 
                params.append(parameter + "=" + value);
            }
        }

        return params.toString();

    }

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

    public FacetFieldEntry getSelectedFacet(Object filterQueries, String field, FacetPage facetPage) {
        if(filterQueries != null) {
	    for(String filterQuery : (String[])filterQueries) {
	        String filterField = filterQuery.split(":")[0];
	        if(field.equals(filterField)) {
	            if(facetPage.getFacetResultPage(field).hasContent()) {
	                return (FacetFieldEntry) facetPage.getFacetResultPage(field).getContent().get(0);
	            }
	        }
	    }
        }
        return null;
    }


}
