package com.tongbanjie.legends.server.dao.dataobject;

import java.util.Date;

import com.tongbanjie.legends.server.dao.dataobject.enums.JobInfoInvokePolicyEnum;
import com.tongbanjie.legends.server.dao.dataobject.enums.JobInfoTypeEnum;

public class JobInfo {

	private Long id;
	private String name;
	private String group;
	private JobInfoTypeEnum type;
	private Date time;
	private String cron;
	private String urls;
	private String classPath;
	private JobInfoInvokePolicyEnum invokePolicy;
	private boolean isActivity;
	private String desc;
	private Date createTime;
	private Date modifyTime;
	private String param;
	private Date latestTriggerTime;
	private String latestServerAddress;
	private String ownerPhone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public JobInfoTypeEnum getType() {
		return type;
	}

	public void setType(JobInfoTypeEnum type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public JobInfoInvokePolicyEnum getInvokePolicy() {
		return invokePolicy;
	}

	public void setInvokePolicy(JobInfoInvokePolicyEnum invokePolicy) {
		this.invokePolicy = invokePolicy;
	}

	public boolean isActivity() {
		return isActivity;
	}

	public void setActivity(boolean isActivity) {
		this.isActivity = isActivity;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Date getLatestTriggerTime() {
		return latestTriggerTime;
	}

	public void setLatestTriggerTime(Date latestTriggerTime) {
		this.latestTriggerTime = latestTriggerTime;
	}

	public String getLatestServerAddress() {
		return latestServerAddress;
	}

	public void setLatestServerAddress(String latestServerAddress) {
		this.latestServerAddress = latestServerAddress;
	}

	public String getOwnerPhone() {
		return ownerPhone;
	}

	public void setOwnerPhone(String ownerPhone) {
		this.ownerPhone = ownerPhone;
	}

	@Override
	public String toString() {
		return "JobInfo{" +
				"id=" + id +
				", name='" + name + '\'' +
				", group='" + group + '\'' +
				", type=" + type +
				", time=" + time +
				", cron='" + cron + '\'' +
				", urls='" + urls + '\'' +
				", classPath='" + classPath + '\'' +
				", invokePolicy=" + invokePolicy +
				", isActivity=" + isActivity +
				", desc='" + desc + '\'' +
				", createTime=" + createTime +
				", modifyTime=" + modifyTime +
				", param='" + param + '\'' +
				", latestTriggerTime=" + latestTriggerTime +
				", latestServerAddress='" + latestServerAddress + '\'' +
				", ownerPhone='" + ownerPhone + '\'' +
				'}';
	}
}
