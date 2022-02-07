/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Pie Title popup sheet
 */

public class PieTitleSheet extends AbstractPopupSheet implements Listener {

	private Composite cmpContent = null;

	private LabelAttributesComposite lacTitle = null;

	private SeriesDefinition seriesDefn = null;

	private Series series;

	private PieSeries defSeries = DefaultValueProvider.defPieSeries();

	/**
	 * @param title
	 * @param context
	 * @param seriesDefn
	 * 
	 * @deprecated since 3.7
	 */
	public PieTitleSheet(String title, ChartWizardContext context, SeriesDefinition seriesDefn) {
		super(title, context, false);
		this.seriesDefn = seriesDefn;
	}

	public PieTitleSheet(String title, ChartWizardContext context, Series series) {
		super(title, context, false);
		this.series = series;
	}

	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_TEXT_FORMAT);

		// Layout for the content composite
		GridLayout glContent = new GridLayout();
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);

		LabelAttributesContext attributesContext = new LabelAttributesContext();
		attributesContext.isFontAlignmentEnabled = false;
		lacTitle = new LabelAttributesComposite(cmpContent, SWT.NONE, getContext(), attributesContext,
				Messages.getString("OrthogonalSeriesLabelAttributeSheetImpl.Lbl.Title"), //$NON-NLS-1$
				getSeriesForProcessing(), "titlePosition", //$NON-NLS-1$
				"label", //$NON-NLS-1$
				defSeries, getChart().getUnits(),
				LabelAttributesComposite.ALLOW_HORIZONTAL_POSITION | LabelAttributesComposite.ALLOW_VERTICAL_POSITION);
		GridData gdLACTitle = new GridData(GridData.FILL_HORIZONTAL);
		lacTitle.setLayoutData(gdLACTitle);
		lacTitle.addListener(this);
		lacTitle.setDefaultLabelValue(DefaultValueProvider.defPieSeries().getLabel());

		return cmpContent;
	}

	private PieSeries getSeriesForProcessing() {
		if (series != null) {
			return (PieSeries) series;
		}
		return (PieSeries) seriesDefn.getDesignTimeSeries();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (event.widget.equals(lacTitle)) {
			switch (event.type) {
			case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLabel(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LabelAttributesComposite.POSITION_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getSeriesForProcessing(), "titlePosition", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LabelAttributesComposite.FONT_CHANGED_EVENT:
				getLabel().getCaption().setFont((FontDefinition) ((Object[]) event.data)[0]);
				getLabel().getCaption().setColor((ColorDefinition) ((Object[]) event.data)[1]);
				break;
			case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT:
				getLabel().setBackground((Fill) event.data);
				break;
			case LabelAttributesComposite.SHADOW_CHANGED_EVENT:
				getLabel().setShadowColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLabel().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLabel().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT:
				getLabel().getOutline().setColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLabel().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LabelAttributesComposite.INSETS_CHANGED_EVENT:
				getLabel().setInsets((Insets) event.data);
				break;
			}
		}
	}

	private Label getLabel() {
		return getSeriesForProcessing().getTitle();
	}
}
