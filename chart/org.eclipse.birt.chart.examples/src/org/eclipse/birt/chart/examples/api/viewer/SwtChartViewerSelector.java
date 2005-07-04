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
package org.eclipse.birt.chart.examples.api.viewer;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Presents the selector for a various of charts in SWT.
 */
public final class SwtChartViewerSelector implements PaintListener, SelectionListener
{
    /**
     * 
     */
    private IDeviceRenderer idr = null;
    
    /**
     * 
     */
    private Chart cm = null;
    
    /**
     * 
     */
    private Combo cb = null;
    
    /**
     * 
     */
    private Canvas ca = null;
    
    /**
     * 
     */
    private Button btn = null, cbPercent, cbLogarithmic, cbTransposed;
    
    /**
     * 
     */
    private Combo cbLegendType = null, cbDimension = null;
    
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        SwtChartViewerSelector scv = new SwtChartViewerSelector();
        GridLayout gl = new GridLayout();
        gl.numColumns = 1;
        Display d = Display.getDefault();
        Shell sh = new Shell(d);
        sh.setSize(900, 700);
        sh.setLayout(gl);
        sh.setText(scv.getClass().getName() + " [device="+scv.idr.getClass().getName()+"]");

        GridData gd = new GridData(GridData.FILL_BOTH);
        Canvas cCenter = new Canvas(sh, SWT.NONE);
        cCenter.setLayoutData(gd);
        cCenter.addPaintListener(scv);
        
        Composite cBottom = new Composite(sh, SWT.NONE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        cBottom.setLayoutData(gd);
        cBottom.setLayout(new RowLayout());
        
        Label la = new Label(cBottom, SWT.NONE);
        la.setText("Choose: ");
        Combo cbType = new Combo(cBottom, SWT.DROP_DOWN | SWT.READ_ONLY);
        cbType.add("Empty Chart w/Axes");
        cbType.add("Simple Bar");
        cbType.add("Simple Bar (Multiple Series)");
        cbType.add("Simple Pie Chart");
        cbType.add("Simple Line Chart");
        cbType.add("Percent Stackable Chart");
        cbType.add("Bar/Line Stacked Combination");
        cbType.add("Numeric Scatter Chart");
        cbType.add("Date Scatter Chart");
        cbType.add("Stock Chart");
        cbType.add("Multiple Pie Series");
        cbType.add("Complex Combination Chart");
        cbType.add("Bar Stacked Logarithmic Chart");
        cbType.add("Line Stacked Logarithmic Chart");
        cbType.add("DateValue Bar Chart containing Grid");
        cbType.add("Grid");
        cbType.add("Fill");
        cbType.add("ImageFill");
        cbType.add("Scale");
        cbType.add("MakerLine1");
        cbType.add("Makerline2");
        cbType.add("MarkerRange");
        cbType.add("DataPoint");
        cbType.add("MarkerRange2");
        cbType.add("LeaderLine and Explosion");
        cbType.select(1);
        
        Combo cbDimension = new Combo(cBottom, SWT.DROP_DOWN | SWT.READ_ONLY);
        cbDimension.add("2D");
        cbDimension.add("2D with depth");
        cbDimension.select(0);
        
        Button cbTransposed = new Button(cBottom, SWT.CHECK);
        cbTransposed.setText("Transposed");
        
        Button cbPercent = new Button(cBottom, SWT.CHECK);
        cbPercent.setText("Percent");
        
        Button cbLogarithmic = new Button(cBottom, SWT.CHECK);
        cbLogarithmic.setText("Logarithmic");
        
        Combo cbLegendType = new Combo(cBottom, SWT.DROP_DOWN | SWT.READ_ONLY);
        cbLegendType.add("Legend @ Series");
        cbLegendType.add("Legend @ Categories");
        cbLegendType.select(0);

        Button btn = new Button(cBottom, SWT.NONE);
        btn.setText("Update");
        btn.addSelectionListener(scv);
        scv.cb = cbType;
        scv.ca = cCenter;
        scv.btn = btn;
        
        scv.cbDimension = cbDimension;
        scv.cbTransposed = cbTransposed;
        scv.cbPercent = cbPercent;
        scv.cbLogarithmic = cbLogarithmic;
        scv.cbLegendType = cbLegendType;
        
        sh.open();
        
        while (!sh.isDisposed())
        {
            if (!d.readAndDispatch())
            {
                d.sleep();
            }
        }
    }
    
