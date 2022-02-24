/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartIntSpinner;
import org.eclipse.birt.chart.ui.swt.composites.GridAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class DialTickSheet extends AbstractPopupSheet implements Listener {

	private transient GridAttributesComposite gacMajor = null;

	private transient GridAttributesComposite gacMinor = null;

	private transient Label lblGridCount = null;

	private transient AbstractChartIntSpinner iscGridCount = null;

	private transient DialSeries series;

	private DialSeries defSeries = DefaultValueProvider.defDialSeries();

	public DialTickSheet(String title, ChartWizardContext context, DialSeries series) {
		super(title, context, false);
		this.series = series;
	}

	protected Composite getComponent(Composite parent) {
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;
		glContent.verticalSpacing = 5;

		Composite cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);

		// Layout for the Major Grid group
		FillLayout flMajor = new FillLayout();

		// Layout for the Minor Grid group
		FillLayout flMinor = new FillLayout();

		// Major Grid
		Group grpMajor = new Group(cmpContent, SWT.NONE);
		GridData gdGRPMajor = new GridData(GridData.FILL_HORIZONTAL);
		grpMajor.setLayoutData(gdGRPMajor);
		grpMajor.setText(Messages.getString("OrthogonalSeriesDataSheetImpl.Lbl.MajorGrid")); //$NON-NLS-1$
		grpMajor.setLayout(flMajor);

		int gridOptionalStyles = useFullMode()
				? GridAttributesComposite.ENABLE_COLOR | GridAttributesComposite.ENABLE_STYLES
						| GridAttributesComposite.ENABLE_VISIBILITY
				: GridAttributesComposite.ENABLE_COLOR;

		gacMajor = new GridAttributesComposite(grpMajor, SWT.NONE, gridOptionalStyles, getContext(),
				getDialForProcessing().getMajorGrid(), false, defSeries.getDial().getMajorGrid());
		gacMajor.addListener(this);

		// Minor Grid
		Group grpMinor = new Group(cmpContent, SWT.NONE);
		GridData gdGRPMinor = new GridData(GridData.FILL_HORIZONTAL);
		grpMinor.setLayoutData(gdGRPMinor);
		grpMinor.setText(Messages.getString("OrthogonalSeriesDataSheetImpl.Lbl.MinorGrid")); //$NON-NLS-1$
		grpMinor.setLayout(flMinor);

		gacMinor = new GridAttributesComposite(grpMinor, SWT.NONE, gridOptionalStyles, getContext(),
				getDialForProcessing().getMinorGrid(), false, defSeries.getDial().getMinorGrid());
		gacMinor.addListener(this);

		if (useFullMode()) {
			Composite cmpGridCount = new Composite(cmpContent, SWT.NONE);
			{
				GridData gdCMPGridCount = new GridData(GridData.FILL_HORIZONTAL);
				gdCMPGridCount.horizontalSpan = 2;
				cmpGridCount.setLayoutData(gdCMPGridCount);
				cmpGridCount.setLayout(new GridLayout(3, false));
			}

			lblGridCount = new Label(cmpGridCount, SWT.NONE);
			lblGridCount.setText(Messages.getString("OrthogonalSeriesDataSheetImpl.Lbl.MinorGridCount")); //$NON-NLS-1$

			iscGridCount = getContext().getUIFactory().createChartIntSpinner(cmpGridCount, SWT.NONE,
					getDialForProcessing().getScale().getMinorGridsPerUnit(), getDialForProcessing().getScale(),
					"minorGridsPerUnit", //$NON-NLS-1$
					true);

			GridData gdISCGridCount = new GridData(GridData.FILL_HORIZONTAL);
			iscGridCount.setLayoutData(gdISCGridCount);
			iscGridCount.addListener(this);
			if (iscGridCount instanceof IntegerSpinControl) {
				((IntegerSpinControl) iscGridCount).addScreenreaderAccessbility(lblGridCount.getText());
			}
		}

		setState(
				!getContext().getUIFactory().isSetInvisible(getDialForProcessing().getMinorGrid().getTickAttributes()));

		return cmpContent;
	}

	protected boolean useFullMode() {
		return true;
	}

	public void handleEvent(Event event) {
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (this.gacMajor.equals(event.widget)) {
			switch (event.type) {
			case GridAttributesComposite.TICK_COLOR_CHANGED_EVENT:
				getDialForProcessing().getMajorGrid().getTickAttributes().setColor((ColorDefinition) event.data);
				break;
			case GridAttributesComposite.TICK_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getDialForProcessing().getMajorGrid(), "tickStyle", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case GridAttributesComposite.TICK_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getDialForProcessing().getMajorGrid().getTickAttributes(),
						"visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			}
		} else if (this.gacMinor.equals(event.widget)) {
			switch (event.type) {
			case GridAttributesComposite.TICK_COLOR_CHANGED_EVENT:
				getDialForProcessing().getMinorGrid().getTickAttributes().setColor((ColorDefinition) event.data);
				break;
			case GridAttributesComposite.TICK_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getDialForProcessing().getMinorGrid(), "tickStyle", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case GridAttributesComposite.TICK_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getDialForProcessing().getMinorGrid().getTickAttributes(),
						"visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				setState(!getContext().getUIFactory()
						.isSetInvisible(getDialForProcessing().getMinorGrid().getTickAttributes()));
				break;
			}
		} else if (event.widget.equals(iscGridCount)) {
			ChartElementUtil.setEObjectAttribute(getDialForProcessing().getScale(), "minorGridsPerUnit", //$NON-NLS-1$
					iscGridCount.getValue(), event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		}
	}

	public Dial getDialForProcessing() {
		return series.getDial();
	}

	private void setState(boolean enabled) {
		if (useFullMode()) {
			lblGridCount.setEnabled(enabled);
			iscGridCount.setEnabled(enabled);
			iscGridCount.setEnabled(enabled);
		}
	}

}
