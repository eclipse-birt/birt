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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.report.model.extension.ExtendedElementException;
import org.eclipse.birt.report.model.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.extension.IReportItem;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author Actuate Corporation
 *  
 */
public final class ChartReportItemImpl implements IReportItem
{
    static
    {
        DefaultLoggerImpl.instance().setVerboseLevel(ILogger.ERROR);
    };

    private Chart cm = null;

    public ChartReportItemImpl()
    {
    }

    public void setModel(Chart cm)
    {
        this.cm = cm;
    }

    public final Chart getModel()
    {
        return cm;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#serialize(java.lang.String)
     */
    public ByteArrayOutputStream serialize(String propName)
    {
        if (propName != null && propName.equalsIgnoreCase("xmlRepresentation"))
        {
            try
            {
                return SerializerImpl.instance().asXml(cm, true);
            }
            catch (IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return new ByteArrayOutputStream();
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#deserialize(java.lang.String, java.io.ByteArrayInputStream)
     */
    public void deserialize(String propName, ByteArrayInputStream data) throws ExtendedElementException
    {
        if (propName != null && propName.equalsIgnoreCase("xmlRepresentation"))
        {
            try
            {
                cm = SerializerImpl.instance().fromXml(data, true);
            }
            catch (IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                cm = null;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#getPropertyDefinitions()
     */
    public IPropertyDefinition[] getPropertyDefinitions()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#getProperty(java.lang.String)
     */
    public Object getProperty(String propName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#checkProperty(java.lang.String, java.lang.Object)
     */
    public void checkProperty(String propName, Object value) throws ExtendedElementException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(String propName, Object value)
    {
        System.out.println("Attempt to set property " + propName + " with value " + value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#validate()
     */
    public void validate() throws ExtendedElementException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#copy()
     */
    public final IReportItem copy()
    {
        final ChartReportItemImpl newItem = new ChartReportItemImpl();
        newItem.setModel((Chart) EcoreUtil.copy(cm));
        return newItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#refreshPropertyDefinition()
     */
    public boolean refreshPropertyDefinition()
    {
        // TODO Auto-generated method stub
        return false;
    }

}