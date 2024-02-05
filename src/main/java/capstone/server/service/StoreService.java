package capstone.server.service;

import capstone.server.dto.*;
//import capstone.server.entity.CommentEntity;
import capstone.server.entity.Comment.*;
import capstone.server.entity.CommunityCommentEntity;
import capstone.server.entity.CommunityEntity;
import capstone.server.entity.MenuEntity;
import capstone.server.entity.StoreEntity;
//import capstone.server.repository.CommentRepository;
import capstone.server.repository.*;
import capstone.server.repository.ETComment.ETChildRepository;
import capstone.server.repository.ETComment.ETParentRepository;
import capstone.server.repository.ETComment.ETPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.vdurmont.emoji.EmojiParser;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    private final NaverCommentRepository naverCommentRepository;
    private final ImageRepository imageRepository;

    private final ETPostRepository etPostRepository;
    private final ETParentRepository etParentRepository;
    private final ETChildRepository etChildRepository;

    private final CommunityCommentRepository communityCommentRepository;
    private final  CommunityChildRepository communityChildRepository;

    public void saveStoreFromJson(StoreDTO storeDTO) {
        // DTO 객체를 Entity 객체로 변환
        StoreEntity storeEntity = StoreEntity.convertToEntity(storeDTO);

        // 가게 이름으로 기존 가게 조회
        Optional<StoreEntity> existingStore = storeRepository.findByName(storeDTO.getStoreName());

        if (existingStore.isPresent()) {
            // 기존 가게가 존재하는 경우 업데이트
            StoreEntity updatedStore = existingStore.get();
            // 업데이트할 필드들을 적절하게 설정
            updatedStore.setAddress(storeEntity.getAddress());
            updatedStore.setBusinessTime(storeEntity.getBusinessTime());
            updatedStore.setScore(storeEntity.getScore());
            updatedStore.setPH(storeEntity.getPH());
            updatedStore.setViewsCounter(updatedStore.getViewsCounter());
            updatedStore.setFavoriteCounter(updatedStore.getFavoriteCounter());
            updatedStore.setURL(storeEntity.getURL());

            // 엔티티 업데이트
            storeRepository.save(updatedStore);
        } else {
            // 기존 가게가 없는 경우 저장
            storeRepository.save(storeEntity);
        }
    }
    public void saveMenusFromJson(MenuDTO menuDTO){
        String storeName = menuDTO.getStoreName();
        List<MenuItemDTO> menuItems = menuDTO.getMenus();

        for (MenuItemDTO menuItem : menuItems) {
            MenuEntity menuEntity = new MenuEntity();
    //        menuEntity.setName(storeName);
            menuEntity.setMenuname(menuItem.getFoodName());
            menuEntity.setMenupicture(menuItem.getMenu_image());
            menuEntity.setPrice(menuItem.getPrice().replace(",", "").replace("원", ""));
            menuEntity.setStore(storeRepository.findByName(storeName).get());

            menuRepository.save(menuEntity);
        }
    }
    public void saveMenuItem(String storeName, MenuItemDTO menuItemDTO){
        String menuName = menuItemDTO.getFoodName();
        String menuPrice = menuItemDTO.getPrice();
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setMenuname(menuName);
        menuEntity.setPrice(menuPrice);
        menuEntity.setStore(storeRepository.findByName(storeName).get());
        menuRepository.save(menuEntity);
    }

    public void saveNaverComment(ReviewDTO reviewDTO){
        String storeName = reviewDTO.getStoreName();
        List<NaverCommentDTO> reviews = reviewDTO.getReviews();

        for (NaverCommentDTO review : reviews) {
            NaverCommentEntity commentEntity = new NaverCommentEntity();

            commentEntity.setDate(review.getDate());
            commentEntity.setReview(removeEmoji(review.getReview()));
            commentEntity.setNickName(removeEmoji(review.getNickName()));
            

            commentEntity.setStore(storeRepository.findByName(storeName).get());

            naverCommentRepository.save(commentEntity);

            List<String> images = review.getImage_urls();
            System.out.println("images = " + images);
            if(images!=null) {
                for (String image : images) {
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setImage_urls(image);
                    NaverCommentEntity naverComment = naverCommentRepository.findByNickName(review.getNickName()).orElse(null);

                    imageEntity.setName(naverComment);
                    imageRepository.save(imageEntity);
                }
            }
            else continue;
        }
    }
    String removeEmoji(String text){
        return Pattern.compile("[^a-zA-Z가-힣0-9!@#$%^&*()_+/.,\\[\\]{}]")
                .matcher(text)
                .replaceAll("");
    }
    public void saveETComment(EveryTimeReviewDTO ETCommentDTO){
        String storeName = ETCommentDTO.getStoreName();
        List<ETPostDTO> reviews= ETCommentDTO.getReviews();

        for(ETPostDTO review: reviews) {
            ETPostEntity etPost = new ETPostEntity();
            etPost.setNum(review.getNum());
            etPost.setTitle(
                    removeEmoji(review.getTitle()));
            etPost.setBody(removeEmoji(review.getReview()));
            etPost.setStore(storeRepository.findByName(storeName).get());

            etPostRepository.save(etPost);

            List<ETCommentDTO> comments = review.getComments();
            Long parentId = null;
            if (comments !=null) {
                for (ETCommentDTO comment : comments) {

                    if (comment.getDivision().equals("parent")) {
                        ETParentCommentEntity etParentComment = new ETParentCommentEntity();
                        etParentComment.setDate(comment.getDate());
                        etParentComment.setText(removeEmoji(comment.getText()));
                        etParentComment.setComments(etPostRepository.findByNumAndStore_Name(review.getNum(), storeName));


                        etParentRepository.save(etParentComment);
                        parentId = etParentComment.getId();

                    } else {
                        ETChildCommentEntity etChildComment = new ETChildCommentEntity();


                        if(parentId!=null) {
                            etChildComment.setDate(comment.getDate());
                            etChildComment.setText(removeEmoji(comment.getText()));
                            ETParentCommentEntity etParentComment = etParentRepository.findById(parentId).orElse(null);
                            etChildComment.setParent(etParentComment);
                            etChildRepository.save(etChildComment);
                        }


                    }

                }

            }
            else{
                continue;
            }
        }

    }
    public void saveCommunity(CommunityReview review){
        String storeName = review.getStoreName();

//        System.out.println("review = " + review.getStoreName());

        LocalDateTime currentTime = LocalDateTime.now();
        // 날짜와 시간 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM월-dd일 HH:mm");
        String formattedDateTime = currentTime.format(formatter);

//        System.out.println("review = " + review.g);

        CommunityDTO communityDTO = review.getReview();
//        System.out.println("communityDTO = " + communityDTO);
        CommunityEntity communityEntity = new CommunityEntity();
        communityEntity.setDate(formattedDateTime);
        communityEntity.setReview(communityDTO.getReview());
        communityEntity.setNickName(communityDTO.getName());
        communityEntity.setTitle(communityDTO.getTitle());
        communityEntity.setStore(storeRepository.findByName(storeName).get());

        communityCommentRepository.save(communityEntity);

    }
    public void saveCommunityChild( CommunityChildDTO child){
        String storeName = child.getStoreName();

//        System.out.println("review = " + review.getStoreName());

        LocalDateTime currentTime = LocalDateTime.now();
        // 날짜와 시간 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM월-dd일 HH:mm");
        String formattedDateTime = currentTime.format(formatter);

//        System.out.println("review = " + review.g);

//        CommunityDTO communityDTO = child();
//        System.out.println("communityDTO = " + communityDTO);
        CommunityCommentEntity commentEntity = new CommunityCommentEntity();
        commentEntity.setDate(formattedDateTime);
//        communityEntity.setReview(communityDTO.getReview());
//        communityEntity.setNickName(communityDTO.getName());
//        communityEntity.setTitle(communityDTO.getTitle());
//        communityEntity.setStore(storeRepository.findByName(storeName).get());
        commentEntity.setReview(child.getComment());
        commentEntity.setNickName(child.getName());

        commentEntity.setParent(communityCommentRepository.findById(child.getId()).get());
        System.out.println("commentEntity = " + commentEntity);

        communityChildRepository.save(commentEntity);

//        communityCommentRepository.save(communityEntity);

    }
    public List<StoreDTO> getStoreList(){
        List<StoreEntity> storeEntities = storeRepository.findAll();
        List<StoreDTO> storeDTOList = new ArrayList<>();
        if( !storeEntities.isEmpty()){
            for(StoreEntity storeEntity: storeEntities){
                storeDTOList.add(StoreDTO.toStoreDTO(storeEntity));
            }
        }
        return storeDTOList;
    }


    public StoreDTO getStoreInfo(String Name ){
        System.out.println("storeName = " + "1234");
        Optional<StoreEntity> store = storeRepository.findByName(Name);
        System.out.println("store = " + store);
        if(store.isPresent()){
            //조회 결과 있음
            StoreEntity storeEntity = store.get();

            storeEntity.incrementViewsCounter(); // viewsCounter 증가
            storeRepository.save(storeEntity); // 변경된 값 저장
            StoreDTO dto = StoreDTO.toStoreDTO(storeEntity);

            return dto;

        }else {
            //조회결과없음
            return null;
        }

    }
    public MenuDTO getMenuInfo(String storeName ){

        List<MenuEntity> menuEntities = menuRepository.findByStoreNameIgnoreCase(storeName);
        List<MenuItemDTO> menuItemDTOs = new ArrayList<>();
        if(!menuEntities.isEmpty()){
            //조회 결과 있음

            for (MenuEntity menuEntity : menuEntities) {
//                System.out.println("menuEntity.getMenuname() = " + menuEntity.getMenuname());
                MenuItemDTO menuItemDTO = new MenuItemDTO();
                menuItemDTO.setFoodName(menuEntity.getMenuname());
                menuItemDTO.setMenu_image(menuEntity.getMenupicture());
                menuItemDTO.setPrice(menuEntity.getPrice());
                menuItemDTOs.add(menuItemDTO);
            }
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setStoreName(storeName);
            menuDTO.setMenus(menuItemDTOs);

            return menuDTO;
        }
        else {
            //조회결과없음
            return null;
        }

    }
    public ReviewDTO getReview(String storeName) {
        List<NaverCommentEntity> comments = naverCommentRepository.findByStoreNameIgnoreCase(storeName);
        if (!comments.isEmpty()) {
            Collections.sort(comments, Comparator.comparing(NaverCommentEntity::getDate));
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setStoreName(storeName);
            List<NaverCommentDTO> naverCommentDTOs = new ArrayList<>();

            for (NaverCommentEntity comment : comments) {
                NaverCommentDTO commentDTO = new NaverCommentDTO();
                commentDTO.setReview(comment.getReview());
                commentDTO.setDate(comment.getDate());
                commentDTO.setNickName(comment.getNickName());

                List<ImageEntity> imageEntities = comment.getImage();
                if (imageEntities != null) {
                    List<String> imageUrls = new ArrayList<>();
                    for (ImageEntity image : imageEntities) {
                        imageUrls.add(image.getImage_urls());
                    }
                    commentDTO.setImage_urls(imageUrls);
                }
                naverCommentDTOs.add(commentDTO);
            }

            reviewDTO.setReviews(naverCommentDTOs);
            return reviewDTO;
        } else {
            // 조회 결과 없음
            return null;
        }
    }

    public EveryTimeReviewDTO getETReview(String storeName) {
        List<ETPostEntity> posts = etPostRepository.findByStoreNameIgnoreCase(storeName);
        if (!posts.isEmpty()) {
//            Collections.sort(comments, Comparator.comparing(ET::getDate));
            EveryTimeReviewDTO reviewDTO = new EveryTimeReviewDTO();
            reviewDTO.setStoreName(storeName);
            List<ETPostDTO> postDTOs = new ArrayList<>();

            for (ETPostEntity post : posts) {
                ETPostDTO commentDTO = new ETPostDTO();
                commentDTO.setTitle(post.getTitle());
                commentDTO.setReview(post.getBody());
                commentDTO.setNum(post.getNum());

                List<ETParentCommentEntity> etParentCommentEntities = post.getComments();
                if (etParentCommentEntities != null) {
                    List<ETCommentDTO> etCommentDTOList = new ArrayList<>();
                    for (ETParentCommentEntity parent : etParentCommentEntities) {
                        ETCommentDTO etCommentDTO = new ETCommentDTO();
                        etCommentDTO.setDate(parent.getDate());
                        etCommentDTO.setText(parent.getText());

                        List<ETChildCommentEntity> etChildCommentEntities = parent.getChild();
                        if(etChildCommentEntities!=null) {
                            List<ETChildCommentDTO> etChildCommentDTOList = new ArrayList<>();
                            for (ETChildCommentEntity child: etChildCommentEntities){
                                ETChildCommentDTO childCommentDTO = new ETChildCommentDTO();
                                childCommentDTO.setDate(child.getDate());
                                childCommentDTO.setText(child.getText());
                                etChildCommentDTOList.add(childCommentDTO);

                            }
                            etCommentDTO.setChild(etChildCommentDTOList);
                        }else{
                            etCommentDTO.setChild(null);
                        }
                        etCommentDTOList.add(etCommentDTO);


                    }
                    commentDTO.setComments(etCommentDTOList);
                    postDTOs.add(commentDTO);

                }
//                reviewDTO.setReviews(postDTOs);
//                return reviewDTO;
            }

            reviewDTO.setReviews(postDTOs);
            return reviewDTO;
        } else {
            // 조회 결과 없음
            return null;
        }
    }
    public CommunityReview getCommunity(String storeName){
        List<CommunityEntity> posts = communityCommentRepository.findByStoreNameIgnoreCase(storeName);

        if (!posts.isEmpty()) {


            List<CommunityDTO> communityDTOList = new ArrayList<>();

            CommunityReview communityReview = new CommunityReview();
            communityReview.setStoreName(storeName);

            for(CommunityEntity post: posts){
                CommunityDTO communityDTO = new CommunityDTO();
//                communityDTO.setStoreName(storeName);
                communityDTO.setName(post.getNickName());
                communityDTO.setReview(post.getReview());
                communityDTO.setTitle(post.getTitle());
                communityDTO.setDate(post.getDate());
                communityDTO.setId(post.getId());

                List<CommunityCommentEntity> comments = post.getComments();
                System.out.println("comments111 = " + comments);
                if(comments!=null){
                    List<CommunityChildDTO> communityChildDTOList = new ArrayList<>();
                    for(CommunityCommentEntity comment: comments){
                        CommunityChildDTO communityChildDTO = new CommunityChildDTO();
                        communityChildDTO.setComment(comment.getReview());
                        communityChildDTO.setName(comment.getNickName());
                        communityChildDTO.setDate(comment.getDate());
                        

                        communityChildDTOList.add(communityChildDTO);
                    }
                    communityDTO.setChildDTOList(communityChildDTOList);
                }
                else{
                    communityDTO.setChildDTOList(null);
                }

                communityDTOList.add(communityDTO);

            }
            communityReview.setReviews(communityDTOList);

            return communityReview;
        }
        else return null;
    }
    public CommunityDTO getCommunity(Long id){
//        List<CommunityCommentEntity> comments = communityChildRepository.findByParent(id);
        CommunityEntity post = communityCommentRepository.findById(id).get();

        CommunityDTO communityDTO = new CommunityDTO();

        if (post!=null) {


            List<CommunityChildDTO> communityDTOList = new ArrayList<>();

//            CommunityReview communityReview = new CommunityReview();
//            communityReview.setStoreName(storeName);

            List<CommunityCommentEntity> comments = post.getComments();

            for(CommunityCommentEntity comment: comments){
                CommunityChildDTO communityChildDTO = new CommunityChildDTO();
                communityChildDTO.setDate(comment.getDate());
                communityChildDTO.setName(comment.getNickName());
                communityChildDTO.setComment(comment.getReview());
//
                communityChildDTO.setId(comment.getId());


                communityDTOList.add(communityChildDTO);

            }
            communityDTO.setTitle(post.getTitle());
            communityDTO.setReview(post.getReview());
            communityDTO.setDate(post.getDate());
            communityDTO.setName(post.getNickName());
            
            communityDTO.setChildDTOList(communityDTOList);

            return communityDTO;
        }
        else return null;
    }

}
