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

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

/**
 *
 */

public class RadarWebLabelSheet extends AbstractPopupSheet implements Listener {

	private final RadarSeries series;

	private Composite cmpContent = null;

	private ChartCheckbox btnWebLabels = null;

	private LabelAttributesComposite webLabelAttr = null;

	private Button btnWLFormatSpecifier = null;

	private RadarSeries defSeries;

	public RadarWebLabelSheet(String title, ChartWizardContext context, boolean needRefresh, RadarSeries series) {
		super(title, context, needRefresh);
		this.series = series;
		this.defSeries = (RadarSeries) ChartDefaultValueUtil.getDefaultSeries(series);
	}

	@Override
	protected Composite getComponent(Composite parent) {
		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glMain = new GridLayout();
			glMain.numColumns = 2;
			cmpContent.setLayout(glMain);
		}

		Group grpLine = new Group(cmpContent, SWT.NONE);
		GridLayout glLine1a = new GridLayout(2, false);
		grpLine.setLayout(glLine1a);
		grpLine.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpLine.setText(Messages.getString("RadarSeriesMarkerSheet.Label.WebLabel")); //$NON-NLS-1$

		btnWebLabels = getContext().getUIFactory().createChartCheckbox(grpLine, SWT.NONE, defSeries.isShowCatLabels());
		{
			btnWebLabels.setText(Messages.getString("RadarSeriesAttributeComposite.Lbl.ShowWeb")); //$NON-NLS-1$
			GridData gd = new GridData(GridData.FILL_VERTICAL);
			gd.horizontalSpan = 2;
			btnWebLabels.setLayoutData(gd);
			btnWebLabels.setSelectionState(series.isSetShowWebLabels()
					? (series.isShowWebLabels() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED);
			btnWebLabels.addListener(SWT.Selection, this);
		}
		// Web Label Configuration
		LabelAttributesContext attributesContext = getLabelAttributeContext();

		if (series.getWebLabel() == null) {
			org.eclipse.birt.chart.model.component.Label lab = LabelImpl.create();
			series.setWebLabel(lab);
		}

		webLabelAttr = new LabelAttributesComposite(grpLine, SWT.NONE, getContext(), attributesContext, null, series,
				null, "webLabel", //$NON-NLS-1$
				defSeries, getChart().getUnits());
		webLabelAttr.setEnabled(!(series.isSetShowWebLabels() && !series.isShowWebLabels()));
		GridData wla = new GridData(GridData.FILL_HORIZONTAL);
		wla.horizontalSpan = 2;
		webLabelAttr.setLayoutData(wla);
		webLabelAttr.addListener(this);
		webLabelAttr.setDefaultLabelValue(LabelImpl.createDefault());

		btnWLFormatSpecifier = new Button(grpLine, SWT.PUSH);
		{
			GridData gdBTNFormatSpecifier = new GridData();
			gdBTNFormatSpecifier.horizontalIndent = -3;
			gdBTNFormatSpecifier.horizontalSpan = 2;
			btnWLFormatSpecifier.setLayoutData(gdBTNFormatSpecifier);
			btnWLFormatSpecifier.setToolTipText(Messages.getString("WebLabel.Tooltip.FormatSpecifier")); //$NON-NLS-1$
			btnWLFormatSpecifier.addListener(SWT.Selection, this);
			btnWLFormatSpecifier.setText(Messages.getString("Format.Button.Web.Label")); //$NON-NLS-1$
		}

		webLabelAttr.setEnabled(!(series.isSetShowWebLabels() && !series.isShowWebLabels()));
		btnWLFormatSpecifier.setEnabled(!(series.isSetShowCatLabels() && !series.isShowWebLabels()));

		return cmpContent;
	}

	protected LabelAttributesContext getLabelAttributeContext() {
		LabelAttributesContext labelAttrs = new LabelAttributesContext();
		labelAttrs.isPositionEnabled = false;
		labelAttrs.isFontAlignmentEnabled = false;
		labelAttrs.isVisibilityEnabled = false;

		return labelAttrs;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(webLabelAttr)) {
			boolean isUnset = (event.detail == ChartElementUtil.PROPERTY_UNSET);
			switch (event.type) {
			case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getWebLabel(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LabelAttributesComposite.FONT_CHANGED_EVENT:
				series.getWebLabel().getCaption().setFont((FontDefinition) ((Object[]) event.data)[0]);
				series.getWebLabel().getCaption().setColor((ColorDefinition) ((Object[]) event.data)[1]);
				break;
			case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT:
				series.getWebLabel().setBackground((Fill) event.data);
				break;
			case LabelAttributesComposite.SHADOW_CHANGED_EVENT:
				series.getWebLabel().setShadowColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getWebLabel().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getWebLabel().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT:
				series.getWebLabel().getOutline().setColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getWebLabel().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LabelAttributesComposite.INSETS_CHANGED_EVENT:
				series.getWebLabel().setInsets((Insets) event.data);
				break;
			}
		} else if (event.widget.equals(btnWebLabels)) {
			ChartElementUtil.setEObjectAttribute(series, "showWebLabels", //$NON-NLS-1$
					btnWebLabels.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnWebLabels.getSelectionState() == ChartCheckbox.STATE_GRAYED);
			boolean enabled = !(series.isSetShowWebLabels() && !series.isShowWebLabels());
			webLabelAttr.setEnabled(enabled);
			btnWLFormatSpecifier.setEnabled(enabled);
		} else if (event.widget.equals(btnWLFormatSpecifier)) {
			FormatSpecifier formatspecifier = null;
			if (series.getWebLabelFormatSpecifier() != null) {
				formatspecifier = series.getWebLabelFormatSpecifier();
			}
			getContext().getUIServiceProvider().getFormatSpecifierHandler().handleFormatSpecifier(cmpContent.getShell(),
					Messages.getString("WebLabel.Tooltip.FormatSpecifier"), //$NON-NLS-1$
					new AxisType[] { AxisType.LINEAR_LITERAL }, formatspecifier, series, "webLabelFormatSpecifier", //$NON-NLS-1$
					getContext());
		}
	}
}
