package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
//@Setter                                     //Setter 대신 Business 로직을 가지고 변경해야 함(유지보수)
public abstract class Item {                //구현체를 가지고 만들 예정이기에 추상 클래스로 생성(여러 종류의 Item ; Album, Book, Movie)

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
    //Collection 생성 후에는 바로 초기화하는 것이 안전
    //엔티티 영속화 후에는 hibernate 자체 내장 컬렉션으로 변경이 되기 때문에, Collection은 함부로 변경하지 말 것 (*****)


    // === 비즈니스 로직(Entity 자체 해결 가능한 것들은 Entity 안에 로직을 넣는 것이 좋음 ; 응집도) === //
    /**
     * 재고 변동 로직
     */
    //(1) Stock 증가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    //(2) Stock 감소
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
