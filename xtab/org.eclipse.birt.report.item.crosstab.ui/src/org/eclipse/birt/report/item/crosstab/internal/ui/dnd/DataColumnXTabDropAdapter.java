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

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.data.adapter.api.LinkedDataSetUtil;
import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.GroupDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.SimpleCubeBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureGroupModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class DataColumnXTabDropAdapter implements IDropAdapter {

	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (!isDataColumn(transfer))
			return DNDService.LOGIC_UNKNOW;
		DesignElementHandle handle = getExtendedItemHandle(target);
		if (handle != null && handle instanceof ReportItemHandle) {
			// when xtab has not bind with Cube, data item can drop on
			// everywhere in xtab.
			if (handle.getProperty(IReportItemModel.CUBE_PROP) == null
					&& !LinkedDataSetUtil.bindToLinkedDataSet((ReportItemHandle) handle)
					&& (target instanceof CrosstabTableEditPart || target instanceof CrosstabCellEditPart)) {
				return DNDService.LOGIC_TRUE;
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	private boolean isDataColumn(Object transfer) {
		if (transfer instanceof Object[]) {
			Object[] transfers = (Object[]) transfer;
			for (int i = 0; i < transfers.length; i++) {
				if (!isDataColumn(transfers[i]))
					return false;
			}
			return true;
		}
		return transfer instanceof ResultSetColumnHandle;
	}

	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {
		DesignElementHandle handle = getExtendedItemHandle(target);
		if (handle != null) {
			if (handle.getProperty(IReportItemModel.CUBE_PROP) != null) {
				EditPart editPart = (EditPart) target;

				if (editPart != null) {
					CreateRequest request = new CreateRequest();

					request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, transfer);
					request.setLocation(location.getPoint());
					Command command = editPart.getCommand(request);
					if (command != null && command.canExecute()) {
						editPart.getViewer().getEditDomain().getCommandStack().execute(command);
						return true;
					} else
						return false;
				}
				return false;
			} else {
				CommandStack stack = getActionStack();
				stack.startTrans("Create Cube"); //$NON-NLS-1$
				try {
					ResultSetColumnHandle columnHandle = getColumnHandle(transfer);
					if (columnHandle != null) {
						DataSetHandle dataSetHandle = (DataSetHandle) columnHandle.getElementHandle();

						TabularCubeHandle newCube = DesignElementFactory.getInstance()
								.newTabularCube(Messages.getString("DataColumnXTabDropAdapter.DataCube") //$NON-NLS-1$
										+ " - " //$NON-NLS-1$
										+ dataSetHandle.getName());

						SessionHandleAdapter.getInstance().getReportDesignHandle().getCubes().add(newCube);

						SimpleCubeBuilder builder = new SimpleCubeBuilder(
								PlatformUI.getWorkbench().getDisplay().getActiveShell());
						builder.setInput(newCube, dataSetHandle);

						EditPart editPart = (EditPart) target;

						if (editPart != null) {
							Object model = editPart.getModel();
							if (model instanceof VirtualCrosstabCellAdapter) {
								VirtualCrosstabCellAdapter adapter = (VirtualCrosstabCellAdapter) model;
								if (adapter.getType() == VirtualCrosstabCellAdapter.ROW_TYPE
										|| adapter.getType() == VirtualCrosstabCellAdapter.COLUMN_TYPE) {
									createDimension(columnHandle, newCube);
								} else if (adapter.getType() == VirtualCrosstabCellAdapter.MEASURE_TYPE) {
									createMeasureGroup(columnHandle, newCube);
								}
							}
						}

						if (builder.open() == Window.OK) {
							if (handle != null) {
								handle.setProperty(IReportItemModel.CUBE_PROP, newCube);
							}
							stack.commit();

							ReportRequest request = new ReportRequest();
							List selectionObjects = new ArrayList();
							selectionObjects.add(handle);
							request.setSelectionObject(selectionObjects);
							request.setType(ReportRequest.SELECTION);
							SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);

							storePreference();
						} else {
							stack.rollback();
						}
					}
					return true;
				} catch (Exception e) {
					stack.rollback();
					ExceptionUtil.handle(e);
				}
			}
		}
		return false;
	}

	private void createMeasureGroup(ResultSetColumnHandle columnHandle, TabularCubeHandle newCube) {
		TabularMeasureGroupHandle measureGroup = DesignElementFactory.getInstance().newTabularMeasureGroup(null);
		try {
			newCube.add(CubeHandle.MEASURE_GROUPS_PROP, measureGroup);
			// if ( newCube.getContentCount( ICubeModel.MEASURE_GROUPS_PROP ) ==
			// 1 )
			// newCube.setDefaultMeasureGroup( measureGroup );
			TabularMeasureHandle measure = DesignElementFactory.getInstance()
					.newTabularMeasure(columnHandle.getColumnName());
			Expression expression = new Expression(
					ExpressionUtility.getExpression(columnHandle,
							ExpressionUtility.getExpressionConverter(UIUtil.getDefaultScriptType())),
					UIUtil.getDefaultScriptType());
			measure.setExpressionProperty(MeasureHandle.MEASURE_EXPRESSION_PROP, expression);
			measure.setDataType(columnHandle.getDataType());
			measureGroup.add(IMeasureGroupModel.MEASURES_PROP, measure);
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
	}

	private void createDimension(ResultSetColumnHandle columnHandle, TabularCubeHandle newCube) {
		try {
			if (isDateType(columnHandle.getDataType())) {
				CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
				stack.startTrans("Create Group"); //$NON-NLS-1$
				GroupDialog dialog = createGroupDialog(newCube);
				dialog.setInput(newCube, columnHandle);
				if (dialog.open() == Window.CANCEL) {
					stack.rollback();
				} else
					stack.commit();
			} else {
				TabularDimensionHandle dimension = DesignElementFactory.getInstance().newTabularDimension(null);
				newCube.add(CubeHandle.DIMENSIONS_PROP, dimension);
				TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension
						.getContent(IDimensionModel.HIERARCHIES_PROP, 0);
				TabularLevelHandle level = DesignElementFactory.getInstance().newTabularLevel(dimension,
						columnHandle.getColumnName());
				level.setColumnName(columnHandle.getColumnName());
				level.setDataType(columnHandle.getDataType());
				hierarchy.add(IHierarchyModel.LEVELS_PROP, level);
			}
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
	}

	protected GroupDialog createGroupDialog(TabularCubeHandle cube) {
		Object adapter = ElementAdapterManager.getAdapter(cube, GroupDialog.class);
		try {
			if (adapter instanceof GroupDialog)
				return ((GroupDialog) adapter).getClass().newInstance();
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		return new GroupDialog();
	}

	private boolean isDateType(String dataType) {
		return dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATE)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_TIME);
	}

	private DesignElementHandle getExtendedItemHandle(Object target) {
		if (target instanceof CrosstabTableEditPart)
			return (DesignElementHandle) ((CrosstabTableEditPart) target).getModel();
		if (target instanceof EditPart) {
			EditPart part = (EditPart) target;
			DesignElementHandle handle = (DesignElementHandle) ((IAdaptable) target)
					.getAdapter(DesignElementHandle.class);
			if (handle == null && part.getParent() != null)
				return getExtendedItemHandle(part.getParent());

		}
		return null;
	}

	private ResultSetColumnHandle getColumnHandle(Object transfer) {
		if (transfer instanceof Object[]) {
			Object[] transfers = (Object[]) transfer;
			for (int i = 0; i < transfers.length; i++) {
				if (transfers[i] instanceof ResultSetColumnHandle)
					return (ResultSetColumnHandle) transfers[i];
			}
			return null;
		}
		if (transfer instanceof ResultSetColumnHandle)
			return (ResultSetColumnHandle) transfer;
		return null;
	}

	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	private void storePreference() {
		String prompt = PreferenceFactory.getInstance().getPreferences(CrosstabPlugin.getDefault())
				.getString(CrosstabPlugin.CUBE_BUILDER_WARNING_PREFERENCE);
		if (prompt == null || prompt.length() == 0 || (prompt.equals(MessageDialogWithToggle.PROMPT))) {
			MessageDialogWithToggle opendialog = MessageDialogWithToggle.openInformation(UIUtil.getDefaultShell(),
					Messages.getString("CubeBuilder.warning.title"), //$NON-NLS-1$
					Messages.getString("CubeBuilder.warning.message"), //$NON-NLS-1$
					Messages.getString("CubeBuilder.warning.prompt"), //$NON-NLS-1$
					false, null, null);
			if (opendialog.getReturnCode() != Window.OK) {
				return;
			}
			if (opendialog.getToggleState() == true) {
				savePreference(MessageDialogWithToggle.NEVER);
			} else {
				savePreference(MessageDialogWithToggle.PROMPT);
			}
		}
	}

	private void savePreference(String value) {
		PreferenceFactory.getInstance().getPreferences(CrosstabPlugin.getDefault())
				.setValue(CrosstabPlugin.CUBE_BUILDER_WARNING_PREFERENCE, value);
	}
}
