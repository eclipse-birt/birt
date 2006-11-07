/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.aggregation;

import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IAggregation;
import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 * test total aggregation function
 */
public class TotalTest extends TestCase
{
    private double[] weight = {1.2, 1.4, 2, 0.5, 1.4, 2.4, 1.5, 2.5, 3.7, 1.4, 1.03, 0.5, 0.2, 0.6, 1.3, 1.5, 12.4};
    private double[] doubleArray1 = {1, 3, 5, 4, 6, 8, 3, 4, 5, 7, 9, 10, 4, 6, 7};
    private boolean[] doubleArray1TopBottom = {false, false, false,false,false,true,false,false,false,true,true,true,false,false,true};
    private double[] doubleArray2 = {4, -43, 4, 23, -15, -6, 4, -6, 3, 63, 33, -6, -23, 34};
    private Double[] doubleArray3 = {Double.valueOf( "100" ),Double.valueOf( "20" ),null,Double.valueOf( "300" ),null,Double.valueOf( "40" ),Double.valueOf( "10" ), Double.valueOf( "10" )};
    private int[] doubleArray3RankDec = {2, 4, 7, 1, 7, 3,5,5 };
    private int[] doubleArray3RankAsc = {7, 5, 1, 8, 1, 6,3,3};
    private int[] doubleArray3PercentRank = {857,571,0,1000,0,714,285,285};
    private Object[] doubleArray3PercentSum = {new Integer(208),new Integer(41), null, new Integer(625),null,new Integer(83),new Integer(20),new Integer(20)};
    private String[] str1 = {"4", "-43", "4", "23", "-15", "-6", "4", "-6", "3", "63", "33", "-6", "-23", "34"};
    
    private Date[] dates = new Date[]
                            {
            new Date(1000000L),
            new Date(2000000L),
            new Date(3000000L),
            new Date(4000000L)
                            };
    
