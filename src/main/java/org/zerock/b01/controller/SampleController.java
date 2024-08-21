package org.zerock.b01.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model){

        log.info("hello...........");

        model.addAttribute("msg", "HELLO WORLD");
    }

    @GetMapping("/ex/ex1")
    public void ex1(Model model){
        List<String> list = Arrays.asList("AAA", "BBB", "CCC", "DDD");

        model.addAttribute("list", list);

        /*
           * 타임리프를 통해 인라인으로 표시할때 [[${list}]]
           * 속성 th:..
           * 이 두 가지 에서 사용하면 됨.
         */

        // 타임리프 주석 처리 <!--/*... */-->
        // 임시 변수 th:with="temp = ${10}" 이런식으로 설정해서 사용가능.

        /*
         * 인라인 기능으로 자바스크립트 다루기
         */
    }

    @Getter
    class SampleDTO{
        private String p1,p2,p3;
    }

    @GetMapping("/ex/ex2")
    public void ex2(Model model){
        log.info("ex/ex2....");

        List<String> strList = IntStream.range(1,10)
                .mapToObj(i -> "Data"+i)
                .collect(Collectors.toList());
        model.addAttribute("list", strList);

        Map<String, String> map = new HashMap<>();
        map.put("A", "AAAA");
        map.put("B", "BBBB");

        model.addAttribute("map", map);

        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.p1 = "Value -- p1";
        sampleDTO.p2 = "Value -- p2";
        sampleDTO.p3 = "Value -- p3";

        model.addAttribute("dto", sampleDTO);
    }

    @GetMapping("/ex/ex3")
    public void ex3(Model model){
        model.addAttribute("arr", new String[]{"AAA","BBB","CCC"});
    }
}
