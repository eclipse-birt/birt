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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * Represents a structure that can be referenced using a property of name type.
 * This structure maintains a cached set of back-references to the "clients" so
 * that changes can be automatically propagated.
 *  
 */

public abstract class ReferencableStructure extends Structure
		implements
			IReferencable
{

	/**
	 * The list of cached clients.
	 */

	protected ArrayList clients = new ArrayList( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#isReferencable()
	 */

	public boolean isReferencable( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencable#addClient(org.eclipse.birt.report.model.core.DesignElement,
	 *      java.lang.String)
	 */
	public void addClient( DesignElement client, String propName )
	{
		clients.add( new BackRef( client, propName ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencable#dropClient(org.eclipse.birt.report.model.core.DesignElement)
	 */
	public void dropClient( DesignElement client )
	{
		for ( int i = 0; i < clients.size( ); i++ )
		{
			if ( ( (BackRef) clients.get( i ) ).element == client )
			{
				clients.remove( i );
				return;
			}
		}
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencable#getClientList()
	 */

	public List getClientList( )
	{
		return clients;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#hasReferences()
	 */

	public boolean hasReferences( )
	{
		return !clients.isEmpty( );
	}

	/**
	 * Sends the event to all clients in addition to the routing for a design
	 * element.
	 * 
	 * @param ev
	 *            the event to send
	 */

	public void broadcast( NotificationEvent ev )
	{
		ev.setDeliveryPath( NotificationEvent.STRUCTURE_CLIENT );
		for ( int i = 0; i < clients.size( ); i++ )
		{
			( (BackRef) clients.get( i ) ).element.broadcast( ev );
		}
	}

	/**
	 * Checks whether the member of the input name is the referencable member or
	 * not.
	 * 
	 * @param memberName
	 *            the member name to check
	 * @return true if the member with the given name is referencable, otherwise
	 *         false
	 */

	public abstract boolean isReferencableProperty( String memberName );	
}