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

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

/**
 * This class implments border conflict algorithm.
 * <p>
 * In the collapsing border model, borders at every edge of every cell may be
 * specified by border properties on a variety of elements that meet at that
 * edge (cells, rows, row groups, columns, column groups, and the table itself),
 * and these borders may vary in width, style, and color. The rule of thumb is
 * that at each edge the most "eye catching" border style is chosen, except that
 * any occurrence of the style <code>hidden</code> unconditionally turns the
 * border off.
 * <p>
 * The following rules determine which border style "wins" in case of a
 * conflict:
 * <ul>
 * <li>Borders with the 'border-style' of 'hidden' take precedence over all
 * other conflicting borders. Any border with this value suppresses all borders
 * at this location.
 * <li>Borders with a style of 'none' have the lowest priority. Only if the
 * border properties of all the elements meeting at this edge are 'none' will
 * the border be omitted (but note that <code>none</code> is the default value
 * for the border style.)
 * <li>If none of the styles are 'hidden' and at least one of them is not
 * 'none', then narrow borders are discarded in favor of wider ones. If several
 * have the same 'border-width' then styles are preferred in this order:
 * <code>double</code>, <code>solid</code>, <code>dashed</code>,
 * <code>dotted</code>, <code>ridge</code>, <code>outset</code>,
 * <code>groove</code>, and the lowest: <code>inset</code>.
 * <li>If border styles differ only in color, then a style set on a cell wins
 * over one on a row, which wins over a row group, column, column group and,
 * lastly, table. It is undefined which color is used when two elements of the
 * same type disagree.
 * </ul>
 * 
 * 
 */
///TODO: change the border style's resolve.
public class BorderConflictResolver
{

	final static int POSITION_LEFT = 0;

	final static int POSITION_TOP = 1;

	final static int POSITION_RIGHT = 2;

	final static int POSITION_BOTTOM = 3;

	static HashMap styleMap = null;
	static
	{
		styleMap = new HashMap( );
		styleMap.put( IStyle.NONE_VALUE, new Integer( 0 ) );
		styleMap.put( IStyle.INSET_VALUE, new Integer( 1 ) );
		styleMap.put( IStyle.GROOVE_VALUE, new Integer( 2 ) );
		styleMap.put( IStyle.OUTSET_VALUE, new Integer( 3 ) );
		styleMap.put( IStyle.RIDGE_VALUE, new Integer( 4 ) );
		styleMap.put( IStyle.DOTTED_VALUE, new Integer( 5 ) );
		styleMap.put( IStyle.DASHED_VALUE, new Integer( 6 ) );
		styleMap.put( IStyle.SOLID_VALUE, new Integer( 7 ) );
		styleMap.put( IStyle.DOUBLE_VALUE, new Integer( 8 ) );
	}

	/**
	 * The used style should be style of area which is writable, and the others
	 * are styles of content which is read-only.
	 * 
	 * @param tableLeft
	 * @param columnLeft
	 * @param cellLeft
	 * @param usedStyle
	 */
	public void resolveTableLeftBorder( IStyle tableLeft, IStyle rowLeft,
			IStyle columnLeft, IStyle cellLeft, IStyle usedStyle )
	{
		resolveBorder( new BorderStyleInfo[]{
				new BorderStyleInfo( cellLeft, POSITION_LEFT ),
				new BorderStyleInfo( columnLeft, POSITION_LEFT ),
				new BorderStyleInfo( rowLeft, POSITION_LEFT ),
				new BorderStyleInfo( tableLeft, POSITION_LEFT )},
				new BorderStyleInfo( usedStyle, POSITION_LEFT ) );

	}

	/**
	 * The used style should be style of area which is writable, and the others
	 * are styles of content which is read-only.
	 * 
	 * @param tableTop
	 * @param rowTop
	 * @param cellTop
	 * @param usedStyle
	 */
	public void resolveTableTopBorder( IStyle tableTop, IStyle rowTop,
			IStyle columnTop, IStyle cellTop, IStyle usedStyle )
	{
		resolveBorder( new BorderStyleInfo[]{
				new BorderStyleInfo( cellTop, POSITION_TOP ),
				new BorderStyleInfo( rowTop, POSITION_TOP ),
				new BorderStyleInfo( columnTop, POSITION_TOP ),
				new BorderStyleInfo( tableTop, POSITION_TOP )},
				new BorderStyleInfo( usedStyle, POSITION_TOP ) );
	}

	/**
	 * The used style should be style of area which is writable, and the others
	 * are styles of content which is read-only.
	 * 
	 * @param tableBottom
	 * @param rowBottom
	 * @param cellBottom
	 * @param usedStyle
	 */
	public void resolveTableBottomBorder( IStyle tableBottom, IStyle rowBottom,
			IStyle columnBottom, IStyle cellBottom, IStyle usedStyle )
	{
		resolveBorder( new BorderStyleInfo[]{
				new BorderStyleInfo( cellBottom, POSITION_BOTTOM ),
				new BorderStyleInfo( rowBottom, POSITION_BOTTOM ),
				new BorderStyleInfo( columnBottom, POSITION_BOTTOM ),
				new BorderStyleInfo( tableBottom, POSITION_BOTTOM )},
				new BorderStyleInfo( usedStyle, POSITION_BOTTOM ) );
	}
	
