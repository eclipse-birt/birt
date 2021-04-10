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

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.type.ConeChart;
import org.eclipse.birt.chart.ui.swt.type.PyramidChart;
import org.eclipse.birt.chart.ui.swt.type.TubeChart;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class BarSeriesAttributeComposite extends Composite implements Listener {
	protected FillChooserComposite fccRiserOutline = null;

	protected Series series = null;

	protected ChartWizardContext context;

	protected static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui.extension/swt.series"); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public BarSeriesAttributeComposite(Composite parent, int style, ChartWizardContext context, Series series) {
		super(parent, style);
		if (!(series instanceof BarSeries)) {
			try {
				throw new ChartException(ChartUIExtensionPlugin.ID, ChartException.VALIDATION,
						"BarSeriesAttributeComposite.Exception.IllegalArgument", //$NON-NLS-1$
						new Object[] { series.getClass().getName() }, Messages.getResourceBundle());
			} catch (ChartException e) {
				logger.log(e);
				e.printStackTrace();
			}
		}
		this.series = series;
		this.context = context;
		init();
		placeComponents();

		initContextHelp(parent, context);
	}

	private void initContextHelp(Composite parent, ChartWizardContext context) {
		String contextHelpID = ChartHelpContextIds.SUBTASK_YSERIES_BAR;
		IChartType ct = context.getChartType();
		if (ct instanceof TubeChart) {
			contextHelpID = ChartHelpContextIds.SUBTASK_YSERIES_TUBE;
		} else if (ct instanceof PyramidChart) {
			contextHelpID = ChartHelpContextIds.SUBTASK_YSERIES_PYRAMID;
		} else if (ct instanceof ConeChart) {
			contextHelpID = ChartHelpContextIds.SUBTASK_YSERIES_CONE;
		}
		ChartUIUtil.bindHelp(parent, contextHelpID);
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
	}

	protected void placeComponents() {
		// Layout for content composite
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.marginHeight = 2;
		glContent.marginWidth = 2;

		// Main content composite
		this.setLayout(glContent);

		// Riser Outline
		Label lblRiserOutline = new Label(this, SWT.NONE);
		GridData gdLBLRiserOutline = new GridData();
		lblRiserOutline.setLayoutData(gdLBLRiserOutline);
		lblRiserOutline.setText(Messages.getString("BarSeriesAttributeComposite.Lbl.SeriesOutline")); //$NON-NLS-1$

		this.fccRiserOutline = new FillChooserComposite(this, SWT.NONE, context, ((BarSeries) series).getRiserOutline(),
				false, false, true, true);
		GridData gdFCCRiserOutline = new GridData(GridData.FILL_HORIZONTAL);
		fccRiserOutline.setLayoutData(gdFCCRiserOutline);
		fccRiserOutline.addListener(this);
	}

	public Point getPreferredSize() {
		return new Point(400, 200);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(fccRiserOutline)) {
			if (event.type == FillChooserComposite.FILL_CHANGED_EVENT) {
				((BarSeries) series).setRiserOutline((ColorDefinition) event.data);
			}
		}
	}
}