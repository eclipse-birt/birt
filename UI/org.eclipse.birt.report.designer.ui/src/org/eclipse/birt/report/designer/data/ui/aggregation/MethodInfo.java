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

package org.eclipse.birt.report.designer.data.ui.aggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IMethodInfo;

/**
 * Represents the method information for both class and element. The class
 * includes the argument list, return type, and whether this method is static or
 * constructor,
 */

public class MethodInfo extends LocalizableInfo implements IMethodInfo
{

	/**
	 * The script type for return.
	 */

	private String returnType;

	/**
	 * Whether this method is static.
	 */

	private boolean isStatic = false;

	/**
	 * Whether this method is constructor.
	 */

	private boolean isConstructor = false;

	private List arguments;

	/**
	 * Constructs method definition.
	 * 
	 * @param isConstructor
	 *            whether this method is constructor
	 */

	public MethodInfo( boolean isConstructor )
	{
		super( );

		this.isConstructor = isConstructor;
	}

	/**
	 * Adds an optional argument list to the method information.
	 * 
	 * @param argumentList
	 *            an optional argument list
	 * 
	 */
	void addArgumentList( ArgumentInfoList argumentList )
	{
		if ( arguments == null )
			arguments = new ArrayList( );

		( (ArrayList) arguments ).add( argumentList );
	}

	/**
	 * Returns the iterator of argument definition. Each one is a list that
	 * contains <code>ArgumentInfoList</code>.
	 * 
	 * @return iterator of argument definition.
	 */

	public Iterator argumentListIterator( )
	{
		if ( arguments == null )
			return Collections.EMPTY_LIST.iterator( );

		return arguments.iterator( );
	}

	/**
	 * Returns the script type for return.
	 * 
	 * @return the script type for return
	 */

	public String getReturnType( )
	{
		return returnType;
	}

	/**
	 * Sets the script type for return.
	 * 
	 * @param returnType
	 *            the script type to set
	 */

	void setReturnType( String returnType )
	{
		this.returnType = returnType;
	}

	/**
	 * Returns whether this method is constructor.
	 * 
	 * @return true, if this method is constructor
	 */

	public boolean isConstructor( )
	{
		return isConstructor;
	}

	/**
	 * Returns whether this method is static.
	 * 
	 * @return true if this method is static
	 */

	public boolean isStatic( )
	{
		return isStatic;
	}

	/**
	 * Sets whether this method is static.
	 * 
	 * @param isStatic
	 *            true if this method is static
	 */

	void setStatic( boolean isStatic )
	{
		this.isStatic = isStatic;
	}

}