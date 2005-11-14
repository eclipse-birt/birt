package org.eclipse.birt.report.engine.presentation;

import java.io.Serializable;


public class PageHint implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7666468796696037741L;
	
	protected String startId;
	protected String endId;
	
	public PageHint (String startId, String endId)
	{
		this.startId = startId;
		this.endId = endId;
	}
	
	public String getStart()
	{
		return startId;
	}
	public String getEnd()
	{
		return endId;
	}

}
