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

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.extension.IMessages;
import org.eclipse.birt.report.model.extension.IReportItem;
import org.eclipse.birt.report.model.extension.IReportItemFactory;

/**
 * @author Actuate Corporation
 *  
 */
public class ChartReportItemFactoryImpl implements IReportItemFactory
{
	/**
	 * Messages for I18N
	 */
	
    private static final String CHART_EXTENSION = "Element.Chart";

    /**
     *  
     */
    public ChartReportItemFactoryImpl()
    {
        super();
    }

    /**
     * To be removed soon 
     */
    public Object getIcon()
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IReportItemFactory#newReportItem(org.eclipse.birt.report.model.api.ReportDesignHandle)
     */
    public IReportItem newReportItem(ReportDesignHandle item)
    {
        return new ChartReportItemImpl();
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItemFactory#getMessages()
	 */
	public IMessages getMessages( )
	{
	    return new IMessages()
		{
            public String getMessage(String key, Locale locale)
            {
                System.out.println("Request for resource key=" + key);
                return key + "_value";
            }
		};
	}

}