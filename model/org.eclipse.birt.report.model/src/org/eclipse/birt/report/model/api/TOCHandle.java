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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Represents an "TOC" attached to an element.Obtain an instance of this class
 * by calling the <code>getTOCHandle</code> method on the handle of an element
 * that defines an action.
 * 
 */

public class TOCHandle extends StructureHandle
{

	/**
	 * Construct an handle to deal with the toc structure.
	 * 
	 * @param element
	 *            the element that defined the action.
	 * @param ref
	 *            reference to the toc property.
	 */

	public TOCHandle( DesignElementHandle element, MemberRef ref )
	{
		super( element, ref );
	}

	/**
	 * Gets expression of TOC.
	 * 
	 * @return expression of TOC.
	 */

	public String getExpression( )
	{
		return getStringProperty( TOC.TOC_EXPRESSION );
	}

	/**
	 * Sets expression of TOC.
	 * 
	 * @param expression
	 *            expression of TOC
	 * @throws SemanticException
	 *             semantic exception
	 */

	public void setExpression( String expression ) throws SemanticException
	{
		setProperty( TOC.TOC_EXPRESSION, expression );
	}

	/**
	 * Gets style of TOC.
	 * 
	 * @return style name of TOC
	 */

	public String getStyleName( )
	{
		StyleHandle handle = getStyle( );
		if ( handle == null )
			return null;
		return handle.getName( );
	}

	/**
	 * Sets style of TOC.
	 * 
	 * @param styleName
	 *            style name
	 * @throws SemanticException
	 */

	public void setStyleName( String styleName ) throws SemanticException
	{
		setProperty( TOC.TOC_STYLE, styleName );
	}

	/**
	 * Gets TOC style.
	 * 
	 * @return style handle.
	 */

	private StyleHandle getStyle( )
	{
		Object value = ( (Structure) getStructure( ) ).getLocalProperty(
				getModule( ), TOC.TOC_STYLE );
		if ( value instanceof ElementRefValue )
		{
			ElementRefValue refValue = (ElementRefValue) value;
			if ( refValue.isResolved( ) )
			{
				Style style = (Style) refValue.getElement( );
				return (SharedStyleHandle) style.getHandle( style.getRoot( ) );
			}
		}
		return null;
	}
	
	/**
	 * Returns a handle to work with the style properties of toc element. Use a
	 * style handle to work with the specific getter/setter methods for each
	 * style property. The style handle is not necessary to work with style
	 * properties generically.
	 * <p>
	 * Note a key difference between this method and the
	 * <code>getStyle( )</code> method. This method returns a handle to the
	 * <em>this</em> element. The <code>getStyle( )</code> method returns a
	 * handle to the shared style, if any, that this element references.
	 * 
	 * @return a style handle to work with the style properties of this element.
	 *         Returns <code>null</code> if this element does not have style
	 *         properties.
	 */

	public PrivateStyleHandle getPrivateStyle( )
	{
		return new PrivateStyleHandle( getModule( ), getElement( ) );
	}

/**
	 * Gets border-top-style property.
	 * 
	 * @return border-top-style property
	 */
	
	public String getBorderTopStyle( )
	{
		Object value = getProperty( TOC.BORDER_TOP_STYLE_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderTopStyle( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets border-top-width property.
	 * 
	 * @return border-top-width property
	 */
	
	public DimensionHandle getBorderTopWidth( )
	{
		Object value = getProperty( TOC.BORDER_TOP_WIDTH_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderTopWidth( );
			}
		}
		return (DimensionHandle) value;
	}

	/**
	 * Gets border-top-color property.
	 * 
	 * @return border-top-color property
	 */
	
	public ColorHandle getBorderTopColor( )
	{
		Object value = getProperty( TOC.BORDER_TOP_COLOR_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderTopColor( );
			}
		}
		return (ColorHandle) value;
	}

	/**
	 * Gets border-left-style property.
	 * 
	 * @return border-left-style property
	 */

	public String getBorderLeftStyle( )
	{
		Object value = getProperty( TOC.BORDER_LEFT_STYLE_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderLeftStyle( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets border-left-width property.
	 * 
	 * @return border-left-width property
	 */

	public DimensionHandle getBorderLeftWidth( )
	{
		Object value = getProperty( TOC.BORDER_LEFT_WIDTH_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderLeftWidth( );
			}
		}
		return (DimensionHandle) value;
	}

	/**
	 * Gets border-left-color property.
	 * 
	 * @return border-left-color property
	 */

	public ColorHandle getBorderLeftColor( )
	{
		Object value = getProperty( TOC.BORDER_LEFT_COLOR_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderLeftColor( );
			}
		}
		return (ColorHandle) value;
	}

	/**
	 * Gets border-bottom-style property.
	 * 
	 * @return border-bottom-style property
	 */

	public String getBorderBottomStyle( )
	{
		Object value = getProperty( TOC.BORDER_BOTTOM_STYLE_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderBottomStyle( );
			}
		}
		return (String) value;
	}

	/**
	 * 
	 * Gets border-bottom-width property.
	 * 
	 * @return border-bottom-width property
	 */

	public DimensionHandle getBorderBottomWidth( )
	{
		Object value = getProperty( TOC.BORDER_BOTTOM_WIDTH_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderBottomWidth( );
			}
		}
		return (DimensionHandle) value;
	}

