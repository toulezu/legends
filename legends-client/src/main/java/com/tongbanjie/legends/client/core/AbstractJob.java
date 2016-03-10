package com.tongbanjie.legends.client.core;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * 附带停止标志的JOB, 子类继承AbstractJob, 通过isTerminated(), 来判断是否停止任务的执行
 * 
 * @author san.feng
 * 
 * 1. 通过标志isTerminated()停止任务(需要任务开发者, 在任务中判断)<br>
 * 2. 如果任务线程处于阻塞中如sleep,wait,io等, 系统会停止该任务<br>
 * 
 * 注意: 因为是单例模式-所以停止的是JOB下的所有线程哈.<br>
 * 
 * 我知道的, 就这么多了.<br>
 * 
 */
public abstract class AbstractJob implements Job{

	
	/**
	 * 任务终止标志 
	 */
	private AtomicBoolean terminated = new AtomicBoolean(false);


	/**
	 * 判断是否继续执行任务
	 */
	public boolean isTerminated() {
		return this.terminated.get();
	}
	

	@Override
	public abstract String execute(String param) throws Exception;
	

	/**
	 * 停止任务操作
	 */
	public void stop() {
		this.terminated.set(true);
	}
	
	/**
	 * 停止任务操作
	 */
	public void open() {
		this.terminated.set(false);
	}
}
