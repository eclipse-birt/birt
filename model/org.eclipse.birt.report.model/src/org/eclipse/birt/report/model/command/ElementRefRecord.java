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

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;

public class ElementRefRecord extends SimpleRecord
{

	/**
	 * Name of property.
	 */
	private String propName = null;

	private DesignElement reference = null;

	private ReferenceableElement referred = null;
	
	private boolean isAdd = true;

	public ElementRefRecord( DesignElement reference, ReferenceableElement referred,
			String propName, boolean isAdd )
	{
		this.reference = reference;
		this.referred = referred;
		this.isAdd = isAdd;
		this.propName = propName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */
	protected void perform( boolean undo )
	{
		if ( isAdd && !undo || !isAdd && undo  )
		{
			referred.addClient( reference, propName );
		}
		else
		{			
			referred.dropClient( reference );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */
	
	public NotificationEvent getEvent( )
	{
		if ( IStyledElementModel.STYLE_PROP.equals( propName ) )
			return new StyleEvent( reference );
		return new PropertyEvent( reference, propName );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */
	
	public DesignElement getTarget( )
	{
		return referred;
	}


}
