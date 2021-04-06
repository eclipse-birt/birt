/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

/**
 * A utility function The toString method dump all non-empty fields of the given
 * object
 *
 * @since 4.8
 */
public abstract class BaseLogUtil {

	public static String toString(Object source) {
		if (source instanceof Collection) {
			return toString_Collection(source);
		} else if (source instanceof FilterDefinition) {
			return toString_FilterDefinition(source);
		} else if (source instanceof ConditionalExpression) {
			return toString_ConditionalExpression(source);
		} else if (source instanceof ScriptExpression) {
			return toString_ScriptExpression(source);
		} else if (source instanceof InputParameterBinding) {
			return toString_InputParameterBinding(source);
		} else if (source instanceof GroupDefinition) {
			return toString_GroupDefinition(source);
		} else if (source instanceof SortDefinition) {
			return toString_SortDefinition(source);
		} else if (source instanceof QueryDefinition) {
			return toString_QueryDefinition(source);
		} else if (source instanceof OdaDataSetDesign) {
			return toString_OdaDataSetDesign(source);
		} else if (source instanceof OdaDataSourceDesign) {
			return toString_OdaDataSourceDesign(source);
		} else if (source == null)
			return "null";
		else
			return source.toString();
	}

	private static String toString_Collection(Object source) {
		StringBuffer stringBuffer = new StringBuffer();
		Iterator iterator = ((Collection) source).iterator();
		while (iterator.hasNext()) {
			stringBuffer.append(toString(iterator.next()) + ", \r\n\t");
		}
		String str = stringBuffer.toString();
		if (str.endsWith("\t")) {
			str = str.substring(0, str.length() - 5);
		}
		return str;
	}

	private static String toString_FilterDefinition(Object source) {
		return "FilterDefinition(" + toString(((FilterDefinition) source).getExpression()) + ")";
	}

	private static String toString_ConditionalExpression(Object source) {
		StringBuffer stringBuffer = new StringBuffer("ConditionalExpression(");
		ConditionalExpression conditionalExpression = (ConditionalExpression) source;
		stringBuffer.append("Operator : " + conditionalExpression.getOperator() + ", ");
		stringBuffer.append("Expression : " + toString(conditionalExpression.getExpression()) + ", ");
		if (!isEmpty(conditionalExpression.getOperand1()))
			stringBuffer.append("Operand1 : " + toString(conditionalExpression.getOperand1()) + ", ");

		if (!isEmpty(conditionalExpression.getOperand2()))
			stringBuffer.append("Operand2 : " + toString(conditionalExpression.getOperand2()) + ")");

		return stringBuffer.toString();
	}

	private static String toString_ScriptExpression(Object source) {
		return "ScriptExpression(Text:" + ((ScriptExpression) source).getText() + ")";
	}

	private static String toString_InputParameterBinding(Object source) {
		InputParameterBinding inputParameterBinding = (InputParameterBinding) source;
		StringBuffer stringBuffer = new StringBuffer("InputParameterBinding(");
		if (!isEmpty(inputParameterBinding.getName()))
			stringBuffer.append("Name : " + inputParameterBinding.getName() + ", ");

		stringBuffer.append("Position : " + inputParameterBinding.getPosition() + ", ");
		stringBuffer.append("Expression : " + toString(inputParameterBinding.getExpr()));
		stringBuffer.append(")");
		return stringBuffer.toString();
	}

	private static String toString_SortDefinition(Object source) {
		SortDefinition sort = (SortDefinition) source;
		StringBuffer stringBuffer = new StringBuffer("SortDefinition(");
		if (!isEmpty(sort.getColumn()))
			stringBuffer.append("Column : " + sort.getColumn() + ", ");

		if (!isEmpty(sort.getExpression()))
			stringBuffer.append("getExpression : " + sort.getExpression() + ", ");

		stringBuffer.append("SortDirection : " + sort.getSortDirection() + ",");

		stringBuffer.append("SortStrength : " + sort.getSortStrength() + ",");

		stringBuffer.append("SortLocale : " + sort.getSortLocale() + ")");

		return stringBuffer.toString();
	}

