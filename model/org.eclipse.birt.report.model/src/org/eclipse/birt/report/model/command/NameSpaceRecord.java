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

import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.RootElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Records an insertion into, or deletion from a name space.
 * 
 */

public class NameSpaceRecord extends SimpleRecord
{

	/**
	 * The root element that has the name space.
	 */

	protected RootElement root = null;

	/**
	 * The element to add or remove.
	 */

	protected DesignElement element = null;

	/**
	 * The name space to use.
	 */

	protected int nameSpaceID = 0;

	/**
	 * Whether to add or remove the element.
	 */

	protected boolean add = false;

	/**
	 * Constructor.
	 * 
	 * @param theRoot
	 *            the root element.
	 * @param ns
	 *            the name space to use.
	 * @param symbol
	 *            the element to insert or remove.
	 * @param isAdd
	 *            whether to add (true) or remove (false) the element.
	 */

	public NameSpaceRecord( RootElement theRoot, int ns, DesignElement symbol,
			boolean isAdd )
	{
		root = theRoot;
		element = symbol;
		nameSpaceID = ns;
		add = isAdd;

		assert root != null;
		assert element != null;

		// This record should never appear by itself in the activity stack.
		// Instead, this record should appear as part of a larger task,
		// and the label for that task should appear in the UI.

		if ( add )
			label = ThreadResources
					.getMessage( MessageConstants.INSERT_ELEMENT_MESSAGE );
		else
			label = ThreadResources
					.getMessage( MessageConstants.DELETE_ELEMENT_MESSAGE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		NameSpace ns = root.getNameSpace( nameSpaceID );
		if ( add && !undo || !add && undo )
			ns.insert( element );
		else
			ns.remove( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		if ( add && state != UNDONE_STATE || !add && state == UNDONE_STATE )
			return new NameSpaceEvent( root, nameSpaceID, NameSpaceEvent.ADD );

		return new NameSpaceEvent( root, nameSpaceID, NameSpaceEvent.REMOVE );
	}

}
