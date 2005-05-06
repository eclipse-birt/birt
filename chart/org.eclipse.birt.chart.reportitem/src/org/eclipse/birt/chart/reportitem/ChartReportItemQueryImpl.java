/**
 * 
 */
package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.extension.ReportItemQueryBase;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;

/**
 *  
 */
public final class ChartReportItemQueryImpl extends ReportItemQueryBase
{
    /**
     *  
     */
    private Chart cm = null;

    /**
     *  
     */
    private ExtendedItemHandle eih = null;

    /**
     *  
     */
    public ChartReportItemQueryImpl()
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemQuery#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
     */
    public void setModelObject(ExtendedItemHandle eih)
    {
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
                return;
            }
        }
        cm = (Chart) ((ChartReportItemImpl) item).getProperty("chart.instance");
        this.eih = eih;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemQuery#getReportQueries(org.eclipse.birt.data.engine.api.IBaseQueryDefinition)
     */
    public IBaseQueryDefinition[] getReportQueries(IBaseQueryDefinition ibqdParent) throws BirtException
    {
        DefaultLoggerImpl.instance()
            .log(ILogger.INFORMATION, "ChartReportItemQueryImpl: getReportQueries(...) - start");

        // BUILD THE QUERY ASSOCIATED WITH THE CHART MODEL
        RunTimeContext rtc = new RunTimeContext();
        //rtc.setLocale(?);
        IBaseQueryDefinition ibqd = null;
        try
        {
            ibqd = QueryHelper.instance(rtc).build(eih, ibqdParent, cm);
        }
        catch (Exception gex )
        {
            DefaultLoggerImpl.instance().log(gex);
            DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                "ChartReportItemQueryImpl: getReportQueries(...) - exception");
            throw new BirtException("getReportQueries", gex);
        }
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "ChartReportItemQueryImpl: getReportQueries(...) - end");
        return new IBaseQueryDefinition[]
        {
            ibqd
        };
    }

}