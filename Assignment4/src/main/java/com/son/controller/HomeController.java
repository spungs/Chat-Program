package com.son.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.son.service.chatService;

@Controller
public class HomeController {
	@Autowired
	chatService service;
	
	// 첫 화면 (유저의 이름 입력하여 프로그램 시작하기)
	@RequestMapping("chatApp")
	public void chatApp(HttpSession session) {
		session.invalidate();
	}
	
	@GetMapping("roomList")
	public String roomList(Model model, RedirectAttributes ra, HttpServletRequest req, 
							String isDelete, String roomName) {
		System.out.println("roomList getMapping");
		HttpSession session= req.getSession();
		String isSession = (String) session.getAttribute("userName");
		
		if (isSession == null) {
			ra.addFlashAttribute("msg", "사용자를 입력하세요.");
			return "redirect:chatApp";
		}
		
		model.addAttribute("roomList", service.getRoomList());
		if (isDelete != null) {
			model.addAttribute("roomName", roomName);
			model.addAttribute("msg", isDelete);
		}
		
		return "roomList";
	}
	
	@PostMapping("roomList")
	public String roomList(RedirectAttributes ra, Model model, HttpServletRequest req, 
							String userName, String roomName, String isDelete) {
		System.out.println("roomList postMapping");
		HttpSession session= req.getSession();
		session.setAttribute("userName", userName);
		
		model.addAttribute("roomList", service.getRoomList());
		
		return "redirect:roomList";
	}

	@RequestMapping("chat")
	public ModelAndView chat(String userName, String roomName, 
							RedirectAttributes ra, HttpServletRequest req) {
		HttpSession session= req.getSession();
		String isSession = (String) session.getAttribute("userName");
		
		if (isSession == null) {
			ModelAndView mv = new ModelAndView("chatApp");
			mv.addObject("msg", "사용자를 입력하세요.");
			return mv;
		}
		
		ModelAndView mv = new ModelAndView("chat"); // mv.setViewName("chat");
		mv.addObject("userName", userName);
		mv.addObject("roomName", roomName);
		
		return mv;
	}

	@RequestMapping(value = "addList", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, String> addRoom(@RequestBody Map<String, String> JsonReqData) {
		String roomName = JsonReqData.get("roomName");
		String userName = JsonReqData.get("userName");
		// test print
//		System.out.println("roomName : " + roomName);
//		System.out.println("userName : " + userName);
		
		Map<String, String> roomInfoMap = new HashMap<>();
		
		String checkAdd = Integer.toString(service.addList(roomName, userName));
		roomInfoMap.put("roomName", roomName);
		roomInfoMap.put("checkAdd", checkAdd);

		return roomInfoMap;
	}

	@RequestMapping("deleteRoom")
	public String deleteRoom(RedirectAttributes ra, String userName, String roomName) {
		// test print
		System.out.println("roomName : " + roomName);
		System.out.println("userName : " + userName);
		
		int result = service.deleteRoom(roomName, userName);
		
		return "redirect:roomList?isDelete=" + result + "&roomName=" + roomName;
	}
	
	// encode
	@RequestMapping(value = "encode", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, String> encode(@RequestBody Map<String, String> JsonReqData) {
		// ajax로 받은 json 형태의 html 코드
		String html = JsonReqData.get("html");
		String method = JsonReqData.get("method");
		
		// test print (OK) ========delete===========
//		System.out.println("html: \n" + html);
//		System.out.println(method);
		
		String newHtml = service.encode(html, method);
		
		JsonReqData.put("html", newHtml);
		
//		System.out.println("newHTML: \n " + newHtml);
		
		return JsonReqData;
	}
}
