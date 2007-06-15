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
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * The implementation of methods on <code>IReferencableElement</code>.
 * 
 */

public class ReferenceableElementAdapter
		implements
			IReferencableElement,
			Cloneable
{

	/**
	 * The list of cached clients.
	 */

	protected ArrayList clients = new ArrayList( );

	/**
	 * The design element.
	 */

	private DesignElement element;

	/**
	 * The constructor.
	 * 
	 * @param element
	 *            the element
	 */

	public ReferenceableElementAdapter( ReferenceableElement element )
	{
		this.element = element;
	}

	/**
	 * The constructor.
	 * 
	 * @param element
	 *            the styled element
	 */

	public ReferenceableElementAdapter( ReferencableStyledElement element )
	{
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#doClone(org.eclipse.birt.report.model.elements.strategy.CopyPolicy)
	 */

	public Object clone( ) throws CloneNotSupportedException
	{
		ReferenceableElementAdapter adapter = (ReferenceableElementAdapter) super
				.clone( );

		adapter.clients = new ArrayList( );
		return adapter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#addClient(org.eclipse.birt.report.model.core.DesignElement,
	 *      java.lang.String)
	 */

	public void addClient( DesignElement client, String propName )
	{
		clients.add( new BackRef( client, propName ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#dropClient(org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void dropClient( DesignElement client )
	{
		dropClient( client, (String) null );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#dropClient(org.eclipse.birt.report.model.core.DesignElement,
	 *      java.lang.String)
	 */

	public void dropClient( DesignElement client, String propName )
	{

		for ( int i = 0; i < clients.size( ); i++ )
		{
			BackRef ref = (BackRef) clients.get( i );
			if ( ref.element == client
					&& ( propName == null || ref.propName.equals( propName ) ) )
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
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#getClientList()
	 */

	public List getClientList( )
	{
		return new ArrayList( clients );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#hasReferences()
	 */

	public boolean hasReferences( )
	{
		return !clients.isEmpty( );
	}

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
			BackRef ref = (BackRef) clients.get( i );
			DesignElement target = ref.element;
			if ( IDesignElementModel.EXTENDS_PROP.equalsIgnoreCase( ref
					.getPropertyName( ) ) )
				ev.setDeliveryPath( NotificationEvent.EXTENDS_EVENT );
			else if ( IStyledElementModel.STYLE_PROP.equalsIgnoreCase( ref
					.getPropertyName( ) ) )
				ev.setDeliveryPath( NotificationEvent.STYLE_CLIENT );
			else
				ev.setDeliveryPath( NotificationEvent.ELEMENT_CLIENT );

			target.broadcast( ev, module );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#updateClientReferences()
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

			CachedMemberRef memberRef = ref.getCachedMemberRef( );
			Object value = null;
			if ( memberRef != null )
			{
				value = client
						.getLocalProperty( root, memberRef.getPropDefn( ) );
			}
			else
			{
				value = client.getLocalProperty( root, ref.propName );
			}
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
					if ( item.getElement( ) == element )
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#clearClients()
	 */

	public void clearClients( )
	{
		clients = new ArrayList( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#addClient(org.eclipse.birt.report.model.core.DesignElement,
	 *      org.eclipse.birt.report.model.core.CachedMemberRef)
	 */
	public void addClient( DesignElement client, CachedMemberRef cachedMemberRef )
	{
		clients.add( new BackRef( client, cachedMemberRef ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#dropClient(org.eclipse.birt.report.model.core.DesignElement,
	 *      org.eclipse.birt.report.model.core.CachedMemberRef)
	 */
	public void dropClient( DesignElement client,
			CachedMemberRef cachedMemberRef )
	{
		for ( int i = 0; i < clients.size( ); i++ )
		{
			BackRef ref = (BackRef) clients.get( i );
			if ( ref.element == client
					&& ( cachedMemberRef == null || cachedMemberRef.equals( ref
							.getCachedMemberRef( ) ) ) )
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
	 * @see org.eclipse.birt.report.model.core.IReferencableElement#insertClient(int,
	 *      org.eclipse.birt.report.model.core.DesignElement,
	 *      org.eclipse.birt.report.model.core.CachedMemberRef)
	 */
	public void insertClient( int index, DesignElement client,
			CachedMemberRef cachedMemberRef )
	{
		assert index > 0 && index <= clients.size( );
		clients.add( index, new BackRef( client, cachedMemberRef ) );
	}

}