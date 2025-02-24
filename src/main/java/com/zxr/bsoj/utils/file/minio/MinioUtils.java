package com.zxr.bsoj.utils.file.minio;


import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 参考 https://github.com/minio/minio-java/tree/master/examples
 */

@Service
@Slf4j
public class MinioUtils {
    @Autowired
    MinioConfig minioConfig;
    @Autowired
    MinioClient minioClient;

    //获取列表
    public List<String> listObjects() {
        List<String> list = new ArrayList<>();
        try {

            ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                    .bucket(minioConfig.getBucketNameImage())
                    .build();

            Iterable<Result<Item>> results = minioClient.listObjects(listObjectsArgs);
            for (Result<Item> result : results) {
                Item item = result.get();
                log.info(item.lastModified() + ", " + item.size() + ", " + item.objectName());
                list.add(item.objectName());
            }
        } catch (Exception e) {
            log.error("错误：" + e.getMessage());
        }
        return list;
    }

    //删除
    public void deleteObject(String objectName) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucketNameImage())
                    .object(objectName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            log.error("错误：" + e.getMessage());
        }
    }

    //上传
    public void uploadObject(InputStream is, String fileName, String contentType) {
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketNameImage())
                    .object(fileName)
                    .contentType(contentType)
                    .stream(is, is.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            is.close();
        } catch (Exception e) {
            log.error("错误：" + e.getMessage());
        }
    }

    //获取minio中地址
    public String getObjectUrl(String objectName, boolean isPermanent) {
        try {
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(minioConfig.getBucketNameImage())
                    .object(objectName)
                    .expiry(7, TimeUnit.DAYS)
                    .build();
            String url = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
            if (isPermanent) {
                String[] parts = url.split(Pattern.quote("?"));
                return parts[0];
            }
            return url.replace("0.0.0.0", "127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("错误：" + e.getMessage());
        }
        return "";
    }


    //下载minio服务的文件
    public InputStream getObject(String objectName) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(minioConfig.getBucketNameImage())
                    .object(objectName)
                    .build();
            return minioClient.getObject(getObjectArgs);
        } catch (Exception e) {
            log.error("错误：" + e.getMessage());
        }
        return null;
    }


}

