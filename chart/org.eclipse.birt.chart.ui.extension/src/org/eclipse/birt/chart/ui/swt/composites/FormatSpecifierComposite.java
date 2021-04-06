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

import java.util.HashMap;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.StringFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.StringFormatSpecifierImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 * 
 */
public class FormatSpecifierComposite extends Composite implements SelectionListener {

	private transient Button btnUndefined = null;

	private transient Button btnStandard = null;

	private transient Button btnAdvanced = null;

	private transient Button btnFraction = null;

	private transient Combo cmbDataType = null;

	// Composites for Standard Properties
	private transient Composite cmpStandardDetails = null;

	private transient StackLayout slStandardDetails = null;

	private transient Composite cmpStandardDateDetails = null;

	private transient Composite cmpStandardNumberDetails = null;

	private transient Composite cmpStandardStringDetails = null;

	private transient IFormatSpecifierUIComponent cpWrapStandardNumber = null;

	private transient IFormatSpecifierUIComponent cpWrapStandardDate = null;

	private transient IFormatSpecifierUIComponent cpWrapStandardString = null;

	private transient IFormatSpecifierUIComponent cpWrapAdvancedNumber = null;

	private transient IFormatSpecifierUIComponent cpWrapAdvancedDate = null;

	private transient IFormatSpecifierUIComponent cpWrapFractionNumber = null;

	// Composites for Advanced Properties
	private transient Composite cmpAdvancedDetails = null;

	private transient StackLayout slAdvancedDetails = null;

	private transient Composite cmpAdvancedDateDetails = null;

	private transient Composite cmpAdvancedNumberDetails = null;

	private transient Composite cmpFractionNumberDetails = null;

	private transient FormatSpecifierPreview fsp = null;

	private transient FormatSpecifier formatspecifier = null;

	private transient boolean bEnableEvents = true;

	public final static String DATA_TYPE_NONE = Messages.getString("FormatSpecifierComposite.Lbl.None"); //$NON-NLS-1$
	public final static String DATA_TYPE_NUMBER = Messages.getString("FormatSpecifierComposite.Lbl.Number"); //$NON-NLS-1$
	public final static String DATA_TYPE_DATETIME = Messages.getString("FormatSpecifierComposite.Lbl.DateTime"); //$NON-NLS-1$
	public final static String DATA_TYPE_STRING = Messages.getString("FormatSpecifierComposite.Lbl.String"); //$NON-NLS-1$

	private String[] supportedTypes = null;

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param formatspecifier
	 */
	public FormatSpecifierComposite(Composite parent, int style, FormatSpecifier formatspecifier) {
		this(parent, style, formatspecifier, new String[] { DATA_TYPE_NUMBER, DATA_TYPE_DATETIME, DATA_TYPE_STRING });
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param formatspecifier
	 * @param supportedTypes  Supported data types. Null means no supported data
	 *                        type.
	 * @since 2.2
	 */
	public FormatSpecifierComposite(Composite parent, int style, FormatSpecifier formatspecifier,
			String[] supportedTypes) {
		super(parent, style);
		this.formatspecifier = formatspecifier;
		this.supportedTypes = supportedTypes;
		init();
		placeComponents();
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
	}

	private void placeComponents() {
		// Layout for the content composite
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.marginHeight = 10;
		glContent.marginWidth = 10;
		glContent.verticalSpacing = 0;

		// Layout for the details composite
		slStandardDetails = new StackLayout();

		// Layout for the details composite
		slAdvancedDetails = new StackLayout();

		this.setLayout(glContent);

		Group grpPreview = new Group(this, SWT.NONE);
		{
			grpPreview.setLayout(new GridLayout());
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			grpPreview.setLayoutData(gd);
			grpPreview.setText(Messages.getString("FormatSpecifierComposite.Lbl.Preview")); //$NON-NLS-1$
		}

		fsp = new FormatSpecifierPreview(grpPreview, SWT.NONE, true);
		{
			GridData gd = new GridData();
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.CENTER;
			fsp.setLayoutData(gd);
		}

		createPlaceHolder(this, 2);

		Label lblDataType = new Label(this, SWT.NONE);
		GridData gdLBLDataType = new GridData();
		lblDataType.setLayoutData(gdLBLDataType);
		lblDataType.setText(Messages.getString("FormatSpecifierComposite.Lbl.DataType")); //$NON-NLS-1$

		cmbDataType = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gdCMBDataType = new GridData(GridData.FILL_HORIZONTAL);
		cmbDataType.setLayoutData(gdCMBDataType);
		cmbDataType.addSelectionListener(this);

		createPlaceHolder(this, 2);

		Composite radios = new Composite(this, SWT.NONE);
		{
			GridLayout gl = new GridLayout();
			gl.numColumns = 2;
			gl.verticalSpacing = 0;
			radios.setLayout(gl);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			radios.setLayoutData(gd);
		}

		btnUndefined = new Button(radios, SWT.RADIO);
		GridData gdBTNUndefined = new GridData(GridData.FILL_HORIZONTAL);
		gdBTNUndefined.horizontalSpan = 2;
		btnUndefined.setLayoutData(gdBTNUndefined);
		btnUndefined.setText(Messages.getString("FormatSpecifierComposite.Lbl.Undefined")); //$NON-NLS-1$
		btnUndefined.addSelectionListener(this);

		createPlaceHolder(radios, 2);

		btnStandard = new Button(radios, SWT.RADIO);
		GridData gdBTNStandard = new GridData(GridData.FILL_HORIZONTAL);
		gdBTNStandard.horizontalSpan = 2;
		btnStandard.setLayoutData(gdBTNStandard);
		btnStandard.setText(Messages.getString("FormatSpecifierComposite.Lbl.Standard")); //$NON-NLS-1$
		btnStandard.addSelectionListener(this);

		cmpStandardDetails = new Composite(radios, SWT.NONE);
		GridData gdCMPStandardDetails = new GridData(GridData.FILL_BOTH);
		gdCMPStandardDetails.horizontalIndent = 16;
		gdCMPStandardDetails.horizontalSpan = 2;
		cmpStandardDetails.setLayoutData(gdCMPStandardDetails);
		cmpStandardDetails.setLayout(slStandardDetails);

		// Date/Time details Composite
		cmpStandardDateDetails = new Composite(cmpStandardDetails, SWT.NONE);
		{
			GridLayout glDate = new GridLayout();
			glDate.verticalSpacing = 5;
			glDate.marginHeight = 0;
			glDate.marginWidth = 0;
			cmpStandardDateDetails.setLayout(glDate);
		}
		cpWrapStandardDate = new DateStandardComposite(cmpStandardDateDetails);

		// Number details Composite
		cmpStandardNumberDetails = new Composite(cmpStandardDetails, SWT.NONE);
		{
			GridLayout glNumber = new GridLayout();
			glNumber.verticalSpacing = 5;
			glNumber.marginHeight = 0;
			glNumber.marginWidth = 0;
			cmpStandardNumberDetails.setLayout(glNumber);
		}
		cpWrapStandardNumber = new NumberStandardComposite(cmpStandardNumberDetails);

		cmpStandardStringDetails = new Composite(cmpStandardDetails, SWT.NONE);
		{
			GridLayout glString = new GridLayout();
			glString.verticalSpacing = 5;
			glString.marginHeight = 0;
			glString.marginWidth = 0;
			cmpStandardStringDetails.setLayout(glString);
		}
		cpWrapStandardString = new StringStandardComposite(cmpStandardStringDetails);

		createPlaceHolder(radios, 2);

		btnAdvanced = new Button(radios, SWT.RADIO);
		GridData gdBTNAdvanced = new GridData(GridData.FILL_HORIZONTAL);
		gdBTNAdvanced.horizontalSpan = 2;
		btnAdvanced.setLayoutData(gdBTNAdvanced);
		btnAdvanced.setText(Messages.getString("FormatSpecifierComposite.Lbl.Advanced")); //$NON-NLS-1$
		btnAdvanced.addSelectionListener(this);

		cmpAdvancedDetails = new Composite(radios, SWT.NONE);
		GridData gdCMPAdvancedDetails = new GridData(GridData.FILL_BOTH);
		gdCMPAdvancedDetails.horizontalIndent = 16;
		gdCMPAdvancedDetails.horizontalSpan = 2;
		cmpAdvancedDetails.setLayoutData(gdCMPAdvancedDetails);
		cmpAdvancedDetails.setLayout(slAdvancedDetails);

		// Date/Time details Composite
		cmpAdvancedDateDetails = new Composite(cmpAdvancedDetails, SWT.NONE);
		{
			GridLayout glAdvDate = new GridLayout();
			glAdvDate.verticalSpacing = 5;
			glAdvDate.marginHeight = 0;
			glAdvDate.marginWidth = 0;
			cmpAdvancedDateDetails.setLayout(glAdvDate);
		}
		cpWrapAdvancedDate = new DateAdvancedComposite(cmpAdvancedDateDetails);

		// Number details Composite
		cmpAdvancedNumberDetails = new Composite(cmpAdvancedDetails, SWT.NONE);
		{
			GridLayout glAdvNumber = new GridLayout();
			glAdvNumber.verticalSpacing = 5;
			glAdvNumber.marginHeight = 0;
			glAdvNumber.marginWidth = 0;
			cmpAdvancedNumberDetails.setLayout(glAdvNumber);
		}
		cpWrapAdvancedNumber = new NumberAdvancedComposite(cmpAdvancedNumberDetails);

		createPlaceHolder(radios, 2);

		btnFraction = new Button(radios, SWT.RADIO);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			btnFraction.setLayoutData(gd);
			btnFraction.setText(Messages.getString("FormatSpecifierComposite.Lbl.Fraction")); //$NON-NLS-1$
			btnFraction.addSelectionListener(this);
		}

		cmpFractionNumberDetails = new Composite(radios, SWT.NONE);
		{
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalIndent = 16;
			gd.horizontalSpan = 2;
			cmpFractionNumberDetails.setLayoutData(gd);
			cmpFractionNumberDetails.setLayout(new FormLayout());
		}
		cpWrapFractionNumber = new NumberFractionComposite(cmpFractionNumberDetails);

		populateLists();
	}

