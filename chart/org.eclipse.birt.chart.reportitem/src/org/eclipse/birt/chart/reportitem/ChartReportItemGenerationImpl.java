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
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.report.engine.extension.DefaultReportItemGenerationImpl;
import org.eclipse.birt.report.engine.extension.Size;

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
    public ChartReportItemGenerationImpl()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#initialize(org.apache.batik.dom.util.HashTable)
     */
    public void initialize(HashMap parameters)
    {
        super.initialize(parameters);
        try
        {
            final FileOutputStream fos = new FileOutputStream("c:\\generation.txt");
            fos.write("Called generation.initialize()".getBytes());
            fos.close();
        }
        catch (Exception ex )
        {
            ex.printStackTrace();
        }
        cm = getModelFromWrapper(parameters.get(MODEL_OBJ));
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Called generation.initialize()");
    }

    /**
     * 
     * @param oReportItemImpl
     * @return
     */
    private final Chart getModelFromWrapper(Object oReportItemImpl)
    {
        if (oReportItemImpl instanceof ChartReportItemImpl)
        {
            return ((ChartReportItemImpl) oReportItemImpl).getModel();
        }
        else
        {
            DefaultLoggerImpl.instance().log(ILogger.ERROR,
                "Unable to retrieve chart model from wrapper " + oReportItemImpl);
            return null;
        }
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
        // TODO Auto-generated method stub
        super.finish();
    }
}