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
import org.eclipse.birt.report.model.core.ReferencableElement;

/**
 * Records a change to the back reference of an element.
 * 
 * @see org.eclipse.birt.report.model.core.ReferencableElement
 */

public class BackRefRecord extends SimpleRecord
{

	/**
	 * The element is referred by <code>reference</code>.
	 */

	protected ReferencableElement referred = null;

	/**
	 * The element that refers to another element.
	 */

	protected DesignElement reference = null;

	/**
	 * The property name.
	 */

	protected String propName = null;

	/**
	 * Constructor.
	 * 
	 * @param obj
	 *            the element to change.
	 * @param reference
	 *            the element that refers to another element.
	 * @param propName
	 *            the property name. The type of the property must be
	 *            <code>PropertyType.ELEMENT_REF_TYPE</code>. Meanwhile, it
	 *            must not be <code>DesignElement.EXTENDS_PROP</code> and
	 *            <code>DesignElement.STYLE_PROP</code>
	 */

	public BackRefRecord( ReferencableElement obj, DesignElement reference,
			String propName )
	{
		this.referred = obj;
		this.reference = reference;
		this.propName = propName;

		assert referred != null;		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		if ( undo )
			referred.addClient( reference, propName );
		else
			referred.dropClient( reference );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return referred;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		return null;
	}

	/**
	 * For the internal record to handle with back references, there is no need
	 * to send out the notification.
	 */

	protected void sendNotifcations( boolean transactionStarted )
	{
	}
}