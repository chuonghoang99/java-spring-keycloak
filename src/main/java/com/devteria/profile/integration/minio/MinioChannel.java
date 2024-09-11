package com.devteria.profile.integration.minio;

import java.util.Objects;

import jakarta.annotation.PostConstruct;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.devteria.profile.exception.AppException;
import com.devteria.profile.exception.ErrorCode;
import com.devteria.profile.utils.ConverterUtils;

import io.minio.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioChannel {

    String BUCKET = "resource";

    MinioClient minioClient;

    @PostConstruct
    private void init() {
        createBucket(BUCKET);
    }

    @SneakyThrows
    private void createBucket(final String name) {

        final var isFound =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());

        if (!isFound) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
        }

        final var policy =
                """
					{
					"Version": "2012-10-17",
					"Statement": [
					{
						"Effect": "Allow",
						"Principal": "*",
						"Action": "s3:GetObject",
						"Resource": "arn:aws:s3:::%s/*"
						}
					]
					}
				"""
                        .formatted(name);

        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder().bucket(name).config(policy).build());
    }

    @SneakyThrows
    public String upload(@NonNull final MultipartFile file) {

        final var fileName = file.getOriginalFilename();
        try {

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET)
                    .object(fileName)
                    .contentType(
                            Objects.isNull(file.getContentType()) ? "image/png; image/jpg;" : file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());

        } catch (Exception e) {

        }

        return minioClient.getPresignedObjectUrl(io.minio.GetPresignedObjectUrlArgs.builder()
                .method(io.minio.http.Method.GET)
                .bucket(BUCKET)
                .object(fileName)
                .build());
    }

    public byte[] download(String bucket, String name) {
        try (GetObjectResponse inputStream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(name).build())) {
            String contentLength = inputStream.headers().get(HttpHeaders.CONTENT_LENGTH);
            int size = StringUtils.isEmpty(contentLength) ? 0 : Integer.parseInt(contentLength);
            return ConverterUtils.readBytesFromInputStream(inputStream, size);
        } catch (Exception e) {
            throw new AppException(ErrorCode.DOWNLOAD_FILE_ERROR);
        }
    }
}
