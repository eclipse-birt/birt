/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartInsets;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * @author Actuate Corporation
 *
 */
public class InsetsComposite extends AbstractChartInsets implements ModifyListener {

	private transient String sUnits = null;

	public static final int INSETS_CHANGED_EVENT = 1;

	protected transient Insets insets = null;

	protected transient Group grpInsets = null;

	protected transient Label lblTop = null;

	protected transient Label lblLeft = null;

	protected transient Label lblBottom = null;

	protected transient Label lblRight = null;

	protected transient LocalizedNumberEditorComposite txtTop = null;

	protected transient LocalizedNumberEditorComposite txtLeft = null;

	protected transient LocalizedNumberEditorComposite txtBottom = null;

	protected transient LocalizedNumberEditorComposite txtRight = null;

	private transient IUIServiceProvider serviceprovider = null;

	protected transient boolean bEnabled = true;

	private transient int numberRows = 2;

	protected Insets defaultInsets = null;

	private ChartWizardContext context;

	/**
	 * Creates a composite for <code>Inserts</code>. Default row number is 2.
	 *
	 * @param parent
	 * @param style
	 * @param insets
	 * @param sUnits
	 * @param serviceprovider
	 * @param context
	 */
	public InsetsComposite(Composite parent, int style, Insets insets, String sUnits,
			IUIServiceProvider serviceprovider, ChartWizardContext context) {
		this(parent, style, 2, insets, sUnits, serviceprovider, context);
	}

	/**
	 *
	 * @param parent
	 * @param style
	 * @param numberRows      specify row number. Valid number is 1,2,4.
	 * @param insets
	 * @param sUnits
	 * @param serviceprovider
	 * @param context
	 */
	public InsetsComposite(Composite parent, int style, int numberRows, Insets insets, String sUnits,
			IUIServiceProvider serviceprovider, ChartWizardContext context) {
		super(parent, style);
		this.numberRows = numberRows;
		this.insets = insets;
		this.sUnits = sUnits;
		this.serviceprovider = serviceprovider;
		this.context = context;
		if (this.sUnits == null) {
			// Get default units.
			this.sUnits = ChartDefaultValueUtil.getDefaultUnits(this.context.getModel());
		}
		init();
		placeComponents();
	}

