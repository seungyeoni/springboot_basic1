package com.ll.basic1.boundedContext.member.controller;

import com.ll.basic1.base.rsData.RsData;
import com.ll.basic1.boundedContext.member.entity.Member;
import com.ll.basic1.boundedContext.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
public class MemberController {
    private final MemberService memberService;

    // 생성자 주입
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/login")
    @ResponseBody
    public RsData login(String username, String password, HttpServletResponse resp) {
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
            resp.addCookie(new Cookie("loginedMemberId", member.getId() + ""));
        }

        return rsData;
    }

    @GetMapping("/member/logout")
    @ResponseBody
    public RsData logout(HttpServletRequest req, HttpServletResponse resp) {
        Rq rq = new Rq(req, resp);
        rq.removeCookie("loginedMemberId");

        return RsData.of("S-1", "로그아웃 되었습니다.");
    }

    @GetMapping("/member/me")
    @ResponseBody
    public RsData showMe(HttpServletRequest req) {
        long loginedMemberId = 0;

        if (req.getCookies() != null) {
            loginedMemberId = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("loginedMemberId"))
                    .map(Cookie::getValue)
                    .mapToLong(Long::parseLong)
                    .findFirst()
                    .orElse(0);
        }

        boolean isLogined = loginedMemberId > 0;

        if (isLogined == false)
            return RsData.of("F-1", "로그인 후 이용해주세요.");

        Member member = memberService.findById(loginedMemberId);

        return RsData.of("S-1", "당신의 username(은)는 %s 입니다.".formatted(member.getUsername()));
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
