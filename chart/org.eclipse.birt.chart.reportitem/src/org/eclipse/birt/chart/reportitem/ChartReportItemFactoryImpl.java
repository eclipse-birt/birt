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

import java.util.Locale;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItemFactory;

/**
 *  
 */
public class ChartReportItemFactoryImpl extends ReportItemFactory implements IMessages
{
    /**
     *  
     */
    public ChartReportItemFactoryImpl()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IReportItemFactory#newReportItem(org.eclipse.birt.report.model.api.ReportDesignHandle)
     */
    public IReportItem newReportItem(DesignElementHandle item)
    {
        return new ChartReportItemImpl(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IReportItemFactory#getMessages()
     */
    public IMessages getMessages()
    {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IMessages#getMessage(java.lang.String, java.util.Locale)
     */
    public String getMessage(String key, Locale locale)
    {
        //DefaultLoggerImpl.instance().log(ILogger.ERROR, "Request for resource key=" + key);
        //return key + "_value";

        // TEMP LOOKUP ALGORITHM - TO BE CHANGED TO LOOKUP A RESOURCEBUNDLE
        final StringBuffer sb = new StringBuffer(key);
        final int iFirstDot = sb.indexOf(".");
        if (iFirstDot == -1)
            return key + "_value";
        sb.delete(0, iFirstDot + 1);
        int i = 0, iDot;
        char c = sb.charAt(0);
        sb.setCharAt(0, Character.toUpperCase(c));
        do
        {
            iDot = sb.indexOf(".", i);
            if (iDot >= 0 && iDot < sb.length() - 1)
            {
                sb.setCharAt(iDot, ' ');
                i = iDot + 1;
            }
        }
        while (i < sb.length() && iDot != -1);
        return sb.toString();
    }
}