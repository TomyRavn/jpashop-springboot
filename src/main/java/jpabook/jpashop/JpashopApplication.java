package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import jpabook.jpashop.domain.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	//주문 조회 V1 : 엔티티를 직접 노출한 경우, 양방향 연관관계 문제 해결을 위해 추가
	//이를 추가하지 않을 경우, 지연로딩에 따른 초기화(ByteBuddyInterCeptor 관련)에 따라 Hibernate가 처리할 수 없어 에러 발생
	@Bean
	Hibernate5Module hibernate5Module() {
		//지연로딩을 무시하고, 아예 띄우지 않도록 함(null)
		//return new Hibernate5Module();

		Hibernate5Module hibernate5Module = new Hibernate5Module();
		//Entity를 외부에 노출하지 말아야 함
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);

		return hibernate5Module;

		//Entity를 외부에 노출하는 Hibernate5Module을 사용하기 보다 DTO 변환해서 반환하는 것을 권장
	}

}
