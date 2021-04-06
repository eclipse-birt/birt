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
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.AddDimensionViewHandleCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.AddLevelAttributeHandleCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.AddMeasureViewHandleCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.AddMultipleMeasureCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.ChangeAreaCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.ChangeMeasureOrderCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CrosstabCellCreateCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CrosstabFlowMoveChildCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CrosstabPasterCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Crosstab cell police
 */

public class CrosstabCellFlowLayoutEditPolicy extends ReportFlowLayoutEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies
	 * .ReportFlowLayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.
	 * CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		EditPart after;
		if (getLocationFromRequest(request) == null) {
			after = null;
		} else {
			after = getInsertionReference(request);
		}

		// CreateCommand command ;

		Object model = this.getHost().getModel();
		Object newObject = request.getExtendedData().get(DesignerConstants.KEY_NEWOBJECT);

		if (model instanceof CrosstabCellAdapter) {
			// EditPart parent = getHost( ).getParent( );
			// CrosstabHandleAdapter adapter = ( (CrosstabTableEditPart) parent
			// ).getCrosstabHandleAdapter( );
			int type = getAreaType((CrosstabCellAdapter) model);
			String position = ((CrosstabCellAdapter) model).getPositionType();
			if ((newObject instanceof DimensionHandle || newObject instanceof LevelHandle)
					&& (type == ICrosstabConstants.COLUMN_AXIS_TYPE || type == ICrosstabConstants.ROW_AXIS_TYPE)) {
				Object afterObj = after == null ? null : after.getModel();

				if (newObject instanceof LevelHandle) {
					DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle((LevelHandle) newObject);
					AddDimensionViewHandleCommand command = new AddDimensionViewHandleCommand(
							(CrosstabCellAdapter) model, type, dimensionHandle, afterObj);
					command.setLevelHandles(new LevelHandle[] { (LevelHandle) newObject });
					return command;
				}

				return new AddDimensionViewHandleCommand((CrosstabCellAdapter) model, type, (DimensionHandle) newObject,
						afterObj);
			} else if (newObject instanceof LevelAttributeHandle) {
				Object afterObj = after == null ? null : after.getModel();
				LevelHandle levelHandle = (LevelHandle) ((LevelAttributeHandle) newObject).getElementHandle();
				DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle(levelHandle);
				AddLevelAttributeHandleCommand command = new AddLevelAttributeHandleCommand((CrosstabCellAdapter) model,
						type, dimensionHandle, new LevelAttributeHandle[] { (LevelAttributeHandle) newObject },
						afterObj);
				return command;
			} else if (newObject instanceof MeasureHandle
					&& position.equals(ICrosstabCellAdapterFactory.CELL_MEASURE)) {
				Object afterObj = null;
				if (after != null) {
					afterObj = after.getModel();
				}
				return new AddMeasureViewHandleCommand((CrosstabCellAdapter) model, (MeasureHandle) newObject,
						afterObj);
			}

			else if (newObject instanceof MeasureGroupHandle
					&& position.equals(ICrosstabCellAdapterFactory.CELL_MEASURE)) {
				List list = new ArrayList();
				list.add(newObject);

				Object afterObj = null;
				if (after != null) {
					afterObj = after.getModel();
				}

				return new AddMultipleMeasureCommand((CrosstabCellAdapter) model, list, afterObj);
			} else if (newObject instanceof Object[] && CrosstabAdaptUtil.canCreateMultipleCommand((Object[]) newObject)
					&& position.equals(ICrosstabCellAdapterFactory.CELL_MEASURE)) {
				List list = new ArrayList();
				Object[] objs = (Object[]) newObject;
				for (int i = 0; i < objs.length; i++) {
					list.add(objs[i]);
				}

				Object afterObj = null;
				if (after != null) {
					afterObj = after.getModel();
				}
				return new AddMultipleMeasureCommand((CrosstabCellAdapter) model, list, afterObj);
			} else if (isLevelHandles(newObject)) {
				Object[] items = (Object[]) newObject;
				LevelHandle[] levelHandles = new LevelHandle[items.length];
				System.arraycopy(items, 0, levelHandles, 0, levelHandles.length);
				Object afterObj = after == null ? null : after.getModel();
				DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle(levelHandles[0]);
				AddDimensionViewHandleCommand command = new AddDimensionViewHandleCommand((CrosstabCellAdapter) model,
						type, dimensionHandle, afterObj);
				command.setLevelHandles(levelHandles);
				return command;
			} else if (newObject instanceof Object[]) {
				Class arrayType = getArrayType((Object[]) newObject);
				if (LevelAttributeHandle.class.isAssignableFrom(arrayType)) {
					Object[] items = (Object[]) newObject;
					LevelAttributeHandle[] levelAttributeHandles = new LevelAttributeHandle[items.length];
					System.arraycopy(items, 0, levelAttributeHandles, 0, levelAttributeHandles.length);
					Object afterObj = after == null ? null : after.getModel();
					LevelHandle levelHandle = (LevelHandle) ((LevelAttributeHandle) items[0]).getElementHandle();
					DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle(levelHandle);
					AddLevelAttributeHandleCommand command = new AddLevelAttributeHandleCommand(
							(CrosstabCellAdapter) model, type, dimensionHandle, levelAttributeHandles, afterObj);
					return command;
				} else {
					return getCrosstabCellCreateCommand(request, after);
				}
			} else {
				return getCrosstabCellCreateCommand(request, after);
			}
		}
		// TODO there is a bug, include design ui
		// ReportFlowLayoutEditPolicy.code can't return null,
		// must call the super method.
		return super.getCreateCommand(request);
	}

	private CrosstabCellCreateCommand getCrosstabCellCreateCommand(CreateRequest request, EditPart after) {
		CrosstabCellCreateCommand command = new CrosstabCellCreateCommand(request.getExtendedData());
		command.setParent(getHost().getModel());
		command.setAfter(after == null ? null : after.getModel());
		return command;
	}

	private boolean isLevelHandles(Object newObject) {
		if (newObject instanceof Object[]) {
			DesignElementHandle container = null;
			Object[] items = (Object[]) newObject;
			for (int i = 0; i < items.length; i++) {
				if (!(items[i] instanceof LevelHandle))
					return false;
				if (container == null)
					container = ((LevelHandle) items[i]).getContainer();
				else if (container != ((LevelHandle) items[i]).getContainer())
					return false;
			}
			return items.length > 0;
		}
		return false;
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

	private int getAreaType(CrosstabCellAdapter cellAdapter) {
		AbstractCrosstabItemHandle handle = cellAdapter.getCrosstabItemHandle().getContainer();
		while (handle != null) {
			if (handle instanceof DimensionViewHandle) {
				return ((DimensionViewHandle) handle).getAxisType();

			}
			handle = handle.getContainer();
		}
		return -1;
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
		// Object parentObj = parent.getModel( );
		// Object source = child.getModel( );
		// Object afterObj = after == null ? null : after.getModel( );
		// Object childParent = child.getParent( ).getModel( );
		// if (parentObj instanceof CrosstabCellAdapter && childParent
		// instanceof CrosstabCellAdapter)
		// {
		// CrosstabCellAdapter childAdapter = (CrosstabCellAdapter)childParent;
		// CrosstabCellAdapter parentAdapter = (CrosstabCellAdapter)parentObj;
		// if (isFirstDataItem( childAdapter, child.getModel( ),
		// ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE )
		// && (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals(
		// parentAdapter.getPositionType( ) )
		// || ICrosstabCellAdapterFactory.CELL_LEVEL_HANDLE.equals(
		// parentAdapter.getPositionType( )))
		// )
		// {
		// if (afterObj != parentAdapter.getFirstDataItem( ))
		// {
		// afterObj = null;
		// }
		// if (childAdapter.getCrosstabCellHandle( ).getCrosstab( ) ==
		// parentAdapter.getCrosstabCellHandle( ).getCrosstab( ))
		// {
		// return new ChangeAreaCommand(parentAdapter.getDesignElementHandle( ),
		// childAdapter.getDesignElementHandle( ),(DesignElementHandle)
		// DNDUtil.unwrapToModel( afterObj ) );
		// }
		// else
		// {
		// return UnexecutableCommand.INSTANCE;
		// }
		// }
		// else if (isFirstDataItem( childAdapter, child.getModel( ),
		// ICrosstabCellAdapterFactory.CELL_MEASURE )
		// && ICrosstabCellAdapterFactory.CELL_MEASURE.equals(
		// parentAdapter.getPositionType( ) ))
		// {
		// if (afterObj != parentAdapter.getFirstDataItem( ))
		// {
		// afterObj = null;
		// }
		// if (childAdapter.getCrosstabCellHandle( ).getCrosstab( ) ==
		// parentAdapter.getCrosstabCellHandle( ).getCrosstab( ))
		// {
		// return new
		// ChangeMeasureOrderCommand(parentAdapter.getDesignElementHandle( ),
		// childAdapter.getDesignElementHandle( ),(DesignElementHandle)
		// DNDUtil.unwrapToModel( afterObj ) );
		// }
		// else
		// {
		// return UnexecutableCommand.INSTANCE;
		// }
		//
		// }
		// }
		// if (childParent instanceof CrosstabCellAdapter)
		// {
		// CrosstabCellAdapter childAdapter = (CrosstabCellAdapter)childParent;
		// if (isFirstDataItem( childAdapter, child.getModel( ),
		// ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE )
		// || isFirstDataItem( childAdapter, child.getModel( ),
		// ICrosstabCellAdapterFactory.CELL_MEASURE ))
		// {
		// return UnexecutableCommand.INSTANCE;
		// }
		// }
		// return new CrosstabPasterCommand( (DesignElementHandle)
		// DNDUtil.unwrapToModel( source ),
		// (DesignElementHandle) DNDUtil.unwrapToModel( parentObj ),
		// (DesignElementHandle) DNDUtil.unwrapToModel( afterObj ) );

		Object parentObj = parent.getModel();
		Object source = child.getModel();
		Object afterObj = after == null ? null : after.getModel();
		Object childParent = child.getModel();
		if (parentObj instanceof CrosstabCellAdapter && childParent instanceof CrosstabCellAdapter) {
			CrosstabCellAdapter childAdapter = (CrosstabCellAdapter) childParent;
			CrosstabCellAdapter parentAdapter = (CrosstabCellAdapter) parentObj;
			if (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals(childAdapter.getPositionType())
					&& (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals(parentAdapter.getPositionType())
							|| ICrosstabCellAdapterFactory.CELL_LEVEL_HANDLE.equals(parentAdapter.getPositionType()))) {
				if (afterObj != parentAdapter.getFirstElement()) {
					afterObj = null;
				}
				if (childAdapter.getCrosstabCellHandle().getCrosstab() == parentAdapter.getCrosstabCellHandle()
						.getCrosstab()) {
					return new ChangeAreaCommand(parentAdapter.getDesignElementHandle(),
							childAdapter.getDesignElementHandle(), DNDUtil.unwrapToModel(afterObj));
				} else {
					return UnexecutableCommand.INSTANCE;
				}
			} else if (ICrosstabCellAdapterFactory.CELL_MEASURE.equals(childAdapter.getPositionType())
					&& ICrosstabCellAdapterFactory.CELL_MEASURE.equals(parentAdapter.getPositionType())) {
				if (afterObj != parentAdapter.getFirstElement()) {
					afterObj = null;
				}
				if (childAdapter.getCrosstabCellHandle().getCrosstab() == parentAdapter.getCrosstabCellHandle()
						.getCrosstab()) {
					return new ChangeMeasureOrderCommand(parentAdapter.getDesignElementHandle(),
							childAdapter.getDesignElementHandle(), DNDUtil.unwrapToModel(afterObj));
				} else {
					return UnexecutableCommand.INSTANCE;
				}

			}
			return UnexecutableCommand.INSTANCE;
		}
		return new CrosstabPasterCommand((DesignElementHandle) DNDUtil.unwrapToModel(source),
				(DesignElementHandle) DNDUtil.unwrapToModel(parentObj),
				(DesignElementHandle) DNDUtil.unwrapToModel(afterObj));
	}

	private boolean isFirstDataItem(CrosstabCellAdapter adapter, Object model, String type) {
		if (adapter.getPositionType().equals(type)) {
			return adapter.getFirstDataItem() == model;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy#createMoveChildCommand
	 * (org.eclipse.gef.EditPart, org.eclipse.gef.EditPart)
	 */
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		Object afterModel = null;
		if (after != null) {
			afterModel = after.getModel();
		}
		CrosstabFlowMoveChildCommand command = new CrosstabFlowMoveChildCommand(child.getModel(), afterModel,
				child.getParent().getModel());
		return command;
	}

	private EditPart getOperator(EditPart child) {
		if (child instanceof CrosstabCellEditPart) {
			return child;
		}
		return child.getParent();
	}

	protected void showLayoutTargetFeedback(Request request) {

		boolean isCrossTabElement = false;

		if (request instanceof ChangeBoundsRequest) {
			List editParts = ((ChangeBoundsRequest) request).getEditParts();
			if (editParts.size() > 0) {
				isCrossTabElement = editParts.get(0) instanceof CrosstabCellEditPart;
			}
		}

		if (!isCrossTabElement) {
			Object template = TemplateTransfer.getInstance().getTemplate();
			if (template instanceof Object[] && ((Object[]) template).length > 0) {
				Object dragObject = ((Object[]) template)[0];
				if (dragObject instanceof DimensionHandle || dragObject instanceof MeasureHandle
						|| dragObject instanceof LevelHandle) {
					isCrossTabElement = true;
				}
			}
		}

		if (isCrossTabElement) {
			Transposer transposer = new Transposer();
			transposer.setEnabled(!isHorizontal());
			Rectangle r = transposer.t(getAbsoluteClientBounds((GraphicalEditPart) getHost()));
			Point p = transposer.t(getLocationFromRequest(request));
			boolean before = p.x <= r.x + (r.width / 2);

			Point p1 = new Point(before ? r.x : r.x + r.width, r.y - 2);
			p1 = transposer.t(p1);

			Point p2 = new Point(before ? r.x : r.x + r.width, r.y + r.height + 7);
			p2 = transposer.t(p2);

			setTargetFeedbackPoints(p1, p2);

		} else {
			super.showLayoutTargetFeedback(request);
		}
	}

}
