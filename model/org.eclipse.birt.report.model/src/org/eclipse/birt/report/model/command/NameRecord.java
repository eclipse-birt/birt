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
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Records a change to the name of an element.
 *  
 */

public class NameRecord extends SimpleRecord
{

	/**
	 * The element to change.
	 */

	protected DesignElement element = null;

	/**
	 * The new name. Can be null.
	 */

	protected String newName = null;

	/**
	 * The old name. Can be null.
	 */

	protected String oldName = null;

	/**
	 * Constructor.
	 * 
	 * @param obj
	 *            the element to change.
	 * @param name
	 *            the new name.
	 */

	public NameRecord( DesignElement obj, String name )
	{
		element = obj;
		newName = name;
		oldName = element.getName( );

		label = ModelMessages.getMessage( MessageConstants.SET_NAME_MESSAGE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		element.setName( undo ? oldName : newName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		return new NameEvent( element, oldName, newName );
	}

}