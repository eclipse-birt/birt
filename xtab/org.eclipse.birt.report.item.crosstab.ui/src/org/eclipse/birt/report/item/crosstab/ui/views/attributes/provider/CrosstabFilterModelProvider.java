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
import java.util.logging.Level;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.FilterModelProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.dialogs.CrosstabFilterConditionBuilder;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.jface.dialogs.Dialog;

/**
 * 
 */

public class CrosstabFilterModelProvider extends FilterModelProvider {

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = new String[0];

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * 
	 */
	public CrosstabFilterModelProvider() {
		// TODO Auto-generated constructor stub
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
		if (item instanceof ExtendedItemHandle && ((ExtendedItemHandle) item).getExtensionName().equals("Crosstab")) //$NON-NLS-1$
		{
			List list = new ArrayList();
			list.add(item);
			Object[] levelArray = getElements(list);
			if (levelArray == null || levelArray.length <= 0) {
				return true;
			}
			TargetFilterConditionHandle targetFilterHandle = (TargetFilterConditionHandle) Arrays.asList(levelArray)
					.get(pos);
			if (targetFilterHandle == null) {
				return false;
			}

			Object target = targetFilterHandle.getTarget();

			FilterConditionElementHandle filterHandle = targetFilterHandle.getfilterConditionHandle();

			CrosstabFilterConditionBuilder dialog = new CrosstabFilterConditionBuilder(UIUtil.getDefaultShell(),
					CrosstabFilterConditionBuilder.DLG_TITLE_EDIT, CrosstabFilterConditionBuilder.DLG_MESSAGE_EDIT);
			dialog.setDesignHandle((DesignElementHandle) item);
			dialog.setInput(filterHandle, target);
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}
		} else {
			return false;
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
		TargetFilterConditionHandle targetfilterKeyHandle = (TargetFilterConditionHandle) Arrays.asList(levelArray)
				.get(pos);
		// Object target = targetfilterKeyHandle.getTarget( );
		// PropertyHandle propertyHandle = level.getModelHandle( )
		// .getPropertyHandle( ILevelViewConstants.FILTER_PROP );
		FilterConditionElementHandle filterCondition = targetfilterKeyHandle.getfilterConditionHandle();
		// if ( propertyHandle != null && filterCondition != null )
		// {
		// propertyHandle.removeItem( filterCondition );
		// }

		try {
			filterCondition.drop();
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
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
			CrosstabFilterConditionBuilder dialog = new CrosstabFilterConditionBuilder(UIUtil.getDefaultShell(),
					CrosstabFilterConditionBuilder.DLG_TITLE_NEW, CrosstabFilterConditionBuilder.DLG_MESSAGE_NEW);
			dialog.setDesignHandle((DesignElementHandle) item);
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}

		} else {
			return super.doAddItem(item, pos);
		}
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

		if (!(element instanceof TargetFilterConditionHandle))
			return "";//$NON-NLS-1$

		if (key.equals(ILevelViewConstants.LEVEL_PROP)) {

			Object target = ((TargetFilterConditionHandle) element).getTarget();

			if (target instanceof LevelViewHandle) {
				LevelHandle cubeLevel = ((LevelViewHandle) target).getCubeLevel();
				if (cubeLevel == null) {
					return EMPTY_STRING;
				} else {
					return cubeLevel.getFullName();
				}
			} else if (target instanceof MeasureViewHandle) {
				MeasureHandle cubeMeasure = ((MeasureViewHandle) target).getCubeMeasure();
				if (cubeMeasure == null) {
					return EMPTY_STRING;
				} else {
					return cubeMeasure.getFullName();
				}
			} else if (target instanceof CrosstabReportItemHandle) {
				CubeHandle cube = ((CrosstabReportItemHandle) target).getCube();
				if (cube == null) {
					return EMPTY_STRING;
				} else {
					return cube.getFullName();
				}
			}

		}

		Object tmpElement = ((TargetFilterConditionHandle) element).getfilterConditionHandle();
		String value = ((FilterConditionElementHandle) tmpElement).getStringProperty(key);
		if (value == null) {
			value = "";//$NON-NLS-1$
		}

		if (key.equals(IFilterConditionElementModel.OPERATOR_PROP)) {
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
	 * Gets the display names of the given property keys.
	 * 
	 * @param keys Property keys
	 * @return String array contains display names
	 */
	public String[] getColumnNames(String[] keys) {
		assert keys != null;
		String[] columnNames = new String[keys.length];
		columnNames[0] = Messages.getString("CrosstabFilterModelProvider.ColumnName.GroupLevelOrMeasure"); //$NON-NLS-1$
		for (int i = 1; i < keys.length; i++) {
			IElementDefn ElementDefn = DEUtil.getMetaDataDictionary()
					.getElement(ReportDesignConstants.FILTER_CONDITION_ELEMENT);
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

		int measureCount = crossTab.getMeasureCount();
		for (int i = 0; i < measureCount; i++) {
			MeasureViewHandle measureView = crossTab.getMeasure(i);
			Iterator iter = measureView.filtersIterator();
			while (iter.hasNext()) {
				TargetFilterConditionHandle levelSortKeyHandle = new TargetFilterConditionHandle(measureView,
						(FilterConditionElementHandle) iter.next());
				list.add(levelSortKeyHandle);
			}
		}

		Iterator iter = crossTab.filtersIterator();
		while (iter.hasNext()) {
			TargetFilterConditionHandle levelSortKeyHandle = new TargetFilterConditionHandle(crossTab,
					(FilterConditionElementHandle) iter.next());
			list.add(levelSortKeyHandle);
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
				Iterator iter = levelHandle.filtersIterator();
				while (iter.hasNext()) {
					TargetFilterConditionHandle levelSortKeyHandle = new TargetFilterConditionHandle(levelHandle,
							(FilterConditionElementHandle) iter.next());
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

	static class TargetFilterConditionHandle {

		protected FilterConditionElementHandle filterHandle;
		protected Object target;

		public TargetFilterConditionHandle(Object target, FilterConditionElementHandle filterCondition) {
			this.target = target;
			this.filterHandle = filterCondition;
		}

		public FilterConditionElementHandle getfilterConditionHandle() {
			return this.filterHandle;
		}

		public void setFilterConditionHandle(FilterConditionElementHandle filterCondition) {
			this.filterHandle = filterCondition;
		}

		public Object getTarget() {
			return this.target;
		}

		public void setTarget(Object target) {
			this.target = target;
		}

	}

}
