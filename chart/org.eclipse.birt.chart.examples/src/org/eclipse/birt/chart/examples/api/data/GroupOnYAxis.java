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

package org.eclipse.birt.chart.examples.api.data;

import java.util.Vector;
import java.util.ArrayList;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.log.impl.DefaultLoggerImpl;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.PaintListener;


/**
 * Presents a bar chart with mulitple Y series, which could be acheived in the report designer as follows:
 * Chart Builder -> Data -> Y Axis -> Set: Series Grouping Key 
 * 
 */

public class GroupOnYAxis implements PaintListener {
	
    private IDeviceRenderer idr = null;

    private Chart cm = null;   
    
    /**
     * execute application
     * @param args
     */
    public static void main(String[] args)
    {
    	GroupOnYAxis scv = new GroupOnYAxis();
        GridLayout gl = new GridLayout();
        gl.numColumns = 1;
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setSize(900, 700);
        shell.setLayout(gl);
        shell.setText(scv.getClass().getName() + " [device="+scv.idr.getClass().getName()+"]");

        GridData gd = new GridData(GridData.FILL_BOTH);
        Canvas canvas = new Canvas(shell, SWT.NONE);
        canvas.setLayoutData(gd);
        canvas.addPaintListener(scv);
        
        shell.open();
        
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
    }
    
    /**
     *Constructor
     */
    GroupOnYAxis()
    {
        final PluginSettings ps = PluginSettings.instance();
        try {
            idr = ps.getDevice("dv.SWT");
        } catch (ChartException pex)
        {
            DefaultLoggerImpl.instance().log(pex);
        }
        cm = createBarChart();
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
        } catch (ChartException gex)
        {
            gex.printStackTrace();
        } 
     }

	/**
	 * Creates a bar chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		
		//Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());		
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(255, 255, 225), -35, false));
		p.getOutline().setVisible(true);
		
		//Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart with Grouping on Y Axis");
		
		//Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		
		//X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);		
		xAxisPrimary.getTitle().getCaption().setValue("Regional Markets");		
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);

		//Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue("Monthly Sales ($Million)");
		
		//Data Set
		Vector vs = new Vector();
		vs.add("Europe");
		vs.add("Asia");
		vs.add("North America");
		
		TextDataSet categoryValues = TextDataSetImpl.create(vs);
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] {
				26.17, 30.21, 21.5});
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] {
				17.81, 13.55, 19.26});
		
		ArrayList orthoValues = new ArrayList();
		orthoValues.add(orthoValues1);
		orthoValues.add(orthoValues2);

		ArrayList month = new ArrayList();
		month.add("July");
		month.add("August");
		
		//X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);
		
		//Y-Series
		ArrayList series = new ArrayList();
		ArrayList barS = new ArrayList();
		for (int i = 0; i < month.size(); i++){
			BarSeries bar = (BarSeries) BarSeriesImpl.create();
			barS.add(bar);
			SeriesDefinition sd = SeriesDefinitionImpl.create();
			series.add(sd);
		}
		
		for (int i = 0; i < month.size(); i++){
			((BarSeries)barS.get(i)).setSeriesIdentifier(month.get(i));
			((BarSeries)barS.get(i)).setDataSet((NumberDataSet)orthoValues.get(i));
			((BarSeries)barS.get(i)).setRiserOutline(null);
			((BarSeries)barS.get(i)).getLabel().setVisible(true);
			((BarSeries)barS.get(i)).setLabelPosition(Position.INSIDE_LITERAL); 
			
		    ((SeriesDefinition)series.get(i)).getSeriesPalette().update(i);
			yAxisPrimary.getSeriesDefinitions().add(series.get(i));
			((SeriesDefinition)series.get(i)).getSeries().add(barS.get(i));
		}
		
		return cwaBar;
	}
}

