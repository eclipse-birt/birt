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
import java.util.Locale;

import org.eclipse.birt.chart.datafeed.ResultSetWrapper;
import org.eclipse.birt.chart.factory.IMessageLookup;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ScriptHandler;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.extension.DefaultReportItemGenerationImpl;
import org.eclipse.birt.report.engine.extension.IReportItemSerializable;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.mozilla.javascript.Scriptable;

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
    private transient RunTimeContext rtc = null;

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
    private transient int iStage = 0;

    /**
     *  
     */
    private static final int PREPARATION = 1;

    /**
     *  
     */
    private static final int EXECUTION = 2;

    /**
     *  
     */
    public ChartReportItemGenerationImpl()
    {
        super();
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemGenerationImpl: constructor");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#initialize(org.apache.batik.dom.util.HashTable)
     */
    public void initialize(HashMap hm) throws BirtException
    {
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemGenerationImpl: initialize(...) - start");
        super.initialize(hm);
        final ExtendedItemHandle eih = (ExtendedItemHandle) hm.get(MODEL_OBJ);
        cm = getModelFromWrapper(eih);
        final String sStage = (String) hm.get(GENERATION_STAGE);
        if (sStage.equals(GENERATION_STAGE_PREPARATION))
        {
            iStage = PREPARATION;
        }
        else if (sStage.equals(GENERATION_STAGE_EXECUTION))
        {
            iStage = EXECUTION;
        }

        // NOTIFY THE SCRIPT HANDLER AT EXECUTION TIME ONLY
        if (iStage == EXECUTION)
        {
            rtc = new RunTimeContext();
            rtc.setMessageLookup(new ExternalizedMessageLookup(eih.getDesignHandle()));

            // SETUP THE SCRIPTABLE INSTANCE
            final Scriptable scParent = null;
            final String sScriptContent = cm.getScript();
            ScriptHandler sh = null;
            if (sScriptContent != null)
            {
                sh = new ScriptHandler();
                try
                {
                    sh.init(scParent);
                    sh.setRunTimeModel(cm);
                    rtc.setScriptHandler(sh);
                    sh.register(sScriptContent);
                }
                catch (Exception sx )
                {
                    throw new BirtException("initialize", sx);
                }
            }
            ScriptHandler.callFunction(sh, ScriptHandler.START_DATA_BINDING, cm);
        }

        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemGenerationImpl: initialize(...) - end");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#getGenerateState()
     */
    public final IReportItemSerializable getGenerateState()
    {
        final SerializedState ss = new SerializedState();
        ss.setRunTimeContext(rtc);
        return ss;
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

    /**
     *  
     */
    public final IBaseQueryDefinition nextQuery(IBaseQueryDefinition ibdqParent) throws BirtException
    {
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemGenerationImpl: nextQuery(...) - start");
        if (iQueryCount > 0) // ONLY 1 QUERY ASSOCIATED WITH A CHART
        {
            DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                "ChartReportItemGenerationImpl: nextQuery(...) - end(nomore)");
            return null;
        }

        // BUILD THE QUERY ASSOCIATED WITH THE CHART MODEL
        final QueryDefinition qd = new QueryDefinition((BaseQueryDefinition) ibdqParent);
        try
        {
            QueryHelper.instance(rtc).build(eih, qd, cm);
            iQueryCount++;
        }
        catch (Exception gex )
        {
            DefaultLoggerImpl.instance().log(gex);
            DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                "ChartReportItemGenerationImpl: nextQuery(...) - exception");
            throw new BirtException("nextQuery", gex);
        }
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemGenerationImpl: nextQuery(...) - end");
        return qd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#pushPreparedQuery(org.eclipse.birt.data.engine.api.IBaseQueryDefinition,
     *      org.eclipse.birt.data.engine.api.IPreparedQuery)
     */
    public final void pushPreparedQuery(IBaseQueryDefinition ibqd, IPreparedQuery ipq)
    {
        // HOLD THIS QUERY FOR SUBSEQUENT EXECUTION
        this.ibqd = ibqd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#process(org.eclipse.birt.report.engine.data.IDataEngine)
     */
    public final void process(IDataEngine ide) throws BirtException
    {
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemGenerationImpl: process(...) - start");

        // EXECUTE THE PREVIOUSLY BUILT QUERY
        try
        {
            final QueryHelper qh = QueryHelper.instance(rtc);
            final ScriptHandler sh = rtc.getScriptHandler();
            ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_QUERY_EXECUTION, ibqd);
            final ResultSetWrapper rsw = qh.execute(ide, ibqd, cm);
            ScriptHandler.callFunction(sh, ScriptHandler.AFTER_QUERY_EXECUTION, rsw);

            // POPULATE THE CHART MODEL WITH THE RESULTSET
            qh.generateRuntimeSeries(cm, rsw);
        }
        catch (Exception gex )
        {
            DefaultLoggerImpl.instance().log(gex);
            DefaultLoggerImpl.instance().log(ILogger.ERROR, "ChartReportItemGenerationImpl: process(...) - exception");
            throw new BirtException("process", gex);
        }

        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemGenerationImpl: process(...) - end");
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
                DefaultLoggerImpl.instance()
                    .log(ILogger.ERROR, "Unable to locate report item wrapper for chart object");
                return null;
            }
        }
        final ChartReportItemImpl crii = ((ChartReportItemImpl) item);
        return crii.getModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#finish()
     */
    public void finish()
    {
        if (iStage == EXECUTION)
        {
            final ScriptHandler sh = rtc.getScriptHandler();
            ScriptHandler.callFunction(sh, ScriptHandler.FINISH_DATA_BINDING, cm);
        }
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemGenerationImpl: finish(...) - start");
        super.finish();
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemGenerationImpl: finish(...) - end");
    }

    /**
     *  
     */
    private static final class ExternalizedMessageLookup implements IMessageLookup
    {
        /**
         *  
         */
        private final ReportDesignHandle rdh;

        /**
         * 
         * @param rdh
         */
        ExternalizedMessageLookup(ReportDesignHandle rdh)
        {
            this.rdh = rdh;
            DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                "Initializing externalized text lookup - " + rdh.getMessageBaseName());
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.birt.chart.factory.IMessageLookup#getMessageValue(java.lang.String, java.util.Locale)
         */
        public String getMessageValue(String sChartKey, Locale lcl)
        {
            final int iKeySeparator = sChartKey.indexOf(IMessageLookup.KEY_SEPARATOR);
            if (iKeySeparator != -1)
            {
                final String sKey = sChartKey.substring(0, iKeySeparator);
                final String sMessage = (iKeySeparator == 0) ? null : rdh.getMessage(sKey, lcl);
                if (sMessage == null) // BECAUSE [KEY NOT FOUND] OR [PROP FILE NOT FOUND]
                {
                    // VALUE ON RHS OF IMessageLookup.KEY_SEPARATOR
                    return sChartKey.substring(iKeySeparator + 1);
                }
                else
                {
                    // VALUE FROM PROPERTIES FILE
                    return sMessage;
                }
            }
            // FOR [BACKWARD COMPATIBILITY] OR [VALUES NOT CONTAINING A KEY]
            return sChartKey;
        }
    }
}

