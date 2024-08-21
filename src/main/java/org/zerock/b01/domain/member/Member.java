package org.zerock.b01.domain.member;

import lombok.*;
import org.zerock.b01.domain.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *  Reply나 Board와 연관관계를 맺으면 모놀리식 구조로 나중에 별도 서비스 분리가 어려워서
 *  MSA를 염두에 둔다면 연관관계 없이 설정한다.
 */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "roleSet")
public class Member extends BaseEntity {

    @Id
    private String mid;

    private String mpw;
    private String email;
    private boolean del; // 회원 탈퇴 여부
    private boolean social; // 소셜 로그인 여부

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default // HashSet을 자동으로 만듬.
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String mpw){
        this.mpw = mpw;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeDel(boolean del){
        this.del = del;
    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void clearRoles(){
        this.roleSet.clear();
    }

    public void changeSocial(boolean social){
        this.social = social;
    }
}
