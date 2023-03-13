/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.radar.ui.series;

import java.math.BigInteger;
import java.text.ParseException;

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.examples.radar.render.Radar;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.swt.AbstractChartTextEditor;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 *
 */

public class RadarLineSheet extends AbstractPopupSheet implements Listener {

	private final RadarSeries series;
	private static final int MAX_STEPS = 20;
	private static final int MIN_STEPS = 1;
	private ChartCheckbox btnAutoScale = null;
	private Label lblWebMax = null;
	private Label lblWebMin = null;
	private AbstractChartTextEditor webMax = null;
	private AbstractChartTextEditor webMin = null;
	private LineAttributesComposite wliacLine = null;
	private ChartSpinner iscScaleCnt = null;
	private ChartCheckbox btnTranslucentBullseye = null;
	private RadarSeries defSeries;

	public RadarLineSheet(String title, ChartWizardContext context, boolean needRefresh, RadarSeries series) {
		super(title, context, needRefresh);
		this.series = series;
		this.defSeries = (RadarSeries) ChartDefaultValueUtil.getDefaultSeries(series);

	}

	@Override
	protected Composite getComponent(Composite parent) {
		Composite cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glMain = new GridLayout();
			glMain.numColumns = 2;
			cmpContent.setLayout(glMain);
		}

		Group grpLine = new Group(cmpContent, SWT.NONE);
		GridLayout glLine = new GridLayout(2, false);
		grpLine.setLayout(glLine);
		grpLine.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpLine.setText(Messages.getString("RadarSeriesMarkerSheet.Label.Web")); //$NON-NLS-1$

		int lineStyles = LineAttributesComposite.ENABLE_COLOR | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_WIDTH;
		lineStyles |= getContext().getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR
				: lineStyles;
		wliacLine = new LineAttributesComposite(grpLine, SWT.NONE, lineStyles, getContext(),
				series.getWebLineAttributes(), defSeries.getWebLineAttributes());
		GridData wgdLIACLine = new GridData(GridData.FILL_HORIZONTAL);
		wgdLIACLine.horizontalSpan = 2;
		wgdLIACLine.widthHint = 200;
		wliacLine.setLayoutData(wgdLIACLine);
		wliacLine.addListener(this);

		GridLayout glRangeValue = new GridLayout();
		glRangeValue.numColumns = 3;
		glRangeValue.horizontalSpacing = 2;
		glRangeValue.verticalSpacing = 5;
		glRangeValue.marginHeight = 0;
		glRangeValue.marginWidth = 0;

		Composite cmpMinMax = new Composite(grpLine, SWT.NONE);
		GridData gdMinMax = new GridData(GridData.FILL_HORIZONTAL);
		cmpMinMax.setLayoutData(gdMinMax);
		cmpMinMax.setLayout(glRangeValue);

		btnAutoScale = getContext().getUIFactory().createChartCheckbox(cmpMinMax, SWT.NONE,
				defSeries.isRadarAutoScale());
		{
			btnAutoScale.setText(Messages.getString("Radar.Composite.Label.ScaleAuto")); //$NON-NLS-1$
			GridData gd = new GridData();
			gd.horizontalSpan = 3;
			btnAutoScale.setLayoutData(gd);
			btnAutoScale.setToolTipText(Messages.getString("Radar.Composite.Label.ScaleAutoTooltip")); //$NON-NLS-1$
			btnAutoScale.setSelectionState(series.isSetRadarAutoScale()
					? (series.isRadarAutoScale() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED);
			btnAutoScale.addListener(SWT.Selection, this);
		}

		lblWebMin = new Label(cmpMinMax, SWT.NONE);
		{
			lblWebMin.setText(Messages.getString("Radar.Composite.Label.ScaleMin")); //$NON-NLS-1$
			lblWebMin.setToolTipText(Messages.getString("Radar.Composite.Label.ScaleMinToolTip")); //$NON-NLS-1$
		}

		webMin = getContext().getUIFactory().createChartTextEditor(cmpMinMax, SWT.BORDER | SWT.SINGLE, series,
				"webLabelMin"); //$NON-NLS-1$
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			webMin.setLayoutData(gd);
			if (series.getWebLabelMin() != Double.NaN) {
				webMin.setText(Double.toString(series.getWebLabelMin()));
			}
			webMin.setToolTipText(Messages.getString("Radar.Composite.Label.ScaleMinToolTip")); //$NON-NLS-1$
			webMin.addListener(this);
		}

