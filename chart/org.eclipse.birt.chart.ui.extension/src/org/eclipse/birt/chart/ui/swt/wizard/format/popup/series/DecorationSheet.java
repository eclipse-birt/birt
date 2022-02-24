/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.type.GanttSeries;
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
 * DecorationSheet
 */
public class DecorationSheet extends AbstractPopupSheet implements Listener {

	private transient GanttSeries series;

	private GanttSeries defSeries = null;

	private transient LabelAttributesComposite lacDeco;

	public DecorationSheet(String title, ChartWizardContext context, GanttSeries series) {
		super(title, context, true);
		this.series = series;
		this.defSeries = DefaultValueProvider.defGanttSeries();
	}

	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_CHART_TITLE_FORMAT);

		// Layout for the content composite
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		// Sheet content composite
		Composite cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);

		LabelAttributesContext attributesContext = getLabelAttributeContext();
		lacDeco = new LabelAttributesComposite(cmpContent, SWT.NONE, getContext(), attributesContext,
				Messages.getString("DecorationSheet.Label.DecorationLabels"), //$NON-NLS-1$
				series, "decorationLabelPosition", //$NON-NLS-1$
				"decorationLabel", //$NON-NLS-1$
				defSeries, getChart().getUnits(), LabelAttributesComposite.ALLOW_VERTICAL_POSITION);
		GridData gdLACLabel = new GridData(GridData.FILL_HORIZONTAL);
		gdLACLabel.horizontalSpan = 2;
		lacDeco.setLayoutData(gdLACLabel);
		lacDeco.addListener(this);
		lacDeco.setDefaultLabelValue(DefaultValueProvider.defGanttSeries().getDecorationLabel());

		return cmpContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (event.widget.equals(lacDeco)) {
			switch (event.type) {
			case LabelAttributesComposite.POSITION_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series, "decorationLabelPosition", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LabelAttributesComposite.FONT_CHANGED_EVENT:
				series.getDecorationLabel().getCaption().setFont((FontDefinition) ((Object[]) event.data)[0]);
				series.getDecorationLabel().getCaption().setColor((ColorDefinition) ((Object[]) event.data)[1]);
				break;
			case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT:
				series.getDecorationLabel().setBackground((Fill) event.data);
				break;
			case LabelAttributesComposite.SHADOW_CHANGED_EVENT:
				series.getDecorationLabel().setShadowColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getDecorationLabel().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getDecorationLabel().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT:
				series.getDecorationLabel().getOutline().setColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getDecorationLabel().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LabelAttributesComposite.INSETS_CHANGED_EVENT:
				series.getDecorationLabel().setInsets((Insets) event.data);
				break;
			}
		}
	}

	protected LabelAttributesContext getLabelAttributeContext() {
		LabelAttributesContext labelAttrs = new LabelAttributesContext();
		labelAttrs.isFontAlignmentEnabled = false;
		labelAttrs.isVisibilityEnabled = false;

		return labelAttrs;
	}

}