	private void createPlaceHolder(Composite parent, int horizontalSpan) {
		Label label = new Label(parent, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalSpan = horizontalSpan;
		gd.heightHint = 10;
		label.setLayoutData(gd);
	}

	private void populateLists() {
		this.bEnableEvents = false;
		if (supportedTypes == null) {
			cmbDataType.add(DATA_TYPE_NONE);
		} else {
			cmbDataType.setItems(supportedTypes);
		}

		if (formatspecifier instanceof DateFormatSpecifier || formatspecifier instanceof JavaDateFormatSpecifier) {
			cmbDataType.setText(DATA_TYPE_DATETIME);
			// support the type selected
			if (cmbDataType.getText().trim().length() != 0) {
				if (formatspecifier instanceof DateFormatSpecifier) {
					btnStandard.setSelection(true);
				} else if (formatspecifier instanceof JavaDateFormatSpecifier) {
					btnAdvanced.setSelection(true);
				} else {
					btnUndefined.setSelection(true);
				}
				slStandardDetails.topControl = this.cmpStandardDateDetails;
				slAdvancedDetails.topControl = this.cmpAdvancedDateDetails;
			}
		} else if (formatspecifier instanceof NumberFormatSpecifier
				|| formatspecifier instanceof JavaNumberFormatSpecifier) {
			cmbDataType.setText(DATA_TYPE_NUMBER);
			// support the type selected
			if (cmbDataType.getText().trim().length() != 0) {
				if (formatspecifier instanceof NumberFormatSpecifier) {
					btnStandard.setSelection(true);
				} else if (formatspecifier instanceof JavaNumberFormatSpecifier) {
					btnAdvanced.setSelection(true);
				} else {
					btnUndefined.setSelection(true);
				}
				slStandardDetails.topControl = this.cmpStandardNumberDetails;
				slAdvancedDetails.topControl = this.cmpAdvancedNumberDetails;
			}
		} else if (formatspecifier instanceof FractionNumberFormatSpecifier) {
			cmbDataType.setText(DATA_TYPE_NUMBER);
			// support the type selected
			if (cmbDataType.getText().trim().length() != 0) {
				btnFraction.setSelection(true);
				slStandardDetails.topControl = this.cmpStandardNumberDetails;
				slAdvancedDetails.topControl = this.cmpAdvancedNumberDetails;
			}
		} else if (formatspecifier instanceof StringFormatSpecifier) {
			cmbDataType.setText(DATA_TYPE_STRING);
			// support the type selected
			if (cmbDataType.getText().trim().length() != 0) {
				btnStandard.setSelection(true);
				slStandardDetails.topControl = this.cmpStandardStringDetails;
			}
		}

		if (formatspecifier == null || cmbDataType.getText().trim().length() == 0) {
			cmbDataType.select(0);
			btnUndefined.setSelection(true);

			if (cmbDataType.getText().equals(DATA_TYPE_NUMBER)) {
				slStandardDetails.topControl = this.cmpStandardNumberDetails;
				slAdvancedDetails.topControl = this.cmpAdvancedNumberDetails;
			} else if (cmbDataType.getText().equals(DATA_TYPE_DATETIME)) {
				slStandardDetails.topControl = this.cmpStandardDateDetails;
				slAdvancedDetails.topControl = this.cmpAdvancedDateDetails;
			} else if (cmbDataType.getText().equals(DATA_TYPE_STRING)) {
				slStandardDetails.topControl = this.cmpStandardStringDetails;
			} else if (cmbDataType.getText().equals(DATA_TYPE_NONE)) {
				// Hide UI
				cmpFractionNumberDetails.setVisible(false);
				btnFraction.setVisible(false);
				btnStandard.setVisible(false);
				btnAdvanced.setVisible(false);
			}
		}

		cpWrapStandardDate.populateLists();
		cpWrapStandardNumber.populateLists();
		cpWrapStandardString.populateLists();
		cpWrapAdvancedNumber.populateLists();
		cpWrapAdvancedDate.populateLists();
		cpWrapFractionNumber.populateLists();
		updateUIState();

		this.layout();
		this.bEnableEvents = true;
	}

	public FormatSpecifier getFormatSpecifier() {
		if (this.btnUndefined.getSelection()) {
			return null;
		}
		// Build (or set) the format specifier instance
		formatspecifier = buildFormatSpecifier();
		return this.formatspecifier;
	}

	private FormatSpecifier buildFormatSpecifier() {
		FormatSpecifier fs = null;
		if (cmbDataType.getText().equals(DATA_TYPE_NUMBER)) {
			if (this.btnAdvanced.getSelection()) {
				fs = cpWrapAdvancedNumber.buildFormatSpecifier();
			} else if (this.btnStandard.getSelection()) {
				fs = cpWrapStandardNumber.buildFormatSpecifier();
			} else if (this.btnFraction.getSelection()) {
				fs = cpWrapFractionNumber.buildFormatSpecifier();
			}
		} else if (cmbDataType.getText().equals(DATA_TYPE_DATETIME)) {
			if (this.btnAdvanced.getSelection()) {
				fs = cpWrapAdvancedDate.buildFormatSpecifier();
			} else if (this.btnStandard.getSelection()) {
				fs = cpWrapStandardDate.buildFormatSpecifier();
			}
		} else if (cmbDataType.getText().equals(DATA_TYPE_STRING)) {
			if (this.btnStandard.getSelection()) {
				fs = cpWrapStandardString.buildFormatSpecifier();
			}
		}
		return fs;
	}

	private void updateUIState() {
		if (cmbDataType.getText().equals(DATA_TYPE_NUMBER)) {
			if (this.btnStandard.getSelection()) {
				cpWrapStandardNumber.setEnabled(true);
				cpWrapAdvancedNumber.setEnabled(false);
				cpWrapFractionNumber.setEnabled(false);
			} else if (this.btnAdvanced.getSelection()) {
				cpWrapStandardNumber.setEnabled(false);
				cpWrapAdvancedNumber.setEnabled(true);
				cpWrapFractionNumber.setEnabled(false);
			} else if (this.btnFraction.getSelection()) {
				cpWrapStandardNumber.setEnabled(false);
				cpWrapAdvancedNumber.setEnabled(false);
				cpWrapFractionNumber.setEnabled(true);
			} else {
				// Disable all properties
				cpWrapStandardNumber.setEnabled(false);
				cpWrapAdvancedNumber.setEnabled(false);
				cpWrapFractionNumber.setEnabled(false);
			}
			cmpAdvancedDetails.setVisible(true);
			btnAdvanced.setVisible(true);

			cmpFractionNumberDetails.setVisible(true);
			btnFraction.setVisible(true);
		} else if (cmbDataType.getText().equals(DATA_TYPE_DATETIME)) {
			if (this.btnStandard.getSelection()) {
				// Enable Standard properties for date
				cpWrapStandardDate.setEnabled(true);

				// Disable Standard properties for date
				cpWrapAdvancedDate.setEnabled(false);
			} else if (this.btnAdvanced.getSelection()) {
				// Disable Standard properties for date
				cpWrapStandardDate.setEnabled(false);

				// Enable Standard properties for date
				cpWrapAdvancedDate.setEnabled(true);
			} else {
				// Disable both Standard and Advanced properties
				cpWrapStandardDate.setEnabled(false);

				cpWrapAdvancedDate.setEnabled(false);
			}
			cmpAdvancedDetails.setVisible(true);
			btnAdvanced.setVisible(true);

			// Hide UI which is not existent in Date type
			cmpFractionNumberDetails.setVisible(false);
			btnFraction.setVisible(false);
		} else if (cmbDataType.getText().equals(DATA_TYPE_STRING)) {
			if (this.btnStandard.getSelection()) {
				cpWrapStandardString.setEnabled(true);
			} else {
				cpWrapStandardString.setEnabled(false);
			}
			// Hide UI which is not existent in String type
			cmpFractionNumberDetails.setVisible(false);
			btnFraction.setVisible(false);
			cmpAdvancedDetails.setVisible(false);
			btnAdvanced.setVisible(false);
		}
		fsp.setDataType(cmbDataType.getText());
		updatePreview();
	}

	/**
	 * @return A preferred size for this composite when used in a layout
	 */
	public Point getPreferredSize() {
		return new Point(200, 150);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (!bEnableEvents) {
			return;
		}
		if (e.getSource().equals(cmbDataType)) {
			if (cmbDataType.getText().equals(DATA_TYPE_NUMBER)) {
				slStandardDetails.topControl = cmpStandardNumberDetails;
				slAdvancedDetails.topControl = cmpAdvancedNumberDetails;
			} else if (cmbDataType.getText().equals(DATA_TYPE_DATETIME)) {
				slStandardDetails.topControl = cmpStandardDateDetails;
				slAdvancedDetails.topControl = cmpAdvancedDateDetails;

				// Select the default value when the current selection is
				// invalid
				if (btnFraction.getSelection()) {
					btnUndefined.setSelection(true);
					btnFraction.setSelection(false);
				}
			} else if (cmbDataType.getText().equals(DATA_TYPE_STRING)) {
				slStandardDetails.topControl = cmpStandardStringDetails;
			}
			updateUIState();
			cmpStandardDetails.layout();
			cmpAdvancedDetails.layout();
		} else if (e.getSource() instanceof Button) {
			updateUIState();
		}
	}

	private void updatePreview() {
		// Here it might throw IllegalArgumentException due to error format pattern, so
		// we should
		// catch the exception in here to ensure format dialog can be displayed
		// in any time for user editing it.
		boolean hasException = false;
		try {
			fsp.updatePreview(getFormatSpecifier());
			ChartWizard.removeException(ChartWizard.FormatSpeciCom_ID);
		} catch (IllegalArgumentException e) {
			ChartWizard.showException(ChartWizard.FormatSpeciCom_ID, e.getMessage());
			hasException = true;
		}

		if (!hasException) {
			ChartWizard.removeException();
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

	interface IFormatSpecifierUIComponent {

		void populateLists();

		FormatSpecifier buildFormatSpecifier();

		void setEnabled(boolean enabled);
	}

	private class NumberStandardComposite extends Composite
			implements IFormatSpecifierUIComponent, ModifyListener, Listener {

		private Text txtPrefix = null;

		private Text txtSuffix = null;

		private LocalizedNumberEditorComposite txtMultiplier = null;

		private Spinner iscFractionDigits = null;

		private Label lblPrefix;

		private Label lblSuffix;

		private Label lblMultiplier;

		private Label lblFractionDigit;

		private NumberStandardComposite(Composite parent) {
			super(parent, SWT.NONE);
			placeComponents();
		}

		private void placeComponents() {
			GridLayout glNumberStandard = new GridLayout();
			glNumberStandard.verticalSpacing = 5;
			glNumberStandard.numColumns = 4;
			glNumberStandard.marginHeight = 2;
			glNumberStandard.marginWidth = 2;

			GridData gdGRPNumberStandard = new GridData(GridData.FILL_BOTH);
			this.setLayoutData(gdGRPNumberStandard);
			this.setLayout(glNumberStandard);

			lblPrefix = new Label(this, SWT.NONE);
			GridData gdLBLPrefix = new GridData();
			lblPrefix.setLayoutData(gdLBLPrefix);
			lblPrefix.setText(Messages.getString("FormatSpecifierComposite.Lbl.Prefix")); //$NON-NLS-1$

			txtPrefix = new Text(this, SWT.BORDER | SWT.SINGLE);
			GridData gdTXTPrefix = new GridData(GridData.FILL_HORIZONTAL);
			gdTXTPrefix.widthHint = 60;
			txtPrefix.setLayoutData(gdTXTPrefix);
			txtPrefix.addModifyListener(this);

			lblSuffix = new Label(this, SWT.NONE);
			GridData gdLBLSuffix = new GridData();
			lblSuffix.setLayoutData(gdLBLSuffix);
			lblSuffix.setText(Messages.getString("FormatSpecifierComposite.Lbl.Suffix")); //$NON-NLS-1$

			txtSuffix = new Text(this, SWT.BORDER | SWT.SINGLE);
			GridData gdTXTSuffix = new GridData(GridData.FILL_HORIZONTAL);
			gdTXTSuffix.widthHint = 60;
			txtSuffix.setLayoutData(gdTXTSuffix);
			txtSuffix.addModifyListener(this);

			lblMultiplier = new Label(this, SWT.NONE);
			GridData gdLBLMultiplier = new GridData();
			lblMultiplier.setLayoutData(gdLBLMultiplier);
			lblMultiplier.setText(Messages.getString("FormatSpecifierComposite.Lbl.Multiplier")); //$NON-NLS-1$

			txtMultiplier = new LocalizedNumberEditorComposite(this, SWT.BORDER | SWT.SINGLE);
			new TextNumberEditorAssistField(txtMultiplier.getTextControl(), null);

			GridData gdTXTMultiplier = new GridData(GridData.FILL_HORIZONTAL);
			gdTXTMultiplier.widthHint = 60;
			txtMultiplier.setLayoutData(gdTXTMultiplier);
			txtMultiplier.addModifyListener(this);

			lblFractionDigit = new Label(this, SWT.NONE);
			GridData gdLBLFractionDigit = new GridData();
			lblFractionDigit.setLayoutData(gdLBLFractionDigit);
			lblFractionDigit.setText(Messages.getString("FormatSpecifierComposite.Lbl.FractionDigits")); //$NON-NLS-1$

			iscFractionDigits = new Spinner(this, SWT.BORDER);
			GridData gdISCFractionDigits = new GridData(GridData.FILL_HORIZONTAL);
			gdISCFractionDigits.widthHint = 60;
			iscFractionDigits.setLayoutData(gdISCFractionDigits);
			iscFractionDigits.setSelection(2);
			iscFractionDigits.addListener(SWT.Selection, this);
		}

		public void modifyText(ModifyEvent e) {
			Object oSource = e.getSource();
			bEnableEvents = false;
			if (oSource.equals(txtPrefix)) {
				if (!(formatspecifier instanceof NumberFormatSpecifier)) {
					formatspecifier = NumberFormatSpecifierImpl.create();
					((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
					((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getSelection());

					if (txtMultiplier.isSetValue()) {
						((NumberFormatSpecifier) formatspecifier).setMultiplier(txtMultiplier.getValue());
					} else {
						((NumberFormatSpecifier) formatspecifier)
								.eUnset(AttributePackage.eINSTANCE.getNumberFormatSpecifier_Multiplier());
					}
				}
				((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
			} else if (oSource.equals(txtSuffix)) {
				if (!(formatspecifier instanceof NumberFormatSpecifier)) {
					formatspecifier = NumberFormatSpecifierImpl.create();
					((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
					((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getSelection());
					if (txtMultiplier.isSetValue()) {
						((NumberFormatSpecifier) formatspecifier).setMultiplier(txtMultiplier.getValue());
					} else {
						((NumberFormatSpecifier) formatspecifier)
								.eUnset(AttributePackage.eINSTANCE.getNumberFormatSpecifier_Multiplier());
					}
				}
				((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
			} else if (oSource.equals(txtMultiplier)) {
				if (!(formatspecifier instanceof NumberFormatSpecifier)) {
					formatspecifier = NumberFormatSpecifierImpl.create();
					((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
					((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
					((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getSelection());
				}
				if (txtMultiplier.isSetValue()) {
					((NumberFormatSpecifier) formatspecifier).setMultiplier(txtMultiplier.getValue());
				} else {
					((NumberFormatSpecifier) formatspecifier)
							.eUnset(AttributePackage.eINSTANCE.getNumberFormatSpecifier_Multiplier());
				}
			}
			bEnableEvents = true;
			updatePreview();
		}

		public void setEnabled(boolean enabled) {
			this.lblPrefix.setEnabled(enabled);
			this.lblSuffix.setEnabled(enabled);
			this.lblMultiplier.setEnabled(enabled);
			this.lblFractionDigit.setEnabled(enabled);
			this.txtPrefix.setEnabled(enabled);
			this.txtSuffix.setEnabled(enabled);
			this.txtMultiplier.setEnabled(enabled);
			this.iscFractionDigits.setEnabled(enabled);
			super.setEnabled(enabled);
		}

		public void populateLists() {
			if (formatspecifier instanceof NumberFormatSpecifier) {
				String prefix = ((NumberFormatSpecifier) formatspecifier).getPrefix();
				if (prefix == null) {
					prefix = ""; //$NON-NLS-1$
				}
				String suffix = ((NumberFormatSpecifier) formatspecifier).getSuffix();
				if (suffix == null) {
					suffix = ""; //$NON-NLS-1$
				}
				int fDigits = ((NumberFormatSpecifier) formatspecifier).getFractionDigits();
				if (((NumberFormatSpecifier) formatspecifier)
						.eIsSet(AttributePackage.eINSTANCE.getNumberFormatSpecifier_Multiplier())) {
					txtMultiplier.setValue(((NumberFormatSpecifier) formatspecifier).getMultiplier());
				}
				iscFractionDigits.setSelection(fDigits);
				txtPrefix.setText(prefix);
				txtSuffix.setText(suffix);
			}
		}

		public FormatSpecifier buildFormatSpecifier() {
			FormatSpecifier fs = NumberFormatSpecifierImpl.create();
			((NumberFormatSpecifier) fs).setPrefix(txtPrefix.getText());
			((NumberFormatSpecifier) fs).setSuffix(txtSuffix.getText());
			((NumberFormatSpecifier) fs).setFractionDigits(iscFractionDigits.getSelection());
			if (txtMultiplier.isSetValue()) {
				((NumberFormatSpecifier) fs).setMultiplier(txtMultiplier.getValue());
			}
			return fs;
		}

		public void handleEvent(Event event) {
			bEnableEvents = false;
			if (event.type == SWT.Selection) {
				if (event.widget.equals(iscFractionDigits)) {
					if (!(formatspecifier instanceof NumberFormatSpecifier)) {
						formatspecifier = NumberFormatSpecifierImpl.create();
						((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
						((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
						if (txtMultiplier.isSetValue()) {
							((NumberFormatSpecifier) formatspecifier).setMultiplier(txtMultiplier.getValue());
						}
					}
					((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getSelection());
				}
			}
			bEnableEvents = true;
			updatePreview();
		}

	}

	private class DateStandardComposite extends Composite implements IFormatSpecifierUIComponent, Listener {

		private Combo cmbDateType = null;

		private Combo cmbDateForm = null;

		private Label lblDateType;

		private Label lblDateDetails;

		private DateStandardComposite(Composite parent) {
			super(parent, SWT.NONE);
			placeComponents();
		}

		private void placeComponents() {
			GridLayout glDateStandard = new GridLayout();
			glDateStandard.verticalSpacing = 5;
			glDateStandard.numColumns = 2;
			glDateStandard.marginHeight = 2;
			glDateStandard.marginWidth = 2;

			GridData gdGRPDateStandard = new GridData(GridData.FILL_BOTH);
			this.setLayoutData(gdGRPDateStandard);
			this.setLayout(glDateStandard);

			lblDateType = new Label(this, SWT.NONE);
			GridData gdLBLDateType = new GridData();
			lblDateType.setLayoutData(gdLBLDateType);
			lblDateType.setText(Messages.getString("FormatSpecifierComposite.Lbl.Type")); //$NON-NLS-1$

			cmbDateType = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
			GridData gdCMBDateType = new GridData(GridData.FILL_HORIZONTAL);
			cmbDateType.setLayoutData(gdCMBDateType);
			cmbDateType.addListener(SWT.Selection, this);

			lblDateDetails = new Label(this, SWT.NONE);
			GridData gdLBLDateDetails = new GridData();
			lblDateDetails.setLayoutData(gdLBLDateDetails);
			lblDateDetails.setText(Messages.getString("FormatSpecifierComposite.Lbl.Details")); //$NON-NLS-1$

			cmbDateForm = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
			GridData gdCMBDateForm = new GridData(GridData.FILL_HORIZONTAL);
			cmbDateForm.setLayoutData(gdCMBDateForm);
			cmbDateForm.addListener(SWT.Selection, this);
		}

		public void populateLists() {
			// Populate Date Types
			NameSet ns = LiteralHelper.dateFormatTypeSet;
			cmbDateType.setItems(ns.getDisplayNames());
			if (formatspecifier instanceof DateFormatSpecifier) {
				cmbDateType.select(ns.getSafeNameIndex(((DateFormatSpecifier) formatspecifier).getType().getName()));
			}
			if (cmbDateType.getSelectionIndex() == -1) {
				cmbDateType.select(0);
			}

			// Populate Date Details
			ns = LiteralHelper.dateFormatDetailSet;
			cmbDateForm.setItems(ns.getDisplayNames());
			if (formatspecifier instanceof DateFormatSpecifier) {
				cmbDateForm.select(ns.getSafeNameIndex(((DateFormatSpecifier) formatspecifier).getDetail().getName()));
			}
			if (cmbDateForm.getSelectionIndex() == -1) {
				cmbDateForm.select(0);
			}
		}

		public FormatSpecifier buildFormatSpecifier() {
			FormatSpecifier fs = AttributeFactory.eINSTANCE.createDateFormatSpecifier();
			((DateFormatSpecifier) fs).setType(DateFormatType
					.getByName(LiteralHelper.dateFormatTypeSet.getNameByDisplayName(cmbDateType.getText())));
			((DateFormatSpecifier) fs).setDetail(DateFormatDetail
					.getByName(LiteralHelper.dateFormatDetailSet.getNameByDisplayName(cmbDateForm.getText())));
			return fs;
		}

		public void setEnabled(boolean enabled) {
			this.lblDateDetails.setEnabled(enabled);
			this.lblDateType.setEnabled(enabled);
			this.cmbDateForm.setEnabled(enabled);
			this.cmbDateType.setEnabled(enabled);
			super.setEnabled(enabled);
		}

		public void handleEvent(Event event) {
			updatePreview();
		}
	}

	private class NumberAdvancedComposite extends Composite implements IFormatSpecifierUIComponent, ModifyListener {

		private CCombo txtNumberPattern = null;

		private LocalizedNumberEditorComposite txtAdvMultiplier = null;

		private Label lblAdvMultiplier;

		private Label lblNumberPattern;

		private NumberAdvancedComposite(Composite parent) {
			super(parent, SWT.NONE);
			placeComponents();
		}

		private void placeComponents() {
			GridLayout glNumberAdvanced = new GridLayout();
			glNumberAdvanced.verticalSpacing = 5;
			glNumberAdvanced.numColumns = 2;
			glNumberAdvanced.marginHeight = 2;
			glNumberAdvanced.marginWidth = 2;

			GridData gdGRPNumberAdvanced = new GridData(GridData.FILL_BOTH);
			this.setLayoutData(gdGRPNumberAdvanced);
			this.setLayout(glNumberAdvanced);

			lblAdvMultiplier = new Label(this, SWT.NONE);
			GridData gdLBLAdvMultiplier = new GridData();
			lblAdvMultiplier.setLayoutData(gdLBLAdvMultiplier);
			lblAdvMultiplier.setText(Messages.getString("FormatSpecifierComposite.Lbl.Multiplier")); //$NON-NLS-1$

			txtAdvMultiplier = new LocalizedNumberEditorComposite(this, SWT.BORDER | SWT.SINGLE);
			new TextNumberEditorAssistField(txtAdvMultiplier.getTextControl(), null);

			GridData gdTXTAdvMultiplier = new GridData(GridData.FILL_HORIZONTAL);
			txtAdvMultiplier.setLayoutData(gdTXTAdvMultiplier);
			txtAdvMultiplier.addModifyListener(this);

			lblNumberPattern = new Label(this, SWT.NONE);
			GridData gdLBLNumberPattern = new GridData();
			lblNumberPattern.setLayoutData(gdLBLNumberPattern);
			lblNumberPattern.setText(Messages.getString("FormatSpecifierComposite.Lbl.NumberPattern")); //$NON-NLS-1$

			txtNumberPattern = new CCombo(this, SWT.BORDER | SWT.SINGLE);
			GridData gdTXTNumberPattern = new GridData(GridData.FILL_HORIZONTAL);
			txtNumberPattern.setLayoutData(gdTXTNumberPattern);
			txtNumberPattern.addModifyListener(this);

			// set sample number patterns
			txtNumberPattern.setItems(new String[] { "##.##%", //$NON-NLS-1$
					"##.###", //$NON-NLS-1$
					"00.###", //$NON-NLS-1$
					"##,###.00", //$NON-NLS-1$
					"0.00'K'", //$NON-NLS-1$
					"##0.00 \u00A4", //$NON-NLS-1$
					"###0.000\u2030" //$NON-NLS-1$
			});
			txtNumberPattern.setVisibleItemCount(txtNumberPattern.getItemCount());
			txtNumberPattern.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {

					bEnableEvents = false;

					if (!(formatspecifier instanceof JavaNumberFormatSpecifier)) {
						formatspecifier = JavaNumberFormatSpecifierImpl.create(""); //$NON-NLS-1$
					}
					((JavaNumberFormatSpecifier) formatspecifier).setPattern(ChartUIUtil.getText(txtNumberPattern));

					bEnableEvents = true;

					updatePreview();
				}
			});

		}

		public void populateLists() {
			if (formatspecifier instanceof JavaNumberFormatSpecifier) {
				String str = ((JavaNumberFormatSpecifier) formatspecifier).getPattern();
				if (str == null) {
					str = ""; //$NON-NLS-1$
				}
				if (((JavaNumberFormatSpecifier) formatspecifier)
						.eIsSet(AttributePackage.eINSTANCE.getJavaNumberFormatSpecifier_Multiplier())) {
					txtAdvMultiplier.setValue(((JavaNumberFormatSpecifier) formatspecifier).getMultiplier());
				}
				ChartUIUtil.setText(txtNumberPattern, str);
			}
		}

		public FormatSpecifier buildFormatSpecifier() {
			FormatSpecifier fs = JavaNumberFormatSpecifierImpl.create(ChartUIUtil.getText(txtNumberPattern));
			if (txtAdvMultiplier.isSetValue()) {
				((JavaNumberFormatSpecifierImpl) fs).setMultiplier(txtAdvMultiplier.getValue());
			}
			return fs;
		}

		public void setEnabled(boolean enabled) {
			this.lblAdvMultiplier.setEnabled(enabled);
			this.lblNumberPattern.setEnabled(enabled);
			this.txtAdvMultiplier.setEnabled(enabled);
			this.txtNumberPattern.setEnabled(enabled);
			super.setEnabled(enabled);
		}

		public void modifyText(ModifyEvent e) {
			Object oSource = e.getSource();
			bEnableEvents = false;
			if (oSource.equals(txtAdvMultiplier)) {
				if (!(formatspecifier instanceof JavaNumberFormatSpecifier)) {
					formatspecifier = JavaNumberFormatSpecifierImpl.create(ChartUIUtil.getText(txtNumberPattern));
				}
				if (txtAdvMultiplier.isSetValue()) {
					((JavaNumberFormatSpecifier) formatspecifier).setMultiplier(txtAdvMultiplier.getValue());
				} else {
					((JavaNumberFormatSpecifier) formatspecifier)
							.eUnset(AttributePackage.eINSTANCE.getJavaNumberFormatSpecifier_Multiplier());
				}
			} else if (oSource.equals(txtNumberPattern)) {
				if (!(formatspecifier instanceof JavaNumberFormatSpecifier)) {
					formatspecifier = JavaNumberFormatSpecifierImpl.create(""); //$NON-NLS-1$
				}
				((JavaNumberFormatSpecifier) formatspecifier).setPattern(ChartUIUtil.getText(txtNumberPattern));
			}
			bEnableEvents = true;

			updatePreview();
		}

	}

	private class DateAdvancedComposite extends Composite implements IFormatSpecifierUIComponent, ModifyListener {

		private CCombo txtDatePattern = null;
		private Label lblDatePattern;

		private DateAdvancedComposite(Composite parent) {
			super(parent, SWT.NONE);
			placeComponents();
		}

		private void placeComponents() {
			GridLayout glDateAdvanced = new GridLayout();
			glDateAdvanced.verticalSpacing = 5;
			glDateAdvanced.numColumns = 2;
			glDateAdvanced.marginHeight = 2;
			glDateAdvanced.marginWidth = 2;

			GridData gdGRPDateAdvanced = new GridData(GridData.FILL_BOTH);
			this.setLayoutData(gdGRPDateAdvanced);
			this.setLayout(glDateAdvanced);

			lblDatePattern = new Label(this, SWT.NONE);
			GridData gdLBLDatePattern = new GridData();
			lblDatePattern.setLayoutData(gdLBLDatePattern);
			lblDatePattern.setText(Messages.getString("FormatSpecifierComposite.Lbl.DatePattern")); //$NON-NLS-1$

			txtDatePattern = new CCombo(this, SWT.BORDER | SWT.SINGLE);
			GridData gdTXTDatePattern = new GridData(GridData.FILL_HORIZONTAL);
			txtDatePattern.setLayoutData(gdTXTDatePattern);
			txtDatePattern.addModifyListener(this);

			txtDatePattern.setItems(new String[] { "EEEE,MMMM dd,yyyy", //$NON-NLS-1$
					"MM/dd/yy", //$NON-NLS-1$
					"dd-MMM-yy", //$NON-NLS-1$
					"LLL,yyyy", //$NON-NLS-1$
					"hh:mm:ss,a", //$NON-NLS-1$
					"HH:mm:ss", //$NON-NLS-1$
					"D,yyyy", //$NON-NLS-1$
					"DDD,yyyy,QQQQ", //$NON-NLS-1$
					"EEE, MMM d, yyyy", //$NON-NLS-1$
					"yyyy.MMMM.dd GGG hh:mm aaa", //$NON-NLS-1$
					"yyyy.MM.dd G 'at' HH:mm:ss zzz", //$NON-NLS-1$
					"w,yyyy", //$NON-NLS-1$
					"W,LLL,yyyy" //$NON-NLS-1$
			});
			txtDatePattern.setVisibleItemCount(txtDatePattern.getItemCount());
			txtDatePattern.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					bEnableEvents = false;

					if (!(formatspecifier instanceof JavaDateFormatSpecifier)) {
						formatspecifier = JavaDateFormatSpecifierImpl.create(""); //$NON-NLS-1$
					}
					((JavaDateFormatSpecifier) formatspecifier).setPattern(ChartUIUtil.getText(txtDatePattern));

					bEnableEvents = true;

					updatePreview();
				}
			});
		}

		public void populateLists() {
			if (formatspecifier instanceof JavaDateFormatSpecifier) {
				String str = ((JavaDateFormatSpecifier) formatspecifier).getPattern();
				if (str == null) {
					str = ""; //$NON-NLS-1$
				}
				ChartUIUtil.setText(txtDatePattern, str);
			}
		}

		public FormatSpecifier buildFormatSpecifier() {
			FormatSpecifier fs = JavaDateFormatSpecifierImpl.create(ChartUIUtil.getText(txtDatePattern));
			return fs;
		}

		public void setEnabled(boolean enabled) {
			this.lblDatePattern.setEnabled(enabled);
			this.txtDatePattern.setEnabled(enabled);
			super.setEnabled(enabled);
		}

		public void modifyText(ModifyEvent e) {
			Object oSource = e.getSource();
			bEnableEvents = false;
			if (oSource.equals(txtDatePattern)) {
				if (!(formatspecifier instanceof JavaDateFormatSpecifier)) {
					formatspecifier = JavaDateFormatSpecifierImpl.create(""); //$NON-NLS-1$
				}
				((JavaDateFormatSpecifier) formatspecifier).setPattern(ChartUIUtil.getText(txtDatePattern));
			}
			bEnableEvents = true;

			updatePreview();
		}

	}

	private class NumberFractionComposite extends Composite implements IFormatSpecifierUIComponent, Listener {

		/**
		 * This format specifier is used to initialize the UI when current format
		 * specifier is not available
		 */
		private transient FractionNumberFormatSpecifier dummyFs = FractionNumberFormatSpecifierImpl.create();

		private transient Button btnApproximate = null;

		private transient Button btnUseNumerator = null;

		private transient Button btnUseDenorminator = null;

		private transient Text txtPrefix = null;

		private transient Text txtSuffix = null;

		private transient Text txtDelimiter = null;

		private transient Spinner spnNumerator = null;

		private transient Spinner spnFractionDigits = null;

		private Label lblDelimiter;

		private Label lblPrefix;

		private Label lblSuffix;

		private NumberFractionComposite(Composite parent) {
			super(parent, SWT.NONE);
			placeComponents();
		}

		private void placeComponents() {
			GridLayout glNumberStandard = new GridLayout();
			glNumberStandard.verticalSpacing = 5;
			glNumberStandard.numColumns = 2;
			glNumberStandard.marginHeight = 2;
			glNumberStandard.marginWidth = 2;

			this.setLayout(glNumberStandard);

			Composite leftComp = new Composite(this, SWT.NONE);
			{
				leftComp.setLayout(glNumberStandard);
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				leftComp.setLayoutData(gd);
			}

			Composite rightComp = new Composite(this, SWT.NONE);
			{
				GridLayout gl = new GridLayout();
				gl.verticalSpacing = 5;
				gl.numColumns = 1;
				gl.marginHeight = 2;
				gl.marginWidth = 2;
				rightComp.setLayout(gl);
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				leftComp.setLayoutData(gd);
			}

			lblDelimiter = new Label(leftComp, SWT.NONE);
			lblDelimiter.setText(Messages.getString("FormatSpecifierComposite.Lbl.Delimiter")); //$NON-NLS-1$

			txtDelimiter = new Text(leftComp, SWT.BORDER | SWT.SINGLE);
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.widthHint = 60;
				txtDelimiter.setLayoutData(gd);
				String str = getFormatSpecifier().getDelimiter();
				if (str == null) {
					str = ""; //$NON-NLS-1$
				}
				txtDelimiter.setText(str);
				txtDelimiter.addListener(SWT.Modify, this);
			}

			btnApproximate = new Button(rightComp, SWT.CHECK);
			{
				GridData gd = new GridData();
				gd.horizontalSpan = 2;
				btnApproximate.setLayoutData(gd);
				btnApproximate.setText(Messages.getString("FormatSpecifierComposite.Lbl.Approximate")); //$NON-NLS-1$
				btnApproximate.addListener(SWT.Selection, this);
			}

			Composite radios = new Composite(rightComp, SWT.NONE);
			{
				radios.setLayout(glNumberStandard);
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				radios.setLayoutData(gd);
			}

			lblPrefix = new Label(leftComp, SWT.NONE);
			lblPrefix.setText(Messages.getString("FormatSpecifierComposite.Lbl.Prefix")); //$NON-NLS-1$

			txtPrefix = new Text(leftComp, SWT.BORDER | SWT.SINGLE);
			{
				GridData gdTXTPrefix = new GridData(GridData.FILL_HORIZONTAL);
				gdTXTPrefix.widthHint = 60;
				txtPrefix.setLayoutData(gdTXTPrefix);
				String str = getFormatSpecifier().getPrefix();
				if (str == null) {
					str = ""; //$NON-NLS-1$
				}
				txtPrefix.setText(str);
				txtPrefix.addListener(SWT.Modify, this);
			}

			btnUseNumerator = new Button(radios, SWT.RADIO);
			{
				btnUseNumerator.setText(Messages.getString("FormatSpecifierComposite.Lbl.Numerator")); //$NON-NLS-1$
				btnUseNumerator.addListener(SWT.Selection, this);
			}

			spnNumerator = new Spinner(radios, SWT.BORDER);
			{
				GridData gdTXTMultiplier = new GridData(GridData.FILL_HORIZONTAL);
				gdTXTMultiplier.widthHint = 60;
				spnNumerator.setLayoutData(gdTXTMultiplier);
				spnNumerator.setMinimum(1);
				spnNumerator.setSelection((int) getFormatSpecifier().getNumerator());
				spnNumerator
						.setToolTipText(Messages.getString("FormatSpecifierComposite.Tooltip.InputAPositiveInteger")); //$NON-NLS-1$
				spnNumerator.addListener(SWT.Selection, this);
				ChartUIUtil.addSpinnerScreenReaderAccessbility(spnNumerator,
						Messages.getString("FormatSpecifierComposite.Label.NumeratorValue")); //$NON-NLS-1$
			}

			lblSuffix = new Label(leftComp, SWT.NONE);
			lblSuffix.setText(Messages.getString("FormatSpecifierComposite.Lbl.Suffix")); //$NON-NLS-1$

			txtSuffix = new Text(leftComp, SWT.BORDER | SWT.SINGLE);
			{
				GridData gdTXTSuffix = new GridData(GridData.FILL_HORIZONTAL);
				gdTXTSuffix.widthHint = 60;
				txtSuffix.setLayoutData(gdTXTSuffix);
				String str = getFormatSpecifier().getSuffix();
				if (str == null) {
					str = ""; //$NON-NLS-1$
				}
				txtSuffix.setText(str);
				txtSuffix.addListener(SWT.Modify, this);
			}

			btnUseDenorminator = new Button(radios, SWT.RADIO);
			{
				btnUseDenorminator.setText(Messages.getString("FormatSpecifierComposite.Lbl.MaxRecursionTimes")); //$NON-NLS-1$
				btnUseDenorminator.addListener(SWT.Selection, this);
			}

			spnFractionDigits = new Spinner(radios, SWT.BORDER);
			{
				spnFractionDigits.setMinimum(1);
				spnFractionDigits.setMaximum(8);
				GridData gdISCFractionDigits = new GridData(GridData.FILL_HORIZONTAL);
				gdISCFractionDigits.widthHint = 60;
				spnFractionDigits.setLayoutData(gdISCFractionDigits);
				spnFractionDigits.setSelection(getFormatSpecifier().getFractionDigits());
				spnFractionDigits.setToolTipText(Messages.getString("FormatSpecifierComposite.Tooltip.FractionDigits")); //$NON-NLS-1$
				spnFractionDigits.addListener(SWT.Selection, this);
				ChartUIUtil.addSpinnerScreenReaderAccessbility(spnFractionDigits,
						Messages.getString("FormatSpecifierComposite.Label.DenominatorDigits")); //$NON-NLS-1$
			}
		}

		void modifyText(ModifyEvent e) {
			Object oSource = e.getSource();
			bEnableEvents = false;
			if (oSource.equals(txtPrefix)) {
				getFormatSpecifier().setPrefix(txtPrefix.getText());
			} else if (oSource.equals(txtSuffix)) {
				getFormatSpecifier().setSuffix(txtSuffix.getText());
			} else if (oSource.equals(txtDelimiter)) {
				getFormatSpecifier().setDelimiter(txtDelimiter.getText());
			}
			bEnableEvents = true;
		}

		public void setEnabled(boolean enabled) {
			this.lblDelimiter.setEnabled(enabled);
			this.lblPrefix.setEnabled(enabled);
			this.lblSuffix.setEnabled(enabled);
			this.txtDelimiter.setEnabled(enabled);
			this.txtPrefix.setEnabled(enabled);
			this.txtSuffix.setEnabled(enabled);

			this.btnApproximate.setSelection(!getFormatSpecifier().isPrecise());
			this.btnApproximate.setEnabled(enabled);

			this.btnUseNumerator.setEnabled(enabled && btnApproximate.getSelection());
			this.btnUseDenorminator.setEnabled(enabled && btnApproximate.getSelection());
			if (getFormatSpecifier().getNumerator() > 0) {
				this.btnUseNumerator.setSelection(true);
				this.btnUseDenorminator.setSelection(false);
			} else {
				this.btnUseNumerator.setSelection(false);
				this.btnUseDenorminator.setSelection(true);
			}

			this.spnNumerator.setEnabled(enabled && btnApproximate.getSelection() && btnUseNumerator.getSelection());
			this.spnFractionDigits
					.setEnabled(enabled && btnApproximate.getSelection() && btnUseDenorminator.getSelection());

			super.setEnabled(enabled);
		}

		public void populateLists() {
			if (formatspecifier instanceof FractionNumberFormatSpecifier) {
				this.setEnabled(true);
			}
		}

		private FractionNumberFormatSpecifier getFormatSpecifier() {
			return formatspecifier instanceof FractionNumberFormatSpecifier
					? (FractionNumberFormatSpecifier) formatspecifier
					: dummyFs;
		}

		public FormatSpecifier buildFormatSpecifier() {
			FractionNumberFormatSpecifier fs = FractionNumberFormatSpecifierImpl.create();
			fs.setPrecise(!btnApproximate.getSelection());
			fs.setDelimiter(txtDelimiter.getText());
			fs.setPrefix(txtPrefix.getText());
			fs.setSuffix(txtSuffix.getText());
			fs.setFractionDigits(spnFractionDigits.getSelection());
			if (btnUseNumerator.getSelection()) {
				fs.setNumerator(spnNumerator.getSelection());
			} else {
				fs.setNumerator(0);
			}
			return fs;
		}

		void widgetSelected(SelectionEvent e) {
			if (e.widget.equals(btnApproximate)) {
				getFormatSpecifier().setPrecise(!btnApproximate.getSelection());
				this.setEnabled(true);
			} else if (e.widget.equals(btnUseNumerator)) {
				this.spnNumerator.setEnabled(true);
				this.spnFractionDigits.setEnabled(false);
			} else if (e.widget.equals(btnUseDenorminator)) {
				this.spnNumerator.setEnabled(false);
				this.spnFractionDigits.setEnabled(true);
			} else if (e.widget.equals(spnFractionDigits)) {
				getFormatSpecifier().setFractionDigits(spnFractionDigits.getSelection());
			} else if (e.widget.equals(spnNumerator)) {
				getFormatSpecifier().setNumerator(spnNumerator.getSelection());
			}
		}

		public void handleEvent(Event event) {
			if (event.type == SWT.Selection) {
				widgetSelected(new SelectionEvent(event));
			} else if (event.type == SWT.Modify) {
				modifyText(new ModifyEvent(event));
			}
			updatePreview();
		}

	}

	private class StringStandardComposite extends Composite
			implements IFormatSpecifierUIComponent, ModifyListener, Listener {

		private Combo typeComb;
		private Text patternTxt;
		private HashMap<String, String> examples;
		private Label typeLbl;
		private Label patternLbl;

		private StringStandardComposite(Composite parent) {
			super(parent, SWT.NONE);
			examples = new HashMap<String, String>();
			examples.put(Messages.getString("FormatSpecifierComposite.StringFormat.UpperCase"), //$NON-NLS-1$
					">"); //$NON-NLS-1$
			examples.put(Messages.getString("FormatSpecifierComposite.StringFormat.LowerCase"), //$NON-NLS-1$
					"<"); //$NON-NLS-1$
			examples.put(Messages.getString("FormatSpecifierComposite.StringFormat.ZipCode"), //$NON-NLS-1$
					"@@@@@-@@@@"); //$NON-NLS-1$
			examples.put(Messages.getString("FormatSpecifierComposite.StringFormat.PhoneNumber"), //$NON-NLS-1$
					"(@@@)@@@-@@@@"); //$NON-NLS-1$
			examples.put(Messages.getString("FormatSpecifierComposite.StringFormat.SocialSNumber"), //$NON-NLS-1$
					"@@@-@@-@@@@"); //$NON-NLS-1$
			examples.put(Messages.getString("FormatSpecifierComposite.StringFormat.WhiteSpace"), //$NON-NLS-1$
					"^"); //$NON-NLS-1$
			placeComponents();
		}

		private void placeComponents() {
			GridLayout glStringStandard = new GridLayout();
			glStringStandard.verticalSpacing = 5;
			glStringStandard.numColumns = 2;
			glStringStandard.marginHeight = 2;
			glStringStandard.marginWidth = 2;
			GridData gdGRPStringStandard = new GridData(GridData.FILL_BOTH);
			this.setLayoutData(gdGRPStringStandard);
			this.setLayout(glStringStandard);

			typeLbl = new Label(this, SWT.NONE);
			GridData gd = new GridData();
			typeLbl.setLayoutData(gd);
			typeLbl.setText(Messages.getString("FormatSpecifierComposite.Lbl.ExampleFormats")); //$NON-NLS-1$

			typeComb = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			typeComb.setLayoutData(gd);
			typeComb.setItems(examples.keySet().toArray(new String[0]));
			typeComb.setVisibleItemCount(typeComb.getItemCount());
			typeComb.addListener(SWT.Selection, this);

			patternLbl = new Label(this, SWT.NONE);
			gd = new GridData();
			patternLbl.setLayoutData(gd);
			patternLbl.setText(Messages.getString("FormatSpecifierComposite.Lbl.Pattern")); //$NON-NLS-1$

			patternTxt = new Text(this, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			patternTxt.setLayoutData(gd);
			patternTxt.addListener(SWT.Modify, this);
		}

		@Override
		public void setEnabled(boolean enabled) {
			typeLbl.setEnabled(enabled);
			typeComb.setEnabled(enabled);
			patternLbl.setEnabled(enabled);
			patternTxt.setEnabled(enabled);
			super.setEnabled(enabled);
		}

		public FormatSpecifier buildFormatSpecifier() {
			StringFormatSpecifier sf = StringFormatSpecifierImpl.create();
			sf.setPattern(patternTxt.getText());
			return sf;
		}

		public void populateLists() {
			if (formatspecifier instanceof StringFormatSpecifier) {
				String pattern = ((StringFormatSpecifier) formatspecifier).getPattern();
				patternTxt.setText(pattern);
			}

		}

		public void modifyText(ModifyEvent e) {
			bEnableEvents = false;
			if (e.getSource() == patternTxt) {
				if (!(formatspecifier instanceof StringFormatSpecifier)) {
					formatspecifier = StringFormatSpecifierImpl.create();
				}
				((StringFormatSpecifier) formatspecifier).setPattern(patternTxt.getText());
			}
			bEnableEvents = true;
			updatePreview();
		}

		public void handleEvent(Event event) {
			bEnableEvents = false;
			if (event.type == SWT.Selection) {
				if (event.widget == typeComb) {
					patternTxt.setText(examples.get(typeComb.getText()));
				}
			}
			bEnableEvents = true;
			updatePreview();
		}

	}

}