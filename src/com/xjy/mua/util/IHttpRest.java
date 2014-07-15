package com.xjy.mua.util;


import com.xjy.mua.exception.NetAccessException;

import java.util.Map;


public interface IHttpRest
{
	public NetBackDataEntity getRequestData(String url, Map<String, String> paramsMap)  throws NetAccessException;
	
	public NetBackDataEntity postRequestData(String url, Map<String, String> paramsMap) throws NetAccessException ;
	
	public NetBackDataEntity putRequestData(String url, Map<String, String> paramsMap) throws NetAccessException ;
	
	public NetBackDataEntity deleteRequestData(String url) throws NetAccessException ;
}
