/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.views.attributes.IPropertyDescriptor;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Dialog to edit report item parameters.
 * 
 */
public class ReportItemParametersDialog extends BaseDialog {

	/**
	 * The list kept Property & PropertyDescriptor pair.
	 */
	private HashMap propertiesMap = new HashMap(7);

	/**
	 * The Binding properties table.
	 */
	private Table table;

	private ArrayList resultList;
	private ArrayList bindingParametersList;

	/**
	 * The static String value serves as key to <code>SetData(key,value)</code>
	 * method.
	 */
	private static final String Binding = "binding"; //$NON-NLS-1$

	/**
	 * The TableViewer of the table widget.
	 */
	private TableViewer tableViewer;

	/**
	 * The column list.
	 */
	private static final String[] columnNames = { Messages.getString("ChartDataBindingPage.Lbl.Parameter"), //$NON-NLS-1$
			Messages.getString("ChartDataBindingPage.Lbl.DataType"), //$NON-NLS-1$
			Messages.getString("ChartDataBindingPage.Lbl.Value") //$NON-NLS-1$
	};

	private ExpressionDialogCellEditor expressionCellEditor;

	private static IChoiceSet DataTypes = DesignEngine.getMetaDataDictionary()
			.getChoiceSet(DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE);

	private static final String DEFAULT_VALUE_LABEL = Messages.getString("ChartDataBindingPage.Lbl.DefaultValue"); //$NON-NLS-1$

	private ReportItemHandle reportItemHandle = null;

	public ReportItemParametersDialog(ReportItemHandle reportItemHandle) {
		super(Messages.getString("ChartDataBindingPage.Lbl.Parameter")); //$NON-NLS-1$
		this.reportItemHandle = reportItemHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.
	 * AbstractDescriptionPropertyPage#createContents(org.eclipse.swt.widgets.
	 * Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.DIALOG_DATA_SET_PARAMETER);

		Composite composite = (Composite) super.createDialogArea(parent);

		buildUI(composite);

		Object[] descriptors = propertiesMap.values().toArray();
		for (int i = 0; i < descriptors.length; i++) {
			IPropertyDescriptor descriptor = (IPropertyDescriptor) descriptors[i];
			ArrayList input = new ArrayList();
			input.add(reportItemHandle);
			descriptor.setInput(input);
		}

		refreshValues();

