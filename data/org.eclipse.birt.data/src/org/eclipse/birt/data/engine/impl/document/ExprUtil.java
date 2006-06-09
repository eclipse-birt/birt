/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.document;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

/**
 * Expression utility for report document save/load.
 */
public class ExprUtil
{
	private final static int NULL_EXPRESSION = 0;
	private final static int SCRIPT_EXPRESSION = 1;
	private final static int CONDITIONAL_EXPRESSION = 2;

	/**
	 * @param dos
	 * @param baseExpr
	 * @throws IOException
	 */
	public static void saveBaseExpr( DataOutputStream dos,
			IBaseExpression baseExpr ) throws IOException
	{
		if ( baseExpr == null )
		{
			IOUtil.writeInt( dos, NULL_EXPRESSION );
		}
		else if ( baseExpr instanceof IScriptExpression )
		{
			IOUtil.writeInt( dos, SCRIPT_EXPRESSION );

			saveScriptExpr( dos, (IScriptExpression) baseExpr );
		}
		else if ( baseExpr instanceof IConditionalExpression )
		{
			IOUtil.writeInt( dos, CONDITIONAL_EXPRESSION );

			IConditionalExpression condExpr = (IConditionalExpression) baseExpr;
			saveBaseExpr( dos, condExpr.getExpression( ) );
			IOUtil.writeInt( dos, condExpr.getOperator( ) );
			saveBaseExpr( dos, condExpr.getOperand1( ) );
			saveBaseExpr( dos, condExpr.getOperand2( ) );
		}
		else
		{
			assert false;
		}
	}
	
	/**
	 * @param dos
	 * @param scriptExpr
	 * @throws IOException
	 */
	private static void saveScriptExpr( DataOutputStream dos,
			IScriptExpression scriptExpr ) throws IOException
	{
		IOUtil.writeString( dos, scriptExpr.getText( ) );
		IOUtil.writeInt( dos, scriptExpr.getDataType( ) );
		IOUtil.writeString( dos, scriptExpr.getGroupName( ) );
	}
	
	/**
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	public static IBaseExpression loadBaseExpr( DataInputStream dis )
			throws IOException
	{
		int exprType = IOUtil.readInt( dis );
		
		if ( exprType == NULL_EXPRESSION )
		{
			return null;
		}
		if ( exprType == SCRIPT_EXPRESSION )
		{
			return loadScriptExpr( dis );
		}
		else if ( exprType == CONDITIONAL_EXPRESSION )
		{
			IScriptExpression expr = (IScriptExpression) loadBaseExpr( dis );
			int operator = IOUtil.readInt( dis );
			IScriptExpression op1 = (IScriptExpression) loadBaseExpr( dis );
			IScriptExpression op2 = (IScriptExpression) loadBaseExpr( dis );

			return new ConditionalExpression( expr, operator, op1, op2 );
		}
		else
		{
			assert false;

			return null;
		}
	}

	/**
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	private static IScriptExpression loadScriptExpr( DataInputStream dis )
			throws IOException
	{
		ScriptExpression scriptExpr = new ScriptExpression( IOUtil.readString( dis ) );
		scriptExpr.setDataType( IOUtil.readInt( dis ) );
		scriptExpr.setGroupName( IOUtil.readString( dis ) );
		return scriptExpr;
	}
	
	/**
	 * 
	 * @param be
	 * @param be2
	 * @return
	 */
	public static boolean isEqualExpression( IBaseExpression be, IBaseExpression be2 )
	{
		if ( be == be2 )
			return true;
		else if ( be == null || be2 == null )
			return false;

		if ( be instanceof IScriptExpression
				&& be2 instanceof IScriptExpression )
		{
			IScriptExpression se = (IScriptExpression) be;
			IScriptExpression se2 = (IScriptExpression) be2;
			return isEqualExpression2( se, se2 );
		}
		else if ( be instanceof IConditionalExpression
				&& be2 instanceof IConditionalExpression )
		{
			IConditionalExpression ce = (IConditionalExpression) be;
			IConditionalExpression ce2 = (IConditionalExpression) be2;
			return ce.getDataType( ) == ce2.getDataType( )
					&& ce.getOperator( ) == ce2.getOperator( )
					&& isEqualExpression2( ce.getExpression( ),
							ce2.getExpression( ) )
					&& isEqualExpression2( ce.getOperand1( ), ce2.getOperand1( ) )
					&& isEqualExpression2( ce.getOperand2( ), ce2.getOperand2( ) );
		}

		return false;
	}

	/**
	 * @param se
	 * @param se2
	 * @return
	 */
	private static boolean isEqualExpression2( IScriptExpression se,
			IScriptExpression se2 )
	{
		if ( se == se2 )
			return true;
		else if ( se == null || se2 == null )
			return false;

		return se.getDataType( ) == se2.getDataType( )
				&& isEqualObject( se.getText( ), se2.getText( ) );
	}

	/**
	 * Only for non-collection object
	 * 
	 * @param ob1
	 * @param ob2
	 * @return
	 */
	private static boolean isEqualObject( Object ob1, Object ob2 )
	{
		if ( ob1 == ob2 )
			return true;
		else if ( ob1 == null || ob2 == null )
			return false;

		return ob1.equals( ob2 );
	}
	
	/**
	 * @param be
	 * @return
	 */
	public static int hashCode( IBaseExpression be )
	{
		if ( be == null )
			return 0;

		if ( be instanceof IScriptExpression )
		{
			return hashCode2( (IScriptExpression) be );
		}
		else if ( be instanceof IConditionalExpression )
		{
			IConditionalExpression ce = (IConditionalExpression) be;
			return ce.getDataType( )
					+ ce.getOperator( ) + hashCode2( ce.getExpression( ) )
					+ hashCode2( ce.getOperand1( ) )
					+ hashCode2( ce.getOperand2( ) );
		}

		return 0;
	}

	/**
	 * @param se
	 * @return
	 */
	private static int hashCode2( IScriptExpression se )
	{
		if ( se == null )
			return 0;

		return se.getDataType( ) + se.getText( ).trim( ).hashCode( );
	}
	
}
