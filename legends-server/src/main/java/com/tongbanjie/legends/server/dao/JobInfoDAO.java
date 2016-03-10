package com.tongbanjie.legends.server.dao;

import java.util.List;

import com.tongbanjie.legends.server.dao.dataobject.JobInfo;

public interface JobInfoDAO {

	/**
	 * 动态查询符合条件的数据
	 *
	 * @param jobInfo
	 * @return
	 */
	List<JobInfo> getList(JobInfo jobInfo);

	/**
	 * 根据name和group查询,默认展示任务列表也会用到
	 *
	 * @param name
	 * @param group
	 * @return
	 */
	List<JobInfo> getListByNameAndGroup(String name, String group);

	/**
	 * 两个参数都必须传
	 * @param name
	 * @param group
	 * @return
	 */
	JobInfo getByNameAndGroup(String name, String group);

	/**
	 * 查找is_activity=1的数据
	 *
	 * @return
	 */
	List<JobInfo> findActivityList();

	int insert(JobInfo jobInfo);

	int updateById(JobInfo jobInfo);

	int deleteById(long id);

	JobInfo findById(long id);

	JobInfo findByIdForUpdate(long id);
}
