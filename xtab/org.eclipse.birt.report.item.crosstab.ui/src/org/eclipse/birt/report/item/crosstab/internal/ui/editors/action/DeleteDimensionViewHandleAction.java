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
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
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
 * Delete the dimension view handle
 */

public class DeleteDimensionViewHandleAction extends AbstractCrosstabAction {

	// LevelViewHandle viewHandle = null;
	DimensionViewHandle dimensionHandle;
//	private static final String NAME = "Delete Dimensionviewhandle";
//	private static final String ID = "delete_test_dimensionviewhandle";
//	private static final String TEXT = "Remove";

	private static final String NAME = Messages.getString("DeleteDimensionViewHandleAction.TransName");//$NON-NLS-1$
	private static final String ID = "delete_test_dimensionviewhandle";//$NON-NLS-1$
	private static final String TEXT = Messages.getString("DeleteDimensionViewHandleAction.DisplayName");//$NON-NLS-1$

	// private static final String DISPALY_NAME = "dimensionviewhandle";

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param index
	 */
	public DeleteDimensionViewHandleAction(DesignElementHandle handle) {
		super(handle);
		setId(ID);

		setText(TEXT);
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle(handle);
		setHandle(extendedHandle);
		dimensionHandle = CrosstabAdaptUtil.getDimensionViewHandle(extendedHandle);
		// viewHandle = dimensionHandle.getLevel( getLevelIndex( ) );

		ISharedImages shareImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
	}

	public boolean isEnabled() {
		return !DEUtil.isReferenceElement(dimensionHandle.getCrosstabHandle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (dimensionHandle == null) {
			return;
		}
		transStar(NAME);
		try {
			CrosstabReportItemHandle handle = dimensionHandle.getCrosstab();
			boolean bool = CrosstabAdaptUtil.needRemoveInvaildBindings(handle);
			if (bool) {
				dimensionHandle.getCrosstab().removeDimension(dimensionHandle.getAxisType(),
						dimensionHandle.getIndex());
				CrosstabAdaptUtil.removeInvalidBindings(handle);
			}
			AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(
					(ExtendedItemHandle) handle.getModelHandle());
			providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);

		} catch (SemanticException e) {
			rollBack();
			ExceptionUtil.handle(e);
			return;
		}
		transEnd();
	}
}
