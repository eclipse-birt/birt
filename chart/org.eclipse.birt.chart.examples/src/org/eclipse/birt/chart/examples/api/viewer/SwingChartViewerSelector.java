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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;

/**
 * Presents the selector for a various of charts in a Swing JPanel
 * 
 * @author Actuate Corporation
 */
public final class SwingChartViewerSelector extends JPanel implements IUpdateNotifier, ComponentListener
{
    /**
     * 
     */
    private boolean bNeedsGeneration = true;
    
    /**
     * 
     */
    private GeneratedChartState gcs = null;

    /**
     * 
     */
    private Chart cm = null;
    
    /**
     * 
     */
    private IDeviceRenderer idr = null;
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        SwingChartViewerSelector scv = new SwingChartViewerSelector();
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.addComponentListener(scv);
        
        Container co = jf.getContentPane();
        co.setLayout(new BorderLayout());
        co.add(scv, BorderLayout.CENTER);
        
        // CENTER WINDOW ON SCREEN
        Dimension dScreen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dApp = new Dimension(800, 700);
        jf.setSize(dApp);
        jf.setLocation((dScreen.width - dApp.width)/2, (dScreen.height - dApp.height) / 2);
        jf.setTitle(scv.getClass().getName() + " [device="+scv.idr.getClass().getName()+"]");

        ControlPanel cp = scv.new ControlPanel(scv);
        co.add(cp, BorderLayout.SOUTH);
        
