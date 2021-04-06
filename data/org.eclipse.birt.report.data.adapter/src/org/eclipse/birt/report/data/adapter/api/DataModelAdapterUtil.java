package org.eclipse.birt.report.data.adapter.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

public class DataModelAdapterUtil {
	public static boolean isAggregationBinding(ComputedColumnHandle computedColumnHandle, ReportItemHandle itemHandle) {
		if (computedColumnHandle.getAggregateFunction() != null) {
			return true;
		}

		try {
			Iterator iter = itemHandle.getAvailableBindings();
			Set result = new HashSet();
			Map<String, String> bindingExpressionMap = new HashMap<String, String>();
			populateDirectAggregationBindingNames(result, iter, bindingExpressionMap);
			DataSetHandle dataSetHandle = itemHandle.getDataSet();
			if (dataSetHandle != null && dataSetHandle.computedColumnsIterator() != null
					&& dataSetHandle.computedColumnsIterator().hasNext()) {
				iter = dataSetHandle.computedColumnsIterator();
				populateDirectAggregationBindingNames(result, iter, bindingExpressionMap);
			}
			String expression = bindingExpressionMap.get(computedColumnHandle.getName());
			if (expression != null) {
				return indirectRefAggregation(expression, result, bindingExpressionMap);
			}
		} catch (BirtException ex) {

		}
		return false;
	}

	private static boolean indirectRefAggregation(String expression, Set directNames,
			Map<String, String> expressionMap) {
		HashSet<String> names = getBindingRefNames(expression);
		for (String name : names) {
			if (directNames.contains(name)) {
				return true;
			}
		}

		names = getBindingRefColumnNames(expression);
		for (String name : names) {
			if (directNames.contains(name)) {
				return true;
			} else {
				String expressionName = expressionMap.get(name);
				if (expressionName == null) {
					return false;
				} else {
					return indirectRefAggregation(expressionName, directNames, expressionMap);
				}
			}
		}
		return false;
	}

	private static HashSet<String> getBindingRefColumnNames(String expression) {
		HashSet<String> result = new HashSet<String>();

		try {
			List columnList = ExpressionUtil.extractColumnExpressions(expression, ExpressionUtil.ROW_INDICATOR);

			for (int i = 0; i < columnList.size(); i++) {
				if (columnList.get(i) instanceof IColumnBinding) {
					result.add(((IColumnBinding) columnList.get(i)).getResultSetColumnName());
				}
			}
		} catch (BirtException e) {
		}

		return result;
	}

	private static HashSet<String> getBindingRefNames(String expression) {
		HashSet<String> result = new HashSet<String>();
		try {
			List columnList = ExpressionUtil.extractColumnExpressions(expression, ExpressionUtil.DATASET_ROW_INDICATOR);

			for (int i = 0; i < columnList.size(); i++) {
				if (columnList.get(i) instanceof IColumnBinding) {
					result.add(((IColumnBinding) columnList.get(i)).getResultSetColumnName());
				}
			}
		} catch (BirtException e) {
		}

		return result;
	}

	private static void populateDirectAggregationBindingNames(Set aggregationBinding, Iterator iter,
			Map<String, String> bindingExpressionMap) throws BirtException {
		while (iter.hasNext()) {
			ComputedColumnHandle computedHandle = (ComputedColumnHandle) iter.next();

			ComputedColumn column = (ComputedColumn) computedHandle.getStructure();
			String expressionString = column.getExpression();
			bindingExpressionMap.put(computedHandle.getName(), expressionString);
			if (computedHandle.getAggregateFunction() != null) {
				String columnName = null;
				columnName = ExpressionUtil.getColumnName(expressionString);
				if (columnName == null) {
					columnName = ExpressionUtil.getColumnBindingName(expressionString);
					if (columnName != null)
						aggregationBinding.add(computedHandle.getName());
				} else {
					aggregationBinding.add(computedHandle.getName());
				}
			}
		}
	}

	public static DataModelAdapterStatus validateRelativeTimePeriod(ReportItemHandle reportItemHandle,
			ComputedColumnHandle computedColumnHandle) {
		return new DataModelAdapterStatus(DataModelAdapterStatus.Status.SUCCESS, "");
	}
}
