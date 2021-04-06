/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.IGridInstance;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of a grid
 */
public class GridInstance extends ReportItemInstance implements IGridInstance {

	public GridInstance(ITableContent grid, ExecutionContext context, RunningState runningState) {
		super(grid, context, runningState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ITableInstance#getCaption(
	 * )
	 */
	public String getCaption() {
		return ((ITableContent) content).getCaption();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ITableInstance#setCaption(
	 * java.lang.String)
	 */
	public void setCaption(String caption) {
		((ITableContent) content).setCaption(caption);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#
	 * getCaptionKey()
	 */
	public String getCaptionKey() {
		return ((ITableContent) content).getCaptionKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#
	 * setCaptionKey(java.lang.String)
	 */
	public void setCaptionKey(String captionKey) {
		((ITableContent) content).setCaptionKey(captionKey);
	}

	/**
	 * Get the summary.
	 * 
	 */
	public String getSummary() {
		return ((ITableContent) content).getSummary();
	}

	/**
	 * Set the summary
	 * 
	 */
	public void setSummary(String summary) {
		((ITableContent) content).setSummary(summary);
	}

}
