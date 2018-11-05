package com.fanwe.hybrid.model;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-20 下午7:53:32 类说明
 */
public class StartAppPageJsonModel
{	
	private String dev_type;
	private String android_page;
	private String ios_page;
	private String data;

	public String getAndroid_page()
	{
		return android_page;
	}

	public void setAndroid_page(String android_page)
	{
		this.android_page = android_page;
	}

	public String getIos_page()
	{
		return ios_page;
	}

	public void setIos_page(String ios_page)
	{
		this.ios_page = ios_page;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getDev_type()
	{
		return dev_type;
	}

	public void setDev_type(String dev_type)
	{
		this.dev_type = dev_type;
	}
}
