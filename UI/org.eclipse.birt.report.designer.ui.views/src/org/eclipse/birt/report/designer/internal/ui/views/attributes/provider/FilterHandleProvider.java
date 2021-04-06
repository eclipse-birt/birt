/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.FilterModelProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 * Filter FormHandleProvider, provides Filter sensitive data and processes.
 */
public class FilterHandleProvider extends AbstractFilterHandleProvider {

	/**
	 * Column properties.
	 */
	private String[] columnKeys = new String[] { FilterCondition.EXPR_MEMBER, FilterCondition.OPERATOR_MEMBER,
			FilterCondition.VALUE1_MEMBER, FilterCondition.VALUE2_MEMBER };

	/**
	 * Column widths.
	 */
	private static int[] columnWidth = new int[] { 200, 150, 200, 200 };

	/**
	 * The display name of columns.
	 */
	private String[] columnNames;

	/**
	 * Column editors for the Filter form.
	 */
	private CellEditor[] editors;

	private List columnList = new ArrayList();

	public FilterHandleProvider() {
		modelAdapter = new FilterModelProvider();
	}

	/**
	 * Gets all dataSet columns
	 * 
	 * @param obj DesignElementHandle object.
	 */
	private void getDataSetColumns(Object obj) {
		if (obj instanceof DesignElementHandle) {
			columnList = new ArrayList();
			String[] columns = modelAdapter.getChoiceSet(obj, FilterCondition.EXPR_MEMBER);
			if (columns != null)
				columnList.addAll(Arrays.asList(columns));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getColumnNames()
	 */
	public String[] getColumnNames() {
		if (columnNames == null) {
			columnNames = modelAdapter.getColumnNames(columnKeys);
		}
		return columnNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getTitle()
	 */
	public String getDisplayName() {
		return Messages.getString("FilterHandleProvider.Label.Filterby"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getEditors(org.eclipse.swt.widgets.Table)
	 */
	public CellEditor[] getEditors(final Table table) {
		if (editors == null) {
			editors = new CellEditor[columnKeys.length];
			editors[0] = new TextCellEditor(table);
			editors[1] = new TextCellEditor(table);
			editors[2] = new TextCellEditor(table);
			editors[3] = new TextCellEditor(table);
		}

		return editors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doMoveItem(int, int)
	 */
	public boolean doMoveItem(int oldPos, int newPos) throws PropertyValueException {
		return modelAdapter.moveItem(contentInput.get(0), oldPos, newPos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doDeleteItem(int)
	 */
	public boolean doDeleteItem(int pos) throws PropertyValueException {
		return modelAdapter.deleteItem(contentInput.get(0), pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doAddItem(int)
	 */
	public boolean doAddItem(int pos) throws SemanticException {
		// return modelAdapter.doAddItem( input.get( 0 ), pos );
		Object item = contentInput.get(0);
		if (item instanceof DesignElementHandle) {
			FilterConditionBuilder dialog = new FilterConditionBuilder(UIUtil.getDefaultShell(),
					FilterConditionBuilder.DLG_TITLE_NEW, FilterConditionBuilder.DLG_MESSAGE_NEW);
			dialog.setDesignHandle((DesignElementHandle) item);
			dialog.setInput(null);
			dialog.setBindingParams(bindingParams);
			if (item instanceof ReportItemHandle) {
				dialog.setReportElement((ReportItemHandle) item);
			} else if (item instanceof GroupHandle) {
				dialog.setReportElement((ReportItemHandle) ((GroupHandle) item).getContainer());
			} else if (item instanceof DataGroupHandle) {
				if (((DataGroupHandle) item).getContainer() instanceof ReportItemHandle) {
					dialog.setReportElement((ReportItemHandle) ((DataGroupHandle) item).getContainer());
				}
			}
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doEditItem(int)
	 */
	public boolean doEditItem(int pos) {

		Object item = contentInput.get(0);
		if (item instanceof DesignElementHandle) {
			DesignElementHandle element = (DesignElementHandle) item;
			PropertyHandle propertyHandle = element.getPropertyHandle(ListingHandle.FILTER_PROP);
			FilterConditionHandle filterHandle = (FilterConditionHandle) (propertyHandle.getAt(pos));
			if (filterHandle == null) {
				return false;
			}

			FilterConditionBuilder dialog = new FilterConditionBuilder(UIUtil.getDefaultShell(),
					FilterConditionBuilder.DLG_TITLE_EDIT, FilterConditionBuilder.DLG_MESSAGE_NEW);
			dialog.setDesignHandle((DesignElementHandle) item);
			dialog.setInput(filterHandle);
			dialog.setBindingParams(bindingParams);
			if (item instanceof ReportItemHandle) {
				dialog.setReportElement((ReportItemHandle) item);
			} else if (item instanceof GroupHandle) {
				dialog.setReportElement((ReportItemHandle) ((GroupHandle) item).getContainer());
			} else if (item instanceof DataGroupHandle) {
				if (((DataGroupHandle) item).getContainer() instanceof ReportItemHandle) {
					dialog.setReportElement((ReportItemHandle) ((DataGroupHandle) item).getContainer());
				}
			}
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		String key = columnKeys[columnIndex];
		return modelAdapter.getText(element, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getImagePath(java.lang.Object, int)
	 */
	public Image getImage(Object element, int columnIndex) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			contentInput = (List) inputElement;
		} else {
			contentInput = new ArrayList();
			contentInput.add(inputElement);
		}
		getDataSetColumns(contentInput.get(0));
		Object[] elements = modelAdapter.getElements(contentInput);
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {
		int index = Arrays.asList(columnNames).indexOf(property);
		String columnText = getColumnText(element, index);

		return columnText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#modify(java.lang.Object, java.lang.String,
	 * java.lang.Object)
	 */
	public boolean modify(Object data, String property, Object value) throws NameException, SemanticException {
		// Clear the value of value2 when operator is not "between" or "not
		// between"
		// if ( property.equals( "Operator" ) )
		// if ( property.equals( modelAdapter.getColumnNames( columnKeys )[1] )
		// )
		// {
		// if ( !( value.equals( getDisplayName(
		// FilterCondition.OPERATOR_MEMBER,
		// DesignChoiceConstants.FILTER_OPERATOR_BETWEEN ) ) || value.equals(
		// getDisplayName( FilterCondition.OPERATOR_MEMBER,
		// DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN ) ) ) )
		// {
		// modelAdapter.setStringValue( input.get( 0 ),
		// data,
		// FilterCondition.VALUE2_MEMBER,
		// "" ); //$NON-NLS-1$
		// }
		// }

		int index = Arrays.asList(columnNames).indexOf(property);
		String key = columnKeys[index];

		String strValue = ""; //$NON-NLS-1$
		if (value instanceof Integer) {
			int intValue = ((Integer) value).intValue();
			if (intValue == -1) {
				CCombo combo = (CCombo) editors[index].getControl();
				strValue = combo.getText();
			} else {
				String[] choices = modelAdapter.getChoiceSet(contentInput.get(0), columnKeys[index]);
				strValue = choices[intValue];
			}
		} else {
			strValue = (String) value;
		}

		return modelAdapter.setStringValue(contentInput.get(0), data, key, strValue);
	}

	// private void updateValueCellEditor( String exp )
	// {
	// String bindingName = null;
	// for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
	// {
	// String columnName = (String) iter.next( );
	// if ( DEUtil.getColumnExpression( columnName ).equals( exp ) )
	// {
	// bindingName = columnName;
	// break;
	// }
	// }
	// ( (ExpressionValueCellEditor) ( editors[2] ) ).setBindingName(
	// bindingName );
	// ( (ExpressionValueCellEditor) ( editors[3] ) ).setBindingName(
	// bindingName );
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getColumnWidths()
	 */
	public int[] getColumnWidths() {
		return columnWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#needRefreshed(org.eclipse.birt.model.activity.
	 * NotificationEvent)
	 */
	public boolean needRefreshed(NotificationEvent event) {
		if (event instanceof PropertyEvent) {
			String propertyName = ((PropertyEvent) event).getPropertyName();
			if (ReportItemHandle.BOUND_DATA_COLUMNS_PROP.equals(propertyName)) {
				getDataSetColumns(contentInput.get(0));
			}
			return true;
		}
		return false;
	}

	public void updateBindingParameters() {
		ParamBindingHandle[] bindingParams = null;

		if (DEUtil.getInputFirstElement(contentInput) instanceof ReportItemHandle) {
			ReportItemHandle inputHandle = (ReportItemHandle) DEUtil.getInputFirstElement(contentInput);
			List list = new ArrayList();
			for (Iterator iterator = inputHandle.paramBindingsIterator(); iterator.hasNext();) {
				ParamBindingHandle handle = (ParamBindingHandle) iterator.next();
				list.add(handle);
			}
			bindingParams = new ParamBindingHandle[list.size()];
			list.toArray(bindingParams);
		}
		setBindingParams(bindingParams);
	}

	public boolean isEditable() {
		if (((ReportItemHandle) DEUtil.getInputFirstElement(super.input))
				.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF)
			return false;
		else
			return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * AbstractFilterHandleProvider#getConcreteFilterProvider()
	 */
	public IFormProvider getConcreteFilterProvider() {
		return this;
	}
}