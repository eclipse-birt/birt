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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.command.ComplexPropertyCommand;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Handle to a structure within a list property. List properties contain objects
 * called structures. Structures have <em>members</em> that hold data values.
 * 
 * @see MemberHandle
 */

public class StructureHandle extends ValueHandle
{

	/**
	 * Reference to the structure.
	 */

	protected CachedMemberRef structRef;

	/**
	 * Constructs a handle for a structure within a list property of a given
	 * element.
	 * 
	 * @param element
	 *            handle to the report element.
	 * @param ref
	 *            reference to the structure
	 */

	public StructureHandle( DesignElementHandle element, MemberRef ref )
	{
		super( element );
		structRef = new CachedMemberRef( ref );
		if ( !structRef.checkOrCacheStructure( getModule( ), getElement( ) ) )
			throw new RuntimeException(
					"The structure is floating, and its handle is invalid!" ); //$NON-NLS-1$
	}

	/**
	 * Constructs a handle for a structure within a list property or a structure
	 * member.
	 * 
	 * @param valueHandle
	 *            handle to a list property or member
	 * @param index
	 *            index of the structure within the list
	 */

	public StructureHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle.getElementHandle( ) );
		structRef = new CachedMemberRef( new CachedMemberRef( valueHandle
				.getReference( ), index ) );
		if ( !structRef.checkOrCacheStructure( getModule( ), getElement( ) ) )
			throw new RuntimeException(
					"The structure is floating, and its handle is invalid!" ); //$NON-NLS-1$
	}

	// Implementation of abstract method defined in base class.

	public IElementPropertyDefn getPropertyDefn( )
	{
		return structRef.getPropDefn( );
	}

	/**
	 * Returns the structure. The application can cast this to the specific
	 * structure type to query the structure directly. Note: do not modify the
	 * structure directly; use the <code>MemberHandle</code> class for all
	 * modifications.
	 * 
	 * @return the structure
	 */

	public IStructure getStructure( )
	{
		return structRef.getStructure( getModule( ), getElement( ) );
	}

	/**
	 * Gets the value of a member.
	 * 
	 * @param memberName
	 *            name of the member to get
	 * @return String value of the member, or <code>null</code> if the member is
	 *         not set or is not found.
	 */

	public Object getProperty( String memberName )
	{
		MemberHandle handle = getMember( memberName );
		if ( handle == null )
			return null;

		return handle.getValue( );
	}

	/**
	 * Get the string value of a member.
	 * 
	 * @param memberName
	 *            name of the member to get
	 * @return String value of the member, or <code>null</code> if the member is
	 *         not set or is not found.
	 */

	protected String getStringProperty( String memberName )
	{
		MemberHandle handle = getMember( memberName );
		if ( handle == null )
			return null;

		return handle.getStringValue( );
	}

	/**
	 * Sets the value of the member.
	 * 
	 * @param memberName
	 *            name of the member to set.
	 * @param value
	 *            the value to set
	 * @throws SemanticException
	 *             if the member name is not defined on the structure or the
	 *             value is not valid for the member.
	 */

	public void setProperty( String memberName, Object value )
			throws SemanticException
	{
		MemberHandle memberHandle = getMember( memberName );
		if ( memberHandle == null )
			throw new PropertyNameException( getElement( ), getStructure( ),
					memberName );

		memberHandle.setValue( value );

		structRef.cacheStructureInForce( getModule( ), getElement( ) );
	}

	/**
	 * Set the value of a member without throwing exceptions. That is the set
	 * operation should not failed. This method is designed to be called by the
	 * sub-class where that it is certain that a set operation should never
	 * failed.
	 * <p>
	 * Note that this method will internal swallow exceptions thrown when
	 * performing the set operation. The exception will be deemed as internal
	 * error. So calling this method when you are sure that exception is a
	 * programming error.
	 * 
	 * @param memberName
	 *            name of the member to set.
	 * @param value
	 *            value to set.
	 * 
	 */

	protected final void setPropertySilently( String memberName, Object value )
	{
		try
		{
			setProperty( memberName, value );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Returns the definition of the structure.
	 * 
	 * @return the structure definition
	 */

	public IStructureDefn getDefn( )
	{
		return structRef.getStructDefn( );
	}

	/**
	 * Returns a handle to a structure member.
	 * 
	 * @param memberName
	 *            the name of the member
	 * @return a handle to the member or <code>null</code> if the member is not
	 *         defined on the structure.
	 */

	public MemberHandle getMember( String memberName )
	{
		StructPropertyDefn memberDefn = (StructPropertyDefn) getDefn( )
				.getMember( memberName );
		if ( memberDefn == null )
			return null;

		return new MemberHandle( this, memberDefn );
	}

	/**
	 * Returns an iterator over the members of this structure. The iterator is
	 * of type <code>MemberIterator</code>.
	 * 
	 * @return an iterator over the members of the structure.
	 * @see MemberIterator
	 */

	public Iterator iterator( )
	{
		return new MemberIterator( this );
	}

	/**
	 * Returns a reference to the structure.
	 * 
	 * @return a reference to the structure
	 * 
	 * @see org.eclipse.birt.report.model.core.MemberRef
	 */

	public MemberRef getReference( )
	{
		return structRef;
	}

	/**
	 * Removes this structure from a list property or member. Once the structure
	 * is dropped, the handle should not be used to do any setter operations.
	 * 
	 * @throws PropertyValueException
	 *             if the structure is not contained in the list.
	 */

	public void drop( ) throws PropertyValueException
	{
		MemberRef listRef = null;
		MemberRef ref = getReference( );
		ElementPropertyDefn propDefn = (ElementPropertyDefn) getPropertyDefn( );

		if ( propDefn.isList( ) )
		{
			if ( ref.getDepth( ) == 1 )
			{
				listRef = new CachedMemberRef( propDefn );
			}
			else if ( ref.getDepth( ) == 2 )
			{
				listRef = ref.getParentRef( );
			}
		}
		else
		{
			assert ref.getDepth( ) == 1;

			listRef = new CachedMemberRef( propDefn, (StructPropertyDefn) ref
					.getMemberDefn( ) );

		}

		ComplexPropertyCommand cmd = new ComplexPropertyCommand( getModule( ),
				getElement( ) );
		cmd.removeItem( listRef, getStructure( ) );
	}

	/**
	 * Returns externalized message.
	 * 
	 * @param textIDProp
	 *            the display key property name
	 * @param textProp
	 *            the property name
	 * @return externalized message.
	 */

	public String getExternalizedValue( String textIDProp, String textProp )
	{
		return ModelUtil.getExternalizedStructValue( getElement( ).getRoot( ),
				getStructure( ), textIDProp, textProp, ThreadResources
						.getLocale( ) );
	}

	/**
	 * Justifies whether this structure handle is generated in design time.
	 * 
	 * @return <true> if the structure handle is generated in design time,
	 *         otherwise return <false>.
	 */
	public boolean isDesignTime( )
	{
		return getStructure( ).isDesignTime( );
	}

	/**
	 * 
	 * @param isDesignTime
	 * @throws SemanticException
	 */
	public void setDesignTime( boolean isDesignTime ) throws SemanticException
	{
		MemberHandle memberHandle = getMember( StyleRule.IS_DESIGN_TIME_MEMBER );
		if ( memberHandle != null )
			memberHandle.setValue( Boolean.valueOf( isDesignTime ) );

	}

	/**
	 * Sets the value of the member as an expression.
	 * 
	 * @param memberName
	 *            name of the member to set.
	 * @param value
	 *            the expression to set
	 * @throws SemanticException
	 *             if the member name is not defined on the structure or the
	 *             value is not valid for the member.
	 */

	public void setExprssionProperty( String memberName, Expression value )
			throws SemanticException
	{
		setProperty( memberName, value );
	}

	/**
	 * Gets the value of the member as an expression.
	 * 
	 * @param memberName
	 *            name of the member to set.
	 * @return the expression
	 * @throws SemanticException
	 *             if the member name is not defined on the structure or the
	 *             value is not valid for the member.
	 */

	public ExpressionHandle getExprssionProperty( String memberName )
	{
		PropertyDefn defn = (PropertyDefn) getDefn( ).getMember( memberName );
		if ( defn == null )
			return null;

		if ( defn.allowExpression( ) )
			return new ExpressionHandle( getElementHandle( ),
					new CachedMemberRef( getReference( ), memberName ) );

		return null;
	}
}