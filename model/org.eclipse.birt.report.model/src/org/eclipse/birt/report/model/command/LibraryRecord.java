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
import java.util.Map;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.LibraryEvent;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.ElementStructureUtil;

/**
 * Records to add/drop library.
 */

class LibraryRecord extends AbstractLibraryRecord
{

	/**
	 * The position of the library
	 */

	protected int position = -1;

	/**
	 * Whether to add or remove the library.
	 */

	protected boolean add = true;

	/**
	 * The cached overridden values when removing one library.
	 */
	protected Map overriddenValues = null;

	/**
	 * Constructs the library record.
	 * 
	 * @param module
	 *            the module
	 * @param library
	 *            the library to add/drop
	 * @param add
	 *            whether the given library is for adding
	 */

	LibraryRecord( Module module, Library library, boolean add )
	{
		super( module, library );

		this.add = add;
	}

	/**
	 * Constructs the library record. Only for adding library.
	 * 
	 * @param module
	 *            the module
	 * @param library
	 *            the library to add/drop
	 * @param values
	 *            the cached overridden values when removing a library
	 */

	LibraryRecord( Module module, Library library, Map values )
	{
		this( module, library, true );

		overriddenValues = values;
		assert overriddenValues != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		if ( add && !undo || !add && undo )
		{
			int toUpdateLibraryCount;
			if ( position == -1 )
			{
				module.addLibrary( library );
				toUpdateLibraryCount = module.getLibraries( ).size( ) - 1;
			}
			else
			{
				module.insertLibrary( library, position );
				toUpdateLibraryCount = position;
			}

			// first resolve the extends and apply overridden values to virtual
			// elements. Only for the add & do case. For remove & undo, it is
			// supported by ContentCommand. See LibraryCommand.reloadLibrary for
			// details.

			if ( add && !undo )
				resolveAllElementDescendants( );

			// One library is added, and the style in it can override the
			// previouse one.

			List librariesToUpdate = module.getLibraries( ).subList( 0,
					toUpdateLibraryCount );
			updateReferenceableClients( librariesToUpdate );
		}
		else
		{
			position = module.dropLibrary( library );

			// The update is performed only on the referred elements in the
			// dropped library.

			updateReferenceableClients( library );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return module;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		if ( add && state != UNDONE_STATE || !add && state == UNDONE_STATE )
			return new LibraryEvent( library, LibraryEvent.ADD );

		return new LibraryEvent( library, LibraryEvent.DROP );
	}

	/**
	 * Resolves extends references for elements in the <code>module</code>.
	 * During the resolving procedure, cached overridden values are also
	 * distributed.
	 */

	protected void resolveAllElementDescendants( )
	{
		for ( int i = 0; i < module.getDefn( ).getSlotCount( ); i++ )
		{
			int slotId = i;
			if ( slotId == ReportDesign.STYLE_SLOT
					|| slotId == Library.THEMES_SLOT
					|| slotId == ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT )
				continue;

			resolveElementDescendantsInSlot( slotId );
		}
	}

	/**
	 * Resolves extends references for elements in the given slot. During the
	 * resolving procedure, cached overridden values are also distributed.
	 * 
	 * @param slotId
	 *            the slot id
	 */

	private void resolveElementDescendantsInSlot( int slotId )
	{
		ContentIterator contentIter = new ContentIterator( module, slotId );

		while ( contentIter.hasNext( ) )
		{
			DesignElement tmpElement = (DesignElement) contentIter.next( );
			ElementDefn elementDefn = (ElementDefn) tmpElement.getDefn( );
			if ( !elementDefn.canExtend( ) )
				continue;

			String name = tmpElement.getExtendsName( );
			if ( StringUtil.isBlank( name ) )
				continue;

			tmpElement.resolveExtends( module );
			if ( tmpElement.getDefn( ).getSlotCount( ) <= 0 )
				continue;

			ElementStructureUtil
					.refreshStructureFromParent( module, tmpElement );

			if ( overriddenValues == null )
				return;

			Long idObj = new Long( tmpElement.getID( ) );
			Map values = (Map) overriddenValues.get( idObj );
			ElementStructureUtil.distributeValues( tmpElement, values );

			ElementStructureUtil.addTheVirualElementsToNamesapce( tmpElement,
					module );
		}
	}

}
