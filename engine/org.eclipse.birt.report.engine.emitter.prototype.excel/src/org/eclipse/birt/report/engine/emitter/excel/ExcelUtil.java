
package org.eclipse.birt.report.engine.emitter.excel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.engine.emitter.excel.GroupInfo.Position;
import org.eclipse.birt.report.engine.ir.DimensionType;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.SimpleDateFormat;
public class ExcelUtil
{
	   private static final BigDecimal zeroThroughTen[] = {
			new BigDecimal( BigInteger.ZERO, 0 ),
			new BigDecimal( BigInteger.ONE, 1 ),
			new BigDecimal( BigInteger.valueOf( 2 ), 2 ),
			new BigDecimal( BigInteger.valueOf( 3 ), 3 ),
			new BigDecimal( BigInteger.valueOf( 4 ), 4 ),
			new BigDecimal( BigInteger.valueOf( 5 ), 5 ),
			new BigDecimal( BigInteger.valueOf( 6 ), 6 ),
			new BigDecimal( BigInteger.valueOf( 7 ), 7 ),
			new BigDecimal( BigInteger.valueOf( 8 ), 8 ),
			new BigDecimal( BigInteger.valueOf( 9 ), 9 ),
			new BigDecimal( BigInteger.valueOf( 10 ), 10 ),};
	public static final BigDecimal ZERO = zeroThroughTen[0];
	public static final BigDecimal ONE = zeroThroughTen[1];
	public static final BigDecimal TEN = zeroThroughTen[10];
	protected static BigDecimal MAX_DOUBLE = new BigDecimal( Double.MAX_VALUE );
	protected static BigDecimal MIN_DOUBLE = MAX_DOUBLE.negate( )
			.subtract( ONE );
	protected static BigDecimal MAX_POSITIVE_DECIMAL_NUMBER = new BigDecimal(10e15).subtract( new BigDecimal("0.0000000000000001"));
	protected static BigDecimal MIN_POSITIVE_DECIMAL_NUMBER = new BigDecimal("0.000000000000001");
	protected static BigDecimal MIN_NEGATIVE_DECIMAL_NUMBER = new BigDecimal(-10e14).add( new BigDecimal("0.000000000000001") );
	protected static BigDecimal MAX_NEGATIVE_DECIMAL_NUMBER = MIN_POSITIVE_DECIMAL_NUMBER.negate( );
 
	private static String currencySymbol= "£¢€￥¥";
	
	public static String ridQuote( String val )
	{
		if ( val.charAt( 0 ) == '"' && val.charAt( val.length( ) - 1 ) == '"' )
		{
			return val.substring( 1, val.length( ) - 1 );
		}

		return val;
	}
    public static String formatDate( Object data )
	{
       
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss" );
		return  dateFormat.format( (Date) data );
        
	}
    
    public static String formatNumber( Object data )
	{
       
    	DecimalFormat numberFormat = new DecimalFormat("0.00E00");
		return  numberFormat.format( (Number) data );
        
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
	
	public static boolean isInfinity(Object number)
	{
		try
		{
			return Double.isInfinite(((Double)number).doubleValue( ));
			
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
	
	public static String formatNumberAsScienceNotation( Number data )
	{
		BigDecimal bigDecimal = (BigDecimal) data;
		int scale = 0;
		if (bigDecimal.compareTo( ZERO ) == 0 )
		{
			return "0";
		}
		String prefix = "";
		if ( bigDecimal.compareTo( ZERO ) == -1 )
		{
			prefix = "-";
			bigDecimal = bigDecimal.negate( );
		}
		if ( bigDecimal.compareTo( ONE ) == -1 )
		{
			while ( bigDecimal.compareTo( ONE ) == -1 )
			{
				bigDecimal = bigDecimal.movePointRight( 1 );
				scale = scale - 1;
			}
		}
		else
		{
			while ( bigDecimal.compareTo( TEN ) == 1 )
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
	
	public static String formatNumberAsDecimal( Object data )
	{
		Number number=(Number)data;
		DecimalFormat numberFormat = new DecimalFormat( "0.##############" );
		numberFormat.setMaximumFractionDigits( 15 );
		return numberFormat.format( number );
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
    	else if(val instanceof CDateTime)
    	{
    	   return Data.CDATETIME;	
    	}
    	else 
    	{
    	   return Data.STRING;	
    	}
    }
	
    public static String getPattern(Object data, String val)
    {
    	if(val != null && data instanceof Date) {
    	   if (val.indexOf( "kk:mm" ) >= 0){
    		   return "Short Time";	   
    	   }
    	   else if(val.startsWith( "ahh" ))
    	   {
    		   return "Long Time";   
    	   }
    	   else if(!val.startsWith( "ahh" ) && val.indexOf( "ahh" ) >= 0)
    	   {
    	      return "General Date";	   
    	   }
    	   return new DateFormatter(val).getPattern( );
    	}
    	else if(val == null && data instanceof Time) {
    		return "Long Time";
    	}
    	else if(val == null && data instanceof java.sql.Date) 
    	{
    		return "yyyy-M-d";
    	}
    	else if(val == null && data instanceof java.util.Date) 
    	{
    		return "yyyy-M-d HH:ss:mm AM/PM";
    	}
    	else if ( val != null && data instanceof Number )
		{
			if ( val.indexOf( "E" ) >= 0 )
			{
				return "Scientific";
			}
			return new NumberFormatter( val ).getPattern( );

		}
    	
    	else if(val != null && data instanceof String)
    	{
    		return new StringFormatter(val).getPattern( );
    	}
    	
    	return null;
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
			e.printStackTrace( );
			return 0;
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
	
	public static boolean isNumber( String val )
	{
		NumberFormat nf = NumberFormat.getInstance( );
		try
		{
			Number num = nf.parse( val );
			return true;
		}
		catch ( Exception e )
		{
			return false;
		}
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
	
	public static String formatNumberPattern(String givenValue)
	{
		String returnStr ="";
		if(givenValue == null )
		{
			return "";
		}
		int count = givenValue.length( );
		for ( int num = 0; num < count; num++ )
		{
			char temp = givenValue.charAt( num );
			if ( currencySymbol.indexOf( temp ) != -1 )
			{
				returnStr = returnStr + "\"" + temp + "\"";
			}
			else
			{
				returnStr = returnStr + temp;
			}
		}
		return returnStr;
	}
	
}
