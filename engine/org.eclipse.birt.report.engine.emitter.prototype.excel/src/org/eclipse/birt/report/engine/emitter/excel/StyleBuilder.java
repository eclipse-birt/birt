
package org.eclipse.birt.report.engine.emitter.excel;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.content.IStyle;

public class StyleBuilder
{

	public static final String C_PATTERN = "(rgb\\()(\\d+)\\,(\\s?\\d+)\\,(\\s?\\d+)\\)";

	public static int[] UNHERITABLE = new int[]{
			StyleConstant.BORDER_BOTTOM_COLOR_PROP,
			StyleConstant.BORDER_BOTTOM_STYLE_PROP,
			StyleConstant.BORDER_BOTTOM_STYLE_PROP,
			StyleConstant.BORDER_TOP_COLOR_PROP,
			StyleConstant.BORDER_TOP_STYLE_PROP,
			StyleConstant.BORDER_TOP_STYLE_PROP,
			StyleConstant.BORDER_LEFT_COLOR_PROP,
			StyleConstant.BORDER_LEFT_STYLE_PROP,
			StyleConstant.BORDER_LEFT_STYLE_PROP,
			StyleConstant.BORDER_RIGHT_COLOR_PROP,
			StyleConstant.BORDER_RIGHT_STYLE_PROP,
			StyleConstant.BORDER_RIGHT_STYLE_PROP};

	public static final Pattern colorp = Pattern.compile( C_PATTERN,
			Pattern.CASE_INSENSITIVE );

	private static Logger logger = Logger.getLogger( StyleBuilder.class
			.getName( ) );

	public static StyleEntry createStyleEntry( IStyle style )
	{
		StyleEntry entry = new StyleEntry( );

		entry.setProperty( StyleConstant.BACKGROUND_COLOR_PROP,
				convertColor( style.getBackgroundColor( ) ) );

		float width = Float.parseFloat( style.getBorderBottomWidth( ) );
		if ( width > 0 )
		{
			entry.setProperty( StyleConstant.BORDER_BOTTOM_COLOR_PROP,
					convertColor( style.getBorderBottomColor( ) ) );

			entry.setProperty( StyleConstant.BORDER_BOTTOM_STYLE_PROP,
					convertBorderStyle( style.getBorderBottomStyle( ) ) );

			entry.setProperty( StyleConstant.BORDER_BOTTOM_WIDTH_PROP,
					convertBorderWeight( style.getBorderBottomWidth( ) ) );
		}

		width = Float.parseFloat( style.getBorderTopWidth( ) );
		if ( width > 0 )
		{
			entry.setProperty( StyleConstant.BORDER_TOP_COLOR_PROP,
					convertColor( style.getBorderTopColor( ) ) );

			entry.setProperty( StyleConstant.BORDER_TOP_STYLE_PROP,
					convertBorderStyle( style.getBorderTopStyle( ) ) );

			entry.setProperty( StyleConstant.BORDER_TOP_WIDTH_PROP,
					convertBorderWeight( style.getBorderTopWidth( ) ) );
		}

		width = Float.parseFloat( style.getBorderLeftWidth( ) );
		if ( width > 0 )
		{
			entry.setProperty( StyleConstant.BORDER_LEFT_COLOR_PROP,
					convertColor( style.getBorderLeftColor( ) ) );

			entry.setProperty( StyleConstant.BORDER_LEFT_STYLE_PROP,
					convertBorderStyle( style.getBorderLeftStyle( ) ) );

			entry.setProperty( StyleConstant.BORDER_LEFT_WIDTH_PROP,
					convertBorderWeight( style.getBorderLeftWidth( ) ) );
		}

		width = Float.parseFloat( style.getBorderRightWidth( ) );
		if ( width > 0 )
		{
			entry.setProperty( StyleConstant.BORDER_RIGHT_COLOR_PROP,
					convertColor( style.getBorderRightColor( ) ) );

			entry.setProperty( StyleConstant.BORDER_RIGHT_STYLE_PROP,
					convertBorderStyle( style.getBorderRightStyle( ) ) );

			entry.setProperty( StyleConstant.BORDER_RIGHT_WIDTH_PROP,
					convertBorderWeight( style.getBorderRightWidth( ) ) );
		}
		
		entry.setProperty( StyleConstant.COLOR_PROP, convertColor( style
				.getColor( ) ) );

		entry.setProperty( StyleConstant.FONT_FAMILY_PROP, ExcelUtil
				.getValue( style.getFontFamily( ) ) );

		entry.setProperty( StyleConstant.FONT_SIZE_PROP, convertFontSize( style
				.getFontSize( ) ) );

		entry
				.setProperty( StyleConstant.FONT_STYLE_PROP, ExcelUtil
						.expression( style.getFontStyle( ), "italic",
								new String[]{"0", "1"}, false ) );

		entry.setProperty( StyleConstant.FONT_WEIGHT_PROP, ExcelUtil
				.expression( style.getFontStyle( ), "bold", new String[]{"0",
						"1"}, false ) );

		entry.setProperty( StyleConstant.TEXT_LINE_THROUGH_PROP, ExcelUtil
				.expression( style.getFontStyle( ), "line-through",
						new String[]{"0", "1"}, false ) );

		entry.setProperty( StyleConstant.TEXT_UNDERLINE_PROP, ExcelUtil
				.expression( style.getFontStyle( ), "underline", new String[]{
						"0", "1"}, false ) );

		entry.setProperty( StyleConstant.H_ALIGN_PROP, convertHAlign( style
				.getTextAlign( ) ) );

		entry.setProperty( StyleConstant.V_ALIGN_PROP, convertVAlign( style
				.getVerticalAlign( ) ) );

		return entry;
	}

