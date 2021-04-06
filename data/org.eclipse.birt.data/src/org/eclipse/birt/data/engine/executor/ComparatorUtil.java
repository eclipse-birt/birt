
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IJoinCondition;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;

/**
 * 
 */

public class ComparatorUtil {

	private final static int B_FALSE = 0;
	private final static int B_UNKNOWN = 1;
	private final static int B_TRUE = 2;

	/**
	 * Only for non-collection object
	 * 
	 * @param ob1
	 * @param ob2
	 * @return
	 */
	public static boolean isEqualObject(Object ob1, Object ob2) {
		if (ob1 == ob2)
			return true;
		else if (ob1 == null || ob2 == null)
			return false;

		return ob1.equals(ob2);
	}

	/**
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isEqualString(String str1, String str2) {
		return isEqualObject(str1, str2);
	}

	/**
	 * @param map1
	 * @param map2
	 * @return
	 */
	public static boolean isEqualProps(Map map1, Map map2) {
		if (map1 == map2) {
			return true;
		} else if (map1 == null || map2 == null) {
			if (map1 == null) {
				if (map2.size() != 0)
					return false;
				else
					return true;
			} else {
				if (map1.size() != 0)
					return false;
				else
					return true;
			}
		} else if (map1.keySet().size() != map2.keySet().size()) {
			return false;
		}

		Set set = map1.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry ob = (Map.Entry) it.next();
			Object value1 = ob.getValue();
			Object value2 = map2.get(ob.getKey());

			if (isEqualObject(value1, value2) == false)
				return false;
		}

