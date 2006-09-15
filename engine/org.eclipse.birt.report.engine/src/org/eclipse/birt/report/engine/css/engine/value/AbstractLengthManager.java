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

package org.eclipse.birt.report.engine.css.engine.value;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSContext;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * This class provides a manager for the property with support for length
 * values.
 * 
 * @version $Id: AbstractLengthManager.java,v 1.3 2005/11/22 09:59:57 wyan Exp $
 */
public abstract class AbstractLengthManager extends AbstractValueManager
{

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	public Value createValue( LexicalUnit lu, CSSEngine engine )
			throws DOMException
	{
		switch ( lu.getLexicalUnitType( ) )
		{
			case LexicalUnit.SAC_EM :
				return new FloatValue( CSSPrimitiveValue.CSS_EMS, lu
						.getFloatValue( ) );

			case LexicalUnit.SAC_EX :
				return new FloatValue( CSSPrimitiveValue.CSS_EXS, lu
						.getFloatValue( ) );

			case LexicalUnit.SAC_PIXEL :
				return new FloatValue( CSSPrimitiveValue.CSS_PX, lu
						.getFloatValue( ) );

			case LexicalUnit.SAC_CENTIMETER :
				return new FloatValue( CSSPrimitiveValue.CSS_CM, lu
						.getFloatValue( ) );

			case LexicalUnit.SAC_MILLIMETER :
				return new FloatValue( CSSPrimitiveValue.CSS_MM, lu
						.getFloatValue( ) );

			case LexicalUnit.SAC_INCH :
				return new FloatValue( CSSPrimitiveValue.CSS_IN, lu
						.getFloatValue( ) );

			case LexicalUnit.SAC_POINT :
				return new FloatValue( CSSPrimitiveValue.CSS_PT, lu
						.getFloatValue( ) );

			case LexicalUnit.SAC_PICA :
				return new FloatValue( CSSPrimitiveValue.CSS_PC, lu
						.getFloatValue( ) );

			case LexicalUnit.SAC_INTEGER :
				return new FloatValue( CSSPrimitiveValue.CSS_NUMBER, lu
						.getIntegerValue( ) );

			case LexicalUnit.SAC_REAL :
				return new FloatValue( CSSPrimitiveValue.CSS_NUMBER, lu
						.getFloatValue( ) );

			case LexicalUnit.SAC_PERCENTAGE :
				return new FloatValue( CSSPrimitiveValue.CSS_PERCENTAGE, lu
						.getFloatValue( ) );
		}
		throw createInvalidLexicalUnitDOMException( lu.getLexicalUnitType( ) );
	}

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	public Value createFloatValue( short type, float floatValue )
			throws DOMException
	{
		switch ( type )
		{
			case CSSPrimitiveValue.CSS_PERCENTAGE :
			case CSSPrimitiveValue.CSS_EMS :
			case CSSPrimitiveValue.CSS_EXS :
			case CSSPrimitiveValue.CSS_PX :
			case CSSPrimitiveValue.CSS_CM :
			case CSSPrimitiveValue.CSS_MM :
			case CSSPrimitiveValue.CSS_IN :
			case CSSPrimitiveValue.CSS_PT :
			case CSSPrimitiveValue.CSS_PC :
			case CSSPrimitiveValue.CSS_NUMBER :
				return new FloatValue( type, floatValue );
		}
		throw createInvalidFloatTypeDOMException( type );
	}

	/**
	 * Implements {@link
	 * ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public Value computeValue( CSSStylableElement elt, CSSEngine engine,
			int idx, Value value )
	{
		if ( value.getCssValueType( ) == CSSValue.CSS_PRIMITIVE_VALUE )
		{
			switch ( value.getPrimitiveType( ) )
			{
				case CSSPrimitiveValue.CSS_NUMBER :
					return value;

				case CSSPrimitiveValue.CSS_PX :
					float v = value.getFloatValue( );
					CSSContext cx = engine.getCSSContext( );
					float ratio = cx.getPixelUnitToMillimeter( );
					return new FloatValue( CSSPrimitiveValue.CSS_NUMBER, v
							* ratio / 25.4f * 72000.0f );

				case CSSPrimitiveValue.CSS_MM :
					v = value.getFloatValue( );
					return new FloatValue( CSSPrimitiveValue.CSS_NUMBER,
							v / 25.4f * 72000.0f );

				case CSSPrimitiveValue.CSS_CM :
					v = value.getFloatValue( );
					return new FloatValue( CSSPrimitiveValue.CSS_NUMBER,
							v / 2.54f * 72000.0f );

				case CSSPrimitiveValue.CSS_IN :
					v = value.getFloatValue( );
					return new FloatValue( CSSPrimitiveValue.CSS_NUMBER,
							v * 72000.0f );

				case CSSPrimitiveValue.CSS_PT :
					v = value.getFloatValue( );
					return new FloatValue( CSSPrimitiveValue.CSS_NUMBER,
							v * 1000.0f );

				case CSSPrimitiveValue.CSS_PC :
					v = value.getFloatValue( );
					return new FloatValue( CSSPrimitiveValue.CSS_NUMBER,
							v / 12.0f * 1000.0f );

				case CSSPrimitiveValue.CSS_EMS :
					v = value.getFloatValue( );
					Value fontSize = (Value) elt.getComputedStyle( )
							.getProperty( IStyle.STYLE_FONT_SIZE );
					float fs = fontSize.getFloatValue( );
					return new FloatValue( CSSPrimitiveValue.CSS_NUMBER, v * fs );

				case CSSPrimitiveValue.CSS_EXS :
					v = value.getFloatValue( );
					fontSize = (Value) elt.getComputedStyle( ).getProperty(
							IStyle.STYLE_FONT_SIZE );
					fs = fontSize.getFloatValue( );
					return new FloatValue( CSSPrimitiveValue.CSS_NUMBER, v * fs
							* 0.5f );
			}
		}
		return value;
	}
}
