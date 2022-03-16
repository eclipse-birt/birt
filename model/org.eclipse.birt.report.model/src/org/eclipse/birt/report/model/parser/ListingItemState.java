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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.DataColumnNameValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * This class parses common properties for both list and table report items.
 *
 * @see org.eclipse.birt.report.model.elements.ListingElement
 */

public abstract class ListingItemState extends ReportItemState {

	/**
	 * Default value of page break interval.
	 */
	private static final Integer PAGE_BREAK_INTERVAL_DEFAULT_VALUE = 50;

	/**
	 * The listing element (table or list) being built.
	 */

	protected ListingElement element;

	/**
	 * Constructs a state to parse the common properties of the list and table
	 * report items.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public ListingItemState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/**
	 * Constructs listing item(table/list) state with the design parser handler, the
	 * container element and the container property name of the report element.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public ListingItemState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
		super(handler, theContainer, prop);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	@Override
	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	@Override
	public void end() throws SAXException {
		makeTestExpressionCompatible();

		checkListingGroup();

		// works on the column binding property on the group element.

		Set elements = handler.tempValue.keySet();
		ContainerSlot groups = element.getSlot(ListingElement.GROUP_SLOT);
		for (int i = 0; i < groups.getCount(); i++) {
			GroupElement group = (GroupElement) groups.getContent(i);

			String groupName = (String) group.getLocalProperty(handler.module, IGroupElementModel.GROUP_NAME_PROP);
			if (StringUtil.isBlank(groupName)) {
				handler.getModule().makeUniqueName(group);
			}

			groupName = (String) group.getLocalProperty(handler.getModule(), IGroupElementModel.GROUP_NAME_PROP);

			if (!elements.contains(group) || (handler.versionNumber >= VersionUtil.VERSION_3_2_2)) {
				continue;
			}

			List columns = (List) handler.tempValue.get(group);
			if (columns == null || columns.isEmpty()) {
				continue;
			}

			List tmpList = (List) element.getLocalProperty(handler.module, ListingElement.BOUND_DATA_COLUMNS_PROP);

			ElementPropertyDefn boundPropDefn = element.getPropertyDefn(ListingElement.BOUND_DATA_COLUMNS_PROP);
			if (tmpList == null) {
				tmpList = new ArrayList();
				element.setProperty(boundPropDefn, tmpList);
			}

			StructureContext context = new StructureContext(element, boundPropDefn, null);
			if (handler.versionNumber <= VersionUtil.VERSION_3_0_0) {
				addCachedListWithAggregateOnToListing(columns, context, tmpList, group, groupName);
				continue;
			}

			addCachedListToListing(columns, context, tmpList, group, groupName);

		}

		if (handler.versionNumber < VersionUtil.VERSION_3_2_16) {
			ElementPropertyDefn prop = element.getPropertyDefn(IListingElementModel.PAGE_BREAK_INTERVAL_PROP);
			Object value = element.getStrategy().getPropertyExceptRomDefault(handler.getModule(), element, prop);
			if (value == null) {
				element.setProperty(IListingElementModel.PAGE_BREAK_INTERVAL_PROP, PAGE_BREAK_INTERVAL_DEFAULT_VALUE);
			}
		}

		super.end();
	}

	/**
	 * Returns the bound column of which expression and aggregateOn values are
	 * equals to the input column.
	 *
	 * @param columns the bound column list
	 * @param column  the input bound column
	 * @return the matched bound column
	 */

	private ComputedColumn checkMatchedBoundColumnForGroup(List columns, String expression, String aggregateOn,
			boolean mustMatchAggregateOn) {
		if ((columns == null) || (columns.size() == 0) || expression == null) {
			return null;
		}

		for (int i = 0; i < columns.size(); i++) {
			ComputedColumn column = (ComputedColumn) columns.get(i);
			if (expression.equals(column.getExpression())) {
				String tmpAggregateOn = column.getAggregateOn();
				if (mustMatchAggregateOn) {
					if ((aggregateOn == null && tmpAggregateOn == null) || (aggregateOn != null && aggregateOn.equals(tmpAggregateOn))) {
						return column;
					}
				} else if (tmpAggregateOn == null || tmpAggregateOn.equals(aggregateOn)) {
					return column;
				}

			}
		}

		return null;
	}

	/**
	 * Creates a unique bound column name in the column bound list.
	 *
	 * @param columns     the bound column list
	 * @param checkColumn the column of which name to check
	 * @return the newly created column name
	 */

