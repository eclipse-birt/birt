package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

public class TimeMember
{
	public static final String TIME_LEVEL_TYPE_MONTH = "month"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_QUARTER = "quarter"; //$NON-NLS-1$

	public static final String TIME_LEVEL_TYPE_DAY_OF_YEAR = "day-of-year"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_DAY_OF_MONTH = "day-of-month"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_DAY_OF_WEEK = "day-of-week"; //$NON-NLS-1$

	
	public static final String TIME_LEVEL_TYPE_HOUR = "hour"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_MINUTE = "minute"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_SECOND = "second"; //$NON-NLS-1$
	
	//not support
	public static final String TIME_LEVEL_TYPE_WEEK_OF_MONTH = "week-of-month"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_WEEK_OF_YEAR = "week-of-year"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_YEAR = "year"; //$NON-NLS-1$
	
	private int[] memberValue;
	private String[] levelType;
	
	public TimeMember( int[] memberValue, String[] levelType )
	{
		this.memberValue = memberValue;
		this.levelType = levelType;
	}

	public int[] getMemberValue()
	{
		return memberValue;
	}

	public String[] getLevelType()
	{
		return levelType;
	}
	
	
}
