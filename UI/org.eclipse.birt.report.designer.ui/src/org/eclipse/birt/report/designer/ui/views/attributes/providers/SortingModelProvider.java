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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.SortkeyBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.dialogs.Dialog;

import com.ibm.icu.util.ULocale;

/**
 * Sort data processor
 */
public class SortingModelProvider {

	protected static Logger logger = Logger.getLogger(SortingModelProvider.class.getName());

	/**
	 * The list of allowed SortKey.DIRECTION_MEMBER
	 */
	protected final IChoiceSet choiceSetDirection = ChoiceSetFactory.getStructChoiceSet(SortKey.SORT_STRUCT,
			SortKey.DIRECTION_MEMBER);

	/**
	 * Constant, represents empty String array.
	 */
	protected static final String[] EMPTY = new String[0];

	private List columnList;

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
			IStructureDefn structure = DEUtil.getMetaDataDictionary().getStructure(SortKey.SORT_STRUCT);
			columnNames[i] = structure.getMember(keys[i]).getDisplayName();
		}
		return columnNames;
	}

	/**
	 * Gets all elements of the given input.
	 * 
	 * @param input The input object.
	 * @return Sorts array.
	 */
	public Object[] getElements(List input) {
		Object obj = input.get(0);
		if (!(obj instanceof DesignElementHandle))
			return EMPTY;
		DesignElementHandle element = (DesignElementHandle) obj;
		PropertyHandle propertyHandle = element.getPropertyHandle(ListingHandle.SORT_PROP);
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
	 * @param element Sort object
	 * @param key     Property key
	 * @return
	 */
	public String getText(Object element, String key) {
		if (!(element instanceof StructureHandle))
			return "";//$NON-NLS-1$

		String value = ((StructureHandle) element).getMember(key).getStringValue();
		if (value == null)
			value = "";//$NON-NLS-1$
		if (key.equals(SortKey.DIRECTION_MEMBER)) {
			IChoice choice = choiceSetDirection.findChoice(value);
			if (choice != null)
				return choice.getDisplayName();
		} else if (key.equals(SortKey.LOCALE_MEMBER) && element instanceof SortKeyHandle) {
			SortKeyHandle sortKey = (SortKeyHandle) element;
			if (sortKey.getLocale() != null) {
				for (Map.Entry<String, ULocale> entry : FormatAdapter.LOCALE_TABLE.entrySet()) {
					if (sortKey.getLocale().equals(entry.getValue())) {
						return entry.getKey();
					}
				}
			}
			return Messages.getString("SortkeyBuilder.Locale.Auto");
		} else if (key.equals(SortKey.STRENGTH_MEMBER) && element instanceof SortKeyHandle) {
			SortKeyHandle sortKey = (SortKeyHandle) element;
			for (Map.Entry<String, Integer> entry : SortkeyBuilder.STRENGTH_MAP.entrySet()) {
				if (sortKey.getStrength() == entry.getValue()) {
					return entry.getKey();
				}
			}
		} else
			return value;

		return "";//$NON-NLS-1$
	}

	/**
	 * Saves new property value to sort
	 * 
	 * @param element  DesignElementHandle object.
	 * @param element  Sort object
	 * @param key      Property key
	 * @param newValue new value
	 * @return
	 * @throws SemanticException
	 * @throws NameException
	 */
	public boolean setStringValue(Object item, Object element, String key, String newValue) throws SemanticException {
		if (key.equals(SortKey.KEY_MEMBER)) {
			String value = DEUtil.getExpression(getResultSetColumn(newValue));
			if (value != null)
				newValue = value;
		}

		String saveValue = newValue;
		StructureHandle handle = (StructureHandle) element;
		if (key.equals(SortKey.DIRECTION_MEMBER)) {
			IChoice choice = choiceSetDirection.findChoiceByDisplayName(newValue);
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
		if (key.equals(SortKey.DIRECTION_MEMBER)) {
			return ChoiceSetFactory.getDisplayNamefromChoiceSet(choiceSetDirection);
		}
		if (!(item instanceof DesignElementHandle)) {
			return EMPTY;
		}
		return getDataSetColumns((DesignElementHandle) item);
	}

	/**
	 * Gets all columns in a dataSet.
	 * 
	 * @param item ReportItem object
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
	 * @throws PropertyValueException
	 */
	public boolean moveItem(Object item, int oldPos, int newPos) throws PropertyValueException {
		DesignElementHandle element = (DesignElementHandle) item;
		PropertyHandle propertyHandle = element.getPropertyHandle(ListingHandle.SORT_PROP);
		propertyHandle.moveItem(oldPos, newPos);

		return true;
	}

	/**
	 * Deletes an item.
	 * 
	 * @param item DesignElement object
	 * @param pos  The item's current position
	 * @return True if success, otherwise false.
	 * @throws PropertyValueException
	 */
	public boolean deleteItem(Object item, int pos) throws PropertyValueException {
		DesignElementHandle element = (DesignElementHandle) item;
		PropertyHandle propertyHandle = element.getPropertyHandle(ListingHandle.SORT_PROP);
		if (propertyHandle.getAt(pos) != null)
			propertyHandle.removeItem(pos);

		try {
			if (propertyHandle.getListValue() == null || propertyHandle.getListValue().size() == 0)
				element.setProperty(ListingHandle.SORT_PROP, null);
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
		if (item instanceof DesignElementHandle) {
			SortkeyBuilder dialog = new SortkeyBuilder(UIUtil.getDefaultShell(), SortkeyBuilder.DLG_TITLE_NEW,
					SortkeyBuilder.DLG_MESSAGE_NEW);
			dialog.setHandle((DesignElementHandle) item);
			dialog.setInput(null);
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}

		}
		return true;
	}

	/**
	 * Edit one item into the given position.
	 * 
	 * @param item DesignElement object
	 * @param pos  The position.
	 * @return True if success, otherwise false.
	 * @throws SemanticException
	 */
	public boolean doEditItem(Object item, int pos) {
		if (item instanceof DesignElementHandle) {
			DesignElementHandle element = (DesignElementHandle) item;
			PropertyHandle propertyHandle = element.getPropertyHandle(ListingHandle.SORT_PROP);
			SortKeyHandle sortKeyHandle = (SortKeyHandle) (propertyHandle.getAt(pos));
			if (sortKeyHandle == null) {
				return false;
			}
			SortkeyBuilder dialog = new SortkeyBuilder(UIUtil.getDefaultShell(), SortkeyBuilder.DLG_TITLE_EDIT,
					SortkeyBuilder.DLG_MESSAGE_EDIT);
			dialog.setHandle((DesignElementHandle) item);
			dialog.setInput(sortKeyHandle);
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}
		}

		return true;
	}

}