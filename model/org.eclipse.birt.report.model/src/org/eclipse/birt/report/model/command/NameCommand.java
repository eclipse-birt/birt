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
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.SlotDefn;

/**
 * Renames a design element.
 * 
 */

public class NameCommand extends AbstractElementCommand
{

	/**
	 * The definition of the slot the element to add or exsit.
	 */

	private SlotDefn slotInfo = null;

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param obj
	 *            the element to modify.
	 * @param slotInfo
	 *            the definition of the slot the element to add or exsit
	 */

	public NameCommand( Module module, DesignElement obj, SlotDefn slotInfo )
	{
		super( module, obj );
		this.slotInfo = slotInfo;
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
		doCheckName( name, false );
	}

	/**
	 * Does some checks about the name of the element to insert, to replace or
	 * to set a new name.
	 * 
	 * @param name
	 *            the new name to check
	 * @param isReplace
	 *            true if the name check occurs in the replacement, otherwise
	 *            false
	 * @throws NameException
	 */

	private void doCheckName( String name, boolean isReplace )
			throws NameException
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
		}
		else
		{
			// Cannot set the name of an element when the name is not allowed.

			if ( metaData.getNameOption( ) == MetaDataConstants.NO_NAME )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_NAME_FORBIDDEN );

			// if the contents in the slot to add should not be added into the
			// namespace, then checks for the names don't work; such as styles
			// in the themes, or the report items or data sets in the template
			// parameter definitions, their names will not put into namepsace.

			if ( slotInfo != null && !slotInfo.isAddToNameSpace( ) )
			{
				return;
			}
			int ns = metaData.getNameSpaceID( );

			// first found the element with the given name. Since the library
			// has it own namespace -- prefix, the range of name check should be
			// in the current module.

			DesignElement existedElement = getModule( ).getNameSpace( ns )
					.getElement( name );

			// if the element is null, then the name is OK. Otherwise, if the
			// found element is the same as the current element, do not consider
			// as a duplicate. Consider a case: 1. new a compound element from
			// ElementFactory; 2. add elements ( names of these elements have
			// been added into namespaces through element handles) into the new
			// compound element (still not in the design tree); 3. add the
			// compound element to the design tree; 4. for this case, should
			// have no exception thrown.

			if ( existedElement != null
					&& existedElement != element
					&& ( !isReplace || ( isReplace && !existedElement
							.isContentOf( element ) ) ) )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_DUPLICATE );
		}
	}

	/**
	 * Checks the current element name. Done when adding a newly created element
	 * where the element name is already set on the new element.
	 * 
	 * @param newElement
	 *            the new element to check.
	 * @throws NameException
	 *             if the element name is not allowed to change.
	 */

	public void checkName( DesignElement newElement ) throws NameException
	{
		if ( newElement == null )
			return;
		String name = newElement.getName( );
		doCheckName( name, true );

		IElementDefn metaData = newElement.getDefn( );
		for ( int i = 0; i < metaData.getSlotCount( ); i++ )
		{
			ContainerSlot slot = newElement.getSlot( i );

			for ( int j = 0; j < slot.getCount( ); j++ )
			{
				DesignElement content = slot.getContent( j );
				checkName( content );
			}
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
		if ( element.getName( ) == null
				|| ( slotInfo != null && !slotInfo.isAddToNameSpace( ) ) )
			return;

		// if it is a style in the theme, no need to check duplicate names.
		// In the library, style names can be duplicate.

		if ( element instanceof StyleElement
				&& element.getContainer( ) instanceof Theme )
			return;

		if ( element.getContainer( ) != null )
		{
			int ns = ( (ElementDefn) element.getDefn( ) ).getNameSpaceID( );

			// if the element has been in the name space, that is, the element
			// is added to another element through handels but the outermost
			// compound element is not in the design tree, then do not insert
			// the element
			// to the name space agian.

			DesignElement existedElement = getModule( ).getNameSpace( ns )
					.getElement( element.getName( ) );

			if ( existedElement != element )
				getActivityStack( ).execute(
						new NameSpaceRecord( getModule( ), ns, element, true ) );
		}
	}

	/**
	 * Implementation of dropping a symbol from a name space. No need to do the
	 * drop if the current name is null.
	 */

	private void dropSymbol( )
	{
		if ( element.getName( ) == null
				|| ( slotInfo != null && !slotInfo.isAddToNameSpace( ) ) )
			return;
		int ns = ( (ElementDefn) element.getDefn( ) ).getNameSpaceID( );
		if ( !module.getNameSpace( ns ).contains( element.getName( ) ) )
			return;
		getActivityStack( ).execute(
				new NameSpaceRecord( getModule( ), ns, element, false ) );
	}

	private void renameSymbolFrom( String oldName )
	{
		// only the slot the element to add or exsit need to handle some
		// namespace issue, we will do some replace operarions for namespace; if
		// not, we will not handle

		if ( element.getContainer( ) != null
				&& ( slotInfo != null && slotInfo.isAddToNameSpace( ) ) )
		{
			RenameInNameSpaceRecord record = new RenameInNameSpaceRecord(
					getModule( ), element, oldName, element.getName( ) );
			getActivityStack( ).execute( record );
		}
	}
}
