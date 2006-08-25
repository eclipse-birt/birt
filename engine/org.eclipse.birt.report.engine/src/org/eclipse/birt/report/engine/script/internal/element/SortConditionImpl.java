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
import org.eclipse.birt.report.engine.api.script.element.ISortCondition;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;

/**
 * Implements of Sort Condition 
 *
 */

public class SortConditionImpl implements ISortCondition
{
	private SortKeyHandle sort;
	
	private DesignElementHandle handle;
	
	public SortConditionImpl( SortKeyHandle sort , DesignElementHandle handle )
	{
		this.handle = handle ;
		this.sort = sort;
	}
	
	public String getDirection( )
	{
		return sort.getDirection( );
	}

	public String getKey( )
	{
		return sort.getKey( );
	}

	public void setDirection( String direction ) throws ScriptException
	{
		checkHandle();
		try
		{
			sort.setDirection( direction );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}
	

	public void setKey( String key ) throws ScriptException
	{
		
		//key is required
		
		checkKey( key );
		try
		{
			sort.setKey( key );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}
	
	private void checkHandle() throws ScriptException
	{
		if( sort != null )
			return;
		throw new ScriptException( "SortKeyHandle is null " ); //$NON-NLS-1$
	}
	
	private void checkKey( String key ) throws ScriptException
	{
		if( sort != null )
			return;
		
		SortKey c = new SortKey();
		c.setKey( key );
		
		if( handle instanceof ListGroupHandle )
		{
			PropertyHandle propHandle = handle.getPropertyHandle( IGroupElementModel.SORT_PROP );
			try
			{
				sort = (SortKeyHandle) propHandle.addItem( c );
			}
			catch ( SemanticException e )
			{
				throw new ScriptException( e.getLocalizedMessage( ) );
			}
		}
		else if( handle instanceof ListingHandle  )
		{
			PropertyHandle propHandle = handle.getPropertyHandle( IListingElementModel.SORT_PROP );
			try
			{
				sort = (SortKeyHandle) propHandle.addItem( c );
			}
			catch ( SemanticException e )
			{
				throw new ScriptException( e.getLocalizedMessage( ) );
			}
		}
	}

}
