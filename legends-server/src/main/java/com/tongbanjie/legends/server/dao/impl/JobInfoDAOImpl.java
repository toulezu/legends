package com.tongbanjie.legends.server.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.tongbanjie.legends.server.dao.JobInfoDAO;
import com.tongbanjie.legends.server.dao.dataobject.JobInfo;
import org.springframework.util.Assert;

@Repository
public class JobInfoDAOImpl extends SqlSessionDaoSupport implements JobInfoDAO {

	@Override
	@SuppressWarnings("unchecked")
	public List<JobInfo> getList(JobInfo jobInfo) {
		return getSqlSession().selectList("JobInfo.getList", jobInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobInfo> getListByNameAndGroup(String name, String group) {
		Map<String, String> map = new HashMap<String, String>(2);
		map.put("name", name);
		map.put("group", group);
		return getSqlSession().selectList("JobInfo.getListByNameAndGroup", map);
	}

	public JobInfo getByNameAndGroup(String name, String group) {

		Assert.hasText(name);
		Assert.hasText(group);

		Map<String, String> map = new HashMap<String, String>(2);
		map.put("name", name);
		map.put("group", group);
		return (JobInfo) getSqlSession().selectOne("JobInfo.getByNameAndGroup", map);

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<JobInfo> findActivityList() {
		return getSqlSession().selectList("JobInfo.findActivityList");
	}

	@Override
	public int insert(JobInfo jobInfo) {

		Assert.notNull(jobInfo);

		jobInfo.setCreateTime(new Date());
		jobInfo.setModifyTime(new Date());
		return getSqlSession().insert("JobInfo.insert", jobInfo);
	}

	@Override
	public int updateById(JobInfo jobInfo) {

		Assert.notNull(jobInfo);
		Assert.notNull(jobInfo.getId());

		if (jobInfo.getParam() == null) {
			jobInfo.setParam("");
		}

		jobInfo.setModifyTime(new Date());
		return getSqlSession().update("JobInfo.updateById", jobInfo);
	}

	@Override
	public int deleteById(long id) {
		return getSqlSession().delete("JobInfo.deleteById", id);
	}

	@Override
	public JobInfo findById(long id) {
		return (JobInfo) getSqlSession().selectOne("JobInfo.findById", id);
	}

	@Override
	public JobInfo findByIdForUpdate(long id) {
		return (JobInfo) getSqlSession().selectOne("JobInfo.findByIdForUpdate", id);
	}

}
