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

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Renames a design element.
 * 
 */

public class NameCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param design
	 *            the report design
	 * @param obj
	 *            the element to modify.
	 */

	public NameCommand( ReportDesign design, DesignElement obj )
	{
		super( design, obj );
	}

	/**
	 * Sets the element name.
	 * 
	 * @param name
	 *            the new name.
	 * @throws NameException
	 *             if the element name is not allowed to change.
	 */

	public void setName( String name ) throws NameException
	{
		name = StringUtil.trimString( name );

		// Ignore change to the current name.

		String oldName = element.getName( );
		if ( ( name == null && oldName == null )
				|| ( name != null && oldName != null && name.equals( oldName ) ) )
			return;

		checkName( name );

		// Record the change.

		ActivityStack stack = getActivityStack( );
		NameRecord rename = new NameRecord( element, name );
		stack.startTrans( rename.getLabel( ) );

		// Drop the old name from the name space.

		dropSymbol( );

		// Change the name.

		stack.execute( rename );

		// Add the new name to the name space.

		addSymbol( );
		stack.commit( );
	}

	/**
	 * Checks the current element name. Done when adding a newly created element
	 * where the element name is already set on the new element.
	 * 
	 * @throws NameException
	 *             if the element name is not allowed to change.
	 */

	public void checkName( ) throws NameException
	{
		checkName( element.getName( ) );
	}

	/**
	 * Checks that the given name is legal for the element.
	 * 
	 * @param name
	 *            the name to check.
	 * @throws NameException
	 *             if the element name is not allowed to change.
	 */

	private void checkName( String name ) throws NameException
	{
		ElementDefn metaData = element.getDefn( );
		if ( name == null )
		{
			// Cannot clear the name when there are references. It would leave
			// the dependents with no way to identify this element.

			if ( element.hasDerived( ) || element.hasReferences( ))
				throw new NameException( element, name,
						NameException.HAS_REFERENCES );
			
			// Cannot clear the name of an element when the name is required.

			if ( metaData.getNameOption( ) == MetaDataConstants.REQUIRED_NAME )
				throw new NameException( element, name,
						NameException.NAME_REQUIRED );
		}
		else
		{
			// Cannot set the name of an element when the name is not allowed.

			if ( metaData.getNameOption( ) == MetaDataConstants.NO_NAME )
				throw new NameException( element, name,
						NameException.NAME_FORBIDDEN );

			// Ensure that the name is not a duplicate.

			int ns = metaData.getNameSpaceID( );
			if ( getRootElement( ).getNameSpace( ns ).getElement( name ) != null )
				throw new NameException( element, name, NameException.DUPLICATE );
		}
	}

	/**
	 * Adds the element into its name space.
	 * 
	 * @throws NameException
	 *             if the element with the same name exists.
	 */

	protected void addElement( ) throws NameException
	{
		checkName( element.getName( ) );
		addSymbol( );
	}

	/**
	 * Drops the element from its name space.
	 */

	protected void dropElement( )
	{
		dropSymbol( );
	}

	/**
	 * Implementation of adding a symbol to a name space. Adds the name only if
	 * it is not null and it has container. This means that we can modify the
	 * element name as will if it's not attached on other elements, and the name
	 * is saved in name space only after the element is added to a slot of the
	 * container.
	 */

	private void addSymbol( )
	{
		if ( element.getName( ) == null )
			return;

		if ( element.getContainer( ) != null )
		{
			int ns = element.getDefn( ).getNameSpaceID( );
			getActivityStack( )
					.execute(
							new NameSpaceRecord( getRootElement( ), ns,
									element, true ) );
		}
	}

	/**
	 * Implementation of dropping a symbol from a name space. No need to do the
	 * drop if the current name is null.
	 */

	private void dropSymbol( )
	{
		if ( element.getName( ) == null )
			return;
		int ns = element.getDefn( ).getNameSpaceID( );
		if ( !design.getNameSpace( ns ).contains( element.getName( ) ) )
			return;
		getActivityStack( ).execute(
				new NameSpaceRecord( getRootElement( ), ns, element, false ) );
	}
}
