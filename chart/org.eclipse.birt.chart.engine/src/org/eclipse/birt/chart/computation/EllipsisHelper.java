/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.exception.ChartException;

public class EllipsisHelper
{

	public interface ILabelVisibilityTester
	{

		public boolean testLabelVisible( String strNew, Object oPara )
				throws ChartException;
	}

	public static final String ELLIPSIS_STRING = "..."; //$NON-NLS-1$
	private int iMinCharToView = 0;
	private String sText;
	private ILabelVisibilityTester tester;

	public EllipsisHelper( ILabelVisibilityTester tester_, int iMinCharToView )
	{
		tester = tester_;
		this.iMinCharToView = iMinCharToView;
	}

	public void setIMinCharToView( int iMinCharToView )
	{
		this.iMinCharToView = iMinCharToView;
	}

	private boolean testNthChar( int iChar, Object oPara )
			throws ChartException
	{
		String newText = sText.substring( 0, iChar ) + ELLIPSIS_STRING;
		return tester.testLabelVisible( newText, oPara );
	}

	public boolean checkLabelEllipsis( String sText_, Object oPara )
			throws ChartException
	{
		sText = sText_;
		boolean bCanViewFullText = tester.testLabelVisible( sText, oPara );

		if ( bCanViewFullText )
		{
			// full text can be displayed, do not need ellipsis
			return true;
		}

		if ( iMinCharToView <= 0 )
		{
			// do not use ellipsis
			return bCanViewFullText;
		}

		int len = sText.length( ) - 1;

		if ( len < iMinCharToView )
		{
			return false;
		}

		if ( !testNthChar( iMinCharToView, oPara ) )
		{
			return false;
		}

		if ( len < 8 )
		{
			for ( int iChar = len; iChar >= iMinCharToView; iChar-- )
			{
				if ( testNthChar( iChar, oPara ) )
				{
					return true;
				}
			}

			return false;
		}
		else
		{
			int iStart = iMinCharToView;
			int iEnd = len;
			int iChar = iEnd;

			for ( int iLimit = 19; iLimit > 0 && iEnd > iStart + 1; iLimit-- )
			{
				iChar = ( iStart + iEnd ) / 2;

				if ( testNthChar( iChar, oPara ) )
				{
					iStart = iChar;
				}
				else
				{
					iEnd = iChar;
				}
			}

			if ( iChar != iStart )
			{
				return testNthChar( iStart, oPara );
			}
			else
			{
				return true;
			}
		}
	}

}
