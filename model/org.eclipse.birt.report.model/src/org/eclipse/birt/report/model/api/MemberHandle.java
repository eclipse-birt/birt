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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;

/**
 * A handle to a member of a property structure. A structure list occurs in an
 * element property that contains a list of structures. The class handles a
 * member of one structure in the list.
 * 
 * 
 * @see StructureHandle
 * @see PropertyIterator
 */

public class MemberHandle extends SimpleValueHandle
{

	/**
	 * The reference to the member itself.
	 */

	protected MemberRef memberRef;

	/**
	 * Constructs a member handle with the given element handle and the member
	 * reference. The application usually does not create a handle directly.
	 * Instead, it obtains a handle by calling a method another handle.
	 * 
	 * @param element
	 *            handle to the report element that has the property that
	 *            contains the structure that contains the member the list that
	 *            contains the structure that contains the member.
	 * @param ref
	 *            The reference to the member.
	 */

	public MemberHandle( DesignElementHandle element, MemberRef ref )
	{
		super( element );
		memberRef = ref;
	}

	/**
	 * Constructs a member handle with the given structure handle and the member
	 * property definition. This form is used by the
	 * <code>StructureIterator</code> class.
	 * 
	 * @param structHandle
	 *            a handle to the structure
	 * @param member
	 *            definition of the member within the structure
	 */

	public MemberHandle( StructureHandle structHandle, StructPropertyDefn member )
	{
		super( structHandle.getElementHandle( ) );
		memberRef = new MemberRef( structHandle.getReference( ), member );
	}

	// Implementation of abstract method defined in base class.

	public IPropertyDefn getDefn( )
	{
		return memberRef.getMemberDefn( );
	}

	// Implementation of abstract method defined in base class.

	public Object getValue( )
	{
		Object value = memberRef.getValue( getDesign( ), getElement( ) );

		if ( value instanceof ElementRefValue )
			value = ( (ElementRefValue) value ).getName( );
		return value;
	}

	// Implementation of abstract method defined in base class.

	public void setValue( Object value ) throws SemanticException
	{
		PropertyCommand cmd = new PropertyCommand( getDesign( ), getElement( ) );
		cmd.setMember( memberRef, value );
	}

	// Implementation of abstract method defined in base class.

	public IElementPropertyDefn getPropertyDefn( )
	{
		return memberRef.getPropDefn( );
	}

	// Implementation of abstract method defined in base class.

	public MemberRef getReference( )
	{
		return memberRef;
	}

}