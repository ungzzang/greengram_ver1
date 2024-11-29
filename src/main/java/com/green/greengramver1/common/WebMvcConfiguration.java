package com.green.greengramver1.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //객체화를 하나만 해준다, 빈등록역할도 있음, 굳이 얘안써도 되고 밑에 애 써도됨, 밑에 @Bean있을때 의미있음
//@Component //빈등록역할도 있음
//빈등록되면서 WebMvcConfiguration가 객체화가 됨
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final String uploadPath;

    public WebMvcConfiguration(@Value("${file.directory}")String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/pic/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer){
        // RestController의 모든 URL에 "/api" prefix를 생성, api가 앞에 붙게된다. ex) localhost:8080/api/feed?page=10&size=20
        configurer.addPathPrefix("api", HandlerTypePredicate.forAnnotation(RestController.class));
        //컨트롤러에서만 api붙인다. 다른곳은 안붙여도됨
    }
}
