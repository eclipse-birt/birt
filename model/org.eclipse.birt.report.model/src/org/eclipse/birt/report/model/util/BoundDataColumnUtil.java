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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.template.TemplateParser;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.core.template.TextTemplate.ExpressionValueNode;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.DataColumnNameValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * The utility class for bound data columns migration.
 * <p>
 * Parts of migration work from BIRT 2.1M5 to BIRT 2.1.0 for bound data columns.
 */

public class BoundDataColumnUtil {

	/**
	 * Visits the content as a template visitor. Updated the expression for
	 * <value-of> and <image> nodes.
	 * <p>
	 * Parts of backward compatiblility work for the Text Item from BIRT 2.1M5 to
	 * BIRT 2.1.0.
	 */

	public static class ContentVisitor implements TextTemplate.Visitor {

		private static final String VALUE_OF_START_TAG = "<value-of>"; //$NON-NLS-1$
		private static final String VALUE_OF_END_TAG = "</value-of>"; //$NON-NLS-1$
		private static final String IMAGE_START_TAG = "<image>"; //$NON-NLS-1$
		private static final String IMAGE_END_TAG = "</image>";//$NON-NLS-1$

		private StringBuffer buffer;
		private Map<Object, String> updatedValues;
		private TextTemplate template;

		/**
		 * Default constructor.
		 * 
		 * @param template
		 * @param updatedValues
		 */

		public ContentVisitor(TextTemplate template, Map<Object, String> updatedValues) {
			this.updatedValues = updatedValues;
			this.template = template;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.core.template.TextTemplate.Visitor#visitText(org
		 * .eclipse.birt.core.template.TextTemplate.TextNode, java.lang.Object)
		 */

		public Object visitText(TextTemplate.TextNode node, Object value) {
			if (value != null)
				buffer.append(value);
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.core.template.TextTemplate.Visitor#visitValue(org
		 * .eclipse.birt.core.template.TextTemplate.ValueNode, java.lang.Object)
		 */

		public Object visitValue(TextTemplate.ValueNode node, Object value) {

			String updatedValue = updatedValues.get(value);
			if (updatedValue != null) {
				buffer.append(VALUE_OF_START_TAG + updatedValue + VALUE_OF_END_TAG);
				return updatedValue;
			}

			return value;
		}

		public Object visitExpressionValue(ExpressionValueNode node, Object value) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.core.template.TextTemplate.Visitor#visitImage(org
		 * .eclipse.birt.core.template.TextTemplate.ImageNode, java.lang.Object)
		 */

		public Object visitImage(TextTemplate.ImageNode node, Object value) {
			String updatedValue = updatedValues.get(value);
			if (updatedValue != null) {
				buffer.append(IMAGE_START_TAG + updatedValue + IMAGE_END_TAG);
				return updatedValue;
			}

			return value;
		}

		/**
		 * Runs the visitor.
		 * 
		 * @return the updated content. Expressions in <value-of> and <image> nodes are
		 *         updated.
		 */

