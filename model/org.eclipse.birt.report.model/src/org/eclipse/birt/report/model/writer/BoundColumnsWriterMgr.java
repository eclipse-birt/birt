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

package org.eclipse.birt.report.model.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.DataColumnNameValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TemplateReportItem;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.BoundColumnsMgr;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.LevelContentIterator;

/**
 * The utility to provide backward compatibility of bound columns during writing
 * the design file.
 */

final class BoundColumnsWriterMgr extends BoundColumnsMgr {

	/**
	 * Elements on which bound columns have been created.
	 */

	private Set<DesignElement> processedElement = new HashSet<DesignElement>();

	/**
	 * 
	 */

	private Map<GroupElement, List<Object>> cachedGroup = new HashMap<GroupElement, List<Object>>();

	/**
	 * The design file version from parsing.
	 */

	private String version = null;

	/**
	 * Constructs a writer manager with the given design version.
	 * 
	 * @param version the design version
	 */

	protected BoundColumnsWriterMgr(String version) {
		super();
		this.version = version;
	}

	/**
	 * Creates bound columns for the given element.
	 * 
	 * @param element   the element
	 * @param module    the root of the element
	 * @param propValue the value from which to create bound columns
	 */

	protected void handleBoundsForValue(DesignElement element, Module module, String propValue) {
		if (propValue == null)
			return;

		List newExprs = null;

		try {
			newExprs = ExpressionUtil.extractColumnExpressions(propValue);
		} catch (BirtException e) {
			// do nothing
		}

		if (newExprs == null || newExprs.isEmpty())
			return;

		DesignElement target = BoundDataColumnUtil.findTargetOfBoundColumns(element, module);

		if (target instanceof GroupElement) {
			appendBoundColumnsToGroup((GroupElement) target, newExprs);
			return;
		}

		if (newExprs != null && newExprs.size() >= 1) {
			for (int i = 0; i < newExprs.size(); i++) {
				IColumnBinding boundColumn = (IColumnBinding) newExprs.get(i);
				String newExpression = boundColumn.getBoundExpression();
				if (newExpression == null)
					continue;

				BoundDataColumnUtil.createBoundDataColumn(target, boundColumn.getResultSetColumnName(), newExpression,
						module);
			}
		}
	}

	/**
	 * Appends to the cached group bound columns. Becuase of "aggregateOn" property
	 * on bound columns, has to add bound columns at end() function of
	 * ListingElementState.
	 * 
	 * @param target   the group element
	 * @param newExprs bound columns returned by ExpressionUtil
	 */

	private void appendBoundColumnsToGroup(GroupElement target, List newExprs) {
		List newColumns = new ArrayList();
		for (int i = 0; i < newExprs.size(); i++) {
			ComputedColumn column = StructureFactory.createComputedColumn();
			IColumnBinding boundColumn = (IColumnBinding) newExprs.get(i);
			String newExpression = boundColumn.getBoundExpression();
			if (newExpression == null)
				continue;

			column.setName(boundColumn.getResultSetColumnName());
			column.setExpression(boundColumn.getBoundExpression());
			if (!newColumns.contains(column))
				newColumns.add(column);
		}

		appendBoundColumnsToCachedGroup(target, newColumns);
	}

	/**
	 * Appends to the cached group bound columns. Becuase of "aggregateOn" property
	 * on bound columns, has to add bound columns at end() function of
	 * ListingElementState.
	 * 
	 * @param target   the group element
	 * @param newExprs bound columns returned by ExpressionUtil
	 */

	private void appendBoundColumnsToCachedGroup(GroupElement target, List newColumns) {
		List<Object> boundColumns = cachedGroup.get(target);
		if (boundColumns == null) {
			cachedGroup.put(target, newColumns);
			return;
		}

		for (int i = 0; i < newColumns.size(); i++) {
			ComputedColumn column = (ComputedColumn) newColumns.get(i);
			if (!boundColumns.contains(column))
				boundColumns.add(column);
		}
	}

	/**
	 * Appends to the cached group bound columns. Becuase of "aggregateOn" property
	 * on bound columns, has to add bound columns at end() function of
	 * ListingElementState.
	 * 
	 * @param target     the group element
	 * @param boundName  the bound column name
	 * @param expression the bound column expression
	 * @return the return bound name
	 */

	protected String appendBoundColumnsToCachedGroup(GroupElement target, String boundName, String expression) {
		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName(boundName);
		column.setExpression(expression);

		List<Object> boundColumns = cachedGroup.get(target);
		if (boundColumns == null) {
			List newColumns = new ArrayList();
			newColumns.add(column);

			cachedGroup.put(target, newColumns);
			return boundName;
		}

		boundColumns.add(column);

		return boundName;
	}