	public void resolvePagenatedTableTopBorder(IStyle rowTop, IStyle cellTop, IStyle usedStyle)
	{
		resolveBorder( new BorderStyleInfo[]{
				new BorderStyleInfo( cellTop, POSITION_TOP ),
				new BorderStyleInfo( rowTop, POSITION_TOP )},
				new BorderStyleInfo( usedStyle, POSITION_TOP ) );
	}
	
	public void resolvePagenatedTableBottomBorder( IStyle rowBottom, IStyle cellBottom,
			IStyle usedStyle )
	{
		resolveBorder( new BorderStyleInfo[]{
				new BorderStyleInfo( cellBottom, POSITION_BOTTOM ),
				new BorderStyleInfo( rowBottom, POSITION_BOTTOM )},
				new BorderStyleInfo( usedStyle, POSITION_BOTTOM ) );
	}

	/**
	 * The used style should be style of area which is writable, and the others
	 * are styles of content which is read-only.
	 * 
	 * @param tableRight
	 * @param columnRight
	 * @param cellRight
	 * @param usedStyle
	 */
	public void resolveTableRightBorder( IStyle tableRight, IStyle rowRight,
			IStyle columnRight, IStyle cellRight, IStyle usedStyle )
	{
		resolveBorder( new BorderStyleInfo[]{
				new BorderStyleInfo( cellRight, POSITION_RIGHT ),
				new BorderStyleInfo( columnRight, POSITION_RIGHT ),
				new BorderStyleInfo( rowRight, POSITION_RIGHT ),
				new BorderStyleInfo( tableRight, POSITION_RIGHT )},
				new BorderStyleInfo( usedStyle, POSITION_RIGHT ) );
	}

	/**
	 * The used style should be style of area which is writable, and the others
	 * are styles of content which is read-only.
	 * 
	 * @param preColumnRight
	 * @param columnLeft
	 * @param preCellRight
	 * @param cellLeft
	 * @param usedStyle
	 */
	public void resolveCellLeftBorder( IStyle preColumnRight,
			IStyle columnLeft, IStyle preCellRight, IStyle cellLeft,
			IStyle usedStyle )
	{
		resolveBorder( new BorderStyleInfo[]{
				new BorderStyleInfo( preCellRight, POSITION_RIGHT ),
				new BorderStyleInfo( cellLeft, POSITION_LEFT ),
				new BorderStyleInfo( preColumnRight, POSITION_RIGHT ),
				new BorderStyleInfo( columnLeft, POSITION_LEFT )},
				new BorderStyleInfo( usedStyle, POSITION_LEFT ) );
	}

	/**
	 * The used style should be style of area which is writable, and the others
	 * are styles of content which is read-only.
	 * 
	 * @param preRowBottom
	 * @param rowTop
	 * @param preCellBottom
	 * @param cellTop
	 * @param usedStyle
	 */
	public void resolveCellTopBorder( IStyle preRowBottom, IStyle rowTop,
			IStyle preCellBottom, IStyle cellTop, IStyle usedStyle )
	{
		resolveBorder( new BorderStyleInfo[]{
				new BorderStyleInfo( preCellBottom, POSITION_BOTTOM ),
				new BorderStyleInfo( cellTop, POSITION_TOP ),
				new BorderStyleInfo( preRowBottom, POSITION_BOTTOM ),
				new BorderStyleInfo( rowTop, POSITION_TOP )},
				new BorderStyleInfo( usedStyle, POSITION_TOP ) );
	}

	private void resolveBorder( BorderStyleInfo[] styles,
			BorderStyleInfo usedStyle )
	{
		CSSValue[] borderStyles = new CSSValue[styles.length];
		for ( int i = 0; i < styles.length; i++ )
		{
			borderStyles[i] = styles[i].getBorderStyle( );
			if ( IStyle.HIDDEN_VALUE.equals( borderStyles[i] ) )
			{
				usedStyle.setBorderStyle( IStyle.HIDDEN_VALUE );
				return;
			}
		}
		
		//resolve border width
		int maxWidth = 0;
		int maxCount = 1;
		int maxFirstIndex = 0;
		int[] ws = new int[styles.length];
		CSSValue[] borderWidths = new CSSValue[styles.length];
		for ( int i = 0; i < styles.length; i++ )
		{
			borderWidths[i] = styles[i].getBorderWidth( );
			ws[i] = PropertyUtil
					.getDimensionValue( styles[i].getBorderWidth( ) );
			if ( ws[i] > maxWidth )
			{
				maxWidth = ws[i];
				maxCount = 1;
				maxFirstIndex = i;
			}
			else if ( ws[i] == maxWidth )
			{
				maxCount++;
			}
		}

		if ( maxCount == 1 )
		{
			usedStyle.setBorder( borderStyles[maxFirstIndex], borderWidths[maxFirstIndex], styles[maxFirstIndex].getBorderColor( ) );
			return;
		}
		else
		{
			//resolve border style
			int max = 0;
			int maxStyleIndex = 0;
			int[] ss = new int[styles.length];
			for ( int i = 0; i < styles.length; i++ )
			{
				if ( ws[i] == maxWidth )
				{
					ss[i] = ( (Integer) styleMap.get( styles[i].getBorderStyle( ) ) )
					.intValue( );
					if ( ss[i] > max )
					{
						max = ss[i];
						maxStyleIndex = i;
					}
				}
			}
			usedStyle.setBorder( borderStyles[maxStyleIndex], 
					borderWidths[maxStyleIndex]!=null ? borderWidths[maxStyleIndex] : IStyle.NUMBER_0, 
							styles[maxStyleIndex].getBorderColor( ) );
		}
	}

