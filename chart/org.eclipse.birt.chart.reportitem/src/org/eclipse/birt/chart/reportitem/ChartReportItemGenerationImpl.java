/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.datafeed.ResultSetDataSet;
import org.eclipse.birt.chart.datafeed.ResultSetWrapper;
import org.eclipse.birt.chart.exception.DataSetException;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.exception.PluginException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.extension.DefaultReportItemGenerationImpl;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.extension.ExtendedElementException;
import org.eclipse.birt.report.model.extension.IReportItem;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 *  
 */
public class ChartReportItemGenerationImpl extends DefaultReportItemGenerationImpl
{
    /**
     *  
     */
    private transient Chart cm = null;
    
    /**
     * 
     */
    private transient ExtendedItemHandle eih = null;

    /**
     * 
     */
    private transient IBaseQueryDefinition ibqd = null;

    /**
     * 
     */
    private transient int iQueryCount = 0;
    
    /**
     *  
     */
    public ChartReportItemGenerationImpl()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#initialize(org.apache.batik.dom.util.HashTable)
     */
    public void initialize(HashMap hm)
    {
        DefaultLoggerImpl.instance().log(ILogger.ERROR, "ChartReportItemGenerationImpl: initialize(...) - start");
        super.initialize(hm);
        cm = getModelFromWrapper(hm.get(MODEL_OBJ));
        DefaultLoggerImpl.instance().log(ILogger.ERROR, "ChartReportItemGenerationImpl: initialize(...) - end");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#getSize()
     */
    public Size getSize()
    {
        if (cm != null)
        {
            final Size sz = new Size();
            sz.setWidth((float) cm.getBlock().getBounds().getWidth());
            sz.setHeight((float) cm.getBlock().getBounds().getHeight());
            sz.setUnit(Size.UNITS_PT);
            return sz;
        }
        return super.getSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#finish()
     */
    public void finish()
    {
        super.finish();
    }

    /**
     * 
     */
    public final IBaseQueryDefinition nextQuery(IBaseQueryDefinition ibdqParent)
    {
        DefaultLoggerImpl.instance().log(ILogger.ERROR, "ChartReportItemGenerationImpl: nextQuery(...) - start");
        if (iQueryCount > 0) // ONLY 1 QUERY ASSOCIATED WITH A CHART
        {
            return null;
        }
        
        final QueryDefinition rqd = new QueryDefinition((BaseQueryDefinition) ibdqParent);
        try {
	        if (cm instanceof ChartWithAxes)
	        {
	            buildQuery(rqd, (ChartWithAxes) cm);
	        }
	        else if (cm instanceof ChartWithoutAxes)
	        {
	            buildQuery(rqd, (ChartWithoutAxes) cm);
	        }
	        iQueryCount++;
        } catch (GenerationException gex)
        {
            DefaultLoggerImpl.instance().log(gex);
            return null;
        }
        return rqd;
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#pushPreparedQuery(org.eclipse.birt.data.engine.api.IBaseQueryDefinition, org.eclipse.birt.data.engine.api.IPreparedQuery)
	 */
	public final void pushPreparedQuery(IBaseQueryDefinition ibqd, IPreparedQuery ipq)
	{
	    this.ibqd = ibqd;
	    
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#process(org.eclipse.birt.report.engine.data.IDataEngine)
     */
    public final void process(IDataEngine ide)
    {
        DefaultLoggerImpl.instance().log(ILogger.ERROR, "ChartReportItemGenerationImpl: process(...) - start");
        
        // EXECUTE THE QUERY
        IQueryResults iqr = null;
        IResultIterator irt = null;
        IResultSet irs = null;
        try {
            irs = ide.execute(ibqd);
    	} catch (Exception ex)
    	{
    	    ex.printStackTrace();
    	}
    	
    	// CREATE A LOOKUP FOR SORT KEYS
    	LinkedHashMap lhmLookup = new LinkedHashMap();
    	Collection co = ibqd.getRowExpressions();
    	Iterator it = co.iterator();
    	ScriptExpression sxp;
    	int i = 0;
    	while (it.hasNext())
    	{
    	    sxp = (ScriptExpression) it.next();
    	    lhmLookup.put(sxp.getText(), new Integer(i++));
    	}
    	
    	// WALK THROUGH RESULTS
    	final List liResultSet = new ArrayList();
    	final int iColumnCount = co.size();
    	Object[] oaTuple;
    	int iColumnIndex;
    	while (irs.next())
    	{
    	    oaTuple = new Object[iColumnCount];
        	it = co.iterator();
        	iColumnIndex = 0;
        	while (it.hasNext())
        	{
        	    oaTuple[iColumnIndex++] = irs.evaluate((IBaseExpression)it.next());
        	}
    	    liResultSet.add(oaTuple);
    	}
    	irs.close();
    	
    	// SORT ON MULTIPLE KEYS
    	final String[] saSortKeys = getSortKeys(cm);
    	final int[] iaSortKeys = new int[saSortKeys.length];
    	for (i = 0; i < saSortKeys.length; i++)
    	{
    	    iaSortKeys[i] = ((Integer) lhmLookup.get(saSortKeys[i])).intValue();
    	}
    	Collections.sort(liResultSet, new TupleComparator(iaSortKeys));
    	
    	// PRESENT THE RESULTS FOR DEBUGGING
    	it = liResultSet.iterator();
    	while (it.hasNext())
    	{
    	    oaTuple = (Object[]) it.next();
    	    for (i = 0; i < oaTuple.length; i++)
    	    {
    	        System.out.print(oaTuple[i] + ", ");
    	    }
    	    System.out.println();
    	}
    	
    	// POPULATE THE CHART MODEL WITH THE RESULTSET
    	try {
    	    generateRuntimeSeries(cm, new ResultSetWrapper(lhmLookup.keySet(), liResultSet));
    	} catch (GenerationException gex)
    	{
    	    DefaultLoggerImpl.instance().log(gex);
    	}
    	
        DefaultLoggerImpl.instance().log(ILogger.ERROR, "ChartReportItemGenerationImpl: process(...) - end");
    }

    /**
     * 
     * @param cm
     * @param rsw
     */
    private final void generateRuntimeSeries(Chart cm, ResultSetWrapper rsw) throws GenerationException
    {
        final int iGroupCount = rsw.getGroupCount();
        if (cm instanceof ChartWithAxes)
        {
            final ChartWithAxes cwa = (ChartWithAxes) cm;

            // POPULATE THE BASE RUNTIME SERIES
            final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];
            EList elSD = axPrimaryBase.getSeriesDefinitions();
            final SeriesDefinition sdBase = (SeriesDefinition) elSD.get(0);
            final Series seBaseDesignSeries = sdBase.getDesignTimeSeries();
            final Series seBaseRuntimeSeries = (Series) EcoreUtil.copy(seBaseDesignSeries);
            sdBase.getSeries().add(seBaseRuntimeSeries);
            final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);
            int iOrthogonalSeriesDefinitionCount = 0;
            SeriesDefinition sd;
            Query qy;
            String sExpression;
            
            for (int i = 0; i < axaOrthogonal.length; i++)
            {
                elSD = axaOrthogonal[i].getSeriesDefinitions();
                for (int j = 0; j < elSD.size(); j++)
                {
                    sd = (SeriesDefinition) elSD.get(j);
                    qy = sd.getQuery();
                    if (qy == null)
                    {
                        continue;
                    }
                    sExpression = qy.getDefinition();
                    if (sExpression == null || sExpression.length() == 0)
                    {
                        continue;
                    }
                    iOrthogonalSeriesDefinitionCount++;
                }
            }
            
            if (iOrthogonalSeriesDefinitionCount < 1)
            {
	            fillSeriesDataSet(
	                seBaseRuntimeSeries, 
	                rsw.getSubset(iOrthogonalSeriesDefinitionCount)
	            );
	            
	            // POPULATE ONE ORTHOGONAL SERIES
	            Series seOrthogonalDesignSeries;
	            Series seOrthogonalRuntimeSeries;
	            SeriesDefinition sdOrthogonal;
	            int iOffset = 0;
	            for (int i = 0; i < axaOrthogonal.length; i++) // FOR EACH AXIS
	            {
	                elSD = axaOrthogonal[i].getSeriesDefinitions();
	                for (int j = 0; j < elSD.size(); j++) // FOR EACH ORTHOGONAL SERIES DEFINITION
	                {
	                    sdOrthogonal = (SeriesDefinition) elSD.get(j);
	                    seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries();
	                    sExpression = ((Query)seOrthogonalDesignSeries.getDataDefinition().get(0)).getDefinition();
                        seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy(seOrthogonalDesignSeries);
                        fillSeriesDataSet(
                            seOrthogonalRuntimeSeries, 
                            rsw.getSubset(sExpression)
                        );
                        seOrthogonalRuntimeSeries.setSeriesIdentifier(sExpression);
                        sdOrthogonal.getSeries().add(seOrthogonalRuntimeSeries);
	                }
	            }
            }
            else
            {
	            fillSeriesDataSet(
	                seBaseRuntimeSeries, 
	                rsw.getSubset(0, iOrthogonalSeriesDefinitionCount)
	            );
	            
	            // POPULATE ALL ORTHOGONAL SERIES
	            Series seOrthogonalDesignSeries;
	            Series seOrthogonalRuntimeSeries;
	            SeriesDefinition sdOrthogonal;
	            int iOffset = 0;
	            for (int i = 0; i < axaOrthogonal.length; i++) // FOR EACH AXIS
	            {
	                elSD = axaOrthogonal[i].getSeriesDefinitions();
	                for (int j = 0; j < elSD.size(); j++) // FOR EACH ORTHOGONAL SERIES DEFINITION
	                {
	                    sdOrthogonal = (SeriesDefinition) elSD.get(j);
	                    seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries();
	                    iOffset++;
	                    for (int k = 0; k < iGroupCount; k++) // FOR EACH ORTHOGONAL RUNTIME SERIES
	                    {
	                        seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy(seOrthogonalDesignSeries);
	                        fillSeriesDataSet(
	                            seOrthogonalRuntimeSeries, 
	                            rsw.getSubset(k, iOrthogonalSeriesDefinitionCount + iOffset)
	                        );
	                        seOrthogonalRuntimeSeries.setSeriesIdentifier(rsw.getGroupKey(k, j));
	                        sdOrthogonal.getSeries().add(seOrthogonalRuntimeSeries);
	                    }
	                }
	            }
            }
        }
        else
        {
            throw new GenerationException("Not yet implemented");
        }
    }

    /**
     * 
     * @param seRuntime
     * @param rsds
     * @throws GenerationException
     */
    private final void fillSeriesDataSet(Series seRuntime, ResultSetDataSet rsds) throws GenerationException
    {
        IDataSetProcessor idsp = null;
        try {
            idsp = PluginSettings.instance().getDataSetProcessor(seRuntime.getClass());
        } catch (PluginException pex)
        {
            throw new GenerationException(pex);
        }
        
        DataSet ds = null;
        switch(rsds.getDataType())
        {
            case IConstants.TEXT:
                ds = TextDataSetImpl.create(null);
                break;
                
            case IConstants.NUMERICAL:
                ds = NumberDataSetImpl.create(null);
                break;
                
            case IConstants.DATE_TIME:
                ds = DateTimeDataSetImpl.create(null);
                break;
        }
        
        if (ds == null)
        {
            throw new GenerationException("Unable to determine data type for base dataset");
        }
        
        try {
            idsp.populate(rsds, ds);
        } catch (DataSetException dsx)
        {
            throw new GenerationException(dsx);
        }
        
        seRuntime.setDataSet(ds);
    }
    
    /**
     * 
     * @param cwa
     * @return
     */
    private final String[] getSortKeys(Chart cm)
    {
        final ArrayList alExpressions = new ArrayList(4);
        
        if (cm instanceof ChartWithAxes)
        {
            final ChartWithAxes cwa = (ChartWithAxes) cm;
            final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];
            EList elSD = axPrimaryBase.getSeriesDefinitions();
            
            // PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES DEFINITION
            SeriesDefinition sd = (SeriesDefinition) elSD.get(0);
            final Query qBaseSeriesDefinition = sd.getQuery();
            String sExpression = qBaseSeriesDefinition.getDefinition();
            if (sExpression != null && sExpression.trim().length() > 0) // CHECK FOR UNSPECIFIED EXPRESSIONS
            {
                DefaultLoggerImpl.instance().log(ILogger.ERROR, "Base series definition (" + sExpression + ") will be used as a static expression");
            }
            
            // PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
            final Series seBase = sd.getDesignTimeSeries();
            EList elBaseSeries = seBase.getDataDefinition();
            final Query qBaseSeries = (Query) elBaseSeries.get(0);
            if (qBaseSeries != null) // NPE PROTECTION
            {
	            sExpression = qBaseSeries.getDefinition();
	            if (sExpression != null && sExpression.trim().length() > 0) // CHECK FOR UNSPECIFIED EXPRESSIONS
	            {
	                if (!alExpressions.contains(sExpression)) // FILTER OUT DUPLICATE ENTRIES
	                {
	                    alExpressions.add(sExpression);
	                }
	            }
            }

            // PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
            Query qOrthogonalSeriesDefinition, qOrthogonalSeries;
            Series seOrthogonal;
            EList elOrthogonalSeries;
            final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);
            int iCount = 0;
            for (int j = 0; j < axaOrthogonal.length; j++)
            {
                elSD = axaOrthogonal[j].getSeriesDefinitions();
                for (int k = 0; k < elSD.size(); k++)
                {
                    sd = (SeriesDefinition) elSD.get(k);
                    qOrthogonalSeriesDefinition = sd.getQuery();
                    if (qOrthogonalSeriesDefinition == null)
                    {
                        continue;
                    }
                    sExpression = qOrthogonalSeriesDefinition.getDefinition();
                    if (sExpression != null && sExpression.trim().length() > 0) // CHECK FOR UNSPECIFIED EXPRESSIONS
                    {
                        if (sExpression != null && sExpression.trim().length() > 0) // CHECK FOR UNSPECIFIED EXPRESSIONS
                        {
                            if (alExpressions.contains(sExpression)) // FILTER OUT DUPLICATE ENTRIES
                            {
                                int iRemovalIndex = alExpressions.indexOf(sExpression);
                                if (iRemovalIndex > iCount)
                                {
                                    alExpressions.remove(iRemovalIndex);
                                }
                                else
                                {
                                    // DON'T ADD IF PREVIOUSLY ADDED BEFORE 'iCount'
                                    continue;
                                }
                            }
                            alExpressions.add(iCount++, sExpression); // INSERT AT START
                        }
                    }
                }
            }
        }
        return (String[]) alExpressions.toArray(new String[0]);
    }
    
