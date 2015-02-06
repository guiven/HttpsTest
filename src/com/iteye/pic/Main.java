package com.iteye.pic;

import java.io.File;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OCR ocr = new OCR();
		try {
//			System.out.println(ocr.recognizeText(new File("chenjie\\9.png"), "png"));
			for(int i=20;i<30;i++){
				ocr.recognizeText(new File("chenjie"+i+".png"), "png");
			}
//			System.out.println(ocr.recognizeText(new File("https://www.gewara.com/captcha.xhtml"), "jpeg"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
