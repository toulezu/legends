package com.tongbanjie.legends.server.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tongbanjie.legends.server.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tongbanjie.legends.server.dao.JobInfoHistoryDAO;
import com.tongbanjie.legends.server.dao.dataobject.JobInfoHistory;
import com.tongbanjie.legends.server.service.JobInfoHistoryService;

/**
 * 
 * @author chen.jie
 *
 */
@Service
public class JobInfoHistoryServiceImpl implements JobInfoHistoryService {

	private static final Logger LOG = LoggerFactory.getLogger(JobInfoHistoryServiceImpl.class);
	
	@Resource
	private JobInfoHistoryDAO jobInfoHistoryDAO;
	
	@Override
	public Result<List<JobInfoHistory>> selectListByNameAndGroup(
			String name, String group) {
		Result<List<JobInfoHistory>> result = new Result<List<JobInfoHistory>>();
		
		try {
			List<JobInfoHistory> jobInfoHistoryList = jobInfoHistoryDAO.getListByNameAndGroup(name, group);
			result.setSuccess(true);
			result.setData(jobInfoHistoryList);
		} catch (Exception e) {
			LOG.error("Legends SysException: ", e);
			result.setSuccess(false);
			result.setErrorMsg("系统异常，请联系开发人员！");
		}
		
		return result;
	}

	@Override
	public Result<JobInfoHistory> selectJobInfoHistoryById(long id) {
		Result<JobInfoHistory> result = new Result<JobInfoHistory>();
		
		if (id <= 0L) {
			result.setSuccess(false);
			result.setErrorMsg("参数不合法！");
			return result;
		}
		
		try {
			JobInfoHistory jobInfoHistory =jobInfoHistoryDAO.findJobInfoHistoryById(id);
			result.setSuccess(true);
			result.setData(jobInfoHistory);
		} catch (Exception e) {
			LOG.error("Legends SysException: ", e);
			result.setSuccess(false);
			result.setErrorMsg("系统异常，请联系开发人员！");
		}
		
		return result;
	}

}
