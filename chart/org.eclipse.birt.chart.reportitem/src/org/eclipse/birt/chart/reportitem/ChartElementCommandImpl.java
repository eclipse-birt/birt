/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IElementCommand;

/**
 * ChartElementCommandImpl
 */
public class ChartElementCommandImpl implements IElementCommand {

	private ChartReportItemImpl item;
	private Chart oldChart;
	private Chart newChart;
	private DesignElementHandle handle;

	/**
	 * @param newChart
	 * @param oldChart
	 * @param impl
	 * 
	 */
	public ChartElementCommandImpl(ExtendedItemHandle handle, ChartReportItemImpl impl, Chart oldChart,
			Chart newChart) {
		this.handle = handle;
		this.item = impl;
		this.oldChart = oldChart;
		this.newChart = newChart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#execute()
	 */
	public void execute() {
		item.setModel(newChart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#undo()
	 */
	public void undo() {
		item.setModel(oldChart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#redo()
	 */
	public void redo() {
		item.setModel(newChart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#canUndo()
	 */
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#canRedo()
	 */
	public boolean canRedo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IElementCommand#getLabel()
	 */
	public String getLabel() {
		return Messages.getString("ChartElementCommandImpl.editChart"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IElementCommand#getElementHandle(
	 * )
	 */
	public DesignElementHandle getElementHandle() {
		return handle;
	}

}