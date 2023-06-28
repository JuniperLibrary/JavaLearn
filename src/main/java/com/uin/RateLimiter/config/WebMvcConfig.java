//package com.uin.RateLimiter.config;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.CacheControl;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.*;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//import org.springframework.web.servlet.mvc.method.annotation.RequestAttributeMethodArgumentResolver;
//
///**
// * @author dingchuan
// */
//@Configuration
//@EnableWebMvc
//@RequiredArgsConstructor
//@Slf4j
//public class WebMvcConfig extends WebMvcConfigurerAdapter {
//
//  @Autowired
//  @Qualifier("rateLimitInterceptor")
//  private HandlerInterceptorAdapter rateLimitInterceptor;
//  @Autowired
//  @Qualifier("authorityInterceptor")
//  private HandlerInterceptorAdapter authorityInterceptor;
//
//  @Override
//  public void addInterceptors(InterceptorRegistry registry) {
//    registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**");
//    registry.addInterceptor(authorityInterceptor).addPathPatterns("/**");
//  }
//
//
//  @Override
//  public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    registry.addResourceHandler("/**").addResourceLocations("/")
//        .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic());
//  }
//
//  @Override
//  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//    argumentResolvers.add(new RequestAttributeMethodArgumentResolver());
//  }
//}
