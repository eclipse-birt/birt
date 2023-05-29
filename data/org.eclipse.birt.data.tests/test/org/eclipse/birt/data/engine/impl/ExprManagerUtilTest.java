/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.junit.Before;
import org.junit.Test;

public class ExprManagerUtilTest {
	ScriptContext cx;

	@Before
	public void exprManagerUtilSetUp() throws Exception {
		cx = new ScriptContext();
	}

	private Map getBindingMap(Map exprMap) throws DataException {
		Map result = new HashMap();
		for (Iterator it = exprMap.keySet().iterator(); it.hasNext();) {
			Object o = it.next();
			if (exprMap.get(o) instanceof IBaseExpression) {
				IBinding b = new Binding(o.toString());
				b.setExpression((IBaseExpression) exprMap.get(o));
				result.put(o, b);
			}
		}
		return result;
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.data.engine.impl.ExprManagerUtil.validateNodes(Node[])'
	 */
	@Test
	public void testValidateNodes1() throws DataException {

		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL2\"]"));

		em.addBindingExpr(null, getBindingMap(m), 0);
		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
		} catch (DataException e) {
			fail("Should not arrive here");
		}
	}

	// nested, directly
	@Test
	public void testValidateNodes2() throws DataException {

		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL0\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL2\"]"));

		em.addBindingExpr(null, getBindingMap(m), 0);

		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
			fail("Should not arrive here");
		} catch (DataException e) {
		}
	}

	// nested, indirectly
	@Test
	public void testValidateNodes3() throws DataException {

		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL0\"]"));

		em.addBindingExpr(null, getBindingMap(m), 0);
		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
			fail("Should not arrive here");
		} catch (DataException e) {
		}
	}

	// nested, self
	@Test
	public void testValidateNodes4() throws DataException {

		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL1\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL0\"]"));

		em.addBindingExpr(null, getBindingMap(m), 0);
		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
			fail("Should not arrive here");
		} catch (DataException e) {
		}
	}

	/**
	 * Test valid group keys
	 *
	 * @throws DataException
	 */
	@Test
	public void testValidateNodes5() throws DataException {
		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL2\"]"));

		em.addBindingExpr("COL0", getBindingMap(m), 1);

		m = new HashMap();
		m.put("COL5", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL6", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL7", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL8", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL9", new ScriptExpression("row[\"COL8\"]"));

		em.addBindingExpr("COL2", getBindingMap(m), 2);

		m = new HashMap();
		m.put("COL10", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL11", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL12", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL13", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL14", new ScriptExpression("row[\"COL8\"]"));

		em.addBindingExpr("COL10", getBindingMap(m), 3);

		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
		} catch (DataException e) {
			fail("Should not arrive here");
		}
	}

	/**
	 * Test invalid group keys. The group key of group 2 directly uses the column
	 * binding defined in group 3.
	 *
	 * @throws DataException
	 */
	@Test
	public void testValidateNodes6() throws DataException {
		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL2\"]"));

		em.addBindingExpr("row.COL0", getBindingMap(m), 1);

		m = new HashMap();
		m.put("row.COL5", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL6", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL7", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL8", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL9", new ScriptExpression("row[\"COL8\"]"));

		em.addBindingExpr("row.COL10", getBindingMap(m), 2);

		m = new HashMap();
		m.put("COL10", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL11", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL12", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL13", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL14", new ScriptExpression("row[\"COL8\"]"));

		em.addBindingExpr("row.COL10", getBindingMap(m), 3);

		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
			fail("Should not arrive here");
		} catch (DataException e) {
		}
	}

	/**
	 * Test valid group column bindings. One non-key column binding of group 2
	 * directly uses the column binding defined in group 3.
	 *
	 * @throws DataException
	 */
	@Test
	public void testValidateNodes7() throws DataException {
		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL2\"]"));

		em.addBindingExpr("COL0", getBindingMap(m), 1);

		m = new HashMap();
		m.put("COL5", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL6", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL7", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL8", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL9", new ScriptExpression("row[\"COL10\"]"));

		em.addBindingExpr("COL5", getBindingMap(m), 2);

		m = new HashMap();
		m.put("COL10", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL11", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL12", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL13", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL14", new ScriptExpression("row[\"COL8\"]"));

		em.addBindingExpr("COL10", getBindingMap(m), 3);

		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
		} catch (DataException e) {
			fail("Should not arrive here");
		}
	}

	/**
	 * Test invalid group key. The key of group 2 directly uses the column binding
	 * defined in group 3.
	 *
	 * @throws DataException
	 */
	@Test
	public void testValidateNodes8() throws DataException {
		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL2\"]"));

		em.addBindingExpr("row.COL0", getBindingMap(m), 1);

		m = new HashMap();
		m.put("row.COL5", new ScriptExpression("row[\"COL1\"]+row[\"COL11\"]+row[\"COL3\"]"));
		m.put("COL6", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL7", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL8", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL9", new ScriptExpression("row[\"COL10\"]"));

		em.addBindingExpr("row.COL5", getBindingMap(m), 2);

		m = new HashMap();
		m.put("row.COL10", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL11", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL12", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL13", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL14", new ScriptExpression("row[\"COL8\"]"));

		em.addBindingExpr("row.COL10", getBindingMap(m), 3);

		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
			fail("Should not arrive here");
		} catch (DataException e) {
		}
	}

	/**
	 * Test invalid group key. The key of group 1 directly uses the column binding
	 * defined in group 3.
	 *
	 * @throws DataException
	 */
	@Test
	public void testValidateNodes9() throws DataException {
		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL11\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL11\"]"));

		em.addBindingExpr("row.COL0", getBindingMap(m), 1);

		m = new HashMap();
		m.put("COL5", new ScriptExpression("row[\"COL1\"]+row[\"COL11\"]+row[\"COL3\"]"));
		m.put("COL6", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL7", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL8", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL9", new ScriptExpression("row[\"COL10\"]"));

		em.addBindingExpr("row.COL5", getBindingMap(m), 2);

		m = new HashMap();
		m.put("COL10", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL11", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL12", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL13", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL14", new ScriptExpression("row[\"COL8\"]"));

		em.addBindingExpr("row.COL10", getBindingMap(m), 3);

		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
			fail("Should not arrive here");
		} catch (DataException e) {
		}
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.data.engine.impl.ExprManagerUtil.validateNodes(Node[])'
	 */
	@Test
	public void testValidateNodes10() throws DataException {

		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL2\"]"));
		m.put("COL5", new ConditionalExpression("row[\"COL2\"]", IConditionalExpression.OP_EQ, "2"));

		em.addBindingExpr(null, getBindingMap(m), 0);
		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
		} catch (DataException e) {
			fail("Should not arrive here");
		}
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.data.engine.impl.ExprManagerUtil.validateNodes(Node[])'
	 */
	@Test
	public void testValidateNodes11() throws DataException {

		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL2\"]"));
		m.put("COL5", new ConditionalExpression("row[\"COL5\"]", IConditionalExpression.OP_EQ, "2"));

		em.addBindingExpr(null, getBindingMap(m), 0);
		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
			fail("Should not arrive here");
		} catch (DataException e) {

		}
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.data.engine.impl.ExprManagerUtil.validateNodes(Node[])'
	 */
	@Test
	public void testValidateNodes12() throws DataException {

		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("1111row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]"));
		m.put("COL2", new ScriptExpression("dataSetRow[\"COL2\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL2\"]+row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL2\"]"));

		em.addBindingExpr(null, getBindingMap(m), 0);
		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
		} catch (DataException e) {
			fail("Should not arrive here");
		}
	}

	/**
	 * Test reference to not exist column binding in an expression.
	 *
	 * @throws DataException
	 */
	@Test
	public void testValidateNodes13() throws DataException {

		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL2\"]"));
		m.put("COL2", new ScriptExpression("row[\"COL3\"]"));
		m.put("COL3", new ScriptExpression("row[\"COL4\"]"));
		m.put("COL4", new ScriptExpression("row[\"COL\"]"));

		em.addBindingExpr(null, getBindingMap(m), 0);
		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
			fail("Should not arrive here");
		} catch (DataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test reference to not exist column binding in an expression.
	 *
	 * @throws DataException
	 */
	@Test
	public void testValidateNodes0() throws DataException {

		ExprManager em = new ExprManager(null, cx);
		Map m = new HashMap();
		m.put("COL0", new ScriptExpression("row[\"COL1\"]"));
		m.put("COL1", new ScriptExpression("row[\"COL0\"]"));

		em.addBindingExpr(null, getBindingMap(m), 0);
		try {
			ExprManagerUtil.validateColumnBinding(em, null, cx);
			fail("Should not arrive here");
		} catch (DataException e) {
			e.printStackTrace();
		}
	}
}
