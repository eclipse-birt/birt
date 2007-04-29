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
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
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

	protected Module root = null;

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
	 * @param theRoot
	 *            the module.
	 * @param ns
	 *            the name space to use.
	 * @param symbol
	 *            the element to insert or remove.
	 * @param isAdd
	 *            whether to add (true) or remove (false) the element.
	 */

	public NameSpaceRecord( Module theRoot, int ns, DesignElement symbol,
			boolean isAdd )
	{
		root = theRoot;
		element = symbol;
		nameSpaceID = ns;
		add = isAdd;

		assert root != null;
		assert element != null;

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
		NameSpace ns = root.getNameSpace( nameSpaceID );
		if ( add && !undo || !add && undo )
		{
			if ( element instanceof IReferencableElement )
			{
				IReferencableElement originalElement = (IReferencableElement) root
						.resolveElement(
								element.getName( ),
								nameSpaceID,
								element
										.getPropertyDefn( IDesignElementModel.NAME_PROP ) );
				ns.insert( element );

				// drop the element from the cached name manager

				root.getNameManager( ).dropElement( element );
				if ( originalElement != null )
					updateAllElementReferences( originalElement );
			}
			else
			{
				ns.insert( element );

				// drop the element from the cached name manager

				root.getNameManager( ).dropElement( element );
			}
		}
		else
		{
			ns.remove( element );

			if ( element instanceof ReferenceableElement )
				updateAllElementReferences( (ReferenceableElement) element );
		}
	}

	private void updateAllElementReferences( IReferencableElement referred )
	{
		List clients = referred.getClientList( );
		Iterator iter = clients.iterator( );
		while ( iter.hasNext( ) )
		{
			BackRef ref = (BackRef) iter.next( );
			DesignElement client = ref.element;

			CachedMemberRef memberRef = ref.getCachedMemberRef( );
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
						// Now special deal with structure list.
						// element -> list-property -> structure-> member is
						// elementRefValue

						int index = memberRef.getIndex( );
						if ( index >= 0 && index < valueList.size( ) )
						{
							Structure structure = (Structure) valueList
									.get( index );
							Iterator iterator = structure.getDefn( )
									.getPropertyIterator( );
							while ( iterator.hasNext( ) )
							{
								IPropertyDefn propDefn = (IPropertyDefn) iterator
										.next( );

								// if member is element ref type , then do
								// unreslove.

								if ( propDefn.getTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE )
								{
									ElementRefValue tempRefValue = (ElementRefValue) structure
											.getProperty( root,
													(PropertyDefn) propDefn );

									if ( referred == tempRefValue.getElement( ) )
									{
										tempRefValue.unresolved( tempRefValue
												.getName( ) );
										referred.dropClient( client );

										// reslove member
										StructPropertyDefn structDefn = (StructPropertyDefn) structure
												.getDefn( ).findProperty(
														propDefn.getName( ) );

										ReferenceValueUtil
												.resolveElementReference(
														structure, root,
														structDefn,
														tempRefValue );

										break;

									}
								}
							}
						}
					}
				}
			}
			else if ( value instanceof DesignElement )
			{
				// Do nothing now.
			}
			else
			{
				assert false;
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
		return root;
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