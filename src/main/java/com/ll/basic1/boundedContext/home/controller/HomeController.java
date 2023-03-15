package com.ll.basic1.boundedContext.home.controller;

import com.ll.basic1.boundedContext.member.entity.Member;
import com.ll.basic1.boundedContext.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

// @Controller 의 의미
// 개발자가 스프링부트에게 말한다.
// 아래 있는 HomeController 는 컨트롤러이다.
@Controller
public class HomeController {
    private int i;
    private int peopleCount;
    private final List<People> peopleList;
    // 필드 주입
    private final MemberService memberService;

    public HomeController(MemberService memberService) {
        i = -1;
        peopleCount = 0;
        peopleList = new ArrayList<>();
        this.memberService = memberService;
    }

    // @GetMapping("/home/main") 의 의미
    // 개발자가 스프링부트에게 말한다.
    // 만약에 /home/main 이런 요청이 오면 아래 메서드를 실행해줘
    @GetMapping("/home/main")
    // @ResponseBody 의 의미
    // 아래 메서드를 실행한 후 그 리턴값을 응답으로 삼아줘
    @ResponseBody
    public String showMain() {
        return "안녕하세요!!";
    }

    @GetMapping("/home/main2")
    @ResponseBody
    public String showMain2() {
        return "반갑습니다.";
    }

    @GetMapping("/home/main3")
    @ResponseBody
    public String showMain3() {
        return "즐거웠습니다.";
    }

    @GetMapping("/home/increase")
    @ResponseBody
    public int increase() { // 리턴되는 int 값은 String화 되어서 고객에게 보여줌.
        i++;
        return i;
    }

    @GetMapping("/home/plus")
    @ResponseBody
    // @RequestParam 의 의미
    // 개발자가 스프링부트에게 말한다.
    // int a 는 쿼리스트링에서 a 파라미터의 값을 얻은 후 정수화 한 값이어야 한다.
    // @RequestParam 는 생략가능
    public int showPlus(@RequestParam(defaultValue = "0") int a, @RequestParam int b) {
        return a + b;
    }

    @GetMapping("/home/returnBoolean")
    @ResponseBody
    public boolean showReturnBoolean() {
        return true;
    }

    @GetMapping("/home/returnDouble")
    @ResponseBody
    public double showReturnDouble() {
        return Math.PI;
    }

    @GetMapping("/home/returnIntArray")
    @ResponseBody
    public int[] showReturnIntArray() {
        int[] arr = new int[]{10, 20, 30};

        return arr;
    }

    @GetMapping("/home/returnIntList")
    @ResponseBody
    public List<Integer> showReturnIntList() {
        List<Integer> list = new ArrayList<>() {{
            add(10);
            add(20);
            add(30);
        }};

        return list;
    }

    @GetMapping("/home/returnMap")
    @ResponseBody
    public Map<String, Object> showReturnMap() {
        Map<String, Object> map = new LinkedHashMap<>() {{
            put("id", 1);
            put("speed", 100);
            put("name", "카니발");
            put("relatedIds", new ArrayList<>() {{
                add(2);
                add(3);
                add(4);
            }});
        }};

        return map;
    }

    @GetMapping("/home/returnCar")
    @ResponseBody
    public Car showReturnCar() {
        Car car = new Car(1, 100, "카니발", new ArrayList<>() {{
            add(2);
            add(3);
            add(4);
        }});

        return car;
    }

    @GetMapping("/home/returnCarV2")
    @ResponseBody
    public CarV2 showReturnCarV2() {
        CarV2 car = new CarV2(1, 100, "카니발", new ArrayList<>() {{
            add(2);
            add(3);
            add(4);
        }});

        car.setName(car.getName() + "V2");

        return car;
    }

    @GetMapping("/home/returnCarMapList")
    @ResponseBody
    public List<Map<String, Object>> showReturnCarMapList() {
        Map<String, Object> carMap1 = new LinkedHashMap<>() {{
            put("id", 1);
            put("speed", 100);
            put("name", "카니발");
            put("relatedIds", new ArrayList<>() {{
                add(2);
                add(3);
                add(4);
            }});
        }};

        Map<String, Object> carMap2 = new LinkedHashMap<>() {{
            put("id", 2);
            put("speed", 200);
            put("name", "포르쉐");
            put("relatedIds", new ArrayList<>() {{
                add(5);
                add(6);
                add(7);
            }});
        }};

        List<Map<String, Object>> list = new ArrayList<>();

        list.add(carMap1);
        list.add(carMap2);

        return list;
    }

