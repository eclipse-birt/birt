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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 * Filter data processor
 * 
 */
public class FilterModelProvider {

	protected static Logger logger = Logger.getLogger(FilterModelProvider.class.getName());

	/**
	 * The property field indicates current filter type, the flag is different
	 * between table and crosstab.
	 * 
	 * @since 2.3
	 */
	protected String fFilterPropertyName;

	/**
	 * The list of allowed FilterCondition.OPERATOR_MEMBER
	 */
	protected static IChoiceSet choiceSet = ChoiceSetFactory.getStructChoiceSet(FilterCondition.FILTER_COND_STRUCT,
			FilterCondition.OPERATOR_MEMBER);

	private List columnList;
	/**
	 * Constant, represents empty String array.
	 */
	protected static final String[] EMPTY = new String[0];

	public FilterModelProvider() {
		fFilterPropertyName = TableHandle.FILTER_PROP;
	}

	/**
	 * Gets the display names of the given property keys.
	 * 
	 * @param keys Property keys
	 * @return String array contains display names
	 */
	public String[] getColumnNames(String[] keys) {
		assert keys != null;
		String[] columnNames = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			IStructureDefn structure = DEUtil.getMetaDataDictionary().getStructure(FilterCondition.FILTER_COND_STRUCT);
			columnNames[i] = structure.getMember(keys[i]).getDisplayName();
		}
		return columnNames;
	}

	/**
	 * Gets all elements of the given input.
	 * 
	 * @param input The input object.
	 * @return Filters array.
	 */
	public Object[] getElements(List input) {
		Object obj = input.get(0);
		if (!(obj instanceof DesignElementHandle))
			return EMPTY;
		DesignElementHandle element = (DesignElementHandle) obj;
		PropertyHandle propertyHandle = element.getPropertyHandle(fFilterPropertyName);
		Iterator iterator = propertyHandle.iterator();
		if (iterator == null)
			return EMPTY;
		List list = new ArrayList();
		while (iterator.hasNext())
			list.add(iterator.next());
		return list.toArray();
	}

	/**
	 * Gets property display name of a given element.
	 * 
	 * @param element Filter object
	 * @param key     Property key
	 * @return The text according the key
	 */
	public String getText(Object element, String key) {
		if (!(element instanceof StructureHandle)) {
			return "";//$NON-NLS-1$
		}

		String value = ((StructureHandle) element).getMember(key).getStringValue();
		if (value == null) {
			value = "";//$NON-NLS-1$
		}

		if (key.equals(FilterCondition.OPERATOR_MEMBER)) {
			IChoice choice = choiceSet.findChoice(value);
			if (choice != null) {
				return choice.getDisplayName();
			}
		} else {
			return value;
		}

		return "";//$NON-NLS-1$

	}

	/**
	 * Saves new property value to filter
	 * 
	 * @param element  Filter object
	 * @param key      Property key
	 * @param newValue new value
	 * @return The value according the key
	 */
	public boolean setStringValue(Object item, Object element, String key, String newValue)
			throws NameException, SemanticException {
		if (!key.equals(FilterCondition.OPERATOR_MEMBER)) {
			String value = DEUtil.getExpression(getResultSetColumn(newValue));
			if (value != null)
				newValue = value;
		}
		// if ( !( element instanceof StructureHandle ) )
		// {
		// FilterCondition filterCond = StructureFactory.createFilterCond( );
		// if ( key.equals( FilterCondition.EXPR_MEMBER ) )
		// {
		// filterCond.setExpr( newValue );
		// }
		// DesignElementHandle handle = (DesignElementHandle) item;
		// PropertyHandle propertyHandle = handle.getPropertyHandle(
		// ListingElement.FILTER_PROP );
		// propertyHandle.addItem( filterCond );
		// element = filterCond.getHandle( propertyHandle );
		// }

		String saveValue = newValue;
		StructureHandle handle = (StructureHandle) element;
		if (key.equals(FilterCondition.OPERATOR_MEMBER)) {
			IChoice choice = choiceSet.findChoiceByDisplayName(newValue);
			if (choice == null)
				saveValue = null;
			else
				saveValue = choice.getName();
		}
		handle.getMember(key).setStringValue(saveValue);
		return true;
	}

	/**
	 * Gets the choice set of one property
	 * 
	 * @param item ReportItem object
	 * @param key  Property key
	 * @return Choice set
	 */
	public String[] getChoiceSet(Object item, String key) {
		if (key.equals(FilterCondition.OPERATOR_MEMBER)) {
			choiceSet = ChoiceSetFactory.getStructChoiceSet(FilterCondition.FILTER_COND_STRUCT, key);
			return ChoiceSetFactory.getDisplayNamefromChoiceSet(choiceSet);
		}
		if (!(item instanceof DesignElementHandle)) {
			return EMPTY;
		}
		return getDataSetColumns((DesignElementHandle) item);
	}

	/**
	 * Gets all columns in a dataSet.
	 * 
	 * @param handle ReportItem object
	 * @return Columns array.
	 */
	private String[] getDataSetColumns(DesignElementHandle handle) {
		columnList = DEUtil.getVisiableColumnBindingsList(handle);
		if (columnList.isEmpty()) {
			return EMPTY;
		}
		String[] values = new String[columnList.size()];
		for (int i = 0; i < columnList.size(); i++) {
			values[i] = ((ComputedColumnHandle) columnList.get(i)).getName();
		}
		return values;
	}

	private Object getResultSetColumn(String name) {
		if (columnList.isEmpty()) {
			return null;
		}
		for (int i = 0; i < columnList.size(); i++) {
			ComputedColumnHandle column = (ComputedColumnHandle) columnList.get(i);
			if (column.getName().equals(name)) {
				return column;
			}
		}
		return null;
	}

	/**
	 * Moves one item from a position to another.
	 * 
	 * @param item   DesignElement object
	 * @param oldPos The item's current position
	 * @param newPos The item's new position
	 * @return True if success, otherwise false.
	 */
	public boolean moveItem(Object item, int oldPos, int newPos) throws PropertyValueException {
		DesignElementHandle element = (DesignElementHandle) item;
		PropertyHandle propertyHandle = element.getPropertyHandle(fFilterPropertyName);
		propertyHandle.moveItem(oldPos, newPos);

		return true;
	}

	/**
	 * Deletes an item.
	 * 
	 * @param item DesignElement object
	 * @param pos  The item's current position
	 * @return True if success, otherwise false.
	 */
	public boolean deleteItem(Object item, int pos) throws PropertyValueException {
		DesignElementHandle element = (DesignElementHandle) item;
		PropertyHandle propertyHandle = element.getPropertyHandle(fFilterPropertyName);
		if (propertyHandle.getAt(pos) != null) {
			propertyHandle.removeItem(pos);
		}

		try {
			if (propertyHandle.getListValue() == null || propertyHandle.getListValue().size() == 0)
				element.setProperty(fFilterPropertyName, null);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}

		return true;
	}

	/**
	 * Inserts one item into the given position.
	 * 
	 * @param item DesignElement object
	 * @param pos  The position.
	 * @return True if success, otherwise false.
	 * @throws SemanticException
	 */
	public boolean doAddItem(Object item, int pos) throws SemanticException {
		return true;
	}
}