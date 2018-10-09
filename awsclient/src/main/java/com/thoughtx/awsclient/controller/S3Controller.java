package com.thoughtx.awsclient.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.thoughtx.awsclient.service.AWSS3Service;

@RestController
@RequestMapping("/s3")
public class S3Controller {

	@Value("${client.bucket}")
	private String bucket;

	@Autowired
	private AWSS3Service service;

	@Value("${document.path}")
	private String DOCUMENT;

	@GetMapping(value = "/aws")
	public ResponseEntity<String> aws() {
		List<Bucket> buckets = service.listBuckets();

		if (buckets != null && !buckets.isEmpty()) {
			for (Bucket bucket : buckets) {
				System.out.println(bucket.getName());
			}
		}

		// service.uploadObject();
		System.out.println("Items in bucket");

		ObjectListing list = service.listObjects(bucket);

		list.getObjectSummaries().forEach(s -> System.out.println(s.getKey()));

		/*
		 * try { service.downloadObject(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * System.out.println(service.getResourceURL("csism",
		 * "Document/README.md"));
		 */
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@GetMapping(value = "/test")
	public ResponseEntity<String> test() {
		return new ResponseEntity<>("S3 Server is UP", HttpStatus.OK);

	}

	@GetMapping(value = "/download/{fileName}")
	public ResponseEntity<InputStream> downloadFile(@PathVariable("fileName") String fileName,
			HttpServletResponse response) {

		try {
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			InputStream s = service.downloadFromS3(bucket, DOCUMENT + "/" + fileName);
			return new ResponseEntity<InputStream>(s, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/uploadMultipart")
	public String uploadFileMultiPart(@RequestPart(value = "file") MultipartFile file) {
		String resourceUrl = null;
		try {
			resourceUrl = this.service.uploadAndGetURL(bucket, DOCUMENT + "/"+file.getOriginalFilename(),
					this.service.convertMultiPartToFile(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resourceUrl;
	}
	@PostMapping("/upload")
	public String uploadFile(@RequestPart(value = "file") byte[] file) {
		String resourceUrl = null;
//		try {
//			resourceUrl = this.service.uploadAndGetURL(bucket, DOCUMENT + "/"+file.getOriginalFilename(),
//					this.service.convertMultiPartToFile(file));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return resourceUrl;
	}
}
