package org.eclipse.birt.data.engine.script;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * The implementation of this class is used to evaluate TopN/BottomN expressions 
 * @author lzhu
 *
 */
abstract class NEvaluator extends FilterPassController
{
	public static final String BOTTOM_INSTANCE = "BOTTOM";
	public static final String TOP_INSTANCE = "TOP";
	private static String currentInstance = null;
	private static Object[] valueList; 
	private static int[] rowIdList;
	private static int firstPassRowNumberCounter = 0;
	private static int secondPassRowNumberCounter = 0;
	private static int qualifiedRowCounter = 0;
	
	//The "N" of topN/bottomN.
	private static int N = -1;
	private static NEvaluator instance = null;
	
	/**
	 * Return the instance of NEvaluagor according to the given instanceName.
	 * @param instanceName
	 * @return
	 */
	public static NEvaluator getInstance( String instanceName )
	{
		if ( instance == null
				|| ( currentInstance == null || !currentInstance.equalsIgnoreCase( instanceName ) ) )
		{
			if ( TOP_INSTANCE.equalsIgnoreCase( instanceName ) )
				instance = new TopNEvaluator( );
			else if ( BOTTOM_INSTANCE.equalsIgnoreCase( instanceName ) )
				instance = new BottomNEvaluator( );
			else
				throw new IllegalArgumentException( );
		}
		return instance;
	}
	
	/**
	 * Evaluate the given value
	 * @param value
	 * @param n
	 * @return
	 * @throws DataException
	 */
	public boolean evaluate(Object value, Object n) throws DataException
	{
		if( N == -1)
		{
			try{
				N = Double.valueOf( n.toString() ).intValue();
			}catch (NumberFormatException e)
			{
				
			}
			//If the exception is thrown in abrove code, then the value of N should
			//remains "-1"
			if( N < 0 )
				throw new DataException(ResourceConstants.INVALID_TOP_BOTTOM_N_ARGUMENT);
		}
		if ( getPassLevel( ) == FIRST_PASS )
		{
			return doFirstPass( value );
		}
		else if ( getPassLevel( ) == SECOND_PASS )
		{
			return doSecondPass( );
		}
		return false;
	}

	/**
	 * Do the first pass. In the first pass we maintain a value list and a row id list that will
	 * host all top/bottom N values/rowIds so that in pass 2 we can use them to filter rows out.
	 * @param value
	 * @return
	 * @throws DataException
	 */
	private boolean doFirstPass( Object value ) throws DataException
	{
		firstPassRowNumberCounter++;
		if ( valueList == null )
		{
			valueList = new Object[N];
			rowIdList = new int[N];
		}
		populateValueListAndRowIdList( value, N );
		return false;
	}

	/**
	 * @param value
	 * @param N
	 * @throws DataException
	 */
	private void populateValueListAndRowIdList( Object value, int N ) throws DataException
	{
		for( int i = 0; i < N; i++ )
		{
			if( valueList[i] == null )
			{
				valueList[i] = value;
				rowIdList[i] = firstPassRowNumberCounter;
				break;
			}else
			{
				Object result = this.doCompare( value, valueList[i] );
				try
				{
					// filter in
					if ( DataTypeUtil.toBoolean( result ).booleanValue( ) == true )
					{
						for( int j = N - 1; j > i; j--)
						{
							valueList[j] = valueList[j-1];
							rowIdList[j] = rowIdList[j-1];
						}
						valueList[i] = value;
						rowIdList[i] = firstPassRowNumberCounter;
						break;
					}
				}
				catch ( BirtException e )
				{
			    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
					throw e1;
				}
			}
		}
	}
	
	/**
	 * Do the second pass
	 * @param N
	 * @return
	 */
	private boolean doSecondPass( )
	{
		secondPassRowNumberCounter++;
		if ( qualifiedRowCounter < N )
		{
			for ( int i = 0; i < N; i++ )
			{
				if ( rowIdList[i] == secondPassRowNumberCounter )
				{
					qualifiedRowCounter++;
					reset( );
					return true;

				}
			}
			return false;
		}
		else
		{
			reset( );
			return false;
		}
	}

	/**
	 * Reset all the member data to their default value.
	 */
	private void reset( )
	{
		if ( firstPassRowNumberCounter  == secondPassRowNumberCounter )
		{
				firstPassRowNumberCounter = 0;
				secondPassRowNumberCounter = 0;
				qualifiedRowCounter = 0;
				rowIdList = null;
				valueList = null;
				N = -1;
		}
	}

	protected abstract Object doCompare( Object value1, Object value2 ) throws DataException;
}

/**
 * The class that provides "Top N" calculation service
 *
 */
class TopNEvaluator extends NEvaluator
{
	protected Object doCompare( Object value1, Object value2 ) throws DataException
	{
		return ScriptEvalUtil.evalConditionalExpr( value1, IConditionalExpression.OP_GT, value2, null);
	}
}

/**
 * The class that provides "Bottom N" calculation service
 *
 */
class BottomNEvaluator extends NEvaluator
{
	protected Object doCompare( Object value1, Object value2 ) throws DataException
	{
		return ScriptEvalUtil.evalConditionalExpr( value1, IConditionalExpression.OP_LT, value2, null);
	}
}
