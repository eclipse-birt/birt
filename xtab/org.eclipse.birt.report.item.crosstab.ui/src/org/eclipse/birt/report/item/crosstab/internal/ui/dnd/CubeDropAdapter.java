/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.model.LibRootModel;
import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportCreationTool;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 *
 */

public class CubeDropAdapter implements IDropAdapter {

	// FIXME need refactor
	@Override
	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (target != null && transfer instanceof CubeHandle) {
			SlotHandle targetSlot = getTargetSlotHandle(target, ICrosstabConstants.CROSSTAB_EXTENSION_NAME); // $NON-NLS-1$
			if (targetSlot != null) {
				if (DNDUtil.handleValidateTargetCanContainType(targetSlot, "Crosstab") //$NON-NLS-1$
						&& DNDUtil.handleValidateTargetCanContainMore(targetSlot, 0)) {
					return DNDService.LOGIC_TRUE;
				}
			} else {
				// If the target is crosstab,and create chart view, maybe the container cann't
				// contain the cube
				if (target instanceof CrosstabCellEditPart) {
					Object model = DNDUtil.unwrapToModel(((CrosstabCellEditPart) target).getModel());
					if (model == null) {
						return DNDService.LOGIC_FALSE;
					}
					ExtendedItemHandle handle = null;
					String name = ReportPlugin.getDefault().getCustomName(ICrosstabConstants.CROSSTAB_EXTENSION_NAME);

					try {
						SessionHandleAdapter.getInstance().getReportDesignHandle().getCommandStack().startTrans(""); //$NON-NLS-1$
						handle = CrosstabExtendedItemFactory.createCrosstabReportItem(
								SessionHandleAdapter.getInstance().getReportDesignHandle(), null, name);
						handle.setProperty(IReportItemModel.CUBE_PROP, (CubeHandle) transfer);
						int flag = DNDUtil.handleValidateTargetCanContain(model, handle, false);
						if (flag == DNDUtil.CONTAIN_NO) {
							return DNDService.LOGIC_FALSE;
						}
					} catch (Exception e) {
						return DNDService.LOGIC_FALSE;
					} finally {
						SessionHandleAdapter.getInstance().getReportDesignHandle().getCommandStack().rollbackAll();
					}
				}
				// fix for 233149
				IStructuredSelection models = InsertInLayoutUtil.editPart2Model(new StructuredSelection(target));
				if (!models.isEmpty()) {
					Object model = DNDUtil.unwrapToModel(models.getFirstElement());
					if (model instanceof DesignElementHandle) {
						DesignElementHandle targetHandle = (DesignElementHandle) model;
						if (targetHandle.canContain(DEUtil.getDefaultContentName(targetHandle),
								ICrosstabConstants.CROSSTAB_EXTENSION_NAME)) {
							return DNDService.LOGIC_TRUE;
						}
					}
				}
			}

		}
		return DNDService.LOGIC_UNKNOW;
	}

	private SlotHandle getTargetSlotHandle(Object target, String insertType) {
		IStructuredSelection models = InsertInLayoutUtil.editPart2Model(new StructuredSelection(target));
		if (models.isEmpty()) {
			return null;
		}
		// model = models.getFirstElement( );
		Object model = DNDUtil.unwrapToModel(models.getFirstElement());
		if (model instanceof LibRootModel) {
			model = ((LibRootModel) model).getModel();
		}
		if (model instanceof SlotHandle) {
			return (SlotHandle) model;
		} else if (model instanceof DesignElementHandle) {
			DesignElementHandle handle = (DesignElementHandle) model;

			if (handle.getDefn().isContainer()) {
				int slotId = DEUtil.getDefaultSlotID(handle);
				if (handle.canContain(slotId, insertType)) {
					return handle.getSlot(slotId);
				}
			}
			return handle.getContainerSlotHandle();
		}
		return null;
	}

	@Override
	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {

		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(Messages.getString("InsertCubeInLayoutAction.action.message")); //$NON-NLS-1$
		CubeHandle cube = (CubeHandle) transfer;
		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		if (cube.getModuleHandle() != moduleHandle && cube.getRoot() instanceof LibraryHandle) {
			try {
				UIUtil.includeLibrary(moduleHandle, (LibraryHandle) cube.getRoot());
				cube = (CubeHandle) moduleHandle.getElementFactory().newElementFrom(cube, cube.getName());
				moduleHandle.getCubes().add(cube);
			} catch (Exception e) {
				stack.rollback();
				return false;
			}
		}
		ExtendedItemHandle handle = null;
		String name = ReportPlugin.getDefault().getCustomName(ICrosstabConstants.CROSSTAB_EXTENSION_NAME);

		try {
			handle = CrosstabExtendedItemFactory
					.createCrosstabReportItem(SessionHandleAdapter.getInstance().getReportDesignHandle(), null, name);
		} catch (Exception e) {
			stack.rollback();
			return false;
		}

		// fix for 233149
		if (target instanceof EditPart)// drop on layout
		{
			EditPartViewer viewer = ((EditPart) target).getViewer();
			EditPart editPart = (EditPart) target;
			if (editPart != null) {
				try {
					CreateRequest request = new CreateRequest();

					request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, handle);
					request.setLocation(location.getPoint());
					Command command = editPart.getCommand(request);
					if (command != null && command.canExecute()) {
						viewer.getEditDomain().getCommandStack().execute(command);

						handle.setProperty(IReportItemModel.CUBE_PROP, cube);

						List dimensions = cube.getContents(CubeHandle.DIMENSIONS_PROP);
						for (Iterator iterator = dimensions.iterator(); iterator.hasNext();) {
							DimensionHandle dimension = (DimensionHandle) iterator.next();
							if (dimension instanceof TabularDimensionHandle && !dimension.isTimeType()) {
								createDimensionViewHandle(handle, dimension, ICrosstabConstants.ROW_AXIS_TYPE);
							} else {
								createDimensionViewHandle(handle, dimension, ICrosstabConstants.COLUMN_AXIS_TYPE);
							}
						}

						List measureGroups = cube.getContents(CubeHandle.MEASURE_GROUPS_PROP);
						int index = 0;
						for (Iterator iterator = measureGroups.iterator(); iterator.hasNext();) {
							MeasureGroupHandle measureGroup = (MeasureGroupHandle) iterator.next();
							List measures = measureGroup.getContents(MeasureGroupHandle.MEASURES_PROP);
							for (int j = 0; j < measures.size(); j++) {
								Object temp = measures.get(j);
								if (temp instanceof MeasureHandle) {
									addMeasureHandle(handle, (MeasureHandle) temp, index++);
								}
							}
						}
						// CrosstabModelUtil.validateCrosstabHeader((
						// CrosstabReportItemHandle)handle.getReportItem( ) );
						CrosstabUtil.addAllHeaderLabel((CrosstabReportItemHandle) handle.getReportItem());
						stack.commit();

						viewer.flush();
						viewer.getControl().setFocus();
						ReportCreationTool.selectAddedObject(handle, viewer);

						return true;
					}
				} catch (Exception e) {
					stack.rollback();
					return false;
				}
			}
		}

		// below may be changed to use edit part's create command as above

		Map map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, handle);
		CreateCommand command = new CreateCommand(map);

		try {
			SlotHandle parentModel = getTargetSlotHandle(target, ICrosstabConstants.CROSSTAB_EXTENSION_NAME);

			if (parentModel != null) {
				command.setParent(parentModel);
			} else {
				command.setParent(SessionHandleAdapter.getInstance().getReportDesignHandle());
			}
			command.execute();

			handle.setProperty(IReportItemModel.CUBE_PROP, cube);

			List dimensions = cube.getContents(CubeHandle.DIMENSIONS_PROP);
			for (Iterator iterator = dimensions.iterator(); iterator.hasNext();) {
				DimensionHandle dimension = (DimensionHandle) iterator.next();
				if (dimension instanceof TabularDimensionHandle && !dimension.isTimeType()) {
					createDimensionViewHandle(handle, dimension, ICrosstabConstants.ROW_AXIS_TYPE);
				} else {
					createDimensionViewHandle(handle, dimension, ICrosstabConstants.COLUMN_AXIS_TYPE);
				}
			}

			List measureGroups = cube.getContents(CubeHandle.MEASURE_GROUPS_PROP);
			int index = 0;
			for (Iterator iterator = measureGroups.iterator(); iterator.hasNext();) {
				MeasureGroupHandle measureGroup = (MeasureGroupHandle) iterator.next();
				List measures = measureGroup.getContents(MeasureGroupHandle.MEASURES_PROP);
				for (int j = 0; j < measures.size(); j++) {
					Object temp = measures.get(j);
					if (temp instanceof MeasureHandle) {
						addMeasureHandle(handle, (MeasureHandle) temp, index++);
					}
				}
			}
			stack.commit();

			if (target instanceof EditPart) {
				((EditPart) target).getViewer().flush();
			}

			ReportRequest request = new ReportRequest();
			List selectionObjects = new ArrayList();
			selectionObjects.add(handle);
			request.setSelectionObject(selectionObjects);
			request.setType(ReportRequest.SELECTION);
			SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);

			if (SessionHandleAdapter.getInstance().getReportDesignHandle() instanceof LibraryHandle) {
				HandleAdapterFactory.getInstance().getLibraryHandleAdapter().setCurrentEditorModel(handle,
						LibraryHandleAdapter.CREATE_ELEMENT);
			}

		} catch (Exception e) {
			stack.rollback();
			return false;
		}

		return true;
	}

	private void addMeasureHandle(ExtendedItemHandle handle, MeasureHandle measureHandle, int index)
			throws SemanticException {
		IReportItem reportItem = handle.getReportItem();
		CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) reportItem;
		CrosstabAdaptUtil.addMeasureHandle(xtabHandle, measureHandle, index);
	}

	private void createDimensionViewHandle(ExtendedItemHandle handle, DimensionHandle dimensionHandle, int type)
			throws SemanticException {
		if (dimensionHandle.getDefaultHierarchy().getLevelCount() > 0) {
			IReportItem reportItem = handle.getReportItem();
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) reportItem;

			DimensionViewHandle viewHandle = xtabHandle.insertDimension(dimensionHandle, type,
					xtabHandle.getDimensionCount(type));

			LevelHandle[] levels = getLevelHandles(dimensionHandle);
			for (int j = 0; j < levels.length; j++) {

				LevelHandle levelHandle = levels[j];
				DataItemHandle dataHandle = CrosstabAdaptUtil
						.createColumnBindingAndDataItem((ExtendedItemHandle) xtabHandle.getModelHandle(), levelHandle);
				LevelViewHandle levelViewHandle = viewHandle.insertLevel(levelHandle, j);
				CrosstabCellHandle cellHandle = levelViewHandle.getCell();

				cellHandle.addContent(dataHandle);

				// copy action to dataHandle
				ActionHandle actionHandle = levelHandle.getActionHandle();
				if (actionHandle != null) {
					List source = new ArrayList();
					source.add(actionHandle.getStructure());
					List newAction = ModelUtil.cloneStructList(source);
					dataHandle.setAction((Action) newAction.get(0));
				}
			}
		}
	}

	private LevelHandle[] getLevelHandles(DimensionHandle dimensionHandle) {
		LevelHandle[] dimensionLevelHandles = new LevelHandle[dimensionHandle.getDefaultHierarchy().getLevelCount()];
		for (int i = 0; i < dimensionLevelHandles.length; i++) {
			dimensionLevelHandles[i] = dimensionHandle.getDefaultHierarchy().getLevel(i);
		}
		return dimensionLevelHandles;
	}
}
