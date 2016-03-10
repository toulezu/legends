package com.tongbanjie.legends.server.service;

import org.springframework.stereotype.Service;

/**
 * @author sunyi
 *         Created on 15/11/10
 */
public interface SmsService {


	/**
	 * 发送报警短信
	 * @param phone
	 * @param jobInfoId
	 * @param jobInfoName
	 * @param errorMessage
	 */
	void sendAlertSms(String phone, Long jobInfoId, String jobInfoName, String errorMessage);


}
