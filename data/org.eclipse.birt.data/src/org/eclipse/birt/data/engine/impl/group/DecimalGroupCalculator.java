package org.eclipse.birt.data.engine.impl.group;

import java.math.BigDecimal;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;

public class DecimalGroupCalculator  extends GroupCalculator
{

	BigDecimal doubleStartValue;

	/**
	 * 
	 * @param intervalStart
	 * @param intervalRange
	 * @throws BirtException
	 */
	public DecimalGroupCalculator( Object intervalStart, double intervalRange )
			throws BirtException
	{
		super( intervalStart, intervalRange );
		intervalRange = (intervalRange == 0 ? 1 : intervalRange);
		this.intervalRange = intervalRange;
		if ( intervalStart == null )
			doubleStartValue = new BigDecimal( -1 );
		else
			doubleStartValue = DataTypeUtil.toBigDecimal( intervalStart );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.group.GroupCalculator#calculate(java.lang.Object)
	 */
	public Object calculate( Object value ) throws BirtException
	{
		if ( value == null )
		{
			return new BigDecimal( -1 );
		}

		BigDecimal dValue = DataTypeUtil.toBigDecimal( value );

		if ( dValue.compareTo( doubleStartValue ) < 0 )
		{
			return new Double( -1 );
		}
		else
		{
			dValue = dValue.subtract( doubleStartValue );
			dValue = dValue.divide(new BigDecimal(intervalRange), 0, BigDecimal.ROUND_FLOOR );
			return dValue;

		}
	}
}