		public String execute() {
			if (template == null) {
				return ""; //$NON-NLS-1$
			}

			buffer = new StringBuffer();
			ArrayList nodes = template.getNodes();
			Iterator iter = nodes.iterator();
			while (iter.hasNext()) {
				TextTemplate.Node node = (TextTemplate.Node) iter.next();

				String text = null;

				if (node instanceof TextTemplate.ValueNode) {
					text = ((TextTemplate.ValueNode) node).getValue();
				} else if (node instanceof TextTemplate.ImageNode) {
					text = ((TextTemplate.ImageNode) node).getExpr();
				} else if (node instanceof TextTemplate.TextNode) {
					text = ((TextTemplate.TextNode) node).getContent();
				}

				node.accept(this, text);
			}

			return buffer.toString();
		}
	}

	/**
	 * Gets the column name with the given expression bound the given list.
	 * 
	 * @param columns    the binding columns
	 * @param expression the old value expression in BIRT 2.1M5
	 * @return the bound column name
	 */

	public static String getColumnName(List<ComputedColumn> columns, String expression) {
		if ((columns == null) || (columns.size() == 0) || expression == null)
			return null;

		for (int i = 0; i < columns.size(); i++) {
			ComputedColumn column = columns.get(i);
			if (expression.equals(column.getExpression()))
				return column.getName();
		}
		return null;
	}

	private static boolean equals(String orginal, String dest, boolean IgnoreCase) {
		if (IgnoreCase) {
			if (orginal != null && orginal.equalsIgnoreCase(dest) || (orginal == null && dest == null)) {
				return true;
			}
			return false;
		} else {
			if (orginal != null && orginal.equals(dest) || (orginal == null && dest == null)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * Gets the column with the given expression and aggregateOn value bound the
	 * given list.
	 * 
	 * @param columns   the binding columns.
	 * @param addColumn the added column.
	 * @return the bound column.
	 */
	public static ComputedColumn getColumn(List<ComputedColumn> columns, ComputedColumn addColumn) {
		if ((columns == null) || (columns.size() == 0))
			return null;

		assert addColumn != null;

		String expression = addColumn.getExpression();
		String function = addColumn.getAggregateFunction();
		String filterExpression = addColumn.getFilterExpression();
		String calculationType = addColumn.getStringProperty(null, ComputedColumn.CALCULATION_TYPE_MEMBER);
		String refDataType = addColumn.getStringProperty(null, ComputedColumn.REFERENCE_DATE_TYPE_MEMBER);
		String timeDimension = addColumn.getStringProperty(null, ComputedColumn.TIME_DIMENSION_MEMBER);
		List<String> aggregateOnList = addColumn.getAggregateOnList();
		String dataType = addColumn.getDataType();

		for (int i = 0; i < columns.size(); i++) {
			ComputedColumn column = columns.get(i);
			String tmpExpression = column.getExpression();
			String tmpFunction = column.getAggregateFunction();
			String tmpFilterExpression = column.getFilterExpression();
			String tmpType = column.getDataType();
			String tmpCalculationType = column.getStringProperty(null, ComputedColumn.CALCULATION_TYPE_MEMBER);
			String tmpRefDataType = column.getStringProperty(null, ComputedColumn.REFERENCE_DATE_TYPE_MEMBER);
			String tmpTimeDimension = column.getStringProperty(null, ComputedColumn.TIME_DIMENSION_MEMBER);

			if (equals(expression, tmpExpression, false) && equals(function, tmpFunction, false)
					&& equals(calculationType, tmpCalculationType, false)
					&& equals(filterExpression, tmpFilterExpression, false)
					&& equals(refDataType, tmpRefDataType, false) && equals(timeDimension, tmpTimeDimension, false)
					&& equals(dataType, tmpType, true)) {
				List tempAggregateOnList = column.getAggregateOnList();
				boolean isEmptyA = aggregateOnList == null || aggregateOnList.isEmpty();
				boolean isEmptyB = tempAggregateOnList == null || tempAggregateOnList.isEmpty();

				// if two is empty list, return this
				if (isEmptyA && isEmptyB)
					return column;
				// if one is empty and other is not, then continue and do the
				// next search
				if ((!isEmptyA && isEmptyB) || (isEmptyA && !isEmptyB))
					continue;

				assert tempAggregateOnList != null && aggregateOnList != null;
				if (tempAggregateOnList.size() != aggregateOnList.size())
					continue;

				// search all the aggregation on is matched.
				boolean isMatch = true;
				for (int j = 0; j < tempAggregateOnList.size(); j++) {
					String aggregationA = (String) tempAggregateOnList.get(j);
					String aggregationB = aggregateOnList.get(j);
					if (!aggregateOnList.contains(aggregationA) || !tempAggregateOnList.contains(aggregationB)) {
						isMatch = false;
						break;
					}
				}
				if (isMatch)
					return column;
			}
		}
		return null;
	}

	/**
	 * Creates a data binding on the target element.
	 * 
	 * @param target     the element
	 * @param columnName the column binding name
	 * @param expression the column binding expression
	 * @param module     the root of the target
	 * 
	 * @return the column binding name
	 */

	public static String createBoundDataColumn(DesignElement target, String columnName, String expression,
			Module module) {
		if (target == null)
			return null;

		String propName = null;
		if (target instanceof ReportItem) {
			propName = IReportItemModel.BOUND_DATA_COLUMNS_PROP;

		}

		if (target instanceof ScalarParameter)
			propName = IScalarParameterModel.BOUND_DATA_COLUMNS_PROP;

		ElementPropertyDefn prop = (ElementPropertyDefn) target.getDefn().getProperty(propName);
		if (prop == null)
			return null;
		List columns = (List) target.getLocalProperty(module, prop);

		if (columns == null) {
			columns = new ArrayList();
			target.setProperty(propName, columns);
		}

		String newName = columnName;

		String foundName = getColumnName(columns, expression);
		if ((foundName == null) && (DataColumnNameValidator.getColumn(columns, newName) == null)) {
			ComputedColumn column = StructureFactory.createComputedColumn();
			// can not call tmpList.add(column) to insert this column to
			// list, must call structureContext to add it; otherwise the
			// column will not set up the structure context
			new StructureContext(target, prop, null).add(column);

			column.setName(newName);
			column.setExpression(expression);
		} else if (foundName != null)
			newName = foundName;

		return newName;
	}

	/**
	 * Returns the nearest container or the element self if there is a not
	 * <code>null</code> dataSet property value.
	 * 
	 * @param element the element
	 * @param module  the root of the element
	 * @return the element has the dataSet value or <code>null</code> when not
	 *         found.
	 */

	public static DesignElement findTargetOfBoundColumns(DesignElement element, Module module) {
		return findTargetOfBoundColumns(element, module, 0);
	}

	/**
	 * Returns the outer listing/extended container or the element self if there is
	 * a not <code>null</code> dataSet property value. This is not a strict match.
	 * If the outer level is not found, the outer matchest element is returned.
	 * 
	 * @param element    the element
	 * @param module     the root of the element
	 * @param outerLevel the 0-based outer level. If it is 0, means find the nearest
	 *                   listing/extended item container.
	 * 
	 * @return the element has the dataSet value or <code>null</code> when not
	 *         found.
	 */

	public static DesignElement findTargetOfBoundColumns(DesignElement element, Module module, int outerLevel) {
		// to consider self. The initial value should be -1. Not 0.

		int tmpOuterLevel = -1;

		DesignElement tmpElement = element;
		DesignElement retElement = null;

		while (tmpElement != null && tmpOuterLevel < outerLevel) {
			if (!(tmpElement instanceof GroupElement || tmpElement instanceof ReportItem
					|| tmpElement instanceof ScalarParameter)) {
				tmpElement = tmpElement.getContainer();
				continue;
			}

			if (tmpElement instanceof ReportItem) {
				String propName = IReportItemModel.BOUND_DATA_COLUMNS_PROP;
				ElementPropertyDefn prop = (ElementPropertyDefn) tmpElement.getDefn().getProperty(propName);
				if (prop == null) {
					tmpElement = tmpElement.getContainer();
					continue;
				}
			}

			if (retElement == null)
				retElement = tmpElement;

			if (tmpElement instanceof ListingElement || tmpElement instanceof GroupElement
					|| tmpElement instanceof ExtendedItem) {
				retElement = tmpElement;
				tmpOuterLevel++;

				tmpElement = tmpElement.getContainer();
				continue;
			}

			if (tmpElement instanceof ReportItem) {
				ElementRefValue dataSetRef = (ElementRefValue) tmpElement.getProperty(module,
						IReportItemModel.DATA_SET_PROP);
				if (dataSetRef != null) {
					retElement = tmpElement;
					tmpOuterLevel++;

					tmpElement = tmpElement.getContainer();
					continue;
				}
			}

			tmpElement = tmpElement.getContainer();
		}

		return retElement;
	}

	/**
	 * Returns the nearest outer data container. This is a strict match. If the
	 * outer level is not found, return <code>null</code>.
	 * 
	 * @param element the element
	 * @param module  the root of the element
	 * 
	 * @return the element has the dataSet value or <code>null</code> when not
	 *         found.
	 */

	public static DesignElement findTargetElementOfParamBinding(DesignElement element, Module module) {
		DesignElement outer1 = findTargetOfBoundColumns(element, module, 0);
		DesignElement outer2 = findTargetOfBoundColumns(outer1, module, 1);
		if (outer1 == outer2)
			return null;

		return outer2;
	}

	/**
	 * Creates a unique column name. If report item has column binding or data set,
	 * The column name is unique in the scope of report item. If report item hasn't
	 * column binding or data set, The column name is unique in the scope of it's
	 * listing container.
	 * 
	 * @param element the element
	 * @param name    the default column binding name
	 * @param struct  the struct to get the unique name
	 * @param columns the bound columns in the binding data
	 * @return the newly created name
	 */

	public static String makeUniqueName(DesignElementHandle element, String name, ComputedColumn struct) {
		// Check value of itself.

		Set<ComputedColumn> columnNames = new HashSet<ComputedColumn>();

		if (element instanceof ListingHandle) {
			addColumnNamesToSet(element, columnNames);
		} else if (element instanceof ReportItemHandle) {
			// If report item has column binding or data set, The column name is
			// unique in the scope of report item. If
			// report item hasn't column binding or data set, The column name is
			// unique in the scope of it's listing container.

			if (((ReportItemHandle) element).getDataSet() != null
					|| element.getProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP) != null) {
				addColumnNamesToSet(element, columnNames);
			} else {
				DesignElementHandle tmpHandle = element;
				while (true) {
					tmpHandle = tmpHandle.getContainer();
					if (tmpHandle == null)
						break;
					if (tmpHandle instanceof ListingHandle) {
						addColumnNamesToSet(tmpHandle, columnNames);
						break;
					}
				}
			}
		}

		int index = 1;

		String trimmedName = name.trim();
		String retName = trimmedName;

		List<ComputedColumn> columns = new ArrayList<ComputedColumn>();
		columns.addAll(columnNames);

		while (true) {
			ComputedColumn column = DataColumnNameValidator.getColumn(columns, retName);
			if (column == null || column == struct)
				break;

			retName = trimmedName + '_' + index;
			index++;
		}

		return retName;
	}

	/**
	 * Adds bound column names of the given element to the given set.
	 * 
	 * @param element     the element
	 * @param module      the module
	 * @param columnNames the set holding column names
	 */

	private static void addColumnNamesToSet(DesignElementHandle element, Set<ComputedColumn> columnNames) {
		if (element == null)
			return;

		String propName = null;

		if (element instanceof ReportItemHandle)
			propName = IReportItemModel.BOUND_DATA_COLUMNS_PROP;
		else if (element instanceof ScalarParameterHandle)
			propName = IScalarParameterModel.BOUND_DATA_COLUMNS_PROP;
		else
			return;
		List boundColumns = element.getListProperty(propName);

		if (boundColumns == null || boundColumns.isEmpty())
			return;

		columnNames.addAll(boundColumns);
	}

	/**
	 * Does backward compatiblility work for the extended item from BIRT 2.1M5 to
	 * BIRT 2.1.0.
	 * 
	 * @param jsExprs      the expression from the extended item.
	 * @param element      the extended item
	 * @param module       the root of the extended item
	 * @param cachedGroups the map to cache group and its bound columns
	 * 
	 * @return a map containing updated expressions.
	 */

	public static Map<String, String> handleJavaExpression(List<String> jsExprs, ExtendedItem element, Module module,
			Map<Object, Object> cachedGroups) {
		Map<String, String> retMap = new HashMap<String, String>();

		if (jsExprs == null || jsExprs.isEmpty())
			return retMap;

		for (int i = 0; i < jsExprs.size(); i++) {
			String jsExpr = jsExprs.get(i);

			List<IColumnBinding> newExprs = null;

			try {
				newExprs = ExpressionUtil.extractColumnExpressions(jsExpr);
			} catch (BirtException e) {

			}

			if (newExprs == null || newExprs.isEmpty())
				continue;

			DesignElement tmpElement = BoundDataColumnUtil.findTargetOfBoundColumns(element, module);

			for (int j = 0; j < newExprs.size(); j++) {
				IColumnBinding boundColumn = newExprs.get(j);

				String columnName = boundColumn.getResultSetColumnName();

				if (tmpElement != null) {
					String tmpName = BoundDataColumnUtil.createBoundDataColumn(tmpElement, columnName,
							boundColumn.getBoundExpression(), module);
					if (tmpName != null)
						columnName = tmpName;
				}

				retMap.put(jsExpr, ExpressionUtil.createRowExpression(columnName));
			}
		}

		return retMap;
	}

	/**
	 * Does backward compatibility work for the text item from BIRT 2.1M5 to BIRT
	 * 2.1.0.
	 * <p>
	 * Parts of backward compatibility work for the Text Item from BIRT 2.1M5 to
	 * BIRT 2.1.0.
	 * 
	 * @param jsExprs      the expression from the extended item.
	 * @param element      the text item
	 * @param module       the root of the text item
	 * @param cachedGroups the map to cache group and its bound columns
	 * @return a map containing updated expressions
	 */

	public static Map handleJavaExpression(List jsExprs, TextItem element, Module module, Map cachedGroups) {
		Map retMap = new HashMap();

		Iterator exprsIter = jsExprs.iterator();

		while (exprsIter.hasNext()) {
			String jsExpr = (String) exprsIter.next();

			List newExprs = null;
			try {
				newExprs = ExpressionUtil.extractColumnExpressions(jsExpr);
			} catch (BirtException e) {

			}

			if (newExprs == null || newExprs.isEmpty())
				continue;

			DesignElement tmpElement = BoundDataColumnUtil.findTargetOfBoundColumns(element, module);
			if (tmpElement instanceof GroupElement && cachedGroups == null) {
				tmpElement = tmpElement.getContainer();
			}

			if (tmpElement instanceof GroupElement) {
				assert cachedGroups != null;

				appendBoundColumnsToGroup((GroupElement) tmpElement, newExprs, cachedGroups);
				for (int j = 0; j < newExprs.size(); j++) {
					IColumnBinding boundColumn = (IColumnBinding) newExprs.get(j);
					String columnName = boundColumn.getResultSetColumnName();

					retMap.put(jsExpr, ExpressionUtil.createRowExpression(columnName));
				}
				return retMap;
			}

			for (int j = 0; j < newExprs.size(); j++) {
				IColumnBinding boundColumn = (IColumnBinding) newExprs.get(j);
				String columnName = boundColumn.getResultSetColumnName();

				String tmpName = BoundDataColumnUtil.createBoundDataColumn(tmpElement, columnName,
						boundColumn.getBoundExpression(), module);
				if (tmpName != null)
					columnName = tmpName;

				retMap.put(jsExpr, ExpressionUtil.createRowExpression(columnName));
			}
		}

		return retMap;
	}

	/**
	 * Returns expressions in the given content text. The text has expressions only
	 * when its type is html or auto.
	 * <p>
	 * Parts of backward compatiblility work for the Text Item from BIRT 2.1M5 to
	 * BIRT 2.1.0.
	 * 
	 * @param contentText the text to check
	 * @param element     the text item
	 * @param module      the root of the text item
	 * @return a list containing expressions.
	 */

	public static List getExpressions(String contentText, TextItem element, Module module) {
		if (contentText == null)
			return null;

		List exprs = new ArrayList();
		String contentType = (String) element.getProperty(module, ITextItemModel.CONTENT_TYPE_PROP);

		if (DesignChoiceConstants.TEXT_CONTENT_TYPE_AUTO.equalsIgnoreCase(contentType)
				|| (DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML.equalsIgnoreCase(contentType))) {

			TextTemplate template = new TemplateParser().parse(contentText);
			if (template != null && template.getNodes() != null) {
				Iterator itor = template.getNodes().iterator();
				Object obj;
				String expression = null;
				while (itor.hasNext()) {
					obj = itor.next();
					if (obj instanceof TextTemplate.ValueNode) {
						expression = ((TextTemplate.ValueNode) obj).getValue();
					} else if (obj instanceof TextTemplate.ImageNode) {
						expression = ((TextTemplate.ImageNode) obj).getExpr();
					}

					if (!StringUtil.isBlank(expression) && !exprs.contains(expression)) {
						exprs.add(expression);
						expression = null;
					}
				}
			}
		}

		return exprs;
	}

	/**
	 * Appends to the cached group bound columns. Becuase of "aggregateOn" property
	 * on bound columns, has to add bound columns at end() function of
	 * ListingElementState.
	 * 
	 * @param target     the group element
	 * @param newExprs   bound columns returned by ExpressionUtil
	 * @param tempValues the map to cache group and its bound columns
	 */

	public static void appendBoundColumnsToGroup(GroupElement target, List newExprs, Map tempValues) {
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

		appendBoundColumnsToCachedGroup(target, newColumns, tempValues);
	}

	/**
	 * Appends to the cached group bound columns. because of "aggregateOn" property
	 * on bound columns, has to add bound columns at end() function of
	 * ListingElementState.
	 * 
	 * @param target   the group element
	 * @param newExprs bound columns returned by ExpressionUtil
	 */

	private static void appendBoundColumnsToCachedGroup(GroupElement target, List newColumns, Map tempValues) {
		List boundColumns = (List) tempValues.get(target);
		if (boundColumns == null) {
			tempValues.put(target, newColumns);
			return;
		}

		for (int i = 0; i < newColumns.size(); i++) {
			ComputedColumn column = (ComputedColumn) newColumns.get(i);
			boundColumns.add(column);
		}

	}

	/**
	 * Appends to the cached group bound columns. because of "aggregateOn" property
	 * on bound columns, has to add bound columns at end() function of
	 * ListingElementState.
	 * 
	 * @param target     the group element
	 * @param boundName  the bound column name
	 * @param expression the bound column expression
	 * @param tempValues the map to cache group and its bound columns
	 * @return the return bound name
	 */

	public static String appendBoundColumnsToCachedGroup(GroupElement target, String boundName, String expression,
			Map tempValues) {
		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName(boundName);
		column.setExpression(expression);

		List boundColumns = (List) tempValues.get(target);
		if (boundColumns == null) {
			List newColumns = new ArrayList();
			newColumns.add(column);

			tempValues.put(target, newColumns);
			return boundName;
		}

		boundColumns.add(column);

		return boundName;
	}
}
