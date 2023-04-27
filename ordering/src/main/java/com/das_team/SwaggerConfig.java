package com.das_team.ordering;

import springfox.documentation.builders.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import org.springframework.context.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@Configuration
@OpenAPIDefinition(info=@Info(
		title="Endpoints for Orders and ShoppingCarts",
		description="Returns Information about Orders and ShoppingCarts for our REST API study project",
		version="1.0.0",
		contact=@Contact(name="Lukas und Fabian", url="https://mattermost.gitlab.rlp.net/vis-ss23/channels/town-square")
))

public class SwaggerConfig {
	
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.das_team.ordering"))
                .paths(PathSelectors.any())
                .build();
    }
}
