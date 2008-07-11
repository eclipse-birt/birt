
package org.eclipse.birt.report.engine.emitter.excel;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.engine.emitter.excel.GroupInfo.Position;
import org.eclipse.birt.report.engine.ir.DimensionType;

import com.ibm.icu.text.SimpleDateFormat;
public class ExcelUtil
{
	protected static Logger log = Logger.getLogger( ExcelUtil.class.getName( ) );
	protected static BigDecimal MAX_DOUBLE=new BigDecimal(Double.MAX_VALUE);
	protected static BigDecimal MIN_DOUBLE=MAX_DOUBLE.negate( ).subtract( BigDecimal.ONE );
	protected static BigDecimal MAX_POSITIVE_DECIMAL_NUMBER = new BigDecimal(10e15).subtract( new BigDecimal("0.0000000000000001"));
	protected static BigDecimal MIN_POSITIVE_DECIMAL_NUMBER = new BigDecimal("0.000000000000001");
	protected static BigDecimal MIN_NEGATIVE_DECIMAL_NUMBER = new BigDecimal(-10e14).add( new BigDecimal("0.000000000000001") );
	protected static BigDecimal MAX_NEGATIVE_DECIMAL_NUMBER = MIN_POSITIVE_DECIMAL_NUMBER.negate( );
	
	private static String validStr = "#.0<>()%_";
	private static String specialStr="mMdDyYhHsSeEbBgGnN/*\"@";
	
	public static String ridQuote( String val )
	{
		if ( val.charAt( 0 ) == '"' && val.charAt( val.length( ) - 1 ) == '"' )
		{
			return val.substring( 1, val.length( ) - 1 );
		}
		return val;
	}
	
	
	private static String invalidBookmarkChars = 
		"`~!@#$%^&*()-=+\\|[]{};:'\",./?>< \t\n\r！￥（）：；，";
	
	// This check can not cover all cases, cause we do not know exactly the
	// excel range name restraint.
	public static boolean isValidBookmarkName( String name )
	{
		if( name.equalsIgnoreCase( "r" ) )
		{
			return false;
		}
		if( name.equalsIgnoreCase( "c" ) )
		{
			return false;
		}
		for ( int i = 0; i < name.length( ); i++ )
		{
			if( invalidBookmarkChars.indexOf( name.charAt( i ) ) != -1 )
			{	
				return false;
			}
		}
		
		//The bookmark name can not start with a digit.
		if (name.matches( "[0-9].*" ))
		{
			return false;	
		}
		//columnID<=IV, rowID<=65536 can not be used as bookmark.
		if( name.matches("([A-Za-z]|[A-Ha-h][A-Za-z]|[Ii][A-Va-v])[0-9]{1,5}" ))
		{
			String[] strs = name.split( "[A-Za-z]" );
			if (strs.length>0)
			{
				int rowId = 0;
				try
				{
					rowId = Integer.parseInt( strs[strs.length-1]);	
				}
				catch( NumberFormatException e )
				{
					return true;
				}
				if (rowId<=65536)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			return true;
		}
		return true;
	}
	
    public static String formatDate( Object data )
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss" );
		Date date = getDate( data );
		if(date == null) {
			return null;
		}
		return  dateFormat.format( date );        
	}

	public static Date getDate( Object data )
	{
		Date date = null;
		if(data instanceof com.ibm.icu.util.Calendar) {
			date = ((com.ibm.icu.util.Calendar) data).getTime( );
		}
		else if(data instanceof Date) {
			date = (Date) data;
		}
		else {
			date = null;
		}
		return date;
	}
    
	public static String formatNumberAsDecimal( Object data )
	{
		Number number=(Number)data;
		DecimalFormat numberFormat = new DecimalFormat( "0.##############" );
		numberFormat.setMaximumFractionDigits( 15 );
		return numberFormat.format( number );
	}

