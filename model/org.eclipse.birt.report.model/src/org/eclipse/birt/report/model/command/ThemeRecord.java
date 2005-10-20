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
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Theme;

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

	private Theme oldTheme;

	/**
	 * The library to operate
	 */

	protected ReportDesign module;

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

	ThemeRecord( ReportDesign module, Theme newTheme, Theme oldTheme )
	{
		this.module = module;
		this.newTheme = newTheme;
		this.oldTheme = oldTheme;
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
			unresolveStyles( newTheme );
		else
			unresolveStyles( oldTheme );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return newTheme;
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

	private void unresolveStyles( Theme theme )
	{
		Iterator iter = theme.getSlot( Theme.STYLES_SLOT ).iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElement element = (DesignElement) iter.next( );
			assert element instanceof StyleElement;

			ReferenceableElement referenceableElement = (ReferenceableElement) element;

			module.updateClientReferences( referenceableElement );
		}
	}
}