	private String getUniqueBoundColumnNameForGroup(List columns, ComputedColumn checkColumn) {
		String oldName = checkColumn.getName();
		String tmpName = oldName;
		int index = 0;

		while (true) {
			ComputedColumn column = DataColumnNameValidator.getColumn(columns, tmpName);
			if (column == null) {
				break;
			}

			tmpName = oldName + "_" + ++index; //$NON-NLS-1$
		}

		return tmpName;
	}

	/**
	 * Reset the result column name for the data item. Since the bound column name
	 * may recreated in this state, the corresponding result set column must be
	 * reseted.
	 *
	 * @param group   the group element
	 * @param columns the bound column list
	 */

	private void reCheckResultSetColumnName(GroupElement group, List columns) {
		int level = -1;
		if (group instanceof TableGroup) {
			level = 3;
		}
		if (group instanceof ListGroup) {
			level = 1;
		}

		LevelContentIterator contentIter = new LevelContentIterator(handler.module, group, level);
		while (contentIter.hasNext()) {
			DesignElement item = contentIter.next();
			if (!(item instanceof DataItem)) {
				continue;
			}

			String resultSetColumn = (String) item.getLocalProperty(handler.module, DataItem.RESULT_SET_COLUMN_PROP);

			if (StringUtil.isBlank(resultSetColumn)) {
				continue;
			}

			ComputedColumn foundColumn = DataColumnNameValidator.getColumn(columns, resultSetColumn);
			if (foundColumn == null) {
				continue;
			}

			foundColumn = checkMatchedBoundColumnForGroup(columns, foundColumn.getExpression(),
					(String) group.getLocalProperty(handler.module, GroupElement.GROUP_NAME_PROP),
					ExpressionUtil.hasAggregation(foundColumn.getExpression()));

			if (foundColumn == null) {
				continue;
			}
			item.setProperty(DataItem.RESULT_SET_COLUMN_PROP, foundColumn.getName());
		}
	}

	/**
	 * Add cached bound columns for the given group to the group's listing
	 * container. This is for old design file that do not have bound column
	 * features.
	 *
	 * @param columns   bound columns to add
	 * @param tmpList   bound column values of the listing container
	 * @param group     the list/table group
	 * @param groupName the group name
	 */

	public void addCachedListWithAggregateOnToListing(List columns, StructureContext context, List tmpList,
			GroupElement group, String groupName) {
		for (int j = 0; j < columns.size(); j++) {
			ComputedColumn column = (ComputedColumn) columns.get(j);

			if (ExpressionUtil.hasAggregation(column.getExpression())) {
				column.setAggregateOn(groupName);
			}

			ComputedColumn foundColumn = checkMatchedBoundColumnForGroup(tmpList, column.getExpression(),
					column.getAggregateOn(), true);
			if (foundColumn == null || !foundColumn.getName().equals(column.getName())) {
				String newName = getUniqueBoundColumnNameForGroup(tmpList, column);
				column.setName(newName);
				// can not call tmpList.add(column) to insert this column to
				// list, must call structureContext to add it; otherwise the
				// column will not set up the structure context
				context.add(column);

			}
		}

		reCheckResultSetColumnName(group, tmpList);
	}

	/**
	 * Add cached bound columns for the given group to the group's listing
	 * container. This method is for the design file with the bound column feature
	 * and the group defined the bound column properties.
	 *
	 * @param columns   bound columns to add
	 * @param tmpList   bound column values of the listing container
	 * @param group     the list/table group
	 * @param groupName the group name
	 */

	public void addCachedListToListing(List columns, StructureContext context, List tmpList, GroupElement group,
			String groupName) {
		for (int j = 0; j < columns.size(); j++) {
			ComputedColumn column = (ComputedColumn) columns.get(j);

			if (!tmpList.contains(column)) {
				if (ExpressionUtil.hasAggregation(column.getExpression())) {
					column.setAggregateOn(groupName);
				}

				// can not call tmpList.add(column) to insert this column to
				// list, must call structureContext to add it; otherwise the
				// column will not set up the structure context
				context.add(column);
			}
		}
	}

	/**
	 * @param listing
	 * @param tmpHandler
	 *
	 */

	private void checkListingGroup() {
		if (handler.versionNumber < VersionUtil.VERSION_3_2_14) {
			return;
		}

		ElementRefValue refValue = (ElementRefValue) element.getLocalProperty(handler.module,
				IReportItemModel.DATA_BINDING_REF_PROP);
		if (refValue == null) {
			return;
		}

		// for template table/list, there is no need to do data group recovery.

		if (element.getContainerInfo().isManagedByNameSpace()) {
			handler.addUnresolveListingElement(element);
		}

	}
}
