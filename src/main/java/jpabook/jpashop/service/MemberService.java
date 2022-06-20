package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@AllArgsConstructor                                 //5. 롬복 적용시 가능, Field의 모든 내용을 생성자 생성
@Service
@Transactional(readOnly = true)                     //readOnly = true일 경우, 최적화가 이루어짐
@RequiredArgsConstructor                            //6. final이 있는 Field만 생성자 생성 (*****)
public class MemberService {

    //1. Field Injection : 많이 썼던 방식 => 테스트 등 변경이 필요할 때 변경 불가한 단점
    //@Autowired
    //private MemberRepository memberRepository;
    
    private final MemberRepository memberRepository;        //변경할 일이 없으므로, final로 설정 권장
                                                            //Compile 시점에 체크

    //2. Setter Injection => 변경 가능 // 누군가 Runtime 시점에 바꿀 수 있는 단점, 실제 개발 중 변경할 일이 별로 없음
    //@Autowired
    //public void setMemberRepository(MemberRepository memberRepository) {
    //    this.memberRepository = memberRepository;
    //}

    //3. Constructor Injection => 중간에 변경 불가, TestCase 작성 시 Mock주입 등 직접 주입을 필요로 하여 의존성 파악 가능 // 코드가 번거로운 단점
    //@Autowired
    //public MemberService(MemberRepository memberRepository) {
    //    this.memberRepository = memberRepository;
    //}

    //4. 생성자 1개만 있는 경우 Spring이 자동으로 Injection
    //public MemberService(MemberRepository memberRepository) {
    //    this.memberRepository = memberRepository;
    //}

    /**
     * 회원 가입
     */
    @Transactional                                  //읽기 전용이 많은 경우 전체를 readOnly = true로 설정하고, 쓰기 부분에만 따로 기재(default : false)
    public Long join(Member member){
        //중복회원 검증
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    //여러 명의 사용자가 동시에 등록하는 것을 막을 수 없다.
    //그러므로, 실무에서는 최후의 방어가 필요하다.
    //멀티 쓰레드 상황을 고려해서 DB의 Member Table의 Name을 Unique 제약조건 설정 등의 처리 필요
    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
