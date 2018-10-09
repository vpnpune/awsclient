package com.thoughtx.awsclient.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AWSConfig {

	private static AWSCredentials credentials;
	private static	AmazonS3 s3client; 
	static {
		credentials = new BasicAWSCredentials(
				  "demo", 
				  "demo"
				);
	
		s3client= AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.AP_SOUTH_1)
				  .build();
	
	}
	public static AmazonS3 getS3client() {
		return s3client;
	}

	
	
}
