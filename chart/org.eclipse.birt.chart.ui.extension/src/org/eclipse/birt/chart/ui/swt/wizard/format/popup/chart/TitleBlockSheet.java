/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartInsets;
import org.eclipse.birt.chart.ui.swt.ChartCombo;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * TitleBlockSheet
 */

public class TitleBlockSheet extends AbstractPopupSheet implements SelectionListener, Listener {

	private transient Composite cmpContent;

	private transient Group grpGeneral;

	private transient ChartCombo cmbAnchor;

	private transient ChartCombo cmbStretch;

	private transient Group grpOutline;

	private transient LineAttributesComposite liacOutline;

	private transient FillChooserComposite fccBackground;

	private transient AbstractChartInsets ic;

	public TitleBlockSheet(String title, ChartWizardContext context) {
		super(title, context, false);
	}

	@Override
	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_TITLE_BLOCK);

		// Layout for the content composite
		GridLayout glContent = new GridLayout();
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		// Sheet content composite
		cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);

		grpGeneral = new Group(cmpContent, SWT.NONE);
		{
			// Layout for general composite
			GridLayout glGeneral = new GridLayout();
			glGeneral.verticalSpacing = 10;
			glGeneral.marginHeight = 7;
			glGeneral.marginWidth = 7;
			GridData gdCMPGeneral = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
			grpGeneral.setLayoutData(gdCMPGeneral);
			grpGeneral.setLayout(glGeneral);
			grpGeneral.setText(Messages.getString("TitlePropertiesSheet.Label.TitleArea")); //$NON-NLS-1$
		}

		Composite cmpGeneralTop = new Composite(grpGeneral, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.horizontalSpacing = 5;
			layout.verticalSpacing = 5;
			cmpGeneralTop.setLayout(layout);
			cmpGeneralTop.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		Label lblAnchor = new Label(cmpGeneralTop, SWT.NONE);
		GridData gdLBLAnchor = new GridData();
		lblAnchor.setLayoutData(gdLBLAnchor);
		lblAnchor.setText(Messages.getString("TitlePropertiesSheet.Label.Anchor")); //$NON-NLS-1$

		cmbAnchor = getContext().getUIFactory().createChartCombo(cmpGeneralTop, SWT.DROP_DOWN | SWT.READ_ONLY,
				getBlockForProcessing(), "anchor", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultTitle(getChart()).getAnchor().getName());
		GridData gdCBAnchor = new GridData(GridData.FILL_HORIZONTAL);
		cmbAnchor.setLayoutData(gdCBAnchor);
		cmbAnchor.addSelectionListener(this);
		cmbAnchor.getWidget().setVisibleItemCount(30);

		Label lblStretch = new Label(cmpGeneralTop, SWT.NONE);
		GridData gdLBLStretch = new GridData();
		lblStretch.setLayoutData(gdLBLStretch);
		lblStretch.setText(Messages.getString("TitlePropertiesSheet.Label.Stretch")); //$NON-NLS-1$

		cmbStretch = getContext().getUIFactory().createChartCombo(cmpGeneralTop, SWT.DROP_DOWN | SWT.READ_ONLY,
				getBlockForProcessing(), "stretch", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultTitle(getChart()).getStretch().getName());
		GridData gdCBStretch = new GridData(GridData.FILL_HORIZONTAL);
		cmbStretch.setLayoutData(gdCBStretch);
		cmbStretch.addSelectionListener(this);

		Label lblBackground = new Label(cmpGeneralTop, SWT.NONE);
		GridData gdLBLBackground = new GridData();
		lblBackground.setLayoutData(gdLBLBackground);
		lblBackground.setText(Messages.getString("TitlePropertiesSheet.Label.Background")); //$NON-NLS-1$

		int fillStyels = FillChooserComposite.ENABLE_GRADIENT | FillChooserComposite.ENABLE_IMAGE
				| FillChooserComposite.ENABLE_TRANSPARENT | FillChooserComposite.ENABLE_TRANSPARENT_SLIDER;
		fillStyels |= getContext().getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : fillStyels;
		fccBackground = new FillChooserComposite(cmpGeneralTop, SWT.NONE, fillStyels, getContext(),
				getBlockForProcessing().getBackground());
		GridData gdFCCBackground = new GridData(GridData.FILL_HORIZONTAL);
		fccBackground.setLayoutData(gdFCCBackground);
		fccBackground.addListener(this);

		grpOutline = new Group(grpGeneral, SWT.NONE);
		GridData gdGRPOutline = new GridData(GridData.FILL_HORIZONTAL);
		grpOutline.setLayoutData(gdGRPOutline);
		grpOutline.setLayout(new FillLayout());
		grpOutline.setText(Messages.getString("TitlePropertiesSheet.Label.Outline")); //$NON-NLS-1$

		int lineStyles = LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_COLOR;
		lineStyles |= getContext().getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR
				: lineStyles;
		liacOutline = new LineAttributesComposite(grpOutline, SWT.NONE, lineStyles, getContext(),
				getBlockForProcessing().getOutline(), ChartDefaultValueUtil.getDefaultTitle(getChart()).getOutline());
		liacOutline.addListener(this);

		ic = getContext().getUIFactory().createChartInsetsComposite(grpGeneral, SWT.NONE, 2,
				getBlockForProcessing().getInsets(), getChart().getUnits(), getContext().getUIServiceProvider(),
				getContext(), ChartDefaultValueUtil.getDefaultTitle(getChart()).getInsets());
		GridData gdInsets = new GridData(GridData.FILL_HORIZONTAL);
		ic.setLayoutData(gdInsets);

		populateLists();

		return cmpContent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(fccBackground)) {
			getBlockForProcessing().setBackground((Fill) event.data);
		} else if (event.widget.equals(this.liacOutline)) {
			boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
			switch (event.type) {
			case LineAttributesComposite.STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getBlockForProcessing().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LineAttributesComposite.WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getBlockForProcessing().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LineAttributesComposite.COLOR_CHANGED_EVENT:
				getBlockForProcessing().getOutline().setColor((ColorDefinition) event.data);
				break;
			case LineAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getBlockForProcessing().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			}
		}
	}

	private boolean isAnchorVertical(Anchor anchor) {
		return anchor.getValue() == Anchor.EAST || anchor.getValue() == Anchor.WEST;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		Object oSource = e.getSource();
		if (oSource.equals(cmbAnchor)) {
			String selectedAnchor = cmbAnchor.getSelectedItemData();
			if (selectedAnchor != null) {
				boolean bAnchorVerticalOld = isAnchorVertical(getBlockForProcessing().getAnchor());
				getBlockForProcessing().setAnchor(Anchor.getByName(selectedAnchor));
				boolean bAnchorVerticalNew = isAnchorVertical(getBlockForProcessing().getAnchor());
				if (bAnchorVerticalOld != bAnchorVerticalNew) {
					double rotationOld = getBlockForProcessing().getLabel().getCaption().getFont().getRotation();
					double rotationNew = rotationOld >= 0 ? 90 - rotationOld : -90 - rotationOld;
					getBlockForProcessing().getLabel().getCaption().getFont().setRotation(rotationNew);
				}
			}
		} else if (oSource.equals(cmbStretch)) {
			String selectedStretch = cmbStretch.getSelectedItemData();
			if (selectedStretch != null) {
				getBlockForProcessing().setStretch(Stretch.getByName(selectedStretch));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private LabelBlock getBlockForProcessing() {
		return getChart().getTitle();
	}

	private void populateLists() {
		// Set block Anchor property
		NameSet ns = LiteralHelper.titleAnchorSet;
		cmbAnchor.setItems(ns.getDisplayNames());
		cmbAnchor.setItemData(ns.getNames());
		cmbAnchor.setSelection(getBlockForProcessing().getAnchor().getName());

		// Set the block Stretch property
		ns = LiteralHelper.stretchSet;
		cmbStretch.setItems(ns.getDisplayNames());
		cmbStretch.setItemData(ns.getNames());
		cmbStretch.setSelection(getBlockForProcessing().getStretch().getName());
	}

}
