package com.son.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.son.repository.ChatDao;

import dto.RoomDTO;

@ComponentScan
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired ChatDao chatDao;
    @Autowired Encrypt encryptService;

    @Override
    public ArrayList<RoomDTO> getRoomList() {
        ArrayList<RoomDTO> arr = chatDao.getRoomList();
        if (arr != null) {
            return arr;
        }
        return null;
    }
    
    @Override
	public String isRoom(String roomName) {
    	String isRoom = chatDao.isRoom(roomName);
		return isRoom;
	}
    
    @Override
	public String isSame(String encodeKey, String inputKey) {
    	System.out.println("isSame encodeKey: " + encodeKey);
    	String result = "false";
    	if (encodeKey.equals(inputKey)) {
    		result = "true";
    	}
		return result;
	}

    @Override
    public int addList(String roomName, String owner) {
    	// 존재하면 name, 없으면 null
    	System.out.println(roomName);
    	String isRoom = chatDao.isRoom(roomName);
    	if (isRoom == null) {
    		chatDao.insertNewRoom(roomName, owner);
    		return 1;
    	}
    	return 0;
    }
    
    @Override
	public int deleteRoom(String roomName, String owner) {
    	int result = chatDao.deleteRoom(roomName, owner);
    	if (result != 0) {
    		return result;
    	} else {
    		return 0;
    	}
		
	}

    // 입력받은 msg를 encoding하여 반환
    @Override
    public String encryptMsg(String msg) {
        // 1. msg를 char화 해서 char 하나씩 배열에서 인덱스를 찾아주고 i와 j를 서로 바꿔준다.
        // 2. msg에 i, j가 바뀐 문자를 추가해준다.(char to string해서)
        // string to charArr and toUpperCase
        char[] msgArr = msg.toUpperCase().toCharArray();
        // 암호화된 msg 생성
        String encodedMsg = "";

        for (int i = 0; i < msgArr.length; i++) {
            // msg와 encodedMsg 비교해서 대문자면 소문자로 변경
            if (Character.isUpperCase(msg.charAt(i))) {
                encodedMsg += encryptService.encoding(msgArr[i]).toLowerCase();
            } else {
                encodedMsg += encryptService.encoding(msgArr[i]).toUpperCase();
            }
        }
        System.out.println("인코딩된 msg : " + encodedMsg);
        return encodedMsg;
    }
    
    @Override
    public String encryptOrDecryptMsgs(String html, String method) {
		// Jsoup 라이브러리를 사용하여 HTML 코드 파싱
		Document doc = Jsoup.parse(html);
		// 특정 클래스를 가진 모든 요소 선택
		Elements leftMsg = doc.select("div.message.left");
		Elements rightMsg = doc.select("div.message.right");

		// left, right msg 텍스트 추출
		for (Element msg : leftMsg) {
			String text = "";
			if (method.equals("decode")) {
				text = msg.select("span.text.content.encode").text();
			} else if (method.equals("encode")) {
				text = msg.select("span.text.content.decode").text();
			}
//			System.out.println("msg \n" + msg);
			System.out.println("text : " + text);
			if (!text.equals("")) {
				int idx = text.indexOf(":");
				String user = text.substring(0, idx);
				text = text.substring(idx + 1);
				String decodedText = encryptMsg(text);
//				System.out.println("leftMsg : " + decodedText);
				if (method.equals("decode")) {
					msg.select("span.text.content.encode").first().text(user + " : " + decodedText);
				} else if (method.equals("encode")) {
					msg.select("span.text.content.decode").first().text(user + " : " + decodedText);
				}
			}
		}

		for (Element msg : rightMsg) {
			String text = "";
			if (method.equals("decode")) {
				text = msg.select("span.text.content.encode").text();
			} else if (method.equals("encode")) {
				text = msg.select("span.text.content.decode").text();
			}
			if (!text.equals("")) {
				String decodedText = encryptMsg(text);
//				System.out.println("rightMsg : " + decodedText);
				if (method.equals("decode")) {
					msg.select("span.text.content.encode").first().text(decodedText);
				} else if (method.equals("encode")) {
					msg.select("span.text.content.decode").first().text(decodedText);
				}
			}
		}
		String newHtml = doc.outerHtml();
		return newHtml;
	}

    

}
