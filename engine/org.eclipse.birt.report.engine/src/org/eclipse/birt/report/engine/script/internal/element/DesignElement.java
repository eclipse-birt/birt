/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDesignElement;
import org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign;
import org.eclipse.birt.report.engine.script.internal.ElementUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;

public class DesignElement implements IDesignElement
{

	protected DesignElementHandle handle;

	public DesignElement( DesignElementHandle handle )
	{
		this.handle = handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getStyle()
	 */
	public IScriptStyleDesign getStyle( )
	{
		return new StyleDesign( handle.getPrivateStyle( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getName()
	 */

	public String getName( )
	{
		return handle.getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getQualifiedName()
	 */

	public String getQualifiedName( )
	{

		return handle.getQualifiedName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setName(java.lang.String)
	 */

	public void setName( String name ) throws ScriptException
	{
		try
		{
			handle.setName( name );
		} catch ( NameException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public String getNamedExpression( String name )
	{
		// TODO: Shouldn't this be an instance of Expression?
		Object prop = getUserProperty( name );
		if ( prop == null || ( prop instanceof String ) )
			return null;
		return ( String ) prop;
	}

	public void setNamedExpression( String name, String exp )
			throws ScriptException
	{
		// TODO: We need to keep named expressions and other user properties
		// separate?
		setUserProperty( name, exp );
	}

	public Object getUserProperty( String name )
	{
		return handle.getProperty( name );
	}

	public void setUserProperty( String name, Object value )
			throws ScriptException
	{
		try
		{
			handle.setProperty( name, value );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public IDesignElement getParent( )
	{
		return ElementUtil.getElement( handle.getContainer( ) );
	}

}
