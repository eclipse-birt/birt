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
import java.util.List;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ThemeEvent;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Records a change to the theme of a report design.
 */

public class ThemeRecord extends SimpleRecord
{

	/**
	 * The target Theme
	 */

	private ElementRefValue newTheme;

	/**
	 * The target Theme
	 */

	private ElementRefValue oldTheme;

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
	 */

	ThemeRecord( Module module, ElementRefValue newTheme )
	{
		this.module = module;
		this.newTheme = newTheme;

		oldTheme = (ElementRefValue) module.getLocalProperty( module,
				IModuleModel.THEME_PROP );

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
			module.setProperty( IModuleModel.THEME_PROP, oldTheme );
			updateStyles( newTheme );
		}
		else
		{
			module.setProperty( IModuleModel.THEME_PROP, newTheme );
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

	private void updateStyles( ElementRefValue theme )
	{
		// if the old theme is empty of not resolved. Do not need to unresolv.

		if ( theme == null )
			return;

		if ( !theme.isResolved( ) )
			return;
		
		Theme t = (Theme) theme.getElement( );
		List styles = t.getAllStyles( );
		Iterator iter = styles.iterator( );
		while( iter.hasNext() )
		{
			Style style = (Style) iter.next();
			style.updateClientReferences( );
		}
	}
}
