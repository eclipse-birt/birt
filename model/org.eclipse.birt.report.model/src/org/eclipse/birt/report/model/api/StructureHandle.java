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
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.command.ComplexPropertyCommand;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.StructureContextUtil;

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

	protected StructureContext structContext;

	/**
	 * Constructs a handle for a structure within a list property of a given
	 * element.
	 * 
	 * @param element
	 *            handle to the report element.
	 * @param context
	 *            context of the structure
	 */

	public StructureHandle( DesignElementHandle element,
			StructureContext context )
	{
		super( element );

		structContext = context;

		checkValidation( );
	}

	/**
	 * Constructs a handle for a structure within a list property of a given
	 * element.
	 * 
	 * @param element
	 *            handle to the report element.
	 * @param ref
	 *            reference to the structure
	 * @deprecated
	 */

	public StructureHandle( DesignElementHandle element, MemberRef ref )
	{
		super( element );
		if ( ref == null )
			throw new IllegalArgumentException(
					"The member reference can not be null when creating the structure handle." ); //$NON-NLS-1$
		structContext = ref.getContext( );
		checkValidation( );
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
		StructureContext context = valueHandle.getContext( );
		assert context.isListRef( );
		assert context.getPropDefn( ).getTypeCode( ) == IPropertyType.STRUCT_TYPE;

		Object value = context.getValue( valueHandle.getModule( ) );
		if ( value instanceof Structure )
		{
			assert index == 0;
			this.structContext = ( (Structure) value ).getContext( );

		}
		else if ( value instanceof List )
		{
			List valueList = (List) value;

			assert index >= 0 && index < valueList.size( );
			Object item = valueList.get( index );

			assert item instanceof Structure;
			this.structContext = ( (Structure) item ).getContext( );

		}
		else
		{
			assert false;
		}

		checkValidation( );
	}

	private void checkValidation( )
	{
		if ( structContext == null )
			throw new IllegalArgumentException(
					"The context can not be null when creating a structure handle!" ); //$NON-NLS-1$
		assert structContext.getStructure( ) != null;
	}

	// Implementation of abstract method defined in base class.

	public IElementPropertyDefn getPropertyDefn( )
	{
		return structContext.getElementProp( );
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
		Structure struct = structContext.getStructure( );

		// user may cache this structure handle, when the structure is removed,
		// we must return null; in another word, if the structure has no
		// context, return null
		if ( struct.getContext( ) == null )
			return null;

		return struct;
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
	 * Get the integer value of a member.
	 * 
	 * @param memberName
	 *            name of the member to get
	 * @return integer value of the member, or <code>0</code> if the member is
	 *         not set or is not defined.
	 */

	protected int getIntProperty( String memberName )
	{
		MemberHandle handle = getMember( memberName );
		if ( handle == null )
			return 0;

		return handle.getIntValue( );
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
		return structContext.getStructDefn( );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ValueHandle#getContext()
	 */
	public StructureContext getContext( )
	{
		return structContext;
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
		Structure struct = (Structure) getStructure( );
		ComplexPropertyCommand cmd = new ComplexPropertyCommand( getModule( ),
				getElement( ) );
		cmd.removeItem( struct.getContext( ), struct );
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

	public void setExpressionProperty( String memberName, Expression value )
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

	public ExpressionHandle getExpressionProperty( String memberName )
	{
		PropertyDefn defn = (PropertyDefn) getDefn( ).getMember( memberName );
		if ( defn == null )
			return null;

		if ( defn.allowExpression( ) && !defn.isListType( ) )
			return new ExpressionHandle( getElementHandle( ),
					StructureContextUtil.createStructureContext( this,
							memberName ) );

		return null;
	}
}