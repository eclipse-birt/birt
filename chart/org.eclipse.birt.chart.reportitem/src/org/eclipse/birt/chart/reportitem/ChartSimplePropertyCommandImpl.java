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

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.extension.IElementCommand;

/**
 * ChartSimplePropertyCommandImpl
 */
public class ChartSimplePropertyCommandImpl implements IElementCommand {

	private ChartReportItemImpl item;
	private Object oldValue;
	private Object newValue;
	private DesignElementHandle handle;
	private String propertyName;

	/**
	 * @param newChart
	 * @param oldChart
	 * @param impl
	 *
	 */
	public ChartSimplePropertyCommandImpl(DesignElementHandle handle, ChartReportItemImpl impl, String propName,
			Object newValue, Object oldValue) {
		this.handle = handle;
		this.item = impl;
		this.propertyName = propName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IElementCommand#execute()
	 */
	@Override
	public void execute() {
		item.basicSetProperty(propertyName, newValue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IElementCommand#undo()
	 */
	@Override
	public void undo() {
		item.basicSetProperty(propertyName, oldValue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IElementCommand#redo()
	 */
	@Override
	public void redo() {
		item.basicSetProperty(propertyName, newValue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IElementCommand#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IElementCommand#canRedo()
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
		return Messages.getString("ChartElementCommandImpl.setProperty." + propertyName); //$NON-NLS-1$
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
