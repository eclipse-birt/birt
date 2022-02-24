/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 * 
 */
public class GradientEditorDialog extends TrayDialog implements SelectionListener, Listener, IAngleChangeListener {

	private Composite cmpContent = null;

	private Composite cmpGeneral = null;

	private FillChooserComposite fccStartColor = null;

	private FillChooserComposite fccEndColor = null;

	private AngleSelectorComposite ascRotation = null;

	private IntegerSpinControl iscRotation = null;

	private Gradient gCurrent = null;

	private Gradient gBackup = null;

	private FillCanvas cnvPreview = null;

	private ChartWizardContext wizardContext;

	private final boolean bSupportAngle;

	/**
	 * 
	 */
	public GradientEditorDialog(Shell shellParent, ChartWizardContext wizardContext, Gradient gSelected,
			boolean bSupportAngle) {
		super(shellParent);
		setHelpAvailable(false);

		this.wizardContext = wizardContext;
		this.bSupportAngle = bSupportAngle;

		gCurrent = gSelected.copyInstance();
		gBackup = gSelected.copyInstance();
	}

	public GradientEditorDialog(Shell shellParent, ChartWizardContext wizardContext, ColorDefinition selectedColor,
			boolean bSupportAngle) {
		this(shellParent, wizardContext, FillUtil.createDefaultGradient(selectedColor), bSupportAngle);
	}

	@Override
	protected Control createContents(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.DIALOG_COLOR_GRADIENT);
		getShell().setText(Messages.getString("GradientEditorDialog.Lbl.GradientEditor")); //$NON-NLS-1$
		getShell().setSize(300, 350);
		UIHelper.centerOnScreen(getShell());
		return super.createContents(parent);
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;

		cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);
		cmpContent.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout glGeneral = new GridLayout();
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;

		cmpGeneral = new Composite(cmpContent, SWT.NONE);
		GridData gdCMPGeneral = new GridData(GridData.FILL_BOTH);
		cmpGeneral.setLayoutData(gdCMPGeneral);
		cmpGeneral.setLayout(glGeneral);

		Label lblStartColor = new Label(cmpGeneral, SWT.NONE);
		GridData gdLBLStartColor = new GridData();
		lblStartColor.setLayoutData(gdLBLStartColor);
		lblStartColor.setText(Messages.getString("GradientEditorDialog.Lbl.StartColor")); //$NON-NLS-1$

		fccStartColor = new FillChooserComposite(cmpGeneral, SWT.NONE, wizardContext, gCurrent.getStartColor(), false,
				false, false);
		GridData gdFCCStartColor = new GridData(GridData.FILL_HORIZONTAL);
		fccStartColor.setLayoutData(gdFCCStartColor);
		fccStartColor.addListener(this);

		Label lblEndColor = new Label(cmpGeneral, SWT.NONE);
		GridData gdLBLEndColor = new GridData();
		lblEndColor.setLayoutData(gdLBLEndColor);
		lblEndColor.setText(Messages.getString("GradientEditorDialog.Lbl.EndColor")); //$NON-NLS-1$

		fccEndColor = new FillChooserComposite(cmpGeneral, SWT.NONE, wizardContext, gCurrent.getEndColor(), false,
				false, false);
		GridData gdFCCEndColor = new GridData(GridData.FILL_HORIZONTAL);
		fccEndColor.setLayoutData(gdFCCEndColor);
		fccEndColor.addListener(this);

		if (bSupportAngle) {
			createRotationPanel();
		}

		Group grpPreview = new Group(cmpGeneral, SWT.NONE);
		GridData gdGRPPreview = new GridData(GridData.FILL_BOTH);
		gdGRPPreview.horizontalSpan = 2;
		grpPreview.setLayoutData(gdGRPPreview);
		grpPreview.setLayout(new FillLayout());
		grpPreview.setText(Messages.getString("GradientEditorDialog.Lbl.Preview")); //$NON-NLS-1$

		cnvPreview = new FillCanvas(grpPreview, SWT.NO_FOCUS);
		cnvPreview.setFill(gCurrent);

		return cmpContent;
	}

	private void createRotationPanel() {
		GridLayout glRotation = new GridLayout();
		glRotation.verticalSpacing = 2;
		glRotation.marginHeight = 2;
		glRotation.marginWidth = 2;

		Group grpRotation = new Group(cmpContent, SWT.NONE);
		GridData gdGRPRotation = new GridData(GridData.FILL_BOTH);
		gdGRPRotation.heightHint = 180;
		grpRotation.setLayoutData(gdGRPRotation);
		grpRotation.setLayout(glRotation);
		grpRotation.setText(Messages.getString("GradientEditorDialog.Lbl.Rotation")); //$NON-NLS-1$

		iscRotation = new IntegerSpinControl(grpRotation, SWT.NONE, (int) gCurrent.getDirection());
		GridData gdISCRotation = new GridData(GridData.FILL_HORIZONTAL);
		iscRotation.setLayoutData(gdISCRotation);
		iscRotation.setMinimum(-90);
		iscRotation.setMaximum(90);
		iscRotation.setIncrement(1);
		iscRotation.addListener(this);

		ascRotation = new AngleSelectorComposite(grpRotation, SWT.BORDER, (int) gCurrent.getDirection(),
				Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		GridData gdASCRotation = new GridData(GridData.FILL_BOTH);
		gdASCRotation.verticalSpan = 3;
		ascRotation.setLayoutData(gdASCRotation);
		ascRotation.setAngleChangeListener(this);
	}

	public Gradient getGradient() {
		return gCurrent;
	}

	@Override
	protected void cancelPressed() {
		gCurrent = gBackup;
		super.cancelPressed();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(fccStartColor)) {
			gCurrent.setStartColor((ColorDefinition) event.data);
		} else if (event.widget.equals(fccEndColor)) {
			gCurrent.setEndColor((ColorDefinition) event.data);
		} else if (event.widget.equals(iscRotation)) {
			gCurrent.setDirection(iscRotation.getValue());
			ascRotation.setAngle(iscRotation.getValue());
			ascRotation.redraw();
		}
		cnvPreview.redraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.composites.IAngleChangeListener#angleChanged
	 * (int)
	 */
	public void angleChanged(int iNewAngle) {
		iscRotation.setValue(iNewAngle);
		gCurrent.setDirection(iNewAngle);
		cnvPreview.redraw();
	}

}
