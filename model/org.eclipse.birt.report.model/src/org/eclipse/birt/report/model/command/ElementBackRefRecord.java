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

import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Records a change to the back reference of an element.
 * 
 * @see org.eclipse.birt.report.model.core.ReferenceableElement
 */

public class ElementBackRefRecord extends BackRefRecord
{

	/**
	 * The element is referred by <code>reference</code>.
	 */

	protected ReferenceableElement referred = null;

	/**
	 * The element is referred by the structure member.
	 */

	private MemberRef memberRef = null;

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param referred
	 *            the element to change
	 * @param reference
	 *            the element that refers to another element.
	 * @param propName
	 *            the property name. The type of the property must be
	 *            <code>PropertyType.ELEMENT_REF_TYPE</code>. Meanwhile, it
	 *            must not be <code>DesignElement.EXTENDS_PROP</code> and
	 *            <code>DesignElement.STYLE_PROP</code>
	 */

	public ElementBackRefRecord( Module module, ReferenceableElement referred,
			DesignElement reference, String propName )
	{
		super( module, reference, propName );
		this.referred = referred;

		assert referred != null;
	}

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param referred
	 *            the element to change
	 * @param reference
	 *            the element that refers to another element.
	 * @param propName
	 *            the property name. The type of the property must be
	 *            <code>PropertyType.ELEMENT_REF_TYPE</code>. Meanwhile, it
	 *            must not be <code>DesignElement.EXTENDS_PROP</code> and
	 *            <code>DesignElement.STYLE_PROP</code>
	 * @param memberRef
	 *            the member reference that refers to a structure member
	 */

	public ElementBackRefRecord( Module module, ReferenceableElement referred,
			DesignElement reference, String propName, MemberRef memberRef )
	{
		this( module, referred, reference, propName );
		this.memberRef = memberRef;

		assert memberRef != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		if ( undo )
		{
			if ( memberRef == null )
			{
				ElementPropertyDefn propDefn = reference
						.getPropertyDefn( propName );

				// To add client is done in resolving element reference.

				reference.getLocalProperty( module, propDefn );
			}
			else
			{
				// try to resolve the element reference for the structure
				// member.

				memberRef.getValue( module, reference );
			}
		}
		else
		{
			if ( memberRef == null )
			{
				removeElementRefOfProperty( );
			}
			else
			{
				removeBackRefOfStructMember( );
			}
		}
	}

	/**
	 * Removes the back reference that established by a structure member value.
	 */

	private void removeBackRefOfStructMember( )
	{
		Object value = memberRef.getStructure( module, reference )
				.getLocalProperty( module, memberRef.getMemberDefn( ) );

		assert value instanceof ElementRefValue;

		ElementRefValue refValue = (ElementRefValue) value;
		refValue.unresolved( refValue.getName( ) );

		referred.dropClient( reference, propName );
	}

	/**
	 * Removes the back reference that established by a element property value.
	 */

	private void removeElementRefOfProperty( )
	{
		Object value = reference.getLocalProperty( module, propName );
		if ( value instanceof ElementRefValue )
		{
			ElementRefValue refValue = (ElementRefValue) value;
			refValue.unresolved( refValue.getName( ) );

			referred.dropClient( reference );
		}
		else if ( value instanceof List )
		{
			List listValue = (List) value;
			for ( int i = 0; i < listValue.size( ); i++ )
			{
				ElementRefValue item = (ElementRefValue) listValue.get( i );
				if ( item.getElement( ) == referred )
				{
					item.unresolved( item.getName( ) );
					referred.dropClient( reference );
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return reference;
	}

}