package com.tongbanjie.legends.server.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.tongbanjie.legends.server.dao.JobInfoHistoryDAO;
import com.tongbanjie.legends.server.dao.dataobject.JobInfoHistory;

@Repository
public class JobInfoHistoryDAOImpl extends SqlSessionDaoSupport implements JobInfoHistoryDAO {

	@Override
	public int insertJobInfoHistory(JobInfoHistory jobInfoHistory) {
		if(jobInfoHistory.getModifyTime() == null){
			jobInfoHistory.setModifyTime(new Date());
		}
		return getSqlSession().insert("JobInfoHistory.insert", jobInfoHistory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobInfoHistory> getListByNameAndGroup(String name, String group) {
		Map<String, String> map = new HashMap<String, String>(2);
		map.put("name", name);
		map.put("group", group);
		return getSqlSession().selectList("JobInfoHistory.getListByNameAndGroup", map);
	}

	@Override
	public JobInfoHistory findJobInfoHistoryById(Long id) {
		return (JobInfoHistory) getSqlSession().selectOne("JobInfoHistory.findJobInfoHistoryById", id);
	}


}