	/**
	 * Creates bound columns for the given value of the given element.
	 * 
	 * @param element   the element
	 * @param module    the root of the element
	 * @param propValue the value from which to create bound columns
	 */

	protected void handleBoundsForParamBinding(DesignElement element, Module module, String propValue) {
		if (propValue == null)
			return;

		List newExprs = null;

		try {
			newExprs = ExpressionUtil.extractColumnExpressions(propValue);
		} catch (BirtException e) {
			// do nothing
		}

		if (newExprs != null && newExprs.size() >= 1) {
			DesignElement target = BoundDataColumnUtil.findTargetElementOfParamBinding(element, module);

			for (int i = 0; i < newExprs.size(); i++) {
				IColumnBinding boundColumn = (IColumnBinding) newExprs.get(i);
				String newExpression = boundColumn.getBoundExpression();
				if (newExpression == null)
					continue;

				BoundDataColumnUtil.createBoundDataColumn(target, boundColumn.getResultSetColumnName(), newExpression,
						module);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealData(org.eclipse
	 * .birt.report.model.elements.DataItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealData(DataItem element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealData(element, module);
		dealCompatibleValueExpr(element, module);
	}

	/**
	 * Converts the old value expression to the new result set column with
	 * correspoding bound columns.
	 * 
	 * @param obj the data item
	 */

	private void dealCompatibleValueExpr(DataItem obj, Module module) {

		String valueExpr = (String) obj.getLocalProperty(module, IDataItemModel.RESULT_SET_COLUMN_PROP);
		if (valueExpr == null)
			return;

		List newExprs = null;

		try {
			newExprs = ExpressionUtil.extractColumnExpressions(valueExpr);
		} catch (BirtException e) {
			// do nothing
		}

		if (newExprs == null || newExprs.size() == 0)
			return;

		DesignElement target = BoundDataColumnUtil.findTargetOfBoundColumns(obj, module);

		if (newExprs != null && newExprs.size() == 1) {
			IColumnBinding column = (IColumnBinding) newExprs.get(0);

			String newName = column.getResultSetColumnName();
			if (target instanceof GroupElement) {
				appendBoundColumnsToCachedGroup((GroupElement) target, newName, column.getBoundExpression());
			} else {
				newName = BoundDataColumnUtil.createBoundDataColumn(target, newName, column.getBoundExpression(),
						module);
			}

			if (valueExpr.equals(ExpressionUtil.createRowExpression(column.getResultSetColumnName()))) {
				// set the property for the result set column property of
				// DataItem.

				obj.setProperty(IDataItemModel.RESULT_SET_COLUMN_PROP, newName);

				return;
			}
		}

		if (newExprs != null && newExprs.size() > 1) {
			if (target instanceof GroupElement) {
				appendBoundColumnsToCachedGroup((GroupElement) target, newExprs);
			} else {
				for (int i = 0; i < newExprs.size(); i++) {
					IColumnBinding boundColumn = (IColumnBinding) newExprs.get(i);
					String newExpression = boundColumn.getBoundExpression();
					if (newExpression == null)
						continue;

					BoundDataColumnUtil.createBoundDataColumn(target, boundColumn.getResultSetColumnName(),
							newExpression, module);
				}
			}
		}

		String newName = valueExpr;
		if (target instanceof GroupElement) {
			appendBoundColumnsToCachedGroup((GroupElement) target, valueExpr, valueExpr);
		} else {
			newName = BoundDataColumnUtil.createBoundDataColumn(target, valueExpr, valueExpr, module);
		}

		// set the property for the result set column property of DataItem.

		obj.setProperty(IDataItemModel.RESULT_SET_COLUMN_PROP, newName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealExtendedItem(org
	 * .eclipse.birt.report.model.elements.ExtendedItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealExtendedItem(ExtendedItem element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealExtendedItem(element, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealGrid(org.eclipse
	 * .birt.report.model.elements.GridItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealGrid(GridItem element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealGrid(element, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealImage(org.eclipse
	 * .birt.report.model.elements.ImageItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealImage(ImageItem element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealImage(element, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealLabel(org.eclipse
	 * .birt.report.model.elements.Label, org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealLabel(Label element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealLabel(element, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealList(org.eclipse
	 * .birt.report.model.elements.ListItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealList(ListItem element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealList(element, module);

		appendCachedBoundColumns(element, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealScalarParameter
	 * (org.eclipse.birt.report.model.elements.ScalarParameter,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealScalarParameter(ScalarParameter element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealScalarParameter(element, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealTable(org.eclipse
	 * .birt.report.model.elements.TableItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealTable(TableItem element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealTable(element, module);

		appendCachedBoundColumns(element, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.BoundColumnsMgr#dealTemplateReportItem
	 * (org.eclipse.birt.report.model.elements.TemplateReportItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealTemplateReportItem(TemplateReportItem element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealTemplateReportItem(element, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealText(org.eclipse
	 * .birt.report.model.elements.TextItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealText(TextItem element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealText(element, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.BoundColumnsMgr#dealTextData(org.eclipse
	 * .birt.report.model.elements.TextDataItem,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealTextData(TextDataItem element, Module module) {
		if (version != null || processedElement.contains(element))
			return;

		processedElement.add(element);

		super.dealTextData(element, module);
	}

	/**
	 * Returns the bound column of which expression and aggregateOn values are
	 * equals to the input column.
	 * 
	 * @param columns the bound column list
	 * @param column  the input bound column
	 * @return the matched bound column
	 */

	private ComputedColumn checkMatchedBoundColumnForGroup(List columns, String expression, String aggregateOn) {
		if ((columns == null) || (columns.size() == 0) || expression == null)
			return null;

		for (int i = 0; i < columns.size(); i++) {
			ComputedColumn column = (ComputedColumn) columns.get(i);
			if (expression.equals(column.getExpression())) {
				if (aggregateOn == null && column.getAggregateOn() == null)
					return column;

				if (aggregateOn != null && aggregateOn.equals(column.getAggregateOn()))
					return column;
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
			if (column == null)
				break;

			tmpName = oldName + "_" + ++index; //$NON-NLS-1$
		}

		return tmpName;
	}

	/**
	 * Reset the result column name for the data item. Since the bound column name
	 * may recreated in this state, the corresponding result set colum must be
	 * resetted.
	 * 
	 * @param group   the group element
	 * @param columns the bound column list
	 */

	private void reCheckResultSetColumnName(GroupElement group, List columns, Module module) {
		int level = -1;
		if (group instanceof TableGroup)
			level = 3;
		if (group instanceof ListGroup)
			level = 1;

		LevelContentIterator contentIter = new LevelContentIterator(module, group, level);
		while (contentIter.hasNext()) {
			DesignElement item = contentIter.next();
			if (!(item instanceof DataItem))
				continue;

			String resultSetColumn = (String) item.getLocalProperty(module, IDataItemModel.RESULT_SET_COLUMN_PROP);

			if (StringUtil.isBlank(resultSetColumn))
				continue;

			ComputedColumn foundColumn = DataColumnNameValidator.getColumn(columns, resultSetColumn);

			if (foundColumn == null)
				continue;

			foundColumn = checkMatchedBoundColumnForGroup(columns, foundColumn.getExpression(),
					(String) group.getLocalProperty(module, IGroupElementModel.GROUP_NAME_PROP));

			if (foundColumn == null)
				continue;

			item.setProperty(IDataItemModel.RESULT_SET_COLUMN_PROP, foundColumn.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	private void appendCachedBoundColumns(ListingElement element, Module module) {

		Set<GroupElement> elements = cachedGroup.keySet();
		ContainerSlot groups = element.getSlot(IListingElementModel.GROUP_SLOT);
		for (int i = 0; i < groups.getCount(); i++) {
			GroupElement group = (GroupElement) groups.getContent(i);

			module.makeUniqueName(group);

			String groupName = (String) group.getLocalProperty(module, IGroupElementModel.GROUP_NAME_PROP);

			if (!elements.contains(group))
				continue;

			List<Object> columns = cachedGroup.get(group);
			if (columns == null || columns.isEmpty())
				continue;

			List tmpList = (List) element.getLocalProperty(module, IReportItemModel.BOUND_DATA_COLUMNS_PROP);

			if (tmpList == null) {
				tmpList = new ArrayList();
				element.setProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP, tmpList);
			}

			for (int j = 0; j < columns.size(); j++) {
				ComputedColumn column = (ComputedColumn) columns.get(j);

				column.setAggregateOn(groupName);

				ComputedColumn foundColumn = checkMatchedBoundColumnForGroup(tmpList, column.getExpression(),
						column.getAggregateOn());
				if (foundColumn == null || !foundColumn.getName().equals(column.getName())) {
					String newName = getUniqueBoundColumnNameForGroup(tmpList, column);
					column.setName(newName);
					tmpList.add(column);
				}
			}

			reCheckResultSetColumnName(group, tmpList, module);
		}

	}
}
