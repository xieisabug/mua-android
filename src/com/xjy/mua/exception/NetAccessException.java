package com.xjy.mua.exception;


public class NetAccessException extends Exception
{
	private static final long serialVersionUID = 1L;

	public NetAccessException()
	{
		super();
	}
	
	public NetAccessException(String msg)
	{
		super(msg);
	}
	
	public NetAccessException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
	
	public NetAccessException(Throwable cause)
	{
		super(cause);
	}
}
