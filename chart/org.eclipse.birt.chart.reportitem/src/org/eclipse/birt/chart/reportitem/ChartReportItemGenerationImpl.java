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

import java.util.HashMap;

import org.eclipse.birt.chart.datafeed.ResultSetWrapper;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.extension.DefaultReportItemGenerationImpl;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.extension.ExtendedElementException;
import org.eclipse.birt.report.model.extension.IReportItem;

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
    public void initialize(HashMap hm) throws BirtException
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
    public final IBaseQueryDefinition nextQuery(IBaseQueryDefinition ibdqParent) throws BirtException
    {
        DefaultLoggerImpl.instance().log(ILogger.ERROR, "ChartReportItemGenerationImpl: nextQuery(...) - start");
        if (iQueryCount > 0) // ONLY 1 QUERY ASSOCIATED WITH A CHART
        {
            return null;
        }
        
        final QueryDefinition qd = new QueryDefinition((BaseQueryDefinition) ibdqParent);
        try {
            QueryHelper.instance().build(eih, qd, cm);
	        iQueryCount++;
        } catch (GenerationException gex)
        {
            DefaultLoggerImpl.instance().log(gex);
            return null;
        }
        return qd;
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#pushPreparedQuery(org.eclipse.birt.data.engine.api.IBaseQueryDefinition, org.eclipse.birt.data.engine.api.IPreparedQuery)
	 */
	public final void pushPreparedQuery(IBaseQueryDefinition ibqd, IPreparedQuery ipq)
	{
	    // HOLD THIS QUERY FOR SUBSEQUENT EXECUTION
	    this.ibqd = ibqd;
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#process(org.eclipse.birt.report.engine.data.IDataEngine)
     */
    public final void process(IDataEngine ide) throws BirtException
    {
        DefaultLoggerImpl.instance().log(ILogger.ERROR, "ChartReportItemGenerationImpl: process(...) - start");
        
        // EXECUTE THE PREVIOUSLY BUILT QUERY
        final ResultSetWrapper rsw = QueryHelper.instance().execute(ide, ibqd, cm);
    	
    	// POPULATE THE CHART MODEL WITH THE RESULTSET
    	try {
    	    QueryHelper.instance().generateRuntimeSeries(cm, rsw);
    	} catch (GenerationException gex)
    	{
    	    DefaultLoggerImpl.instance().log(gex);
    	}
    	
        DefaultLoggerImpl.instance().log(ILogger.ERROR, "ChartReportItemGenerationImpl: process(...) - end");
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
}