package org.eclipse.birt.chart.internal.model;

import java.util.ArrayList;
import java.util.List;

public class ContinuedFraction
{
	private double decimal = 0;
	private List integerList = new ArrayList( );

	public ContinuedFraction( double decimal )
	{
		this.decimal = decimal;
		computeList();
	}
	
	public Fraction getExactFraction(  )
	{
		int lastIndex = integerList.size() - 1;
		return getFraction( lastIndex, new Fraction( ((Long)integerList.get(lastIndex)).intValue(), 1 ));
	}
	public Fraction getFractionWithMaxDigits( int maxDigitsForDenominator )
	{
		int lastIndex = integerList.size();
		Fraction previousFraction = null;
		for ( int i = 0; i < lastIndex; i++ )
		{
			Fraction fraction = getFraction( i, new Fraction( ((Long)integerList.get(i)).intValue(), 1 ));
			if ( fraction.getDenominatorDigits() > maxDigitsForDenominator )
				return previousFraction;
			previousFraction = fraction;
		}
		return previousFraction;
	}
	private Fraction getFraction( int index, Fraction fraction )
	{
		if ( index > 0 )
			return getFraction( index - 1, (fraction.invert()).add( (Long)integerList.get( index - 1 ) ) );
		else
			return fraction;
		
	}
	
	private void computeList( )
	{
		
		int decimalDigits = 0 ;
		double decimalTemp = decimal;
		while ( Math.abs( Math.ceil( decimalTemp ) - decimalTemp ) > Math.pow( 10, decimalDigits - 8))
		{
			decimalTemp *= 10.0;
			decimalDigits ++;
		}
		long dividend = (long) Math.pow( 10, decimalDigits ) ;
		long start = (long)decimalTemp ;
		
		
		long quotient = 0;
		long oldDividend = 0;
		do
		{
			quotient = start / dividend;
			integerList.add( new Long( quotient ) );
			oldDividend = dividend;
			dividend = start % dividend;
			start = oldDividend;
			
		}
		while (dividend != 0);
	}
}