		lblWebMax = new Label(cmpMinMax, SWT.NONE);
		{
			lblWebMax.setText(Messages.getString("Radar.Composite.Label.ScaleMax")); //$NON-NLS-1$
			lblWebMax.setToolTipText(Messages.getString("Radar.Composite.Label.ScaleMaxToolTip")); //$NON-NLS-1$
		}

		webMax = getContext().getUIFactory().createChartTextEditor(cmpMinMax, SWT.BORDER | SWT.SINGLE, series,
				"webLabelMax");//$NON-NLS-1$
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			webMax.setLayoutData(gd);
			if (series.getWebLabelMax() != Double.NaN) {
				webMax.setText(Double.toString(series.getWebLabelMax()));
			}
			webMax.setToolTipText(Messages.getString("Radar.Composite.Label.ScaleMaxToolTip")); //$NON-NLS-1$
			webMax.addListener(this);
		}

		boolean enabled = !(btnAutoScale.getSelectionState() == ChartCheckbox.STATE_SELECTED);
		updateScaleUI(enabled);

		if (supportNumberOfStep()) {
			Label lblWebStep = new Label(cmpMinMax, SWT.NONE);
			{
				lblWebStep.setText(Messages.getString("Radar.Composite.Label.ScaleCount")); //$NON-NLS-1$
				lblWebStep.setToolTipText(Messages.getString("Radar.Composite.Label.ScaleCountToolTip")); //$NON-NLS-1$
			}

			iscScaleCnt = getContext().getUIFactory().createChartSpinner(cmpMinMax, SWT.BORDER, series, "plotSteps", //$NON-NLS-1$
					true);
			GridData gdISCLeaderLength = new GridData(GridData.FILL_HORIZONTAL);
			iscScaleCnt.setLayoutData(gdISCLeaderLength);
			iscScaleCnt.getWidget().setMinimum(MIN_STEPS);
			iscScaleCnt.getWidget().setMaximum(MAX_STEPS);
			iscScaleCnt.getWidget().setSelection(series.getPlotSteps().intValue());
			iscScaleCnt.addListener(SWT.Modify, this);
		}

		if (getChart().getSubType().equals(Radar.BULLSEYE_SUBTYPE_LITERAL)) {
			btnTranslucentBullseye = getContext().getUIFactory().createChartCheckbox(cmpMinMax, SWT.NONE,
					defSeries.isBackgroundOvalTransparent());
			btnTranslucentBullseye.setText(Messages.getString("Radar.Composite.Label.bullsEye")); //$NON-NLS-1$
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 3;
			gd.verticalAlignment = SWT.TOP;
			btnTranslucentBullseye.setLayoutData(gd);
			btnTranslucentBullseye.setSelectionState(
					series.isSetBackgroundOvalTransparent()
							? (series.isBackgroundOvalTransparent() ? ChartCheckbox.STATE_SELECTED
									: ChartCheckbox.STATE_UNSELECTED)
							: ChartCheckbox.STATE_GRAYED);
			btnTranslucentBullseye.addListener(SWT.Selection, this);
		}
		return cmpContent;
	}

	protected boolean supportNumberOfStep() {
		return true;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(wliacLine)) {
			boolean isUnset = (event.detail == ChartElementUtil.PROPERTY_UNSET);
			if (event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getWebLineAttributes(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				// enableLineSettings( series.getWebLineAttributes( ).isVisible(
				// ) );
			} else if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getWebLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getWebLineAttributes(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
			} else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				series.getWebLineAttributes().setColor((ColorDefinition) event.data);
			}
		} else if (event.widget.equals(webMin)) {
			double tmin = this.getTypedDataElement(webMin.getText());
			double tmax = this.getTypedDataElement(webMax.getText());
			if (tmin > tmax) {
				tmin = tmax;
			}
			if (!TextEditorComposite.TEXT_RESET_MODEL.equals(event.data)) {
				series.setWebLabelMin(tmin);
			}
			webMin.setText(Double.toString(tmin));
		} else if (event.widget.equals(webMax)) {

			double tmin = this.getTypedDataElement(webMin.getText());
			double tmax = this.getTypedDataElement(webMax.getText());
			if (tmax < tmin) {
				tmax = tmin;
			}
			if (!TextEditorComposite.TEXT_RESET_MODEL.equals(event.data)) {
				series.setWebLabelMax(tmax);
			}
			webMax.setText(Double.toString(tmax));

		} else if (event.widget.equals(btnTranslucentBullseye)) {
			ChartElementUtil.setEObjectAttribute(series, "backgroundOvalTransparent", //$NON-NLS-1$
					btnTranslucentBullseye.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnTranslucentBullseye.getSelectionState() == ChartCheckbox.STATE_GRAYED);
		} else if (event.widget.equals(iscScaleCnt)) {
			if (supportNumberOfStep()) {
				if (iscScaleCnt.getWidget().getText() == null || iscScaleCnt.getWidget().getText().length() == 0) {
					series.setPlotSteps(BigInteger.valueOf(iscScaleCnt.getWidget().getSelection()));
					return;
				}
				if (new BigInteger(iscScaleCnt.getWidget().getText()).intValue() > MAX_STEPS) {
					iscScaleCnt.getWidget().setSelection(MAX_STEPS);
					series.setPlotSteps(BigInteger.valueOf(MAX_STEPS));
					MessageBox mb = new MessageBox(iscScaleCnt.getShell(), SWT.ICON_WARNING | SWT.OK);
					mb.setMessage(Messages.getString("Radar.Composite.Label.ScaleCount.MinMaxWarning",
							new Object[] { MIN_STEPS, MAX_STEPS }, ULocale.getDefault()));
					mb.open();
				} else if (new BigInteger(iscScaleCnt.getWidget().getText()).intValue() < MIN_STEPS) {
					iscScaleCnt.getWidget().setSelection(MIN_STEPS);
					series.setPlotSteps(BigInteger.valueOf(MIN_STEPS));
					MessageBox mb = new MessageBox(iscScaleCnt.getShell(), SWT.ICON_WARNING | SWT.OK);
					mb.setMessage(Messages.getString("Radar.Composite.Label.ScaleCount.MinMaxWarning",
							new Object[] { MIN_STEPS, MAX_STEPS }, ULocale.getDefault()));
					mb.open();
				}
			}
		} else if (event.widget.equals(btnAutoScale)) {
			ChartElementUtil.setEObjectAttribute(series, "radarAutoScale", //$NON-NLS-1$
					btnAutoScale.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnAutoScale.getSelectionState() == ChartCheckbox.STATE_GRAYED);

			boolean enabled = !(btnAutoScale.getSelectionState() == ChartCheckbox.STATE_SELECTED);
			updateScaleUI(enabled);
		}
	}

	protected void updateScaleUI(boolean enabled) {
		lblWebMin.setEnabled(enabled);
		lblWebMax.setEnabled(enabled);
		webMin.setEnabled(enabled);
		webMax.setEnabled(enabled);
	}

	private double getTypedDataElement(String strDataElement) {
		if (strDataElement.trim().length() == 0) {
			return 0.0;
		}
		NumberFormat nf = ChartUIUtil.getDefaultNumberFormatInstance();

		try {
			Number numberElement = nf.parse(strDataElement);
			return numberElement.doubleValue();
		} catch (ParseException e1) {
			return 0.0;
		}
	}
}
