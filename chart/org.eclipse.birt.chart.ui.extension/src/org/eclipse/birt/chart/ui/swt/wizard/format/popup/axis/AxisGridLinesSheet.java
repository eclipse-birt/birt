/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis;

import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Grid;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.GridAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
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
 * Gridlines popup sheet
 */

public class AxisGridLinesSheet extends AbstractPopupSheet implements SelectionListener, Listener {

	private Composite cmpContent;

	private FillChooserComposite fccLine = null;

	private ChartCheckbox btnShow = null;

	private ChartCheckbox btnTickBetweenCategory = null;

	private Group grpMajor = null;

	private Group grpMinor = null;

	private GridAttributesComposite gacMajor = null;

	private GridAttributesComposite gacMinor = null;

	private Label lblGridCount = null;

	private ChartSpinner iscGridCount = null;

	private Axis axis;

	private int angleType;

	private ChartSpinner majGridStNum;

	private Label lblGridStepNum;

	private Label lblColor;

	private Axis defAxis;

	/**
	 * @param title     popup title
	 * @param context   wizard context
	 * @param axis      axis model
	 * @param angleType indicate axis type, value is AngleType.X or AngleType.Y or
	 *                  AngleType.Z
	 */
	public AxisGridLinesSheet(String title, ChartWizardContext context, Axis axis, int angleType, Axis defAxis) {
		super(title, context, true);
		this.axis = axis;
		this.angleType = angleType;
		this.defAxis = defAxis;
	}

