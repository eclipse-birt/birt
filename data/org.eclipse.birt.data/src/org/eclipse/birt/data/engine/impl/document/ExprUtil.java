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
package org.eclipse.birt.data.engine.impl.document;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.CollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.ICollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ExpressionCollection;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

/**
 * Expression utility for report document save/load.
 */
public class ExprUtil {
	private final static int NULL_EXPRESSION = 0;
	private final static int SCRIPT_EXPRESSION = 1;
	private final static int CONDITIONAL_EXPRESSION = 2;
	private final static int COMBINED_EXPRESSION = 3;
	private final static int COLLECTION_EXPRESSION = 4;

	/**
	 * @param dos
	 * @param baseExpr
	 * @throws IOException
	 */
	public static void saveBaseExpr(DataOutputStream dos, IBaseExpression baseExpr) throws IOException {
		if (baseExpr == null) {
			IOUtil.writeInt(dos, NULL_EXPRESSION);
		} else if (baseExpr instanceof IScriptExpression) {
			IOUtil.writeInt(dos, SCRIPT_EXPRESSION);

			saveScriptExpr(dos, (IScriptExpression) baseExpr);
		} else if (baseExpr instanceof IConditionalExpression) {
			IOUtil.writeInt(dos, CONDITIONAL_EXPRESSION);

			IConditionalExpression condExpr = (IConditionalExpression) baseExpr;
			saveBaseExpr(dos, condExpr.getExpression());
			IOUtil.writeInt(dos, condExpr.getOperator());
			saveBaseExpr(dos, condExpr.getOperand1());
			saveBaseExpr(dos, condExpr.getOperand2());
		} else if (baseExpr instanceof IExpressionCollection) {
			IOUtil.writeInt(dos, COMBINED_EXPRESSION);
			IExpressionCollection combinedExpr = (IExpressionCollection) baseExpr;
			IOUtil.writeInt(dos, combinedExpr.getDataType());
			Object[] exprs = combinedExpr.getExpressions().toArray();
			IOUtil.writeInt(dos, exprs.length);
			for (int i = 0; i < exprs.length; i++) {
				saveBaseExpr(dos, (IBaseExpression) exprs[i]);
			}
		} else if (baseExpr instanceof ICollectionConditionalExpression) {
			IOUtil.writeInt(dos, COLLECTION_EXPRESSION);
			ICollectionConditionalExpression collectionExpr = (ICollectionConditionalExpression) baseExpr;
			Object[] expr = collectionExpr.getExpr().toArray();
			IOUtil.writeInt(dos, expr.length);
			for (int i = 0; i < expr.length; i++) {
				saveBaseExpr(dos, (IBaseExpression) expr[i]);
			}
			IOUtil.writeInt(dos, collectionExpr.getOperator());
			Object[] values = collectionExpr.getOperand().toArray();
			IOUtil.writeInt(dos, values.length);
			for (int i = 0; i < values.length; i++) {
				Object[] operands = ((Collection) values[i]).toArray();
				IOUtil.writeInt(dos, operands.length);
				for (int k = 0; k < operands.length; k++) {
					saveBaseExpr(dos, (IBaseExpression) operands[k]);
				}
			}
		} else {
			assert false;
		}
	}

	/**
	 * @param dos
	 * @param scriptExpr
	 * @throws IOException
	 */
	private static void saveScriptExpr(DataOutputStream dos, IScriptExpression scriptExpr) throws IOException {
		IOUtil.writeString(dos, scriptExpr.getText());
		IOUtil.writeInt(dos, scriptExpr.getDataType());
		IOUtil.writeString(dos, scriptExpr.getGroupName());
	}

