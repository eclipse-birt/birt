/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class PositiveNegativeColorDialog extends TrayDialog implements Listener {

	private transient Composite cmpContent = null;

	private transient Composite cmpPos = null;

	private transient Composite cmpNeg = null;

	private transient FillChooserComposite fccPosColor = null;

	private transient FillChooserComposite fccNegColor = null;

	private transient FillCanvas cnvPreview = null;

	private transient MultipleFill mCurrent = null;

	private transient MultipleFill mBackup = null;

	private transient ChartWizardContext wizardContext;

	public PositiveNegativeColorDialog(Shell shellParent, ChartWizardContext wizardContext, MultipleFill mSelected,
			ColorDefinition selectedColor) {
		super(shellParent);
		setHelpAvailable(false);
		this.wizardContext = wizardContext;
		if (mSelected != null) {
			mCurrent = mSelected.copyInstance();
			mBackup = mSelected.copyInstance();
		} else {
			mCurrent = AttributeFactory.eINSTANCE.createMultipleFill();
			setMultipleColor(mCurrent, selectedColor);
		}
	}

	public PositiveNegativeColorDialog(Shell shellParent, ChartWizardContext wizardContext, MultipleFill mSelected) {
		this(shellParent, wizardContext, mSelected, ColorDefinitionImpl.create(0, 0, 254));
	}

	protected Control createContents(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.DIALOG_COLOR_POSNEG);
		getShell().setText(Messages.getString("PositiveNegativeColorDialog.Lbl.PositiveNegativeColorEditor")); //$NON-NLS-1$
		getShell().setSize(420, 240);
		UIHelper.centerOnScreen(getShell());
		return super.createContents(parent);
	}

	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	protected Control createDialogArea(Composite parent) {
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;

		cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);
		cmpContent.setLayoutData(new GridData(GridData.FILL_BOTH));

		cmpPos = new Composite(cmpContent, SWT.NONE);
		cmpPos.setLayoutData(new GridData(GridData.FILL_BOTH));
		cmpPos.setLayout(new GridLayout());

		cmpNeg = new Composite(cmpContent, SWT.NONE);
		cmpNeg.setLayoutData(new GridData(GridData.FILL_BOTH));
		cmpNeg.setLayout(new GridLayout());

		Label lblStartColor = new Label(cmpPos, SWT.NONE);
		GridData gdLBLStartColor = new GridData();
		lblStartColor.setLayoutData(gdLBLStartColor);
		lblStartColor.setText(Messages.getString("PositiveNegativeColorDialog.Lbl.PositiveColor")); //$NON-NLS-1$

		fccPosColor = new FillChooserComposite(cmpPos, SWT.NONE, wizardContext, (Fill) mCurrent.getFills().get(0),
				false, false);
		GridData gdFCCStartColor = new GridData(GridData.FILL_HORIZONTAL);
		fccPosColor.setLayoutData(gdFCCStartColor);
		fccPosColor.addListener(this);

		Label lblEndColor = new Label(cmpNeg, SWT.NONE);
		GridData gdLBLEndColor = new GridData();
		lblEndColor.setLayoutData(gdLBLEndColor);
		lblEndColor.setText(Messages.getString("PositiveNegativeColorDialog.Lbl.NegativeColor")); //$NON-NLS-1$

		fccNegColor = new FillChooserComposite(cmpNeg, SWT.NONE, wizardContext, (Fill) mCurrent.getFills().get(1),
				false, false);
		GridData gdFCCEndColor = new GridData(GridData.FILL_HORIZONTAL);
		fccNegColor.setLayoutData(gdFCCEndColor);
		fccNegColor.addListener(this);

		Group grpPreview = new Group(cmpContent, SWT.NONE);
		GridData gdGRPPreview = new GridData(GridData.FILL_BOTH);
		gdGRPPreview.horizontalSpan = 2;
		grpPreview.setLayoutData(gdGRPPreview);
		grpPreview.setLayout(new FillLayout());
		grpPreview.setText(Messages.getString("PositiveNegativeColorDialog.Lbl.Preview")); //$NON-NLS-1$

		cnvPreview = new FillCanvas(grpPreview, SWT.NO_FOCUS);
		cnvPreview.setFill(mCurrent);

		return cmpContent;
	}

	private void setMultipleColor(MultipleFill mFill, ColorDefinition selectedColor) {
		int currentLuminance = convertRGBToLuminance(selectedColor.getRed(), selectedColor.getGreen(),
				selectedColor.getBlue());
		if (currentLuminance < 200) {
			mFill.getFills().add(0, selectedColor);
			ColorDefinition newColor = selectedColor.copyInstance();
			newColor.eAdapters().addAll(selectedColor.eAdapters());

			int lumDiff = 240 - currentLuminance;
			newColor.setRed(getNewColor(lumDiff, newColor.getRed(), 0.3));
			newColor.setGreen(getNewColor(lumDiff, newColor.getGreen(), 0.59));
			newColor.setBlue(getNewColor(lumDiff, newColor.getBlue(), 0.11));
			mFill.getFills().add(1, newColor);
		} else {
			mFill.getFills().add(0, selectedColor);
			ColorDefinition newColor = selectedColor.copyInstance();
			newColor.eAdapters().addAll(selectedColor.eAdapters());

			int lumDiff = -100;
			newColor.setRed(getNewColor(lumDiff, newColor.getRed(), 0.3));
			newColor.setGreen(getNewColor(lumDiff, newColor.getGreen(), 0.59));
			newColor.setBlue(getNewColor(lumDiff, newColor.getBlue(), 0.11));
			mFill.getFills().add(1, newColor);
		}
	}

	public MultipleFill getMultipleColor() {
		return mCurrent;
	}

	private int convertRGBToLuminance(int red, int green, int blue) {
		return (int) (0.3 * red + 0.59 * green + 0.11 * blue);
	}

	private int getNewColor(int lumDiff, int oldColor, double coefficient) {
		int newColor = (int) (lumDiff * coefficient) + oldColor;
		return newColor < 255 ? newColor : 255;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(fccPosColor)) {
			mCurrent.getFills().set(0, (Fill) event.data);
		} else if (event.widget.equals(fccNegColor)) {
			mCurrent.getFills().set(1, (Fill) event.data);
		}
		cnvPreview.redraw();
	}

	protected void cancelPressed() {
		mCurrent = mBackup;
		super.cancelPressed();
	}
}
