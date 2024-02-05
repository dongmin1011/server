package capstone.server.controller;

import capstone.server.dto.*;
import capstone.server.service.StoreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class databaseController {


//    @Autowired
//    private RestTemplate restTemplate;

    private final StoreService storeService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private String ip = "211.179.26.243:5000";
    @GetMapping("/get/storeList")
    @ResponseBody
    public List<StoreDTO> getStoreList(@RequestParam(required = false) String orderBy) {
        List<StoreDTO> storeList = storeService.getStoreList();

        if ("name".equals(orderBy)) { // 가나다 순으로 정렬
            storeList.sort(Comparator.comparing(StoreDTO::getStoreName));
        } else if ("favorite".equals(orderBy)) { // 좋아요 개수에 따라 정렬
            storeList.sort(Comparator.comparingInt(StoreDTO::getFavoriteCount).reversed());
        }
        else if ("views".equals(orderBy)) { // 조회수에 따라 정렬
            storeList.sort(Comparator.comparingInt(StoreDTO::getViewsCount).reversed());
        }

        return storeList;
    }


    @PostMapping("/api/call")
    public String callApi(@RequestParam("textValue") String textValue) {
        System.out.println("textValue = " + textValue);
        RestTemplate restTemplate = new RestTemplate();
        // 입력된 값을 JSON 형태로 변환
        String jsonBody;
        try {
            ObjectNode requestData = objectMapper.createObjectNode();
            requestData.put("검색", textValue);
            requestData.put("community", "네이버");
            jsonBody = objectMapper.writeValueAsString(requestData);
            System.out.println("jsonBody = " + jsonBody);

            // API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set(HttpHeaders.ACCEPT_CHARSET, "UTF-8");


            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://"+ip+"/profile",requestEntity, String.class);

//            System.out.println("responseEntity = " + responseEntity);
            String jsonString = responseEntity.getBody();
            System.out.println("jsonString = " + jsonString);

            try {
                // JSON 문자열을 DTO 객체로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                StoreDTO storeDTO = objectMapper.readValue(jsonString, StoreDTO.class);


                System.out.println("storeDTO = " + storeDTO);
                storeService.saveStoreFromJson(storeDTO);
                return "redirect:/";
            }catch (Exception e){
                System.out.println("e = " + e);
                return "error";
            }

        } catch (JsonProcessingException e) {
            // JSON 처리 예외 처리
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process JSON");
        }
        return null;
    }
    @PostMapping("/api/menu")
    public String  callMenu(@RequestParam("textValue2") String textValue) {
        RestTemplate restTemplate = new RestTemplate();
        // 입력된 값을 JSON 형태로 변환
        String jsonBody;
        try {
            ObjectNode requestData = objectMapper.createObjectNode();
            requestData.put("검색", textValue);
            requestData.put("community", "네이버");
            jsonBody = objectMapper.writeValueAsString(requestData);
            System.out.println("jsonBody = " + jsonBody);

            // API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set(HttpHeaders.ACCEPT_CHARSET, "UTF-8");


            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://"+ip+"/menu", requestEntity, String.class);

//            System.out.println("responseEntity = " + responseEntity);
            String jsonString = responseEntity.getBody();
            System.out.println("jsonString = " + jsonString);

            try {
                // JSON 문자열을 DTO 객체로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                MenuDTO menuDTO = objectMapper.readValue(jsonString, MenuDTO.class);



                storeService.saveMenusFromJson(menuDTO);

                return "redirect:/";
            }catch (Exception e){
                System.out.println("e = " + e);
                return "error";
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/api/comment/naver")
    public String  callnaver(@RequestParam("textValue2") String textValue) {
        RestTemplate restTemplate = new RestTemplate();
        // 입력된 값을 JSON 형태로 변환
        String jsonBody;
        try {
            ObjectNode requestData = objectMapper.createObjectNode();
            requestData.put("검색", textValue);
            requestData.put("community", "네이버");
            jsonBody = objectMapper.writeValueAsString(requestData);
            System.out.println("jsonBody = " + jsonBody);

            // API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://"+ip+"/review", requestEntity, String.class);

//            System.out.println("responseEntity = " + responseEntity);
            String jsonString = responseEntity.getBody();
            System.out.println("jsonString = " + jsonString);

            try {
                // JSON 문자열을 DTO 객체로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                ReviewDTO reviewDTO = objectMapper.readValue(jsonString, ReviewDTO.class);

                System.out.println("naverCommentDTO = " + reviewDTO);

                storeService.saveNaverComment(reviewDTO);

//                storeService.saveNaverComment(naverCommentDTO);


                return "redirect:/";
            }catch (Exception e){
                System.out.println("e = " + e);
                return "error";
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/api/comment/everytime")
    public String  callET(@RequestParam("textValue2") String textValue) {
        RestTemplate restTemplate = new RestTemplate();
        // 입력된 값을 JSON 형태로 변환
        String jsonBody;
        try {
            ObjectNode requestData = objectMapper.createObjectNode();
            requestData.put("검색", textValue);
            requestData.put("community", "에브리타임");
            jsonBody = objectMapper.writeValueAsString(requestData);
            System.out.println("jsonBody = " + jsonBody);

            // API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://"+ip+"/review", requestEntity, String.class);

            String jsonString = responseEntity.getBody();
            System.out.println("jsonString = " + jsonString);

            try {
                // JSON 문자열을 DTO 객체로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                EveryTimeReviewDTO reviewDTO = objectMapper.readValue(jsonString, EveryTimeReviewDTO.class);

//                System.out.println("naverCommentDTO = " + reviewDTO);

                storeService.saveETComment(reviewDTO);

                return "redirect:/";
            }catch (Exception e){
                System.out.println("e = " + e);
                return "error";
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/database/save/menuItem")
    public void saveMenu(@RequestBody Map menu){
        System.out.println("storeName = " + menu);
//        System.out.println("menuItemDTO = " + menuItemDTO);
        String storeName = (String) menu.get("storeName");
        MenuItemDTO menuItemDTO = new MenuItemDTO();
        menuItemDTO.setFoodName((String) menu.get("menuName"));
        menuItemDTO.setPrice((String) menu.get("menuPrice"));
        System.out.println("menuItemDTO = " + menuItemDTO);
        storeService.saveMenuItem(storeName, menuItemDTO);
    }

    @PostMapping("/database/save/community")
//    @ResponseStatus
//    @ResponseBody
    public void saveCommunity(@RequestBody  CommunityReview review){
        System.out.println("review = " + review);
        storeService.saveCommunity(review);
    }
    @PostMapping("/database/save/community/comment")
//    @ResponseStatus
//    @ResponseBody
    public void saveCommunityChild(@RequestBody  CommunityChildDTO review){
        System.out.println("review = " + review);
        storeService.saveCommunityChild(review);
    }
    @GetMapping("/database/getStore")
    @ResponseBody
    public StoreDTO getStore(@RequestParam String Name){
        System.out.println("Name = " + Name);
        StoreDTO storeInfo = storeService.getStoreInfo(Name);
        System.out.println("storeInfo = " + storeInfo);
        return storeInfo;

    }
    @GetMapping("/database/getMenu")
    @ResponseBody
    public MenuDTO getMenu(@RequestParam String Name){
        System.out.println("Name = " + Name);
        MenuDTO menuDTO = storeService.getMenuInfo(Name);
//        System.out.println("storeInfo = " + storeInfo);
        if(menuDTO==null){
            System.out.println("review = " + menuDTO);
            MenuDTO nullMenu = new MenuDTO();
            nullMenu.setStoreName(Name);
//            nullMenu.setReviews(null);
            return  nullMenu;

        }
        return menuDTO;

    }
    @GetMapping("/database/review/naver")
    @ResponseBody
    public ReviewDTO getNaverReview(@RequestParam String Name){
        System.out.println("Name = " + Name);
        ReviewDTO review = storeService.getReview(Name);
        if(review==null){
            System.out.println("review = " + review);
            ReviewDTO nullReview = new ReviewDTO();
            nullReview.setStoreName(Name);
            nullReview.setReviews(null);
            return  nullReview;

        }
//        System.out.println("storeInfo = " + storeInfo);
        return review;

    }
    @GetMapping("/database/review/everytime")
    @ResponseBody
    public EveryTimeReviewDTO getETReview(@RequestParam String Name){
        System.out.println("Name = " + Name);
        EveryTimeReviewDTO review = storeService.getETReview(Name);
        if(review==null){
            System.out.println("review = " + review);
            EveryTimeReviewDTO nullReview = new EveryTimeReviewDTO();
            nullReview.setStoreName(Name);
            nullReview.setReviews(null);
            return  nullReview;

        }
//        System.out.println("storeInfo = " + storeInfo);
        return review;

    }
    @GetMapping("/database/review/community")
    @ResponseBody
    public CommunityReview getCommunityReview(@RequestParam String Name){
        System.out.println("Name = " + Name);
        CommunityReview review = storeService.getCommunity(Name);
//        System.out.println("storeInfo = " + storeInfo);
        if(review==null){
            System.out.println("review = " + review);
            CommunityReview nullReview = new CommunityReview();
            nullReview.setStoreName(Name);
            nullReview.setReviews(null);
            return  nullReview;

        }
        return review;

    }
    @GetMapping("/database/review/community/comments")
    @ResponseBody
    public CommunityDTO getCommunitycomment(@RequestParam int id){
//        Long id = Long.parseLong(idString);
        Long longId = Long.valueOf(id);
        System.out.println("Name = " + id);
        CommunityDTO review = storeService.getCommunity(longId);

        return review;

    }

}
