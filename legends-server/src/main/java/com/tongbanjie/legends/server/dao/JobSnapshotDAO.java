package com.tongbanjie.legends.server.dao;

import java.util.Date;
import java.util.List;

import com.tongbanjie.legends.server.dao.dataobject.JobSnapshot;
import com.tongbanjie.legends.server.dao.dataobject.enums.JobSnapshotStatusEnum;

public interface JobSnapshotDAO {

	JobSnapshot findById(Long id);

	JobSnapshot findByIdForUpdate(Long id);

	void insert(JobSnapshot jobSnapshot);

	/**
	 * 如果 lastModifyTime 不等于 null， 则被视为乐观锁， 会当做 where 条件
	 * @param jobSnapshot
	 * @return
	 */
	int updateById(JobSnapshot jobSnapshot);

	/**
	 * {@link JobSnapshotStatusEnum#EXECUTING} 状态的.
	 */
	List<JobSnapshot> findExecutingList();

	List<JobSnapshot> selectJobSnapshotList(JobSnapshot jobSnapshot);

	/**
	 * 根据name、group或status查询,默认展示任务列表也会用到
	 *
	 * @param name
	 * @param group
	 * @param status
	 * @return
	 */
	List<JobSnapshot> getListByNameAndGroupAndStatus(String name, String group, String status);

	/**
	 * 根据name、group或status查询,默认展示任务列表也会用到
	 *
	 * @param name
	 * @param group
	 * @param status
	 * @return
	 */
	List<JobSnapshot> getListByNameAndGroupAndStatus(String name, String group, String status, int limit);

	/**
	 * 查询 createTime 之前的记录,并插入到 job_snapshot_history 表.
	 */
	void findAndInsertIntoHistoryBeforeCreateTime(Date createTime);

	/**
	 * 删除 createTime 之前的记录.
	 */
	void deleteBeforeCreateTime(Date createTime);

}
