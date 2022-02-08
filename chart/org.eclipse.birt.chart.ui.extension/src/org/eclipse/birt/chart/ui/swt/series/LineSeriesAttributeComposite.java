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

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
public class LineSeriesAttributeComposite extends Composite implements SelectionListener, Listener {

	private Label lblShadow;

	private FillChooserComposite fccShadow = null;

	protected Group grpLine = null;

	private LineAttributesComposite liacLine = null;

	protected Series series = null;

	protected LineSeries defSeries = DefaultValueProvider.defLineSeries();

	protected ChartWizardContext context;

	private ChartCheckbox btnPalette;

	private ChartCheckbox btnCurve;

	private ChartCheckbox btnMissingValue;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui.extension/swt.series"); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public LineSeriesAttributeComposite(Composite parent, int style, ChartWizardContext context, Series series) {
		super(parent, style);
		if (!(series instanceof LineSeriesImpl)) {
			try {
				throw new ChartException(ChartUIExtensionPlugin.ID, ChartException.VALIDATION,
						"LineSeriesAttributeComposite.Exception.IllegalArgument", //$NON-NLS-1$
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

		ChartUIUtil.bindHelp(parent, getHelpId(series));
	}

	private String getHelpId(Series series) {
		String helpId = ChartHelpContextIds.SUBTASK_YSERIES_LINE;
		if (series instanceof AreaSeries) {
			helpId = ChartHelpContextIds.SUBTASK_YSERIES_AREA;
		} else if (series instanceof ScatterSeries) {
			helpId = ChartHelpContextIds.SUBTASK_YSERIES_SCATTER;
		}
		return helpId;
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
	}

	protected void placeComponents() {
		// Main content composite
		this.setLayout(new GridLayout());
		grpLine = new Group(this, SWT.NONE);
		GridLayout glLine = new GridLayout(2, true);
		glLine.horizontalSpacing = 0;
		grpLine.setLayout(glLine);
		grpLine.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpLine.setText(Messages.getString("LineSeriesAttributeComposite.Lbl.Line")); //$NON-NLS-1$
		initUIComponents(grpLine);
		enableLineSettings(!context.getUIFactory().isSetInvisible(((LineSeries) series).getLineAttributes()));
	}

	protected void initUIComponents(Composite parent) {

		Composite cmpLine = new Composite(parent, SWT.NONE);
		{
			GridLayout gl = new GridLayout(2, false);
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			gl.horizontalSpacing = 0;
			gl.verticalSpacing = 0;
			cmpLine.setLayout(gl);
			cmpLine.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		int lineStyles = LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_COLOR;
		lineStyles |= context.getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR : lineStyles;
		liacLine = new LineAttributesComposite(cmpLine, SWT.NONE, lineStyles, context,
				((LineSeries) series).getLineAttributes(), defSeries.getLineAttributes());
		GridData gdLIACLine = new GridData(GridData.FILL_HORIZONTAL);
		gdLIACLine.horizontalSpan = 2;
		liacLine.setLayoutData(gdLIACLine);
		liacLine.addListener(this);

		if (isShadowNeeded()) {
			Composite cmpShadow = new Composite(cmpLine, SWT.NONE);
			{
				GridLayout gl = new GridLayout(2, false);
				gl.marginHeight = 0;
				gl.marginBottom = 0;
				gl.verticalSpacing = 0;
				cmpShadow.setLayout(gl);
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				cmpShadow.setLayoutData(gd);
			}

			lblShadow = new Label(cmpShadow, SWT.NONE);
			lblShadow.setText(Messages.getString("LineSeriesAttributeComposite.Lbl.ShadowColor")); //$NON-NLS-1$

			int iFillOption = FillChooserComposite.DISABLE_PATTERN_FILL | FillChooserComposite.ENABLE_TRANSPARENT
					| FillChooserComposite.ENABLE_TRANSPARENT_SLIDER;
			iFillOption |= context.getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : iFillOption;

			fccShadow = new FillChooserComposite(cmpShadow, SWT.DROP_DOWN | SWT.READ_ONLY, iFillOption, context,
					((LineSeries) series).getShadowColor());

			GridData gdFCCShadow = new GridData(GridData.FILL_HORIZONTAL);
			fccShadow.setLayoutData(gdFCCShadow);
			fccShadow.addListener(this);
		}

		Composite cmp = new Composite(grpLine, SWT.NONE);
		GridLayout gl = new GridLayout(1, false);
		cmp.setLayout(gl);

		btnPalette = context.getUIFactory().createChartCheckbox(cmp, SWT.NONE, defSeries.isPaletteLineColor());
		{
			btnPalette.setText(Messages.getString("LineSeriesAttributeComposite.Lbl.LinePalette")); //$NON-NLS-1$
			btnPalette.setSelectionState(
					((LineSeries) series).isSetPaletteLineColor()
							? (((LineSeries) series).isPaletteLineColor() ? ChartCheckbox.STATE_SELECTED
									: ChartCheckbox.STATE_UNSELECTED)
							: ChartCheckbox.STATE_GRAYED);
			btnPalette.addSelectionListener(this);
		}

		btnCurve = context.getUIFactory().createChartCheckbox(cmp, SWT.NONE, defSeries.isCurve());
		{
			btnCurve.setText(Messages.getString("LineSeriesAttributeComposite.Lbl.ShowLinesAsCurves")); //$NON-NLS-1$
			btnCurve.setSelectionState(((LineSeries) series).isSetCurve()
					? (((LineSeries) series).isCurve() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED);
			btnCurve.addSelectionListener(this);
		}

		if (!(series instanceof AreaSeries && (series.isSetStacked() && series.isStacked()))) {
			btnMissingValue = context.getUIFactory().createChartCheckbox(cmp, SWT.NONE,
					defSeries.isConnectMissingValue());
			{
				btnMissingValue.setText(Messages.getString("LineSeriesAttributeComposite.Lbl.ConnectMissingValue")); //$NON-NLS-1$
				btnMissingValue.setSelectionState(((LineSeries) series).isSetConnectMissingValue()
						? (((LineSeries) series).isConnectMissingValue() ? ChartCheckbox.STATE_SELECTED
								: ChartCheckbox.STATE_UNSELECTED)
						: ChartCheckbox.STATE_GRAYED);
				btnMissingValue.addSelectionListener(this);
			}
		}
	}

	public Point getPreferredSize() {
		return new Point(400, 200);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.widget == btnCurve) {
			ChartElementUtil.setEObjectAttribute(series, "curve", //$NON-NLS-1$
					btnCurve.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnCurve.getSelectionState() == ChartCheckbox.STATE_GRAYED);
		} else if (e.widget == btnPalette) {
			ChartElementUtil.setEObjectAttribute(series, "paletteLineColor", //$NON-NLS-1$
					btnPalette.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnPalette.getSelectionState() == ChartCheckbox.STATE_GRAYED);
		} else if (e.widget == btnMissingValue) {
			ChartElementUtil.setEObjectAttribute(series, "connectMissingValue", //$NON-NLS-1$
					btnMissingValue.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnMissingValue.getSelectionState() == ChartCheckbox.STATE_GRAYED);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (event.widget.equals(liacLine)) {
			if (event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(((LineSeries) series).getLineAttributes(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);

				enableLineSettings(!context.getUIFactory().isSetInvisible(((LineSeries) series).getLineAttributes()));
			} else if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(((LineSeries) series).getLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(((LineSeries) series).getLineAttributes(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
			} else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				((LineSeries) series).getLineAttributes().setColor((ColorDefinition) event.data);
			}
		} else if (event.widget.equals(fccShadow)) {
			((LineSeries) series).setShadowColor((ColorDefinition) event.data);
		}
	}

	protected boolean isShadowNeeded() {
		return !(series instanceof AreaSeries)
				&& context.getModel().getDimension().getValue() != ChartDimension.THREE_DIMENSIONAL;
	}

	protected void enableLineSettings(boolean isEnabled) {
		if (lblShadow != null) {
			lblShadow.setEnabled(isEnabled);
		}
		if (fccShadow != null) {
			fccShadow.setEnabled(isEnabled);
		}
		if (btnPalette != null) {
			btnPalette.setEnabled(isEnabled);
		}
		if (btnMissingValue != null) {
			btnMissingValue.setEnabled(isEnabled);
		}
		btnCurve.setEnabled(isEnabled);
	}

}