    /**
     * 
     * @param cwa
     */
    private final void buildQuery(QueryDefinition qd, ChartWithAxes cwa) throws GenerationException
    {
        final ArrayList alExpressions = new ArrayList(4);
        final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];
        EList elSD = axPrimaryBase.getSeriesDefinitions();
        if (elSD.size() != 1)
        {
            throw new GenerationException("Cannot decipher contents of a chart containing multiple base series definitions");
        }
        
        // PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES DEFINITION
        final SortDefinition srtd = new SortDefinition();
        SeriesDefinition sd = (SeriesDefinition) elSD.get(0);
        final Query qBaseSeriesDefinition = sd.getQuery();
        String sExpression = qBaseSeriesDefinition.getDefinition();
        if (sExpression != null && sExpression.trim().length() > 0) // CHECK FOR UNSPECIFIED EXPRESSIONS
        {
            DefaultLoggerImpl.instance().log(ILogger.ERROR, "Base series definition (" + sExpression + ") will be used as a static expression");
        }
        
        // PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
        final Series seBase = sd.getDesignTimeSeries();
        EList elBaseSeries = seBase.getDataDefinition();
        if (elBaseSeries.size() != 1)
        {
            throw new GenerationException("Found "+elBaseSeries.size()+" data definition(s) associated with the base (X) series expression.  Only ONE base series data definition is supported.");
        }
        final Query qBaseSeries = (Query) elBaseSeries.get(0);
        sExpression = qBaseSeries.getDefinition();
        if (sExpression != null && sExpression.trim().length() > 0) // CHECK FOR UNSPECIFIED EXPRESSIONS
        {
            if (!alExpressions.contains(sExpression)) // FILTER OUT DUPLICATE ENTRIES
            {
                alExpressions.add(sExpression);
                srtd.setExpression(sExpression);
            }
        }

        // PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
        Query qOrthogonalSeriesDefinition, qOrthogonalSeries;
        Series seOrthogonal;
        EList elOrthogonalSeries;
        final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);
        int iCount = 0;
        for (int j = 0; j < axaOrthogonal.length; j++)
        {
            elSD = axaOrthogonal[j].getSeriesDefinitions();
            for (int k = 0; k < elSD.size(); k++)
            {
                sd = (SeriesDefinition) elSD.get(k);
                qOrthogonalSeriesDefinition = sd.getQuery();
                if (qOrthogonalSeriesDefinition == null)
                {
                    continue;
                }
                sExpression = qOrthogonalSeriesDefinition.getDefinition();
                if (sExpression != null && sExpression.trim().length() > 0) // CHECK FOR UNSPECIFIED EXPRESSIONS
                {
                    if (sExpression != null && sExpression.trim().length() > 0) // CHECK FOR UNSPECIFIED EXPRESSIONS
                    {
                        if (alExpressions.contains(sExpression)) // FILTER OUT DUPLICATE ENTRIES
                        {
                            int iRemovalIndex = alExpressions.indexOf(sExpression);
                            if (iRemovalIndex > iCount)
                            {
                                alExpressions.remove(iRemovalIndex);
                            }
                            else
                            {
                                // DON'T ADD IF PREVIOUSLY ADDED BEFORE 'iCount'
                                continue;
                            }
                        }
                        alExpressions.add(iCount++, sExpression); // INSERT AT START
                    }
                }
                
                seOrthogonal = sd.getDesignTimeSeries();
                elOrthogonalSeries = seOrthogonal.getDataDefinition();
                if (elOrthogonalSeries.isEmpty())
                {
                    throw new GenerationException("A data definition expression must be associated with the orthogonal (Y) series-" + iCount + " defined by " + seOrthogonal);
                }
                for (int i = 0; i < elOrthogonalSeries.size(); i++)
                {
                    qOrthogonalSeries = (Query) elOrthogonalSeries.get(i);
                    if (qOrthogonalSeries == null) // NPE PROTECTION
                    {
                        continue;
                    }
                    sExpression = qOrthogonalSeries.getDefinition();
                    if (sExpression != null && sExpression.trim().length() > 0) // CHECK FOR UNSPECIFIED EXPRESSIONS
                    {
                        if (!alExpressions.contains(sExpression)) // FILTER OUT DUPLICATE ENTRIES
                        {
                            alExpressions.add(sExpression); // APPEND AT END
                        }
                    }
                }
            }
        }
        
        // ADD ALL REQUIRED EXPRESSIONS
        qd.setDataSetName(eih.getDataSet().getName());
        //qd.addSort(srtd); // CAN'T SORT
        ScriptExpression sxp;
        for (int i = 0; i < alExpressions.size(); i++)
        {
            sxp = new ScriptExpression((String) alExpressions.get(i));
            qd.addExpression(sxp, QueryDefinition.ON_EACH_ROW);
        }
    }
    
    /**
     * 
     * @param cwa
     */
    private final void buildQuery(QueryDefinition rqd, ChartWithoutAxes cwa) throws GenerationException
    {
        throw new GenerationException("Not yet implemented");
    }

    /**
     * 
     * @param oReportItemImpl
     * @return
     */
    private final Chart getModelFromWrapper(Object oReportItemImpl)
    {
        eih = (ExtendedItemHandle) oReportItemImpl;
        IReportItem item = ((ExtendedItem) eih.getElement()).getExtendedElement();
        if (item == null)
        {
            try
            {
                eih.loadExtendedElement();
            }
            catch (ExtendedElementException eeex )
            {
                DefaultLoggerImpl.instance().log(eeex);
            }
            item = ((ExtendedItem) eih.getElement()).getExtendedElement();
            if (item == null)
            {
                DefaultLoggerImpl.instance().log(ILogger.ERROR, "Unable to locate report item wrapper for chart object");
                return null;
            }
        }
        final ChartReportItemImpl crii = ((ChartReportItemImpl) item);
        return crii.getModel();
    }
    
    /**
     * An internal comparator capable of sorting tuples on multiple keys
     */
    private static final class TupleComparator implements Comparator
    {
        /**
         * 
         */
        private final int[] iaSortKeys;
        
        /**
         * 
         * @param iaSortKeys
         */
        private TupleComparator(int[] iaSortKeys)
        {
            this.iaSortKeys = iaSortKeys;
        }

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2)
        {
            final Object[] oaTuple1 = (Object []) o1;
            final Object[] oaTuple2 = (Object []) o2;
            Object oValue1, oValue2;
            int iResult;
            for (int i = 0; i < iaSortKeys.length; i++)
            {
                oValue1 = oaTuple1[iaSortKeys[i]];
                oValue2 = oaTuple2[iaSortKeys[i]];
                iResult = compareObjects(oValue1, oValue2);
                if (iResult != 0)
                {
                    return iResult;
                }
            }
            return 0;
        }
    }

	/**
	 * Compare two objects of the same data type
	 */ 
	public static int compareObjects(Object a, Object b)
	{
		// a == b
		if (a == null && b == null)
		{
			return 0;
		}

		// a < b
		else if (a == null && b != null)
		{
			return -1;
		}

		// a > b
		else if (a != null && b == null)
		{
			return 1;
		}

		else if (a instanceof String)
		{
			int iC = a.toString().compareTo(b.toString());
            if (iC != 0) iC = ((iC < 0) ? -1 : 1);
            return iC;
		}
		else if (a instanceof Number)
		{
			final double d1 = ((Number)a).doubleValue();
			final double d2 = ((Number)b).doubleValue();
			return (d1 == d2 ) ? 0 : (d1 < d2) ? -1 : 1 ;
		}
		else if (a instanceof java.util.Date)
		{
			final long d1 = ((java.util.Date)a).getTime();
			final long d2 = ((java.util.Date)b).getTime();
			return (d1 == d2 ) ? 0 : (d1 < d2) ? -1 : 1 ;
		}
		else if (a instanceof java.util.Calendar)
		{
		    final long d1 = ((java.util.Calendar)a).getTime().getTime();
		    final long d2 = ((java.util.Calendar)b).getTime().getTime();
			return (d1 == d2 ) ? 0 : (d1 < d2) ? -1 : 1 ;
		}
		else // HANDLE AS STRINGs
		{
		    return compareObjects(a.toString(), b.toString());
		}
	}
}