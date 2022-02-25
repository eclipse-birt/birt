/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
	@Override
	public int getType() {
		return content.getType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IAction#getBookmark()
	 */
	@Override
	public String getBookmark() {
		return content.getBookmark();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IAction#getActionString()
	 */
	@Override
	public String getActionString() {
		switch (content.getType()) {
		case IHyperlinkAction.ACTION_HYPERLINK:
			return content.getHyperlink();
		case IHyperlinkAction.ACTION_BOOKMARK:
			return content.getBookmark();
		}
		return null;
	}

	@Override
	public String getSystemId() {
		return systemId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IAction#getReportName()
	 */
	@Override
	public String getReportName() {
		return content.getReportName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IAction#getParameterBindings()
	 */
	@Override
	public Map getParameterBindings() {
		return content.getParameterBindings();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IAction#getSearchCriteria()
	 */
	@Override
	public Map getSearchCriteria() {
		return content.getSearchCriteria();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IAction#getTargetWindow()
	 */
	@Override
	public String getTargetWindow() {
		return content.getTargetWindow();
	}

	@Override
	public String getFormat() {
		return content.getFormat();
	}

	@Override
	public boolean isBookmark() {
		return content.isBookmark();
	}

	/**
	 * @return the type of the target report file.
	 */
	@Override
	public String getTargetFileType() {
		IDrillThroughAction drillThrough = content.getDrillThrough();
		if (null == drillThrough) {
			return null;
		} else {
			return drillThrough.getTargetFileType();
		}
	}

	@Override
	public String getTooltip() {
		return content.getTooltip();
	}

}
