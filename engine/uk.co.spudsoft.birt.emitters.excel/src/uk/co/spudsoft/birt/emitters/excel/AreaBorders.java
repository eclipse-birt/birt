/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.w3c.dom.css.CSSValue;

public class AreaBorders {
	public boolean isMergedCells;
	
	public int bottom;
	public int left;
	public int right;
	public int top;
	
	public CSSValue[] cssStyle = new CSSValue[4];
	public CSSValue[] cssWidth = new CSSValue[4];
	public CSSValue[] cssColour = new CSSValue[4];

	private AreaBorders(boolean isMergedCells, int bottom, int left, int right, int top,
			CSSValue[] cssStyle, CSSValue[] cssWidth, CSSValue[] cssColour) {
		this.isMergedCells = isMergedCells;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		this.top = top;
		this.cssStyle = cssStyle;
		this.cssWidth = cssWidth;
		this.cssColour = cssColour;
	}

	public static AreaBorders create(int bottom, int left, int right, int top, BirtStyle borderStyle) {
		return create( false, bottom, left, right, top, borderStyle );
	}

	public static AreaBorders createForMergedCells(int bottom, int left, int right, int top, BirtStyle borderStyle) {
		return create( true, bottom, left, right, top, borderStyle );
	}
	
	public static AreaBorders create(boolean isMergedCells, int bottom, int left, int right, int top, BirtStyle borderStyle) {
		
		CSSValue borderStyleBottom = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_BOTTOM_STYLE );
		CSSValue borderWidthBottom = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_BOTTOM_WIDTH );
		CSSValue borderColourBottom = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_BOTTOM_COLOR );
		CSSValue borderStyleLeft = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_LEFT_STYLE );
		CSSValue borderWidthLeft = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_LEFT_WIDTH );
		CSSValue borderColourLeft = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_LEFT_COLOR );
		CSSValue borderStyleRight = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_RIGHT_STYLE );
		CSSValue borderWidthRight = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_RIGHT_WIDTH );
		CSSValue borderColourRight = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_RIGHT_COLOR );
		CSSValue borderStyleTop = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_TOP_STYLE );
		CSSValue borderWidthTop = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_TOP_WIDTH );
		CSSValue borderColourTop = borderStyle.getProperty( StylePropertyIndexes.STYLE_BORDER_TOP_COLOR );
				
/*		borderMsg.append( ", Bottom:" ).append( borderStyleBottom ).append( "/" ).append( borderWidthBottom ).append( "/" + borderColourBottom );
		borderMsg.append( ", Left:" ).append( borderStyleLeft ).append( "/" ).append( borderWidthLeft ).append( "/" + borderColourLeft );
		borderMsg.append( ", Right:" ).append( borderStyleRight ).append( "/" ).append( borderWidthRight ).append( "/" ).append( borderColourRight );
		borderMsg.append( ", Top:" ).append( borderStyleTop ).append( "/" ).append( borderWidthTop ).append( "/" ).append( borderColourTop );
		log.debug( borderMsg.toString() );
*/
		if( ( borderStyleBottom == null ) || ( CSSConstants.CSS_NONE_VALUE.equals( borderStyleBottom.getCssText() ) )
				|| ( borderWidthBottom == null ) || ( "0".equals(borderWidthBottom.getCssText()) )
				|| ( borderColourBottom == null ) || ( CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourBottom.getCssText() ) ) ) {
				borderStyleBottom = null;
				borderWidthBottom = null;
				borderColourBottom = null;
		}

		if( ( borderStyleLeft == null ) || ( CSSConstants.CSS_NONE_VALUE.equals( borderStyleLeft.getCssText() ) )
				|| ( borderWidthLeft == null ) || ( "0".equals(borderWidthLeft.getCssText()) )
				|| ( borderColourLeft == null ) || ( CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourLeft.getCssText() ) ) ) {
				borderStyleLeft = null;
				borderWidthLeft = null;
				borderColourLeft = null;
		}

        if( ( borderStyleRight == null ) || ( CSSConstants.CSS_NONE_VALUE.equals( borderStyleRight.getCssText() ) )
				|| ( borderWidthRight == null ) || ( "0".equals(borderWidthRight.getCssText()) )
				|| ( borderColourRight == null ) || ( CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourRight.getCssText() ) ) ) {
				borderStyleRight = null;
				borderWidthRight = null;
				borderColourRight = null;
		}

		if( ( borderStyleTop == null ) || ( CSSConstants.CSS_NONE_VALUE.equals( borderStyleTop.getCssText() ) )
				|| ( borderWidthTop == null ) || ( "0".equals(borderWidthTop.getCssText()) )
				|| ( borderColourTop == null ) || ( CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourTop.getCssText() ) ) ) {
				borderStyleTop = null;
				borderWidthTop = null;
				borderColourTop = null;
		}

		if( ( ( bottom >= 0 ) && ( ( borderStyleBottom != null ) || ( borderWidthBottom != null ) || ( borderColourBottom != null ) ) ) 
				|| ( ( left >= 0 ) && ( ( borderStyleLeft != null ) || ( borderWidthLeft != null ) || ( borderColourLeft != null ) ) )
				|| ( ( right >= 0 ) && ( ( borderStyleRight != null ) || ( borderWidthRight != null ) || ( borderColourRight != null ) ) ) 
				|| ( ( top >= 0 ) && ( ( borderStyleTop != null ) || ( borderWidthTop != null ) || ( borderColourTop != null ) ) ) 
				) {
			CSSValue[] cssStyle = new CSSValue[] { borderStyleBottom, borderStyleLeft, borderStyleRight, borderStyleTop };
			CSSValue[] cssWidth = new CSSValue[] { borderWidthBottom, borderWidthLeft, borderWidthRight, borderWidthTop };
			CSSValue[] cssColour = new CSSValue[] { borderColourBottom, borderColourLeft, borderColourRight, borderColourTop };
			return new AreaBorders(isMergedCells, bottom, left, right, top, cssStyle, cssWidth, cssColour);
		}
		return null;
	}


	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append( "[" ).append( top ).append( "," ).append( left ).append( "]" );
		result.append("-");
		result.append( "[" ).append( bottom ).append( "," ).append( right ).append( "]" );
		result.append("=");
		for( int i = 0; i < 4; ++i ) {
			result.append("[");
			result.append( cssStyle[i] );
			result.append(";");
			result.append( cssWidth[i] );
			result.append(";");
			result.append( cssColour[i] );
			result.append("]");
		}
		return result.toString();
	}
	
	
	
}
