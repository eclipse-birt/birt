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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis;

import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractScaleSheet;
import org.eclipse.swt.widgets.Composite;

/**
 * AxisScaleSheet
 */

public class AxisScaleSheet extends AbstractScaleSheet {

	private Axis axis;
	private int axisAngleType;
	private Axis defAxis;

	public AxisScaleSheet(String title, ChartWizardContext context, Axis axis, int axisAngleType, Axis defAxis) {
		super(title, context);
		this.axis = axis;
		this.axisAngleType = axisAngleType;
		this.defAxis = defAxis;
	}

	protected Axis getAxisForProcessing() {
		return axis;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractScaleSheet#
	 * getComponent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Composite getComponent(Composite parent) {
		Composite comp = super.getComponent(parent);
		this.btnShowOutside
				.setVisible(!getAxisForProcessing().isSetType() || getValueType() == TextEditorComposite.TYPE_NUMBERIC);
		return comp;
	}

	@Override
	protected Scale getScale() {
		return getAxisForProcessing().getScale();
	}

	@Override
	protected int getValueType() {
		if (getAxisForProcessing().getType() == AxisType.TEXT_LITERAL) {
			return TextEditorComposite.TYPE_NONE;
		}
		if (getAxisForProcessing().getType() == AxisType.DATE_TIME_LITERAL) {
			return TextEditorComposite.TYPE_DATETIME;
		}
		return TextEditorComposite.TYPE_NUMBERIC;
	}

	@Override
	protected void setState() {
		// Bugzilla#103961 Marker line and range only work for non-category
		// style X-axis,
		boolean bEnabled = (!getAxisForProcessing().isSetCategoryAxis() || !getAxisForProcessing().isCategoryAxis())
				&& (!getAxisForProcessing().isSetType() || getAxisForProcessing().getType() != AxisType.TEXT_LITERAL);
		setState(bEnabled);
		// Show outside is only available in Y axis
		if (axisAngleType != AngleType.Y) {
			btnShowOutside.setEnabled(false);
			// Unselect 'ShowOutSide'.
			btnShowOutside.setSelectionState(ChartCheckbox.STATE_UNSELECTED); // False
			getScale().setShowOutside(false);
		} else {
			getScale().setAutoExpand(true);
			btnAutoExpand.setSelectionState(ChartCheckbox.STATE_SELECTED);
		}

		boolean bAxisX = (axisAngleType == AngleType.X);
		boolean bEnableAutoExpand = btnStepAuto.getSelection() && bAxisX
				&& (!getAxisForProcessing().isSetType() || !(getAxisForProcessing().getType() == AxisType.TEXT_LITERAL))
				&& (!getAxisForProcessing().isSetCategoryAxis() || !(getAxisForProcessing().isCategoryAxis()));

		btnAutoExpand.setEnabled(bEnableAutoExpand);

		if ((!getAxisForProcessing().isSetType() || getAxisForProcessing().getType() == AxisType.LINEAR_LITERAL)
				&& (getAxisForProcessing().isSetCategoryAxis() || !getAxisForProcessing().isCategoryAxis())) {
			if (!getAxisForProcessing().getScale().isSetStepNumber()) {
				btnFactor.setEnabled(true);
				if (btnFactor.getSelection()) {
					txtFactor.setEnabled(true);
				} else {
					txtFactor.setEnabled(false);
				}

			}
			if (btnFactor.getSelection()) {
				btnStepNumber.setEnabled(false);
				spnStepNumber.setEnabled(false);
				lblMax.setEnabled(false);
				txtScaleMax.setEnabled(false);
				// lblMin.setEnabled( false );
				// txtScaleMin.setEnabled( false );
			}
		}
	}

	@Override
	protected Scale getDefaultVauleScale() {
		return defAxis.getScale();
	}

}
