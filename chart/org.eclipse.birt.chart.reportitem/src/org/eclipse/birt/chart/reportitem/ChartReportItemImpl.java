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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStack;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IChoiceDefinition;
import org.eclipse.birt.report.model.api.extension.IElementCommand;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItem;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author Actuate Corporation
 *  
 */
public final class ChartReportItemImpl extends ReportItem
{
    private Chart cm = null;

    private Object oDesignerRepresentation = null;

    private static final List liLegendPositions = new LinkedList();

    private static final List liLegendAnchors = new LinkedList();

    private static final List liChartDimensions = new LinkedList();

    private transient DesignElementHandle handle = null;

    static
    {
        // SUPPRESS EVERYTHING EXCEPT FOR ERRORS
        //DefaultLoggerImpl.instance().setVerboseLevel(ILogger.ERROR);

        // SETUP LEGEND POSITION CHOICE LIST
        List li = Position.VALUES;
        Iterator it = li.iterator();
        IChoiceDefinition icd;
        String sName, sLowercaseName;
        while (it.hasNext())
        {
            sName = ((Position) it.next()).getName();
            sLowercaseName = sName.toLowerCase(Locale.US);
            if (sLowercaseName.equals("outside")) // DISALLOWED FOR LEGEND POSITION
            {
                continue;
            }
            icd = new ChartChoiceDefinitionImpl("choice.legend.position." + sLowercaseName, sName, null);
            liLegendPositions.add(icd);
        }

        // SETUP LEGEND ANCHOR CHOICE LIST
        li = Anchor.VALUES;
        it = li.iterator();
        while (it.hasNext())
        {
            sName = ((Anchor) it.next()).getName();
            sLowercaseName = sName.toLowerCase(Locale.US);
            icd = new ChartChoiceDefinitionImpl("choice.legend.anchor." + sLowercaseName, sName, null);
            liLegendAnchors.add(icd);
        }

        // SETUP CHART DIMENSION CHOICE LIST
        li = ChartDimension.VALUES;
        it = li.iterator();
        while (it.hasNext())
        {
            sName = ((ChartDimension) it.next()).getName();
            sLowercaseName = sName.toLowerCase(Locale.US);
            icd = new ChartChoiceDefinitionImpl("choice.chart.dimension." + sLowercaseName, sName, null);
            liChartDimensions.add(icd);
        }
    };

    /**
     *  
     */
    public ChartReportItemImpl(DesignElementHandle handle)
    {
        this.handle = handle;
    }

    /**
     * Set the chart directly (no command)
     */
    public void setModel( Chart chart )
    {
        this.cm = chart;
    }
    /**
     * Set the new chart through a command for command stack integration
     */
    public void executeSetModelCommand(ExtendedItemHandle eih, Chart oldChart, Chart newChart)
    {
        if (handle == null)
        {
            return;
        }
        IElementCommand command = new ChartElementCommandImpl(eih, this, oldChart, newChart);
        // TODO Cast to be removed when CommandStack has the execute(IElementCommand) method
        ((ActivityStack)this.handle.getDesignHandle().getCommandStack()).execute(command);
    }

    /**
     * 
     * @param oDesignerRepresentation
     */
    public final void setDesignerRepresentation(Object oDesignerRepresentation)
    {
        this.oDesignerRepresentation = oDesignerRepresentation;
    }

