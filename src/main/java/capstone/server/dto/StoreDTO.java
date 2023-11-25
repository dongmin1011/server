package capstone.server.dto;


import capstone.server.entity.StoreEntity;
import capstone.server.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StoreDTO {
    private String storeName;

    @JsonProperty("PH")
    private String ph;
    private String address;
    private String businessTime;
    private String score;
    private int favoriteCount;
    private int viewsCount;
    private String storePhoto;

    public static StoreDTO toStoreDTO(StoreEntity storeEntity){
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreName(storeEntity.getName());
        storeDTO.setPh(storeEntity.getPH());
        storeDTO.setAddress(storeEntity.getAddress());
        storeDTO.setScore(storeEntity.getScore());
        storeDTO.setBusinessTime(storeEntity.getBusinessTime());
storeDTO.setFavoriteCount(storeEntity.getFavoriteCounter());
storeDTO.setViewsCount(storeEntity.getViewsCounter());
storeDTO.setStorePhoto(storeEntity.getURL());
        return storeDTO;
    }
}
