/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.api.data;

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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.PlatformConfig;

/**
 * The selector of interactivity charts in Swing JPanel.
 *
 */
public final class DataChartsViewer extends JPanel implements IUpdateNotifier, ComponentListener {

	private static final long serialVersionUID = 1L;

	private boolean bNeedsGeneration = true;

	private transient GeneratedChartState gcs = null;

	private Chart cm = null;

	private IDeviceRenderer idr = null;

	/**
	 * Contructs the layout with a container for displaying chart and a control
	 * panel for selecting interactivity.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		DataChartsViewer dcv = new DataChartsViewer();

		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.addComponentListener(dcv);

		Container co = jf.getContentPane();
		co.setLayout(new BorderLayout());
		co.add(dcv, BorderLayout.CENTER);

		Dimension dScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dApp = new Dimension(600, 400);
		jf.setSize(dApp);
		jf.setLocation((dScreen.width - dApp.width) / 2, (dScreen.height - dApp.height) / 2);

		jf.setTitle(dcv.getClass().getName() + " [device=" //$NON-NLS-1$
				+ dcv.idr.getClass().getName() + "]");//$NON-NLS-1$

		ControlPanel cp = dcv.new ControlPanel(dcv);
		co.add(cp, BorderLayout.SOUTH);

		jf.setVisible(true);
	}

	/**
	 * Get the connection with SWING device to render the graphics.
	 */
	DataChartsViewer() {
		PlatformConfig config = new PlatformConfig();
		config.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		final PluginSettings ps = PluginSettings.instance(config);
		try {
			idr = ps.getDevice("dv.SWING");//$NON-NLS-1$
		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = DataCharts.createMinSliceChart();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
	 */
	@Override
	public void regenerateChart() {
		bNeedsGeneration = true;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
	 */
	@Override
	public void repaintChart() {
		repaint();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#peerInstance()
	 */
	@Override
	public Object peerInstance() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getDesignTimeModel()
	 */
	@Override
	public Chart getDesignTimeModel() {
		return cm;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getRunTimeModel()
	 */
	@Override
	public Chart getRunTimeModel() {
		return gcs.getChartModel();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
		idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, this);

		Dimension d = getSize();
		Bounds bo = BoundsImpl.create(0, 0, d.width, d.height);
		bo.scale(72d / idr.getDisplayServer().getDpiResolution());

		Generator gr = Generator.instance();

		// When the update button has been pushed, build a chart offscreen.
		if (bNeedsGeneration) {
			bNeedsGeneration = false;
			try {
				gcs = gr.build(idr.getDisplayServer(), cm, bo, null, null, null);
			} catch (ChartException ex) {
				showException(g2d, ex);
			}
		}

		// Draw the previous built chart on screen.
		try {
			gr.render(idr, gcs);
		} catch (ChartException ex) {
			showException(g2d, ex);
		}
	}

	/**
	 * Presents the Exceptions if the chart cannot be displayed properly.
	 *
	 * @param g2d
	 * @param ex
	 */
	private void showException(Graphics2D g2d, Exception ex) {
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
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		bNeedsGeneration = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
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

		private final DataChartsViewer dcv;

		ControlPanel(DataChartsViewer dcv) {
			this.dcv = dcv;

			setLayout(new GridLayout(0, 1, 0, 0));

			JPanel jp = new JPanel();
			jp.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

			JLabel choose = new JLabel("Choose:");//$NON-NLS-1$
			choose.setDisplayedMnemonic('c');
			jp.add(choose);
			jcbModels = new JComboBox();

			jcbModels.addItem("Min Slice");//$NON-NLS-1$
			jcbModels.addItem("Multiple Y Axis");//$NON-NLS-1$
			jcbModels.addItem("Multiple Y Series");//$NON-NLS-1$
			jcbModels.addItem("Big number Y Series");//$NON-NLS-1$

			jcbModels.setSelectedIndex(0);
			choose.setLabelFor(jcbModels);
			jp.add(jcbModels);

			jbUpdate = new JButton("Update");//$NON-NLS-1$
			jbUpdate.setMnemonic('u');
			jbUpdate.setToolTipText("Update");//$NON-NLS-1$
			jbUpdate.addActionListener(this);
			jp.add(jbUpdate);

			add(jp);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int i = jcbModels.getSelectedIndex();
			cm = null;
			switch (i) {
			case 0:
				cm = DataCharts.createMinSliceChart();
				break;
			case 1:
				cm = DataCharts.createMultiYSeriesChart();
				break;
			case 2:
				cm = DataCharts.createMultiYAxisChart();
				break;
			case 3:
				cm = DataCharts.createBigNumberSliceChart();
			}

			bNeedsGeneration = true;
			dcv.repaint();
		}
	}
}