	/**
	 *
	 */
	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
	}

	/**
	 *
	 */
	protected void placeComponents() {
		Group grpInsets = createGroup();
		createTop(grpInsets);
		createLeft(grpInsets);
		createBottom(grpInsets);
		createRight(grpInsets);
		updateInsetsData(insets);
		setModifyListener(true);
	}

	protected Group createGroup() {
		FillLayout flMain = new FillLayout();
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;

		GridLayout glGroup = new GridLayout();
		glGroup.horizontalSpacing = 8;
		glGroup.verticalSpacing = 5;
		glGroup.marginHeight = 4;
		glGroup.marginWidth = 4;
		glGroup.numColumns = 8 / numberRows;

		this.setLayout(flMain);

		grpInsets = new Group(this, SWT.NONE);
		grpInsets.setLayout(glGroup);
		grpInsets.setText(Messages.getFormattedString("InsetsComposite.Lbl.Insets", //$NON-NLS-1$
				LiteralHelper.unitsOfMeasurementSet.getDisplayNameByName(sUnits, sUnits)));

		return grpInsets;
	}

	protected void createRight(Composite grpInsets) {
		lblRight = new Label(grpInsets, SWT.NONE);
		GridData gdLRight = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		// gdLRight.heightHint = 20;
		lblRight.setLayoutData(gdLRight);
		lblRight.setText(Messages.getString("InsetsComposite.Lbl.Right")); //$NON-NLS-1$

		txtRight = new LocalizedNumberEditorComposite(grpInsets, SWT.BORDER);
		new TextNumberEditorAssistField(txtRight.getTextControl(), null);

		GridData gdTRight = new GridData(GridData.FILL_BOTH);
		// gdTRight.heightHint = 20;
		gdTRight.widthHint = 45;
		txtRight.setLayoutData(gdTRight);
	}

	protected void createBottom(Composite grpInsets) {
		lblBottom = new Label(grpInsets, SWT.NONE);
		GridData gdLBottom = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		// gdLBottom.heightHint = 20;
		lblBottom.setLayoutData(gdLBottom);
		lblBottom.setText(Messages.getString("InsetsComposite.Lbl.Bottom")); //$NON-NLS-1$

		txtBottom = new LocalizedNumberEditorComposite(grpInsets, SWT.BORDER);
		new TextNumberEditorAssistField(txtBottom.getTextControl(), null);

		GridData gdTBottom = new GridData(GridData.FILL_BOTH);
		// gdTBottom.heightHint = 20;
		gdTBottom.widthHint = 45;
		txtBottom.setLayoutData(gdTBottom);
	}

	protected void createLeft(Composite grpInsets) {
		lblLeft = new Label(grpInsets, SWT.NONE);
		GridData gdLLeft = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		// gdLLeft.heightHint = 20;
		lblLeft.setLayoutData(gdLLeft);
		lblLeft.setText(Messages.getString("InsetsComposite.Lbl.Left")); //$NON-NLS-1$

		txtLeft = new LocalizedNumberEditorComposite(grpInsets, SWT.BORDER);
		new TextNumberEditorAssistField(txtLeft.getTextControl(), null);

		GridData gdTLeft = new GridData(GridData.FILL_BOTH);
		// gdTLeft.heightHint = 20;
		gdTLeft.widthHint = 45;
		txtLeft.setLayoutData(gdTLeft);
	}

	protected void createTop(Composite grpInsets) {
		lblTop = new Label(grpInsets, SWT.NONE);
		GridData gdLTop = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		// gdLTop.heightHint = 20;
		lblTop.setLayoutData(gdLTop);
		lblTop.setText(Messages.getString("InsetsComposite.Lbl.Top")); //$NON-NLS-1$

		txtTop = new LocalizedNumberEditorComposite(grpInsets, SWT.BORDER);
		new TextNumberEditorAssistField(txtTop.getTextControl(), null);

		GridData gdTTop = new GridData(GridData.FILL_BOTH);
		// gdTTop.heightHint = 20;
		gdTTop.widthHint = 45;
		txtTop.setLayoutData(gdTTop);
	}

	protected void setModifyListener(boolean enabled) {
		if (enabled) {
			txtTop.addModifyListener(this);
			txtLeft.addModifyListener(this);
			txtRight.addModifyListener(this);
			txtBottom.addModifyListener(this);
		} else {
			txtTop.removeModifyListener(this);
			txtLeft.removeModifyListener(this);
			txtRight.removeModifyListener(this);
			txtBottom.removeModifyListener(this);
		}
	}

	protected void updateInsetsData(Insets insets) {
		double dblPoints = insets.getTop();
		double dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits); //$NON-NLS-1$
		txtTop.setValue(dblCurrent);

		dblPoints = insets.getLeft();
		dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits); //$NON-NLS-1$
		txtLeft.setValue(dblCurrent);

		dblPoints = insets.getBottom();
		dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits); //$NON-NLS-1$
		txtBottom.setValue(dblCurrent);

		dblPoints = insets.getRight();
		dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits); //$NON-NLS-1$
		txtRight.setValue(dblCurrent);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean bState) {
		super.setEnabled(bState);
		setEnabledImpl(bState);
	}

	protected void setEnabledImpl(boolean bState) {
		grpInsets.setEnabled(bState);
		updateInsetsButtons(bState);
		bEnabled = bState;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return bEnabled;
	}

	@Override
	public void setInsets(Insets insets, String sUnits) {
		if (insets == null) {
			return;
		}
		setModifyListener(false);

		this.insets = insets;
		if (sUnits != null) {
			this.sUnits = sUnits;
		}

		updateInsetsButtons(bEnabled && ChartElementUtil.isSetInsets(insets));

		// Update the UI
		double dblPoints = insets.getBottom();
		double dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits); //$NON-NLS-1$
		txtBottom.setValue(dblCurrent);

		dblPoints = insets.getLeft();
		dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits); //$NON-NLS-1$
		txtLeft.setValue(dblCurrent);

		dblPoints = insets.getTop();
		dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits); //$NON-NLS-1$
		txtTop.setValue(dblCurrent);

		dblPoints = insets.getRight();
		dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits); //$NON-NLS-1$
		txtRight.setValue(dblCurrent);

		setModifyListener(true);

		this.grpInsets.setText(Messages.getFormattedString("InsetsComposite.Lbl.Insets", //$NON-NLS-1$
				LiteralHelper.unitsOfMeasurementSet.getDisplayNameByName(this.sUnits)));
	}

	public Point getPreferredSize() {
		return new Point(300, 70);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.
	 * ModifyEvent)
	 */
	@Override
	public void modifyText(ModifyEvent event) {
		double dblCurrent = -1;
		double dblPoints = -1;
		if (event.widget.equals(txtTop)) {
			dblCurrent = txtTop.getValue();
			dblPoints = serviceprovider.getConvertedValue(dblCurrent, sUnits, "Points"); //$NON-NLS-1$
			insets.setTop(dblPoints);
		} else if (event.widget.equals(txtLeft)) {
			dblCurrent = txtLeft.getValue();
			dblPoints = serviceprovider.getConvertedValue(dblCurrent, sUnits, "Points"); //$NON-NLS-1$
			insets.setLeft(dblPoints);
		} else if (event.widget.equals(txtBottom)) {
			dblCurrent = txtBottom.getValue();
			dblPoints = serviceprovider.getConvertedValue(dblCurrent, sUnits, "Points"); //$NON-NLS-1$
			insets.setBottom(dblPoints);
		} else if (event.widget.equals(txtRight)) {
			dblCurrent = txtRight.getValue();
			dblPoints = serviceprovider.getConvertedValue(dblCurrent, sUnits, "Points"); //$NON-NLS-1$
			insets.setRight(dblPoints);
		}
	}

	protected void updateInsetsButtons(boolean bState) {
		lblTop.setEnabled(bState);
		txtTop.setEnabled(bState);
		lblLeft.setEnabled(bState);
		txtLeft.setEnabled(bState);
		lblBottom.setEnabled(bState);
		txtBottom.setEnabled(bState);
		lblRight.setEnabled(bState);
		txtRight.setEnabled(bState);
	}

	@Override
	public void setDefaultInsets(Insets insets) {
		this.defaultInsets = insets;
	}
}