	public static String formatNumberAsScienceNotation( Number data )
	{
		assert data instanceof BigDecimal;
		BigDecimal bigDecimal = (BigDecimal) data;
		int scale = 0;
		if (bigDecimal.compareTo( BigDecimal.ZERO ) == 0 )
		{
			return "0";
		}
		String prefix = "";
		if ( bigDecimal.compareTo( BigDecimal.ZERO ) == -1 )
		{
			prefix = "-";
			bigDecimal = bigDecimal.negate( );
		}
		if ( bigDecimal.compareTo( BigDecimal.ONE ) == -1 )
		{
			while ( bigDecimal.compareTo( BigDecimal.ONE ) == -1 )
			{
				bigDecimal = bigDecimal.movePointRight( 1 );
				scale = scale - 1;
			}
		}
		else
		{
			while ( bigDecimal.compareTo( BigDecimal.TEN ) == 1 )
			{
				bigDecimal = bigDecimal.movePointLeft( 1 );
				scale = scale + 1;
			}
		}
		DecimalFormat decimalFormat = new DecimalFormat("0.##############");
		String number = decimalFormat.format( bigDecimal );
		String sign = scale >= 0 ? "+" : "";
		return prefix + number + "E" + sign + scale;
	}
    
    public static String getType(Object val)
    {
    	if ( val instanceof Number )
    	{
   			return Data.NUMBER;
    	}
    	else if(val instanceof Date)
    	{
    	   return Data.DATE;	
    	}
    	else if (val instanceof Calendar)
    	{
    	   return Data.CALENDAR;	
    	}
//    	else if(val instanceof CDateTime)
//    	{
//    	   return Data.CDATETIME;	
//    	}
    	else 
    	{
    	   return Data.STRING;	
    	}
    }
      
    private static String replaceDateFormat( String pattern )
	{
		if ( pattern == null )
		{
			String rg = "";

			return rg;
		}

		StringBuffer toAppendTo = new StringBuffer( );
		boolean inQuote = false;
		char prevCh = 0;
		int count = 0;

		for ( int i = 0; i < pattern.length( ); ++i )
		{
			char ch = pattern.charAt( i );

			if ( ch != prevCh && count > 0 )
			{
				toAppendTo.append( subReplaceDateFormat( prevCh, count ) );
				count = 0;
			}

			if ( ch == '/' )
			{
				toAppendTo.append( '\\' );
				toAppendTo.append( ch );
			}
			else if ( ch == '\'' )
			{
				if ( ( i + 1 ) < pattern.length( )
						&& pattern.charAt( i + 1 ) == '\'' )
				{
					toAppendTo.append( "\"" );
					++i;
				}
				else
				{
					inQuote = !inQuote;
				}
			}
			else if ( !inQuote )
			{
				prevCh = ch;
				++count;
			}
			else
			{

				toAppendTo.append( ch );
			}
		}

		if ( count > 0 )
		{
			toAppendTo.append( subReplaceDateFormat( prevCh, count ) );
		}

		return toAppendTo.toString( );
	}

	/**
	 * only used in the method replaceDataFormat().
	 */
	private static String subReplaceDateFormat( char ch, int count )
	{
		String current = "";
		int patternCharIndex = -1;
		String datePatternChars = "GyMdkHmsSEDFwWahKz";
		if ( ( patternCharIndex = datePatternChars.indexOf( ch ) ) == -1 )
		{
			for ( int i = 0; i < count; i++ )
			{
				current += "" + ch;
			}

			return current;
		}

		switch ( patternCharIndex )
		{
			case 0 : // 'G' - ERA
				current = "";
				break;
			case 1 : // 'y' - YEAR
				for ( int i = 0; i < count; i++ )
				{
					current += "" + ch;
				}

				break;
			case 2 : // 'M' - MONTH
				for ( int i = 0; i < count; i++ )
				{
					current += "" + ch;
				}

				break;
			case 3 : // 'd' - Date
				for ( int i = 0; i < count; i++ )
				{
					current += "" + ch;
				}

				break;
			case 4 : // 'k' - HOUR_OF_DAY: 1-based. eg, 23:59 + 1 hour =>>
						// 24:59
				current = "h";
				break;
			case 5 : // case 5: // 'H'-HOUR_OF_DAY:0-based.eg, 23:59+1
						// hour=>>00:59
				for ( int i = 0; i < count; i++ )
				{
					current += "" + ch;
				}

				break;
			case 6 : // case 6: // 'm' - MINUTE
				for ( int i = 0; i < count; i++ )
				{
					current += "" + ch;
				}

				break;
			case 7 : // case 7: // 's' - SECOND
				for ( int i = 0; i < count; i++ )
				{
					current += "" + ch;
				}

				break;
			case 8 : // case 8: // 'S' - MILLISECOND
				for ( int i = 0; i < count; i++ )
				{
					current += "" + ch;
				}

				break;
			case 9 : // 'E' - DAY_OF_WEEK
				for ( int i = 0; i < count; i++ )
				{
					current += "a";
				}

				break;
			case 14 : // 'a' - AM_PM
				current = "AM/PM";
				break;
			case 15 : // 'h' - HOUR:1-based. eg, 11PM + 1 hour =>> 12 AM
				for ( int i = 0; i < count; i++ )
				{
					current += "" + ch;
				}

				break;
			case 17 : // 'z' - ZONE_OFFSET
				current = "";
				break;
			default :
				// case 10: // 'D' - DAY_OF_YEAR
				// case 11: // 'F' - DAY_OF_WEEK_IN_MONTH
				// case 12: // 'w' - WEEK_OF_YEAR
				// case 13: // 'W' - WEEK_OF_MONTH
				// case 16: // 'K' - HOUR: 0-based. eg, 11PM + 1 hour =>> 0 AM
				current = "";
				break;
		}

		return current;
	}
       