		return true;
	}

	/**
	 * @param col1
	 * @param col2
	 * @return
	 */
	public static int isEqualBasicCol(Collection col1, Collection col2) {
		if (col1 == col2) {
			return B_TRUE;
		} else if (col1 == null || col2 == null) {
			if (col1 == null) {
				if (col2.size() == 0)
					return B_TRUE;
				else
					return B_FALSE;
			} else {
				if (col1.size() == 0)
					return B_TRUE;
				else
					return B_FALSE;
			}
		} else {
			if (col1.size() == col2.size())
				return B_UNKNOWN;
			else
				return B_FALSE;
		}
	}

	/**
	 * @param filter1
	 * @param filter2
	 * @return
	 */
	public static boolean isEqualFilters(List filters1, List filters2) {
		int i = isEqualBasicCol(filters1, filters2);
		if (i == B_TRUE) {
			return true;
		} else if (i == B_FALSE) {
			return false;
		}
		Iterator itr1 = filters1.iterator();
		Iterator itr2 = filters2.iterator();
		while (itr1.hasNext()) {
			IFilterDefinition fd1 = (IFilterDefinition) itr1.next();
			IFilterDefinition fd2 = (IFilterDefinition) itr2.next();
			if (!isEqualExpression(fd1.getExpression(), fd2.getExpression())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param be
	 * @param be2
	 * @return
	 */
	public static boolean isEqualExpression(IBaseExpression be, IBaseExpression be2) {
		if (be == be2)
			return true;
		else if (be == null || be2 == null)
			return false;

		if (be instanceof IScriptExpression && be2 instanceof IScriptExpression) {
			IScriptExpression se = (IScriptExpression) be;
			IScriptExpression se2 = (IScriptExpression) be2;
			return isEqualExpression2(se, se2);
		} else if (be instanceof IConditionalExpression && be2 instanceof IConditionalExpression) {
			IConditionalExpression ce = (IConditionalExpression) be;
			IConditionalExpression ce2 = (IConditionalExpression) be2;
			return ce.getDataType() == ce2.getDataType() && ce.getOperator() == ce2.getOperator()
					&& isEqualExpression2(ce.getExpression(), ce2.getExpression())
					&& isEqualExpression(ce.getOperand1(), ce2.getOperand1())
					&& isEqualExpression(ce.getOperand2(), ce2.getOperand2());
		} else if (be instanceof IExpressionCollection && be2 instanceof IExpressionCollection) {
			return be.getDataType() == be2.getDataType() && isEqualExpressionArray(
					((IExpressionCollection) be).getExpressions(), ((IExpressionCollection) be2).getExpressions());

		}
		return false;
	}

	/**
	 * @param se
	 * @param se2
	 * @return
	 */
	public static boolean isEqualExpression2(IScriptExpression se, IBaseExpression se2) {
		if (se == se2)
			return true;
		else if (se == null || se2 == null)
			return false;
		return se.getDataType() == se2.getDataType()
				&& isEqualString(((IScriptExpression) se).getText(), ((IScriptExpression) se2).getText());
	}

	/**
	 * 
	 * @param operands
	 * @param operands2
	 * @return
	 */
	public static boolean isEqualExpressionArray(Collection op1, Collection op2) {
		if (op1 == op2)
			return true;
		Object[] operands1 = op1.toArray();
		Object[] operands2 = op2.toArray();
		if (operands1.length != operands2.length)
			return false;
		for (int i = 0; i < operands1.length; i++) {
			if (!isEqualExpression((IBaseExpression) operands1[i], (IBaseExpression) operands2[i]))
				return false;
		}
		return true;
	}

	/**
	 * Very special for computed column, temp computed column can not be counted as
	 * the real computed column
	 * 
	 * @param computedCol1
	 * @param computedCol2
	 * @return
	 */
	public static boolean isEqualComputedColumns(List computedCol1, List computedCol2) {
		if (computedCol1 == computedCol2)
			return true;

		List newComputedCol1 = getRealComputedColumn(computedCol1);
		List newComputedCol2 = getRealComputedColumn(computedCol2);

		int basicCol = isEqualBasicCol(newComputedCol1, newComputedCol2);
		if (basicCol == B_TRUE)
			return true;
		else if (basicCol == B_FALSE)
			return false;

		Iterator it = newComputedCol1.iterator();
		Iterator it2 = newComputedCol2.iterator();
		while (it.hasNext()) {
			IComputedColumn cc = (IComputedColumn) it.next();
			IComputedColumn cc2 = (IComputedColumn) it2.next();
			if (isEqualComputedCol(cc, cc2) == false)
				return false;
		}

		return true;
	}

	/**
	 * @param computedCols
	 * @return
	 */
	public static List<IComputedColumn> getRealComputedColumn(List computedCols) {
		if (computedCols == null)
			return null;

		List<IComputedColumn> list = new ArrayList<IComputedColumn>();
		for (int i = 0; i < computedCols.size(); i++) {
			IComputedColumn cc = (IComputedColumn) computedCols.get(i);
			if (cc.getName().matches("\\Q_{$TEMP_GROUP_\\E\\d*\\Q$}_\\E")
					|| cc.getName().matches("\\Q_{$TEMP_SORT_\\E\\d*\\Q$}_\\E")
					|| cc.getName().matches("\\Q_{$TEMP_FILTER_\\E\\d*\\Q$}_\\E"))
				continue;
			else
				list.add(cc);
		}

		return list;
	}

	/**
	 * @param cc
	 * @param cc2
	 * @return
	 */
	public static boolean isEqualComputedCol(IComputedColumn cc, IComputedColumn cc2) {
		return cc.getDataType() == cc2.getDataType() && isEqualString(cc.getName(), cc2.getName())
				&& isEqualExpression(cc.getExpression(), cc2.getExpression());
	}

	/**
	 * @param params1
	 * @param params2
	 * @return
	 */
	public static boolean isEqualParameters(List params1, List params2) {
		if (params1 == params2)
			return true;

		int basicCol = isEqualBasicCol(params1, params2);
		if (basicCol == B_TRUE)
			return true;
		else if (basicCol == B_FALSE)
			return false;

		Iterator it = params1.iterator();
		Iterator it2 = params2.iterator();
		while (it.hasNext()) {
			IParameterDefinition pd = (IParameterDefinition) it.next();
			IParameterDefinition pd2 = (IParameterDefinition) it2.next();
			if (isEqualParameter(pd, pd2) == false)
				return false;
		}

		return true;
	}

	/**
	 * @param pd
	 * @param pd2
	 * @return
	 */
	public static boolean isEqualParameter(IParameterDefinition pd, IParameterDefinition pd2) {
		return pd.getPosition() == pd2.getPosition() && pd.getType() == pd2.getType()
				&& pd.getNativeType() == pd2.getNativeType() && pd.isInputMode() == pd2.isInputMode()
				&& pd.isInputOptional() == pd2.isInputOptional() && pd.isNullable() == pd2.isNullable()
				&& pd.isOutputMode() == pd2.isOutputMode()
				&& isEqualString(pd.getDefaultInputValue(), pd2.getDefaultInputValue());
	}

	/**
	 * @param paramsBinding1
	 * @param paramsBinding2
	 * @return
	 */
	public static boolean isEqualParameterHints(Collection paramsBinding1, Collection paramsBinding2) {
		if (paramsBinding1 == paramsBinding2)
			return true;

		int basicCol = isEqualBasicCol(paramsBinding1, paramsBinding2);
		if (basicCol == B_TRUE)
			return true;
		else if (basicCol == B_FALSE)
			return false;

		Iterator it = paramsBinding1.iterator();
		Iterator it2 = paramsBinding2.iterator();
		while (it.hasNext()) {
			if (isEqualParamterOjbect(it.next(), it2.next()) == false)
				return false;
		}

		return true;
	}

	/**
	 * 
	 * @param param1
	 * @param param2
	 * @return
	 */
	public static boolean isEqualParamterOjbect(Object param1, Object param2) {
		if (param1 instanceof ParameterHint && param2 instanceof ParameterHint) {
			return isEqualParameterHint((ParameterHint) param1, (ParameterHint) param2);
		}
		if (param1 instanceof IInputParameterBinding && param2 instanceof IInputParameterBinding) {
			return isEqualParameterBinding((IInputParameterBinding) param1, (IInputParameterBinding) param2);
		}
		return false;
	}

	/**
	 * 
	 * @param param1
	 * @param param2
	 * @return
	 */
	public static boolean isEqualParameterBinding(IInputParameterBinding param1, IInputParameterBinding param2) {
		return param1.getName().equals(param2.getName()) && param1.getPosition() == param2.getPosition()
				&& isEqualExpression(param1.getExpr(), param2.getExpr());
	}

	/**
	 * @param pb
	 * @param pb2
	 * @return
	 */
	public static boolean isEqualParameterHint(ParameterHint pb, ParameterHint pb2) {
		return pb.getPosition() == pb2.getPosition() && isEqualString(pb.getName(), pb2.getName())
				&& isEqualObject(pb.getDefaultInputValue(), pb2.getDefaultInputValue())
				&& DataTypeUtil.toApiDataType(pb.getDataType()) == DataTypeUtil.toApiDataType(pb2.getDataType())
				&& pb.getNativeDataType() == pb2.getNativeDataType();
	}

	/**
	 * 
	 * @param resultHints1
	 * @param resultHints2
	 * @return
	 */
	public static boolean isEqualResultHints(List resultHints1, List resultHints2) {
		if (resultHints1 == resultHints2)
			return true;

		int basicCol = isEqualBasicCol(resultHints1, resultHints2);
		if (basicCol == B_TRUE)
			return true;
		else if (basicCol == B_FALSE)
			return false;

		Iterator it = resultHints1.iterator();
		Iterator it2 = resultHints2.iterator();
		while (it.hasNext()) {
			IColumnDefinition cd = (IColumnDefinition) it.next();
			IColumnDefinition cd2 = (IColumnDefinition) it2.next();
			if (isEqualColumnDefn(cd, cd2) == false)
				return false;
		}

		return true;
	}

	/**
	 * @param cd
	 * @param cd2
	 * @return
	 */
	public static boolean isEqualColumnDefn(IColumnDefinition cd, IColumnDefinition cd2) {
		if (cd == cd2)
			return true;
		else if (cd == null || cd2 == null)
			return false;

		return cd.getColumnPosition() == cd2.getColumnPosition() && cd.getDataType() == cd2.getDataType()
				&& cd.getNativeDataType() == cd2.getNativeDataType() && cd.getExportHint() == cd2.getExportHint()
				&& cd.getSearchHint() == cd2.getSearchHint() && isEqualString(cd.getAlias(), cd2.getAlias())
				&& isEqualString(cd.getColumnName(), cd2.getColumnName())
				&& isEqualString(cd.getDisplayName(), cd2.getDisplayName());

	}

	/**
	 * compare joint condition
	 * 
	 * @param joinConditions1
	 * @param joinConditions2
	 * @return
	 */
	public static boolean isEqualJointCondition(List joinConditions1, List joinConditions2) {
		if (joinConditions1 == joinConditions2)
			return true;
		int basicCol = isEqualBasicCol(joinConditions1, joinConditions2);
		if (basicCol == B_TRUE)
			return true;
		else if (basicCol == B_FALSE)
			return false;

		Iterator it = joinConditions1.iterator();
		Iterator it2 = joinConditions2.iterator();

		while (it.hasNext() || it2.hasNext()) {
			IJoinCondition cc = (IJoinCondition) it.next();
			IJoinCondition cc2 = (IJoinCondition) it2.next();
			if (isEqualJointConditionItem(cc, cc2) == false)
				return false;
		}
		return true;
	}

	/**
	 * compare
	 * 
	 * @param cc1
	 * @param cc2
	 * @return
	 */
	public static boolean isEqualJointConditionItem(IJoinCondition cc1, IJoinCondition cc2) {
		if (cc1 == cc2)
			return true;
		else if (cc1 == null || cc2 == null)
			return false;

		return isEqualExpression2(cc1.getLeftExpression(), cc2.getLeftExpression())
				&& isEqualExpression2(cc1.getRightExpression(), cc2.getRightExpression())
				&& cc1.getOperator() == cc2.getOperator();
	}
}
