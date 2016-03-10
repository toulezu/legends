package com.tongbanjie.legends.server.service.impl;

import com.tongbanjie.legends.server.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author sunyi
 */
@Service
public class SmsServiceImpl implements SmsService {

	private Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);


	@Override
	public void sendAlertSms(String phone, Long jobInfoId, String jobInfoName, String errorMessage) {

		//TODO  send sms

	}
}
