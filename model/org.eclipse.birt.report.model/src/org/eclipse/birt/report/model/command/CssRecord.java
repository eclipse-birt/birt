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
import org.eclipse.birt.report.model.api.command.CssEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssNameManager;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;

/**
 * 
 * Records to add/drop css.
 * 
 */

public class CssRecord extends SimpleRecord
{

	/**
	 * The target module
	 */

	protected Module module;

	/**
	 * Design element
	 */

	protected DesignElement element;

	/**
	 * The css to operate
	 */

	protected CssStyleSheet css;

	/**
	 * Whether to add or remove the css.
	 */

	protected boolean add = true;

	/**
	 * Constructs the library record.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            design element
	 * @param css
	 *            the css style sheet to add/drop
	 * @param add
	 *            whether the given css is for adding
	 */

	CssRecord( Module module, DesignElement element, CssStyleSheet css,
			boolean add )
	{
		this.module = module;
		this.element = element;
		this.css = css;
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
			if ( element instanceof ICssStyleSheetOperation )
			{
				( (ICssStyleSheetOperation) element ).addCss( css );
			}

			// re-resolve
			CssNameManager.adjustStylesForAdd( module , (ICssStyleSheetOperation)element , css );
		}
		else
		{
			if ( element instanceof ICssStyleSheetOperation )
			{
				( (ICssStyleSheetOperation) element ).dropCss( css );
			}

			// unresolve
			CssNameManager.adjustStylesForRemove( css );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		if ( add && state != UNDONE_STATE || !add && state == UNDONE_STATE )
			return new CssEvent( css, CssEvent.ADD );

		return new CssEvent( css, CssEvent.DROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return element;
	}
}