    public static String getPattern(Object data, String val)
    {
    	if(val != null && data instanceof Date) {
    	   return replaceDateFormat(val);   
    	}
    	else if(val == null && data instanceof Time) {
    		return "Long Time";
    	}
    	else if(val == null && data instanceof java.sql.Date) 
    	{
    		// According to java SDK 1.4.2-16, sql.Date doesn't have
    		// a time component.
    		return "mmm d, yyyy";// hh:mm AM/PM";
    	}
    	else if(val == null && data instanceof java.util.Date) 
    	{
    		return "mmm d, yyyy h:mm AM/PM";
    	}
    	else if(val != null && data instanceof Number)
    	{
    	   
    	   if(val.indexOf( "E" ) >= 0){
    	      return "Scientific";
    	   }
    	   return new NumberFormatter(val).getPattern( );	
    	}
    	else if(val != null && data instanceof String)
    	{
    		return new StringFormatter(val).getPattern( );
    	}
    	
    	return null;
    }
    
    public static String replaceAll(String str, String old, String news) {
        if(str == null) {
           return str;
        }

        int begin = 0;
        int idx = 0;
        int len = old.length();
        StringBuffer buf = new StringBuffer();

        while((idx = str.indexOf(old, begin)) >= 0) {
           buf.append(str.substring(begin, idx));
           buf.append(news);
           begin = idx + len;
        }

        return new String(buf.append(str.substring(begin)));
     }
	// TODO
	public static String getValue( String val )
	{
		if ( val == null )
		{
			return StyleConstant.NULL;
		}
		if ( val.charAt( 0 ) == '"' && val.charAt( val.length( ) - 1 ) == '"' )
		{
			return val.substring( 1, val.length( ) - 1 );
		}

		return val;
	}

