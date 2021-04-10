/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

/**
 * This dialog takes an expression and a data set and shows a list of unique
 * values for selection from the data set. It allows both multiple and single
 * selection. The default is single selection.
 */
public class SelectValueDialog extends BaseDialog {

	private boolean multipleSelection = false;
	private TableViewer tableViewer = null;
	private Table table = null;
	private int sortDir = SWT.UP;
	private int[] selectedIndices = null;
	private Object[] selectedItems = null;
	private java.util.List<Object> modelValueList = new ArrayList<Object>();
	private ParamBindingHandle[] bindingParams = null;
	private final String nullValueDispaly = Messages.getString("SelectValueDialog.SelectValue.NullValue"); //$NON-NLS-1$

	// private java.util.List<String> viewerValueList = new ArrayList<String>();

	/**
	 * @param parentShell
	 * @param title
	 */
	public SelectValueDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	/**
	 * @return Returns the paramBindingHandles.
	 */
	public ParamBindingHandle[] getBindingParams() {
		return bindingParams;
	}

	/**
	 * Set handles for binding parameters
	 */
	public void setBindingParams(ParamBindingHandle[] handles) {
		this.bindingParams = handles;
	}

	/**
	 * @param expression The expression to set.
	 */
	public void setSelectedValueList(Collection valueList) {
		modelValueList.clear();
		if (valueList != null) {
			Iterator iter = valueList.iterator();
			while (iter.hasNext()) {
				Object value = iter.next();
				if (value == null) {
					modelValueList.add(new NullValue());
				} else
					modelValueList.add(value);
			}
		}
	}

	/**
	 * @return Returns the multipleSelection.
	 */
	public boolean isMultipleSelection() {
		return multipleSelection;
	}

	/**
	 * @param multipleSelection The multipleSelection to set.
	 */
	public void setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		tableViewer = new TableViewer(composite,
				(isMultipleSelection() ? SWT.MULTI : SWT.SINGLE) | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 250;
		data.widthHint = 300;
		table = tableViewer.getTable();
		table.setLayoutData(data);
		table.setHeaderVisible(true);
		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText(Messages.getString("SelectValueDialog.selectValue")); //$NON-NLS-1$
		column.setWidth(data.widthHint);

		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, Messages.getString("SelectValueDialog.retrieving")); //$NON-NLS-1$

