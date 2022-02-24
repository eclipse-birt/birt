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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatAdapter;
import org.eclipse.birt.report.designer.ui.dialogs.SortkeyBuilder;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.SortingModelProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.dialogs.CrosstabSortKeyBuilder;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.ISortElementModel;
import org.eclipse.jface.dialogs.Dialog;

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class CrosstabSortingModelProvider extends SortingModelProvider {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * Edit one item into the given position.
	 * 
	 * @param item DesignElement object
	 * @param pos  The position.
	 * @return True if success, otherwise false.
	 * @throws SemanticException
	 */
	public boolean doEditItem(Object item, int pos) {
		if (item instanceof ExtendedItemHandle && ((ExtendedItemHandle) item).getExtensionName().equals("Crosstab")) //$NON-NLS-1$
		{
			List list = new ArrayList();
			list.add(item);
			Object[] levelArray = getElements(list);
			if (levelArray == null || levelArray.length <= 0) {
				return true;
			}
			LevelSortKeyHandle levelSortKeyHandle = (LevelSortKeyHandle) Arrays.asList(levelArray).get(pos);
			if (levelSortKeyHandle == null) {
				return false;
			}
			LevelViewHandle level = levelSortKeyHandle.getLevelHandle();
			SortElementHandle sortKey = levelSortKeyHandle.getSortKeyHandle();

			CrosstabSortKeyBuilder dialog = new CrosstabSortKeyBuilder(UIUtil.getDefaultShell(),
					SortkeyBuilder.DLG_TITLE_EDIT, SortkeyBuilder.DLG_MESSAGE_EDIT);
			dialog.setHandle((DesignElementHandle) item);
			dialog.setInput(sortKey, level);
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}
		} else {
			return super.doEditItem(item, pos);
		}

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
		List list = new ArrayList();
		list.add(item);
		Object[] levelArray = getElements(list);
		if (levelArray == null || levelArray.length <= 0) {
			return true;
		}
		LevelSortKeyHandle levelSortKeyHandle = (LevelSortKeyHandle) Arrays.asList(levelArray).get(pos);
		LevelViewHandle level = levelSortKeyHandle.getLevelHandle();
		SortElementHandle sortKey = levelSortKeyHandle.getSortKeyHandle();

		try {
			level.getModelHandle().drop(ILevelViewConstants.SORT_PROP, sortKey);
		} catch (SemanticException e) {
			return false;
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
		if (item instanceof ExtendedItemHandle && ((ExtendedItemHandle) item).getExtensionName().equals("Crosstab")) //$NON-NLS-1$
		{
			CrosstabSortKeyBuilder dialog = new CrosstabSortKeyBuilder(UIUtil.getDefaultShell(),
					SortkeyBuilder.DLG_TITLE_NEW, SortkeyBuilder.DLG_MESSAGE_NEW);
			dialog.setHandle((DesignElementHandle) item);
			dialog.setInput(null);
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}

		} else {
			return super.doAddItem(item, pos);
		}
		return true;
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
		// Because user cannot modify the value of table cell directly(User only
		// modify the value by CrosstabSortKeyBuilder), return true.
		return true;
	}

	/**
	 * Gets property display name of a given element.
	 * 
	 * @param element Sort object
	 * @param key     Property key
	 * @return
	 */
	public String getText(Object element, String key) {

		if (!(element instanceof LevelSortKeyHandle))
			return "";//$NON-NLS-1$

		if (key.equals(ILevelViewConstants.LEVEL_PROP)) {
			LevelHandle cubeLevel = ((LevelSortKeyHandle) element).getLevelHandle().getCubeLevel();
			if (cubeLevel == null) {
				return EMPTY_STRING;
			} else {
				return cubeLevel.getFullName();
			}
		}

		element = ((LevelSortKeyHandle) element).getSortKeyHandle();
		String value = null;

		value = ((SortElementHandle) element).getStringProperty(key);
		if (value == null)
			value = "";//$NON-NLS-1$

		if (key.equals(ISortElementModel.DIRECTION_PROP)) {
			IChoice choice = choiceSetDirection.findChoice(value);
			if (choice != null)
				return choice.getDisplayName();
		} else if (key.equals(SortKey.LOCALE_MEMBER)) {
			SortElementHandle sortKey = (SortElementHandle) element;
			if (sortKey.getLocale() != null) {
				for (Map.Entry<String, ULocale> entry : FormatAdapter.LOCALE_TABLE.entrySet()) {
					if (sortKey.getLocale().equals(entry.getValue())) {
						return entry.getKey();
					}
				}
			}
			return Messages.getString("SortkeyBuilder.Locale.Auto");
		} else if (key.equals(SortKey.STRENGTH_MEMBER)) {
			SortElementHandle sortKey = (SortElementHandle) element;
			for (Map.Entry<String, Integer> entry : SortkeyBuilder.STRENGTH_MAP.entrySet()) {
				if (sortKey.getStrength() == entry.getValue()) {
					return entry.getKey();
				}
			}
		}

		return value;

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
		columnNames[0] = Messages.getString("CrosstabSortingModelProvider.ColumnName.GroupLevel"); //$NON-NLS-1$
		for (int i = 1; i < keys.length; i++) {
			IElementDefn ElementDefn = DEUtil.getMetaDataDictionary().getElement(ReportDesignConstants.SORT_ELEMENT);
			columnNames[i] = ElementDefn.getProperty(keys[i]).getDisplayName();
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
		List list = new ArrayList();
		Object obj = input.get(0);
		if (!(obj instanceof ExtendedItemHandle))
			return EMPTY;
		ExtendedItemHandle element = (ExtendedItemHandle) obj;
		CrosstabReportItemHandle crossTab = null;
		try {
			crossTab = (CrosstabReportItemHandle) element.getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		if (crossTab == null) {
			return list.toArray();
		}
		if (crossTab.getCrosstabView(ICrosstabConstants.COLUMN_AXIS_TYPE) != null) {
			DesignElementHandle elementHandle = crossTab.getCrosstabView(ICrosstabConstants.COLUMN_AXIS_TYPE)
					.getModelHandle();
			list.addAll(getLevel((ExtendedItemHandle) elementHandle));
		}

		if (crossTab.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE) != null) {
			DesignElementHandle elementHandle = crossTab.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE)
					.getModelHandle();
			list.addAll(getLevel((ExtendedItemHandle) elementHandle));
		}

		return list.toArray();
	}

	private List getLevel(ExtendedItemHandle handle) {
		CrosstabViewHandle crossTabViewHandle = null;
		try {
			crossTabViewHandle = (CrosstabViewHandle) handle.getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		List list = new ArrayList();
		if (crossTabViewHandle == null) {
			return list;
		}
		int dimensionCount = crossTabViewHandle.getDimensionCount();

		for (int i = 0; i < dimensionCount; i++) {
			DimensionViewHandle dimension = crossTabViewHandle.getDimension(i);
			int levelCount = dimension.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				LevelViewHandle levelHandle = dimension.getLevel(j);
				Iterator iter = levelHandle.sortsIterator();
				while (iter.hasNext()) {
					LevelSortKeyHandle levelSortKeyHandle = new LevelSortKeyHandle(levelHandle,
							(SortElementHandle) iter.next());
					list.add(levelSortKeyHandle);
				}

			}
		}
		return list;
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
		// can not move for Crosstab sorting.
		return false;
	}

	static class LevelSortKeyHandle {

		protected LevelViewHandle levelHandle;
		protected SortElementHandle sortKeyHandle;

		public LevelSortKeyHandle(LevelViewHandle level, SortElementHandle sortKey) {
			this.levelHandle = level;
			this.sortKeyHandle = sortKey;
		}

		public LevelViewHandle getLevelHandle() {
			return this.levelHandle;
		}

		public void setLevelHandle(LevelViewHandle level) {
			this.levelHandle = level;
		}

		public SortElementHandle getSortKeyHandle() {
			return this.sortKeyHandle;
		}

		public void setSortKeyHandle(SortElementHandle sortKey) {
			this.sortKeyHandle = sortKey;
		}

	}

}
