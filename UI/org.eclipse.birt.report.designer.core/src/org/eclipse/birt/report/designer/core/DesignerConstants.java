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

package org.eclipse.birt.report.designer.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.elements.DesignChoiceConstants;

/**
 * This class collects commonly-used choice constants. These constants define
 * the internal value of choices for several property choice constants.
 */

public class DesignerConstants
{

	private static final String FONT_FAMILY_COURIER_NEW = "Courier New"; //$NON-NLS-1$

	private static final String FONT_FAMILY_IMPACT = "Impact"; //$NON-NLS-1$

	private static final String FONT_FAMILY_COMIC_SANS_MS = "Comic Sans MS"; //$NON-NLS-1$

	private static final String FONT_FAMILY_ARIAL = "Arial"; //$NON-NLS-1$

	private static final String FONT_FAMILY_TIMES_NEW_ROMAN = "Times New Roman"; //$NON-NLS-1$

	/**
	 * Map between CSS style font family to system font family
	 */
	public static Map familyMap = new HashMap( );

	/**
	 * Map between CSS font to system font
	 */
	public static Map fontMap = new HashMap( );

	/**
	 * Static table stores the font families. It provides the font name and the
	 * family of the fonts.
	 */
	static
	{
		familyMap.put( DesignChoiceConstants.FONT_FAMILY_SERIF,
				FONT_FAMILY_TIMES_NEW_ROMAN );
		familyMap.put( DesignChoiceConstants.FONT_FAMILY_SANS_SERIF,
				FONT_FAMILY_ARIAL );
		familyMap.put( DesignChoiceConstants.FONT_FAMILY_CURSIVE,
				FONT_FAMILY_COMIC_SANS_MS );
		familyMap.put( DesignChoiceConstants.FONT_FAMILY_FANTASY,
				FONT_FAMILY_IMPACT );
		familyMap.put( DesignChoiceConstants.FONT_FAMILY_MONOSPACE,
				FONT_FAMILY_COURIER_NEW );
	};

	/**
	 * Static table stores the font sizes. It provides the font name and the
	 * size of the fonts.
	 */
	public static final String[][] fontSizes = {
			{
					DesignChoiceConstants.FONT_SIZE_XX_SMALL, "7"}, //$NON-NLS-1$  
			{
					DesignChoiceConstants.FONT_SIZE_X_SMALL, "8"}, //$NON-NLS-1$ 
			{
					DesignChoiceConstants.FONT_SIZE_SMALL, "9"}, //$NON-NLS-1$ 
			{
					DesignChoiceConstants.FONT_SIZE_MEDIUM, "10"}, //$NON-NLS-1$  
			{
					DesignChoiceConstants.FONT_SIZE_LARGE, "11"}, //$NON-NLS-1$  
			{
					DesignChoiceConstants.FONT_SIZE_X_LARGE, "12"}, //$NON-NLS-1$ 
			{
					DesignChoiceConstants.FONT_SIZE_XX_LARGE, "13"}, //$NON-NLS-1$ 
	};

	static
	{
		// initialize the font map, pair fonts with their size values.
		for ( int i = 0; i < fontSizes.length; i++ )
		{
			fontMap.put( fontSizes[i][0], fontSizes[i][1] );
		}
	}
}