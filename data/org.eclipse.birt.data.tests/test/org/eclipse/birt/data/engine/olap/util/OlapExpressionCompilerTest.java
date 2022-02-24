
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
package org.eclipse.birt.data.engine.olap.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class OlapExpressionCompilerTest {
	@Test
	public void testGetReferencedDimensionName() {
		assertEquals("dim1", OlapExpressionCompiler
				.getReferencedScriptObject(new ScriptExpression("dimension[\"dim1\"][\"level1\"]"), "dimension"));
		assertEquals("dim1", OlapExpressionCompiler.getReferencedScriptObject(
				new ScriptExpression("ra[\"abc\"]+dimension[\"dim1\"][\"level1\"]"), "dimension"));
		assertEquals("dim1", OlapExpressionCompiler.getReferencedScriptObject(
				new ScriptExpression("ra[\"abc\"]+dimension[\"dim1\"][\"level1\"]+15"), "dimension"));
		assertEquals("dim1",
				OlapExpressionCompiler.getReferencedScriptObject(
						new ScriptExpression("ra[\"abc\"]+dimension[\"dim1\"]+dimension[\"dim2\"][\"level1\"]+15"),
						"dimension"));
		assertEquals("dim1", OlapExpressionCompiler.getReferencedScriptObject(
				new ScriptExpression("ra[\"abc\"]+rb[\"dim2\"]+dimension[\"dim1\"][\"level1\"]+15"), "dimension"));

		assertEquals("dim1",
				OlapExpressionCompiler.getReferencedScriptObject(
						new ConditionalExpression("ra[\"abc\"]+dimension[\"dim1\"][\"level1\"]", 0, "dim[\"abc\"]"),
						"dimension"));
	}

	@Test
	public void testGetReferencedDimLevel() throws DataException {
		IBinding binding1 = new Binding("b1", new ScriptExpression("dimension[\"dim1\"][\"level1\"]"));
		IBinding binding2 = new Binding("b2", new ScriptExpression("dimension.dim1.level2 + 1"));
		IBinding binding3 = new Binding("b3",
				new ScriptExpression("dimension[\"dim1\"][\"level3\"] + dimension.dim1.level2"));
		IBinding binding4 = new Binding("b4", new ScriptExpression("data.b1"));
		IBinding binding5 = new Binding("b5", new ScriptExpression("dimension.dim1.level1 + 25"));
		IBinding binding6 = new Binding("b6", new ScriptExpression("data.b4 + 1"));
		IBinding binding7 = new Binding("b7", new ScriptExpression("if( true ) data.b2; else data.b3;"));
		IBinding binding8 = new Binding("b8",
				new ScriptExpression("BirtComp.equalTo( dimension[\"customerRegions\"][\"COUNTRY\"], \"USA\" )"));
		List bindings = new ArrayList();
		bindings.add(binding1);
		bindings.add(binding2);
		bindings.add(binding3);
		bindings.add(binding4);
		bindings.add(binding5);
		bindings.add(binding6);
		bindings.add(binding7);

		Set s1 = OlapExpressionCompiler.getReferencedDimLevel(binding1.getExpression(), bindings);
		assertTrue(s1.size() == 1);
		assertTrue(((DimLevel) s1.iterator().next()).getDimensionName().equals("dim1"));
		assertTrue(((DimLevel) s1.iterator().next()).getLevelName().equals("level1"));

		Set s2 = OlapExpressionCompiler.getReferencedDimLevel(binding2.getExpression(), bindings);
		assertTrue(s2.size() == 1);
		assertTrue(((DimLevel) s2.iterator().next()).getDimensionName().equals("dim1"));
		assertTrue(((DimLevel) s2.iterator().next()).getLevelName().equals("level2"));

		Set s21 = OlapExpressionCompiler.getReferencedDimLevel(binding2.getExpression(), bindings, true);
		assertTrue(s21.size() == 0);

		Set s3 = OlapExpressionCompiler.getReferencedDimLevel(binding3.getExpression(), bindings);
		assertTrue(s3.size() == 2);
		assertTrue(s3.contains(new DimLevel("dim1", "level3")));
		assertTrue(s3.contains(new DimLevel("dim1", "level2")));

		Set s4 = OlapExpressionCompiler.getReferencedDimLevel(binding4.getExpression(), bindings);
		assertTrue(s4.size() == 1);
		assertTrue(s4.contains(new DimLevel("dim1", "level1")));

		Set s41 = OlapExpressionCompiler.getReferencedDimLevel(binding4.getExpression(), bindings, true);
		assertTrue(s41.size() == 1);
		assertTrue(s41.contains(new DimLevel("dim1", "level1")));

		Set s5 = OlapExpressionCompiler.getReferencedDimLevel(binding5.getExpression(), bindings);
		assertTrue(s5.size() == 1);
		assertTrue(s5.contains(new DimLevel("dim1", "level1")));

		Set s6 = OlapExpressionCompiler.getReferencedDimLevel(binding6.getExpression(), bindings);
		assertTrue(s6.size() == 1);
		assertTrue(s6.contains(new DimLevel("dim1", "level1")));

		Set s61 = OlapExpressionCompiler.getReferencedDimLevel(binding6.getExpression(), bindings, true);
		assertTrue(s61.size() == 0);

		Set s7 = OlapExpressionCompiler.getReferencedDimLevel(binding7.getExpression(), bindings);
		assertTrue(s7.size() == 1);
		assertTrue(s7.contains(new DimLevel("dim1", "level2")));

		Set s8 = OlapExpressionCompiler.getReferencedDimLevel(binding8.getExpression(), bindings);
		assertTrue(s8.size() == 1);
		assertTrue(s8.contains(new DimLevel("customerRegions", "COUNTRY")));

		Set s9 = OlapExpressionCompiler.getReferencedDimLevel(
				new ScriptExpression("dimension[\"\\\"dim1\"][\"level1\\\"\"][\"attr1\"]"), Collections.EMPTY_LIST);
		assertTrue(s9.size() == 1);
		assertTrue(s9.contains(new DimLevel("\"dim1", "level1\"", "attr1")));
	}
}
