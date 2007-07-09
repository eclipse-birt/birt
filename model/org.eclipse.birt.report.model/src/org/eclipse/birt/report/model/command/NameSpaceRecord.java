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

package org.eclipse.birt.report.model.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.NameEvent;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

/**
 * Records an insertion into, or deletion from a name space.
 * 
 */

public class NameSpaceRecord extends SimpleRecord
{

	/**
	 * The module that has the name space.
	 */

	protected INameHelper nameHelper = null;

	/**
	 * The element to add or remove.
	 */

	protected DesignElement element = null;

	/**
	 * The name space to use.
	 */

	protected int nameSpaceID = 0;

	/**
	 * Whether to add or remove the element.
	 */

	protected boolean add = false;

	/**
	 * Constructor.
	 * 
	 * @param nameHelper
	 *            the name container
	 * @param ns
	 * @param symbol
	 *            the element to insert or remove.
	 * @param isAdd
	 *            whether to add (true) or remove (false) the element.
	 */

	public NameSpaceRecord( INameHelper nameHelper, int ns,
			DesignElement symbol, boolean isAdd )
	{
		this.nameHelper = nameHelper;
		this.nameSpaceID = ns;
		element = symbol;
		add = isAdd;

		assert element != null;
		assert nameHelper != null;

		// This record should never appear by itself in the activity stack.
		// Instead, this record should appear as part of a larger task,
		// and the label for that task should appear in the UI.

		if ( add )
			label = ModelMessages
					.getMessage( MessageConstants.INSERT_ELEMENT_MESSAGE );
		else
			label = ModelMessages
					.getMessage( MessageConstants.DELETE_ELEMENT_MESSAGE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		int nameSpaceID = ( (ElementDefn) element.getDefn( ) ).getNameSpaceID( );
		Module root = nameHelper.getElement( ).getRoot( );
		NameSpace ns = nameHelper.getNameSpace( nameSpaceID );
		assert root != null;
		if ( add && !undo || !add && undo )
		{
			if ( element instanceof IReferencableElement )
			{
				IReferencableElement originalElement = (IReferencableElement) root
						.resolveElement( element.getName( ), null, element
								.getDefn( ) );
				ns.insert( element );

				// drop the element from the cached name manager

				nameHelper.dropElement( element );
				if ( originalElement != null )
					updateAllElementReferences( root, originalElement );
			}
			else
			{
				ns.insert( element );

				// drop the element from the cached name manager

				nameHelper.dropElement( element );
			}
		}
		else
		{
			ns.remove( element );

			if ( element instanceof ReferenceableElement )
				updateAllElementReferences( root,
						(ReferenceableElement) element );
		}
	}

	private void updateAllElementReferences( Module root,
			IReferencableElement referred )
	{
		List clients = referred.getClientList( );
		Iterator iter = clients.iterator( );
		while ( iter.hasNext( ) )
		{
			BackRef ref = (BackRef) iter.next( );
			DesignElement client = ref.element;

			Object value = client.getLocalProperty( root, ref.propName );
			if ( value instanceof ElementRefValue )
			{
				ElementRefValue refValue = (ElementRefValue) value;
				refValue.unresolved( refValue.getName( ) );

				referred.dropClient( client );

				client.resolveElementReference( root, client
						.getPropertyDefn( ref.propName ) );
			}
			else if ( value instanceof List )
			{
				List valueList = (List) value;
				for ( int i = 0; i < valueList.size( ); i++ )
				{
					Object tempObj = valueList.get( i );
					if ( tempObj instanceof ElementRefValue )
					{
						ElementRefValue item = (ElementRefValue) tempObj;
						if ( referred == item.getElement( ) )
						{
							item.unresolved( item.getName( ) );
							referred.dropClient( client );
							ReferenceValueUtil.resolveElementReference( root,
									client, client
											.getPropertyDefn( ref.propName ),
									item );
							break;
						}
					}
					else
					{
						updatePropertyListnMemberCase( root, referred, ref
								.getCachedMemberRef( ), valueList, client );
					}
				}
			}
			else if ( value instanceof DesignElement )
			{
				// Do nothing now.
			}
			else
			{
				// TODO: assertion is failed for bug 194843
				// assert false;
			}
		}
	}

	/**
	 * Now special deal with case: element -> list-property -> structure->
	 * member is elementRefValue
	 * 
	 * @param referred
	 *            reference element
	 * @param memberRef
	 *            member ref
	 * @param valueList
	 *            structure list
	 * @param client
	 *            client element
	 */

	private void updatePropertyListnMemberCase( Module root,
			IReferencableElement referred, CachedMemberRef memberRef,
			List valueList, DesignElement client )
	{
		int index = memberRef.getIndex( );
		if ( index >= 0 && index < valueList.size( ) )
		{
			Structure structure = (Structure) valueList.get( index );
			Iterator iterator = structure.getDefn( ).getPropertyIterator( );
			while ( iterator.hasNext( ) )
			{
				IPropertyDefn propDefn = (IPropertyDefn) iterator.next( );

				// if member is element ref type , then do
				// unreslove.

				if ( propDefn.getTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE )
				{
					ElementRefValue tempRefValue = (ElementRefValue) structure
							.getProperty( root, (PropertyDefn) propDefn );

					if ( referred == tempRefValue.getElement( ) )
					{
						tempRefValue.unresolved( tempRefValue.getName( ) );
						referred.dropClient( client );

						// reslove member

						ReferenceValueUtil.resolveElementReference( structure,
								root, (StructPropertyDefn) propDefn,
								tempRefValue );

						break;
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return nameHelper.getElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		NotificationEvent event = null;
		if ( this.add )
			event = new NameEvent( element, null, element.getName( ) );
		else
			event = new NameEvent( element, element.getName( ), null );
		return event;
	}

}