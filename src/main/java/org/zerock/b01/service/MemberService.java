package org.zerock.b01.service;

import org.zerock.b01.dto.member.MemberJoinDTO;

public interface MemberService {

    // 예외를 인터페이스 내부에서 처리
    static class MidExistException extends Exception{
    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
}
