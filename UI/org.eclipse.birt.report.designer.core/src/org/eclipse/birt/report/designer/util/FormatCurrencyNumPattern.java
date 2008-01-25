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

import java.util.Locale;

import org.eclipse.birt.report.designer.nls.Messages;

import com.ibm.icu.util.Currency;

/**
 * A pattern class serves for getting and setting pattern string for a currency.
 */
public class FormatCurrencyNumPattern extends FormatNumberPattern
{

	private int decPlaces = 0;
	private boolean useSep = false;
	private boolean useBracket = false;
	private String symbol = ""; //$NON-NLS-1$
	private String symPos = ""; //$NON-NLS-1$

	private static String[] symbols = {
			// "none", "£¤","$", "?", "¡ê"
			Messages.getString( "FormatNumberPage.currency.symbol.none" ), //$NON-NLS-1$
			Currency.getInstance( Locale.getDefault( ) ).getSymbol( ),
			"\u00A5", "$", "\u20ac", "\u00A3", "\u20A9" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	};

	/**
	 * Constructor.
	 * 
	 * @param category
	 *            Category name for currency number format pattern.
	 */
	public FormatCurrencyNumPattern( String category )
	{
		super( category );
		setType( 'C' );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.NumGeneralPattern#getPattern()
	 */
	public String getPattern( )
	{
		String numStr = "###0"; //$NON-NLS-1$
		String decStr = ""; //$NON-NLS-1$

		String positivePatt = numStr;

		String negativePatt = null;

		String pattern;

		if ( useSep )
		{
			positivePatt = "#,##0"; //$NON-NLS-1$
		}
		decStr = DEUtil.getDecmalStr( decPlaces );

		if ( decStr != "" ) //$NON-NLS-1$
		{
			positivePatt = positivePatt + "." + decStr; //$NON-NLS-1$
		}
		if ( useBracket )
		{
			negativePatt = "(" + positivePatt + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if ( symbol.length( ) != 0
				&& !symbol.equalsIgnoreCase( FormatNumberPattern.TEXT_CURRENCY_SYMBOL_NONE ) )
		{
			if ( symPos.equalsIgnoreCase( FormatNumberPattern.SYMBOL_POSITION_BEFORE ) )
			{
				positivePatt = symbol + positivePatt;
				if ( negativePatt != null )
				{
					negativePatt = symbol + negativePatt;
				}
			}
			else if ( symPos.equalsIgnoreCase( FormatNumberPattern.SYMBOL_POSITION_AFTER ) )
			{
				positivePatt = positivePatt + symbol;
				if ( negativePatt != null )
				{
					negativePatt = negativePatt + symbol;
				}
			}
		}
		if ( negativePatt != null )
		{
			pattern = positivePatt + ";" + negativePatt;//$NON-NLS-1$
		}
		else
		{
			pattern = positivePatt;
		}
		/**
		 * For currency, there is no default pattern, because DTE not support
		 * "currency" as a predefined pattern string.
		 */
		// if ( pattern.equals( DEFAULT_CURRENCY_PATTERN ) )
		// {
		// return getCategory( );
		// }
		return pattern;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.NumGeneralPattern#setPattern(java.lang.String)
	 */
	public void setPattern( String patternStr )
	{
		String patt = valPattern( patternStr );

		this.useSep = patt.indexOf( "," ) != -1; //$NON-NLS-1$
		this.useBracket = patt.indexOf( "(" ) != -1 //$NON-NLS-1$
				&& patt.indexOf( ")" ) != -1; //$NON-NLS-1$
		if ( patt.indexOf( "." ) != -1 ) //$NON-NLS-1$
		{
			this.decPlaces = patt.lastIndexOf( "0" ) - patt.lastIndexOf( "." ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		for ( int i = 0; i < symbols.length; i++ )
		{
			String sTemp = symbols[i];
			int sPos = patt.indexOf( sTemp );
			if ( sPos != -1 )
			{
				this.symbol = sTemp;
				if ( sPos == 0 )
				{
					this.symPos = FormatNumberPattern.SYMBOL_POSITION_BEFORE;
				}
				else
				{
					this.symPos = FormatNumberPattern.SYMBOL_POSITION_AFTER;
				}
				break;
			}
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.FormatNumberPattern#getDefaultPatt()
	 */
	protected String getDefaultPatt( )
	{
		return DEFAULT_CURRENCY_PATTERN;
	}

	/**
	 * Get decPlaces.
	 * 
	 * @return Returns the decPlaces.
	 */
	public int getDecPlaces( )
	{
		return decPlaces;
	}

	/**
	 * Set decPaces
	 * 
	 * @param decPlaces
	 *            The decPlaces to set.
	 */
	public void setDecPlaces( int decPlaces )
	{
		this.decPlaces = decPlaces;
	}

	/**
	 * Returns useSep.
	 */
	public boolean getUseSep( )
	{
		return this.useSep;
	}

	/**
	 * @param useSep
	 *            The useSep to set.
	 */
	public void setUseSep( boolean useSep )
	{
		this.useSep = useSep;
	}

	/**
	 * Returns useBracket.
	 */
	public boolean getUseBracket( )
	{
		return this.useBracket;
	}

	/**
	 * @param useBracket
	 *            The useBracket to set.
	 */
	public void setUseBracket( boolean useBracket )
	{
		this.useBracket = useBracket;
	}

	/**
	 * Get symbeol
	 * 
	 * @return Returns the symbol.
	 */
	public String getSymbol( )
	{
		return symbol;
	}

	/**
	 * Set symbol
	 * 
	 * @param symbol
	 *            The symbol to set.
	 */
	public void setSymbol( String symbol )
	{
		this.symbol = symbol;
	}

	/**
	 * get SysmPos
	 * 
	 * @return Returns the symPos.
	 */
	public String getSymPos( )
	{
		return symPos;
	}

	/**
	 * Set symPos
	 * 
	 * @param symPos
	 *            The symPos to set.
	 */
	public void setSymPos( String symPos )
	{
		this.symPos = symPos;
	}
}