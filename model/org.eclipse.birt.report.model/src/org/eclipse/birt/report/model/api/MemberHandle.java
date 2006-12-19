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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.command.ComplexPropertyCommand;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

/**
 * A handle to a member of a property structure. A structure list occurs in an
 * element property that contains a list of structures. The class handles a
 * member of one structure in the list.
 * 
 * 
 * @see StructureHandle
 */

public class MemberHandle extends SimpleValueHandle
{

	/**
	 * The reference to the member itself.
	 */

	protected CachedMemberRef memberRef;

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
		memberRef = new CachedMemberRef( ref );
		if ( !memberRef.checkOrCacheStructure( elementHandle.getModule( ),
				elementHandle.getElement( ) ) )
			throw new RuntimeException(
					"The structure is floating, and its handle is invalid!" ); //$NON-NLS-1$
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
		memberRef = new CachedMemberRef( structHandle.getReference( ), member );
		if ( !memberRef.checkOrCacheStructure( elementHandle.getModule( ),
				elementHandle.getElement( ) ) )
			throw new RuntimeException(
					"The structure is floating, and its handle is invalid!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#getDefn()
	 */
	public IPropertyDefn getDefn( )
	{
		return memberRef.getMemberDefn( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#getValue()
	 */
	public Object getValue( )
	{
		Object value = memberRef.getValue( getModule( ), getElement( ) );

		if ( value instanceof ReferenceValue )
			return ReferenceValueUtil.needTheNamespacePrefix(
					(ReferenceValue) value, getElementHandle( ).getModule( ) );

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#setValue(java.lang.Object)
	 */
	public void setValue( Object value ) throws SemanticException
	{
		PropertyCommand cmd = new PropertyCommand( getModule( ), getElement( ) );
		cmd.setMember( memberRef, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#removeItem(int)
	 */
	public void removeItem( int posn ) throws PropertyValueException
	{
		ComplexPropertyCommand cmd = new ComplexPropertyCommand( getModule( ),
				getElement( ) );
		cmd.removeItem( memberRef, posn );

	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#addItem(java.lang.Object)
	 */
	public void addItem( Object item ) throws SemanticException
	{
		if ( item == null )
			return;
		if ( item instanceof IStructure )
			super.addItem( (IStructure) item );
		else
		{
			ComplexPropertyCommand cmd = new ComplexPropertyCommand( getModule( ),
					getElement( ) );
			cmd.addItem( memberRef, item );
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isReadOnly()
	 */

	public boolean isReadOnly( )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isVisible()
	 */

	public boolean isVisible( )
	{
		return true;
	}

}