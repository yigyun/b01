package org.zerock.b01.security.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 소셜 로그인 성공 후에 비밀번호 수정하는 페이지로 이동시킴
 *
 */

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("------------------------------------------------------------");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess---------------------");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        String encodePw = memberSecurityDTO.getMpw();

        // 소셜 로그인이고 회원의 패스워드는 1111
        if (memberSecurityDTO.isSocial() && (memberSecurityDTO.getMpw().equals("1111")
                || passwordEncoder.matches("1111", memberSecurityDTO.getMpw()))) {
            log.info("Should Change Password");

            log.info("Redirect to Member Modify");
            response.sendRedirect("/member/modify");
            return;
        } else {
            response.sendRedirect("/board/list");
        }
    }
}
