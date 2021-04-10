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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands;

import java.util.List;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * 
 */

public class AddMultipleMeasureCommand extends AbstractCrosstabCommand {

	private CrosstabCellAdapter handleAdpter;
	// private MeasureHandle measureHandle;
	private List list;
	private Object after = null;
	int position = -1;
	/**
	 * trans name
	 */
	// private static final String NAME = "Add MeasureHandle";
	private static final String NAME = Messages.getString("AddMeasureViewHandleCommand.TransName");//$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param handleAdpter
	 * @param measureHandle
	 */
	public AddMultipleMeasureCommand(CrosstabCellAdapter handleAdpter, List list, Object after) {
		super(handleAdpter.getDesignElementHandle());
		this.handleAdpter = handleAdpter;
		this.list = list;
		this.after = after;

		setLabel(NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		transStart(NAME);
//		CrosstabReportItemHandle reportHandle = (CrosstabReportItemHandle) handleAdpter.getCrosstabCellHandle( )
//				.getCrosstab( );

		try {

			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				if (obj instanceof MeasureHandle) {
					addMeasureHandle((MeasureHandle) obj);
				}
				if (obj instanceof MeasureGroupHandle) {
					List children = ((MeasureGroupHandle) obj).getContents(MeasureGroupHandle.MEASURES_PROP);
					for (int j = 0; j < children.size(); j++) {
						Object temp = children.get(j);
						if (temp instanceof MeasureHandle) {
							addMeasureHandle((MeasureHandle) temp);
						}
					}
				}
			}
		} catch (SemanticException e) {
			rollBack();
			ExceptionUtil.handle(e);
			return;
		}
		transEnd();
	}

	private void addMeasureHandle(MeasureHandle measureHandle) throws SemanticException {
		CrosstabReportItemHandle reportHandle = handleAdpter.getCrosstabCellHandle().getCrosstab();

		if (isContainMeasureHandle(measureHandle)) {
			return;
		}
		if (position == -1) {
			position = findPosition();
		}
		CrosstabAdaptUtil.addMeasureHandle(reportHandle, measureHandle, position);
		position++;
	}

	private boolean isContainMeasureHandle(MeasureHandle measureHandle) {
		CrosstabReportItemHandle reportHandle = handleAdpter.getCrosstabCellHandle().getCrosstab();

		int count = reportHandle.getMeasureCount();
		// loop the measure
		for (int i = 0; i < count; i++) {
			MeasureViewHandle temp = reportHandle.getMeasure(i);
			if (temp.getCubeMeasure() == measureHandle) {
				return true;
			}
		}
		return false;
	}

	private int findPosition() {
		int base = CrosstabAdaptUtil
				.getMeasureViewHandle((ExtendedItemHandle) handleAdpter.getCrosstabCellHandle().getModelHandle())
				.getModelHandle().getIndex();
		if (after instanceof DesignElementHandle) {
			int index = ((DesignElementHandle) after).getIndex();
			if (index == 0) {
				return base;
			}
		}
		return base + 1;
		// return ((CrosstabReportItemHandle)
		// handleAdpter.getCrosstabItemHandle( )).getDimensionCount( getType( )
		// );
	}
}