		return composite;
	}

	private void buildUI(Composite parent) {
		// sets the layout
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		parent.setLayout(layout);

		// create table and tableViewer
		table = new Table(parent, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 100;
		table.setLayoutData(gridData);
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(columnNames[i]);
			if (i == 1) {
				column.setWidth(80);
			} else {
				column.setWidth(160);
			}
		}

		table.addListener(SWT.KeyDown, new Listener() {

			public void handleEvent(Event event) {
				// Use space key to open expression builder to edit
				if (event.keyCode == ' ') {
					int selectionIndex = table.getSelectionIndex();
					if (selectionIndex < 0) {
						return;
					}
					TableItem item = table.getItem(selectionIndex);
					Object[] pair = (Object[]) item.getData();
					DataSetParameterHandle dataHandle = (DataSetParameterHandle) pair[0];
					ParamBindingHandle bindingHandle = (ParamBindingHandle) pair[1];
					String oldValue = bindingHandle == null ? null : bindingHandle.getExpression();
					if (oldValue == null) {
						oldValue = dataHandle.getDefaultValue();
					}
					Object value = expressionCellEditor.openDialogBox(table, oldValue);
					setValue(bindingHandle, value, item);
				}

			}
		});

		createTableViewer();

	}

	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	/**
	 * Creates the TableViewer and set all kinds of processors.
	 */
	private void createTableViewer() {
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(columnNames);
		expressionCellEditor = new ExpressionDialogCellEditor(table);
		tableViewer.setCellEditors(new CellEditor[] { null, null, expressionCellEditor });
		tableViewer.setContentProvider(new BindingContentProvider());
		tableViewer.setLabelProvider(new BindingLabelProvider());
		tableViewer.setCellModifier(new BindingCellModifier());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * AttributePage#refreshValues(java.util.Set)
	 */
	private void refreshValues() {
		reconstructTable();
		updateBindingData();
	}

	/**
	 * reconstruct the content of the table to show the last parameters in DataSet.
	 */
	private void reconstructTable() {
		// DataSetHandle dataHandle = (DataSetHandle)
		// reportItemHandle.getDataSet();
		tableViewer.setInput(reportItemHandle);
		// tableViewer.refresh( );
		// Get parent handle for parameter expression editing,
		// only parent dataset column binding can be as parameter expression.
		expressionCellEditor.setItemHandle(reportItemHandle.getContainer());
	}

	/**
	 * Sets text of Value column
	 */
	private void updateBindingData() {
		if (DEUtil.getDataSetList(reportItemHandle).size() == 0)
			return;
		Iterator iterator = reportItemHandle.paramBindingsIterator();
		while (iterator != null && iterator.hasNext()) {
			ParamBindingHandle handle = (ParamBindingHandle) iterator.next();
			String expression = handle.getExpression();
			int rowIndex = this.bindingParametersList.indexOf(handle);
			if (rowIndex != -1 && expression != null) {
				table.getItem(rowIndex).setText(columnNames.length - 1, expression);
				Item item = table.getItem(rowIndex);
				if (item.getData(Binding) == null)
					item.setData(Binding, handle);
			}
		}
	}

	/**
	 * Creates a new ParamBinding Handle.
	 * 
	 * @return ParamBinding Handle.
	 * @throws SemanticException
	 */
	private ParamBindingHandle createBindingHandle(String name) throws SemanticException {
		PropertyHandle propertyHandle = getPropertyHandle();
		ParamBinding binding = StructureFactory.createParamBinding();
		binding.setParamName(name);
		propertyHandle.addItem(binding);
		return (ParamBindingHandle) binding.getHandle(propertyHandle);
	}

	/**
	 * Gets the PropertyHandle of PARAM_BINDINGS_PROP property.
	 * 
	 * @return PropertyHandle
	 */
	private PropertyHandle getPropertyHandle() {
		return reportItemHandle.getPropertyHandle(ReportItemHandle.PARAM_BINDINGS_PROP);
	}

	private static class BindingLabelProvider extends LabelProvider implements ITableLabelProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.
		 * Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
		 * int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			String text = ""; //$NON-NLS-1$
			DataSetParameterHandle parameter = (DataSetParameterHandle) ((Object[]) element)[0];
			ParamBindingHandle bindingParameter = (ParamBindingHandle) ((Object[]) element)[1];
			switch (columnIndex) {
			case 0:
				if (parameter.getName() != null) {
					text = parameter.getName();
				}
				break;
			case 1:
				if (parameter.getDataType() != null) {
					text = ChoiceSetFactory.getDisplayNameFromChoiceSet(parameter.getDataType(), DataTypes);
				}
				break;
			case 2:
				if (bindingParameter != null && bindingParameter.getExpression() != null) {
					text = bindingParameter.getExpression();
				} else if (parameter.getDefaultValue() != null) {
					text = parameter.getDefaultValue() + " " //$NON-NLS-1$
							+ DEFAULT_VALUE_LABEL;
				}
				break;
			}
			return text;
		}
	}

	private class BindingCellModifier implements ICellModifier {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object,
		 * java.lang.String)
		 */
		public boolean canModify(Object element, String property) {
			return property.equals(columnNames[2]);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object,
		 * java.lang.String)
		 */
		public Object getValue(Object element, String property) {
			ParamBindingHandle bindingParameter = (ParamBindingHandle) ((Object[]) element)[1];
			if (bindingParameter != null) {
				return bindingParameter.getExpression();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
		 * java.lang.String, java.lang.Object)
		 */
		public void modify(Object element, String property, Object value) {
			Object model = element;
			if (element instanceof Item) {
				model = ((Item) element).getData();
			}
			int index = resultList.indexOf(model);

			// remove the ParamBindingHandle
			if (index != -1) {
				Object[] pair = (Object[]) model;
				// DataSetParameterHandle dataHandle =
				// (DataSetParameterHandle)pair[0];
				ParamBindingHandle bindingHandle = (ParamBindingHandle) pair[1];

				setValue(bindingHandle, value, (TableItem) element);
			}
		}
	}

	private class BindingContentProvider implements IStructuredContentProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.
		 * Object)
		 */
		public Object[] getElements(Object inputElement) {
			if (inputElement == null) {
				return new Object[0];
			}
			ReportItemHandle inputHandle = (ReportItemHandle) inputElement;
			DataSetHandle dataHandle = getDataSetFromHandle();
			if (dataHandle == null) {
				return new Object[0];
			}
			bindingParametersList = new ArrayList();
			List bindingParametersNameList = new ArrayList();
			resultList = new ArrayList();
			for (Iterator iterator = inputHandle.paramBindingsIterator(); iterator.hasNext();) {
				ParamBindingHandle handle = (ParamBindingHandle) iterator.next();
				bindingParametersList.add(handle);
				bindingParametersNameList.add(handle.getParamName());
			}
			for (Iterator iterator = dataHandle.parametersIterator(); iterator.hasNext();) {
				DataSetParameterHandle handle = (DataSetParameterHandle) iterator.next();
				Object[] result = new Object[] { handle, null };
				int index = bindingParametersNameList.indexOf(handle.getName());
				if (index != -1) {
					result[1] = bindingParametersList.get(index);
				}
				resultList.add(result);
			}
			return resultList.toArray();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.
		 * viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	/**
	 * Gets dataset from ReportItemHandle at first. If null, get dataset from its
	 * container.
	 * 
	 * @return direct dataset
	 */
	protected DataSetHandle getDataSetFromHandle() {
		if (reportItemHandle.getDataSet() != null) {
			return reportItemHandle.getDataSet();
		}
		List datasetList = DEUtil.getDataSetList(reportItemHandle.getContainer());
		if (datasetList.size() > 0) {
			return (DataSetHandle) datasetList.get(0);
		}
		return null;
	}

	private void setValue(ParamBindingHandle bindingHandle, Object value, TableItem item) {
		// if value is reset, remove the ParamBindingHandle
		if ((value == null || "".equals(value)) && bindingHandle != null) //$NON-NLS-1$
		{

			try {
				getPropertyHandle().removeItem(bindingHandle.getStructure());
			} catch (PropertyValueException e) {
				e.printStackTrace();
				return;
			}
		} else {
			if (bindingHandle == null) {
				try {
					bindingHandle = createBindingHandle(item.getText(0));
				} catch (SemanticException e) {
					e.printStackTrace();
					return;
				}
			}
			bindingHandle.setExpression((String) value);
		}

		reconstructTable();
	}
}