	protected class BorderStyleInfo
	{

		private int position;

		private IStyle style;

		public BorderStyleInfo( IStyle style, int position )
		{
			this.style = style;
			this.position = position;
		}

		public void setBorderColor( CSSValue value )
		{
			assert ( style != null );
			switch ( position )
			{
				case POSITION_LEFT :
					style.setProperty( IStyle.STYLE_BORDER_LEFT_COLOR, value );
					break;
				case POSITION_RIGHT :
					style.setProperty( IStyle.STYLE_BORDER_RIGHT_COLOR, value );
					break;
				case POSITION_TOP :
					style.setProperty( IStyle.STYLE_BORDER_TOP_COLOR, value );
					break;
				case POSITION_BOTTOM :
					style.setProperty( IStyle.STYLE_BORDER_BOTTOM_COLOR, value );
					break;
				default :
					assert false;

			}

		}

		public CSSValue getBorderColor( )
		{
			if ( style == null )
			{
				return IStyle.BLACK_RGB_VALUE;
			}
			switch ( position )
			{
				case POSITION_LEFT :
					return style.getProperty( IStyle.STYLE_BORDER_LEFT_COLOR );
				case POSITION_RIGHT :
					return style.getProperty( IStyle.STYLE_BORDER_RIGHT_COLOR );
				case POSITION_TOP :
					return style.getProperty( IStyle.STYLE_BORDER_TOP_COLOR );
				case POSITION_BOTTOM :
					return style.getProperty( IStyle.STYLE_BORDER_BOTTOM_COLOR );
				default :
					assert false;

			}
			return IStyle.BLACK_RGB_VALUE;
		}

		public CSSValue getBorderStyle( )
		{
			if ( style == null )
			{
				return IStyle.NONE_VALUE;
			}
			switch ( position )
			{
				case POSITION_LEFT :
					return style.getProperty( IStyle.STYLE_BORDER_LEFT_STYLE );
				case POSITION_RIGHT :
					return style.getProperty( IStyle.STYLE_BORDER_RIGHT_STYLE );
				case POSITION_TOP :
					return style.getProperty( IStyle.STYLE_BORDER_TOP_STYLE );
				case POSITION_BOTTOM :
					return style.getProperty( IStyle.STYLE_BORDER_BOTTOM_STYLE );
				default :
					assert false;

			}
			return IStyle.NONE_VALUE;
		}

		public CSSValue getBorderWidth( )
		{
			if ( style == null )
			{
				return null;
			}
			switch ( position )
			{
				case POSITION_LEFT :
					return style
							.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH );
				case POSITION_RIGHT :
					return style
							.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH );
				case POSITION_TOP :
					return style
							.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH );
				case POSITION_BOTTOM :
					return style
							.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH );
				default :
					assert false;

			}
			return null;
		}

		private void setBorderStyle( CSSValue value )
		{
			switch ( position )
			{
				case POSITION_LEFT :
					style.setProperty( IStyle.STYLE_BORDER_LEFT_STYLE, value );
					break;
				case POSITION_RIGHT :
					style.setProperty( IStyle.STYLE_BORDER_RIGHT_STYLE, value );
					break;
				case POSITION_TOP :
					style.setProperty( IStyle.STYLE_BORDER_TOP_STYLE, value );
					break;
				case POSITION_BOTTOM :
					style.setProperty( IStyle.STYLE_BORDER_BOTTOM_STYLE, value );
					break;
				default :
					assert false;

			}
		}

		private void setBorderWidth( CSSValue value )
		{
			switch ( position )
			{
				case POSITION_LEFT :
					style.setProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH,
							value );
					break;
				case POSITION_RIGHT :
					style.setProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH,
							value );
					break;
				case POSITION_TOP :
					style.setProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH,
							value );
					break;
				case POSITION_BOTTOM :
					style.setProperty(
							StyleConstants.STYLE_BORDER_BOTTOM_WIDTH, value );
					break;
				default :
					assert false;

			}

		}

		public void setBorder(CSSValue style, CSSValue width, CSSValue color)
		{
			setBorderStyle( style );
			setBorderWidth( width );
			setBorderColor( color );
		}
	}
}
