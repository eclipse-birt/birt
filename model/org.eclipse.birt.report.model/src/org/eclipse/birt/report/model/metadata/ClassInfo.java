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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Represents the script object definition. This definition defines one
 * constructor, several members and methods. It also includes the name, display
 * name ID, and tool tip ID.
 */

public class ClassInfo extends LocalizableInfo
{

	/**
	 * The constructor definition.
	 */

	private List constructors;

	/**
	 * The list of method definitions.
	 */

	private List methods;

	/**
	 * The list of member definitions.
	 */

	private List members;

	/**
	 * The flag indicates if an object is native or not.
	 */

	private boolean isNative = false;

	/**
	 * Adds one method definition to this class definition.
	 * 
	 * @param methodInfo
	 *            the definition of the method to add
	 * @throws MetaDataException
	 *             if the duplicate method name exists.
	 */

	void addMethod( MethodInfo methodInfo ) throws MetaDataException
	{
		if ( methods == null )
		{
			methods = new ArrayList( );
		}

		if ( StringUtil.isBlank( methodInfo.getName( ) ) )
			throw new MetaDataException( new String[]{methodInfo.getName( )},
					MetaDataException.MISSING_METHOD_NAME );

		methods.add( methodInfo );
	}

	/**
	 * Adds one member definition to this class definition.
	 * 
	 * @param memberDefn
	 *            the definition of the member to add
	 * @throws MetaDataException
	 *             if the duplicate member name exists.
	 */

	void addMemberDefn( MemberInfo memberDefn ) throws MetaDataException
	{
		if ( members == null )
		{
			members = new ArrayList( );
		}

		if ( StringUtil.isBlank( memberDefn.getName( ) ) )
			throw new MetaDataException( new String[]{memberDefn.getName( )},
					MetaDataException.MISSING_MEMBER_NAME );

		if ( findMember( memberDefn.getName( ) ) != null )
		{
			throw new MetaDataException( new String[]{memberDefn.getName( ),
					name}, MetaDataException.DUPLICATE_MEMBER_NAME );
		}

		members.add( memberDefn );
	}

	/**
	 * Returns the method definition list.
	 * 
	 * @return list of method definitions
	 */

	public List getMethods( )
	{
		if ( methods != null )
			return new ArrayList( methods );

		return new ArrayList( );
	}

	/**
	 * Get the method definition given the method name.
	 * 
	 * @param name
	 *            the name of the method to get
	 * @return the definition of the method to get
	 */

	public List getMethod( String name )
	{
		List methodsToReturn = new ArrayList( );

		if ( methods == null )
		{
			return methodsToReturn;
		}

		for ( Iterator iter = methods.iterator( ); iter.hasNext( ); )
		{
			MethodInfo methodDefn = (MethodInfo) iter.next( );

			if ( methodDefn.getName( ).equalsIgnoreCase( name ) )
			{
				methodsToReturn.add( methodDefn );
			}
		}

		return methodsToReturn;
	}

	/**
	 * Returns the list of member definitions.
	 * 
	 * @return the list of member definitions
	 */

	public List getMembers( )
	{
		if ( members != null )
			return new ArrayList( members );

		return new ArrayList( );
	}

	/**
	 * Returns the member definition given method name.
	 * 
	 * @param name
	 *            name of the member to get
	 * @return the member definition to get
	 */

	public MemberInfo getMember( String name )
	{
		return findMember( name );
	}

	/**
	 * Returns the member definition given method name.
	 * 
	 * @param name
	 *            name of the member to find
	 * @return the member definition to find
	 */

	private MemberInfo findMember( String name )
	{
		if ( members == null )
		{
			return null;
		}

		for ( Iterator iter = members.iterator( ); iter.hasNext( ); )
		{
			MemberInfo memberDefn = (MemberInfo) iter.next( );

			if ( memberDefn.getName( ).equals( name ) )
				return memberDefn;
		}

		return null;
	}

	/**
	 * Returns the constructor definition.
	 * 
	 * @return the constructor definition of this class
	 */

	public List getConstructors( )
	{
		if ( constructors != null )
			return new ArrayList( constructors );

		return new ArrayList( );
	}

	/**
	 * Adds constructor since some class has more than one constructor with
	 * different arguments.
	 * 
	 * @param constructor
	 *            the constructor definition to add
	 * @throws MetaDataException
	 *             if the constructor's name is empty.
	 */

	void addConstructor( MethodInfo constructor ) throws MetaDataException
	{
		if ( constructors == null )
		{
			constructors = new ArrayList( );
		}

		if ( StringUtil.isBlank( constructor.getName( ) ) )
			throw new MetaDataException( new String[]{constructor.getName( )},
					MetaDataException.MISSING_METHOD_NAME );

		constructors.add( constructor );
	}

	/**
	 * Returns whether a class object is native.
	 * 
	 * @return <code>true</code> if an object of this class is native,
	 *         otherwise <code>false</code>
	 */

	public boolean isNative( )
	{
		return isNative;
	}

	/**
	 * Sets the native attribute of this class.
	 * 
	 * @param isNative
	 *            <code>Boolean.TRUE</code> if an object of this class is
	 *            native, otherwise <code>Boolean.FALSE</code>
	 */

	protected void setNative( boolean isNative )
	{
		this.isNative = isNative;
	}
}