    private String[] str2 = new String[]
                                       {
            "test",
            "string",
            "array",
            "for",
            "aggregation"
                                       };
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testTotalCount() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("count");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_COUNT_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(0, ag.getParameterDefn().length);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Object[]{});
        }
        ac.finish();
        assertEquals(new Integer(15), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Object[]{});
        }
        ac.finish();
        assertEquals(new Integer(14), ac.getValue());
        
        ac.start();
        
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{});
        }
        ac.finish();
        assertEquals(new Integer(14), ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(new Integer(0), ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }
    }
    
    public void testTotalSum() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("sum");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_SUM_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(82.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(new Double(69.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals(new Double(69.0), ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(new Double(0D), ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }
    }

    public void testTotalRunningSum() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("RUNNINGSUM");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_RUNNINGSUM_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        double sum = 0D;
        ac.start( );
		for ( int i = 0; i < doubleArray1.length; i++ )
		{
			ac.onRow( new Double[]{
				new Double( doubleArray1[i] )
			} );
			sum += doubleArray1[i];
			assertEquals( new Double( sum ), ac.getValue( ) );
		}
        ac.finish();
        sum = 0D;
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
            sum += doubleArray2[i];
			assertEquals( new Double( sum ), ac.getValue( ) );
        }
        ac.finish();
        sum = 0D;
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
            sum += new Double(str1[i]).doubleValue();
			assertEquals( new Double( sum ), ac.getValue( ) );
        }
        ac.finish();
        sum = 0D;
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
    }
    
    public void testTotalAva() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("ave");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_AVE_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(5.466666666666667), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(new Double(4.928571428571429), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals(new Double(4.928571428571429), ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }
    }
    
    public void testTotalFirst() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("first");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_FIRST_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(1.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(new Double(4), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals("4", ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }
    }
    
    public void testTotalLast() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("last");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_LAST_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(7.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(new Double(34.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals("34", ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }
    }
    
    public void testTotalMax() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("max");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_MAX_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(10.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(new Double(63.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals("63", ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }
    }
    
    public void testTotalMin() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("min");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_MIN_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(1.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(new Double(-43), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals("-15", ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }
    }
    
    public void testTotalMedian() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("median");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_MEDIAN_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(5.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(new Double(3.5), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals(new Double(3.5), ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }

    }
    
    public void testTotalMode() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("mode");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_MODE_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(4.0), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }
        
        ac.start();
        ac.onRow( new Double[]{ new Double(4)} );
        ac.onRow( new Double[]{ new Double(4)} );
        ac.onRow( new Double[]{ new Double(3)} );
        ac.finish();
        assertEquals( ac.getValue(), new Double(4) );

    }
    
    public void testTotalStdDev() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("stddev");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_STDDEV_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(2.445598573141631), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(new Double(26.560422510872147), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals(new Double(26.560422510872147), ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }

    }
    
    
    public void testTotalVariance() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("variance");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_VARIANCE_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i])});
        }
        ac.finish();
        assertEquals(new Double(5.980952380952381), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i])});
        }
        ac.finish();
        assertEquals(new Double(705.4560439560439), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i]});
        }
        ac.finish();
        assertEquals(new Double(705.4560439560439), ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }

    }
    

    public void testTotalWeightedAva() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("weightedAve");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_WEIGHTEDAVE_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertTrue(ag.getParameterDefn()[1]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(weight[i])});
        }
        ac.finish();
       
        assertEquals(new Double(5.343042071197409), ac.getValue());
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray2[i]), new Double(weight[i])});
        }
        ac.finish();
        
        assertEquals(new Double(3.236104279390063), ac.getValue());
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i], new Double(weight[i])});
        }
        ac.finish();
       
        assertEquals(new Double(3.236104279390063), ac.getValue());
       
        ac.start();
        ac.onRow(new Object[]{new Double(1), new Double(1)});
        ac.onRow(new Object[]{new Double(2), new Double(2)});
        ac.finish();
        System.out.println(ac.getValue( ));
        assertEquals(new Double(1.6666666666666667), ac.getValue());
        
        ac.start();
        ac.onRow(new Object[]{null, new Double(3)});
        ac.onRow(new Object[]{new Double(2), new Double(2)});
        ac.onRow(new Object[]{new Double(2), null});
        ac.finish();
        System.out.println(ac.getValue( ));
        assertEquals(new Double(2), ac.getValue());
        
        ac.start();
        ac.onRow(new Object[]{new Double(1), new Double(3)});
        ac.onRow(new Object[]{new Double(1), new Double(-3)});
        ac.finish();
        System.out.println(ac.getValue( ));
        assertEquals(null, ac.getValue());
        
        ac.start();
        ac.onRow(new Object[]{new Double(1), new Double(2)});
        ac.onRow(new Object[]{new Double(2), new Double(-4)});
        ac.finish();
        System.out.println(ac.getValue( ));
        assertEquals(new Double(3.0), ac.getValue());
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
        ac.start();
        try
        {
            ac.getValue();
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            assertTrue(true);
        }

    }
    
    public void testTotalMovingAva() throws Exception
    {
        double[] values1 = new double[]{1.0, 2.0, 3.0, 3.25, 3.8, 4.5, 4.285714285714286, 4.25, 4.75, 5.25, 5.75, 6.5, 6.25, 6, 6.5};
        double[] values2 = new double[]{4.0, -19.5, -11.666666666666666, -3.0, -5.4, -5.5, -5.5, 0.6666666666666666, 0.5, 7.1666666666666666, 15.1666666666666666, 15.1666666666666666, 10.6666666666666666, 17.3333333333333333};
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("movingAve");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_MOVINGAVE_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn()[1]);
        

        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Object[]{new Double(doubleArray1[i]), new Integer(8)});
            assertEquals(new Double(values1[i]), ac.getValue());
        }
        ac.finish();
        
        ac.start();
        for(int i=0; i<doubleArray2.length; i++)
        {
            ac.onRow(new Object[]{new Double(doubleArray2[i]), new Integer(6)});
            assertEquals(new Double(values2[i]), ac.getValue());
        }
        ac.finish();
        
        ac.start();
        for(int i=0; i<str1.length; i++)
        {
            ac.onRow(new Object[]{str1[i], new Integer(6)});
            assertEquals(new Double(values2[i]), ac.getValue());
        }
        ac.finish();
        
        ac.start();
        ac.finish();
        assertEquals(null, ac.getValue());
        
    }
    
    public void testTotalAvaDate() throws Exception
    {
                                
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("ave");
        Accumulator ac = ag.newAccumulator();
        ac.start();
        for(int i=0; i<dates.length; i++)
        {
            ac.onRow(new Object[]{dates[i]});
        }
        ac.finish();
        assertEquals(new Date(2500000L), ac.getValue());

    }


    public void testTotalMaxDate() throws Exception
    {                                
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("max");
        Accumulator ac = ag.newAccumulator();
        ac.start();
        for(int i=0; i<dates.length; i++)
        {
            ac.onRow(new Object[]{dates[i]});
        }
        ac.finish();
        assertEquals(new Date(4000000L), ac.getValue());

    }
    
    public void testTotalMinDate() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("min");
        Accumulator ac = ag.newAccumulator();
        ac.start();
        for(int i=0; i<dates.length; i++)
        {
            ac.onRow(new Object[]{dates[i]});
        }
        ac.finish();
        assertEquals(new Date(1000000L), ac.getValue());

    }
    
    public void testTotalMaxString() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("max");
        Accumulator ac = ag.newAccumulator();
        ac.start();
        for(int i=0; i<str2.length; i++)
        {
            ac.onRow(new Object[]{str2[i]});
        }
        ac.finish();
        assertEquals("test", ac.getValue());
    }
    
    public void testTotalMinString() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("min");
        Accumulator ac = ag.newAccumulator();
        ac.start();
        for(int i=0; i<str2.length; i++)
        {
            ac.onRow(new Object[]{str2[i]});
        }
        ac.finish();
        assertEquals("aggregation", ac.getValue());
    }
    
    public void testTotalTop() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("isTopN");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_TOP_N_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn()[1]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(5)});
        }
        ac.finish();
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(5)});
            assertEquals(new Boolean(doubleArray1TopBottom[i]), ac.getValue());
        }
        ac.finish();
        
        ag = BuiltInAggregationFactory.getInstance().getAggregation("isTopN");
        ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_TOP_N_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn()[1]);
        
        ac.start();
        	ac.onRow(new Double[]{new Double(6), new Double(5)});
        ac.finish();
        ac.start();
    	ac.onRow(new Double[]{new Double(6), new Double(5)});
    	ac.finish();
 
        
       	assertEquals(new Boolean(true), ac.getValue());
      	     
        ag = BuiltInAggregationFactory.getInstance().getAggregation("isTopNPercent");
        ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_TOP_PERCENT_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn()[1]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(33)});
        }
        ac.finish();
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(33)});
            assertEquals(new Boolean(doubleArray1TopBottom[i]),ac.getValue());
        }
        ac.finish();
        
        
        ag = BuiltInAggregationFactory.getInstance().getAggregation("isTopNPercent");
        ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_TOP_PERCENT_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn()[1]);
        
        ac.start();
        ac.onRow(new Double[]{new Double(6), new Double(100)});
        ac.finish();
        ac.start();
        ac.onRow(new Double[]{new Double(6), new Double(100)});
        ac.finish();
        
        assertEquals(new Boolean(true), ac.getValue());
    }
    
    public void testTotalBottom() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("isBottomN");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_BOTTOM_N_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn()[1]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(10)});
        }
        ac.finish();
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(10)});
        	assertEquals(new Boolean(!doubleArray1TopBottom[i]), ac.getValue());
        }
        ac.finish();
        
        ag = BuiltInAggregationFactory.getInstance().getAggregation("isBottomNPercent");
        ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_BOTTOM_PERCENT_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn()[1]);
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(66)});
        }
        ac.finish();
        
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(66)});
            assertEquals(new Boolean(!doubleArray1TopBottom[i]), ac.getValue());
        }
        ac.finish();
    }
    
    public void testTotalRank() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("rank");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_RANK_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn()[1]);
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Boolean(false)});
        }
        ac.finish();
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Integer(0)});
            assertEquals(new Integer(doubleArray3RankDec[i]), ac.getValue());
        }
        ac.finish();
        
        ag = BuiltInAggregationFactory.getInstance().getAggregation("rank");
        ac = ag.newAccumulator();
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Boolean(true)});
        }
        ac.finish();
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Integer(1)});
        	assertEquals(new Integer(doubleArray3RankAsc[i]), ac.getValue());
        }
        ac.finish();
     }
    
    public void testTotalPercentRank() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("percentrank");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_PERCENT_RANK_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i]});
        }
        ac.finish();
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i]});
            assertEquals(doubleArray3PercentRank[i], new Double((((Double)ac.getValue()).doubleValue( )*1000)).intValue( ));
        }
        ac.finish();
     }
    
    public void testTotalPercentSum() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("percentsum");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_PERCENTSUM_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(1, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i]});
        }
        ac.finish();
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i]});
        	assertEquals(doubleArray3PercentSum[i], ac.getValue().equals( "" )?null:new Integer((int)(new Double(ac.getValue().toString()).doubleValue( )*1000)));
        }
        ac.finish();
    }
    
    public void testTotalPercentile() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("percentile");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_PERCENTILE_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn( )[1]);
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(0.1)});
        }
        ac.finish();
        Object value = ac.getValue( );
        assertEquals(value, new Double( 10.0 ));
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(0)});
        }
        ac.finish();
        value = ac.getValue( );
        assertEquals(value, new Double( 10 ));
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(1)});
        }
        ac.finish();
        value = ac.getValue( );
        assertEquals(value, new Double( 300 ));
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(0.7)});
        }
        ac.finish();
        value = ac.getValue( );
        assertEquals(value, new Double( 70 ));
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(0.35)});
        }
        ac.finish();
        value = ac.getValue( );
        assertEquals(value, new Double( 17.5 ));
        
        try{
        	 ac.start();
             for(int i=0; i<doubleArray3.length; i++)
             {
                 ac.onRow(new Object[]{doubleArray3[i], new Double(-1)});
             }
             ac.finish();
             fail("should not arrive here");
        }catch ( DataException e )
        {}
     }
    
    public void testTotalQuartile() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("quartile");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_QUARTILE_FUNC, ag.getName());
        assertEquals(IAggregation.SUMMARY_AGGR, ag.getType());
        assertEquals(2, ag.getParameterDefn().length);
        assertTrue(ag.getParameterDefn()[0]);
        assertFalse(ag.getParameterDefn( )[1]);
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(0)});
        }
        ac.finish();
        Object value = ac.getValue( );
        assertEquals(value, new Double( 10 ));
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(1)});
        }
        ac.finish();
        value = ac.getValue( );
        assertEquals(value, new Double( 12.5 ));
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(2)});
        }
        ac.finish();
        value = ac.getValue( );
        assertEquals(value, new Double( 30 ));
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(3)});
        }
        ac.finish();
        value = ac.getValue( );
        assertEquals(value, new Double( 85 ));
        
        ac.start();
        for(int i=0; i<doubleArray3.length; i++)
        {
            ac.onRow(new Object[]{doubleArray3[i], new Double(4)});
        }
        ac.finish();
        value = ac.getValue( );
        assertEquals(value, new Double( 300 ));
        
        try{
       	 ac.start();
            for(int i=0; i<doubleArray3.length; i++)
            {
                ac.onRow(new Object[]{doubleArray3[i], new Double(5)});
            }
            ac.finish();
            fail("should not arrive here");
       }catch ( DataException e )
       {}
     }
    
    public void testTotalRunningCount() throws Exception
    {
        IAggregation ag = BuiltInAggregationFactory.getInstance().getAggregation("runningcount");
        Accumulator ac = ag.newAccumulator();
        assertEquals(BuiltInAggregationFactory.TOTAL_RUNNINGCOUNT_FUNC, ag.getName());
        assertEquals(IAggregation.RUNNING_AGGR, ag.getType());
        assertEquals(0, ag.getParameterDefn().length);
               
        ac.start();
        for(int i=0; i<doubleArray1.length; i++)
        {
            ac.onRow(new Double[]{new Double(doubleArray1[i]), new Double(5)});
            assertEquals( new Integer(i+1), ac.getValue());
        }
        ac.finish();
        
   }
    
}
