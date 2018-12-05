package com.szruito.goldfields.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-9 下午6:57:01 类说明
 */
@Table(name = "LoginSuccessModel")
public class LoginSuccessModel
{
	@Column(name = "id", isId = true)
	private String id;
	@Column(name = "user_name")
	private String user_name;
	@Column(name = "patternpassword")
	private String patternpassword;
	@Column(name = "sess_id")
	private String  sess_id;
	@Column(name = "is_current")
	private int is_current;
	@Column(name = "userid")
	private String userid;

	public String getSess_id()
	{
		return sess_id;
	}

	public void setSess_id(String sess_id)
	{
		this.sess_id = sess_id;
	}

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getPatternpassword()
	{
		return patternpassword;
	}

	public void setPatternpassword(String patternpassword)
	{
		this.patternpassword = patternpassword;
	}

	public int getIs_current()
	{
		return is_current;
	}

	public void setIs_current(int is_current)
	{
		this.is_current = is_current;
	}

	public String getUser_name()
	{
		return user_name;
	}

	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
	}
}
