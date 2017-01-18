package com.repleteinc.motherspromise.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class ExtendedMultipartResolver extends CommonsMultipartResolver {
	
    @Override
    public boolean isMultipart(HttpServletRequest request) {
        if(request!=null) {
            String httpMethod = request.getMethod().toLowerCase();
            // test for allowed methods here...
            String contentType = request.getContentType();
            return (contentType != null && contentType.toLowerCase().startsWith("multipart"));
        }else {
            return false;
        }
    }
}