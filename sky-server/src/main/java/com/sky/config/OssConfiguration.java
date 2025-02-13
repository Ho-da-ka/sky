package com.sky.config;

import com.sky.properties.AliyunOSSProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliyunOSSProperties aliyunOSSProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}", aliyunOSSProperties);
        return new AliOssUtil(aliyunOSSProperties);
    }
}
