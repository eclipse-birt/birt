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

import java.io.FileOutputStream;
import java.util.HashMap;

import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.report.engine.extension.DefaultReportItemPresentationImpl;

/**
 * @author Actuate Corporation
 *  
 */
public class ChartReportItemPresentationImpl extends DefaultReportItemPresentationImpl
{

    /**
     *  
     */
    public ChartReportItemPresentationImpl()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public final void initialize(HashMap hm)
    {
        super.initialize(hm);
        try
        {
            final FileOutputStream fos = new FileOutputStream("c:\\presentation.txt");
            fos.write("Called presentation.initialize()".getBytes());
            fos.close();
        }
        catch (Exception ex )
        {
            ex.printStackTrace();
        }
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Called presentation.initialize()");
    }
}