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

public class RadarCategoryLabelSheet extends AbstractPopupSheet implements Listener {

	private final RadarSeries series;

	private Composite cmpContent = null;

	private ChartCheckbox btnCatLabels = null;

	private Button btnCLFormatSpecifier = null;

	private LabelAttributesComposite catLabelAttr = null;

	private RadarSeries defSeries;

	public RadarCategoryLabelSheet(String title, ChartWizardContext context, boolean needRefresh, RadarSeries series) {
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

		// Category label configuration
		Group grpLine = new Group(cmpContent, SWT.NONE);
		GridLayout glLine1b = new GridLayout(2, false);
		grpLine.setLayout(glLine1b);
		grpLine.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpLine.setText(Messages.getString("RadarSeriesMarkerSheet.Label.CatLabel")); //$NON-NLS-1$

		btnCatLabels = getContext().getUIFactory().createChartCheckbox(grpLine, SWT.NONE, defSeries.isShowCatLabels());
		{
			btnCatLabels.setText(Messages.getString("RadarSeriesAttributeComposite.Lbl.ShowCat")); //$NON-NLS-1$
			GridData gd = new GridData(GridData.FILL_VERTICAL);
			gd.horizontalSpan = 2;
			btnCatLabels.setLayoutData(gd);
			btnCatLabels.setSelectionState(series.isSetShowCatLabels()
					? (series.isShowCatLabels() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED);
			btnCatLabels.addListener(SWT.Selection, this);
		}

		LabelAttributesContext clattributesContext = getLabelAttributeContext();
		if (series.getCatLabel() == null) {
			org.eclipse.birt.chart.model.component.Label lab = LabelImpl.create();
			series.setCatLabel(lab);
		}

		catLabelAttr = new LabelAttributesComposite(grpLine, SWT.NONE, getContext(), clattributesContext, null, series,
				null, "catLabel", //$NON-NLS-1$
				defSeries, getChart().getUnits());
		GridData cla = new GridData(GridData.FILL_HORIZONTAL);
		cla.horizontalSpan = 2;
		catLabelAttr.setLayoutData(cla);
		catLabelAttr.addListener(this);
		catLabelAttr.setDefaultLabelValue(LabelImpl.createDefault());

		btnCLFormatSpecifier = new Button(grpLine, SWT.PUSH);
		{
			GridData gdBTNFormatSpecifier = new GridData();
			gdBTNFormatSpecifier.horizontalIndent = -3;
			gdBTNFormatSpecifier.horizontalSpan = 2;
			btnCLFormatSpecifier.setLayoutData(gdBTNFormatSpecifier);
			btnCLFormatSpecifier.setToolTipText(Messages.getString("CatLabel.Tooltip.FormatSpecifier")); //$NON-NLS-1$
			btnCLFormatSpecifier.addListener(SWT.Selection, this);
			btnCLFormatSpecifier.setText(Messages.getString("Format.Button.Cat.Label")); //$NON-NLS-1$
		}

		updateUIState();
		return cmpContent;
	}

	public void handleEvent(Event event) {
		if (event.widget.equals(catLabelAttr)) {
			boolean isUnset = (event.detail == ChartElementUtil.PROPERTY_UNSET);
			switch (event.type) {
			case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getCatLabel(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LabelAttributesComposite.FONT_CHANGED_EVENT:
				series.getCatLabel().getCaption().setFont((FontDefinition) ((Object[]) event.data)[0]);
				series.getCatLabel().getCaption().setColor((ColorDefinition) ((Object[]) event.data)[1]);
				break;
			case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT:
				series.getCatLabel().setBackground((Fill) event.data);
				break;
			case LabelAttributesComposite.SHADOW_CHANGED_EVENT:
				series.getCatLabel().setShadowColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getCatLabel().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getCatLabel().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT:
				series.getCatLabel().getOutline().setColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getCatLabel().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LabelAttributesComposite.INSETS_CHANGED_EVENT:
				series.getCatLabel().setInsets((Insets) event.data);
				break;
			}
		} else if (event.widget.equals(btnCatLabels)) {
			ChartElementUtil.setEObjectAttribute(series, "showCatLabels", //$NON-NLS-1$
					btnCatLabels.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnCatLabels.getSelectionState() == ChartCheckbox.STATE_GRAYED);
			updateUIState();
		} else if (event.widget.equals(btnCLFormatSpecifier)) {

			FormatSpecifier formatspecifier = null;
			if (series.getCatLabelFormatSpecifier() != null) {
				formatspecifier = series.getCatLabelFormatSpecifier();
			}
			getContext().getUIServiceProvider().getFormatSpecifierHandler().handleFormatSpecifier(cmpContent.getShell(),
					Messages.getString("CatLabel.Tooltip.FormatSpecifier"), //$NON-NLS-1$
					new AxisType[] { AxisType.LINEAR_LITERAL, AxisType.TEXT_LITERAL, AxisType.DATE_TIME_LITERAL },
					formatspecifier, series, "catLabelFormatSpecifier", //$NON-NLS-1$
					getContext());
		}
	}

	protected void updateUIState() {
		boolean enabled = !(series.isSetShowCatLabels() && !series.isShowCatLabels());
		catLabelAttr.setEnabled(enabled);
		btnCLFormatSpecifier.setEnabled(enabled);
	}

	protected LabelAttributesContext getLabelAttributeContext() {
		LabelAttributesContext clattributesContext = new LabelAttributesContext();
		clattributesContext.isPositionEnabled = false;
		clattributesContext.isFontAlignmentEnabled = false;
		clattributesContext.isVisibilityEnabled = false;
		return clattributesContext;
	}
}
