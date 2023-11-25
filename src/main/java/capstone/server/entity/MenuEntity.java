package capstone.server.entity;

import capstone.server.dto.MenuDTO;
import capstone.server.dto.MenuItemDTO;
import capstone.server.dto.StoreDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name="menu")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;

//    @Column(length = 20)
//    private String name;

    @Column()
    private String menuname;


    @Column(length = 1000)
    private String menupicture;

    @Column
    private String price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name")
    private StoreEntity store;

//    public static MenuEntity convertToEntity(MenuDTO menuDTO) {
//
//
//
//    }
}
