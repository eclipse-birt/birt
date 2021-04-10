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

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Add the measure handle to the cross tab in the measure area.
 */
public class AddMeasureViewHandleCommand extends AbstractCrosstabCommand {

	private CrosstabCellAdapter handleAdpter;
	private MeasureHandle measureHandle;
	private Object after = null;
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
	public AddMeasureViewHandleCommand(CrosstabCellAdapter handleAdpter, MeasureHandle measureHandle, Object after) {
		super(measureHandle);
		this.handleAdpter = handleAdpter;
		this.measureHandle = measureHandle;
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
		CrosstabReportItemHandle reportHandle = handleAdpter.getCrosstabCellHandle().getCrosstab();

		try {
			// int position = reportHandle.getMeasureCount( );
			int position = findPosition();
			CrosstabAdaptUtil.addMeasureHandle(reportHandle, measureHandle, position);
			transEnd();
		} catch (SemanticException e) {
			rollBack();
			ExceptionUtil.handle(e);
		}
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
