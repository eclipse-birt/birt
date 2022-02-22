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

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 *
 */

/**
 * MeasureHandleDropAdapter
 */
/**
 * MeasureHandleDropAdapter
 */
public class MeasureHandleDropAdapter implements IDropAdapter {
	private IExtendedDataModelUIAdapter adapter = ExtendedDataModelUIAdapterHelper.getInstance().getAdapter();

	@Override
	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (!isMeasureHandle(transfer)) {
			return DNDService.LOGIC_UNKNOW;
		}
		if (target instanceof EditPart) {
			EditPart editPart = (EditPart) target;
			if (editPart.getModel() instanceof IVirtualValidator) {
				if (((IVirtualValidator) editPart.getModel()).handleValidate(transfer)) {
					return DNDService.LOGIC_TRUE;
				} else {
					return DNDService.LOGIC_FALSE;
				}
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	/**
	 * Allow drop multi MeasureHandle or single MeasureGroupHandle
	 *
	 * @param transfer
	 * @return
	 */
	private boolean isMeasureHandle(Object transfer) {
		if (transfer instanceof Object[]) {
			Object[] items = (Object[]) transfer;
			for (int i = 0; i < items.length; i++) {
				if (!(items[i] instanceof MeasureHandle)) {
					return false;
				}
			}
			return true;
		}
		return transfer instanceof MeasureHandle || transfer instanceof MeasureGroupHandle;
	}

	@Override
	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {
		// if ( transfer instanceof Object[] )
		// {
		// Object[] objects = (Object[]) transfer;
		// for ( int i = 0; i < objects.length; i++ )
		// {
		// if ( !performDrop( objects[i], target, operation, location ) )
		// return false;
		// }
		// return true;
		// }

		if (target instanceof EditPart) {
			EditPart editPart = (EditPart) target;

			CreateRequest request = new CreateRequest();

			request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, transfer);
			request.setLocation(location.getPoint());
			Command command = editPart.getCommand(request);
			if (command != null && command.canExecute()) {
				CrosstabReportItemHandle crosstab = getCrosstab(editPart);
				if (crosstab != null) {
					crosstab.getModuleHandle().getCommandStack()
							.startTrans(Messages.getString("MeasureHandleDropAdapter_trans_name")); //$NON-NLS-1$
				}

				// Carl: Add this part below to set the binding for the crosstab in case it is
				// not already set
				// Carl: This binding property should be set before execute the drop command
				// Carl: This behavior is the same as taken from
				// ExtendedDataColumnXtabDropAdapter

				CubeHandle measureCubeHandle = CrosstabAdaptUtil.getCubeHandle((ReportElementHandle) transfer);

				if (measureCubeHandle == null) {

					ReportElementHandle extendedData = adapter
							.getBoundExtendedData((ReportItemHandle) crosstab.getModelHandle());

					if (extendedData == null
							|| !extendedData.equals(adapter.resolveExtendedData((ReportElementHandle) transfer))) {
						if (!adapter.setExtendedData((ReportItemHandle) crosstab.getModelHandle(),
								adapter.resolveExtendedData((ReportElementHandle) transfer))) {
							crosstab.getModuleHandle().getCommandStack().rollback();
							return false;
						}
					}

				}

				editPart.getViewer().getEditDomain().getCommandStack().execute(command);

				if (crosstab != null) {
					AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(crosstab);
					providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);

					if (crosstab.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE) != 0) {
						DimensionViewHandle viewHnadle = crosstab.getDimension(ICrosstabConstants.COLUMN_AXIS_TYPE,
								crosstab.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE) - 1);
						CrosstabUtil.addLabelToHeader(viewHnadle.getLevel(viewHnadle.getLevelCount() - 1));
					}

					crosstab.getModuleHandle().getCommandStack().commit();
				}
				return true;
			} else {
				return false;
			}

		}
		return false;
	}

	private CrosstabReportItemHandle getCrosstab(EditPart editPart) {
		CrosstabReportItemHandle crosstab = null;
		Object tmp = editPart.getModel();
		if (!(tmp instanceof CrosstabCellAdapter)) {
			return null;
		}
		if (tmp instanceof VirtualCrosstabCellAdapter) {
			return ((VirtualCrosstabCellAdapter) tmp).getCrosstabReportItemHandle();
		}

		CrosstabCellHandle handle = ((CrosstabCellAdapter) tmp).getCrosstabCellHandle();
		if (handle != null) {
			crosstab = handle.getCrosstab();
		}

		return crosstab;

	}
}
