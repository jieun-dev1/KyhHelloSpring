package hello.hellospring.Service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
//    private MemberRepository memberRepository;


    //수정자 주입: 한번 세팅 되면 잘 안바꾸기 때문에 자주 쓰이지 x
    //누구나 수정 가능하게 열려있다는 점. 개발은 최대한 변경 가능점 줄이는게 중요
    //생성시점에 설정하고, 변경하지 못하도록 하는 게 낫다.
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    //memberRepository를 외부에서 넣어줌. 의존성 주입 Di.
    @Autowired
    public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
    }

    /**
     * 회원가입 
     */
    
    public Long join(Member member) {
    //같은 이름이 있는 중복 회원 x
    //과거에는 if null 로 체크를 했다면, 지금은 null 일 가능성이 있는 경우 처음부터 Optional로 감싸준다.
//        Optional<Member> result = memberRepository.findByName(member.getName());
//        result.ifPresent(m -> {
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        });

        //더 간단히 (Optional 바로 반환하는 게 좋지 않다.
        validateDuplicateMember(member); //중복 회원 검증 메서드로 빼주자.
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
        .ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }

    /**
     * 전체 회원 조회
     * @return
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
