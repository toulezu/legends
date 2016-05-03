package com.tongbanjie.legends.server.component;

import com.alibaba.fastjson.JSON;
import com.tongbanjie.legends.client.enums.JobStatus;
import com.tongbanjie.legends.client.enums.MethodFlag;
import com.tongbanjie.legends.client.model.JobExecutingResponse;
import com.tongbanjie.legends.client.model.JobRequest;
import com.tongbanjie.legends.client.model.JobResult;
import com.tongbanjie.legends.server.dao.JobInfoDAO;
import com.tongbanjie.legends.server.dao.JobSnapshotDAO;
import com.tongbanjie.legends.server.dao.dataobject.JobInfo;
import com.tongbanjie.legends.server.dao.dataobject.JobSnapshot;
import com.tongbanjie.legends.server.dao.dataobject.enums.JobInfoTypeEnum;
import com.tongbanjie.legends.server.dao.dataobject.enums.JobSnapshotStatusEnum;
import com.tongbanjie.legends.server.service.JobInfoService;
import com.tongbanjie.legends.server.service.SmsService;
import com.tongbanjie.legends.server.utils.HttpClientUtils;
import com.tongbanjie.legends.server.utils.Result;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 处理正在执行JOB组件
 *
 * @author sunyi
 */
@Component
public class JobExecutingComponent {

	private Logger logger = LoggerFactory.getLogger(JobInvokeComponent.class);
	protected static int TIME_OUT = 1 * 1000;

	@Autowired
	private JobInfoDAO jobInfoDAO;

	@Autowired
	private JobInfoService jobInfoService;

	@Autowired
	private JobSnapshotDAO jobSnapshotDAO;

	@Autowired
	private SmsService smsService;

	@Transactional(rollbackFor = {Throwable.class})
	public boolean handleExecuting(Long jobSnapshotId) {

		JobSnapshot jobSnapshot = null;
		JobInfo jobInfo = null;

		boolean isJobExecuting = false;

		try {

			jobSnapshot = jobSnapshotDAO.findByIdForUpdate(jobSnapshotId);

			if (!JobSnapshotStatusEnum.EXECUTING.equals(jobSnapshot.getStatus())) {
				return isJobExecuting;
			}

			Long jobInfoId = jobSnapshot.getJobInfoId();
			jobInfo = jobInfoDAO.findById(jobInfoId);

			if (jobInfo == null) {
				logger.warn("JobSnapshot ID:[" + jobSnapshotId + "] 获取任务结果时，JobInfo == null");
				jobFail("获取任务结果时，JobInfo == null", null, jobSnapshot);
				return isJobExecuting;
			}

			if (!jobInfo.isActivity()) {
				logger.warn("JobSnapshot ID:[" + jobSnapshotId + "] 获取任务结果时，JobInfo 是不激活状态");
				jobFail("获取任务结果时，JobInfo 是不激活状态", jobInfo, jobSnapshot);
				return isJobExecuting;
			}

			JobExecutingResponse exeRes = null;

			try {
				// 获取任务执行情况或者结果.
				exeRes = getExecuteResult(jobInfo, jobSnapshot);
			} catch (Exception e) {
				logger.warn("获取任务结果时发生异常, jobSnapshotId: " + jobSnapshotId, e);
				return true;
				// 获取任务执行结果时发生异常，有可能是网络问题，目标服务器假死等。
				// 不希望直接把任务标记为失败，最终还是目标服务器情况为准。
			}

			JobStatus jobStatus = exeRes.getJobStatus();

			if (jobStatus.equals(JobStatus.EXECUTING)) { // 任务执行中
				isJobExecuting = true;
			} else if (jobStatus.equals(JobStatus.FINISHED)) { // 任务完成
				JobResult jobResult = exeRes.getJobResult();
				handleJobCompleted(jobResult, jobInfo, jobSnapshot);
			} else if (jobStatus.equals(JobStatus.UNKNOW)) { // 目标服务器没有找到任务.
				handleJobUnknow(jobInfo, jobSnapshot);
			}

		} catch (Exception e) {
			logger.error("获取任务结果时发生异常, jobSnapshotId: " + jobSnapshotId, e);
		}
		return isJobExecuting;

	}

