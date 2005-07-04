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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.util.PluginSettings;

/**
 * Generates a combination of live chart (Line chart + bar chart) using a Swing JPanel.
 */

public final class SwingLiveChartViewer extends JPanel
{
    /**
     * A chart model instance
     */
    private Chart cm = null;
    
    /**
     * The swing rendering device
     */
    private IDeviceRenderer sri = null;
    
    /**
     * Maintains the structure of the chart for quick refresh
     */
    private GeneratedChartState gcs = null;
    
    /**
     * Used in building the chart for the first time
     */
    private boolean bFirstPaint = true;
    
    /**
     * The program entry point
     *  
     * @param args
     */
    public static void main(String[] args)
    {
        DefaultLoggerImpl.instance().setVerboseLevel(ILogger.ERROR | ILogger.FATAL);
        SwingLiveChartViewer scs = new SwingLiveChartViewer();
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /*jf.addWindowListener(
            new WindowAdapter()
            {
                public final void windowClosing(WindowEvent wev)
                {
                    System.exit(0);
                }
            }
        );*/
        Container co = jf.getContentPane();
        co.setLayout(new BorderLayout());
        co.add(scs, BorderLayout.CENTER);
        
        // CENTER WINDOW ON SCREEN
        Dimension dScreen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dApp = new Dimension(600, 480);
        jf.setSize(dApp);
        jf.setLocation((dScreen.width - dApp.width)/2, (dScreen.height - dApp.height) / 2);
        
        jf.setTitle(scs.getClass().getName() + " [device="+scs.sri.getClass().getName()+"]");
        jf.show();
    }
    
    /**
     * A default constructor
     */
    SwingLiveChartViewer()
    {
        final PluginSettings ps = PluginSettings.instance();
        try {
            sri = ps.getDevice("dv.SWING");
        } catch (ChartException ex)
        {
            DefaultLoggerImpl.instance().log(ex);
        }
        cm = PrimitiveCharts.createLiveChart();
    }
    
    /**
     * Called to refresh the panel that renders the chart 
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        sri.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
        Dimension d = getSize();
        Bounds bo = BoundsImpl.create(0, 0, d.width, d.height);
        bo.scale(72d/sri.getDisplayServer().getDpiResolution());
        
        final Generator gr = Generator.instance();
        if (bFirstPaint)
        {
            bFirstPaint = false;
            try {
		        gcs = gr.build(
		            sri.getDisplayServer(), 
		            cm, null,
		            bo,
		            null
		        );
            } catch (ChartException ex)
            {
                ex.printStackTrace();
            }
            
            Timer t = new Timer();
            t.schedule(new ChartRefresh(), 1000);
        }
        try {
            gr.render(
                sri, gcs 
            );
        } catch (ChartException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * A background thread that scrolls/refreshes the chart
     */
    private final class ChartRefresh extends TimerTask
    {
        /**
         * 
         */
        public final void run()
        {
            for (;;)
            {
	            final Generator gr = Generator.instance();
	            PrimitiveCharts.scrollData((ChartWithAxes) cm);
	            //repaint();
	            
	            // REFRESH
	            try {
	                // RESIZE
	                /*final Dimension d = getSize();
	                Bounds bo = gcs.getBounds();
	                bo.setWidth(d.width);
	                bo.setHeight(d.height);
	                bo.scale(72d/sri.getXServer().getDpiResolution());*/
	                
	                gr.refresh(gcs);
	            } catch (ChartException ex)
	            {
	                ex.printStackTrace();
	            }
	            repaint();
	            
	            // DELAY
	            try {
	                Thread.sleep(500);
	            } catch (InterruptedException iex)
	            {
	                iex.printStackTrace();
	            }
            }
        }
    }
}
