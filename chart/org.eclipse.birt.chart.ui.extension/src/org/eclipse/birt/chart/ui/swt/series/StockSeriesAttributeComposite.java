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
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class StockSeriesAttributeComposite extends Composite implements Listener {

	// FillChooserComposite fccCandle = null;

	private LineAttributesComposite liacStock = null;

	protected ChartSpinner iscStick = null;

	protected StockSeries series = null;

	protected StockSeries defSeries = null;

	protected transient ChartWizardContext context;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui.extension/swt.series"); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public StockSeriesAttributeComposite(Composite parent, int style, ChartWizardContext context, Series series) {
		super(parent, style);
		if (!(series instanceof StockSeries)) {
			try {
				throw new ChartException(ChartUIExtensionPlugin.ID, ChartException.VALIDATION,
						"StockSeriesAttributeComposite.Exception.IllegalArgument", //$NON-NLS-1$
						new Object[] { series.getClass().getName() }, Messages.getResourceBundle());
			} catch (ChartException e) {
				logger.log(e);
				e.printStackTrace();
			}
		}
		this.series = (StockSeries) series;
		this.defSeries = (StockSeries) ChartDefaultValueUtil.getDefaultSeries(this.series);
		this.context = context;
		init();
		placeComponents();
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.SUBTASK_YSERIES_STOCK);
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
	}

	protected void placeComponents() {
		// Main content composite
		this.setLayout(new GridLayout());

		Group grpLine = new Group(this, SWT.NONE);
		{
			grpLine.setText(Messages.getString("StockSeriesAttributeComposite.Lbl.Line")); //$NON-NLS-1$
			GridLayout glLine = new GridLayout();
			glLine.numColumns = needStickLength() ? 4 : 1;
			grpLine.setLayout(glLine);
			grpLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		initUIComponents(grpLine);
	}

	protected void initUIComponents(Group grpLine) {
		// Line Attributes composite
		liacStock = new LineAttributesComposite(grpLine, SWT.NONE, getLineOptionalStyles(), context,
				series.getLineAttributes(), defSeries.getLineAttributes());

		GridData gdLIACStock = new GridData(GridData.FILL_HORIZONTAL);
		gdLIACStock.verticalSpan = 3;
		liacStock.setLayoutData(gdLIACStock);
		liacStock.addListener(this);

		if (needStickLength()) {
			Composite comp = new Composite(grpLine, SWT.NONE);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			comp.setLayoutData(gd);
			GridLayout gl = new GridLayout();
			gl.numColumns = 3;
			gl.marginBottom = 0;
			gl.marginHeight = 0;
			gl.marginLeft = 0;
			gl.marginRight = 0;
			gl.marginTop = 0;
			gl.marginWidth = 0;
			comp.setLayout(gl);

			new Label(comp, SWT.NONE).setText(Messages.getString("StockSeriesAttributeComposite.Lbl.StickLength")); //$NON-NLS-1$

			iscStick = context.getUIFactory().createChartSpinner(comp, SWT.BORDER, series, "stickLength", //$NON-NLS-1$
					true);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			iscStick.setLayoutData(gd);
			iscStick.getWidget().setMinimum(0);
			iscStick.getWidget().setMaximum(Integer.MAX_VALUE);
			iscStick.getWidget().setSelection(series.getStickLength());
		}
	}

	protected boolean needStickLength() {
		return series.isShowAsBarStick();
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
		// if ( event.widget.equals( fccCandle ) )
		// {
		// series.setFill( (Fill) event.data );
		// }
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (event.widget.equals(liacStock)) {
			if (event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getLineAttributes(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
			} else if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getLineAttributes(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
			} else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				series.getLineAttributes().setColor((ColorDefinition) event.data);
			}
		}
	}

	protected int getLineOptionalStyles() {
		return LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_COLOR | LineAttributesComposite.ENABLE_AUTO_COLOR;
	}
}