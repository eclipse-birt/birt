/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import org.eclipse.birt.report.engine.content.IDrillThroughAction;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;

/**
 * An Action class that implements IAction interface
 */
public class Action implements IAction {
	protected IHyperlinkAction content;
	protected String systemId;

	public Action(String systemId, IHyperlinkAction content) {
		this.systemId = systemId;
		this.content = content;
	}

	public Action(IHyperlinkAction content) {
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IAction#getType()
	 */
	public int getType() {
		return content.getType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IAction#getBookmark()
	 */
	public String getBookmark() {
		return content.getBookmark();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IAction#getActionString()
	 */
	public String getActionString() {
		switch (content.getType()) {
		case IHyperlinkAction.ACTION_HYPERLINK:
			return content.getHyperlink();
		case IHyperlinkAction.ACTION_BOOKMARK:
			return content.getBookmark();
		}
		return null;
	}

	public String getSystemId() {
		return systemId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IAction#getReportName()
	 */
	public String getReportName() {
		return content.getReportName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IAction#getParameterBindings()
	 */
	public Map getParameterBindings() {
		return content.getParameterBindings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IAction#getSearchCriteria()
	 */
	public Map getSearchCriteria() {
		return content.getSearchCriteria();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IAction#getTargetWindow()
	 */
	public String getTargetWindow() {
		return content.getTargetWindow();
	}

	public String getFormat() {
		return content.getFormat();
	}

	public boolean isBookmark() {
		return content.isBookmark();
	}

	/**
	 * @return the type of the target report file.
	 */
	public String getTargetFileType() {
		IDrillThroughAction drillThrough = content.getDrillThrough();
		if (null == drillThrough) {
			return null;
		} else {
			return drillThrough.getTargetFileType();
		}
	}

	public String getTooltip() {
		return content.getTooltip();
	}

}
