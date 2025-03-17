package com.project.datn.controller.admin.banhang;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class LoadImageController {

	@RequestMapping(value = "getbill/{photo}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ByteArrayResource> getBill(@PathVariable("photo") String photo) {
		if (!photo.equals("") || photo != null) {
			try {
				Path filename = Paths.get("uploads/bill", photo);
				byte[] buffer = Files.readAllBytes(filename);
				ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
				return ResponseEntity.ok().contentLength(buffer.length)
						.contentType(MediaType.parseMediaType("image/png")).body(byteArrayResource);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@RequestMapping(value = "getproduct/{photo}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ByteArrayResource> getProduct(@PathVariable("photo") String photo) {
		if (photo != null && !photo.isEmpty()) { // Sửa lại điều kiện kiểm tra
			try {
				Path filename = Paths.get("uploads/products", photo);

				// Kiểm tra xem file có tồn tại không trước khi đọc
				if (!Files.exists(filename)) {
					return ResponseEntity.notFound().build();
				}

				byte[] buffer = Files.readAllBytes(filename);
				ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);

				return ResponseEntity.ok()
						.contentLength(buffer.length)
						.contentType(MediaType.IMAGE_PNG) // Tự động lấy kiểu ảnh
						.body(byteArrayResource);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
		return ResponseEntity.badRequest().build();
	}


}