	/**
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	public static IBaseExpression loadBaseExpr(DataInputStream dis) throws IOException {
		int exprType = IOUtil.readInt(dis);

		if (exprType == NULL_EXPRESSION) {
			return null;
		}
		if (exprType == SCRIPT_EXPRESSION) {
			return loadScriptExpr(dis);
		} else if (exprType == CONDITIONAL_EXPRESSION) {
			IScriptExpression expr = (IScriptExpression) loadBaseExpr(dis);
			int operator = IOUtil.readInt(dis);
			IBaseExpression op1 = (IBaseExpression) loadBaseExpr(dis);
			IBaseExpression op2 = (IBaseExpression) loadBaseExpr(dis);

			return new ConditionalExpression(expr, operator, op1, op2);
		} else if (exprType == COMBINED_EXPRESSION) {
			int type = IOUtil.readInt(dis);
			int size = IOUtil.readInt(dis);
			IBaseExpression[] baseExpr = new IBaseExpression[size];
			for (int i = 0; i < size; i++) {
				baseExpr[i] = (IBaseExpression) loadBaseExpr(dis);
			}
			return new ExpressionCollection(baseExpr);
		} else if (exprType == COLLECTION_EXPRESSION) {
			int size = IOUtil.readInt(dis);
			List baseExpr = new ArrayList();
			for (int i = 0; i < size; i++) {
				baseExpr.add(loadBaseExpr(dis));
			}
			int operater = IOUtil.readInt(dis);

			size = IOUtil.readInt(dis);
			List operandList = new ArrayList();
			for (int i = 0; i < size; i++) {
				List valueList = new ArrayList();
				int valueSize = IOUtil.readInt(dis);
				for (int k = 0; k < valueSize; k++) {
					valueList.add(loadBaseExpr(dis));
				}
				operandList.add(valueList);
			}

			return new CollectionConditionalExpression(baseExpr, operater, operandList);
		} else {
			assert false;

			return null;
		}
	}

	/**
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	private static IScriptExpression loadScriptExpr(DataInputStream dis) throws IOException {
		ScriptExpression scriptExpr = new ScriptExpression(IOUtil.readString(dis));
		scriptExpr.setDataType(IOUtil.readInt(dis));
		scriptExpr.setGroupName(IOUtil.readString(dis));
		return scriptExpr;
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
					&& isEqualExpression(ce.getExpression(), ce2.getExpression())
					&& isEqualExpression(ce.getOperand1(), ce2.getOperand1())
					&& isEqualExpression(ce.getOperand2(), ce2.getOperand2());
		} else if (be instanceof IExpressionCollection && be2 instanceof IExpressionCollection) {
			return be.getDataType() == be2.getDataType() && isEqualExpressionArray(
					((IExpressionCollection) be).getExpressions(), ((IExpressionCollection) be2).getExpressions());

		} else if (be instanceof ICollectionConditionalExpression && be2 instanceof ICollectionConditionalExpression) {
			ICollectionConditionalExpression f1 = (ICollectionConditionalExpression) be;
			ICollectionConditionalExpression f2 = (ICollectionConditionalExpression) be2;

			if (be.getDataType() != be2.getDataType() || f1.getExpr().size() != f2.getExpr().size()
					|| f1.getOperand().size() != f2.getOperand().size())
				return false;

			if (!isEqualExpressionArray(f1.getExpr(), f2.getExpr()))
				return false;
			Iterator iter1 = f1.getOperand().iterator();
			Iterator iter2 = f2.getOperand().iterator();
			while (iter1.hasNext()) {
				if (!isEqualExpressionArray((Collection) iter1.next(), (Collection) iter2.next()))
					return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * @param se
	 * @param se2
	 * @return
	 */
	private static boolean isEqualExpression2(IScriptExpression se, IScriptExpression se2) {
		if (se == se2)
			return true;
		else if (se == null || se2 == null)
			return false;

		return (se.getDataType() == se2.getDataType()
				|| (se.getDataType() == DataType.ANY_TYPE && se2.getDataType() == DataType.UNKNOWN_TYPE)
				|| (se.getDataType() == DataType.UNKNOWN_TYPE && se2.getDataType() == DataType.ANY_TYPE))
				&& isEqualObject(se.getText(), se2.getText());
	}

	/**
	 * 
	 * @param operands
	 * @param operands2
	 * @return
	 */
	private static boolean isEqualExpressionArray(Collection op1, Collection op2) {
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
	 * Only for non-collection object
	 * 
	 * @param ob1
	 * @param ob2
	 * @return
	 */
	private static boolean isEqualObject(Object ob1, Object ob2) {
		if (ob1 == ob2)
			return true;
		else if (ob1 == null || ob2 == null)
			return false;

		return ob1.equals(ob2);
	}

	/**
	 * @param be
	 * @return
	 */
	public static int hashCode(IBaseExpression be) {
		if (be == null)
			return 0;

		if (be instanceof IScriptExpression) {
			return hashCode2((IScriptExpression) be);
		} else if (be instanceof IConditionalExpression) {
			IConditionalExpression ce = (IConditionalExpression) be;
			return ce.getDataType() + ce.getOperator() + hashCode2(ce.getExpression()) + hashCode2(ce.getOperand1())
					+ hashCode2(ce.getOperand2());
		}

		return 0;
	}

	/**
	 * @param se
	 * @return
	 */
	private static int hashCode2(IBaseExpression se) {
		if (se == null)
			return 0;

		if (se instanceof IScriptExpression)
			return se.getDataType() + ((IScriptExpression) se).getText().trim().hashCode();
		else if (se instanceof IExpressionCollection) {
			int hashCode = 0;
			Object[] exprs = ((IExpressionCollection) se).getExpressions().toArray();
			for (int i = 0; i < exprs.length; i++) {
				hashCode += hashCode2((IBaseExpression) exprs[i]);
			}
			return se.getDataType() + hashCode;
		}
		return 0;
	}

}
