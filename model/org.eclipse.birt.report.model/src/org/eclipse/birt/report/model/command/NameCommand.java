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

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.ThemeStyleNameValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ReferenceValue;

/**
 * Renames a design element.
 * 
 */

public class NameCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param obj
	 *            the element to modify.
	 */

	public NameCommand( Module module, DesignElement obj )
	{
		super( module, obj );
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

		// Change the name.

		stack.execute( rename );

		// Add the new name to the name space.

		renameSymbolFrom( oldName );

		stack.commit( );
	}

	/**
	 * Checks the current element name. Done when adding a newly created element
	 * where the element name is already set on the new element.
	 * 
	 * @param name
	 *            the name to check.
	 * @throws NameException
	 *             if the element name is not allowed to change.
	 */

	public void checkName( String name ) throws NameException
	{
		ElementDefn metaData = (ElementDefn) element.getDefn( );

		if ( name == null )
		{
			// Cannot clear the name when there are references. It would leave
			// the dependents with no way to identify this element.

			if ( element.hasDerived( ) || element.hasReferences( ) )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_HAS_REFERENCES );

			// Cannot clear the name of an element when the name is required.

			if ( metaData.getNameOption( ) == MetaDataConstants.REQUIRED_NAME )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_NAME_REQUIRED );

			if ( ( module instanceof Library )
					&& ( element.getContainer( ) instanceof Library ) )
			{
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_NAME_REQUIRED );
			}
		}
		else
		{
			if ( !isNameValidInContext( name ) )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_DUPLICATE );

			// Cannot set the name of an element when the name is not allowed.

			if ( metaData.getProperty( IDesignElementModel.NAME_PROP ) == null
					&& metaData.getNameOption( ) == MetaDataConstants.NO_NAME )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_NAME_FORBIDDEN );

			if ( name.indexOf( ReferenceValue.NAMESPACE_DELIMITER ) != -1 )
			{
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_DOT_FORBIDDEN );
			}

			// if the element is a pending node and not in any module, or it is
			// in a slot that is not managed by namespace, then we need not
			// check whether the name is duplicate

			if ( !element.isManagedByNameSpace( ) )
			{
				return;
			}

			int ns = metaData.getNameSpaceID( );

			// first found the element with the given name. Since the library
			// has it own namespace -- prefix, the range of name check should be
			// in the current module.

			DesignElement existedElement = getModule( ).getNameSpace( ns )
					.getElement( name );

			// if the element is null, then the name is OK. Now, the name of the
			// element is inserted into the namespace only if the element is in
			// a design tree(module) and the slot it exsits is managed by
			// namespace

			if ( existedElement != null )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_DUPLICATE );

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

		// if it is a style in the theme, no need to check duplicate names.
		// In the library, style names can be duplicate.

		if ( !element.isManagedByNameSpace( ) )
			return;

		assert element.getRoot( ) != null;

		int ns = ( (ElementDefn) element.getDefn( ) ).getNameSpaceID( );

		// if the element has been in the name space, that is, the element
		// is added to another element through handels but the outermost
		// compound element is not in the design tree, then do not insert
		// the element
		// to the name space agian.

		DesignElement existedElement = getModule( ).getNameSpace( ns )
				.getElement( element.getName( ) );

		assert existedElement == null;
		getActivityStack( ).execute(
				new NameSpaceRecord( getModule( ), ns, element, true ) );
	}

	/**
	 * Implementation of dropping a symbol from a name space. No need to do the
	 * drop if the current name is null.
	 */

	private void dropSymbol( )
	{
		if ( element.getName( ) == null )
			return;
		int ns = ( (ElementDefn) element.getDefn( ) ).getNameSpaceID( );
		if ( module.getNameSpace( ns ).getElement( element.getName( ) ) != element )
			return;
		getActivityStack( ).execute(
				new NameSpaceRecord( getModule( ), ns, element, false ) );
	}

	/**
	 * Renames the namespace when call {@link #setName(String)}.
	 * 
	 * @param oldName
	 *            the old name of the element
	 */

	private void renameSymbolFrom( String oldName )
	{
		// only the slot the element to add or exsit need to handle some
		// namespace issue, we will do some replace operarions for namespace; if
		// not, we will not handle

		if ( element.isManagedByNameSpace( ) )
		{
			RenameInNameSpaceRecord record = new RenameInNameSpaceRecord(
					getModule( ), element, oldName, element.getName( ) );
			getActivityStack( ).execute( record );
		}
	}

	/**
	 * Checks whether the name is valid in the context.
	 * 
	 * @param name
	 *            the new name
	 * @return <code>true</code> if the name is valid. Otherwise
	 *         <code>false</code>.
	 */

	private boolean isNameValidInContext( String name )
	{
		if ( element instanceof Style )
		{
			DesignElement tmpContainer = element.getContainer( );
			if ( tmpContainer instanceof Theme )
			{
				List errors = ThemeStyleNameValidator.getInstance( )
						.validateForRenamingStyle(
								(ThemeHandle) tmpContainer.getHandle( module ),
								(StyleHandle) element.getHandle( module ),
								(String) name );
				if ( !errors.isEmpty( ) )
					return false;
			}
		}

		return true;
	}
}
