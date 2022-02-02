/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
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
public class FilterHandleProvider implements IFormHandleProvider {

	/**
	 * The current selections in outline or Editor.
	 */
	protected List input;

	/**
	 * Column properties.
	 */
	protected String[] columnKeys = new String[] { FilterCondition.EXPR_MEMBER, FilterCondition.OPERATOR_MEMBER,
			FilterCondition.VALUE1_MEMBER, FilterCondition.VALUE2_MEMBER };

	/**
	 * Column widths.
	 */
	private static int[] columnWidth = new int[] { 200, 150, 200, 200 };

	/**
	 * Model processor, provide data process of Filter model.
	 */
	protected FilterModelProvider modelAdapter = new FilterModelProvider();

	/**
	 * The display name of columns.
	 */
	private String[] columnNames;

	/**
	 * Column editors for the Filter form.
	 */
	private CellEditor[] editors;

	private List columnList = new ArrayList();

	protected ParamBindingHandle[] bindingParams = null;

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

	/**
	 * 
	 */
	public void setBindingParams(ParamBindingHandle[] params) {
		this.bindingParams = params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
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
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getTitle()
	 */
	public String getTitle() {
		return Messages.getString("FilterHandleProvider.Label.Filterby"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
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
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doMoveItem(int, int)
	 */
	public boolean doMoveItem(int oldPos, int newPos) throws PropertyValueException {
		return modelAdapter.moveItem(input.get(0), oldPos, newPos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doDeleteItem(int)
	 */
	public boolean doDeleteItem(int pos) throws PropertyValueException {
		return modelAdapter.deleteItem(input.get(0), pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doAddItem(int)
	 */
	public boolean doAddItem(int pos) throws SemanticException {
		// return modelAdapter.doAddItem( input.get( 0 ), pos );
		Object item = input.get(0);
		if (item instanceof DesignElementHandle) {
			FilterConditionBuilder dialog = new FilterConditionBuilder(UIUtil.getDefaultShell(),
					FilterConditionBuilder.DLG_TITLE_NEW, FilterConditionBuilder.DLG_MESSAGE_NEW);
			dialog.setUsedForEditGroup(isEditGroup());
			dialog.setDesignHandle((DesignElementHandle) item);
			dialog.setInput(null);
			dialog.setBindingParams(bindingParams);
			if (item instanceof ReportItemHandle) {
				dialog.setReportElement((ReportItemHandle) item);
			} else if (item instanceof GroupHandle) {
				if (((GroupHandle) item).getContainer() instanceof ReportItemHandle) {
					dialog.setReportElement((ReportItemHandle) ((GroupHandle) item).getContainer());
				}
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
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doEditItem(int)
	 */
	public boolean doEditItem(int pos) {

		Object item = input.get(0);
		if (item instanceof DesignElementHandle) {
			DesignElementHandle element = (DesignElementHandle) item;
			PropertyHandle propertyHandle = element.getPropertyHandle(ListingHandle.FILTER_PROP);
			FilterConditionHandle filterHandle = (FilterConditionHandle) (propertyHandle.getAt(pos));
			if (filterHandle == null) {
				return false;
			}

			FilterConditionBuilder dialog = new FilterConditionBuilder(UIUtil.getDefaultShell(),
					FilterConditionBuilder.DLG_TITLE_EDIT, FilterConditionBuilder.DLG_MESSAGE_EDIT);
			dialog.setUsedForEditGroup(isEditGroup());
			dialog.setDesignHandle((DesignElementHandle) item);
			dialog.setInput(filterHandle);
			dialog.setBindingParams(bindingParams);
			if (item instanceof ReportItemHandle) {
				dialog.setReportElement((ReportItemHandle) item);
			} else if (item instanceof GroupHandle) {
				if (((GroupHandle) item).getContainer() instanceof ReportItemHandle) {
					dialog.setReportElement((ReportItemHandle) ((GroupHandle) item).getContainer());
				}
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
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		String key = columnKeys[columnIndex];
		return modelAdapter.getText(element, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getImagePath(java.lang.Object, int)
	 */
	public String getImagePath(Object element, int columnIndex) {
		return null;
	}

	public Image getImage(Object element, int columnIndex) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			input = (List) inputElement;
		} else {
			input = new ArrayList();
			input.add(inputElement);
		}
		getDataSetColumns(DEUtil.getInputFirstElement(input));
		Object[] elements = modelAdapter.getElements(input);
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
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
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#modify(java.lang.Object, java.lang.String,
	 * java.lang.Object)
	 */
	public boolean modify(Object data, String property, Object value) throws NameException, SemanticException {
		// Clear the value of value2 when operator is not "between" or "not
		// between"
		// if ( property.equals( "Operator" ) )
		if (property.equals(modelAdapter.getColumnNames(columnKeys)[1])) {
			if (!(value.equals(
					getDisplayName(FilterCondition.OPERATOR_MEMBER, DesignChoiceConstants.FILTER_OPERATOR_BETWEEN))
					|| value.equals(getDisplayName(FilterCondition.OPERATOR_MEMBER,
							DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN)))) {
				modelAdapter.setStringValue(input.get(0), data, FilterCondition.VALUE2_MEMBER, ""); //$NON-NLS-1$
			}
		}

		int index = Arrays.asList(columnNames).indexOf(property);
		String key = columnKeys[index];

		String strValue = ""; //$NON-NLS-1$
		if (value instanceof Integer) {
			int intValue = ((Integer) value).intValue();
			if (intValue == -1) {
				CCombo combo = (CCombo) editors[index].getControl();
				strValue = combo.getText();
			} else {
				String[] choices = modelAdapter.getChoiceSet(input.get(0), columnKeys[index]);
				strValue = choices[intValue];
			}
		} else {
			strValue = (String) value;
		}

		return modelAdapter.setStringValue(input.get(0), data, key, strValue);
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
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getColumnWidths()
	 */
	public int[] getColumnWidths() {
		return columnWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider
	 * #needRefreshed(org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public boolean needRefreshed(NotificationEvent event) {
		if (event instanceof PropertyEvent) {
			String propertyName = ((PropertyEvent) event).getPropertyName();
			if (ReportItemHandle.BOUND_DATA_COLUMNS_PROP.equals(propertyName)) {
				getDataSetColumns(input.get(0));
			}
			return true;
		}
		return false;
	}

	private Object getDisplayName(final String key, final String value) {
		IChoiceSet choiceSet = ChoiceSetFactory.getStructChoiceSet(FilterCondition.FILTER_COND_STRUCT, key);
		IChoice choice = choiceSet.findChoice(value);
		if (choice != null) {
			return choice.getDisplayName();
		}

		return null;
	}

	public boolean isEditable() {
		return true;
	}

	// used for edit group,sub class can overwrite this method
	//
	public boolean isEditGroup() {
		return false;
	}
}
