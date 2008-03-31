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

package org.eclipse.birt.report.engine.adapter;


import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.engine.api.EngineException;

/**
 * 
 */
public class ExpressionUtilTest extends TestCase
{
	ExpressionUtil expressionUtil;

	protected void setUp( )
	{
		expressionUtil = new ExpressionUtil( );
	}

	public void testprepareTotalExpression( ) throws EngineException
	{
		String[] oldExpressions = new String[]{null,
				"   " + Messages.getString( "ExpressionUtilTest.old.1" ),
				Messages.getString( "ExpressionUtilTest.old.2" ),
				Messages.getString( "ExpressionUtilTest.old.3" ),
				Messages.getString( "ExpressionUtilTest.old.4" ),
				Messages.getString( "ExpressionUtilTest.old.5" ),
				Messages.getString( "ExpressionUtilTest.old.6" ),
				Messages.getString( "ExpressionUtilTest.old.7" ),
				Messages.getString( "ExpressionUtilTest.old.8" ),
				Messages.getString( "ExpressionUtilTest.old.9" ),
				Messages.getString( "ExpressionUtilTest.old.10" )
		};

		String[] newExpressions = new String[]{null,
				"   " + Messages.getString( "ExpressionUtilTest.new.1" ),
				Messages.getString( "ExpressionUtilTest.new.2" ),
				Messages.getString( "ExpressionUtilTest.new.3" ),
				Messages.getString( "ExpressionUtilTest.new.4" ),
				Messages.getString( "ExpressionUtilTest.new.5" ),
				Messages.getString( "ExpressionUtilTest.new.6" ),
				Messages.getString( "ExpressionUtilTest.new.7" ),
				Messages.getString( "ExpressionUtilTest.new.8" ),
				Messages.getString( "ExpressionUtilTest.new.9" ),
				Messages.getString( "ExpressionUtilTest.new.10" )
		};
	
		IConditionalExpression ce1 = new ConditionalExpression(
				new ScriptExpression("Total.TopN(100,5)+6"),
				IConditionalExpression.OP_BETWEEN,
				new ScriptExpression("Total.sum(row.a)"),
				new ScriptExpression("row.b")
		);
		
		IConditionalExpression ce2 = new ConditionalExpression(
				new ScriptExpression("Total.TopN(100,5)+6"),
				IConditionalExpression.OP_BOTTOM_N,
				new ScriptExpression("5"),null
		);
		
		List array = new ArrayList();
		for( int i = 0; i < oldExpressions.length; i++ )
		{
			array.add( oldExpressions[i] );
		}
		array.add( ce1 );
		array.add( ce2 );
				
		ITotalExprBindings  l = expressionUtil.prepareTotalExpressions( array, null  );
		for( int i = 0; i < oldExpressions.length; i++ )
		{
			assertEquals( newExpressions[i], l.getNewExpression( ).get( i ));
		}
		
		assertEquals( "row[\"TOTAL_COLUMN_13\"]", l.getNewExpression( ).get( oldExpressions.length ));
		assertEquals( "row[\"TOTAL_COLUMN_14\"]", l.getNewExpression( ).get( oldExpressions.length+1));
		
		IBinding[] bindings = l.getColumnBindings( );
		assertEquals( bindings.length, 15 );
	}
}
