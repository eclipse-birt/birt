/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
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
 * LegendTitleSheet
 */

public class LegendTitleSheet extends AbstractPopupSheet implements Listener {

	private transient Composite cmpContent = null;

	private transient LabelAttributesComposite lacTitle = null;

	public LegendTitleSheet(String title, ChartWizardContext context) {
		super(title, context, true);
	}

	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_LEGEND_TITLE);

		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glMain = new GridLayout();
			glMain.horizontalSpacing = 5;
			glMain.verticalSpacing = 5;
			glMain.marginHeight = 7;
			glMain.marginWidth = 7;
			cmpContent.setLayout(glMain);
		}

		LabelAttributesContext attributesContext = new LabelAttributesContext();
		attributesContext.isVisibilityEnabled = false;
		attributesContext.isFontAlignmentEnabled = false;
		lacTitle = new LabelAttributesComposite(cmpContent, SWT.NONE, getContext(), attributesContext,
				Messages.getString("BaseAxisLabelAttributeSheetImpl.Lbl.Title"), //$NON-NLS-1$
				getLegend(), "titlePosition", //$NON-NLS-1$
				"title", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultLegend(getChart()), getChart().getUnits(),
				LabelAttributesComposite.ALLOW_VERTICAL_POSITION | LabelAttributesComposite.ALLOW_HORIZONTAL_POSITION);
		{
			GridData gdLACTitle = new GridData(GridData.FILL_BOTH);
			gdLACTitle.verticalSpan = 2;
			lacTitle.setLayoutData(gdLACTitle);
			lacTitle.addListener(this);
			lacTitle.setEnabled(!getContext().getUIFactory().isSetInvisible(getLegend().getTitle()));
			lacTitle.setDefaultLabelValue(DefaultValueProvider.defLegend().getTitle());
		}

		return cmpContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(lacTitle)) {
			boolean isUnset = ChartUIExtensionUtil.PROPERTY_UNSET == event.detail;
			switch (event.type) {
			case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getTitle(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;

			case LabelAttributesComposite.POSITION_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend(), "titlePosition", //$NON-NLS-1$
						event.data, isUnset);
				break;

			case LabelAttributesComposite.FONT_CHANGED_EVENT:
				getLegend().getTitle().getCaption().setFont((FontDefinition) ((Object[]) event.data)[0]);
				getLegend().getTitle().getCaption().setColor((ColorDefinition) ((Object[]) event.data)[1]);
				break;

			case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT:
				getLegend().getTitle().setBackground((Fill) event.data);
				break;

			case LabelAttributesComposite.SHADOW_CHANGED_EVENT:
				getLegend().getTitle().setShadowColor((ColorDefinition) event.data);
				break;

			case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getTitle().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;

			case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getTitle().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;

			case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT:
				getLegend().getTitle().getOutline().setColor((ColorDefinition) event.data);
				break;

			case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getTitle().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;

			case LabelAttributesComposite.INSETS_CHANGED_EVENT:
				getLegend().getTitle().setInsets((Insets) event.data);
				break;
			}
		}
	}

	private Legend getLegend() {
		return getChart().getLegend();
	}

}
