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

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.LibraryEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;

/**
 * Records to add/drop library.
 */

class LibraryRecord extends SimpleRecord
{

	/**
	 * The target module
	 */

	protected Module module;

	/**
	 * The library to operate
	 */

	protected Library library;

	/**
	 * The position of the library
	 */

	protected int position = -1;

	/**
	 * Whether to add or remove the library.
	 */

	protected boolean add = true;

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
		this.module = module;
		this.library = library;
		this.add = add;
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

			// One library is added, and the style in it can override the
			// previouse one.

			List librariesToUpdate = module.getLibraries( ).subList( 0,
					toUpdateLibraryCount );
			module.updateStyleClients( librariesToUpdate );
		}
		else
		{
			position = module.dropLibrary( library );

			// The update is performed only on the referred elements in the
			// dropped library.

			module.updateStyleClients( library );
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

}