    /**
     *
     */
    SwtChartViewerSelector()
    {
        final PluginSettings ps = PluginSettings.instance();
        try {
            idr = ps.getDevice("dv.SWT");
        } catch (ChartException ex)
        {
            DefaultLoggerImpl.instance().log(ex);
        }
//        cm = SampleCharts.createSimplePieChartLabelShadow();
        cm = PrimitiveCharts.createSimplePieChart();
        
    }
    

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
     */
    public final void paintControl(PaintEvent pe)
    {
        idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, pe.gc);
        
        Composite co = (Composite) pe.getSource();
        Rectangle re = co.getClientArea();
        Bounds bo = BoundsImpl.create(re.x, re.y, re.width, re.height);
        bo.scale(72d/idr.getDisplayServer().getDpiResolution());
        
        Generator gr = Generator.instance();
        try {
            gr.render(
                idr, 
                gr.build(
                    idr.getDisplayServer(), 
                    cm, null,
                    bo,
                    null
                )
            );
        } catch (ChartException ex)
        {
            showException(pe.gc, ex);
        } 
     }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        int iSelection = cb.getSelectionIndex();
        switch(iSelection)
        {
	    	case 0:
	    	    cm = PrimitiveCharts.createSimplePieChartDataPoint();
	    	    break;
	    	case 1:
	    	    cm = PrimitiveCharts.createSimpleBarChart();
	    	    break;
	    	case 2:
	    	    cm = PrimitiveCharts.createMultiBarChart();
	    	    break;
	    	case 3:
	    	    cm = PrimitiveCharts.createSimplePieChart();
	    	    break;
	    	case 4:
	    	    cm = PrimitiveCharts.createSimpleLineChart();
	    	    break;
	    	case 5:
	    	    cm = PrimitiveCharts.createPercentStackedChart();
	    	    break;
	    	case 6:
	    	    cm = PrimitiveCharts.createStackedChart();
	    	    break;
	    	case 7:
	    	    cm = PrimitiveCharts.createNumericScatterChart();
	    	    break;
	    	case 8:
	    	    cm = PrimitiveCharts.createDateScatterChart();
	    	    break;
	    	case 9:
	    	    cm = PrimitiveCharts.createStockChart();
	    	    break;
	    	case 10:
	    	    cm = PrimitiveCharts.createMultiPieChart();
	    	    break;
	    	case 11:
	    	    cm = PrimitiveCharts.createCombinationChart();
	    	    break;
	    	case 12:
	    	    cm = PrimitiveCharts.createLogarithmicStackedBarChart();
	    	    break;
	    	case 13:
	    	    cm = PrimitiveCharts.createLogarithmicStackedLineChart();
	    	    break;
	    	case 14:
        	    cm = PrimitiveCharts.createDateValueBarChartGrid();
        	    break;
	    	case 15:
        	    cm = PrimitiveCharts.createDateValueBarChartFillImage();
        	    break;
        	case 16:
        	    cm = PrimitiveCharts.createDateValueBarChartFill();
        	    break;
        	case 17:
        	    cm = PrimitiveCharts.createLinearStackedLineChartScale();
        	    break;
        	case 18:
        	    cm = PrimitiveCharts.createSimpleLineChartMarkerLine();
        	    break;
        	case 19:
        	    cm = PrimitiveCharts.createDateValueBarChartMarkerLine();
        	    break;
        	case 20:
        	    cm = PrimitiveCharts.createDateValueBarChartScaleType();
        	    break;
        	case 21:
        	    cm = PrimitiveCharts.createSimplePieChartTitleLabel();
        	    break;
        	case 22:
        	    cm = PrimitiveCharts.createSimplePieChartDataPoint();
        	    break;
        	case 23:
        	    cm = PrimitiveCharts.createNumericScatterChartMarker();
        	    break;
        	case 24:
        	    cm = PrimitiveCharts.createSimplePieChartExplosionLeaderLine();
        	    break;
        }

        if (cm instanceof ChartWithAxes)
        {
            ChartWithAxes cwa = ((ChartWithAxes) cm);
            cwa.setTransposed(cbTransposed.getSelection());
            Axis ax = cwa.getPrimaryOrthogonalAxis(cwa.getPrimaryBaseAxes()[0]);
            
            if (cbLogarithmic.getSelection())
            {
                if (ax.getType() == AxisType.LINEAR_LITERAL)
                {
                    ax.setType(AxisType.LOGARITHMIC_LITERAL);
                }
            }
            else
            {
                if (ax.getType() == AxisType.LOGARITHMIC_LITERAL)
                {
                    ax.setType(AxisType.LINEAR_LITERAL);
                }
            }
            
            ax.setPercent(cbPercent.getSelection());
            if (ax.isPercent())
            {
                ax.setFormatSpecifier(JavaNumberFormatSpecifierImpl.create("0'%'"));
            }
            else
            {
                ax.setFormatSpecifier(null);
            }
        }
        
        iSelection = cbDimension.getSelectionIndex();
        switch(iSelection)
        {
        	case 0:
        	    cm.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
        	    break;
        	case 1:
        	    cm.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
        	    break;
        }

        iSelection = cbLegendType.getSelectionIndex();
        switch(iSelection)
        {
        	case 0:
        	    cm.getLegend().setItemType(LegendItemType.SERIES_LITERAL);
        	    break;
        	case 1:
        	    cm.getLegend().setItemType(LegendItemType.CATEGORIES_LITERAL);
        	    break;
        }
        
        ca.redraw();
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub
        
    }
    
  
    private final void showException(GC g2d, Exception ex)
    {
        String sWrappedException = ex.getClass().getName();
        Throwable th = ex;
        while (ex.getCause() != null)
        {
            ex = (Exception) ex.getCause();
        }
        String sException = ex.getClass().getName();
        if (sWrappedException.equals(sException))
        {
            sWrappedException = null;
        }
        
        String sMessage = null;
        if (th instanceof BirtException)
        {
            sMessage = ((BirtException) th).getLocalizedMessage(); 
        }
        else
        {
            sMessage = ex.getMessage();
        }
        
        if (sMessage == null)
        {
            sMessage = "<null>";
        }
        StackTraceElement[] stea = ex.getStackTrace();
        Point d = ca.getSize();
        
        Device dv = Display.getCurrent();
        Font fo = new Font(dv, "Courier", SWT.BOLD, 16);
        g2d.setFont(fo);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setBackground(dv.getSystemColor(SWT.COLOR_WHITE));
        g2d.fillRectangle(20, 20, d.x - 40, d.y - 40);
        g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLACK));
        g2d.drawRectangle(20, 20, d.x - 40, d.y - 40);
        g2d.setClipping(20, 20, d.x - 40, d.y - 40);
        int x = 25, y = 20 + fm.getHeight();
        g2d.drawString("Exception:", x, y);
        x += g2d.textExtent("Exception:").x + 5;
        g2d.setForeground(dv.getSystemColor(SWT.COLOR_RED));
        g2d.drawString(sException, x, y); x = 25; y += fm.getHeight();
        if (sWrappedException != null)
        {
            g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLACK));
	        g2d.drawString("Wrapped In:", x, y);
	        x += g2d.textExtent("Wrapped In:").x + 5;
	        g2d.setForeground(dv.getSystemColor(SWT.COLOR_RED));
	        g2d.drawString(sWrappedException, x, y); x = 25; y += fm.getHeight();
        }
        g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLACK)); y += 10;
        g2d.drawString("Message:", x, y);
        x += g2d.textExtent("Message:").x + 5;
        g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLUE));
        g2d.drawString(sMessage, x, y); x = 25; y += fm.getHeight();
        g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLACK)); y += 10;
        g2d.drawString("Trace:", x, y); x = 40; y += fm.getHeight();
        g2d.setForeground(dv.getSystemColor(SWT.COLOR_DARK_GREEN));
        for (int i = 0; i < stea.length; i++)
        {
            g2d.drawString(stea[i].getClassName() + ":" + stea[i].getMethodName() + "(...):" + stea[i].getLineNumber(), x, y); x = 40; y += fm.getHeight();
        }
        fo.dispose();
    }
}
