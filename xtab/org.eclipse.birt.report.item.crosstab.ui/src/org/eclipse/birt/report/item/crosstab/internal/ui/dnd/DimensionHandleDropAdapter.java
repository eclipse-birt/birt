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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 *
 */

public class DimensionHandleDropAdapter implements IDropAdapter {

	@Override
	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (!isDimensionHandle(transfer)) {
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
		// else if ( target instanceof PropertyHandle )
		// {
		// if ( ( (PropertyHandle) target ).getPropertyDefn( )
		// .getName( )
		// .equals( ICrosstabReportItemConstants.COLUMNS_PROP )
		// || ( (PropertyHandle) target ).getPropertyDefn( )
		// .getName( )
		// .equals( ICrosstabReportItemConstants.ROWS_PROP ) )
		// {
		// return DNDService.LOGIC_TRUE;
		// }
		// else
		// {
		// return DNDService.LOGIC_UNKNOW;
		// }
		// }
		return DNDService.LOGIC_UNKNOW;
	}

	private boolean isDimensionHandle(Object transfer) {
		if (transfer instanceof Object[]) {
			Object[] items = (Object[]) transfer;
			DesignElementHandle container = null;
			for (int i = 0; i < items.length; i++) {
				if (!(items[i] instanceof DimensionHandle)) {
					return false;
				}
				if (container == null) {
					container = ((DimensionHandle) items[i]).getContainer();
				} else if (container != ((DimensionHandle) items[i]).getContainer()) {
					return false;
				}
			}
			return true;
		}
		return transfer instanceof DimensionHandle;
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
		//
		// DimensionHandle dimensionHandle = (DimensionHandle) transfer;
		// CrosstabReportItemHandle xtabHandle = null;
		// int axisType = 0;
		//
		// if ( target instanceof EditPart )//drop on layout
		// {
		// EditPart editPart = (EditPart) target;
		// CrosstabTableEditPart parent = (CrosstabTableEditPart)
		// editPart.getParent( );
		// CrosstabHandleAdapter handleAdpter = parent.getCrosstabHandleAdapter(
		// );
		// if ( editPart.getModel( ) instanceof VirtualCrosstabCellAdapter )
		// axisType = ( (VirtualCrosstabCellAdapter) editPart.getModel( )
		// ).getType( );
		// else if ( editPart.getModel( ) instanceof NormalCrosstabCellAdapter )
		// axisType = ( (NormalCrosstabCellAdapter) editPart.getModel( ) ).
		// xtabHandle = (CrosstabReportItemHandle)
		// handleAdpter.getCrosstabItemHandle( );
		// }
		// else if ( target instanceof PropertyHandle )//drop on outline
		// {
		// PropertyHandle property = (PropertyHandle) target;
		// Object handle = property.getElementHandle( );
		// if ( handle instanceof CrosstabReportItemHandle )
		// {
		// xtabHandle = (CrosstabReportItemHandle) handle;
		// }
		// else if ( handle instanceof DesignElementHandle )
		// {
		// xtabHandle = (CrosstabReportItemHandle) CrosstabUtil.getReportItem(
		// (DesignElementHandle) handle );
		// }
		// if ( property.getPropertyDefn( )
		// .getName( )
		// .equals( ICrosstabReportItemConstants.COLUMNS_PROP ) )
		// {
		// axisType = ICrosstabConstants.COLUMN_AXIS_TYPE;
		// }
		// else
		// {
		// axisType = ICrosstabConstants.ROW_AXIS_TYPE;
		// }
		// }
		// return createDimensionViewHandle( xtabHandle, dimensionHandle,
		// axisType );

		if (target instanceof EditPart)// drop on layout
		{
			EditPart editPart = (EditPart) target;

			CreateRequest request = new CreateRequest();

			request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, transfer);
			request.setLocation(location.getPoint());
			Command command = editPart.getCommand(request);
			if (command != null && command.canExecute()) {
				editPart.getViewer().getEditDomain().getCommandStack().execute(command);

				CrosstabReportItemHandle crosstab = getCrosstab(editPart);
				if (crosstab != null) {
					AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(crosstab);
					providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);
				}
				return true;
			} else {
				return false;
			}

			// CrosstabTableEditPart parent = (CrosstabTableEditPart)
			// editPart.getParent( );
			// CrosstabHandleAdapter handleAdpter =
			// parent.getCrosstabHandleAdapter( );
			//
			// xtabHandle = (CrosstabReportItemHandle)
			// handleAdpter.getCrosstabItemHandle( );
		} else if (target instanceof PropertyHandle)// drop on outline
		{
			DimensionHandle dimensionHandle = (DimensionHandle) transfer;
			CrosstabReportItemHandle xtabHandle;
			int axisType = 0;

			PropertyHandle property = (PropertyHandle) target;
			Object handle = property.getElementHandle();

			xtabHandle = (CrosstabReportItemHandle) CrosstabUtil.getReportItem((DesignElementHandle) handle);

			if (property.getPropertyDefn().getName().equals(ICrosstabReportItemConstants.COLUMNS_PROP)) {
				axisType = ICrosstabConstants.COLUMN_AXIS_TYPE;
			} else {
				axisType = ICrosstabConstants.ROW_AXIS_TYPE;
			}
			boolean ret = createDimensionViewHandle(xtabHandle, dimensionHandle, axisType);

			if (ret) {
				AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(xtabHandle);
				providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);

			}

			return ret;
		}
		return false;
	}

	private boolean createDimensionViewHandle(CrosstabReportItemHandle xtabHandle, DimensionHandle dimensionHandle,
			int type) {

		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans("Create Dimension"); //$NON-NLS-1$
		try {
			DimensionViewHandle viewHandle = xtabHandle.insertDimension(dimensionHandle, type, 0);
			HierarchyHandle hierarchyHandle = dimensionHandle.getDefaultHierarchy();
			int count = hierarchyHandle.getLevelCount();
			if (count == 0) {
				stack.rollback();
				return false;
			}
			LevelHandle levelHandle = hierarchyHandle.getLevel(0);
			// new a bing
			DataItemHandle dataHandle = CrosstabAdaptUtil
					.createColumnBindingAndDataItem((ExtendedItemHandle) xtabHandle.getModelHandle(), levelHandle);

			LevelViewHandle levelViewHandle = viewHandle.insertLevel(levelHandle, 0);

			CrosstabCellHandle cellHandle = levelViewHandle.getCell();

			cellHandle.addContent(dataHandle);

			ActionHandle actionHandle = levelHandle.getActionHandle();
			if (actionHandle != null) {
				List source = new ArrayList();
				source.add(actionHandle.getStructure());
				List newAction = ModelUtil.cloneStructList(source);
				dataHandle.setAction((Action) newAction.get(0));
			}

			stack.commit();
		} catch (SemanticException e) {
			stack.rollback();
			ExceptionUtil.handle(e);
			return false;
		}
		return true;

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