	/**
	 * 获取任务执行情况或者结果.
	 */
	protected JobExecutingResponse getExecuteResult(JobInfo jobInfo, JobSnapshot jobSnapshot) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		JobRequest req = new JobRequest();
		req.setJobDetailId(jobSnapshot.getId());
		req.setMethodFlag(MethodFlag.EXECUTING);
		req.setClassFullPath(jobInfo.getClassPath());
		String reqBody = JSON.toJSONString(req);

		String resBody = HttpClientUtils.post(jobSnapshot.getUrl(), reqBody, "application/json", "utf-8", TIME_OUT, TIME_OUT);

		JobExecutingResponse exeRes = JSON.parseObject(resBody, JobExecutingResponse.class);
		return exeRes;
	}


	/**
	 * 任务失败时.
	 */
	protected void jobFail(String errorMessage, JobInfo jobInfo, JobSnapshot jobSnapshot) {
		String detail = "任务失败：" + errorMessage + getNowTime() + "\n";

		JobSnapshot update = new JobSnapshot();
		update.setId(jobSnapshot.getId());
		update.setResult(errorMessage);
		update.setStatus(JobSnapshotStatusEnum.ERROR);
		update.setTimeConsume(0L);
		update.setDetail(jobSnapshot.getDetail() + detail);
		jobSnapshotDAO.updateById(update);

		if (jobInfo != null) {
			JobInfoTypeEnum type = jobInfo.getType();
			if (type.equals(JobInfoTypeEnum.ONCE)) {
				Result<Boolean> result = jobInfoService.deleteJobInfoById(jobInfo.getId());
				if (!result.isSuccess()) {
					logger.error("jobInfoService.deleteById fail! " + result.getErrorMsg());
				}
			}
		}

		if (jobInfo != null && jobInfo.getOwnerPhone() != null) {
			smsService.sendAlertSms(jobInfo.getOwnerPhone(), jobInfo.getId(), jobInfo.getName(), jobSnapshot.getId(), errorMessage);
		}
	}

	/**
	 * 处理任务完成的情况.
	 */
	protected void handleJobCompleted(JobResult jobResult, JobInfo jobInfo, JobSnapshot jobSnapshot) {
		if (jobResult.isSuccess()) {
			String detail = "任务已结束,并且执行成功. 结果[" + jobResult.getResult() + "] " + getNowTime() + "\n";

			JobSnapshot update = new JobSnapshot();
			update.setId(jobSnapshot.getId());
			update.setStatus(JobSnapshotStatusEnum.COMPLETED);
			update.setResult(jobResult.getResult());
			update.setTimeConsume(jobResult.getTimeConsume());
			update.setDetail(jobSnapshot.getDetail() + detail);

			jobSnapshotDAO.updateById(update);

		} else {
			String detail = "任务已结束,但执行时发生异常. 异常信息[" + jobResult.getResult() + "] " + getNowTime() + "\n";

			JobSnapshot update = new JobSnapshot();
			update.setId(jobSnapshot.getId());
			update.setStatus(JobSnapshotStatusEnum.ERROR);
			update.setResult("任务已结束,但执行时发生异常. 异常信息[" + jobResult.getResult() + "] ");
			update.setTimeConsume(jobResult.getTimeConsume());
			update.setDetail(jobSnapshot.getDetail() + detail);

			jobSnapshotDAO.updateById(update);
		}

		JobInfoTypeEnum type = jobInfo.getType();
		if (type.equals(JobInfoTypeEnum.ONCE)) {
			Result<Boolean> result = jobInfoService.deleteJobInfoById(jobInfo.getId());
			if (!result.isSuccess()) {
				logger.error("jobInfoService.deleteById fail! " + result.getErrorMsg());
			}
		}
	}

	/**
	 * 在目标服务器,获取不到任务信息时.
	 */
	protected void handleJobUnknow(JobInfo jobInfo, JobSnapshot jobSnapshot) {
		jobFail("目标服务器有没有这条任务执行的记录或结果", jobInfo, jobSnapshot);
	}


	/**
	 * 获取现在的时间 yyyy-MM-dd HH:mm:ss
	 */
	private String getNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

}
