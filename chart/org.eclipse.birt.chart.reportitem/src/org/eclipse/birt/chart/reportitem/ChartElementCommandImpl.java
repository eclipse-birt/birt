/***********************************************************************
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
	@Override
	public void execute() {
		item.setModel(newChart);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#undo()
	 */
	@Override
	public void undo() {
		item.setModel(oldChart);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#redo()
	 */
	@Override
	public void redo() {
		item.setModel(newChart);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#canRedo()
	 */
	@Override
	public boolean canRedo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IElementCommand#getLabel()
	 */
	@Override
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
	@Override
	public DesignElementHandle getElementHandle() {
		return handle;
	}

}
