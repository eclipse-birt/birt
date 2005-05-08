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
import org.eclipse.birt.report.engine.content.IHyperlinkAction;

/**
 *
 */
public class Action extends ReportPart implements IAction
{

	protected IHyperlinkAction content;
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
}