	public static int convertToPt( String size )
	{
		try
		{
			int s = Integer.valueOf( size.substring( 0, size.length( ) - 2 ) )
					.intValue( );
			if ( size.endsWith( "in" ) )
			{
				return s * 72;
			}
			else if ( size.endsWith( "cm" ) )
			{
				return (int) ( s / 2.54 * 72 );
			}
			else if ( size.endsWith( "mm" ) )
			{
				return (int) ( s * 10 / 2.54 * 72 );
			}
			else if ( size.endsWith( "pc" ) )
			{
				return s;
			}
			else
			{
				return s;
			}
		}
		catch ( Exception e )
		{
			log.log(Level.WARNING, "unknown size: " + size);
//			e.printStackTrace( );
			return 0;
		}
	}


	
	// the parse method can just see if the start of the String is a number
	// like "123 bbs"
	// it will parse successful and returns the value of 123 in number
	public static boolean isBigNumber( Object number )
	{
		try
		{
			BigDecimal num = getBigDecimal( number );
			if( num.compareTo( MAX_DOUBLE )==1||num.compareTo(MIN_DOUBLE)==-1 )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch ( Exception e )
		{
			return false;
		}
	}

	private static BigDecimal getBigDecimal( Object number )
	{
		BigDecimal num = null;
		if ( number instanceof BigDecimal )
		{
			num = (BigDecimal) number;
		}
		else
		{
			num = new BigDecimal( number.toString( ) );
		}
		return num;
	}
	
	public static boolean displayedAsScientific( Object number )
	{
		BigDecimal num = getBigDecimal( number );
		if ( num.compareTo( MAX_POSITIVE_DECIMAL_NUMBER ) <= 0
				&& num.compareTo( MIN_POSITIVE_DECIMAL_NUMBER ) >= 0 )
		{
			return false;
		}
		if ( num.compareTo( MAX_NEGATIVE_DECIMAL_NUMBER ) <= 0
				&& num.compareTo( MIN_NEGATIVE_DECIMAL_NUMBER ) >= 0 )
		{
			return false;
		}
		return true;
	}
	
	public static boolean isInfinity(Object number)
	{
		try
		{
			return Double.isInfinite( ( Double)number);
		}
		catch ( Exception e )
		{
			return false;
		}
	}
	public static String getColumnOfExp( String exp )
	{
		return exp.substring( exp.indexOf( "dataSetRow[" ), exp
				.lastIndexOf( "]" ) + 1 );
	}
	private static final int max_formula_length = 512;

	public static String createFormula( String txt, String exp, List positions )
	{
		exp = getFormulaName( exp );
		StringBuffer sb = new StringBuffer( exp + "(" );
		for ( int i = 0; i < positions.size( ); i++ )
		{
			Position p = (Position) positions.get( i );
			sb.append( "R" + p.row + "C" + p.column + "," );
		}
		sb.setCharAt( sb.length( ) - 1, ')' );

		if ( sb.length( ) > max_formula_length || positions.size( ) == 0 )
		{
			return txt;
		}
		return sb.toString( );
	}

	private static String getFormulaName( String expression )
	{
		if ( expression.startsWith( "Total.sum" ) )
		{
			return "=SUM";
		}
		else if ( expression.startsWith( "Total.ave" ) )
		{
			return "=AVERAGE";
		}
		else if ( expression.startsWith( "Total.max" ) )
		{
			return "=MAX";
		}
		else if ( expression.startsWith( "Total.min" ) )
		{
			return "=MIN";
		}
		else if ( expression.startsWith( "Total.count" ) )
		{
			return "=COUNT";
		}
		throw new RuntimeException( "Cannot parse the expression" + expression );
	}

	private static final String reg1 = "Total." + "(count|ave|sum|max|min)"
			+ "\\(", reg2 = "\\)", reg3 = "\\[", reg4 = "\\]";

	public static boolean isValidExp( String exp, String[] columnNames )
	{
		StringBuffer sb = new StringBuffer( );
		for ( int i = 0; i < columnNames.length; i++ )
		{
			sb.append( columnNames[i] + "|" );
		}
		String columnRegExp = "(" + sb.substring( 0, sb.length( ) - 1 ) + ")";
		columnRegExp = columnRegExp.replaceAll( reg3, "Z" );
		columnRegExp = columnRegExp.replaceAll( reg4, "Z" );

		String aggregateRegExp = reg1 + columnRegExp + reg2;

		exp = exp.replaceAll( reg3, "Z" );
		exp = exp.replaceAll( reg4, "Z" );

		Pattern p = Pattern.compile( aggregateRegExp );
		Matcher m = p.matcher( exp );
		boolean agg = m.matches( );

		p = Pattern.compile( columnRegExp );
		m = p.matcher( exp );
		return agg || m.matches( );
	}

	public static String expression( String val, String target, String[] res,
			boolean casesenstive )
	{
		boolean flag = casesenstive ? target.equals( val ) : target
				.equalsIgnoreCase( val );
		return flag ? res[1] : res[0];
	}

	public static int covertDimensionType( DimensionType value, int parent )
	{
		if ( DimensionType.UNITS_PERCENTAGE.equals( value.getUnits( ) ) )
		{
			return (int) (value.getMeasure( ) / 100 * parent);
		}
		else
		{
			return (int) (value.convertTo( DimensionType.UNITS_PT ));
		}
	}
	
	public static String parse(String dateTime,Locale locale)
	{
		if ( dateTime == null )
		{
			return "";
		}
		if ( dateTime.indexOf( "Date" ) != -1
				|| dateTime.indexOf( "Time" ) != -1 )
		{
			DateFormatter dateFormatter = new DateFormatter( dateTime, locale );
			dateTime = dateFormatter.getLocalizedFormatCode( );
		}
		StringBuffer buffer = new StringBuffer( );
		boolean inQuto = false;
		for(int count = 0 ; count < dateTime.length(); count ++)
		{
			char tempChar = dateTime.charAt(count);
			if(inQuto)
			{
				if(tempChar == '\'' && nextIsQuto(dateTime , count))
				{
					buffer.append( tempChar );
					count ++;
				}
				else
				{
					if(tempChar == '\'')
					{
						inQuto = false;
					}
					else
					{
						if(specialStr.indexOf( tempChar )!=-1)
						{
							buffer.append( "\\"+tempChar );
						}
						else{
							buffer.append( tempChar );
						}
					}
				}
			}
			else
			{
				if(tempChar == '\'')
				{
					if(nextIsQuto(dateTime , count))
					{
						buffer.append( tempChar );
						count ++;
					}
					else
					{
						inQuto = true;
					}
				}
				else
				{
					if(tempChar == 'a')
					{
						buffer.append( "AM/PM" );
						continue;
					}
					if("zZFWwGE".indexOf( tempChar ) != -1)
					{
						continue;
					}
					if ( "kK".indexOf( tempChar ) != -1 )
					{
						buffer.append( "h" );
						continue;
					}
					buffer.append( tempChar );
				}
			}
		}
		return buffer.toString( );
	}

	public static String formatNumberPattern(String givenValue)
	{
		String returnStr ="";
		if ( givenValue.length( ) == 1 )
		{
			char ch = givenValue.charAt( 0 );
			if ( ch == 'G' || ch == 'g' || ch == 'd' || ch == 'D' )
			{
				returnStr = givenValue + "###";
			}
			if ( ch == 'C' || ch == 'c' )
			{
				return "###,##0.00";
			}
			if ( ch == 'f' || ch == 'F' )
			{
				return "#0.00";
			}
			if ( ch == 'N' || ch == 'n' )
			{
				return "###,##0.00";
			}
			if ( ch == 'p' || ch == 'P' )
			{
				return "###,##0.00 %";
			}
			if ( ch == 'e' || ch == 'E' )
			{
				return "0.000000E00";
			}
			if ( ch == 'x' || ch == 'X' )
			{
				returnStr = "####";
			}
			returnStr = returnStr + givenValue + "###";
		}
		else
		{
			if ( givenValue.equals( "Fixed" ) )
				return "Fixed";
			if ( givenValue.equals( "Percent" ) )
				return "Percent";
			if ( givenValue.equals( "Scientific" ) )
				return "Scientific";
			if ( givenValue.equals( "Standard" ) )
				return "Standard";
			if(givenValue.equals( "General Number" ))
				return "General";
			
			if(validType(givenValue)){
				return givenValue + "###";
			}
			int count = givenValue.length( );
			boolean flag=false;
			for ( int num = 0; num < count ; num++)
			{
				char temp=givenValue.charAt( num );
				if(temp=='\'')
				{
					if(flag)
					{
						flag=false;
					}
					else
					{
						char nextChar=givenValue.charAt(num+1);
						if(nextChar=='\'')
						{
							returnStr=returnStr+'\'';
							num++;
							flag=false;
						}
						else
						{
							flag=true;
						}
					}
				}
				
				else
				{	
					if(flag)
					{
						returnStr=returnStr+"\\"+temp;
					}
					else
					{
						if(specialStr.indexOf( temp )!=-1)
						{
							returnStr=returnStr+"\\"+temp;
						}
						else
						{
							returnStr=returnStr+temp;
						}
					}
				}
			}
		returnStr = returnStr + "###";
		}
		return returnStr;
	}
	
	protected static boolean validType( String str )
	{
		for ( int count = 0; count < str.length( ); count++ )
		{
			char ch = str.charAt( count );
			if ( validStr.indexOf( ch ) == -1 )
			{
				return false;
			}
		}
		return true;
	}
	
	private static boolean nextIsQuto(String forPar , int index)
	{
		if( forPar.length() - 1 == index )
		{
			return false;
		}
		if(forPar.charAt(index + 1) == '\'')
		{
			return true;
		}
		return false;
	}

	
}
