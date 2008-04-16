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

package org.eclipse.birt.report.designer.internal.ui.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;

/**
 * A JSSyntaxContext represents a variables container. JSSyntaxContext also
 * provides methods to access avaible Type meta-data.
 */

public class JSSyntaxContext
{

	/**
	 * BIRT engine objects defined in DesignEngine.
	 */
	private static Map engineObjectMap = new HashMap( );

	// TODO use a smart cache.
	private static Map classMap = new HashMap( );

	/**
	 * Context variables map.
	 */
	private Map objectMetaMap = new HashMap( );

	static
	{
		List engineClassesList = DEUtil.getClasses( );
		for ( Iterator iter = engineClassesList.iterator( ); iter.hasNext( ); )
		{
			IClassInfo element = (IClassInfo) iter.next( );
			engineObjectMap.put( element.getName( ),
					new EngineClassJSObject( element ) );
		}
	}

	// static methods

	public static JSObjectMetaData getEnginJSObject( String classType )
	{
		return engineObjectMap.containsKey( classType ) ? (JSObjectMetaData) engineObjectMap.get( classType )
				: null;
	}

	public static JSObjectMetaData[] getAllEnginJSObjects( )
	{
		return (JSObjectMetaData[]) engineObjectMap.values( )
				.toArray( new JSObjectMetaData[engineObjectMap.values( ).size( )] );
	}

	public static JSObjectMetaData getJavaClassMeta( String className )
			throws ClassNotFoundException
	{
		JSObjectMetaData meta = null;
		if ( !classMap.containsKey( className ) )
		{
			meta = new JavaClassJSObject( className );
			classMap.put( className, meta );
		}
		else
		{
			meta = (JSObjectMetaData) classMap.get( className );
		}
		return meta;
	}

	public boolean setVariable( String name, String className )
	{
		try
		{
			objectMetaMap.put( name, getJavaClassMeta( className ) );

			return true;
		}
		catch ( Exception e )
		{
			if ( getEnginJSObject( className ) != null )
			{
				objectMetaMap.put( name, getEnginJSObject( className ) );

				return true;
			}
			else
			{
				removeVariable( name );

				return false;
			}
		}
	}

	public void setVariable( String name, Class clazz )
			throws ClassNotFoundException
	{
		objectMetaMap.put( name, new JavaClassJSObject( clazz.getName( ) ) );
	}

	public void setVariable( String name, IClassInfo classInfo )
	{
		objectMetaMap.put( name, new ExtensionClassJSObject( classInfo ) );
	}

	public void removeVariable( String name )
	{
		objectMetaMap.remove( name );
	}

	public void clear( )
	{
		objectMetaMap.clear( );
	}

	public JSObjectMetaData getVariableMeta( String variableName )
	{
		if ( objectMetaMap.containsKey( variableName ) )
			return (JSObjectMetaData) objectMetaMap.get( variableName );
		else
			return getEnginJSObject( variableName );
	}

	// inner class

}
