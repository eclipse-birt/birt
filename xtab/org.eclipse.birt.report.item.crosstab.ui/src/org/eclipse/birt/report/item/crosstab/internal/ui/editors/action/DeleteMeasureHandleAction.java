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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class DeleteMeasureHandleAction extends AbstractCrosstabAction {
	private MeasureViewHandle measureViewHandle;
	/**
	 * Action displayname
	 */
	// private static final String ACTION_MSG_MERGE = "Remove";
	private static final String ACTION_MSG_MERGE = Messages.getString("DeleteMeasureHandleAction.DisplayName");//$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.DeleteMeasureHandleAction"; //$NON-NLS-1$

	/**
	 * Trans name
	 */
	// private static final String NAME = "Delete MeasureViewHandle";
	private static final String NAME = Messages.getString("DeleteMeasureHandleAction.TransName");//$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public DeleteMeasureHandleAction(DesignElementHandle handle) {
		super(handle);
		setId(ID);
		setText(ACTION_MSG_MERGE);
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle(handle);
		setHandle(extendedHandle);
		measureViewHandle = CrosstabAdaptUtil.getMeasureViewHandle(extendedHandle);

		ISharedImages shareImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
	}

	public boolean isEnabled() {
		return !DEUtil.isReferenceElement(measureViewHandle.getCrosstabHandle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return !DEUtil.isReferenceElement(measureViewHandle.getCrosstabHandle());
	}

	private CrosstabReportItemHandle getCrosstabReportItemHandle(Object editpart) {
		return measureViewHandle.getCrosstab();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		CrosstabReportItemHandle reportItem = getCrosstabReportItemHandle(getHandle());

		if (reportItem != null) {
			transStar(NAME);

			try {
				boolean bool = CrosstabAdaptUtil.needRemoveInvaildBindings(reportItem);

				if (bool) {
					reportItem.removeMeasure(measureViewHandle.getIndex());
					CrosstabAdaptUtil.removeInvalidBindings(reportItem);
				}
				AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(
						(ExtendedItemHandle) reportItem.getModelHandle());
				providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);
			} catch (SemanticException e) {
				rollBack();
				ExceptionUtil.handle(e);
				return;
			}
			transEnd();
		}

	}
}
