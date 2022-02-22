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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.runtime.GUIException;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.util.ColumnBindingUtil;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;

/**
 * The tool handle extends used by elements in the library
 */

public class LibraryElementsToolHandleExtends extends AbstractToolHandleExtends {

	private DesignElementHandle elementHandle;

	/**
	 * Constructor. Creates a new extends for the given element.
	 *
	 * @param elementHandle the handle of the element
	 */
	public LibraryElementsToolHandleExtends(DesignElementHandle elementHandle) {
		super();
		Assert.isLegal(elementHandle.getRoot() instanceof LibraryHandle);
		this.elementHandle = elementHandle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * AbstractToolHandleExtends#preHandleMouseUp()
	 */
	@Override
	public boolean preHandleMouseUp() {
		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		LibraryHandle library = (LibraryHandle) elementHandle.getRoot();
		try {
			if (UIUtil.includeLibrary(moduleHandle, library)) {
				if (elementHandle instanceof ThemeHandle) {
					ThemeHandle model = UIUtil.applyTheme((ThemeHandle) elementHandle, moduleHandle, library);
					if (model != null) {
						setModel(elementHandle);
					}
				} else {
					DesignElementHandle newHandle = moduleHandle.getElementFactory().newElementFrom(elementHandle,
							elementHandle.getName());
					setModel(newHandle);
				}
			}
		} catch (Exception e) {
			if (e instanceof InvalidParentException || e instanceof WrongTypeException) {
				GUIException exception = GUIException.createGUIException(ReportPlugin.REPORT_UI, e,
						"Library.DND.messages.outofsync");//$NON-NLS-1$
				ExceptionHandler.handle(exception);
			} else {
				ExceptionHandler.handle(e);
			}

		}
		getRequest().getExtendedData().put(DesignerConstants.NEWOBJECT_FROM_LIBRARY, Boolean.TRUE);
		return super.preHandleMouseUp();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * AbstractToolHandleExtends#preHandleMouseDown()
	 */
	@Override
	public boolean preHandleMouseDown() {
		return false;
	}

	@Override
	public boolean postHandleCreation() {
		Object model = getModel();
		boolean isMove = false;
		if (needProcessDataItem(model)) {
			String prompt = ReportPlugin.getDefault().getPreferenceStore()
					.getString(ReportPlugin.LIBRARY_MOVE_BINDINGS_PREFERENCE);

			MessageDialogWithToggle dialog;

			if (MessageDialogWithToggle.ALWAYS.equals(prompt)) {
				moveBindToHost((DataItemHandle) model);
				isMove = true;
			} else if ((prompt == null || MessageDialogWithToggle.PROMPT.equals(prompt))
					&& ((dialog = MessageDialogWithToggle.openYesNoQuestion(UIUtil.getDefaultShell(),
							Messages.getString("LibraryElementsToolHandleExtends_question"), //$NON-NLS-1$
							Messages.getString("LibraryElementsToolHandleExtends_message"), //$NON-NLS-1$
							Messages.getString("LibraryElementsToolHandleExtends_toggle"), //$NON-NLS-1$
							false, ReportPlugin.getDefault().getPreferenceStore(),
							ReportPlugin.LIBRARY_MOVE_BINDINGS_PREFERENCE)) != null)) {
				if (dialog.getReturnCode() == IDialogConstants.YES_ID) {
					moveBindToHost((DataItemHandle) model);
					isMove = true;
				}

				if (dialog.getToggleState()) {
					if (dialog.getReturnCode() == IDialogConstants.YES_ID) {
						ReportPlugin.getDefault().getPreferenceStore().setValue(
								ReportPlugin.LIBRARY_MOVE_BINDINGS_PREFERENCE, MessageDialogWithToggle.ALWAYS);
					} else if (dialog.getReturnCode() == IDialogConstants.NO_ID) {
						ReportPlugin.getDefault().getPreferenceStore()
								.setValue(ReportPlugin.LIBRARY_MOVE_BINDINGS_PREFERENCE, MessageDialogWithToggle.NEVER);
					}
				}
			}
		}
		if (!isMove && model instanceof DataItemHandle) {
			DataItemHandle dataHandle = (DataItemHandle) model;
			Iterator iter = dataHandle.columnBindingsIterator();
			String resultColumnName = dataHandle.getResultSetColumn();
			ComputedColumnHandle activeBinding = null;
			while (iter.hasNext()) {
				ComputedColumnHandle computedColumnHandle = (ComputedColumnHandle) iter.next();
				if (computedColumnHandle.getName().equals(resultColumnName)) {
					Expression newExpression = (Expression) computedColumnHandle
							.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER).getValue();
					if (newExpression == null || newExpression.getExpression() == null) {
						activeBinding = computedColumnHandle;
					} else if (newExpression.getExpression() instanceof String
							&& ((String) newExpression.getExpression()).length() == 0) {
						activeBinding = computedColumnHandle;
					}
					break;
				}
			}
			if (activeBinding != null) {
				DataColumnBindingDialog dialog = new DataColumnBindingDialog(false);
				dialog.setNeedPrompt(false);
				dialog.setInput(dataHandle, activeBinding);

				if (dialog.open() == Dialog.OK) {
					// do nothing now
				}
			}
		}
		return super.postHandleCreation();
	}

	private boolean needProcessDataItem(Object handle) {
		if (handle instanceof DataItemHandle) {
			DataItemHandle dataHandle = (DataItemHandle) handle;
			if (dataHandle.getExtends() != null && dataHandle.getExtends().getContainer() instanceof LibraryHandle) {
				if ((dataHandle.getDataSet() != null) || DEUtil.getBindingHolder(dataHandle, true) == null
						|| DEUtil.getBindingHolder(dataHandle, true) == dataHandle) {
					return false;
				}
				Iterator iter = dataHandle.columnBindingsIterator();
				if (!iter.hasNext()) {
					return false;
				}
				return true;
			}

		}
		return false;
	}

	private void moveBindToHost(DataItemHandle dataHandle) {
		ReportItemHandle hostHnadle = DEUtil.getBindingHolder(dataHandle, true);
		Iterator iter = dataHandle.columnBindingsIterator();
		String resultColumnName = dataHandle.getResultSetColumn();
		List<String> list = new ArrayList<>();
		ComputedColumnHandle activeBinding = null;
		while (iter.hasNext()) {
			ComputedColumnHandle computedColumnHandle = (ComputedColumnHandle) iter.next();
			String name = computedColumnHandle.getName();
			boolean isDataBinding = false;
			if (name.equals(resultColumnName)) {
				isDataBinding = true;
			}

			ComputedColumn bindingColumn = (ComputedColumn) computedColumnHandle.getStructure().copy();

			try {
				ComputedColumnHandle newComputedColumnHandle = ColumnBindingUtil.addColumnBinding(hostHnadle,
						bindingColumn);
				if (isDataBinding && !newComputedColumnHandle.getName().equals(name)) {
					dataHandle.setResultSetColumn(newComputedColumnHandle.getName());
				}
				if (isDataBinding) {
					Expression newExpression = (Expression) newComputedColumnHandle
							.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER).getValue();
					if (newExpression == null || newExpression.getExpression() == null) {
						activeBinding = newComputedColumnHandle;
					} else if (newExpression.getExpression() instanceof String
							&& ((String) newExpression.getExpression()).length() == 0) {
						activeBinding = newComputedColumnHandle;
					}
				}
			} catch (SemanticException e) {
				// do nothing nowDataColumnBindingDialog
			}
			list.add(computedColumnHandle.getName());
		}

		try {
			dataHandle.removedColumnBindings(list);
			dataHandle.setProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP, new ArrayList());
		} catch (SemanticException e) {
			// do nothing now
		}
		if (activeBinding != null) {
			DataColumnBindingDialog dialog = new DataColumnBindingDialog(false);
			dialog.setNeedPrompt(false);
			dialog.setInput(hostHnadle, activeBinding);

			if (dialog.open() == Dialog.OK) {
				// do nothing now
			}
		}
	}

}
