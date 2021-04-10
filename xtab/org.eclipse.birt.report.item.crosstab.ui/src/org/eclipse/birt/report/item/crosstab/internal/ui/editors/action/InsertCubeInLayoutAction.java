/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.model.LibRootModel;
import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction;
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
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
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
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 */

public class InsertCubeInLayoutAction extends AbstractViewAction {

	public static final String DISPLAY_TEXT = Messages.getString("InsertCubeInLayoutAction.action.text"); //$NON-NLS-1$

	private EditPart targetPart;

	public InsertCubeInLayoutAction(Object selectedObject, String text) {
		super(selectedObject, text);
		// TODO Auto-generated constructor stub
	}

	public InsertCubeInLayoutAction(Object selectedObject) {
		super(selectedObject, DISPLAY_TEXT);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public boolean isEnabled() {
		Object obj = getSelection();
		if (obj instanceof Object[]) {
			if (((Object[]) obj).length == 0 || ((Object[]) obj).length > 1) {
				return false;
			}
		} else if (obj instanceof IStructuredSelection) {
			if (((IStructuredSelection) obj).size() == 0 || ((IStructuredSelection) obj).size() > 1) {
				return false;
			}
		}

		return canDrop(getFirstSelectedObj(), getTargetEditPart());
	}

	protected Object getFirstSelectedObj() {
		Object obj = getSelection();
		if (obj instanceof Object[]) {
			obj = ((Object[]) obj)[0];
		} else if (obj instanceof IStructuredSelection) {
			obj = ((IStructuredSelection) obj).getFirstElement();
		}
		return obj;
	}

	protected EditPart getTargetEditPart() {
		if (targetPart == null) {
			EditPartViewer viewer = UIUtil.getLayoutEditPartViewer();
			if (viewer == null) {
				return null;
			}
			IStructuredSelection targets = (IStructuredSelection) viewer.getSelection();
			if (targets.isEmpty() && targets.size() > 1)
				return null;
			targetPart = (EditPart) targets.getFirstElement();
		}
		return targetPart;
	}

	public boolean canDrop(Object transfer, Object target) {
		if (target != null && transfer instanceof CubeHandle) {
			SlotHandle targetSlot = getTargetSlotHandle(target, ICrosstabConstants.CROSSTAB_EXTENSION_NAME); // $NON-NLS-1$
			if (targetSlot != null) {
				if (DNDUtil.handleValidateTargetCanContainType(targetSlot, "Crosstab")
						&& DNDUtil.handleValidateTargetCanContainMore(targetSlot, 0))
					return true;
			} else {
				IStructuredSelection models = InsertInLayoutUtil.editPart2Model(new StructuredSelection(target));
				if (!models.isEmpty()) {
					Object model = DNDUtil.unwrapToModel(models.getFirstElement());
					if (model instanceof DesignElementHandle) {
						DesignElementHandle targetHandle = (DesignElementHandle) model;
						if (targetHandle.canContain(DEUtil.getDefaultContentName(targetHandle),
								ICrosstabConstants.CROSSTAB_EXTENSION_NAME))
							return true;
					}
				}
			}

		}
		return false;
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

	/**
	 * The default implementation of this <code>IAction</code> method does nothing.
	 * Subclasses should override this method if they do not need information from
	 * the triggering event, or override <code>runWithEvent(Event)</code> if they
	 * do.
	 */
	public void run() {

		CubeHandle transfer = (CubeHandle) this.getFirstSelectedObj();

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
				return;
			}
		}
		ExtendedItemHandle handle = null;
		String name = ReportPlugin.getDefault().getCustomName(ICrosstabConstants.CROSSTAB_EXTENSION_NAME);

		try {
			handle = CrosstabExtendedItemFactory
					.createCrosstabReportItem(SessionHandleAdapter.getInstance().getReportDesignHandle(), null, name);
		} catch (Exception e) {
			stack.rollback();
			return;
		}

		// fix for 233149
		if (targetPart instanceof EditPart)// drop on layout
		{
			EditPartViewer viewer = ((EditPart) targetPart).getViewer();
			EditPart editPart = (EditPart) targetPart;
			if (editPart != null) {
				try {
					// CreateRequest request = new CreateRequest( );
					//
					// request.getExtendedData( )
					// .put( DesignerConstants.KEY_NEWOBJECT, handle );
					//
					// Command command = editPart.getCommand( request );
					HashMap map = new HashMap();
					map.put(DesignerConstants.KEY_NEWOBJECT, handle);
					CreateCommand command = new CreateCommand(map);

					Object parentModel = DNDUtil.unwrapToModel(targetPart.getModel());

					if (parentModel instanceof DesignElementHandle) {
						DesignElementHandle parentHandle = (DesignElementHandle) parentModel;
						if (parentHandle.getDefn().isContainer() && (parentHandle
								.canContain(DEUtil.getDefaultSlotID(parentHandle), handle)
								|| parentHandle.canContain(DEUtil.getDefaultContentName(parentHandle), handle))) {
							command.setParent(parentHandle);
						} else {
							if (parentHandle.getContainerSlotHandle() != null) {
								command.setAfter(
										parentHandle.getContainerSlotHandle().get(parentHandle.getIndex() + 1));
							} else if (parentHandle.getContainerPropertyHandle() != null) {
								command.setAfter(
										parentHandle.getContainerPropertyHandle().get(parentHandle.getIndex() + 1));
							}

							DesignElementHandle container = parentHandle.getContainer();

							// special handling for list item, always use
							// slothandle
							// as parent
							if (container instanceof ListHandle) {
								command.setParent(parentHandle.getContainerSlotHandle());
							} else {
								command.setParent(container);
							}
						}
					} else if (parentModel instanceof SlotHandle) {
						command.setParent(parentModel);
					} else {
						command.setParent(SessionHandleAdapter.getInstance().getReportDesignHandle());
					}
					if (command != null && command.canExecute()) {
						viewer.getEditDomain().getCommandStack().execute(command);

						handle.setProperty(IReportItemModel.CUBE_PROP, cube);

						List dimensions = cube.getContents(CubeHandle.DIMENSIONS_PROP);
						for (Iterator iterator = dimensions.iterator(); iterator.hasNext();) {
							DimensionHandle dimension = (DimensionHandle) iterator.next();
							if (dimension.isTimeType()) {
								createDimensionViewHandle(handle, dimension, ICrosstabConstants.COLUMN_AXIS_TYPE);
							} else {
								createDimensionViewHandle(handle, dimension, ICrosstabConstants.ROW_AXIS_TYPE);
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
						CrosstabUtil.addAllHeaderLabel((CrosstabReportItemHandle) handle.getReportItem());
						stack.commit();

						// 251960, viewer had been refreshed since model commit,
						// and the targetEditPart is changed,
						// if we need to flush the viewer, we must re-retrieve
						// it
						// if ( targetPart instanceof EditPart )
						// {
						// ( (EditPart) targetPart ).getViewer( ).flush( );
						// }

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

						return;
					}
				} catch (Exception e) {
					stack.rollback();
					return;
				}
			}
		}

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
