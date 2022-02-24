/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.chart.regression;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.ICallBackNotifier;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.CallBackValue;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;

/**
 * Regression description:
 * </p>
 * 3D line chart or area chart, set marker to icon, interactivity can not take
 * effect
 * </p>
 * Test description:
 * <p>
 * Set interactivity showtooltip to a scatter chart with local image marker
 * shaple, verify if the tooltip can be showed.
 * </p>
 */

public final class Regression_142687_swing extends JPanel implements ICallBackNotifier, ComponentListener {

	private static final long serialVersionUID = 1L;

	private boolean bNeedsGeneration = true;

	private GeneratedChartState gcs = null;

	private Chart cm = null;

	private IDeviceRenderer idr = null;

	private BufferedImage bi = null;

	private Map contextMap;

	/**
	 * Contructs the layout with a container for displaying chart and a control
	 * panel for selecting interactivity.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final Regression_142687_swing siv = new Regression_142687_swing();

		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.addComponentListener(siv);

		Container co = jf.getContentPane();
		co.setLayout(new BorderLayout());
		co.add(siv, BorderLayout.CENTER);

		Dimension dScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dApp = new Dimension(600, 400);
		jf.setSize(dApp);
		jf.setLocation((dScreen.width - dApp.width) / 2, (dScreen.height - dApp.height) / 2);

		jf.setTitle(siv.getClass().getName() + " [device=" //$NON-NLS-1$
				+ siv.idr.getClass().getName() + "]");//$NON-NLS-1$

		ControlPanel cp = siv.new ControlPanel(siv);
		co.add(cp, BorderLayout.SOUTH);

		siv.idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, siv);

		jf.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				siv.idr.dispose();
			}

		});

		jf.setVisible(true);
	}

	/**
	 * Get the connection with SWING device to render the graphics.
	 */
	Regression_142687_swing() {
		contextMap = new HashMap();

		final PluginSettings ps = PluginSettings.instance();
		try {
			idr = ps.getDevice("dv.SWING");//$NON-NLS-1$
		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = create3DLineChart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
	 */
	public void regenerateChart() {
		bNeedsGeneration = true;
		updateBuffer();
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
	 */
	public void repaintChart() {
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#peerInstance()
	 */
	public Object peerInstance() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getDesignTimeModel()
	 */
	public Chart getDesignTimeModel() {
		return cm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getRunTimeModel()
	 */
	public Chart getRunTimeModel() {
		return gcs.getChartModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.IUpdateNotifier#getContext(java.lang.Object)
	 */
	public Object getContext(Object key) {
		return contextMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.IUpdateNotifier#putContext(java.lang.Object,
	 * java.lang.Object)
	 */
	public Object putContext(Object key, Object value) {
		return contextMap.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.IUpdateNotifier#removeContext(java.lang.Object)
	 */
	public Object removeContext(Object key) {
		return contextMap.remove(key);
	}

	public void updateBuffer() {
		Dimension d = getSize();

		if (bi == null || bi.getWidth() != d.width || bi.getHeight() != d.height) {
			bi = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
		}

		Graphics2D g2d = (Graphics2D) bi.getGraphics();

		idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
		Bounds bo = BoundsImpl.create(0, 0, d.width, d.height);
		bo.scale(72d / idr.getDisplayServer().getDpiResolution()); // BOUNDS
		// MUST
		// BE
		// SPECIFIED
		// IN
		// POINTS

		Generator gr = Generator.instance();
		if (bNeedsGeneration) {
			bNeedsGeneration = false;
			try {
				gcs = gr.build(idr.getDisplayServer(), cm, bo, null, null, null);
			} catch (ChartException ex) {
				showException(g2d, ex);
			}
		}

		try {
			gr.render(idr, gcs);
		} catch (ChartException rex) {
			showException(g2d, rex);
		} finally {
			g2d.dispose();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);

		if (bi == null) {
			updateBuffer();
		}

		g.drawImage(bi, 0, 0, this);
	}

	/**
	 * Presents the Exceptions if the chart cannot be displayed properly.
	 * 
	 * @param g2d
	 * @param ex
	 */
	private final void showException(Graphics2D g2d, Exception ex) {
		String sWrappedException = ex.getClass().getName();
		Throwable th = ex;
		while (ex.getCause() != null) {
			ex = (Exception) ex.getCause();
		}
		String sException = ex.getClass().getName();
		if (sWrappedException.equals(sException)) {
			sWrappedException = null;
		}

		String sMessage = null;
		if (th instanceof BirtException) {
			sMessage = ((BirtException) th).getLocalizedMessage();
		} else {
			sMessage = ex.getMessage();
		}

		if (sMessage == null) {
			sMessage = "<null>";//$NON-NLS-1$
		}

		StackTraceElement[] stea = ex.getStackTrace();
		Dimension d = getSize();

		Font fo = new Font("Monospaced", Font.BOLD, 14);//$NON-NLS-1$
		g2d.setFont(fo);
		FontMetrics fm = g2d.getFontMetrics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(20, 20, d.width - 40, d.height - 40);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(20, 20, d.width - 40, d.height - 40);
		g2d.setClip(20, 20, d.width - 40, d.height - 40);
		int x = 25, y = 20 + fm.getHeight();
		g2d.drawString("Exception:", x, y);//$NON-NLS-1$
		x += fm.stringWidth("Exception:") + 5;//$NON-NLS-1$
		g2d.setColor(Color.RED);
		g2d.drawString(sException, x, y);
		x = 25;
		y += fm.getHeight();
		if (sWrappedException != null) {
			g2d.setColor(Color.BLACK);
			g2d.drawString("Wrapped In:", x, y);//$NON-NLS-1$
			x += fm.stringWidth("Wrapped In:") + 5;//$NON-NLS-1$
			g2d.setColor(Color.RED);
			g2d.drawString(sWrappedException, x, y);
			x = 25;
			y += fm.getHeight();
		}
		g2d.setColor(Color.BLACK);
		y += 10;
		g2d.drawString("Message:", x, y);//$NON-NLS-1$
		x += fm.stringWidth("Message:") + 5;//$NON-NLS-1$
		g2d.setColor(Color.BLUE);
		g2d.drawString(sMessage, x, y);
		x = 25;
		y += fm.getHeight();
		g2d.setColor(Color.BLACK);
		y += 10;
		g2d.drawString("Trace:", x, y);//$NON-NLS-1$
		x = 40;
		y += fm.getHeight();
		g2d.setColor(Color.GREEN.darker());
		for (int i = 0; i < stea.length; i++) {
			g2d.drawString(stea[i].getClassName() + ":"//$NON-NLS-1$
					+ stea[i].getMethodName() + "(...):"//$NON-NLS-1$
					+ stea[i].getLineNumber(), x, y);
			x = 40;
			y += fm.getHeight();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.
	 * ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.
	 * ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.
	 * ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		bNeedsGeneration = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.
	 * ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * An inner class Control Panel, which provides the interactive interface with
	 * the user.
	 */
	private final class ControlPanel extends JPanel implements ActionListener {

		private static final long serialVersionUID = 1L;

		private JComboBox jcbModels = null;

		private JButton jbUpdate = null;

		private final Regression_142687_swing siv;

		ControlPanel(Regression_142687_swing siv) {
			this.siv = siv;

			setLayout(new GridLayout(0, 1, 0, 0));

			JPanel jp = new JPanel();
			jp.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

			jp.add(new JLabel("Choose:"));//$NON-NLS-1$
			jcbModels = new JComboBox();

			jcbModels.addItem("3D Line Chart");
			jcbModels.setSelectedIndex(0);
			jp.add(jcbModels);

			jbUpdate = new JButton("Update");//$NON-NLS-1$
			jbUpdate.addActionListener(this);
			jp.add(jbUpdate);

			add(jp);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.
		 * ComponentEvent)
		 */
		public void componentHidden(ComponentEvent cev) {
			setVisible(false);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.
		 * ComponentEvent)
		 */
		public void componentMoved(ComponentEvent cev) {
			JFrame jf = (JFrame) cev.getComponent();
			Rectangle r = jf.getBounds();
			setLocation(r.x, r.y + r.height);
			setSize(r.width, 50);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.
		 * ComponentEvent)
		 */
		public void componentResized(ComponentEvent cev) {
			JFrame jf = (JFrame) cev.getComponent();
			Rectangle r = jf.getBounds();
			setLocation(r.x, r.y + r.height);
			setSize(r.width, 50);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.
		 * ComponentEvent)
		 */
		public void componentShown(ComponentEvent cev) {
			JFrame jf = (JFrame) cev.getComponent();
			Rectangle r = jf.getBounds();
			setLocation(r.x, r.y + r.height);
			setSize(r.width, 50);
			setVisible(true);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			int i = jcbModels.getSelectedIndex();
			cm = null;
			switch (i) {
			case 0:
				cm = create3DLineChart();
				break;
			}

			bNeedsGeneration = true;
			siv.updateBuffer();
			siv.repaint();
		}
	}

	public void callback(Object event, Object source, CallBackValue value) {
		JOptionPane.showMessageDialog(Regression_142687_swing.this, value.getIdentifier());
	}

	/**
	 * Creates a 3D Line chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart create3DLineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Chart Type
		cwaLine.setType("Line Chart"); //$NON-NLS-1$
		cwaLine.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);
		cwaLine.setRotation(
				Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.createY(45), Angle3DImpl.createX(-20), }));

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Z-Axis
		Axis zAxisPrimary = AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);
		zAxisPrimary.getTitle().getCaption().setValue("Z Axis Title"); //$NON-NLS-1$
		zAxisPrimary.getTitle().setVisible(false);
		zAxisPrimary.setPrimaryAxis(true);
		zAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);
		zAxisPrimary.setOrientation(Orientation.HORIZONTAL_LITERAL);
		zAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		zAxisPrimary.getOrigin().setValue(NumberDataElementImpl.create(0));
		zAxisPrimary.getTitle().setVisible(false);
		zAxisPrimary.setType(AxisType.TEXT_LITERAL);
		cwaLine.getPrimaryBaseAxes()[0].getAncillaryAxes().add(zAxisPrimary);

		cwaLine.getPrimaryOrthogonalAxis(cwaLine.getPrimaryBaseAxes()[0]).getTitle().getCaption().getFont()
				.setRotation(0);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { 15.29, -14.53, -47.05, 32.55 });
		TextDataSet dsStringValue1 = TextDataSetImpl.create(new String[] { "Actuate", "Microsoft" });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		ls.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ls.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(207, 41, 207), LineStyle.SOLID_LITERAL, 1));
		ls.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls.getLabel().setVisible(false);
		ls.setDataSet(dsNumericValues1);
		Marker marker = (Marker) ls.getMarkers().get(0);
		marker.setType(MarkerType.ICON_LITERAL);
		marker.setFill(ImageImpl.create("file:///" + System.getProperty("user.dir") + "/27.gif"));
		ls.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEMOVE_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(ls);

		LineSeries ls2 = (LineSeries) LineSeriesImpl.create();
		ls2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		ls2.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		ls2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls2.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(122, 169, 168), LineStyle.DOTTED_LITERAL, 1));
		ls2.getLabel().setVisible(false);
		ls2.setDataSet(dsNumericValues2);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.PINK());
		sdY2.getSeries().add(ls2);

		Series seZ = SeriesImpl.create();
		seZ.setDataSet(dsStringValue1);

		SeriesDefinition sdZ = SeriesDefinitionImpl.create();
		sdZ.getSeriesPalette().update(0);
		sdZ.getSeries().add(SeriesImpl.create());
		zAxisPrimary.getSeriesDefinitions().add(sdZ);
		sdZ.getSeries().add(seZ);
		return cwaLine;

	}
}
