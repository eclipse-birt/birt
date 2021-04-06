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
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Drop the demension view handle from the row to column
 */

public class ChangeAreaCommand extends AbstractCrosstabCommand {
	private int type = -1;
	// private DimensionHandle dimensionHandle;
	private Object after = null;

	DimensionViewHandle parentVewHandle;
	DimensionViewHandle childViewHandle;
	private AggregationCellProviderWrapper providerWrapper;
	/**
	 * Trans name
	 */
	// private static final String NAME = "Drop dimension handle";
	private static final String NAME = Messages.getString("ChangeAreaCommand.TransName");//$NON-NLS-1$

	public ChangeAreaCommand(DesignElementHandle parent, DesignElementHandle child, Object after) {
		super(child);
		// this.parent = parent;
		// this.child = child;
		this.after = after;
		if (parent != null) {
			parentVewHandle = CrosstabAdaptUtil.getDimensionViewHandle(CrosstabAdaptUtil.getExtendedItemHandle(parent));
			setType(parentVewHandle.getAxisType());
		}

		childViewHandle = CrosstabAdaptUtil.getDimensionViewHandle(CrosstabAdaptUtil.getExtendedItemHandle(child));
		providerWrapper = new AggregationCellProviderWrapper(childViewHandle.getCrosstab());
		setLabel(NAME);
	}

	public boolean canExecute() {
		return !DEUtil.isReferenceElement(childViewHandle.getCrosstab().getCrosstabHandle());
		// return getType( ) != childViewHandle.getAxisType( );
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
			boolean bool = CrosstabAdaptUtil.needRemoveInvaildBindings(reportHandle);
			reportHandle.pivotDimension(childViewHandle.getAxisType(), childViewHandle.getIndex(), getType(),
					findPosition());

			CrosstabUtil.addAllHeaderLabel(reportHandle);
			if (bool) {
				CrosstabAdaptUtil.removeInvalidBindings(reportHandle);
			}
			providerWrapper.updateAllAggregationCells();
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
		if (parentVewHandle == null) {
			return 0;
		}
		int ori = childViewHandle.getIndex();
		int base = parentVewHandle.getIndex();
		int value = 0;
		if (ori < base && getType() == childViewHandle.getAxisType()) {
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

	/**
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
}
