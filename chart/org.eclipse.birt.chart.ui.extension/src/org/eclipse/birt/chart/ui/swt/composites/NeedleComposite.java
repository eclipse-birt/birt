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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 *
 */

public class NeedleComposite extends Composite implements Listener {

	private transient DialSeries series;

	private transient LineAttributesComposite liacNeedle;

	private HeadStyleAttributeComposite cmbHeadStyle;

	public static class NeedleAttributesContext {
		public int lineOptionalStyles = LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_STYLES;

		public boolean bEnableHeadStyle = true;
	}

	public NeedleComposite(Composite coParent, ChartWizardContext wizardContext, DialSeries series) {
		super(coParent, SWT.NONE);

		construct(coParent, wizardContext, series, new NeedleAttributesContext());
	}

	public NeedleComposite(Composite coParent, ChartWizardContext wizardContext, DialSeries series,
			NeedleAttributesContext needleAttributeContext) {
		super(coParent, SWT.NONE);

		construct(coParent, wizardContext, series, needleAttributeContext);
	}

	private void construct(Composite coParent, ChartWizardContext wizardContext, DialSeries series,
			NeedleAttributesContext needleAttributeContext) {
		this.series = series;
		DialSeries defSeries = (DialSeries) ChartDefaultValueUtil.getDefaultSeries(series);
		GridLayout gl = new GridLayout(1, true);
		gl.verticalSpacing = 0;
		gl.marginWidth = 10;
		gl.marginHeight = 0;
		setLayout(gl);

		liacNeedle = new LineAttributesComposite(this, SWT.NONE, needleAttributeContext.lineOptionalStyles,
				wizardContext, series.getNeedle().getLineAttributes(), defSeries.getNeedle().getLineAttributes());
		GridData gdLIACNeedle = new GridData(GridData.FILL_HORIZONTAL);
		liacNeedle.setLayoutData(gdLIACNeedle);
		liacNeedle.addListener(this);

		if (needleAttributeContext.bEnableHeadStyle) {
			cmbHeadStyle = new HeadStyleAttributeComposite(this, SWT.NONE, series.getNeedle().getDecorator(),
					series.getNeedle(), "decorator", //$NON-NLS-1$
					wizardContext);
			GridData gdCMBHeadStyle = new GridData(GridData.FILL_HORIZONTAL);
			cmbHeadStyle.setLayoutData(gdCMBHeadStyle);
			cmbHeadStyle.addListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (event.widget.equals(liacNeedle)) {
			if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getNeedle().getLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getNeedle().getLineAttributes(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
			}
		} else if (event.widget.equals(cmbHeadStyle)) {
			if (event.type == HeadStyleAttributeComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getNeedle(), "decorator", //$NON-NLS-1$
						event.data, isUnset);
			}
		}
	}
}
