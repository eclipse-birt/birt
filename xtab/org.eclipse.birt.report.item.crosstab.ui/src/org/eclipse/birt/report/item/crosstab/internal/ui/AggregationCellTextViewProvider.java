/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * AggregationCellTextViewProvider
 */
public class AggregationCellTextViewProvider extends AggregationCellViewAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#getViewName()
	 */
	public String getViewName() {
		return "Text"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#matchView(org.eclipse.birt.report.item.crosstab.
	 * core.de.AggregationCellHandle)
	 */
	public boolean matchView(AggregationCellHandle cell) {
		List contents = cell.getContents();
		if (contents != null && contents.size() == 1) {
			Object content = contents.get(0);

			return (content instanceof DataItemHandle);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#switchView(org.eclipse.birt.report.item.crosstab
	 * .ui.extension.SwitchCellInfo)
	 */
	public void switchView(SwitchCellInfo info) {
		switchView(info.getAggregationCell());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#switchView(org.eclipse.birt.report.item.crosstab
	 * .core.de.AggregationCellHandle)
	 */
	public void switchView(AggregationCellHandle cell) {
		if (cell == null || (!canSwitch(cell))) {
			return;
		}

		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans("Switch to Text View"); //$NON-NLS-1$

		try {
			// clear span over
			cell.setSpanOverOnColumn(null);
			cell.setSpanOverOnRow(null);

			List contents = cell.getContents();

			for (int i = 0; i < contents.size(); i++) {
				((DesignElementHandle) contents.get(i)).drop();
			}

			// create text view
			createTextView(cell);

			stack.commit();
		} catch (Exception e) {
			stack.rollback();
			ExceptionUtil.handle(e);
		}
	}

	private void createTextView(AggregationCellHandle cell) throws SemanticException {
		CrosstabReportItemHandle crosstab = cell.getCrosstab();

		MeasureViewHandle measureView = (MeasureViewHandle) cell.getContainer();

		LevelHandle rowLevelHandle = cell.getAggregationOnRow();
		LevelHandle colLevelHandle = cell.getAggregationOnColumn();

		String rowLevel = rowLevelHandle == null ? null : rowLevelHandle.getFullName();
		String colLevel = colLevelHandle == null ? null : colLevelHandle.getFullName();

		String rowDimension = null;
		String colDimension = null;

		if (rowLevelHandle != null && rowLevelHandle.getContainer() != null
				&& rowLevelHandle.getContainer().getContainer() != null) {
			rowDimension = rowLevelHandle.getContainer().getContainer().getFullName();
		}

		if (colLevelHandle != null && colLevelHandle.getContainer() != null
				&& colLevelHandle.getContainer().getContainer() != null) {
			colDimension = colLevelHandle.getContainer().getContainer().getFullName();
		}

		CrosstabUtil.addDataItem(crosstab, cell, measureView, null, rowDimension, rowLevel, colDimension, colLevel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#restoreView(org.eclipse.birt.report.item.
	 * crosstab.core.de.AggregationCellHandle)
	 */
	public void restoreView(AggregationCellHandle cell) {
	}

	/**
	 * @deprecated use {@link #canSwitch(SwitchCellInfo)}
	 */
	public boolean canSwitch(AggregationCellHandle cell) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#canSwitch(org.eclipse.birt.report.item.crosstab.
	 * ui.extension.SwitchCellInfo)
	 */
	public boolean canSwitch(SwitchCellInfo info) {
		return true;
	}

	public String getViewDisplayName() {
		// TODO Auto-generated method stub
		return Messages.getString("AggregationCellTextViewProvider.displayName");
	}
}
