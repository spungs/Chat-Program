package com.son.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sound.midi.Soundbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
		// mybatis seminar test method
//		service.ChooseWhen();
	}
	
	@GetMapping("roomList")
	public String roomList(Model model, RedirectAttributes ra, HttpServletRequest req, 
							String isDelete, String roomName) {
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
		
		HttpSession session= req.getSession();
		session.setAttribute("userName", userName);
		
		/* 지워도 될거같음(90%)
		 * 
		// null 처리 필요 여부에 따라 수정
//        if(service.getRoomList() != null) {
//            model.addAttribute("roomList", service.getRoomList());
//        }
		
//		ra.addFlashAttribute("roomList", service.getRoomList());
//		ra.addFlashAttribute("userName", userName);
//		ra.addFlashAttribute("roomName", roomName);
//		if (isDelete != null) {
//			ra.addFlashAttribute("msg", isDelete);
//		}
		*/
		model.addAttribute("roomList", service.getRoomList());
		
		return "redirect:roomList";
	}

	@RequestMapping("chat")
	public ModelAndView chat(String userName, String roomName) {
		ModelAndView mv = new ModelAndView("chat"); // mv.setViewName("chat");
		mv.addObject("userName", userName);
		mv.addObject("roomName", roomName);
		
		return mv;
	}

	@RequestMapping("addList")
	@ResponseBody
	public Map<String, String> addRoom(@RequestBody String roomName) {
		Map<String, String> roomInfoMap = new HashMap<>();
		String checkAdd = Integer.toString(service.addList(roomName));
		roomInfoMap.put("roomName", roomName);
		roomInfoMap.put("checkAdd", checkAdd);

		return roomInfoMap;
	}

	@RequestMapping("deleteRoom")
	public String deleteRoom(RedirectAttributes ra, String userName, String roomName) {
		// test print
//		System.out.println("roomName : " + roomName);
		
		int result = service.deleteRoom(roomName);
		
		/* 지워도 될거 같음(80%)
//		ra.addAttribute("userName", userName);	// session을 대체함
//		ra.addAttribute("isDelete", result);	// Get방식으로 전달
//		ra.addAttribute("roomName", roomName);	// Get방식으로 전달
		
//		return "redirect:roomList";
	 	*/
		return "redirect:roomList?isDelete=" + result + "&roomName=" + roomName;
	}
}
