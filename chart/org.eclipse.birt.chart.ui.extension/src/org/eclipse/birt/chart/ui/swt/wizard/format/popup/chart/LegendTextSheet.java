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

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartInsets;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierPreview;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * LegendTextSheet
 */

public class LegendTextSheet extends AbstractPopupSheet implements Listener {

	protected transient Composite cmpContent = null;

	protected transient FontDefinitionComposite fdcFont = null;

	private transient LineAttributesComposite lineSeparator;

	private transient FillChooserComposite fccBackground;

	private transient FillChooserComposite fccShadow;

	private transient LineAttributesComposite outlineText;

	private transient AbstractChartInsets icText;

	protected transient FormatSpecifierPreview fsp;

	protected transient Button btnFormatSpecifier;

	private ChartSpinner spnEllipsis;

	private boolean isByCategory;

	private boolean containsYOG;

	public LegendTextSheet(String title, ChartWizardContext context) {
		super(title, context, true);
		isByCategory = getChart().getLegend().getItemType() != LegendItemType.SERIES_LITERAL;
		containsYOG = ChartUtil.containsYOptionalGrouping(getChart());
	}

	@Override
	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_LEGEND_BLOCK);

		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glMain = new GridLayout();
			glMain.horizontalSpacing = 5;
			glMain.verticalSpacing = 5;
			glMain.marginHeight = 7;
			glMain.marginWidth = 7;
			cmpContent.setLayout(glMain);
		}

		Group grpTxtArea = new Group(cmpContent, SWT.NONE);
		{
			GridLayout layout = new GridLayout(3, false);
			layout.marginHeight = 7;
			layout.marginWidth = 7;
			grpTxtArea.setLayout(layout);
			grpTxtArea.setLayoutData(new GridData(GridData.FILL_BOTH));
			grpTxtArea.setText(Messages.getString("MoreOptionsChartLegendSheet.Label.TextArea")); //$NON-NLS-1$
		}

		Label lblFormat = new Label(grpTxtArea, SWT.NONE);
		{
			lblFormat.setText(Messages.getString("DialLabelSheet.Label.Format")); //$NON-NLS-1$
		}

		Composite cmpFormat = new Composite(grpTxtArea, SWT.BORDER);
		{
			GridLayout layout = new GridLayout(2, false);
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			layout.horizontalSpacing = 0;
			cmpFormat.setLayout(layout);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			cmpFormat.setLayoutData(gd);
			cmpFormat.setBackground(cmpFormat.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		}

		fsp = new FormatSpecifierPreview(cmpFormat, SWT.NONE, false);
		{
			GridData gd = new GridData();
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.CENTER;
			fsp.setLayoutData(gd);
			fsp.updatePreview(getChart().getLegend().getFormatSpecifier());
		}

		btnFormatSpecifier = new Button(cmpFormat, SWT.PUSH);
		{
			GridData gd = new GridData();
			btnFormatSpecifier.setLayoutData(gd);
			btnFormatSpecifier.setToolTipText(Messages.getString("BaseDataDefinitionComponent.Text.EditFormat")); //$NON-NLS-1$
			// btnFormatSpecifier.setImage( UIHelper.getImage(
			// "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
			// btnFormatSpecifier.getImage( )
			// .setBackground( btnFormatSpecifier.getBackground( ) );
			btnFormatSpecifier.setText(Messages.getString("Format.Button.Lbl&")); //$NON-NLS-1$
			btnFormatSpecifier.addListener(SWT.Selection, this);
		}

		new Label(grpTxtArea, SWT.NONE).setText(Messages.getString("LegendTextSheet.Label.Font")); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite(grpTxtArea, SWT.NONE, getContext(), getLegend().getText().getFont(),
				getLegend().getText().getColor(), false);
		GridData gdFDCFont = new GridData(GridData.FILL_HORIZONTAL);
		// gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
		gdFDCFont.widthHint = fdcFont.getPreferredSize().x;
		gdFDCFont.grabExcessVerticalSpace = false;
		gdFDCFont.horizontalSpan = 2;
		fdcFont.setLayoutData(gdFDCFont);
		fdcFont.addListener(this);

		new Label(grpTxtArea, SWT.NONE).setText(Messages.getString("LegendTextSheet.Label.Ellipsis")); //$NON-NLS-1$
		spnEllipsis = getContext().getUIFactory().createChartSpinner(grpTxtArea, SWT.BORDER, getLegend(), "ellipsis", //$NON-NLS-1$
				true);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			spnEllipsis.setLayoutData(gd);
			spnEllipsis.getWidget().setMinimum(0);
			spnEllipsis.getWidget().setSelection(getLegend().getEllipsis());
			spnEllipsis.setToolTipText(Messages.getString("LegendTextSheet.Tooltip.Ellipsis")); //$NON-NLS-1$
		}

		Label lblShadow = new Label(grpTxtArea, SWT.NONE);
		GridData gdLBLShadow = new GridData();
		lblShadow.setLayoutData(gdLBLShadow);
		lblShadow.setText(Messages.getString("ClientAreaAttributeComposite.Lbl.Shadow")); //$NON-NLS-1$

		int fillStyles = FillChooserComposite.ENABLE_TRANSPARENT | FillChooserComposite.ENABLE_TRANSPARENT_SLIDER
				| FillChooserComposite.DISABLE_PATTERN_FILL;
		fillStyles |= getContext().getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : fillStyles;
		fccShadow = new FillChooserComposite(grpTxtArea, SWT.NONE, fillStyles, getContext(),
				getLegend().getClientArea().getShadowColor());
		GridData gdFCCShadow = new GridData(GridData.FILL_HORIZONTAL);
		gdFCCShadow.horizontalSpan = 2;
		fccShadow.setLayoutData(gdFCCShadow);
		fccShadow.addListener(this);

		Label lblBackground = new Label(grpTxtArea, SWT.NONE);
		lblBackground.setText(Messages.getString("Shared.mne.Background_K")); //$NON-NLS-1$

		fillStyles = FillChooserComposite.ENABLE_TRANSPARENT | FillChooserComposite.ENABLE_TRANSPARENT_SLIDER
				| FillChooserComposite.ENABLE_IMAGE | FillChooserComposite.ENABLE_GRADIENT;
		fillStyles |= getContext().getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : fillStyles;
		fccBackground = new FillChooserComposite(grpTxtArea, SWT.DROP_DOWN | SWT.READ_ONLY, fillStyles, getContext(),
				getChart().getLegend().getClientArea().getBackground());
		{
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			fccBackground.setLayoutData(gridData);
			fccBackground.addListener(this);
		}

		Group grpOutline = new Group(grpTxtArea, SWT.NONE);
		GridData gdGRPOutline = new GridData(GridData.FILL_HORIZONTAL);
		gdGRPOutline.horizontalSpan = 3;
		grpOutline.setLayoutData(gdGRPOutline);
		grpOutline.setLayout(new FillLayout());
		grpOutline.setText(Messages.getString("MoreOptionsChartLegendSheet.Label.Outline")); //$NON-NLS-1$

		Legend defLegend = ChartDefaultValueUtil.getDefaultLegend(getChart());
		int lineStyles = LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_COLOR;
		lineStyles |= getContext().getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR
				: lineStyles;
		outlineText = new LineAttributesComposite(grpOutline, SWT.NONE, lineStyles, getContext(),
				getLegend().getClientArea().getOutline(), defLegend.getClientArea().getOutline());
		outlineText.addListener(this);
		outlineText.setAttributesEnabled(true);

		icText = getContext().getUIFactory().createChartInsetsComposite(grpTxtArea, SWT.NONE, 2,
				getLegend().getClientArea().getInsets(), getChart().getUnits(), getContext().getUIServiceProvider(),
				getContext(), ChartDefaultValueUtil.getDefaultLegend(getChart()).getClientArea().getInsets());
		GridData gdInsets = new GridData(GridData.FILL_HORIZONTAL);
		gdInsets.horizontalSpan = 3;
		icText.setLayoutData(gdInsets);

		Group grpSeparator = new Group(cmpContent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 5;
			grpSeparator.setLayout(layout);
			grpSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			grpSeparator.setText(Messages.getString("LegendTextSheet.Label.Separator")); //$NON-NLS-1$
		}

		lineSeparator = new LineAttributesComposite(grpSeparator, SWT.NONE, lineStyles, getContext(),
				getLegend().getSeparator(), defLegend.getSeparator());
		{
			lineSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			lineSeparator.addListener(this);
			lineSeparator.setAttributesEnabled(true);
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
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (event.widget.equals(fdcFont)) {
			getLegend().getText().setFont((FontDefinition) ((Object[]) event.data)[0]);
			getLegend().getText().setColor((ColorDefinition) ((Object[]) event.data)[1]);
		} else if (event.widget.equals(fccShadow)) {
			getLegend().getClientArea().setShadowColor((ColorDefinition) event.data);
		} else if (event.widget.equals(fccBackground)) {
			getLegend().getClientArea().setBackground((Fill) event.data);
		} else if (event.widget.equals(icText)) {
			getLegend().getClientArea().setInsets((Insets) event.data);
		} else if (event.widget.equals(outlineText)) {
			switch (event.type) {
			case LineAttributesComposite.STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getClientArea().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LineAttributesComposite.WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getClientArea().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LineAttributesComposite.COLOR_CHANGED_EVENT:
				getLegend().getClientArea().getOutline().setColor((ColorDefinition) event.data);
				break;
			case LineAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getClientArea().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			}
		} else if (event.widget.equals(lineSeparator)) {
			switch (event.type) {
			case LineAttributesComposite.STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getSeparator(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LineAttributesComposite.WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getSeparator(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LineAttributesComposite.COLOR_CHANGED_EVENT:
				getLegend().getSeparator().setColor((ColorDefinition) event.data);
				break;
			case LineAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getLegend().getSeparator(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			}
		} else if (event.widget.equals(btnFormatSpecifier)) {
			handleFormatBtnSelected();
		}
	}

	protected void handleFormatBtnSelected() {
		FormatSpecifier fs = getContext().getUIServiceProvider().getFormatSpecifierHandler().handleFormatSpecifier(
				cmpContent.getShell(), Messages.getString("BaseDataDefinitionComponent.Text.EditFormat"), //$NON-NLS-1$
				new AxisType[] { getEntryType() }, getChart().getLegend().getFormatSpecifier(), getChart().getLegend(),
				"formatSpecifier", //$NON-NLS-1$
				getContext());
		fsp.updatePreview(fs);
	}

	protected Legend getLegend() {
		return getChart().getLegend();
	}

	protected AxisType getEntryType() {
		DataType type = DataType.TEXT_LITERAL;
		if (isByCategory) {
			type = getCategoryQueryType();
		} else if (containsYOG) {
			type = getContext().getDataServiceProvider().getDataType(ChartUtil.getYOptoinalExpressions(getChart())[0]);
		}
		if (type == DataType.NUMERIC_LITERAL) {
			return AxisType.LINEAR_LITERAL;
		} else if (type == DataType.DATE_TIME_LITERAL) {
			return AxisType.DATE_TIME_LITERAL;
		}

		return AxisType.TEXT_LITERAL;
	}

	private DataType getCategoryQueryType() {
		String query = ChartUIUtil.getDataQuery(ChartUIUtil.getBaseSeriesDefinitions(getChart()).get(0), 0)
				.getDefinition();
		return getContext().getDataServiceProvider().getDataType(query);
	}

}
