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
 * An Object used to communicate with Views. Request encapsulates the information
 * views need to perform various functions. Requests are used for obtaining selection,
 * and performing generic operations.
 */

public class ReportRequest extends Request
{
	
	/**
	 * Selection request <code>SELECTION</code>
	 */
	public static final String SELECTION = "selection";
	
	/**
	 * Open masterpage request. <code>OPENMATERPAGE</code>
	 */
	public static final String OPEN_MATERPAGE = "open masterPage";
	
	/**
	 * Create element request. <code>CREATE_ELEMENT</code>
	 */
	public static final String CREATE_ELEMENT = "create element";
	
	private Object source;
	
	private List selectionObject = new ArrayList();
	
	/**
	 * Create a report request.
	 */
	public ReportRequest( )
	{
		this(null);
	}
	/**
	 * Create a report request with give source object.
	 * @param source
	 */
	public ReportRequest( Object source )
	{
		super( );
		this.source = source;
	}
	
	/**
	 * Get the source of request.
	 * @return Returns the source.
	 */
	public Object getSource( )
	{
		return source;
	}
	
	/**
	 * Set the source of request.
	 * @param source The source to set.
	 */
	public void setSource( Object source )
	{
		this.source = source;
	}
	
	/**
	 * Get the selection objcect of request source.
	 * @return Returns the selectionObject.
	 */
	public List getSelectionObject( )
	{
		return selectionObject;
	}
	
	/**
	 * Set the selection object of reqeust source
	 * @param selectionObject The selectionObject to set.
	 */
	public void setSelectionObject( List selectionObject )
	{
		assert selectionObject != null;
		this.selectionObject = selectionObject;
	}
}
