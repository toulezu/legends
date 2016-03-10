package com.tongbanjie.legends.server.service;

import java.util.List;

import com.tongbanjie.legends.server.dao.dataobject.JobInfoHistory;
import com.tongbanjie.legends.server.utils.Result;

/**
 * 
 * @author chen.jie
 * 
 */
public interface JobInfoHistoryService {

	/**
	 * 根据name、group查询,默认展示任务列表也会用到
	 * 
	 * @param name
	 * @param group
	 * @return
	 */
	Result<List<JobInfoHistory>> selectListByNameAndGroup(String name, String group);
	
	Result<JobInfoHistory> selectJobInfoHistoryById(long id);

}
