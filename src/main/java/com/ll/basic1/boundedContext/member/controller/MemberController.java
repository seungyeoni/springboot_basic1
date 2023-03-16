package com.ll.basic1.boundedContext.member.controller;

import com.ll.basic1.base.rq.Rq;
import com.ll.basic1.base.rsData.RsData;
import com.ll.basic1.boundedContext.member.entity.Member;
import com.ll.basic1.boundedContext.member.service.MemberService;
import lombok.AllArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/member/login")
    // @ResponseBody를 빼야 아래 메소드 안에 경로로 인식함.
    public String showLogin() {
        return "usr/member/login";
    }

    @PostMapping("/member/login")
    @ResponseBody
    public RsData login(String username, String password) {

        if ( username == null || username.trim().length() == 0 ) {
            return RsData.of("F-3", "username(을)를 입력해주세요.");
        }

        if ( password == null || password.trim().length() == 0 ) {
            return RsData.of("F-4", "password(을)를 입력해주세요.");
        }

        /*resp.addCookie(new Cookie("username", username));
        return memberService.tryLogin(username, password);*/
        RsData rsData = memberService.tryLogin(username, password);

        if (rsData.isSuccess()) {
            Member member = (Member) rsData.getData();
            rq.setSession("loginedMemberId", member.getId());
        }

        return rsData;
    }

    @GetMapping("/member/logout")
    @ResponseBody
    public RsData logout() {
        boolean cookieRemoved = rq.removeSession("loginedMemberId");

        if (cookieRemoved == false) {
            return RsData.of("S-2", "이미 로그아웃 상태입니다.");
        }

        return RsData.of("S-1", "로그아웃 되었습니다.");
    }

    @GetMapping("/member/me")
    public String showMe(Model model) {
        long loginedMemberId = rq.getLoginedMemberId();

        Member member = memberService.findById(loginedMemberId);

        model.addAttribute("member", member);

        return "usr/member/me";
    }

    // 디버깅용 함수
    @GetMapping("/member/session")
    @ResponseBody
    public String showSession() {
        return rq.getSessionDebugContents().replaceAll("\n", "<br>");
    }

    /* //내가 작성한 코드
    @GetMapping("/member/me")
    @ResponseBody
    public RsData showCookielogin(HttpServletRequest req) throws IOException { // 리턴되는 int 값은 String 화 되어서 고객(브라우저)에게 전달된다.
        String usernameCookie;
        if (req.getCookies() != null) {
            usernameCookie = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("username"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("null");
            return RsData.of("S-1", "당신의 username(은)는 %s 입니다." .formatted(usernameCookie));
        }

        return RsData.of("F-1", "로그인 후 이용해주세요.");
    }*/
}
