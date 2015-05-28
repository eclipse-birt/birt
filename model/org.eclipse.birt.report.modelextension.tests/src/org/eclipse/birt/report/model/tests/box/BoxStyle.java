
package org.eclipse.birt.report.model.tests.box;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.elements.Style;

public class BoxStyle implements IStyleDeclaration
{

	String styleName = null;

	/**
	 * 
	 * @param name
	 */

	public BoxStyle( String name )
	{
		styleName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IStyleDeclaration#getName()
	 */
	public String getName( )
	{
		// TODO Auto-generated method stub
		return styleName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IStyleDeclaration#getProperty(java.lang.String)
	 */
	public Object getProperty( String name )
	{
		if ( name != null )
		{
			if ( name.equals( Style.NAME_PROP ) )
			{
				return styleName;
			}
			if ( name.indexOf( "color" ) != -1 || name.indexOf( "Color" ) != -1 ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return new Integer( ColorUtil.formRGB( 0xcc, 0xcc, 0xcc ) );
			}

			if ( name.startsWith( "border" ) && name.endsWith( "Style" ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return DesignChoiceConstants.LINE_STYLE_SOLID;
			}

			if ( name.startsWith( "border" ) && name.endsWith( "Width" ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return "1pt"; //$NON-NLS-1$
			}

			if ( name.startsWith( "margin" ) || name.startsWith( "padding" ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return "10pt"; //$NON-NLS-1$
			}

			if ( FONT_FAMILY_PROP.equals( name ) )
			{
				return "Tahoma"; //$NON-NLS-1$
			}

			if ( FONT_SIZE_PROP.equals( name ) )
			{
				return "12pt"; //$NON-NLS-1$
			}

			if ( FONT_STYLE_PROP.equals( name ) )
			{
				return DesignChoiceConstants.FONT_STYLE_ITALIC;
			}

			if ( FONT_WEIGHT_PROP.equals( name ) )
			{
				return DesignChoiceConstants.FONT_WEIGHT_BOLD;
			}

			if ( TEXT_LINE_THROUGH_PROP.equals( name ) )
			{
				return DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH;
			}

			if ( TEXT_UNDERLINE_PROP.equals( name ) )
			{
				return DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE;
			}

			if ( TEXT_OVERLINE_PROP.equals( name ) )
			{
				return DesignChoiceConstants.TEXT_OVERLINE_OVERLINE;
			}

			if ( TEXT_ALIGN_PROP.equals( name ) )
			{
				return DesignChoiceConstants.TEXT_ALIGN_CENTER;
			}

			if ( VERTICAL_ALIGN_PROP.equals( name ) )
			{
				return DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE;
			}

			if ( PAGE_BREAK_BEFORE_PROP.equals( name ) )
			{
				return DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS;
			}

			if ( PAGE_BREAK_AFTER_PROP.equals( name ) )
			{
				return DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS;
			}

			if ( PAGE_BREAK_INSIDE_PROP.equals( name ) )
			{
				return DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID;
			}

		}

		return null;
	}

}
