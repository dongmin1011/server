package capstone.server.dto;

import capstone.server.entity.MenuEntity;
import capstone.server.entity.StoreEntity;
import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {
    private List<MenuItemDTO> menus;
    private String storeName;


//    public static MenuDTO toMenuDTO(MenuEntity menuEntity){
//        MenuDTO menuDTO = new MenuDTO();
//        menuDTO.setFoodName(menuEntity.getName());
//        menuDTO.setMenuImage(menuEntity.getMenupicture());
//        menuDTO.setPrice(menuEntity.getPrice());
//
//        return menuDTO;
//    }
}
