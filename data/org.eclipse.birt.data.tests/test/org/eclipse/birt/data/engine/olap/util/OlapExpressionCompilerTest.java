
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
package org.eclipse.birt.data.engine.olap.util;

import junit.framework.TestCase;

import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;


/**
 * 
 */

public class OlapExpressionCompilerTest extends TestCase
{

	public void testGetReferencedDimensionName( )
	{
		assertEquals( "dim1", OlapExpressionCompiler.getReferencedDimensionName( new ScriptExpression("dimension[\"dim1\"][\"level1\"]") ));
		assertEquals( "dim1", OlapExpressionCompiler.getReferencedDimensionName( new ScriptExpression("ra[\"abc\"]+dimension[\"dim1\"][\"level1\"]")));
		assertEquals( "dim1", OlapExpressionCompiler.getReferencedDimensionName( new ScriptExpression("ra[\"abc\"]+dimension[\"dim1\"][\"level1\"]+15")));
		assertEquals( "dim1", OlapExpressionCompiler.getReferencedDimensionName( new ScriptExpression("ra[\"abc\"]+dimension[\"dim1\"]+dimension[\"dim2\"][\"level1\"]+15")));
		assertEquals( "dim1", OlapExpressionCompiler.getReferencedDimensionName( new ScriptExpression("ra[\"abc\"]+rb[\"dim2\"]+dimension[\"dim1\"][\"level1\"]+15")));
		
		assertEquals( "dim1", OlapExpressionCompiler.getReferencedDimensionName( new ConditionalExpression("ra[\"abc\"]+dimension[\"dim1\"][\"level1\"]", 0, "dim[\"abc\"]")));
	}

}