    /**
     * 
     * @return
     */
    public final Object getDesignerRepresentation()
    {
        return oDesignerRepresentation;
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
        if (cm == null)
        {
            DefaultLoggerImpl.instance().log(ILogger.WARNING,
                "Request for property definitions recieved before model was created.");
            return null;
        }
        final boolean bTransposed = (cm instanceof ChartWithAxes) ? ((ChartWithAxes) cm).isTransposed() : false;

        return new IPropertyDefinition[]
        {

            new ChartPropertyDefinitionImpl(null, "title.value", "property.title.value", false,
                PropertyType.STRING_TYPE, null, null, cm.getTitle().getLabel().getCaption().getValue()),

            new ChartPropertyDefinitionImpl(null, "title.font.rotation", "property.title.font.rotation", false,
                PropertyType.FLOAT_TYPE, null, null, new Double(cm.getTitle().getLabel().getCaption().getFont()
                    .getRotation())),

            /*
             * new ChartPropertyDefinitionImpl( null, "title.textColor", "property.title.textColor", false,
             * PropertyType.COLOR_TYPE, null, null, cm.getTitle().getLabel().getCaption().getColor()),
             */

            new ChartPropertyDefinitionImpl(null, "legend.position", "property.legend.position", false,
                PropertyType.CHOICE_TYPE, liLegendPositions, null, null),

            new ChartPropertyDefinitionImpl(null, "legend.anchor", "property.legend.anchor", false,
                PropertyType.CHOICE_TYPE, liLegendAnchors, null, null),

            new ChartPropertyDefinitionImpl(null, "chart.dimension", "property.chart.dimension", false,
                PropertyType.CHOICE_TYPE, liChartDimensions, null, null),

            new ChartPropertyDefinitionImpl(null, "plot.transposed", "property.chart.plot.transposed", false,
                PropertyType.BOOLEAN_TYPE, null, null, new Boolean(bTransposed)),
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#getProperty(java.lang.String)
     */
    public final Object getProperty(String propName)
    {
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "getProperty(...) - " + propName);
        if (propName.equals("title.value"))
        {
            return cm.getTitle().getLabel().getCaption().getValue();
        }
        else if (propName.equals("title.font.rotation"))
        {
            return new Double(cm.getTitle().getLabel().getCaption().getFont().getRotation());
        }
        else if (propName.equals("legend.position"))
        {
            return cm.getLegend().getPosition().getName();
        }
        else if (propName.equals("legend.anchor"))
        {
            return cm.getLegend().getAnchor().getName();
        }
        else if (propName.equals("chart.dimension"))
        {
            return cm.getDimension().getName();
        }
        else if (propName.equals("plot.transposed"))
        {
            return new Boolean((cm instanceof ChartWithAxes) ? ((ChartWithAxes) cm).isTransposed() : false);
        }
        else if (propName.equals("chart.instance"))
        {
            return cm;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#checkProperty(java.lang.String, java.lang.Object)
     */
    public void checkProperty(String propName, Object value) throws ExtendedElementException
    {
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
            "checkProperty(...) - " + propName + " with value " + value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(String propName, Object value)
    {
        System.out.println("ChartReportItem's setProperty called with property name " + propName);
        DefaultLoggerImpl.instance()
            .log(ILogger.INFORMATION, "setProperty(...) - " + propName + " with value " + value);
        if (propName.equals("title.value"))
        {
            cm.getTitle().getLabel().getCaption().setValue((String) value);
        }
        else if (propName.equals("title.font.rotation"))
        {
            cm.getTitle().getLabel().getCaption().getFont().setRotation(((Double) value).doubleValue());
        }
        else if (propName.equals("legend.position"))
        {
            cm.getLegend().setPosition(Position.get((String) value));
        }
        else if (propName.equals("legend.anchor"))
        {
            cm.getLegend().setAnchor(Anchor.get((String) value));
        }
        else if (propName.equals("chart.dimension"))
        {
            cm.setDimension(ChartDimension.get((String) value));
        }
        else if (propName.equals("plot.transposed"))
        {
            if (cm instanceof ChartWithAxes)
            {
                ((ChartWithAxes) cm).setTransposed(((Boolean) value).booleanValue());
            }
            else
            {
                DefaultLoggerImpl.instance()
                    .log(ILogger.ERROR, "Cannot set 'transposed' state on a chart without axes");
            }
        }
        else if (propName.equals("chart.instance"))
        {
            this.cm = (Chart) value;
        }
        // Add the command to the Activity Stack

        if (oDesignerRepresentation != null)
        {
            ((DesignerRepresentation) oDesignerRepresentation).setDirty(true);
        }
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
        final ChartReportItemImpl crii = new ChartReportItemImpl(handle);
        crii.setProperty("chart.instance", EcoreUtil.copy(cm));
        return crii;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElement#refreshPropertyDefinition()
     */
    public boolean refreshPropertyDefinition()
    {
        return false;
    }

    /**
     * 
     * @param sName
     * @return
     */
    private final String choiceNameToValue(String sName)
    {
        final int iLastDot = sName.lastIndexOf('.');
        if (iLastDot == -1 || sName.length() == 0)
        {
            DefaultLoggerImpl.instance().log(ILogger.ERROR, "Unexpected choice name found: " + sName);
            return sName;
        }
        final StringBuffer sb = new StringBuffer(sName.substring(iLastDot + 1));
        char c = sb.charAt(0);
        sb.setCharAt(0, Character.toUpperCase(c));
        int i = 1, iSlash;
        do
        {
            iSlash = sb.indexOf("_", i);
            if (iSlash >= 0 && iSlash < sb.length() - 1)
            {
                c = sb.charAt(iSlash + 1);
                sb.setCharAt(iSlash + 1, Character.toUpperCase(c));
                i = iSlash + 1;
            }
        }
        while (i < sb.length() && iSlash != -1);
        return sb.toString();
    }
}