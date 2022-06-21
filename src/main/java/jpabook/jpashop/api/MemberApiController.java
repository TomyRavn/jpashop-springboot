package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /** version 1 : 문제 코드 **/
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        //Entity는 여러 곳에서 사용될 수 있으므로 스펙이 언제든 바뀔 수 있다.
        //그러나 현재 상태에서는 Entity 스펙이 바뀌면 API를 사용하는 클라이언트 측에서 문제가 발생한다.
        //따라서 Entity에서 바로 처리하는 것이 아닌, API를 위한 별도의 DTO를 생성해주어야 한다.
        //뿐만 아니라, 등록 API만 해도 여러 개의 Case가 존재할 수 있다.
        //즉, Entity를 외부에 노출하여 사용하는 것을 지양해야 한다.
        //( = API를 생성할 때는 Entity가 Parameter가 되어서는 안된다!!
        //  = Entity를 외부에 노출해서는 안된다!! )
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }
    /** end of version 1 **/

    /*
    회원 등록 API
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /*
    회원 수정 API
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        //command
        memberService.update(id , request.getName());
        //query
        Member findMember = memberService.findOne(id);
        //Command 와 Query를 분리하여 사용 => 유지보수성 증대
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    /*
    회원 조회 API
     */
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    /**
     * JSON 배열 타입의 유연성을 높이기 위해, 한번 감싸서 내보내는 용도
     * {
     *     "count": 4,
     *     "data": [
     *         {
     *              "id": 1,
     *              "name": "test"
     *         }
     *     ]
     * }
     * 형식으로 내보내기 위함
     *
     * -> 기존 :
     * [
     *     {
     *         "id": 1,
     *         "name": "test"
     *     }
     * ]
     */
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    /*
    DTO : API 스펙과 1대1로 매칭(*** Entity가 API 스펙과 1대1로 매칭되어서는 안됨)
     */
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

}
