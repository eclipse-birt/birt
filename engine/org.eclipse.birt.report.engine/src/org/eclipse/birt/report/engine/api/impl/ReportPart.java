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

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportPart;
import org.eclipse.birt.report.engine.api.IReportRunnable;

/**
 * 
 */
public class ReportPart implements IReportPart {

	protected IReportRunnable reportRunnable;

	protected IRenderOption renderOption;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IReportItem#getReportRunnable()
	 */
	public IReportRunnable getReportRunnable() {
		return reportRunnable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IReportItem#getRenderOption()
	 */
	public IRenderOption getRenderOption() {
		return renderOption;
	}

	/**
	 * @param renderOption The renderOption to set.
	 */
	public void setRenderOption(IRenderOption renderOption) {
		this.renderOption = renderOption;
	}

	/**
	 * @param reportRunnable The reportRunnable to set.
	 */
	public void setReportRunnable(IReportRunnable reportRunnable) {
		this.reportRunnable = reportRunnable;
	}
}
