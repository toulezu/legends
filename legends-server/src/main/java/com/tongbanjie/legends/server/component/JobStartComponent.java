package com.tongbanjie.legends.server.component;

import com.tongbanjie.legends.server.dao.JobInfoDAO;
import com.tongbanjie.legends.server.dao.JobSnapshotDAO;
import com.tongbanjie.legends.server.dao.dataobject.JobInfo;
import com.tongbanjie.legends.server.dao.dataobject.JobSnapshot;
import com.tongbanjie.legends.server.dao.dataobject.enums.JobSnapshotStatusEnum;
import com.tongbanjie.legends.server.service.SmsService;
import com.tongbanjie.legends.server.utils.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 启动一个JOB
 *
 * @author sunyi
 */
@Component
public class JobStartComponent {

	private Logger logger = LoggerFactory.getLogger(JobStartComponent.class);

	@Autowired
	private JobInfoDAO jobInfoDAO;

	@Autowired
	private JobSnapshotDAO jobSnapshotDAO;

	@Autowired
	private SmsService smsService;

	@Transactional(rollbackFor = {Throwable.class})
	public JobSnapshot startJob(Long jobInfoId, boolean isTemporaryExecute) throws Exception {
		JobInfo jobInfo = null;
		JobSnapshot jobSnapshot = null;

		try {
			jobInfo = jobInfoDAO.findById(jobInfoId);

			if (jobInfo == null) {
				return null;
			}

			if (!jobInfo.isActivity()) {
				return null;
			}

			jobInfo = jobInfoDAO.findByIdForUpdate(jobInfoId);

			if (!isTemporaryExecute) { // 临时执行的任务，不校验一分钟限制。
				Date latestTriggerTime = jobInfo.getLatestTriggerTime();
				long latestTriggerTimeLong = latestTriggerTime == null ? 0L : latestTriggerTime.getTime();
				long now = (new Date()).getTime();
				if (now - latestTriggerTimeLong < 60 * 1000) {
					// 如果距离上次任务触发时间小于一分钟, 则不执行此次任务。
					// 原因是任务调度服务器是集群的， 防止多次触发任务。
					return null;
				}
			}

			logger.info("Start job, JobInfoId: " + jobInfoId);

			// 更新 jobInfo 信息
			updateJobInfo(jobInfoId);

			// 创建JobSnapshot
			jobSnapshot = createJobSnapshot(jobInfo);

		} catch (Exception e) {
			if (jobInfo != null && jobInfo.getOwnerPhone() != null) {
				smsService.sendAlertSms(jobInfo.getOwnerPhone(), jobInfoId, jobInfo.getName(), "任务启动失败！");
			}
			throw e; // 因为会把异常抛出去， 这里就不重复记日志了
		}

		return jobSnapshot;
	}

	private JobSnapshot createJobSnapshot(JobInfo jobInfo) {
		JobSnapshot jobSnapshot = new JobSnapshot();
		jobSnapshot.setJobInfoId(jobInfo.getId());
		jobSnapshot.setName(jobInfo.getName());
		jobSnapshot.setGroup(jobInfo.getGroup());
		jobSnapshot.setStatus(JobSnapshotStatusEnum.INIT);
		jobSnapshot.setServerAddress(NetUtils.getLocalAddressIp());
		jobSnapshot.setDetail("初始化 " + getNowTime() + "\n");
		jobSnapshotDAO.insert(jobSnapshot);

		return jobSnapshot;
	}

	private void updateJobInfo(Long jobInfoId) {
		JobInfo update = new JobInfo();
		update.setId(jobInfoId);
		update.setActivity(true);
		update.setLatestTriggerTime(new Date());
		update.setLatestServerAddress(NetUtils.getLocalAddressIp());

		jobInfoDAO.updateById(update);
	}

	/**
	 * 获取现在的时间 yyyy-MM-dd HH:mm:ss
	 */
	private String getNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}


}
