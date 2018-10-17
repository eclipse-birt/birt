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

import java.util.Map;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.AbstractStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

public class BirtStyle {
	
	public static final int NUMBER_OF_STYLES = StylePropertyIndexes.NUMBER_OF_BIRT_PROPERTIES + 1;
	public static final int TEXT_ROTATION = StylePropertyIndexes.NUMBER_OF_BIRT_PROPERTIES;
	
	private CSSValue[] propertyOverride = new CSSValue[ BirtStyle.NUMBER_OF_STYLES ]; 
	private CSSEngine cssEngine;
	
	public BirtStyle( CSSEngine cssEngine ) {
		this.cssEngine = cssEngine;
	}
	
	public BirtStyle(IContent element) {
		IStyle elemStyle = element.getComputedStyle();
		
		if( elemStyle instanceof AbstractStyle ) {
			cssEngine = ((AbstractStyle)elemStyle).getCSSEngine();
		} else {
			throw new IllegalStateException( "Unable to obtain CSSEngine from elemStyle: " + elemStyle );
		}
		
		Float rotation = extractRotation(element);
		if( rotation != null ) {
			setFloat(TEXT_ROTATION, CSSPrimitiveValue.CSS_DEG, rotation);
		}
		
		// Cache the element properties to avoid calculation cost many time
		for( int i = 0; i < StyleManager.COMPARE_CSS_PROPERTIES.length; ++i ) {
			int prop = StyleManager.COMPARE_CSS_PROPERTIES[ i ];
			propertyOverride[ prop ] = elemStyle.getProperty( prop ); 
		}
		propertyOverride[ StylePropertyIndexes.STYLE_DATA_FORMAT ] = elemStyle.getProperty( StylePropertyIndexes.STYLE_DATA_FORMAT ); 
		for( int i = 0; i < FontManager.COMPARE_CSS_PROPERTIES.length; ++i ) {
			int prop = FontManager.COMPARE_CSS_PROPERTIES[ i ];
			propertyOverride[ prop ] = elemStyle.getProperty( prop ); 
		}
	}
	
	
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for( int i = 0; i < propertyOverride.length; ++i ) {
			CSSValue value = propertyOverride[ i ]; 
			if( value != null ) {
				if( value instanceof DataFormatValue ) {
					result = prime * result + StyleManagerUtils.dataFormatHash( (DataFormatValue)value );
				} else {
					String cssText = value.getCssText();
					if ( cssText != null ) {
						result = prime * result + cssText.hashCode();
					}
				}
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BirtStyle other = (BirtStyle) obj;

		for( int i = 0; i < StyleManager.COMPARE_CSS_PROPERTIES.length; ++i ) {
			int prop = StyleManager.COMPARE_CSS_PROPERTIES[ i ];
			CSSValue value1 = getProperty( prop );
			CSSValue value2 = other.getProperty( prop );
			if( ! StyleManagerUtils.objectsEqual( value1, value2 ) ) {
				// System.out.println( "Differ on " + i + " because " + value1 + " != " + value2 );
				return false;
			}
		}
		if( ! StyleManagerUtils.objectsEqual( getProperty( BirtStyle.TEXT_ROTATION ), other.getProperty( BirtStyle.TEXT_ROTATION ) ) ) {
			// System.out.println( "Differ on BirtStyle.TEXT_ROTATION because " + getProperty( BirtStyle.TEXT_ROTATION ) + " != " + other.getProperty( BirtStyle.TEXT_ROTATION ) );
			return false;
		}
		
		
		// Number format
		if( ! StyleManagerUtils.dataFormatsEquivalent( (DataFormatValue)getProperty( StylePropertyIndexes.STYLE_DATA_FORMAT )
				, (DataFormatValue)other.getProperty( StylePropertyIndexes.STYLE_DATA_FORMAT ) ) ) {
			// System.out.println( "Differ on DataFormat" );
			return false;
		}		
        
		// Font
		if( ! FontManager.fontsEquivalent( this, other ) ) {
			// System.out.println( "Differ on font" );
			return false;
		}
		return true;
	}

	private static Float extractRotation(IContent element) {
		Object generatorObject = element.getGenerateBy();
		if( generatorObject instanceof ReportElementDesign ) {
			ReportElementDesign generatorDesign = (ReportElementDesign)generatorObject;
			Map<String,Expression> userProps = generatorDesign.getUserProperties(); 
			if( userProps != null ) {
				Expression rotationExpression = userProps.get( ExcelEmitter.ROTATION_PROP );
				if( rotationExpression != null ) {
					try {
						return Float.valueOf( rotationExpression.getScriptText() );
					} catch( Exception ex ) {
					}
				}
			}
		}
		return null;
	}
	
	public void setProperty( int propIndex, CSSValue newValue ) {
		propertyOverride[ propIndex ] = newValue;
	}
	
	public CSSValue getProperty( int propIndex ) {
		return propertyOverride[ propIndex ];
		/*
		if( ( propertyOverride != null )
				&& ( propertyOverride[ propIndex ] != null ) ) {
			return propertyOverride[ propIndex ];
		}
		if( ( elemStyle != null ) && ( propIndex < StylePropertyIndexes.NUMBER_OF_STYLE ) ) {
			return elemStyle.getProperty( propIndex );
		} else {
			return null;
		}
		*/
	}
	
	public void setFloat( int propIndex, short units, float newValue ) {
		propertyOverride[ propIndex ] = new FloatValue( units, newValue );
	}
	
	public void parseString( int propIndex, String newValue ) {
		if( propIndex < StylePropertyIndexes.NUMBER_OF_BIRT_PROPERTIES ) {
			propertyOverride[ propIndex ] = cssEngine.parsePropertyValue( propIndex , newValue );
		} else {
			propertyOverride[ propIndex ] = new StringValue( StringValue.CSS_STRING, newValue);
		}
	}
	
	public String getString( int propIndex ) {
		CSSValue value = getProperty( propIndex );
		if( value != null ) {
			return value.getCssText();
		} else {
			return null;
		}
	}

	@Override
	protected BirtStyle clone() {
		BirtStyle result = new BirtStyle(this.cssEngine);

		result.propertyOverride = new CSSValue[ BirtStyle.NUMBER_OF_STYLES ];
				
		for(int i = 0; i < NUMBER_OF_STYLES; ++i ) {
			CSSValue value = getProperty( i );
			if( value != null ) {
				if( value instanceof DataFormatValue ) {
					value = StyleManagerUtils.cloneDataFormatValue((DataFormatValue)value);
				}
				
 				result.propertyOverride[ i ] = value;
 			}
		}
		
		return result;
	}

	private static final boolean[] SPECIAL_OVERLAY_PROPERTIES = PrepareSpecialOverlayProperties();
	
	private static boolean[] PrepareSpecialOverlayProperties() {
		boolean[] result = new boolean[ BirtStyle.NUMBER_OF_STYLES ];
		result[ StylePropertyIndexes.STYLE_BACKGROUND_COLOR ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_BOTTOM_STYLE ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_BOTTOM_WIDTH ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_BOTTOM_COLOR ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_LEFT_STYLE ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_LEFT_WIDTH ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_LEFT_COLOR ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_RIGHT_STYLE ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_RIGHT_WIDTH ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_RIGHT_COLOR ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_TOP_STYLE ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_TOP_WIDTH ] = true;
		result[ StylePropertyIndexes.STYLE_BORDER_TOP_COLOR ] = true;
		result[ StylePropertyIndexes.STYLE_VERTICAL_ALIGN ] = true;
		result[ StylePropertyIndexes.STYLE_DATA_FORMAT ] = true;
		return result;
	}
	
	private void overlayBorder( IStyle style, int propStyle, int propWidth, int propColour ) {
		CSSValue ovlStyle = style.getProperty( propStyle );
		CSSValue ovlWidth = style.getProperty( propWidth );
		CSSValue ovlColour = style.getProperty( propColour );
		if( ( ovlStyle != null )
				&& ( ovlWidth != null )
				&& ( ovlColour != null ) 
				&& ( ! CSSConstants.CSS_NONE_VALUE.equals( ovlStyle.getCssText() ) ) ) {
			setProperty( propStyle, ovlStyle );
			setProperty( propWidth, ovlWidth );
			setProperty( propColour, ovlColour );
		}
	}
	
	public void overlay( IContent element ) {
		
		// System.out.println( "overlay: Before - " + this.toString() );
		
		IStyle style = element.getComputedStyle();
		for(int propIndex = 0; propIndex < StylePropertyIndexes.NUMBER_OF_BIRT_PROPERTIES; ++propIndex ) {
			if( ! SPECIAL_OVERLAY_PROPERTIES[ propIndex ] ) {
				CSSValue overlayValue = style.getProperty( propIndex );
				if( overlayValue != null ) {
					setProperty( propIndex, overlayValue );
				}
			}	
		}
		
		// Background colour, only overlay if not null and not transparent
		CSSValue overlayBgColour = style.getProperty( StylePropertyIndexes.STYLE_BACKGROUND_COLOR );
		if( ( overlayBgColour != null ) 
				&& ( ! CSSConstants.CSS_TRANSPARENT_VALUE.equals( overlayBgColour.getCssText() ) )
				) {
			setProperty( StylePropertyIndexes.STYLE_BACKGROUND_COLOR, overlayBgColour );
		}
		
		// Borders, only overlay if all three components are not null - and then overlay all three
		overlayBorder( style, StylePropertyIndexes.STYLE_BORDER_BOTTOM_STYLE, StylePropertyIndexes.STYLE_BORDER_BOTTOM_WIDTH, StylePropertyIndexes.STYLE_BORDER_BOTTOM_COLOR );
		overlayBorder( style, StylePropertyIndexes.STYLE_BORDER_LEFT_STYLE, StylePropertyIndexes.STYLE_BORDER_LEFT_WIDTH, StylePropertyIndexes.STYLE_BORDER_LEFT_COLOR );
		overlayBorder( style, StylePropertyIndexes.STYLE_BORDER_RIGHT_STYLE, StylePropertyIndexes.STYLE_BORDER_RIGHT_WIDTH, StylePropertyIndexes.STYLE_BORDER_RIGHT_COLOR );
		overlayBorder( style, StylePropertyIndexes.STYLE_BORDER_TOP_STYLE, StylePropertyIndexes.STYLE_BORDER_TOP_WIDTH, StylePropertyIndexes.STYLE_BORDER_TOP_COLOR );
		
		// Vertical align, not computed safely, so only check immediate style
		CSSValue verticalAlign = element.getStyle().getProperty( StylePropertyIndexes.STYLE_VERTICAL_ALIGN );
		if( verticalAlign != null ) {
			setProperty( StylePropertyIndexes.STYLE_VERTICAL_ALIGN, verticalAlign );
		}
				
		// Data format
		CSSValue overlayDataFormat = style.getProperty( StylePropertyIndexes.STYLE_DATA_FORMAT );
		CSSValue localDataFormat = getProperty( StylePropertyIndexes.STYLE_DATA_FORMAT );
		if( ! StyleManagerUtils.dataFormatsEquivalent((DataFormatValue)overlayDataFormat, (DataFormatValue)localDataFormat) ) {
			setProperty( StylePropertyIndexes.STYLE_DATA_FORMAT, StyleManagerUtils.cloneDataFormatValue((DataFormatValue)overlayDataFormat) );
		}
		
		// Rotation
		Float rotation = extractRotation(element);
		if( rotation != null ) {
			setFloat(TEXT_ROTATION, CSSPrimitiveValue.CSS_DEG, rotation);
		}
	
		// System.out.println( "overlay: After - " + this.toString() );
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for( int i = 0; i < NUMBER_OF_STYLES; ++i ) {				
			CSSValue val = getProperty( i );
			if( val != null ) {
				try {
					result.append(StylePropertyIndexes.getPropertyName(i)).append(':').append(val.getCssText()).append("; ");
				} catch(Exception ex) {
					result.append(StylePropertyIndexes.getPropertyName(i)).append(":{").append(ex.getMessage()).append("}; ");						
				}
			}
		}
		return result.toString();
	}
	
	

}
