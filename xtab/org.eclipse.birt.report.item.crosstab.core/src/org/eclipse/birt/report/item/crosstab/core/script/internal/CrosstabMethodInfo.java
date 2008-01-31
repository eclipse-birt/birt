/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.scripts.MethodInfo;

/**
 * CrosstabMethodInfo
 */
public class CrosstabMethodInfo extends MethodInfo
{

	protected CrosstabMethodInfo( Method method )
	{
		super( method );
	}

	public boolean isDeprecated( )
	{
		String javaDoc = getJavaDoc( );
		return javaDoc != null && javaDoc.indexOf( "@deprecated" ) != -1; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#getJavaDoc()
	 */
	public String getJavaDoc( )
	{
		return (String) javaDoc.get( getMethod( ).getName( ) );
	}

	private final static Map javaDoc = new HashMap( );

	static
	{
		//TODO
		javaDoc.put( "onPrepareCrosstab",
				"/**\n"
						+ " * Called before populating the series dataset using the DataSetProcessor.\n"
						+ " *\n"
						+ " * @param series\n"
						+ " *            Series\n"
						+ " * @param idsp\n"
						+ " *            IDataSetProcessor\n"
						+ " * @param icsc\n"
						+ " *            IChartScriptContext\n"
						+ " */\n" );

	}
}
