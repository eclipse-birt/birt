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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.util.ColorUtil;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

/**
 * A field editor for a color type preference.
 */

public class ColorFieldEditor extends
		org.eclipse.jface.preference.ColorFieldEditor
{

	/**
	 * Creates a new color field editor
	 */
	public ColorFieldEditor( )
	{
		super( );
	}

	/**
	 * Creates a color field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public ColorFieldEditor( String name, String labelText, Composite parent )
	{
		super( name, labelText, parent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.ColorFieldEditor#doLoad()
	 */
	protected void doLoad( )
	{
		RGB rgb = DEUtil.getRGBValue( ColorUtil.parseColor( getPreferenceStore( ).getString( getPreferenceName( ) ) ) );

		if ( rgb != null )
		{
			getColorSelector( ).setColorValue( rgb );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.ColorFieldEditor#doStore()
	 */
	protected void doStore( )
	{
		RGB rgb = getColorSelector( ).getColorValue( );

		getPreferenceStore( ).setValue( getPreferenceName( ),
				( rgb == null ) ? null
						: ( ColorUtil.format( DEUtil.getRGBInt( getColorSelector( ).getColorValue( ) ),
								ColorUtil.HTML_FORMAT ) ) );
	}
}