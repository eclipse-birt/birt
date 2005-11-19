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

import java.util.Iterator;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ThemeEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Records a change to the theme of a report design.
 */

public class ThemeRecord extends SimpleRecord
{

	/**
	 * The target Theme
	 */

	private Theme newTheme;

	/**
	 * The target Theme
	 */

	private Object oldTheme;

	/**
	 * The library to operate
	 */

	protected Module module;

	/**
	 * Constructs the library record.
	 * 
	 * @param module
	 *            the module
	 * @param newTheme
	 *            the new theme
	 * @param oldTheme
	 *            the old theme
	 */

	ThemeRecord( Module module, Theme newTheme )
	{
		this.module = module;
		this.newTheme = newTheme;
		
		if ( module.getTheme( ) != null )
			oldTheme = module.getTheme();
		else 
			oldTheme = module.getThemeName( );

		label = ModelMessages.getMessage( MessageConstants.SET_THEME_MESSAGE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		// if undo, must unresolve the current theme; if do/redo, must unresolve
		// the previous theme

		if ( undo )
		{
			if ( oldTheme instanceof String )
				module.setThemeName( (String) oldTheme );
			else
				module.setTheme( (Theme) oldTheme );
			
			updateStyles( newTheme );
		}
		else
		{
			module.setTheme( newTheme );
			updateStyles( oldTheme );
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
		return new ThemeEvent( module );
	}

	/**
	 * Unresolves references of styles of a theme.
	 * 
	 * @param theme
	 *            the theme
	 */

	private void updateStyles( Object theme )
	{
		// if the old theme is empty of not resolved. Do not need to unresolv.

		if ( theme == null || theme instanceof String )
			return;

		Iterator iter = ( (Theme) theme ).getSlot( Theme.STYLES_SLOT )
				.iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElement element = (DesignElement) iter.next( );
			assert element instanceof StyleElement;

			ReferenceableElement referenceableElement = (ReferenceableElement) element;

			module.updateClientReferences( referenceableElement );
		}
	}
}
