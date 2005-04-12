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
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Records a change to the style of an element.
 *  
 */

public class StyleRecord extends SimpleRecord
{

	/**
	 * The element to change.
	 */

	protected StyledElement element = null;

	/**
	 * The old style. Can be null.
	 */

	protected StyleElement oldStyle = null;

	/**
	 * The new style. Can be null.
	 */

	protected StyleElement newStyle = null;

	/**
	 * Constructor.
	 * 
	 * @param obj
	 *            the element to modify.
	 * @param style
	 *            the style to set.
	 */

	public StyleRecord( StyledElement obj, StyleElement style )
	{
		assert obj != null;
		element = obj;
		newStyle = style;
		oldStyle = element.getStyle( );

		label = ModelMessages.getMessage( MessageConstants.SET_STYLE_MESSAGE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		element.setStyle( undo ? oldStyle : newStyle );
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
		return new StyleEvent( element );
	}

}