	private static String toString_GroupDefinition(Object source) {
		GroupDefinition group = (GroupDefinition) source;
		StringBuffer stringBuffer = new StringBuffer("GroupDefinition(");
		if (!isEmpty(group.getName()))
			stringBuffer.append("Name : " + group.getName() + ", ");

		if (!isEmpty(group.getKeyColumn()))
			stringBuffer.append("KeyColumn : " + group.getKeyColumn() + ", ");

		if (!isEmpty(group.getKeyExpression()))
			stringBuffer.append("KeyExpression : " + group.getKeyExpression() + ", ");

		stringBuffer.append("SortDirection : " + group.getSortDirection() + ", ");

		stringBuffer.append("Interval : " + group.getInterval() + ", ");
		stringBuffer.append("IntervalRange : " + group.getIntervalRange() + ", ");

		if (!isEmpty(group.getIntervalStart()))
			stringBuffer.append("IntervalStart : " + group.getIntervalStart() + ", ");

		if (!isEmpty(group.getSubqueries()))
			stringBuffer.append("Subqueries : " + toString(group.getSubqueries()) + ", ");

		if (!isEmpty(group.getSorts()))
			stringBuffer.append("Sorts : " + toString(group.getSorts()) + ", ");

		if (!isEmpty(group.getFilters()))
			stringBuffer.append("Filters : " + toString(group.getFilters()));
		stringBuffer.append(")");
		return stringBuffer.toString();
	}

	private static String toString_QueryDefinition(Object source) {
		QueryDefinition querySpec = (QueryDefinition) source;
		StringBuffer stringBuffer = new StringBuffer("QueryDefinition(");
		stringBuffer.append("DataSetName : " + querySpec.getDataSetName() + "\r\n");
		if (!isEmpty(querySpec.getBindings()))
			stringBuffer.append("ResultSetExpressions : " + BaseLogUtil.toString(querySpec.getBindings()) + "\r\n");

		if (!isEmpty(querySpec.getParentQuery()))
			stringBuffer.append("ParentQuery : " + BaseLogUtil.toString(querySpec.getParentQuery()) + "\r\n");

		if (!isEmpty(querySpec.getSubqueries()))
			stringBuffer.append("Subqueries : " + BaseLogUtil.toString(querySpec.getSubqueries()) + "\r\n");

		stringBuffer.append("MaxRows : " + querySpec.getMaxRows() + "\r\n");

		if (!isEmpty(querySpec.getColumnProjection())) {
			for (int i = 0; i < querySpec.getColumnProjection().length; i++)
				stringBuffer.append("ColumnProjection : " + querySpec.getColumnProjection()[i] + "   ");
			stringBuffer.append("\r\n");
		}

		if (!isEmpty(querySpec.getGroups()))
			stringBuffer.append("Groups : " + BaseLogUtil.toString(querySpec.getGroups()) + "\r\n");

		if (!isEmpty(querySpec.getFilters()))
			stringBuffer.append("Filters : " + BaseLogUtil.toString(querySpec.getFilters()) + "\r\n");

		if (!isEmpty(querySpec.getSorts()))
			stringBuffer.append("Sorts : " + BaseLogUtil.toString(querySpec.getSorts()) + "\r\n");

		if (!isEmpty(querySpec.getInputParamBindings()))
			stringBuffer.append(
					"InputParamBindings : " + BaseLogUtil.toString(querySpec.getInputParamBindings()) + ")\r\n");

		return stringBuffer.toString();

	}

