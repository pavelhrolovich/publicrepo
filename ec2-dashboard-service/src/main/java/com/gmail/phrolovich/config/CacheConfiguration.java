package com.gmail.phrolovich.config;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

@EnableMethodCache(basePackages = "com.gmail.phrolovich")
@EnableCreateCacheAnnotation
@Configuration
public class CacheConfiguration  {

}