	@Override
	protected void bindHelp(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_AXIS_GRIDLINES);
	}

	protected Composite getComponent(Composite parent) {
		// Layout for the content composite
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		// Layout for the general composite
		GridLayout glGeneral = new GridLayout();
		glGeneral.numColumns = 10;
		glGeneral.horizontalSpacing = 5;
		glGeneral.verticalSpacing = 5;
		glGeneral.marginHeight = 4;
		glGeneral.marginWidth = 4;

		// Layout for the Major Grid group
		FillLayout flMajor = new FillLayout();

		// Main content composite
		cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);

		// Axis Visibility
		btnShow = getContext().getUIFactory().createChartCheckbox(cmpContent, SWT.NONE,
				defAxis.getLineAttributes().isVisible());
		btnShow.setText(Messages.getString("BaseAxisAttributeSheetImpl.Lbl.ShowAxisLine")); //$NON-NLS-1$
		btnShow.setLayoutData(new GridData(GridData.FILL_BOTH));
		btnShow.setSelectionState(axis.getLineAttributes().isSetVisible()
				? (axis.getLineAttributes().isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
				: ChartCheckbox.STATE_GRAYED);
		btnShow.addSelectionListener(this);

		// Axis as Category / Value type
		if (isTickBetweenCategory()) {
			btnTickBetweenCategory = getContext().getUIFactory().createChartCheckbox(cmpContent, SWT.NONE,
					defAxis.getScale().isTickBetweenCategories());
			btnTickBetweenCategory
					.setText(Messages.getString("BaseAxisAttributeSheetImpl.Lbl.IsTickBetweenCategories")); //$NON-NLS-1$
			btnTickBetweenCategory.setSelectionState(
					axis.getScale().isSetTickBetweenCategories()
							? (axis.getScale().isTickBetweenCategories() ? ChartCheckbox.STATE_SELECTED
									: ChartCheckbox.STATE_UNSELECTED)
							: ChartCheckbox.STATE_GRAYED);
			btnTickBetweenCategory.addSelectionListener(this);
			btnTickBetweenCategory.setEnabled(!(axis.isSetCategoryAxis() && !axis.isCategoryAxis()));
		} else {
			new Label(cmpContent, SWT.NONE);
		}

		lblColor = new Label(cmpContent, SWT.NONE);
		GridData gdLBLColor = new GridData(GridData.FILL);
		lblColor.setLayoutData(gdLBLColor);
		lblColor.setText(Messages.getString("BaseAxisAttributeSheetImpl.Lbl.AxisLineColor")); //$NON-NLS-1$

		ColorDefinition clrCurrent = null;
		if (axis.eIsSet(ComponentPackage.eINSTANCE.getAxis_LineAttributes())) {
			clrCurrent = axis.getLineAttributes().getColor();
		}
		fccLine = new FillChooserComposite(cmpContent, SWT.NONE, getContext(), clrCurrent, false, false,
				getContext().getUIFactory().supportAutoUI(), true, false, false);
		GridData gdFCCLine = new GridData(GridData.FILL_BOTH);
		gdFCCLine.horizontalSpan = 1;
		gdFCCLine.heightHint = fccLine.getPreferredSize().y;
		gdFCCLine.grabExcessVerticalSpace = false;
		fccLine.setLayoutData(gdFCCLine);
		fccLine.addListener(this);
		lblColor.setEnabled(getContext().getUIFactory().canEnableUI(btnShow));
		fccLine.setEnabled(getContext().getUIFactory().canEnableUI(btnShow));

		createGridSteps();

		// Comments out for deficient support
		// Axis gap width
		// cmpGapWidth = new Composite( cmpGeneral, SWT.NONE );
		// GridData gdCMPGapWidth = new GridData( GridData.FILL_BOTH );
		// gdCMPGapWidth.horizontalSpan = 5;
		// cmpGapWidth.setLayoutData( gdCMPGapWidth );
		// cmpGapWidth.setLayout( glGapWidth );
		//
		// Label lblSpacing = new Label( cmpGapWidth, SWT.NONE );
		// GridData gdLBLSpacing = new GridData( GridData.FILL );
		// gdLBLSpacing.heightHint = 20;
		// lblSpacing.setLayoutData( gdLBLSpacing );
		// lblSpacing.setText( Messages.getString(
		// "BaseAxisAttributeSheetImpl.Lbl.GapWidth" ) ); //$NON-NLS-1$
		//
		// iscGapWidth = new IntegerSpinControl( cmpGapWidth,
		// SWT.NONE,
		// (int) axis.getGapWidth( ) );
		// GridData gdISCSpacing = new GridData( GridData.FILL_BOTH
		// | GridData.GRAB_HORIZONTAL );
		// gdISCSpacing.horizontalSpan = 6;
		// gdISCSpacing.widthHint = 100;
		// gdISCSpacing.heightHint = iscGapWidth.getPreferredSize( ).y;
		// iscGapWidth.setLayoutData( gdISCSpacing );
		// iscGapWidth.addListener( this );
		//
		// Label lblSpacingUnit = new Label( cmpGapWidth, SWT.NONE );
		// GridData gdLBLSpacingUnit = new GridData( GridData.FILL );
		// gdLBLSpacingUnit.widthHint = 20;
		// gdLBLSpacingUnit.grabExcessHorizontalSpace = false;
		// lblSpacingUnit.setLayoutData( gdLBLSpacingUnit );
		// lblSpacingUnit.setText( Messages.getString(
		// "BaseAxisAttributeSheetImpl.Lbl.PercentSign" ) ); //$NON-NLS-1$

		// Major Grid
		grpMajor = new Group(cmpContent, SWT.NONE);
		GridData gdGRPMajor = new GridData(GridData.FILL_HORIZONTAL);
		grpMajor.setLayoutData(gdGRPMajor);
		grpMajor.setText(Messages.getString("BaseAxisAttributeSheetImpl.Lbl.MajorGrid")); //$NON-NLS-1$
		grpMajor.setLayout(flMajor);

		boolean ticksVisible = (this.angleType != AngleType.Z)
				&& (getContext().getModel().getDimension().getValue() != ChartDimension.THREE_DIMENSIONAL);
		gacMajor = new GridAttributesComposite(grpMajor, SWT.NONE,
				(GridAttributesComposite.ENABLE_COLOR | GridAttributesComposite.ENABLE_STYLES
						| GridAttributesComposite.ENABLE_VISIBILITY),
				getContext(), axis.getMajorGrid(), axis.getOrientation().getValue(), ticksVisible,
				defAxis.getMajorGrid()) {
			protected void init(Grid grid) {
				super.init(grid);
				super.lineVisibleLabel = Messages.getString("AxisGridLinesSheet.MajorLabel.LineIsVisible"); //$NON-NLS-1$
				super.tickVisibleLabel = Messages.getString("AxisGridLinesSheet.MajorLabel.TickIsVisible"); //$NON-NLS-1$
			}
		};
		gacMajor.addListener(this);

		// Minor Grid
		grpMinor = new Group(cmpContent, SWT.NONE);
		{
			grpMinor.setLayout(new FillLayout());
			GridData gdGRPMinor = new GridData(GridData.FILL_HORIZONTAL);
			grpMinor.setLayoutData(gdGRPMinor);
			grpMinor.setText(Messages.getString("BaseAxisAttributeSheetImpl.Lbl.MinorGrid")); //$NON-NLS-1$
		}

		gacMinor = new GridAttributesComposite(grpMinor, SWT.NONE,
				(GridAttributesComposite.ENABLE_COLOR | GridAttributesComposite.ENABLE_STYLES
						| GridAttributesComposite.ENABLE_VISIBILITY),
				getContext(), axis.getMinorGrid(), axis.getOrientation().getValue(), ticksVisible,
				defAxis.getMinorGrid()) {
			protected void init(Grid grid) {
				super.init(grid);
				super.lineVisibleLabel = Messages.getString("AxisGridLinesSheet.MinorLabel.LineIsVisible"); //$NON-NLS-1$
				super.tickVisibleLabel = Messages.getString("AxisGridLinesSheet.MinorLabel.TickIsVisible"); //$NON-NLS-1$
			}
		};
		gacMinor.addListener(this);

		setStateOfMajorGrid();
		setStateOfMinorGrid();

		return cmpContent;
	}

	protected void createGridSteps() {
		lblGridStepNum = new Label(cmpContent, SWT.NONE);
		GridData gdLblGridStepNum = new GridData(GridData.FILL);
		lblGridStepNum.setLayoutData(gdLblGridStepNum);
		lblGridStepNum.setText(Messages.getString("BaseAxisDataSheetImpl.Lbl.MajorGridStepNum")); //$NON-NLS-1$

		Composite copMajGrid = new Composite(cmpContent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		copMajGrid.setLayout(gl);

		majGridStNum = getContext().getUIFactory().createChartSpinner(copMajGrid, SWT.BORDER,
				getAxisForProcessing().getScale(), "majorGridsStepNumber", //$NON-NLS-1$
				true);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			majGridStNum.setLayoutData(gd);
			majGridStNum.getWidget().setMinimum(1);
			majGridStNum.getWidget().setSelection(getAxisForProcessing().getScale().getMajorGridsStepNumber());
		}

		lblGridCount = new Label(cmpContent, SWT.NONE);
		GridData gdLBLGridCount = new GridData();
		lblGridCount.setLayoutData(gdLBLGridCount);
		lblGridCount.setText(Messages.getString("BaseAxisDataSheetImpl.Lbl.MinorGridCount")); //$NON-NLS-1$

		Composite copMinGrid = new Composite(cmpContent, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		copMinGrid.setLayout(gl);

		iscGridCount = getContext().getUIFactory().createChartSpinner(copMinGrid, SWT.BORDER,
				getAxisForProcessing().getScale(), "minorGridsPerUnit", //$NON-NLS-1$
				true);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			iscGridCount.setLayoutData(gd);
			iscGridCount.getWidget().setMinimum(1);
			iscGridCount.getWidget().setSelection(getAxisForProcessing().getScale().getMinorGridsPerUnit());
		}
	}

	protected boolean isTickBetweenCategory() {
		return (angleType == AngleType.X) && !(axis.isSetCategoryAxis() && !axis.isCategoryAxis());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		// if ( this.iscGapWidth.equals( event.widget ) )
		// {
		// if ( event.type == IntegerSpinControl.VALUE_CHANGED_EVENT )
		// {
		// getAxisForProcessing( ).setGapWidth( iscGapWidth.getValue( ) );
		// }
		// }
		if (this.fccLine.equals(event.widget)) {
			if (event.type == FillChooserComposite.FILL_CHANGED_EVENT) {
				getAxisForProcessing().getLineAttributes().setColor((ColorDefinition) event.data);
			}
		} else if (this.gacMajor.equals(event.widget)) {
			boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
			switch (event.type) {
			case GridAttributesComposite.LINE_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMajorGrid().getLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case GridAttributesComposite.LINE_WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMajorGrid().getLineAttributes(),
						"thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case GridAttributesComposite.LINE_COLOR_CHANGED_EVENT:
				getAxisForProcessing().getMajorGrid().getLineAttributes().setColor((ColorDefinition) event.data);
				break;
			case GridAttributesComposite.LINE_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMajorGrid().getLineAttributes(),
						"visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				setStateOfMajorGrid();
				break;
			case GridAttributesComposite.TICK_COLOR_CHANGED_EVENT:
				getAxisForProcessing().getMajorGrid().getTickAttributes().setColor((ColorDefinition) event.data);
				break;
			case GridAttributesComposite.TICK_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMajorGrid(), "tickStyle", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case GridAttributesComposite.TICK_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMajorGrid().getTickAttributes(),
						"visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			}
		} else if (this.gacMinor.equals(event.widget)) {
			boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
			switch (event.type) {
			case GridAttributesComposite.LINE_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMinorGrid().getLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case GridAttributesComposite.LINE_WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMinorGrid().getLineAttributes(),
						"thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case GridAttributesComposite.LINE_COLOR_CHANGED_EVENT:
				getAxisForProcessing().getMinorGrid().getLineAttributes().setColor((ColorDefinition) event.data);
				break;
			case GridAttributesComposite.LINE_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMinorGrid().getLineAttributes(),
						"visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				setStateOfMinorGrid();
				break;
			case GridAttributesComposite.TICK_COLOR_CHANGED_EVENT:
				getAxisForProcessing().getMinorGrid().getTickAttributes().setColor((ColorDefinition) event.data);
				break;
			case GridAttributesComposite.TICK_STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMinorGrid(), "tickStyle", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case GridAttributesComposite.TICK_VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getMinorGrid().getTickAttributes(),
						"visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				setStateOfMinorGrid();
				break;
			}
		}
	}

	protected void setStateOfMinorGrid() {
		boolean enabled;
		if (ChartUIUtil.is3DWallFloorSet(getChart())) {
			enabled = !getContext().getUIFactory()
					.isSetInvisible(getAxisForProcessing().getMinorGrid().getLineAttributes());
			if (!ChartUIUtil.is3DType(getChart())) {
				enabled = enabled || !getContext().getUIFactory()
						.isSetInvisible(getAxisForProcessing().getMinorGrid().getTickAttributes());
			}
		} else {
			enabled = false;
		}

		lblGridCount.setEnabled(enabled);
		iscGridCount.setEnabled(enabled);
	}

	protected void setStateOfMajorGrid() {
		boolean enabled;
		if (ChartUIUtil.is3DWallFloorSet(getChart())) {
			enabled = !getContext().getUIFactory()
					.isSetInvisible(getAxisForProcessing().getMajorGrid().getLineAttributes());
		} else {
			enabled = false;
		}

		lblGridStepNum.setEnabled(enabled);
		majGridStNum.setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		Object oSource = e.getSource();
		if (e.widget == btnShow) {
			boolean visible = btnShow.getSelectionState() == ChartCheckbox.STATE_SELECTED;
			// Process hiding showing of axis
			ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getLineAttributes(), "visible", //$NON-NLS-1$
					visible, btnShow.getSelectionState() == ChartCheckbox.STATE_GRAYED);
			lblColor.setEnabled(getContext().getUIFactory().canEnableUI(btnShow));
			fccLine.setEnabled(getContext().getUIFactory().canEnableUI(btnShow));
		} else if (oSource.equals(btnTickBetweenCategory)) {
			// Process setting of cross categories
			ChartElementUtil.setEObjectAttribute(getAxisForProcessing().getScale(), "tickBetweenCategories", //$NON-NLS-1$
					btnTickBetweenCategory.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnTickBetweenCategory.getSelectionState() == ChartCheckbox.STATE_GRAYED);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private Axis getAxisForProcessing() {
		return axis;
	}

}
