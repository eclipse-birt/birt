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
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
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

public class AxisLabelSheet extends AbstractPopupSheet implements Listener {

	private Composite cmpContent = null;

	private LabelAttributesComposite lacLabel = null;

	private ChartSpinner iscInterval;

	private ChartSpinner iscEllipsis;

	private Axis axis;

	private int axisType;

	private Axis defAxis;

	public AxisLabelSheet(String title, ChartWizardContext context, Axis axis, int axisType, Axis defAxis) {
		super(title, context, true);
		this.axis = axis;
		this.axisType = axisType;
		this.defAxis = defAxis;
	}

	@Override
	protected void bindHelp(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_TEXT_FORMAT);
	}

	@Override
	protected Composite getComponent(Composite parent) {
		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glMain = new GridLayout();
			glMain.marginHeight = 7;
			glMain.marginWidth = 7;
			cmpContent.setLayout(glMain);
		}

		boolean isLabelEnabled = !getContext().getUIFactory().isSetInvisible(getAxisForProcessing().getLabel());

		Group grpLabel = new Group(cmpContent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.marginWidth = 0;
			layout.marginHeight = 10;
			grpLabel.setLayout(layout);
			grpLabel.setText(Messages.getString("BaseAxisLabelAttributeSheetImpl.Lbl.Label")); //$NON-NLS-1$
			grpLabel.setEnabled(isLabelEnabled);
		}

		lacLabel = new LabelAttributesComposite(grpLabel, SWT.NONE, getContext(), getLabelAttributesContext(), null,
				getAxisForProcessing(), "labelPosition", //$NON-NLS-1$
				"label", //$NON-NLS-1$
				defAxis, getChart().getUnits(), getPositionScope());
		GridData gdLACLabel = new GridData(GridData.FILL_HORIZONTAL);
		gdLACLabel.horizontalSpan = 2;
		lacLabel.setLayoutData(gdLACLabel);
		lacLabel.addListener(this);
		lacLabel.setEnabled(isLabelEnabled);
		switch (axisType) {
		case AngleType.X:
			lacLabel.setDefaultLabelValue(DefaultValueProvider.defBaseAxis().getLabel());
			break;
		case AngleType.Y:
			lacLabel.setDefaultLabelValue(DefaultValueProvider.defOrthogonalAxis().getLabel());
			break;
		case AngleType.Z:
			lacLabel.setDefaultLabelValue(DefaultValueProvider.defAncillaryAxis().getLabel());
			break;
		}

		Composite cmpOther = lacLabel.getGeneralComposite();

		Label lblInterval = new Label(cmpOther, SWT.NONE);
		{
			GridData gd = new GridData();
			lblInterval.setLayoutData(gd);
			lblInterval.setText(Messages.getString("AxisTextSheet.Label.Interval")); //$NON-NLS-1$
			lblInterval.setEnabled(isLabelEnabled);
		}

		iscInterval = getContext().getUIFactory().createChartSpinner(cmpOther, SWT.BORDER, getAxisForProcessing(),
				"interval", //$NON-NLS-1$
				isLabelEnabled);
		{
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.widthHint = 135;
			iscInterval.setLayoutData(gd);
			iscInterval.getWidget().setMinimum(1);
			iscInterval.getWidget().setSelection(getAxisForProcessing().getInterval());
		}

		// Ellipsis
		createEllipsis(cmpOther);

		return cmpContent;
	}

	protected LabelAttributesContext getLabelAttributesContext() {
		LabelAttributesContext attributesContext = new LabelAttributesContext();
		if (axisType == AngleType.Z) {
			attributesContext.isPositionEnabled = false;
			attributesContext.isVisibilityEnabled = false;
			attributesContext.isFontEnabled = false;
			attributesContext.isFontAlignmentEnabled = false;
		} else {
			attributesContext.isVisibilityEnabled = false;
			attributesContext.isFontEnabled = false;
			attributesContext.isFontAlignmentEnabled = false;
		}
		return attributesContext;
	}

	protected void createEllipsis(Composite cmpOther) {
		Label lbEllipsis = new Label(cmpOther, SWT.NONE);
		{
			GridData gd = new GridData();
			lbEllipsis.setLayoutData(gd);
			lbEllipsis.setText(Messages.getString("AxisLabelSheet.Label.Ellipsis")); //$NON-NLS-1$
			lbEllipsis.setEnabled(true);
		}

		boolean enableEllipsis = canEnableEllipsisUI();
		iscEllipsis = getContext().getUIFactory().createChartSpinner(cmpOther, SWT.BORDER,
				getAxisForProcessing().getLabel(), "ellipsis", //$NON-NLS-1$
				enableEllipsis);
		{
			GridData gd = new GridData(GridData.FILL_BOTH);
			iscEllipsis.setLayoutData(gd);
			iscEllipsis.getWidget().setMinimum(0);
			iscEllipsis.getWidget().setToolTipText(Messages.getString("AxisLabelSheet.Label.Ellipsis.Tooltip")); //$NON-NLS-1$
			iscEllipsis.getWidget().setSelection(getAxisForProcessing().getLabel().getEllipsis());
		}
	}

	protected boolean canEnableEllipsisUI() {
		Axis axis = getAxisForProcessing();
		return (axis.isSetType() && axis.getType() == AxisType.TEXT_LITERAL)
				|| (!axis.isSetType() && defAxis.getType() == AxisType.TEXT_LITERAL)
				|| (axis.isSetCategoryAxis() && axis.isCategoryAxis())
				|| (!axis.isSetCategoryAxis() && defAxis.isCategoryAxis());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(lacLabel)) {
			boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
			switch (event.type) {
			case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getLabel(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LabelAttributesComposite.POSITION_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing(), "labelPosition", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LabelAttributesComposite.FONT_CHANGED_EVENT:
				getAxisForProcessing().getLabel().getCaption().setFont((FontDefinition) ((Object[]) event.data)[0]);
				getAxisForProcessing().getLabel().getCaption().setColor((ColorDefinition) ((Object[]) event.data)[1]);
				break;
			case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT:
				getAxisForProcessing().getLabel().setBackground((Fill) event.data);
				break;
			case LabelAttributesComposite.SHADOW_CHANGED_EVENT:
				getAxisForProcessing().getLabel().setShadowColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getLabel().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getLabel().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT:
				getAxisForProcessing().getLabel().getOutline().setColor((ColorDefinition) event.data);
				break;
			case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getLabel().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LabelAttributesComposite.INSETS_CHANGED_EVENT:
				getAxisForProcessing().getLabel().setInsets((Insets) event.data);
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private Axis getAxisForProcessing() {
		return axis;
	}

	private int getPositionScope() {
		// Vertical position for X axis
		if (axisType == AngleType.X) {
			return LabelAttributesComposite.ALLOW_VERTICAL_POSITION;
		}
		return LabelAttributesComposite.ALLOW_HORIZONTAL_POSITION;
	}

}
