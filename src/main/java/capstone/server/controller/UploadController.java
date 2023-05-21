package capstone.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Value("${file.dir}")
    private String fileDir;
    private String getBase64String(MultipartFile multipartFile) throws Exception {
        byte[] bytes = multipartFile.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
    @PostMapping("/picture")
    public HttpEntity<String> saveFile(@RequestParam MultipartFile image) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.setConnection("Keep-Alive");
//        httpHeaders.set(HttpHeaders.CONNECTION, "keep-alive");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String imageFileString = getBase64String(image);
        body.add("filename", image.getOriginalFilename());
        body.add("image", imageFileString);

        HttpEntity<?> requestMessage = new HttpEntity<>(body, httpHeaders);

        if(!image.isEmpty()){
            String fullPath = fileDir+image.getOriginalFilename();
            File file = new File(fileDir);
            if(!file.exists()) file.mkdir();
            image.transferTo(new File(fullPath));

            HttpEntity<String> response = restTemplate.postForEntity("http://localhost:5000/file_upload", requestMessage, String.class);
            System.out.println("response = " + response);

            return response;
        }
        else{
            System.out.println("image = " + image);
        }
        return null;
    }
}

