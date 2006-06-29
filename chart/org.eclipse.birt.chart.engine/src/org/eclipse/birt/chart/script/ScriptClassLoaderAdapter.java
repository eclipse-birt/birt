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

package org.eclipse.birt.chart.script;

/**
 * An adapter class for IScriptClassLoader. It first try to load class from
 * current context, if fail, try to load by parent loader.
 */
public class ScriptClassLoaderAdapter implements IScriptClassLoader
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IScriptClassLoader#loadClass(java.lang.String,
	 *      java.lang.ClassLoader)
	 */
	public Class loadClass( String className, ClassLoader parentLoader )
			throws ClassNotFoundException
	{
		try
		{
			return Class.forName( className );
		}
		catch ( ClassNotFoundException ex )
		{
			if ( parentLoader != null )
			{
				return parentLoader.loadClass( className );
			}

			throw ex;
		}
	}
}
