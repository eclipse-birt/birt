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
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * 
 */

public class ChangeMeasureOrderCommand extends AbstractCrosstabCommand {
	// private static final String NAME = "Change the measue order";
	private static final String NAME = Messages.getString("ChangeMeasureOrderCommand.TransName");//$NON-NLS-1$
	private Object after = null;

	MeasureViewHandle parentVewHandle;
	MeasureViewHandle childViewHandle;

	/**
	 * @param parent
	 * @param child
	 * @param after
	 */
	public ChangeMeasureOrderCommand(DesignElementHandle parent, DesignElementHandle child, Object after) {
		super(child);
		// this.parent = parent;
		// this.child = child;
		this.after = after;

		parentVewHandle = CrosstabAdaptUtil.getMeasureViewHandle(CrosstabAdaptUtil.getExtendedItemHandle(parent));

		childViewHandle = CrosstabAdaptUtil.getMeasureViewHandle(CrosstabAdaptUtil.getExtendedItemHandle(child));

		setLabel(NAME);
	}

	public boolean canExecute() {
		return !DEUtil.isReferenceElement(childViewHandle.getCrosstab().getCrosstabHandle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		transStart(NAME);
		CrosstabReportItemHandle reportHandle = childViewHandle.getCrosstab();

		try {
			// reportHandle.removeDimension(childViewHandle.getAxisType( ),
			// childViewHandle.getIndex( ) );
			// CrosstabUtil.insertDimension( reportHandle, childViewHandle,
			// parentVewHandle.getAxisType( ), findPosition( ), measureMap, funcMap );
			reportHandle.pivotMeasure(childViewHandle.getModelHandle().getIndex(), findPosition());
		} catch (SemanticException e) {
			rollBack();
			ExceptionUtil.handle(e);
			return;
		}
		transEnd();
	}

	private int findPosition() {
		// int base = handleAdpter.getCrosstabCellHandle( ).getCrosstabHandle(
		// ).getIndex( );
		// System.out.println(after);
		int ori = childViewHandle.getModelHandle().getIndex();
		int base = parentVewHandle.getModelHandle().getIndex();
		int value = 0;
		if (ori < base) {
			value = -1;
		}
		if (after instanceof DesignElementHandle) {
			int index = ((DesignElementHandle) after).getIndex();
			if (index == 0) {
				return base + value;
			}
		}
		return base + 1 + value;
		// return ((CrosstabReportItemHandle) handleAdpter.getCrosstabItemHandle(
		// )).getDimensionCount( getType( ) );
	}
}
