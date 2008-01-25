/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.util.mediator.request;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Request;

/**
 * An Object used to communicate with Views. Request encapsulates the
 * information views need to perform various functions. Requests are used for
 * obtaining selection, and performing generic operations.
 */

public class ReportRequest extends Request
{

	/**
	 * Selection request <code>SELECTION</code>
	 */
	public static final String SELECTION = "selection"; //$NON-NLS-1$

	/**
	 * Open editor request. <code>OPEN_EDITOR</code>
	 */
	public static final String OPEN_EDITOR = "open editor"; //$NON-NLS-1$

	/**
	 * Open editor request. <code>OPEN_EDITOR</code>
	 */
	public static final String LOAD_MASTERPAGE = "load masterpage"; //$NON-NLS-1$

	/**
	 * Create element request. <code>CREATE_ELEMENT</code>
	 */
	public static final String CREATE_ELEMENT = "create element"; //$NON-NLS-1$

	/**
	 * Added for fixing bugs 144165 and 151317 Create scalarparameter or
	 * resultsetcolumn request.
	 */
	public static final String CREATE_SCALARPARAMETER_OR_RESULTSETCOLUMN = "create scalarparameter or resultsetcolumn"; //$NON-NLS-1$

	private Object source;

	private IRequestConvert convert;

	private List selectionObject = new ArrayList( );

	/**
	 * Create a report request.
	 */
	public ReportRequest( )
	{
		this( null, SELECTION );
	}

	public ReportRequest( String type )
	{
		this( null, type );
	}

	/**
	 * Create a report request with give source object.
	 * 
	 * @param source
	 */
	public ReportRequest( Object source )
	{
		this( source, SELECTION );
	}

	public ReportRequest( Object source, String type )
	{
		super( );
		setSource( source );
		setType( type );
	}

	/**
	 * Get the source of request.
	 * 
	 * @return Returns the source.
	 */
	public Object getSource( )
	{
		return source;
	}

	/**
	 * Set the source of request.
	 * 
	 * @param source
	 *            The source to set.
	 */
	public void setSource( Object source )
	{
		this.source = source;
	}

	/**
	 * Get the selection objcect of request source.
	 * 
	 * @return Returns the selectionObject.
	 */
	public List getSelectionObject( )
	{
		return selectionObject;
	}

	/**
	 * Get the selection objcect of request source.
	 * 
	 * @return Returns the selectionObject.
	 */
	public List getSelectionModelList( )
	{
		if ( getRequestConvert( ) != null )
		{
			return getRequestConvert( ).convertSelectionToModelLisr( getSelectionObject( ) );
		}
		return getSelectionObject( );
	}

	/**
	 * Set the selection object of reqeust source
	 * 
	 * @param selectionObject
	 *            The selectionObject to set.
	 */
	public void setSelectionObject( List selectionObject )
	{
		assert selectionObject != null;
		this.selectionObject = selectionObject;
	}

	/**
	 * @return Returns the convert.
	 */
	public IRequestConvert getRequestConvert( )
	{
		return convert;
	}

	/**
	 * @param convert
	 *            The convert to set.
	 */
	public void setRequestConvert( IRequestConvert convert )
	{
		this.convert = convert;
	}

}