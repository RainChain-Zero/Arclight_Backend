package com.rainchain.arclight.config;

import com.rainchain.arclight.interceptors.ApiKeyInterceptor;
import com.rainchain.arclight.interceptors.FrequencyInterceptor;
import com.rainchain.arclight.interceptors.RegisterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private ApiKeyInterceptor apiKeyInterceptor;
    @Autowired
    private FrequencyInterceptor frequencyInterceptor;
    @Autowired
    private RegisterInterceptor registerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(apiKeyInterceptor).addPathPatterns("/**").excludePathPatterns("/register", "/searchKey", "/version");
        registry.addInterceptor(frequencyInterceptor).addPathPatterns("/**").excludePathPatterns("/register", "/searchKey", "/version");
        registry.addInterceptor(registerInterceptor).addPathPatterns("/register");
    }
}
