package com.tongbanjie.legends.server.test;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.tongbanjie.legends.client.core.AbstractJob;

/**
 * 测试停止功能
 * @author san.feng
 *
 */
@Component
public class ClientAbstractJobTest extends AbstractJob {

	@Override
	public String execute(String param) {
		System.out.println(param);
		
		while(!isTerminated()) {
			System.out.println("======================");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "interrupt";
			}
		}
		
		
		return "success";
	}

}
