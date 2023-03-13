/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
	@Override
	public IReportRunnable getReportRunnable() {
		return reportRunnable;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IReportItem#getRenderOption()
	 */
	@Override
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
