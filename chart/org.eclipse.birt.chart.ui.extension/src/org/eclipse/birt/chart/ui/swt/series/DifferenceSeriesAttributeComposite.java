/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

public class DifferenceSeriesAttributeComposite extends Composite implements SelectionListener, Listener {

	protected Group grpLine = null;

	protected Group grpLine1 = null;

	protected LineAttributesComposite liacLine1 = null;

	protected Group grpLine2 = null;

	protected LineAttributesComposite liacLine2 = null;

	protected Series series = null;

	protected DifferenceSeries defSeries = DefaultValueProvider.defDifferenceSeries();

	protected ChartWizardContext context;

	private ChartCheckbox btnPalette;

	private ChartCheckbox btnCurve;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui.extension/swt.series"); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public DifferenceSeriesAttributeComposite(Composite parent, int style, ChartWizardContext context, Series series) {
		super(parent, style);
		if (!(series instanceof LineSeriesImpl)) {
			try {
				throw new ChartException(ChartUIExtensionPlugin.ID, ChartException.VALIDATION,
						"DifferenceSeriesAttributeComposite.Exception.IllegalArgument", //$NON-NLS-1$
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
		String helpId = ChartHelpContextIds.SUBTASK_YSERIES_DIFFERENCE;
		return helpId;
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
	}

	private void placeComponents() {
		// Layout for content composite
		GridLayout glContent = new GridLayout(2, false);
		glContent.marginHeight = 2;
		glContent.marginWidth = 2;

		// Main content composite
		this.setLayout(glContent);

		grpLine = new Group(this, SWT.NONE);
		{
			GridLayout glGroup = new GridLayout(2, true);
			glGroup.horizontalSpacing = 5;
			grpLine.setLayout(glGroup);
			grpLine.setLayoutData(new GridData(GridData.FILL_BOTH));
			grpLine.setText(Messages.getString("DifferenceSeriesAttributeComposite.Lbl.Line")); //$NON-NLS-1$
		}

		if (isPosivitiveAndNegativeLineAvailable()) {
			placePositiveAndNegativeLineComponents();
		} else {
			placeOneLineComponents();
		}

		Composite cmpButton = new Composite(grpLine, SWT.NONE);
		{
			GridData gd = new GridData();
			gd.horizontalSpan = isPosivitiveAndNegativeLineAvailable() ? 2 : 1;
			cmpButton.setLayoutData(gd);
			cmpButton.setLayout(new GridLayout(isPosivitiveAndNegativeLineAvailable() ? 2 : 1, false));
		}

		btnPalette = context.getUIFactory().createChartCheckbox(cmpButton, SWT.NONE, defSeries.isPaletteLineColor());
		{
			btnPalette.setText(Messages.getString("DifferenceSeriesAttributeComposite.Lbl.LinePalette")); //$NON-NLS-1$
			btnPalette.setLayoutData(new GridData());
			btnPalette.setSelectionState(
					((LineSeries) series).isSetPaletteLineColor()
							? (((LineSeries) series).isPaletteLineColor() ? ChartCheckbox.STATE_SELECTED
									: ChartCheckbox.STATE_UNSELECTED)
							: ChartCheckbox.STATE_GRAYED);
			btnPalette.addSelectionListener(this);
		}

		btnCurve = context.getUIFactory().createChartCheckbox(cmpButton, SWT.NONE, defSeries.isCurve());
		{
			btnCurve.setText(Messages.getString("DifferenceSeriesAttributeComposite.Lbl.ShowLinesAsCurves")); //$NON-NLS-1$
			btnCurve.setLayoutData(new GridData());
			btnCurve.setSelectionState(
					((DifferenceSeries) series).isSetCurve()
							? (((DifferenceSeries) series).isCurve() ? ChartCheckbox.STATE_SELECTED
									: ChartCheckbox.STATE_UNSELECTED)
							: ChartCheckbox.STATE_GRAYED);
			btnCurve.addSelectionListener(this);
		}

		enableLinePaletteSetting(canEnableLinePalette());
	}

	protected boolean isPosivitiveAndNegativeLineAvailable() {
		return true;
	}

	protected void placePositiveAndNegativeLineComponents() {
		grpLine1 = new Group(grpLine, SWT.NONE);
		{
			GridData gdGRPLine = new GridData(GridData.FILL_BOTH);
			grpLine1.setLayout(new FillLayout());
			grpLine1.setLayoutData(gdGRPLine);
			grpLine1.setText(Messages.getString("DifferenceSeriesAttributeComposite.Lbl.PositiveLine")); //$NON-NLS-1$
		}

		liacLine1 = createLineComponent(grpLine1, ((DifferenceSeries) series).getLineAttributes(),
				defSeries.getLineAttributes());

		grpLine2 = new Group(grpLine, SWT.NONE);
		{
			GridData gdGRPLine = new GridData(GridData.FILL_BOTH);
			grpLine2.setLayout(new FillLayout());
			grpLine2.setLayoutData(gdGRPLine);
			grpLine2.setText(Messages.getString("DifferenceSeriesAttributeComposite.Lbl.NegativeLine")); //$NON-NLS-1$
		}

		liacLine2 = createLineComponent(grpLine2, ((DifferenceSeries) series).getNegativeLineAttributes(),
				defSeries.getNegativeLineAttributes());

	}

	protected void placeOneLineComponents() {
		liacLine1 = createLineComponent(grpLine, ((DifferenceSeries) series).getLineAttributes(),
				defSeries.getLineAttributes());

		GridData gdLiacLine1 = new GridData(GridData.FILL_HORIZONTAL);
		liacLine1.setLayoutData(gdLiacLine1);
		liacLine1.addListener(this);
	}

	protected LineAttributesComposite createLineComponent(Composite parent, LineAttributes lineAttrs,
			LineAttributes lineAttrsDef) {
		LineAttributesComposite lineAttrComp = new LineAttributesComposite(parent, SWT.NONE, context, lineAttrs, true,
				true, true, true, true, lineAttrsDef);

		lineAttrComp.addListener(this);

		return lineAttrComp;
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
		if (e.getSource().equals(btnCurve)) {
			ChartElementUtil.setEObjectAttribute(series, "curve", //$NON-NLS-1$
					btnCurve.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnCurve.getSelectionState() == ChartCheckbox.STATE_GRAYED);
		} else if (e.getSource().equals(btnPalette)) {
			ChartElementUtil.setEObjectAttribute(series, "paletteLineColor", //$NON-NLS-1$
					btnPalette.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnPalette.getSelectionState() == ChartCheckbox.STATE_GRAYED);
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
		if (event.widget.equals(liacLine1)) {
			if (event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(((DifferenceSeries) series).getLineAttributes(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				enableLinePaletteSetting(canEnableLinePalette());
			} else if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(((DifferenceSeries) series).getLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(((DifferenceSeries) series).getLineAttributes(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
			} else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				((DifferenceSeries) series).getLineAttributes().setColor((ColorDefinition) event.data);
			}
		} else if (event.widget.equals(liacLine2)) {
			if (event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(((DifferenceSeries) series).getNegativeLineAttributes(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				enableLinePaletteSetting(canEnableLinePalette());
			} else if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(((DifferenceSeries) series).getNegativeLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(((DifferenceSeries) series).getNegativeLineAttributes(),
						"thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
			} else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				((DifferenceSeries) series).getNegativeLineAttributes().setColor((ColorDefinition) event.data);
			}
		}
	}

	protected boolean canEnableLinePalette() {
		return !context.getUIFactory().isSetInvisible((((DifferenceSeries) series).getLineAttributes()))
				|| !context.getUIFactory().isSetInvisible((((DifferenceSeries) series).getNegativeLineAttributes()));
	}

	/**
	 * Enable the LinePalette button.
	 * 
	 * @param isEnabled enabled status.
	 */
	private void enableLinePaletteSetting(boolean isEnabled) {
		if (btnPalette != null) {
			btnPalette.setEnabled(isEnabled);
		}
	}
}