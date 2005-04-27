/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.Map;

import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportPart;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.impl.ActionContent;

/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Action implements IAction, IReportPart
{

	protected IHyperlinkAction content;
	
	protected IReportRunnable reportRunnable;
	
	protected IRenderOption renderOption;
	
	protected String actionString;
	
	public Action(IHyperlinkAction content)
	{
		this.content = content;
		switch(content.getType())
		{
			case IHyperlinkAction.ACTION_HYPERLINK: 
				actionString = content.getHyperlink();
				break;
			case IHyperlinkAction.ACTION_BOOKMARK:
				actionString = content.getBookmark();
				break;
			case IHyperlinkAction.ACTION_DRILLTHROUGH:
				break;
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IAction#getType()
	 */
	public int getType()
	{
		return content.getType();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IAction#getBookmark()
	 */
	public String getBookmark()
	{
		return content.getBookmark();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IAction#getActionString()
	 */
	public String getActionString()
	{
		return actionString;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IAction#getReportName()
	 */
	public String getReportName()
	{
		return content.getReportName();
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IAction#getParameterBindings()
	 */
	public Map getParameterBindings()
	{
		return content.getParameterBindings();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IAction#getSearchCriteria()
	 */
	public Map getSearchCriteria()
	{
		return content.getSearchCriteria();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IAction#getTargetWindow()
	 */
	public String getTargetWindow()
	{
		return content.getTargetWindow();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IReportItem#getReportRunnable()
	 */
	public IReportRunnable getReportRunnable()
	{
		return reportRunnable;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IReportItem#getRenderOption()
	 */
	public IRenderOption getRenderOption()
	{
		return renderOption;
	}

	/**
	 * @param renderOption The renderOption to set.
	 */
	public void setRenderOption(IRenderOption renderOption)
	{
		this.renderOption = renderOption;
	}
	/**
	 * @param reportRunnable The reportRunnable to set.
	 */
	public void setReportRunnable(IReportRunnable reportRunnable)
	{
		this.reportRunnable = reportRunnable;
	}
}
