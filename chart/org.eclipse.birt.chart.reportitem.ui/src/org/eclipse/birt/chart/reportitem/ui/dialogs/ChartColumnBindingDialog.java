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

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.ui.ChartXTabUIUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * Data binding dialog for Charts
 */

public class ChartColumnBindingDialog extends ColumnBindingDialog {

	private ChartWizardContext context;
	private Button btnRefresh;

	/** The field indicates if all bindings are read-only in chart. */
	private boolean fIsReadOnly;

	public ChartColumnBindingDialog(ReportItemHandle input, Shell parent, ChartWizardContext context) {
		super(input, parent, false, true);
		this.context = context;
	}

	@Override
	protected void handleAddEvent() {
		DataColumnBindingDialog dialog = new DataColumnBindingDialog(true, true);
		dialog.setInput(inputElement);
		dialog.setExpressionProvider(expressionProvider);
		if (dialog.open() == Dialog.OK) {
			if (bindingTable != null) {
				refreshBindingTable();
				bindingTable.getTable().setSelection(bindingTable.getTable().getItemCount() - 1);
			}
		}

	}

	@Override
	protected void handleEditEvent() {
		ComputedColumnHandle bindingHandle = null;
		int pos = getColumnBindingIndexFromTableSelection();
		if (pos > -1) {
			bindingHandle = (ComputedColumnHandle) (DEUtil.getBindingHolder(inputElement)).getColumnBindings()
					.getAt(pos);
		}
		if (bindingHandle == null) {
			return;
		}

		DataColumnBindingDialog dialog = new DataColumnBindingDialog(false);
		dialog.setInput(inputElement, bindingHandle, context);
		dialog.setExpressionProvider(expressionProvider);
		if (dialog.open() == Dialog.OK) {
			if (bindingTable != null) {
				bindingTable.getTable().setSelection(pos);
			}
		}
	}

	@Override
	protected void handleDelEvent() {
		if (!btnDel.isEnabled()) {
			return;
		}
		int pos = getColumnBindingIndexFromTableSelection();
		if (pos > -1) {
			try {
				ComputedColumnHandle handle = (ComputedColumnHandle) (DEUtil.getBindingHolder(inputElement))
						.getColumnBindings().getAt(pos);
				deleteRow(handle);
			} catch (Exception e1) {
				ExceptionHandler.handle(e1);
			}
		}
	}

	/**
	 * Disable/enable button to make all items in the dialog read-only.
	 *
	 * @since 2.3
	 */
	private void updateButtonStatusForReadOnly() {
		if (fIsReadOnly) {
			btnAdd.setEnabled(false);
			btnEdit.setEnabled(false);
			btnDel.setEnabled(false);
			getAggregationButton().setEnabled(false);
			// btnRefresh.setEnabled( false );
		}
	}

