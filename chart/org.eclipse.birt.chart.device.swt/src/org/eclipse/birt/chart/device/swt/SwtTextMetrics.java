/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.swt;

import java.util.ArrayList;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.TextAdapter;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * 
 */
public final class SwtTextMetrics extends TextAdapter
{

	/**
	 * 
	 */
	private int iLineCount = 0;

	/**
	 * 
	 */
	private String[] oText = null;

	/**
	 * 
	 */
	private GC gc = null;

	/**
	 * 
	 */
	private Label la = null;

	/**
	 * 
	 */
	private final IDisplayServer ids;

	private Font font;

	/**
	 * The constructor initializes a tiny image that provides a graphics context
	 * capable of performing computations in the absence of a visual component
	 * 
	 * @param _ids
	 * @param _la
	 */
	public SwtTextMetrics( IDisplayServer _ids, Label _la )
	{
		gc = new GC( ( (SwtDisplayServer) _ids ).getDevice( ) );
		ids = _ids;
		la = _la;
		reuse( la );
	}

	/**
	 * Allows reuse of the multi-line text element for computing bounds of a
	 * different font
	 * 
	 * @param fd
	 */
	public final void reuse( Label la, double forceWrappingSize )
	{
		String s = la.getCaption( ).getValue( );

		if ( s == null )
		{
			s = IConstants.NULL_STRING;
		}
		String[] sa = splitOnBreaks( s, forceWrappingSize );
		if ( sa == null )
		{
			iLineCount = 1;
			oText = new String[]{
				s
			};
		}
		else
		{
			iLineCount = sa.length;
			oText = sa;
		}

		if ( forceWrappingSize > 0 )
		{
			// update label with new broken content.
			StringBuffer sb = new StringBuffer( );
			for ( int i = 0; i < oText.length; i++ )
			{
				sb.append( oText[i] ).append( "\n" ); //$NON-NLS-1$
			}

			if ( sb.length( ) > 0 )
			{
				sb.deleteCharAt( sb.length( ) - 1 );
			}

			la.getCaption( ).setValue( sb.toString( ) );
		}
	}

	/**
	 * Disposal of the internal image
	 */
	public final void dispose( )
	{
		disposeFont( );
		gc.dispose( );
	}

	public void disposeFont( )
	{
		if ( font != null )
		{
			font.dispose( );
			font = null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public final boolean isDisposed( )
	{
		return gc.isDisposed( );
	}

	protected Font getFont( )
	{
		if ( null == font )
		{
			font = (Font) ids.createFont( la.getCaption( ).getFont( ) );
		}
		return font;
	}

	/**
	 * 
	 * @param fm
	 * @return
	 */
	public final double getHeight( )
	{
		gc.setFont( getFont( ) );
		final int iHeight = gc.textExtent( "X" ).y; //$NON-NLS-1$
		return iHeight;
	}

	/**
	 * 
	 * @param fm
	 * @return
	 */
	public final double getDescent( )
	{
		gc.setFont( getFont( ) );
		final int iDescent = gc.getFontMetrics( ).getDescent( );
		return iDescent;
	}

	/**
	 * 
	 * @return The width of the line containing the maximum width (if multiline
	 *         split by hard breaks) or the width of the single line of text
	 */
	private final double stringWidth( )
	{
		gc.setFont( getFont( ) );
		double dWidth, dMaxWidth = 0;
		if ( iLineCount > 1 )
		{
			String[] sa = oText;
			for ( int i = 0; i < iLineCount; i++ )
			{
				dWidth = gc.textExtent( sa[i] ).x;
				if ( dWidth > dMaxWidth )
				{
					dMaxWidth = dWidth;
				}
			}
		}
		else
		{
			dMaxWidth = gc.textExtent( oText[0] ).x;
		}
		return dMaxWidth;
	}

	public final double getFullHeight( )
	{
		final Insets ins = la.getInsets( );
		return getHeight( ) * getLineCount( ) + ins.getTop( ) + ins.getBottom( );
	}

	public final double getFullWidth( )
	{
		final Insets ins = la.getInsets( );
		return stringWidth( ) + ins.getLeft( ) + ins.getRight( );
	}

	/**
	 * 
	 * @return The number of lines created due to the hard breaks inserted
	 */
	public final int getLineCount( )
	{
		return iLineCount;
	}

	/**
	 * 
	 * @return The line requested for
	 */
	public final String getLine( int iIndex )
	{
		return ( iLineCount > 1 ) ? oText[iIndex] : oText[0];
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private String[] splitOnBreaks( String s, double maxSize )
	{
		final ArrayList al = new ArrayList( );

		if ( maxSize > 0 )
		{
			gc.setFont( getFont( ) );
		}

		int i = 0, j;
		do
		{
			j = s.indexOf( '\n', i );
			if ( j == -1 )
			{
				j = s.length( );
			}
			String ss = s.substring( i, j ).trim( );
			if ( ss != null && ss.length( ) > 0 )
			{
				// check max size.
				if ( maxSize > 0 )
				{
					Point size = gc.textExtent( ss );
					if ( size.x > maxSize )
					{
						// try fuzzy match first
						int estCount = (int) ( maxSize / size.x ) * ss.length( );

						if ( estCount < 1 )
						{
							estCount = ss.length( );
						}

						String fs;
						Point fsize;
						int curPos = 0;

						while ( ss.length( ) > 0 )
						{
							fs = ss.substring( 0, Math.min( estCount,
									ss.length( ) ) );
							fsize = gc.textExtent( fs );

							if ( fsize.x <= maxSize )
							{
								al.add( fs );
								curPos = fs.length( );
							}
							else
							{
								boolean matched = false;

								// decrease the count and test again.
								int curCount = Math.min( estCount - 1,
										ss.length( ) );
								while ( curCount > 1 )
								{
									fs = ss.substring( 0, curCount );
									fsize = gc.textExtent( fs );

									if ( fsize.x <= maxSize )
									{
										al.add( fs );
										curPos = fs.length( );
										matched = true;
										break;
									}
									else
									{
										curCount--;
									}
								}

								if ( !matched )
								{
									al.add( fs );
									curPos = fs.length( );
								}
							}

							ss = ss.substring( curPos );
							curPos = 0;
						}

					}
					else
					{
						al.add( ss );
					}
				}
				else
				{
					al.add( ss );
				}
			}
			i = j + 1;
		} while ( j != -1 && j < s.length( ) );

		final int n = al.size( );
		if ( n == 1 || n == 0 )
		{
			return null;
		}

		final String[] sa = new String[n];
		for ( i = 0; i < al.size( ); i++ )
		{
			sa[i] = (String) al.get( i );
		}
		return sa;
	}
}
