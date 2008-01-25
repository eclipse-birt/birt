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

package org.eclipse.birt.report.designer.util;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * A pattern class serves for getting and setting pattern string for a number.
 */

public class FormatNumberPattern
{

	public static final String TEXT_CURRENCY_SYMBOL_NONE = Messages.getString( "FormatNumberPage.currency.symbol.none" ); //$NON-NLS-1$
	public static final String SYMBOL_POSITION_AFTER = Messages.getString( "FormatNumberPage.symblePos.after" ); //$NON-NLS-1$
	public static final String SYMBOL_POSITION_BEFORE = Messages.getString( "FormatNumberPage.symblePos.before" ); //$NON-NLS-1$

	// private HashMap categoryPatternMaps;

	/**
	 * Category of number format pattern
	 */
	private String category = ""; //$NON-NLS-1$

	/**
	 * type
	 */
	private char type;

	protected String zeroIndicator = "\'0\'"; //$NON-NLS-1$
	protected String defaultDecs = "0000000000"; //$NON-NLS-1$

	/**
	 * For currency, there is no default pattern, because DTE not support
	 * "currency" as a predefined pattern string, however, giving a default
	 * currency pattern here to provide convinence for setting pattern. Do not
	 * save the category name for this according defalut pattern because there
	 * is actually no default pattern for category "currency". Expect DTE to
	 * support this default pattern in future.
	 */
	protected String DEFAULT_CURRENCY_PATTERN = "#,##0.00"; //$NON-NLS-1$

	/** default pattern for fiexed number */
	protected String DEFAULT_FIXED_PATTERN = "###0.00"; //$NON-NLS-1$

	/** default pattern for percent number */
	protected String DEFAULT_PERCENT_PATTERN = "###0.00%"; //$NON-NLS-1$

	/** default pattern for scientific number */
	protected String DEFAULT_SCIENTIFIC_PATTERN = "0.00E00"; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param category
	 *            Name for the pattern.
	 */
	public FormatNumberPattern( String category )
	{
		this.category = category;
		setType( 'G' );
		// createCategoryPatterns( );
	}

	/**
	 * Constructor.
	 */
	public FormatNumberPattern( )
	{
		this.category = DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER;
		setType( 'G' );
		// createCategoryPatterns( );
	}

	/**
	 * Get category name
	 * 
	 * @return Returns the name.
	 */
	public String getCategory( )
	{
		return category;
	}

	/**
	 * Get type
	 * 
	 * @return Returns the type.
	 */
	protected char getType( )
	{
		return type;
	}

	/**
	 * Set type
	 * 
	 * @param t
	 *            The type to set
	 */
	protected void setType( char t )
	{
		this.type = t;
	}

	/**
	 * Gets pattern string for the pattern class.
	 */
	public String getPattern( )
	{
		return this.category;
	}

	/**
	 * Sets the pattern string.
	 * 
	 * @param patternStr
	 */
	public void setPattern( String patternStr )
	{
		this.category = valPattern( patternStr );
	}

	/**
	 * Validates the pattern string for predefined format category excludeing
	 * "custom".
	 * 
	 * @param patternStr
	 *            The pattern string to be validated.
	 * @return The validated pattern.
	 */
	protected String valPattern( String patternStr )
	{
		String patt;
		if ( patternStr == null || getCategory( ).equals( patternStr ) )
		{
			/**
			 * when the input patternStr is null or equals the category, use its
			 * corresponding defalut pattern value.
			 */
			patt = getDefaultPatt( );
		}
		else
		{
			patt = patternStr;
		}
		return patt;
	}

	/**
	 * Gets default pattern for predefined format category. Should be overrided
	 * in sub classes, including :{ FormatCurrencyNumPattern,
	 * FormatFixedNumPattern, FormatPercentNumPattern,
	 * FormatScientificNumPattern.}
	 */
	protected String getDefaultPatt( )
	{
		return this.category;
	}
}