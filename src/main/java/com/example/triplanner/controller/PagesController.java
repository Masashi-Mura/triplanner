package com.example.triplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

	/**  最終版のコード
    @GetMapping("/")
    public String index() {
        return "pages/index";
    }
    */
	
	//開発用コード(return の値を変更して使用)
    @GetMapping("/")
    public String index() {
        return "pages/index";
    }
    //開発用コード  ここまで。
}
