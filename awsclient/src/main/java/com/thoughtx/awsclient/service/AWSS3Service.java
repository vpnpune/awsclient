package com.thoughtx.awsclient.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.thoughtx.awsclient.config.AWSConfig;

@Service
public class AWSS3Service {
	private final AmazonS3 s3client;

	public AWSS3Service() {
		this(AWSConfig.getS3client());
	}

	public AWSS3Service(AmazonS3 s3client) {
		this.s3client = s3client;
	}

	// is bucket exist?
	public boolean doesBucketExist(String bucketName) {
		return s3client.doesBucketExist(bucketName);
	}

	// create a bucket
	public Bucket createBucket(String bucketName) {
		return s3client.createBucket(bucketName);
	}

	// list all buckets
	public List<Bucket> listBuckets() {
		return s3client.listBuckets();
	}

	// delete a bucket
	public void deleteBucket(String bucketName) {
		s3client.deleteBucket(bucketName);
	}

	// uploading object
	private PutObjectResult putObject(String bucketName, String key, File file) {
		return s3client.putObject(bucketName, key, file);
	}

	// listing objects
	public ObjectListing listObjects(String bucketName) {
		return s3client.listObjects(bucketName);
	}

	// get an object
	public S3Object getObject(String bucketName, String objectKey) {
		return s3client.getObject(bucketName, objectKey);
	}

	// copying an object
	public CopyObjectResult copyObject(String sourceBucketName, String sourceKey, String destinationBucketName,
			String destinationKey) {
		return s3client.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
	}

	// deleting an object
	public void deleteObject(String bucketName, String objectKey) {
		s3client.deleteObject(bucketName, objectKey);
	}

	// deleting multiple Objects
	public DeleteObjectsResult deleteObjects(DeleteObjectsRequest delObjReq) {
		return s3client.deleteObjects(delObjReq);
	}

	public String getResourceURL(String bucketName, String key) {
		return String.valueOf(s3client.getUrl(bucketName, key));
	}

	public String uploadAndGetURL(String bucketName, String key, File file) {
		PutObjectResult result = putObject(bucketName, key, file);
		return getResourceURL(bucketName, key);
	}

	public  InputStream  downloadFromS3(String bucketName, String objectKey) throws IOException {
		S3Object s3object = getObject(bucketName, objectKey);
		//File f = new File(objectKey);
		//FileUtils.copyInputStreamToFile(s3object.getObjectContent(), f);
		return s3object.getObjectContent();
	}
	public File convertMultiPartToFile(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	    return convFile;
	}
}