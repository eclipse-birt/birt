/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.script;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Implements Javascript ColumnDefn object, which wraps one field in a odi IResultClass.
 */
public class JSColumnDefn extends ScriptableObject
{
	private static String  		INDEX = "index";
	private static String		NAME = "name";
	private static String		TYPE = "type";
	private static String		NATIVE_TYPE = "nativeType";
	private static String		LABEL = "label";
	private static String		ALIAS = "alias";
	
	private static String		INTEGER = "integer";
	private static String		FLOAT = "float";
	private static String		DECIMAL = "decimal";
	private static String		BOOLEAN = "boolean";
	private static String		STRING = "string";
	private static String		DATETIME = "dateTime";
	
	private static String		INTEGER_VAL = "integer";
	private static String		FLOAT_VAL = "float";
	private static String		DECIMAL_VAL = "decimal";
	private static String		BOOLEAN_VAL = "boolean";
	private static String		STRING_VAL = "string";
	private static String		DATETIME_VAL = "dateTime";
	
	private static String[]		propNames = new String[]
					{ INDEX, NAME, TYPE, NATIVE_TYPE, LABEL, ALIAS, INTEGER, FLOAT,
					DECIMAL, BOOLEAN, STRING, DATETIME };
	
	private static HashSet		propNameSet = new HashSet(
					Arrays.asList( propNames ) );

	private IResultClass resultClass;
	private int fieldIndex;
	
	/**
	 * Constructor
	 * @param index 1-based index of column in resultClass
	 */
	JSColumnDefn( IResultClass resultClass, int index )
	{
		assert resultClass != null;
		assert index > 0 && index <= resultClass.getFieldCount();
		this.resultClass = resultClass;
		this.fieldIndex = index;
		
		// This object is not modifiable in any way
		sealObject();
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public Object get(String name, Scriptable start)
	{
		if ( ! propNameSet.contains( name ) )
			return super.get(name, start);
		
		// Static properties
		if ( name.equals( INTEGER ) )
			return INTEGER_VAL;
		if ( name.equals( FLOAT) )
			return FLOAT_VAL;
		if ( name.equals( DECIMAL ) )
			return DECIMAL_VAL;
		if ( name.equals( BOOLEAN ) )
			return BOOLEAN_VAL;
		if ( name.equals( STRING ) )
			return STRING_VAL;
		if ( name.equals( DATETIME ) )
			return DATETIME_VAL;
		
		try
		{
			// Result class properties
			if ( name.equals(INDEX))
				return new Integer(fieldIndex);
			if ( name.equals(NAME))
				return resultClass.getFieldName( fieldIndex );
			
			if ( name.equals(TYPE) )
			{
				Class c = resultClass.getFieldValueClass(fieldIndex);
				if( c == Integer.class )
				    return INTEGER_VAL;
				if( c == Double.class )
				    return FLOAT_VAL;
				if( c == String.class )
					return STRING_VAL;
				if( c == BigDecimal.class )
					return DECIMAL_VAL;
				if( c == Boolean.class )
					return BOOLEAN_VAL;
				if( c == Date.class ||
					c == Time.class ||
					c == Timestamp.class )
					return DATETIME_VAL;
				// unknown type
				return null;
			}
			
			if ( name.equals(NATIVE_TYPE))
			{
				// TODO: need IResultClass to return the native data type string
				return null;
			}
			
			if ( name.equals(LABEL) )
				return resultClass.getFieldLabel(fieldIndex);
			if ( name.equals(ALIAS))
				return resultClass.getFieldAlias(fieldIndex);
		}
		catch ( DataException e)
		{
			// TODO: log exception
			return null;
		}
		
		// Should never get here
		assert false;
		return null;
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	public String getClassName()
	{
		return "ColumnDefn";
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	public Object[] getIds()
	{
		return propNames;
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#has(java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public boolean has(String name, Scriptable start)
	{
		if ( propNameSet.contains( name ) )
			return true;
		else
			return super.has(name, start);
	}
}
