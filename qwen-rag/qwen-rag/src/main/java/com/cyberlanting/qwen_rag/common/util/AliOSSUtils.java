package com.cyberlanting.qwen_rag.common.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOSSUtils {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传
     *
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        OSS ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }

    public void delete(String objectName) {
        OSS ossClient = null;
        try {
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            // 检查文件是否存在（可选）
            boolean exists = ossClient.doesObjectExist(bucketName, objectName);
            if (!exists) {
                log.warn("文件在OSS上不存在，objectName: {}", objectName);
                return;
            }

            // 删除文件
            ossClient.deleteObject(bucketName, objectName);
            log.info("OSS文件删除成功，objectName: {}", objectName);
        } catch (Exception e) {
            log.error("OSS文件删除失败，objectName: {}", objectName, e);
            throw new RuntimeException("OSS文件删除失败", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}

