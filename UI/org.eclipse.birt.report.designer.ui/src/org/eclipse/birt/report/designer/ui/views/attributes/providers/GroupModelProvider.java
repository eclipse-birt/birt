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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;

/**
 * Group data processor
 */
public class GroupModelProvider {

	/**
	 * Column widths.
	 */
	private static int[] columnWidth = { 250, 250 };

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = {};

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
			columnNames[i] = getDisplayName(ReportDesignConstants.TABLE_GROUP_ELEMENT, keys[i]);
		}
		return columnNames;
	}

	private String getDisplayName(String elementName, String property) {
		String name = null;
		IElementPropertyDefn propertyDefn = DEUtil.getMetaDataDictionary().getElement(elementName)
				.getProperty(property);
		if (propertyDefn != null) {
			name = Messages.getString(propertyDefn.getDisplayNameID());
		}

		if (name == null) {
			return ""; //$NON-NLS-1$
		}
		return name;
	}

	/**
	 * Gets all elements of the given input.
	 *
	 * @param input The input object.
	 * @return Groups array. Return null if the list if empty.
	 */
	public Object[] getElements(List input) {
		if (input.isEmpty()) {
			return null;
		}
		Object obj = input.get(0);
		if (!(obj instanceof ListingHandle)) {
			return EMPTY;
		}

		ListingHandle element = (ListingHandle) obj;
		SlotHandle slot = element.getGroups();

		Iterator iterator = slot.iterator();
		if (iterator == null) {
			return EMPTY;
		}
		List list = new ArrayList();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list.toArray();
	}

	/**
	 * Gets property display name of a given element.
	 *
	 * @param element Group object
	 * @param key     Property key
	 * @return The property display name
	 */
	public String getText(Object element, String key) {

		GroupHandle handle = (GroupHandle) element;

		if (key.equals(GroupHandle.GROUP_NAME_PROP)) {
			if (handle.getName() == null) {
				return ""; //$NON-NLS-1$
			}
			return handle.getName();
		}

		if (handle.getKeyExpr() == null) {
			return ""; //$NON-NLS-1$
		}
		return handle.getKeyExpr();
	}

	/**
	 * Saves new property value to filter
	 *
	 * @param element  Group object
	 * @param key      Property key
	 * @param newValue new value
	 * @return True for success, False for failure
	 */
	public boolean setStringValue(Object item, Object element, String key, String newValue)
			throws NameException, SemanticException {
		GroupHandle handle = (GroupHandle) element;
		handle.setKeyExpr(newValue);

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
		if (!(item instanceof ReportItemHandle)) {
			return EMPTY;
		}
		return getDataSetColumns((ReportItemHandle) item);
	}

	/**
	 * Gets all columns in a dataSet.
	 *
	 * @param handle ReportItem object
	 * @return Columns array.
	 */
	private String[] getDataSetColumns(ReportItemHandle handle) {
		DataSetHandle dataSet = handle.getDataSet();
		if (dataSet == null) {
			return EMPTY;
		}
		Iterator iterator = dataSet.resultSetHintsIterator();
		if (iterator == null) {
			return EMPTY;
		}
		ArrayList columns = new ArrayList();
		while (iterator.hasNext()) {
			ResultSetColumn resultSetColumn = (ResultSetColumn) iterator.next();
			columns.add(resultSetColumn.getColumnName());
		}
		return (String[]) columns.toArray(new String[0]);
	}

	/**
	 * Moves one item from a position to another.
	 *
	 * @param item   DesignElement object
	 * @param oldPos The item's old position
	 * @param newPos The item's current position
	 * @return True if success, otherwise false.
	 */
	public boolean moveItem(Object item, int oldPos, int newPos) throws SemanticException {
		DesignElementHandle element = (DesignElementHandle) item;

		SlotHandle slotHandle = element.getSlot(ListingHandle.GROUP_SLOT);
		DesignElementHandle group = slotHandle.get(oldPos);
		slotHandle.shift(group, newPos);

		return true;
	}

	/**
	 * Deletes an item.
	 *
	 * @param item DesignElement object
	 * @param pos  The item's current position
	 * @return True if success, otherwise false.
	 * @throws SemanticException
	 */
	public boolean deleteItem(Object item, int pos) throws SemanticException {
		DesignElementHandle element = (DesignElementHandle) item;
		element.getSlot(ListingHandle.GROUP_SLOT).drop(pos);

		return true;
	}

	/**
	 * Add an item.
	 *
	 * @param item     DesignElement object
	 * @param newGroup The new group to be added
	 * @param pos      The item's current position
	 * @return True if success, otherwise false.
	 * @throws ContentException, NameException
	 */
	public boolean addItem(Object item, Object newGroup, int pos) throws ContentException, NameException {
		DesignElementHandle element = (DesignElementHandle) item;
		if (newGroup instanceof GroupHandle) {
			element.getSlot(ListingHandle.GROUP_SLOT).add((GroupHandle) newGroup, pos);
			return true;
		}
		return false;
	}

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
			if (GroupHandle.KEY_EXPR_PROP.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}
}
