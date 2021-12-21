package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
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

}