        jf.show();
    }
    
    /**
     * 
     */
    SwingChartViewerSelector()
    {
        final PluginSettings ps = PluginSettings.instance();
        try {
            idr = ps.getDevice("dv.SWING");
        } catch (ChartException ex)
        {
            DefaultLoggerImpl.instance().log(ex);
        }
        cm = PrimitiveCharts.createNumericScatterChartMarker();
    }
    

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
     */
    public void regenerateChart()
    {
        bNeedsGeneration = true;
        repaint();
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
     */
    public void repaintChart()
    {
        repaint();
    }


    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#peerInstance()
     */
    public Object peerInstance()
    {
        return this;
    }
    

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getDesignTimeModel()
     */
    public Chart getDesignTimeModel()
    {
        return cm;
    }


    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getRunTimeModel()
     */
    public Chart getRunTimeModel()
    {
        return gcs.getChartModel();
    }
    
    /**
     * 
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
        idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, this);
        Dimension d = getSize();
        Bounds bo = BoundsImpl.create(0, 0, d.width, d.height);
        bo.scale(72d/idr.getDisplayServer().getDpiResolution()); // BOUNDS MUST BE SPECIFIED IN POINTS
        
        Generator gr = Generator.instance();
        if (bNeedsGeneration)
        {
            bNeedsGeneration = false;
            try {
	            gcs = gr.build(
	                idr.getDisplayServer(), 
	                cm, null,
	                bo,
	                null
	            );
	        } catch (ChartException ex)
	        {
	            showException(g2d, ex);
	        }
        }
        
        try {
            gr.render(
                idr,
                gcs
            );
        } catch (ChartException ex)
        {
            showException(g2d, ex);
        }
    }
    
    /**
     * 
     * @param g2d
     * @param ex
     */
    private final void showException(Graphics2D g2d, Exception ex)
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
        Dimension d = getSize();
        
        Font fo = new Font("Monospaced", Font.BOLD, 14);
        g2d.setFont(fo);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(20, 20, d.width - 40, d.height - 40);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(20, 20, d.width - 40, d.height - 40);
        g2d.setClip(20, 20, d.width - 40, d.height - 40);
        int x = 25, y = 20 + fm.getHeight();
        g2d.drawString("Exception:", x, y);
        x += fm.stringWidth("Exception:") + 5;
        g2d.setColor(Color.RED);
        g2d.drawString(sException, x, y); x = 25; y += fm.getHeight();
        if (sWrappedException != null)
        {
	        g2d.setColor(Color.BLACK);
	        g2d.drawString("Wrapped In:", x, y);
	        x += fm.stringWidth("Wrapped In:") + 5;
	        g2d.setColor(Color.RED);
	        g2d.drawString(sWrappedException, x, y); x = 25; y += fm.getHeight();
        }
        g2d.setColor(Color.BLACK); y += 10;
        g2d.drawString("Message:", x, y);
        x += fm.stringWidth("Message:") + 5;
        g2d.setColor(Color.BLUE);
        g2d.drawString(sMessage, x, y); x = 25; y += fm.getHeight();
        g2d.setColor(Color.BLACK); y += 10;
        g2d.drawString("Trace:", x, y); x = 40; y += fm.getHeight();
        g2d.setColor(Color.GREEN.darker());
        for (int i = 0; i < stea.length; i++)
        {
            g2d.drawString(stea[i].getClassName() + ":" + stea[i].getMethodName() + "(...):" + stea[i].getLineNumber(), x, y); x = 40; y += fm.getHeight();
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(ComponentEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public void componentMoved(ComponentEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public void componentResized(ComponentEvent e)
    {
        bNeedsGeneration = true;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(ComponentEvent e)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 
     */
    private final class ControlPanel extends JPanel implements ActionListener
    {
        private JComboBox jcbModels = null; 
        private JButton jbUpdate = null;
        private JComboBox jcbDimensions = null;
        private JCheckBox jcbTransposed = null;
        private JCheckBox jcbPercent = null;
        private JCheckBox jcbLogarithmic = null;
        private JComboBox jcbLegendType = null;
        private JComboBox jcbLegendOrientation = null;
        private JComboBox jcbLegendDirection = null;
        private JComboBox jcbLegendLocation = null;
        private JComboBox jcbLegendAnchor = null;
        
        private final SwingChartViewerSelector scv;
        
        ControlPanel(SwingChartViewerSelector scv)
        {
            this.scv = scv;
            
            setLayout(new GridLayout(0, 1, 0, 0));
            
            JPanel jp1 = new JPanel(); 
            jp1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

            jp1.add(new JLabel("Choose:"));
            jcbModels = new JComboBox();
            jcbModels.addItem("Empty Chart w/Axes");
            jcbModels.addItem("Simple Bar");
            jcbModels.addItem("Simple Bar (2 Series)");
            jcbModels.addItem("Simple Pie");
            jcbModels.addItem("Simple Line");
            jcbModels.addItem("Percent Stackable");
            jcbModels.addItem("Bar/Line Stacked");
            jcbModels.addItem("Numeric Scatter");
            jcbModels.addItem("Date Scatter");
            jcbModels.addItem("Stock Chart");
            jcbModels.addItem("Multiple Pie Series");
            jcbModels.addItem("Complex Combination");
            jcbModels.addItem("Bar Stacked Logarithmic");
            jcbModels.addItem("Line Stacked Logarithmic");
            jcbModels.addItem("DateValue Bar Chart containing Grid");
            jcbModels.addItem("Grid");
            jcbModels.addItem("Fill");
            jcbModels.addItem("ImageFill");
            jcbModels.addItem("Scale");
            jcbModels.addItem("MakerLine1");
            jcbModels.addItem("Makerline2");
            jcbModels.addItem("MarkerRange");
            jcbModels.addItem("DataPoint");
            jcbModels.addItem("MarkerRange2");
            jcbModels.addItem("LeaderLine and Explosion");
            
            jcbModels.setSelectedIndex(1);
            jp1.add(jcbModels);

            jcbDimensions = new JComboBox();
            jcbDimensions.addItem("2D");
            jcbDimensions.addItem("2.5D");
            jp1.add(jcbDimensions);
            
            jcbTransposed = new JCheckBox("Transposed", false);
            jp1.add(jcbTransposed);
            
            jcbPercent = new JCheckBox("Percent", false);
            jp1.add(jcbPercent);

            jcbLogarithmic = new JCheckBox("Logarithmic", false);
            jp1.add(jcbLogarithmic);
            
            jbUpdate = new JButton("Update");
            jbUpdate.addActionListener(this);
            jp1.add(jbUpdate);
            
            add(jp1);

            JPanel jp2 = new JPanel(); 
            jp2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            jp2.add(new JLabel("Legend attributes: "));
            jcbLegendType = new JComboBox();
            jcbLegendType.addItem("Group by Series");
            jcbLegendType.addItem("Group by Categories");
            jp2.add(jcbLegendType);
            
            jcbLegendOrientation = new JComboBox();
            jcbLegendOrientation.addItem("Vertical");
            jcbLegendOrientation.addItem("Horizontal");
            jp2.add(jcbLegendOrientation);
            
            jcbLegendDirection = new JComboBox();
            jcbLegendDirection.addItem("Top => Bottom");
            jcbLegendDirection.addItem("Left => Right");
            jp2.add(jcbLegendDirection);
            
            jcbLegendLocation = new JComboBox();
            jcbLegendLocation.addItem("Above");
            jcbLegendLocation.addItem("Below");
            jcbLegendLocation.addItem("Left");
            jcbLegendLocation.addItem("Right");
            jcbLegendLocation.addItem("Inside");
            jcbLegendLocation.setSelectedIndex(3);
            jp2.add(jcbLegendLocation);
            
            jcbLegendAnchor = new JComboBox();
            jcbLegendAnchor.addItem("North");
            jcbLegendAnchor.addItem("NorthEast");
            jcbLegendAnchor.addItem("NorthWest");
            jcbLegendAnchor.addItem("South");
            jcbLegendAnchor.addItem("SouthEast");
            jcbLegendAnchor.addItem("SouthWest");
            jcbLegendAnchor.addItem("East");
            jcbLegendAnchor.addItem("West");
            jcbLegendAnchor.addItem("Center");
            jcbLegendAnchor.setSelectedIndex(8);
            jp2.add(jcbLegendAnchor);
            
            add(jp2);
            
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
         */
        public void componentHidden(ComponentEvent cev)
        {
            setVisible(false);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
         */
        public void componentMoved(ComponentEvent cev)
        {
            JFrame jf = (JFrame) cev.getComponent();
            Rectangle r = jf.getBounds();
            setLocation(r.x, r.y + r.height);
            setSize(r.width, 50);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
         */
        public void componentResized(ComponentEvent cev)
        {
            JFrame jf = (JFrame) cev.getComponent();
            Rectangle r = jf.getBounds();
            setLocation(r.x, r.y + r.height);
            setSize(r.width, 50);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
         */
        public void componentShown(ComponentEvent cev)
        {
            JFrame jf = (JFrame) cev.getComponent();
            Rectangle r = jf.getBounds();
            setLocation(r.x, r.y + r.height);
            setSize(r.width, 50);
            setVisible(true);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
            int i = jcbModels.getSelectedIndex();
            cm = null;
            switch(i)
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
                cwa.setTransposed(jcbTransposed.isSelected());
                Axis ax = cwa.getPrimaryOrthogonalAxis(cwa.getPrimaryBaseAxes()[0]);
                
                if (jcbLogarithmic.isSelected())
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
                
                ax.setPercent(jcbPercent.isSelected());
                if (ax.isPercent())
                {
                    ax.setFormatSpecifier(JavaNumberFormatSpecifierImpl.create("0'%'"));
                }
                else
                {
                    ax.setFormatSpecifier(null);
                }
            }
            
            i = jcbDimensions.getSelectedIndex();
            switch(i)
            {
            	case 0:
            	    cm.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
            	    break;
            	case 1:
            	    cm.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
            	    break;
            }

            i = jcbLegendType.getSelectedIndex();
            Legend lg = cm.getLegend();
            switch(i)
            {
            	case 0:
            	    lg.setItemType(LegendItemType.SERIES_LITERAL);
            	    break;
            	case 1:
            	    lg.setItemType(LegendItemType.CATEGORIES_LITERAL);
            	    break;
            }

            i = jcbLegendOrientation.getSelectedIndex();
            switch(i)
            {
            	case 0:
            	    lg.setOrientation(Orientation.VERTICAL_LITERAL);
            	    break;
            	case 1:
            	    lg.setOrientation(Orientation.HORIZONTAL_LITERAL);
            	    break;
            }

            i = jcbLegendDirection.getSelectedIndex();
            switch(i)
            {
            	case 0:
            	    lg.setDirection(Direction.TOP_BOTTOM_LITERAL);
            	    break;
            	case 1:
            	    lg.setDirection(Direction.LEFT_RIGHT_LITERAL);
            	    break;
            }
            
            i = jcbLegendLocation.getSelectedIndex();
            switch(i)
            {
            	case 0:
            	    lg.setPosition(Position.ABOVE_LITERAL);
            	    break;
            	case 1:
            	    lg.setPosition(Position.BELOW_LITERAL);
            	    break;
            	case 2:
            	    lg.setPosition(Position.LEFT_LITERAL);
            	    break;
            	case 3:
            	    lg.setPosition(Position.RIGHT_LITERAL);
            	    break;
            	case 4:
            	    lg.setPosition(Position.INSIDE_LITERAL);
            	    break;
            }
            
            i = jcbLegendAnchor.getSelectedIndex();
            switch(i)
            {
            	case 0:
            	    lg.setAnchor(Anchor.NORTH_LITERAL);
            	    break;
            	case 1:
            	    lg.setAnchor(Anchor.NORTH_EAST_LITERAL);
            	    break;
            	case 2:
            	    lg.setAnchor(Anchor.NORTH_WEST_LITERAL);
            	    break;
            	case 3:
            	    lg.setAnchor(Anchor.SOUTH_LITERAL);
            	    break;
            	case 4:
            	    lg.setAnchor(Anchor.SOUTH_EAST_LITERAL);
            	    break;
            	case 5:
            	    lg.setAnchor(Anchor.SOUTH_WEST_LITERAL);
            	    break;
            	case 6:
            	    lg.setAnchor(Anchor.EAST_LITERAL);
            	    break;
            	case 7:
            	    lg.setAnchor(Anchor.WEST_LITERAL);
            	    break;
            	case 8:
            	    lg.unsetAnchor();
            	    break;
            }
            bNeedsGeneration = true;
            scv.repaint();
        }
    }
}