package com.tongbanjie.legends.server.dao;

import java.util.List;

import com.tongbanjie.legends.server.dao.dataobject.JobInfoHistory;

/**
 * 
 * @author chen.jie
 *
 */
public interface JobInfoHistoryDAO {

	int insertJobInfoHistory(JobInfoHistory jobInfoHistory);
	
	/**
	 * 根据name、group查询,默认展示任务列表也会用到
	 * 
	 * @param name
	 * @param group
	 * @return
	 */
	List<JobInfoHistory> getListByNameAndGroup(String name, String group);
	
	JobInfoHistory findJobInfoHistoryById(Long id);
}
