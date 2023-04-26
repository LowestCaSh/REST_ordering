package com.das_team.ordering;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import java.util.Collections;
import org.springframework.context.annotation.Bean;

@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.das_team.ordering"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo());
    }
	
	private ApiInfo metaInfo() {
	    return new ApiInfo(
	    		"Endpoints for Orders and ShoppingCarts",
	            "Returns Information about Orders and ShoppingCarts for our REST API study project",
	            "1.0",
	            "Terms of Service",
	            new Contact("Lukas und Fabian", "E-Mails", "luko0005 und faam0004"),
	            "No License",
	            "No License url",
	            Collections.emptyList()
	    );
	}
}