		column.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				sortDir = sortDir == SWT.UP ? SWT.DOWN : SWT.UP;
				table.setSortDirection(sortDir);
				tableViewer.setSorter(new TableSorter(sortDir));
				table.setSelection(table.getSelectionIndices());
			}
		});

		table.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				if (table.getSelectionCount() > 0) {
					okPressed();
				}
			}
		});

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			public void run() {
				populateList();
			}

		});

		UIUtil.bindHelp(parent, IHelpContextIds.SELECT_VALUE_DIALOG_ID);
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		selectedItems = new Object[table.getSelectionCount()];
		for (int i = 0; i < table.getSelectionCount(); i++) {
			selectedItems[i] = table.getSelection()[i].getData();
		}
		selectedIndices = table.getSelectionIndices();
		setResult(table.getSelection());
		super.okPressed();
	}

	/**
	 * Populates all available values
	 */
	private void populateList() {
		try {
			if (this.getShell() == null || this.getShell().isDisposed())
				return;
			if (this.getOkButton() != null && !this.getOkButton().isDisposed())
				getOkButton().setEnabled(false);

			table.removeAll();
			tableViewer.setContentProvider(new ContentProvider());
			tableViewer.setLabelProvider(new TableLabelProvider());

			if (modelValueList != null) {
				tableViewer.setInput(modelValueList);
			} else {
				ExceptionHandler.openErrorMessageBox(Messages.getString("SelectValueDialog.errorRetrievinglist"), //$NON-NLS-1$
						Messages.getString("SelectValueDialog.noExpressionSet")); //$NON-NLS-1$
			}
			if (table.getItemCount() > 0) {
				if (this.getOkButton() != null && !this.getOkButton().isDisposed())
					getOkButton().setEnabled(true);

				for (int i = 0; i < table.getItemCount(); i++) {
					table.getItem(i).setData(modelValueList.get(i));
				}

				table.setSortColumn(table.getColumn(0));
				table.setSortDirection(sortDir);
				tableViewer.setSorter(new TableSorter(sortDir));

				table.setSelection(0);
			}
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

	// /*
	// * Return the first selected value if selected result is not null
	// */
	// public String getSelectedValue( )
	// {
	// String[] result = (String[]) getResult( );
	// return ( result != null && result.length > 0 ) ? result[0] : null;
	// }
	//
	// /*
	// * Return all the selected value if selected result is not null
	// */
	// public String[] getSelectedValues( )
	// {
	// String[] result = (String[]) getResult( );
	// return ( result != null && result.length > 0 ) ? result : null;
	// }

	/**
	 * Return expression string value as expression required format. For example
	 * number type: Integer value 1 to String value "1" Boolean type: Boolean value
	 * true to String value "true" other types: String value "abc" to String value
	 * "\"abc\"" Date value "2000-10-10" to String value "\"2000-10-10\""
	 * 
	 * @deprecated
	 * @return expression value
	 */
	public String getSelectedExprValue() {
		String exprValue = null;
		if (selectedIndices != null && selectedIndices.length > 0) {
			Object modelValue = selectedItems[0];
			if (modelValue instanceof NullValue) {
				return "null"; //$NON-NLS-1$
			}

			if (modelValue instanceof Boolean || modelValue instanceof Integer || modelValue instanceof Double) {
				exprValue = getDataText(modelValue);
			} else if (modelValue instanceof BigDecimal) {
				exprValue = "new java.math.BigDecimal(\"" //$NON-NLS-1$
						+ getDataText(modelValue) + "\")"; //$NON-NLS-1$
			} else {
				exprValue = "\"" //$NON-NLS-1$
						+ JavascriptEvalUtil.transformToJsConstants(getDataText(modelValue)) + "\""; //$NON-NLS-1$
			}
		}
		return exprValue;
	}

	/**
	 * Return expression string value as expression required format. For example
	 * number type: Integer value 1 to String value "1" Boolean type: Boolean value
	 * true to String value "true" other types: String value "abc" to String value
	 * "\"abc\"" Date value "2000-10-10" to String value "\"2000-10-10\""
	 * 
	 * @deprecated
	 * @return expression value
	 */
	public String[] getSelectedExprValues() {
		String[] exprValues = null;
		if (selectedIndices != null && selectedIndices.length > 0) {
			exprValues = new String[selectedIndices.length];
			for (int i = 0; i < selectedIndices.length; i++) {
				Object modelValue = selectedItems[i];

				if (modelValue instanceof NullValue) {
					exprValues[i] = "null"; //$NON-NLS-1$
				} else {
					if (modelValue instanceof Boolean || modelValue instanceof Integer
							|| modelValue instanceof Double) {
						exprValues[i] = getDataText(modelValue);
					} else if (modelValue instanceof BigDecimal) {
						exprValues[i] = "new java.math.BigDecimal(\"" //$NON-NLS-1$
								+ getDataText(modelValue) + "\")"; //$NON-NLS-1$
					} else {
						exprValues[i] = "\"" //$NON-NLS-1$
								+ JavascriptEvalUtil.transformToJsConstants(getDataText(modelValue)) + "\""; //$NON-NLS-1$
					}
				}
			}
		}
		return exprValues;
	}

	/**
	 * Return expression string value as expression required format. For example
	 * number type: Integer value 1 to String value "1" Boolean type: Boolean value
	 * true to String value "true" other types: String value "abc" to String value
	 * "\"abc\"" Date value "2000-10-10" to String value "\"2000-10-10\""
	 * 
	 * @return expression value
	 */
	public String getSelectedExprValue(IExpressionConverter convert) {
		String exprValue = null;
		if (selectedIndices != null && selectedIndices.length > 0) {
			Object modelValue = selectedItems[0];
			String dataType = null;
			if (!(modelValue instanceof NullValue)) {
				dataType = DataSetUIUtil.toModelDataType(DataTypeUtil.toApiDataType(modelValue.getClass()));
				String viewerValue = getDataText(modelValue);
				if (convert != null)
					exprValue = convert.getConstantExpression(viewerValue, dataType);
			}
		}
		return exprValue;
	}

	/**
	 * Return expression string value as expression required format. For example
	 * number type: Integer value 1 to String value "1" Boolean type: Boolean value
	 * true to String value "true" other types: String value "abc" to String value
	 * "\"abc\"" Date value "2000-10-10" to String value "\"2000-10-10\""
	 * 
	 * @return expression value
	 */
	public String[] getSelectedExprValues(IExpressionConverter convert) {
		String[] exprValues = null;
		if (selectedIndices != null && selectedIndices.length > 0) {
			exprValues = new String[selectedIndices.length];
			for (int i = 0; i < selectedIndices.length; i++) {
				Object modelValue = selectedItems[i];
				String dataType = null;
				if (!(modelValue instanceof NullValue)) {
					dataType = DataSetUIUtil.toModelDataType(DataTypeUtil.toApiDataType(modelValue.getClass()));
					String viewerValue = getDataText(modelValue);
					if (convert != null)
						exprValues[i] = convert.getConstantExpression(viewerValue, dataType);
				}

			}
		}
		return exprValues;
	}

	public class TableSorter extends ViewerSorter {

		private int sortDir;

		private TableSorter(int sortDir) {
			this.sortDir = sortDir;
		}

		public int compare(Viewer viewer, Object e1, Object e2) {
			if (sortDir == SWT.UP) {
				if (e1 instanceof NullValue) {
					return -1;
				}
				if (e2 instanceof NullValue) {
					return 1;
				}
				if (e1 instanceof Integer) {
					return ((Integer) e1).compareTo((Integer) e2);
				} else if (e1 instanceof Double) {
					return ((Double) e1).compareTo((Double) e2);
				} else if (e1 instanceof BigDecimal) {
					return ((BigDecimal) e1).compareTo((BigDecimal) e2);
				} else {
					return getDataText(e1).compareTo(getDataText(e2));
				}
			} else if (sortDir == SWT.DOWN) {
				if (e1 instanceof NullValue) {
					return 1;
				}
				if (e2 instanceof NullValue) {
					return -1;
				}
				if (e2 instanceof Double) {
					return ((Double) e2).compareTo((Double) e1);
				} else if (e2 instanceof BigDecimal) {
					return ((BigDecimal) e2).compareTo((BigDecimal) e1);
				} else {
					return getDataText(e2).compareTo(getDataText(e1));
				}
			}
			return 0;
		}
	}

	public static class ContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	public class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 0) {
				if (!(element instanceof NullValue))
					return getDataText(element);
				else
					return nullValueDispaly;
			}
			return null;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	private String getDataText(Object element) {
		if (element != null) {
			try {
				return DataTypeUtil.toLocaleNeutralString(element);
			} catch (BirtException e) {
				ExceptionHandler.handle(e);
			}
		}
		return null;
	}

	class NullValue {
	}
}