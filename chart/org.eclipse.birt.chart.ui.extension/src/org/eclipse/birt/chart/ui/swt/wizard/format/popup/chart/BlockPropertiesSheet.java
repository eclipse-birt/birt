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
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartInsets;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

/**
 * BlockPropertiesSheet
 */

public class BlockPropertiesSheet extends AbstractPopupSheet implements Listener {

	private Composite cmpContent;

	protected Group grpOutline;

	protected LineAttributesComposite liacOutline;

	protected AbstractChartInsets ic;

	public BlockPropertiesSheet(String title, ChartWizardContext context) {
		super(title, context, true);
	}

	@Override
	protected void bindHelp(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_CHART_OUTLINE);
	}

	protected Composite getComponent(Composite parent) {
		// Sheet content composite
		cmpContent = new Composite(parent, SWT.NONE);
		{
			// Layout for the content composite
			GridLayout glContent = new GridLayout();
			cmpContent.setLayout(glContent);
		}

		ic = getContext().getUIFactory().createChartInsetsComposite(cmpContent, SWT.NONE, 2,
				getBlockForProcessing().getInsets(), getChart().getUnits(), getContext().getUIServiceProvider(),
				getContext(), ChartDefaultValueUtil.getDefaultBlock(getChart()).getInsets());
		GridData gdInsets = new GridData(GridData.FILL_HORIZONTAL);
		gdInsets.widthHint = 300;
		ic.setLayoutData(gdInsets);

		grpOutline = new Group(cmpContent, SWT.NONE);
		GridData gdGRPOutline = new GridData(GridData.FILL_HORIZONTAL);
		grpOutline.setLayoutData(gdGRPOutline);
		grpOutline.setLayout(new FillLayout());
		grpOutline.setText(Messages.getString("BlockPropertiesSheet.Label.Outline")); //$NON-NLS-1$

		liacOutline = new LineAttributesComposite(grpOutline, SWT.NONE, getOutlineAttributesStyle(), getContext(),
				getBlockForProcessing().getOutline(), ChartDefaultValueUtil.getDefaultBlock(getChart()).getOutline());
		liacOutline.addListener(this);

		return cmpContent;
	}

	protected int getOutlineAttributesStyle() {
		int style = LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_COLOR;
		style |= getContext().getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR : style;
		return style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(this.liacOutline)) {
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

	protected Block getBlockForProcessing() {
		return getChart().getBlock();
	}
}
