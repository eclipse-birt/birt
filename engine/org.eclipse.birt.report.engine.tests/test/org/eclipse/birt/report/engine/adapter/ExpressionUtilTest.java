/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.ir.Expression;

import junit.framework.TestCase;

/**
 *
 */
public class ExpressionUtilTest extends TestCase {
	ExpressionUtil expressionUtil;

	@Override
	protected void setUp() {
		expressionUtil = new ExpressionUtil();
	}

	public void testprepareTotalExpression() throws EngineException {
		String[] oldExpressions = { null, "   " + Messages.getString("ExpressionUtilTest.old.1"),
				Messages.getString("ExpressionUtilTest.old.2"), Messages.getString("ExpressionUtilTest.old.3"),
				Messages.getString("ExpressionUtilTest.old.4"), Messages.getString("ExpressionUtilTest.old.5"),
				Messages.getString("ExpressionUtilTest.old.6"), Messages.getString("ExpressionUtilTest.old.7"),
				Messages.getString("ExpressionUtilTest.old.8"), Messages.getString("ExpressionUtilTest.old.9"),
				Messages.getString("ExpressionUtilTest.old.10") };

		String[] newExpressions = { null, "   " + Messages.getString("ExpressionUtilTest.new.1"),
				Messages.getString("ExpressionUtilTest.new.2"), Messages.getString("ExpressionUtilTest.new.3"),
				Messages.getString("ExpressionUtilTest.new.4"), Messages.getString("ExpressionUtilTest.new.5"),
				Messages.getString("ExpressionUtilTest.new.6"), Messages.getString("ExpressionUtilTest.new.7"),
				Messages.getString("ExpressionUtilTest.new.8"), Messages.getString("ExpressionUtilTest.new.9"),
				Messages.getString("ExpressionUtilTest.new.10") };

		IConditionalExpression ce1 = new ConditionalExpression(new ScriptExpression("Total.TopN(100,5)+6"),
				IConditionalExpression.OP_BETWEEN, new ScriptExpression("Total.sum(row.a)"),
				new ScriptExpression("row.b"));

		IConditionalExpression ce2 = new ConditionalExpression(new ScriptExpression("Total.TopN(100,5)+6"),
				IConditionalExpression.OP_BOTTOM_N, new ScriptExpression("5"), null);

		List<Expression> array = new ArrayList<>(oldExpressions.length);
		for (int i = 0; i < oldExpressions.length; i++) {
			array.add(Expression.newScript(oldExpressions[i]));
		}
		array.add(Expression.newConditional(ce1));
		array.add(Expression.newConditional(ce2));

		ITotalExprBindings l = expressionUtil.prepareTotalExpressions(array, null);
		for (int i = 0; i < oldExpressions.length; i++) {
			assertEquals(newExpressions[i], l.getNewExpression().get(i).getScriptText());
		}

		assertEquals("row[\"TOTAL_COLUMN_13\"]", l.getNewExpression().get(oldExpressions.length).getScriptText());
		assertEquals("row[\"TOTAL_COLUMN_14\"]", l.getNewExpression().get(oldExpressions.length + 1).getScriptText());

		IBinding[] bindings = l.getColumnBindings();
		assertEquals(bindings.length, 15);
	}
}
