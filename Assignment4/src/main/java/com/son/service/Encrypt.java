package com.son.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Encrypt {

	private String nonOverlapString = "";
	private char[] nonOverlapCharArr = null;
	// encodeArr에 알파벳을 넣기위한 ArrayList 생성
	private ArrayList<String> alphabetArr = new ArrayList<>();
	// encodeArr 5x5 2차원 배열
	private String[][] encodeArr = new String[5][5];

	// 생성자에서 encode 배열을 만듬
	public Encrypt(@Value("${encode.key}") String encodeKey) {
		// encodeKey 중복 character 제거와 uppercase 변환
		deduplicationToUpperCase(encodeKey);
		// string to char Array, alphabet Array Change the order
		ChangeTypeAndOrder();
		// 5x5 encode 배열 만들기
		makeEncodeArr();
	}

	// encodeKey 중복 character 제거와 uppercase 변환
	public void deduplicationToUpperCase(String encodeKey) {
		// 문장이 들어와도 key가 될 수 있도록 하기 위한 replace문 (현재 미사용)
//		encodeKey = encodeKey.replace(" ","");
		// encodeKey를 모두 대문자로 변경
		String upperEncodeKey = encodeKey.toUpperCase();
		// encodeKey의 중복 character 제거
		for (int i = 0; i < upperEncodeKey.length(); i++) {
			if (upperEncodeKey.indexOf(upperEncodeKey.charAt(i)) == i) {
				nonOverlapString += upperEncodeKey.charAt(i);
			}
		}
	}

	// string to char Array, alphabat Array Change the order
	public void ChangeTypeAndOrder() {
		// String to char[]
		nonOverlapCharArr = nonOverlapString.toCharArray();
		System.out.println(nonOverlapCharArr);
		// 알파벳 A-Y까지 array에 담기
		char lower = 'A';
		for (int i = 0; i < 25; i++) {
			alphabetArr.add(Character.toString(lower));
			lower++;
		}

		// encodeKey alphaArr에서 제거
		for (int i = 0; i < nonOverlapCharArr.length; i++) {
			String character = Character.toString(nonOverlapCharArr[i]);
			alphabetArr.remove(character);
		}

		// encodeKey 제거 후 alphaArr 출력 테스트
//		for (int i = 0; i < alphaArr.size(); i++) {
//			System.out.print(alphaArr.get(i));
//		}
//		System.out.println();
	}

	// 5x5 encode 배열 만들기
	public void makeEncodeArr() {
		int addNonOverlapCharArr = 0;
		int addAlphaArr = 0;
		int cntAlphabetArr = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (addNonOverlapCharArr != (25 - alphabetArr.size())) {
					encodeArr[i][j] = Character.toString(nonOverlapCharArr[cntAlphabetArr]);
					addNonOverlapCharArr++;
//                    System.out.print(" if : " + Character.toString(nonOverlapCharArr[cntAlphabetArr]) + "\n");
					cntAlphabetArr++;
				} else {
					encodeArr[i][j] = alphabetArr.get(addAlphaArr);
//					System.out.print(" else : " + alphabetArr.get(addAlphaArr) + "\n");
					addAlphaArr++;
				}
			}
		}
		// 배열에 어떻게 들어가 있는지 보기위한 출력
		System.out.println(Arrays.deepToString(encodeArr));
	}

	/*
	 * Happiness keyword 예시 
	 * [H, A, P, I, N] 
	 * [E, S, B, C, D] 
	 * [F, G, J, K, L] 
	 * [M, O, Q, R, T] 
	 * [U, V, W, X, Y]
	 */
	public String encoding(char msgChar) {
		// char가 아스키 코드로 되어있어서 String과 equals로 비교할 수 없음..
		String msg = Character.toString(msgChar);
		for (int i = 0; i < encodeArr.length; i++) {
			for (int j = 0; j < encodeArr.length; j++) {
				// msgArr와 encodeArr의 문자를 비교해 index 추출
				switch (msgChar) {
				case ' ':
					return " ";
				case 'Z':
					return "z";
				default:
					if (encodeArr[i][j].equals(msg)) {
						return encodeArr[j][i];
					}
					break;
				} // switch case
			} // for j
		} // for i
		return msg;
	}
}