	/**
	 * Gets border-bottom-width property.
	 * 
	 * @return border-bottom-width property
	 */

	public ColorHandle getBorderBottomColor( )
	{
		Object value = getProperty( TOC.BORDER_BOTTOM_COLOR_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderBottomColor( );
			}
		}
		return (ColorHandle) value;
	}

	/**
	 * Gets border-right-style property.
	 * 
	 * @return border-right-style property
	 */

	public String getBorderRightStyle( )
	{
		Object value = getProperty( TOC.BORDER_RIGHT_STYLE_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderRightStyle( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets border-right-width property.
	 * 
	 * @return border-right-width property
	 */

	public DimensionHandle getBorderRightWidth( )
	{
		Object value = getProperty( TOC.BORDER_RIGHT_WIDTH_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderRightWidth( );
			}
		}
		return (DimensionHandle) value;
	}

	/**
	 * Gets border-right-color property.
	 * 
	 * @return border-right-color property
	 */

	public ColorHandle getBorderRightColor( )
	{
		Object value = getProperty( TOC.BORDER_RIGHT_COLOR_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBorderRightColor( );
			}
		}
		return (ColorHandle) value;
	}

	/**
	 * Gets back-ground-color property.
	 * 
	 * @return back-ground-color property
	 */

	public ColorHandle getBackgroundColor( )
	{
		Object value = getProperty( TOC.BACKGROUND_COLOR_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getBackgroundColor( );
			}
		}
		return (ColorHandle) value;
	}

	/**
	 * Gets date time format property.
	 * 
	 * @return date time format property
	 */

	public String getDateTimeFormat( )
	{
		Object value = getProperty( TOC.DATE_TIME_FORMAT_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getDateTimeFormat( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets number format property.
	 * 
	 * @return number format property
	 */

	public String getNumberFormat( )
	{
		Object value = getProperty( TOC.NUMBER_FORMAT_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getNumberFormat( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets number align property.
	 * 
	 * @return number align property
	 */

	public String getNumberAlign( )
	{
		Object value = getProperty( TOC.NUMBER_ALIGN_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				// TODO no related method in style
			}
		}
		return (String) value;
	}

	/**
	 * Gets string format property.
	 * 
	 * @return string format property
	 */

	public String getStringFormat( )
	{
		Object value = getProperty( TOC.STRING_FORMAT_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getStringFormat( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets font family property.
	 * 
	 * @return font family property
	 */

	public FontHandle getFontFamily( )
	{
		Object value = getProperty( TOC.FONT_FAMILY_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getFontFamilyHandle( );
			}
		}
		return (FontHandle) value;
	}

	/**
	 * Gets font size property.
	 * 
	 * @return font size property
	 */

	public DimensionHandle getFontSize( )
	{
		Object value = getProperty( TOC.FONT_SIZE_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getFontSize( );
			}
		}
		return (DimensionHandle) value;
	}

	/**
	 * Gets font style property.
	 * 
	 * @return font style property
	 */

	public String getFontStyle( )
	{
		Object value = getProperty( TOC.FONT_STYLE_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getFontStyle( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets font weight property.
	 * 
	 * @return font weight property
	 */

	public String getFontWeight( )
	{
		Object value = getProperty( TOC.FONT_WEIGHT_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getFontWeight( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets font Variant property.
	 * 
	 * @return font Variant property
	 */

	public String getFontVariant( )
	{
		Object value = getProperty( TOC.FONT_VARIANT_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getFontVariant( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets font color property.
	 * 
	 * @return font color property
	 */

	public ColorHandle getColor( )
	{
		Object value = getProperty( TOC.COLOR_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getColor( );
			}
		}
		return (ColorHandle) value;
	}

	/**
	 * Gets Text Underline property.
	 * 
	 * @return Text Underline property
	 */

	public String getTextUnderline( )
	{
		Object value = getProperty( TOC.TEXT_UNDERLINE_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getTextUnderline( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets Text Overline property.
	 * 
	 * @return Text Overline property
	 */

	public String getTextOverline( )
	{
		Object value = getProperty( TOC.TEXT_OVERLINE_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getTextOverline( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets Text Line Through property.
	 * 
	 * @return Text Line Through property
	 */

	public String getTextLineThrough( )
	{
		Object value = getProperty( TOC.TEXT_LINE_THROUGH_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getTextLineThrough( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets text align property.
	 * 
	 * @return text align property
	 */

	public String getTextAlign( )
	{
		Object value = getProperty( TOC.TEXT_ALIGN_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getTextAlign( );
			}
		}
		return (String) value;
	}

	/**
	 * Gets text indent property.
	 * 
	 * @return text indent property
	 */

	public DimensionHandle getTextIndent( )
	{
		Object value = getProperty( TOC.TEXT_INDENT_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getTextIndent( );
			}
		}
		return (DimensionHandle) value;
	}

	/**
	 * Gets text transform property.
	 * 
	 * @return text transform property
	 */

	public String getTextTransform( )
	{
		Object value = getProperty( TOC.TEXT_TRANSFORM_MEMBER );
		if ( value == null )
		{
			StyleHandle style = getStyle( );
			if ( style == null )
			{
				return null;
			}
			else
			{
				return style.getTextTransform( );
			}
		}
		return (String) value;
	}
}
