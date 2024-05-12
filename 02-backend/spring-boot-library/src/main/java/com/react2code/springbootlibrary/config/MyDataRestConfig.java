package com.react2code.springbootlibrary.config;

import com.react2code.springbootlibrary.entity.Book;
import com.react2code.springbootlibrary.entity.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${frontend_url}")
    private String theAllowedOrigins;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration configuration,
                                                     CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.POST,
                HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH};


        configuration.exposeIdsFor(Book.class);
        configuration.exposeIdsFor(Review.class);
        disableHttpMethods(Book.class, theUnsupportedActions, configuration);
        disableHttpMethods(Review.class, theUnsupportedActions, configuration);

        /* Configure CORS MAPPING */
        cors.addMapping(configuration.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);

    }

    private void disableHttpMethods(Class theClass, HttpMethod[] theUnsupportedActions,
                                    RepositoryRestConfiguration configuration) {
        configuration.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }


}