	public static StyleEntry createEmptyStyleEntry( )
	{
		StyleEntry entry = new StyleEntry( );

		for ( int i = 0; i < StyleEntry.COUNT; i++ )
		{
			entry.setProperty( i, StyleEntry.NULL );
		}

		return entry;
	}

	public static String convertColor( String value )
	{
		if ( value == null || "transparent".equalsIgnoreCase( value ) )
		{
			return StyleConstant.NULL;
		}

		value = ExcelUtil.getValue( value );

		Matcher m = colorp.matcher( value );

		if ( m.matches( ) )
		{
			StringBuffer buffer = new StringBuffer( );
			buffer.append( "#" );

			for ( int i = 2; i <= 4; i++ )
			{
				String hex = Integer.toHexString( Integer.parseInt( ExcelUtil
						.getValue( m.group( i ) ).trim( ) ) );

				if ( hex.length( ) < 2 )
				{
					buffer.append( 0 );
				}

				buffer.append( hex );
			}

			return buffer.toString( );
		}

		return StyleConstant.NULL;
	}

	public static String convertFontSize( String size )
	{
		String fsize = StyleConstant.NULL;

		try
		{
			fsize = Float.toString( Math
					.round( Float.parseFloat( size ) / 1000 ) );
		}
		catch ( NumberFormatException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}

		return fsize;
	}

	public static String convertBorderWeight( String weight )
	{
		String w = StyleConstant.NULL;

		if ( weight != null && !"0".equalsIgnoreCase( weight ) )

		{
			weight = ExcelUtil.getValue( weight );

			if ( "thin".equalsIgnoreCase( weight ) )
			{
				w = "1";
			}
			else if ( "medium".equalsIgnoreCase( weight ) )
			{
				w = "2";
			}
			else
			{
				w = "3";
			}
		}

		return w;
	}

	public static String convertBorderStyle( String style )
	{
		String bs = ExcelUtil.getValue( style );

		if ( !StyleEntry.isNull( bs ) )
		{
			if ( "dotted".equalsIgnoreCase( bs ) )
			{
				bs = "Dot";
			}
			else if ( "dashed".equalsIgnoreCase( bs ) )
			{
				bs = "DashDot";
			}
			else if ( "double".equalsIgnoreCase( bs ) )
			{
				bs = "Double";
			}
			else
			{
				bs = "Continuous";
			}
		}

		return bs;
	}

	public static String convertHAlign( String align )
	{
		String ha = "Left";
		align = ExcelUtil.getValue( align );

		if ( "right".equalsIgnoreCase( align ) )
		{
			ha = "Right";
		}
		else if ( "center".equalsIgnoreCase( align ) )
		{
			ha = "Center";
		}

		return ha;
	}

	public static String convertVAlign( String align )
	{
		String va = "Top";
		align = ExcelUtil.getValue( align );

		if ( "bottom".equalsIgnoreCase( align ) )
		{
			va = "Bottom";
		}
		else if ( "middle".equalsIgnoreCase( align ) )
		{
			va = "Center";
		}

		return va;
	}

	public static boolean isHeritable( int id )
	{
		for ( int i = 0; i < UNHERITABLE.length; i++ )
		{
			if ( id == UNHERITABLE[i] )
			{
				return false;
			}
		}

		return true;
	}

	public static void mergeInheritableProp( StyleEntry cEntry, StyleEntry entry )
	{
		for ( int i = 0; i < StyleConstant.COUNT; i++ )
		{
			if ( StyleBuilder.isHeritable( i )
					&& StyleEntry.isNull( entry.getProperty( i )) )
			{
				entry.setProperty( i, cEntry.getProperty( i ) );
			}
		}
	}

	public static void applyRightBorder( StyleEntry cEntry, StyleEntry entry )
	{
		overwriteProp( cEntry, entry, StyleConstant.BORDER_RIGHT_COLOR_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_RIGHT_STYLE_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_RIGHT_WIDTH_PROP );
	}

	public static void applyLeftBorder( StyleEntry cEntry, StyleEntry entry )
	{
		overwriteProp( cEntry, entry, StyleConstant.BORDER_LEFT_COLOR_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_LEFT_STYLE_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_LEFT_WIDTH_PROP );
	}

	public static void applyTopBorder( StyleEntry cEntry, StyleEntry entry )
	{
		overwriteProp( cEntry, entry, StyleConstant.BORDER_TOP_COLOR_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_TOP_STYLE_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_TOP_WIDTH_PROP );
	}

	public static void applyBottomBorder( StyleEntry cEntry, StyleEntry entry )
	{
		overwriteProp( cEntry, entry, StyleConstant.BORDER_BOTTOM_COLOR_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_BOTTOM_STYLE_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_BOTTOM_WIDTH_PROP );
	}

	public static void overwriteProp( StyleEntry cEntry, StyleEntry entry,
			int id )
	{
		if ( !( StyleEntry.isNull( cEntry.getProperty( id ) ) ))
		{
			entry.setProperty( id, cEntry.getProperty( id ) );
		}
	}

	public static void main( String[] args )
	{
		Matcher m = colorp.matcher( "rgb(0,15, 0)" );

		if ( m.matches( ) )
		{
			System.out.println( m.group( ) );
		}
	}
}