	private static String toString_OdaDataSetDesign(Object source) {
		OdaDataSetDesign dataSet = (OdaDataSetDesign) source;
		StringBuffer stringBuffer = new StringBuffer("OdaDataSetDesign(");
		// BaseDataSetDesign
		if (!isEmpty(dataSet.getName()))
			stringBuffer.append("Name : " + dataSet.getName() + "\r\n");

		if (!isEmpty(dataSet.getDataSourceName()))
			stringBuffer.append("DataSourceName : " + dataSet.getDataSourceName() + "\r\n");

		if (!isEmpty(dataSet.getAfterCloseScript()))
			stringBuffer.append("AfterCloseScript : " + dataSet.getAfterCloseScript() + "\r\n");

		if (!isEmpty(dataSet.getAfterOpenScript()))
			stringBuffer.append("AfterOpenScript : " + dataSet.getAfterOpenScript() + "\r\n");

		if (!isEmpty(dataSet.getBeforeCloseScript()))
			stringBuffer.append("BeforeCloseScript : " + dataSet.getBeforeCloseScript() + "\r\n");

		if (!isEmpty(dataSet.getBeforeOpenScript()))
			stringBuffer.append("BeforeOpenScript : " + dataSet.getBeforeOpenScript() + "\r\n");

		if (!isEmpty(dataSet.getOnFetchScript()))
			stringBuffer.append("OnFetchScript : " + dataSet.getOnFetchScript() + "\r\n");

		if (!isEmpty(dataSet.getComputedColumns()))
			stringBuffer.append("ComputedColumns : " + toString(dataSet.getComputedColumns()) + "\r\n");

		if (!isEmpty(dataSet.getFilters()))
			stringBuffer.append("Filters : " + toString(dataSet.getFilters()) + "\r\n");

		if (!isEmpty(dataSet.getParameters()))
			stringBuffer.append("Parameters : " + toString(dataSet.getParameters()) + "\r\n");

		if (!isEmpty(dataSet.getInputParamBindings()))
			stringBuffer.append("InputParamBindings : " + toString(dataSet.getInputParamBindings()) + "\r\n");

		if (!isEmpty(dataSet.getResultSetHints()))
			stringBuffer.append("ResultSetHints : " + toString(dataSet.getResultSetHints()) + "\r\n");
		// OdaDataSetDesign
		if (!isEmpty(dataSet.getExtensionID()))
			stringBuffer.append("ExtensionID : " + dataSet.getExtensionID() + "\r\n");

		if (!isEmpty(dataSet.getPrimaryResultSetName()))
			stringBuffer.append("PrimaryResultSetName : " + dataSet.getPrimaryResultSetName() + "\r\n");

		if (!isEmpty(dataSet.getQueryText()))
			stringBuffer.append("QueryText : " + dataSet.getQueryText() + "\r\n");

		if (!isEmpty(dataSet.getPrivateProperties()))
			stringBuffer.append("PrivateProperties : " + toString(dataSet.getPrivateProperties()) + "\r\n");

		if (!isEmpty(dataSet.getPublicProperties()))
			stringBuffer.append("PublicProperties : " + toString(dataSet.getPublicProperties()) + "\r\n");

		stringBuffer.append(")");
		return stringBuffer.toString();
	}

	private static String toString_OdaDataSourceDesign(Object source) {
		OdaDataSourceDesign dataSource = (OdaDataSourceDesign) source;
		StringBuffer stringBuffer = new StringBuffer("OdaDataSourceDesign(");
		// BaseDataSourceDesign
		if (!isEmpty(dataSource.getName()))
			stringBuffer.append("Name : " + dataSource.getName() + "\r\n");
		if (!isEmpty(dataSource.getAfterCloseScript()))
			stringBuffer.append("AfterCloseScript : " + dataSource.getAfterCloseScript() + "\r\n");
		if (!isEmpty(dataSource.getAfterOpenScript()))
			stringBuffer.append("AfterOpenScript : " + dataSource.getAfterOpenScript() + "\r\n");
		if (!isEmpty(dataSource.getBeforeCloseScript()))
			stringBuffer.append("BeforeCloseScript : " + dataSource.getBeforeCloseScript() + "\r\n");
		if (!isEmpty(dataSource.getBeforeOpenScript()))
			stringBuffer.append("BeforeOpenScript : " + dataSource.getBeforeOpenScript() + "\r\n");
		// OdaDataSourceDesign
		if (!isEmpty(dataSource.getExtensionID()))
			stringBuffer.append("ExtensionID : " + dataSource.getExtensionID() + "\r\n");
		if (!isEmpty(dataSource.getPrivateProperties()))
			stringBuffer.append("PrivateProperties : " + dataSource.getPrivateProperties() + "\r\n");
		if (!isEmpty(dataSource.getPublicProperties())) {
			Map publicProperties = dataSource.getPublicProperties();
			String logMsg = " PulicProperties : ";
			Iterator iterator = publicProperties.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String propName = entry.getKey().toString();
				// Don't log value of any property that looks like a password
				String lcPropName = propName.toLowerCase();
				String propVal;
				if (lcPropName.indexOf("password") >= 0 || lcPropName.indexOf("pwd") >= 0)
					propVal = "***";
				else
					propVal = entry.getValue().toString();
				logMsg += (propName + "=" + propVal + ";");
			}

			stringBuffer.append(logMsg + "\r\n");
		}
		stringBuffer.append(")");
		return stringBuffer.toString();
	}

	private static boolean isEmpty(Object source) {
		if (source == null || "".equals(toString(source)))
			return true;
		else
			return false;
	}
}