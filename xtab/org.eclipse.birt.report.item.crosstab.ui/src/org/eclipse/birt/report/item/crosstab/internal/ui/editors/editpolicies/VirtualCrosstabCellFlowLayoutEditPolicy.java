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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.AddLevelAttributeHandleCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.ChangeAreaCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CreateDimensionViewCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CreateMeasureViewCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CreateMultipleMeasureCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.FirstLevelHandleDataItemEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;

/**
 * 
 */

public class VirtualCrosstabCellFlowLayoutEditPolicy extends ReportFlowLayoutEditPolicy {

	protected Command getCreateCommand(CreateRequest request) {
		// EditPart after = getInsertionReference( request );

		// CreateCommand command = new CreateCommand( request.getExtendedData( )
		// );

		Object model = this.getHost().getModel();
		Object newObject = request.getExtendedData().get(DesignerConstants.KEY_NEWOBJECT);

		if (model instanceof VirtualCrosstabCellAdapter) {
			EditPart parent = getHost().getParent();
			CrosstabHandleAdapter adapter = ((CrosstabTableEditPart) parent).getCrosstabHandleAdapter();
			int type = ((VirtualCrosstabCellAdapter) model).getType();
			if (newObject instanceof DimensionHandle) {
				return new CreateDimensionViewCommand(adapter, type, (DimensionHandle) newObject);
			}
			if (newObject instanceof LevelHandle) {
				DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle((LevelHandle) newObject);
				CreateDimensionViewCommand command = new CreateDimensionViewCommand(adapter, type, dimensionHandle);
				command.setLevelHandles(new LevelHandle[] { (LevelHandle) newObject });
				return command;
			}
			if (newObject instanceof LevelAttributeHandle) {
				LevelHandle levelHandle = (LevelHandle) ((LevelAttributeHandle) newObject).getElementHandle();
				DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle(levelHandle);
				AddLevelAttributeHandleCommand command = new AddLevelAttributeHandleCommand(adapter, type,
						dimensionHandle, new LevelAttributeHandle[] { (LevelAttributeHandle) newObject });
				return command;
			} else if (newObject instanceof MeasureHandle && type == VirtualCrosstabCellAdapter.MEASURE_TYPE) {
				return new CreateMeasureViewCommand(adapter, (MeasureHandle) newObject);
			} else if (newObject instanceof MeasureGroupHandle && type == VirtualCrosstabCellAdapter.MEASURE_TYPE) {
				List list = new ArrayList();
				list.add(newObject);
				return new CreateMultipleMeasureCommand(adapter, list);
			} else if (newObject instanceof Object[] && CrosstabAdaptUtil.canCreateMultipleCommand((Object[]) newObject)
					&& type == VirtualCrosstabCellAdapter.MEASURE_TYPE) {
				List list = new ArrayList();
				Object[] objs = (Object[]) newObject;
				for (int i = 0; i < objs.length; i++) {
					list.add(objs[i]);
				}
				return new CreateMultipleMeasureCommand(adapter, list);
			} else if (newObject instanceof Object[]) {
				Object[] objs = (Object[]) newObject;

				if (objs.length > 0) {
					Class arrayType = getArrayType(objs);
					if (LevelHandle.class.isAssignableFrom(arrayType)) {
						LevelHandle[] levels = new LevelHandle[objs.length];
						System.arraycopy(objs, 0, levels, 0, levels.length);
						DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle((LevelHandle) objs[0]);
						CreateDimensionViewCommand command = new CreateDimensionViewCommand(adapter, type,
								dimensionHandle);
						command.setLevelHandles(levels);
						return command;
					} else if (DimensionHandle.class.isAssignableFrom(arrayType)) {
						DimensionHandle[] dimensions = new DimensionHandle[objs.length];
						System.arraycopy(objs, 0, dimensions, 0, dimensions.length);
						return new CreateDimensionViewCommand(adapter, type, dimensions);
					} else if (LevelAttributeHandle.class.isAssignableFrom(arrayType)) {
						Object[] items = (Object[]) newObject;
						LevelAttributeHandle[] levelAttributeHandles = new LevelAttributeHandle[items.length];
						System.arraycopy(items, 0, levelAttributeHandles, 0, levelAttributeHandles.length);
						LevelHandle levelHandle = (LevelHandle) ((LevelAttributeHandle) items[0]).getElementHandle();
						DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle(levelHandle);
						AddLevelAttributeHandleCommand command = new AddLevelAttributeHandleCommand(adapter, type,
								dimensionHandle, levelAttributeHandles);
						return command;
					}
				}
			}
		}
		// No previous edit part
		// if ( after != null )
		// {
		// command.setAfter( after.getModel( ) );
		// }
		return super.getCreateCommand(request);
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies
	 * .ReportFlowLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart,
	 * org.eclipse.gef.EditPart, org.eclipse.gef.EditPart)
	 */
	protected Command createAddCommand(EditPart parent, EditPart child, EditPart after) {
		Object parentObj = parent.getModel();
		// Object source = child.getModel( );
		Object afterObj = after == null ? null : after.getModel();
		Object childParent = getOperator(child).getModel();
		if (parentObj instanceof VirtualCrosstabCellAdapter && childParent instanceof CrosstabCellAdapter) {
			CrosstabCellAdapter childAdapter = (CrosstabCellAdapter) childParent;
			VirtualCrosstabCellAdapter parentAdapter = (VirtualCrosstabCellAdapter) parentObj;
			if (parentAdapter.getType() == VirtualCrosstabCellAdapter.IMMACULATE_TYPE
					|| parentAdapter.getType() == VirtualCrosstabCellAdapter.MEASURE_TYPE) {
				return UnexecutableCommand.INSTANCE;
			}
			if (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals(childAdapter.getPositionType())) {
				if (!(after instanceof FirstLevelHandleDataItemEditPart)) {
					afterObj = null;
				}
				if (parent.getParent() == getOperator(child).getParent()) {
					ChangeAreaCommand command = new ChangeAreaCommand(parentAdapter.getDesignElementHandle(),
							childAdapter.getDesignElementHandle(), DNDUtil.unwrapToModel(afterObj));

					command.setType(parentAdapter.getType());
					return command;
				} else {
					return UnexecutableCommand.INSTANCE;
				}
			}
		}
		return UnexecutableCommand.INSTANCE;
	}

	private EditPart getOperator(EditPart child) {
		// if (child instanceof CrosstabCellEditPart)
		// {
		// return child;
		// }
		// return child.getParent( );
		return child;
	}

	private Class getArrayType(Object[] array) {
		Class type = null;
		for (int i = 0; i < array.length; i++) {
			if (type == null)
				type = array[i].getClass();
			else if (type != array[i].getClass())
				return null;
		}
		return type;
	}
}
