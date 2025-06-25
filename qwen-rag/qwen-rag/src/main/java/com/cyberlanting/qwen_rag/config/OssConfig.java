package com.cyberlanting.qwen_rag.config;

import com.cyberlanting.qwen_rag.common.util.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfig {

    @Value("${qwen-rag.alioss.endpoint}")
    private String endpoint;
    @Value("${qwen-rag.alioss.access-key-id}")
    private String accessKeyId;
    @Value("${qwen-rag.alioss.access-key-secret}")
    private String accessKeySecret;
    @Value("${qwen-rag.alioss.bucket-name}")
    private String bucketName;

    /*在这个配置类中，定义了一个名为aliOssUtil的@Bean方法，用于创建一个AliOssUtil对象。*/
    @Bean
    /*@ConditionalOnMissingBean注解表示当不存在名为aliOssUtil的bean时，才会创建该bean。
    @ConditionalOnMissingBean
    /*在方法体中，通过依赖注入的方式获取AliOssProperties对象，并使用它的属性值来创建AliOssUtil对象。*/
    public AliOSSUtils aliOssUtil(){
//        log.info("开始创建阿里云文件上传工具类对象：{}",aliOssProperties);
        return new AliOSSUtils(
                endpoint,
                accessKeyId,
                accessKeySecret,
                bucketName
        );
    }

}