    @GetMapping("/home/returnCarList")
    @ResponseBody
    public List<CarV2> showReturnCarList() {
        CarV2 car1 = new CarV2(1, 100, "카니발", new ArrayList<>() {{
            add(2);
            add(3);
            add(4);
        }});

        CarV2 car2 = new CarV2(2, 200, "포르쉐", new ArrayList<>() {{
            add(5);
            add(6);
            add(7);
        }});

        List<CarV2> list = new ArrayList<>();

        list.add(car1);
        list.add(car2);

        return list;
    }

    @GetMapping("/home/addPerson")
    @ResponseBody
    // @RequestParam 의 의미
    // 개발자가 스프링부트에게 말한다.
    // int a 는 쿼리스트링에서 a 파라미터의 값을 얻은 후 정수화 한 값이어야 한다.
    // @RequestParam 는 생략가능
    public String AddPerson(@RequestParam(defaultValue = "Noname") String name, @RequestParam int age) {
        peopleList.add(new People(name, age));
        peopleCount++;
        return peopleCount+"번 사람이 추가되었습니다.";
    }

    @GetMapping("/home/removePerson")
    @ResponseBody
    // @RequestParam 의 의미
    // 개발자가 스프링부트에게 말한다.
    // int a 는 쿼리스트링에서 a 파라미터의 값을 얻은 후 정수화 한 값이어야 한다.
    // @RequestParam 는 생략가능
    public String RemovePerson(int id) {
        boolean removed = peopleList.removeIf(person -> person.getId() == id);
        /* 스트림 안쓴 버전.
        for ( People p : peopleList ) {
            if (p.getId() == id) people.remove(p);
        }*/
        if(removed){
            return id+"번 사람이 삭제되었습니다.";
        }else{
            return id+"번 사람이 존재하지않습니다.";
        }

    }

    @GetMapping("/home/modifyPerson")
    @ResponseBody
    public String modifyPerson(int id, String name, int age) {
        boolean modify = false;
        for ( People p : peopleList ) {
            if (p.getId() == id) {
                p.setName(name);
                p.setAge(age);
                modify = true;
            }
        }
        if (modify) {
            return id + "번 사람이 수정되었습니다.";
        } else {
            return id + "번 사람이 존재하지않습니다.";
        }

    }

    @GetMapping("/home/people")
    @ResponseBody
    public List<People> showPeople() {
        return peopleList;
    }

    @GetMapping("/home/reqAndResp")
    @ResponseBody
    public void showReqAndResp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int age = Integer.parseInt(req.getParameter("age"));
        resp.getWriter().append("Hello, you are %d years old.".formatted(age));
    }

    @GetMapping("/home/cookie/increase")
    @ResponseBody
    public int showCookieIncrease(HttpServletRequest req, HttpServletResponse resp)throws IOException { // 리턴되는 int 값은 String 화 되어서 고객(브라우저)에게 전달된다.
        int countInCookie = 0;

        if (req.getCookies() != null) {
            countInCookie = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("count"))
                    .map(Cookie::getValue)
                    .mapToInt(Integer::parseInt)
                    .findFirst()
                    .orElse(0);
        }

        int newCountInCookie = countInCookie + 1;

        resp.addCookie(new Cookie("count", newCountInCookie + ""));

        return newCountInCookie;
    }
    @GetMapping("/home/user1")
    @ResponseBody
    public Member showUser1() {
        return memberService.findByUsername("user1");
    }
}

class Car {
    private final int id;
    private final int speed;
    private final String name;
    private final List<Integer> relatedIds;

    public Car(int id, int speed, String name, List<Integer> relatedIds) {
        this.id = id;
        this.speed = speed;
        this.name = name;
        this.relatedIds = relatedIds;
    }

    public int getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getRelatedIds() {
        return relatedIds;
    }
}

@AllArgsConstructor
@Getter
class CarV2 {
    private final int id;
    private final int speed;
    @Setter
    private String name;
    private final List<Integer> relatedIds;

}

@Getter
@Setter
@AllArgsConstructor
@ToString
class People{
    private static int lastId;
    private int id;
    private String name;
    private int age;

    static{
        lastId = 0;
    }

    People(String name, int age){
        this(++lastId, name, age);
    }
}