	@Override
	protected int addButtons(Composite cmp, final Table table) {
		Listener[] listeners = getAggregationButton().getListeners(SWT.Selection);
		if (listeners.length > 0) {
			getAggregationButton().removeListener(SWT.Selection, listeners[0]);
		}
		getAggregationButton().addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				DataColumnBindingDialog dialog = new DataColumnBindingDialog(true);
				dialog.setInput(inputElement, null, context);
				dialog.setExpressionProvider(expressionProvider);
				dialog.setAggreate(true);
				if (dialog.open() == Dialog.OK) {
					if (bindingTable != null) {
						refreshBindingTable();
						bindingTable.getTable().setSelection(bindingTable.getTable().getItemCount() - 1);
					}
				}

				refreshBindingTable();
				if (table.getItemCount() > 0) {
					setSelectionInTable(table.getItemCount() - 1);
				}
				updateButtons();
			}

		});

		btnRefresh = new Button(cmp, SWT.PUSH);
		btnRefresh.setText(Messages.getString("ChartColumnBindingDialog.Button.Refresh")); //$NON-NLS-1$

		GridData data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		data.widthHint = Math.max(60, btnRefresh.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnRefresh.setLayoutData(data);
		btnRefresh.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				try {
					List<ComputedColumn> columnList = new ArrayList<>();

					CubeHandle cubeHandle = ChartReportItemHelper.instance().getBindingCubeHandle(inputElement);
					if (cubeHandle != null) {
						if (inputElement.getCube() == null || ChartItemUtil.isInMultiViews(inputElement)) {
							// It inherits bindings from crosstab or sharing
							// query with crosstab, only need to refresh
							// bindings.
							refreshBindingTable();
						} else {
							// It uses cube set, needs to added available new
							// dimension or measure to current report item as
							// bindings.

							// since chart's cube binding is not editable, so
							// there's no newly added column binding on chart
							// item, we could clear the bindings first
							// this can help remove the bindings which not exist
							inputElement.getColumnBindings().clearValue();

							columnList = ChartXTabUIUtil.generateComputedColumns((ExtendedItemHandle) inputElement,
									cubeHandle);

							if (columnList.size() > 0) {
								for (Iterator<ComputedColumn> iter = columnList.iterator(); iter.hasNext();) {
									DEUtil.addColumn(inputElement, iter.next(), false);
								}
							}
						}
					} else {

						DataSetHandle dataSetHandle = inputElement.getDataSet();

						if (dataSetHandle == null || ChartItemUtil.isInMultiViews(inputElement)) {
							// It inherits bindings from table or sharing query
							// with table, only need to refresh bindings.
							refreshBindingTable();
						} else {
							// It uses data set, needs to added available new
							// computed columns of data set to current report
							// item as new bindings.
							List resultSetColumnList = DataUtil.getColumnList(dataSetHandle);
							for (Iterator iterator = resultSetColumnList.iterator(); iterator.hasNext();) {
								ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) iterator.next();
								ComputedColumn column = StructureFactory.newComputedColumn(inputElement,
										resultSetColumn.getColumnName());
								column.setDataType(resultSetColumn.getDataType());
								column.setExpression(DEUtil.getExpression(resultSetColumn));
								columnList.add(column);
							}

							if (columnList.size() > 0) {
								for (Iterator<ComputedColumn> iter = columnList.iterator(); iter.hasNext();) {
									DEUtil.addColumn(inputElement, iter.next(), false);
								}
							}
						}
					}
					bindingTable.setInput(inputElement);
				} catch (SemanticException e) {
					WizardBase.displayException(e);
				}
			}
		});

		// Return the number of buttons
		return 2;
	}

	@Override
	protected void updateButtons() {
		super.updateButtons();
		getAggregationButton().setEnabled(btnAdd.isEnabled());
		if (!isOwnColumnBinding(bindingTable.getTable().getSelectionIndex())) {
			btnDel.setEnabled(false);
			btnEdit.setEnabled(false);
		}
		updateButtonStatusForReadOnly();
	}

	private boolean isOwnColumnBinding(int pos) {
		List<ComputedColumnHandle> bindings = getBindingList(inputElement);

		return pos < 0 ? false : bindings.get(pos).getElementHandle() == inputElement;
	}

	private int getColumnBindingIndexFromTableSelection() {
		int selection = bindingTable.getTable().getSelectionIndex();
		int index = -1;
		for (int i = 0; i <= selection; i++) {
			if (isOwnColumnBinding(i)) {
				index++;
			}
		}
		return index;
	}

	@Override
	protected void addBinding(ComputedColumn column) {
		try {
			DEUtil.addColumn(inputElement, column, true);
			ChartWizard.removeException(ChartWizard.ChartColBinDia_ID);
		} catch (SemanticException e) {
			ChartWizard.showException(ChartWizard.ChartColBinDia_ID, e.getLocalizedMessage());
		}
	}

	@Override
	protected List<ComputedColumnHandle> getBindingList(DesignElementHandle inputElement) {
		Iterator<ComputedColumnHandle> iterator = ChartItemUtil.getColumnDataBindings((ReportItemHandle) inputElement);
		List<ComputedColumnHandle> list = new ArrayList<>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	/**
	 * Set read-only flag.
	 *
	 * @param isReadOnly
	 * @since 2.3
	 */
	public void setReadOnly(boolean isReadOnly) {
		fIsReadOnly = isReadOnly;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog#
	 * setDialogInput
	 * (org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog
	 * , org.eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	@Override
	protected void setDialogInput(DataColumnBindingDialog dialog, ComputedColumnHandle bindingHandle) {
		if (dialog != null) {
			dialog.setInput(inputElement, bindingHandle, context);
		}
	}
}
