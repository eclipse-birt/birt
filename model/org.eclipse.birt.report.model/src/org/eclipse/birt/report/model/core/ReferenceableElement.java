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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Represents an element that can be referenced using an element reference. This
 * element maintains a cached set of back-references to the "clients" so that
 * changes can be automatically propagated.
 * 
 */

public abstract class ReferenceableElement extends DesignElement
		implements
			IReferencable
{

	/**
	 * The list of cached clients.
	 */

	protected ArrayList clients = new ArrayList( );

	/**
	 * Default constructor.
	 */

	public ReferenceableElement( )
	{
	}

	/**
	 * Constructs the ReferenceableElement with the element name.
	 * 
	 * @param theName
	 *            the element name
	 */

	public ReferenceableElement( String theName )
	{
		super( theName );
	}

	/**
	 * Makes a clone of this referencable element. The cloned element has an
	 * empty client list,which is used to point to the clients who reference
	 * this element.
	 * 
	 * @return Object the cloned referencable element.
	 * 
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone( ) throws CloneNotSupportedException
	{
		ReferenceableElement element = (ReferenceableElement) super.clone( );
		element.clients = new ArrayList( );
		return element;
	}

	/**
	 * Adds a client. Should be called only from
	 * {@link DesignElement#setProperty( ElementPropertyDefn, Object )}.
	 * 
	 * @param client
	 *            The client to add.
	 * @param propName
	 *            the property name.
	 */

	public void addClient( DesignElement client, String propName )
	{
		clients.add( new BackRef( client, propName ) );
	}

	/**
	 * Drops a client. Should be called only from
	 * {@link DesignElement#setProperty( ElementPropertyDefn, Object )}.
	 * 
	 * @param client
	 *            The client to drop.
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

	/**
	 * Returns the list of clients for this element.
	 * 
	 * @return The list of clients.
	 */

	public List getClientList( )
	{
		return new ArrayList( clients );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IReferencable#hasReferences()
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
	 * @param module
	 *            the root node of the design tree.
	 */

	public void broadcast( NotificationEvent ev, Module module )
	{
		super.broadcast( ev, module );

		adjustDeliveryPath( ev );
		broadcastToClients( ev, module );
	}

	/**
	 * Sets the path type for the notification event.
	 * 
	 * @param ev
	 *            the notification event
	 */

	abstract protected void adjustDeliveryPath( NotificationEvent ev );

	/**
	 * Broadcasts the event to clients.
	 * 
	 * @param ev
	 *            the event to broadcast
	 * @param module
	 *            the module
	 */

	protected void broadcastToClients( NotificationEvent ev, Module module )
	{
		for ( int i = 0; i < clients.size( ); i++ )
		{
			( (BackRef) clients.get( i ) ).element.broadcast( ev, module );
		}
	}

	/**
	 * Updates the element reference which refers to the given referenceable
	 * element.
	 * 
	 */

	public void updateClientReferences( )
	{
		// creates another list for the iterator

		Iterator backRefIter = new ArrayList( clients ).iterator( );

		while ( backRefIter.hasNext( ) )
		{
			BackRef ref = (BackRef) backRefIter.next( );
			DesignElement client = ref.element;

			Module root = client.getRoot( );

			Object value = client.getLocalProperty( root, ref.propName );
			if ( value instanceof ElementRefValue )
			{
				ElementRefValue refValue = (ElementRefValue) value;
				refValue.unresolved( refValue.getName( ) );
				dropClient( client );
			}
			else if ( value instanceof List )
			{
				List valueList = (List) value;
				for ( int i = 0; i < valueList.size( ); i++ )
				{
					ElementRefValue item = (ElementRefValue) valueList.get( i );
					if ( item.getElement( ) == this )
					{
						item.unresolved( item.getName( ) );
						dropClient( client );
					}
				}
			}

			// for the style, send out a event to let UI repaint the element.
			// otherwise, try to resolve it.

			if ( IStyledElementModel.STYLE_PROP.equalsIgnoreCase( ref.propName ) )
				client.broadcast( new StyleEvent( client ) );
			else
				client.resolveElementReference( root, client
						.getPropertyDefn( ref.propName ) );
		}
	}

	/**
	 * Clears all clients.
	 */

	public void clearClients( )
	{
		clients = new ArrayList( );
	}

}