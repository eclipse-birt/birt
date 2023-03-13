/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FormatCurrencyNumPattern;
import org.eclipse.birt.report.designer.util.FormatCustomNumPattern;
import org.eclipse.birt.report.designer.util.FormatFixedNumPattern;
import org.eclipse.birt.report.designer.util.FormatNumberPattern;
import org.eclipse.birt.report.designer.util.FormatPercentNumPattern;
import org.eclipse.birt.report.designer.util.FormatScientificNumPattern;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * FormatNumberLayoutPeer
 */
public class FormatNumberLayoutPeer extends FormatLayoutPeer {

	private static final String PREVIEW_TEXT_INVALID_NUMBER_TO_PREVIEW = Messages
			.getString("FormatNumberPage.preview.invalidNumber"); //$NON-NLS-1$

	private static final String LABEL_CURRENCY_SETTINGS_GROUP = Messages
			.getString("FormatNumberPage.label.currency.settings"); //$NON-NLS-1$
	private static final String LABEL_CURRENCY_SYMBOL = Messages.getString("FormatNumberPage.label.symbol"); //$NON-NLS-1$
	private static final String LABEL_FIXED_SETTINGS_GROUP = Messages
			.getString("FormatNumberPage.label.fixed.settings"); //$NON-NLS-1$
	private static final String LABEL_PERCENT_SETTINGS_GROUP = Messages
			.getString("FormatNumberPage.label.percent.settings"); //$NON-NLS-1$
	private static final String LABEL_USE_1000S_SEPARATOR = Messages
			.getString("FormatNumberPage.label.use1000sSeparator"); //$NON-NLS-1$
	private static final String LABEL_USE_SYMBOL_SPACE = Messages.getString("FormatNumberPage.label.useSymbolSpace"); //$NON-NLS-1$
	// private static final String LABEL_USE_LEADING_ZERO = Messages.getString(
	// "FormatNumberPage.label.useLeadingZero" ); //$NON-NLS-1$
	private static final String LABEL_SYMBOL_POSITION = Messages.getString("FormatNumberPage.label.symbol.position"); //$NON-NLS-1$
	private static final String LABEL_NEGATIVE_NUMBERS = Messages.getString("FormatNumberPage.label.negative.numbers"); //$NON-NLS-1$
	private static final String LABEL_SCIENTIFIC_SETTINGS_GROUP = Messages
			.getString("FormatNumberPage.label.scientific.settings"); //$NON-NLS-1$
	private static final String LABEL_DECIMAL_PLACES = Messages.getString("FormatNumberPage.label.decimal.places"); //$NON-NLS-1$
	private static final String LABEL_ROUNDING_MODE = Messages.getString("FormatNumberPage.label.rounding.mode"); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_PREVIEW_NUMBER = Messages
			.getString("FormatNumberPage.label.preview.number"); //$NON-NLS-1$
	private static final String LABEL_GENERAL_PREVIEW_GROUP = Messages
			.getString("FormatNumberPage.label.general.preview.group"); //$NON-NLS-1$

	private static final String DEFAULT_PATTERN = "#,##0.00"; //$NON-NLS-1$

	private static final int FORMAT_TYPE_INDEX = 0;

	private static final double DEFAULT_PREVIEW_NUMBER = 1234.56;
	private static final String DEFAULT_PREVIEW_TEXT = NumberFormat.getNumberInstance(ULocale.getDefault())
			.format(DEFAULT_PREVIEW_NUMBER);

	private HashMap<String, Object> categoryPatternMaps;

	private Composite currencyPage;
	private Composite fixedPage;
	private Composite percentPage;
	private Composite scientificPage;

	private Label gPreviewLabel, cPreviewLabel, fPreviewLabel, pPreviewLabel, sPreviewLabel;

	private XCombo cPlacesChoice, cSymbolChoice, cSymPosChoice, fPlacesChoice, pSymPosChoice, pPlacesChoice,
			sPlacesChoice;

	private XCombo cRoundgingChoice, pRoundgingChoice, fRoundgingChoice, sRoundgingChoice;

	private Button cUseSep, pUseSep, fUseSep, cUseSpace;

	private List cNegNumChoice, fNegNumChoice, pNegNumChoice;

	private SelectionListener mySelectionListener = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			updatePreview();
			notifyFormatChange();
		}
	};

	private ModifyListener myModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			if (hasLoaded) {
				updatePreview();
			}
		}
	};
	private FocusListener myFocusListener = new FocusListener() {

		@Override
		public void focusLost(FocusEvent e) {
			notifyFormatChange();
		}

		@Override
		public void focusGained(FocusEvent e) {
		}
	};

	public FormatNumberLayoutPeer(int pageAlignment, boolean isFormStyle, boolean showLocale) {
		super(pageAlignment, isFormStyle, showLocale);

		this.formatAdapter = new FormatNumberAdapter();
	}

	@Override
	protected void fireFormatChanged(String newCategory, String newPattern, String newLocale) {
		fireFormatChanged(StyleHandle.NUMBER_FORMAT_PROP, newCategory, newPattern, newLocale);
	}

	@Override
	protected void createCategoryPages(Composite parent) {
		categoryPageMaps = new HashMap<>();

		categoryPageMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY, getCurrencyPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED, getFixedPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT, getPercentPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC, getScientificPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM,
				getCustomPage(parent, false, LABEL_CUSTOM_PREVIEW_NUMBER));
	}

	@Override
	protected void createCategoryPatterns() {
		categoryPatternMaps = new HashMap<>();

		categoryPatternMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER, new FormatNumberPattern());

		categoryPatternMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY,
				new FormatCurrencyNumPattern(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY));

		categoryPatternMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED,
				new FormatFixedNumPattern(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED));

		categoryPatternMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT,
				new FormatPercentNumPattern(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT));

		categoryPatternMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC,
				new FormatScientificNumPattern(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC));

		categoryPatternMaps.put(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM,
				new FormatCustomNumPattern(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM));
	}

	@Override
	protected String getDefaultPreviewText() {
		return DEFAULT_PREVIEW_TEXT;
	}

	@Override
	protected void updateDefaultsByLocale() {
		ULocale locale = getLocaleByDisplayName(localeChoicer.getText());
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		String category = formatAdapter.getCategory4DisplayName(typeChoicer.getText());

		if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY)) {
			String defaultPosition = FormatCurrencyNumPattern.getDefaultSymbolPosition(locale);
			boolean defaultUseSpace = FormatCurrencyNumPattern.getDefaultUsingSymbolSpace(locale);
			int defaultDigits = FormatCurrencyNumPattern.getDefaultFractionDigits(locale);

			cUseSpace.setSelection(defaultUseSpace);
			if (defaultPosition != null) {
				cSymPosChoice.setText(defaultPosition);
			}
			cPlacesChoice.setText(String.valueOf(defaultDigits));
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT)) {
			String defaultPosition = FormatPercentNumPattern.getDefaultSymbolPosition(locale);

			if (defaultPosition != null) {
				pSymPosChoice.setText(defaultPosition);
			}
		}

	}

	@Override
	protected void initiatePageLayout(String categoryStr, String patternStr, String localeStr) {
		initiatePageLayout(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM, categoryStr, patternStr, localeStr);

		// Fixed bug 314201, reset the CategoryPatternMaps when init the page.
		createCategoryPatterns();

		FormatNumberPattern fmtPattern = (FormatNumberPattern) categoryPatternMaps.get(categoryStr);

		if (fmtPattern == null) {
			return;
		}

		fmtPattern.setPattern(patternStr);

		if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY.equals(categoryStr)) {
			refreshCurrencySetting((FormatCurrencyNumPattern) fmtPattern);
		} else if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED.equals(categoryStr)) {
			refreshFixedSetting((FormatFixedNumPattern) fmtPattern);
		} else if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT.equals(categoryStr)) {
			refreshPercentSetting((FormatPercentNumPattern) fmtPattern);
		} else if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC.equals(categoryStr)) {
			refreshScientificSetting((FormatScientificNumPattern) fmtPattern);
		} else if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM.equals(categoryStr)) {
			refreshCustomSetting((FormatCustomNumPattern) fmtPattern);
		}
	}

	@Override
	protected void reLayoutSubPages() {
		reLayoutSubPages(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
	}

	private void refreshCurrencySetting(FormatCurrencyNumPattern fmtPattern) {
		cPlacesChoice.setText(String.valueOf(fmtPattern.getDecPlaces()));
		cUseSep.setSelection(fmtPattern.getUseSep());
		cUseSpace.setSelection(fmtPattern.getUseSpace());
		cRoundgingChoice.select(FormatNumberPattern.getRoundingModeIndexByValue(fmtPattern.getRoundingMode()));
		if (!StringUtil.isBlank(fmtPattern.getSymbol())) {
			cSymbolChoice.setText(fmtPattern.getSymbol());
		} else {
			cSymbolChoice.setText(FormatNumberPattern.TEXT_CURRENCY_SYMBOL_NONE);
		}
		if (!StringUtil.isBlank(fmtPattern.getSymPos())) {
			cSymPosChoice.setText(fmtPattern.getSymPos());
			cSymPosChoice.setEnabled(true);
		}
		if (cSymbolChoice.getSelectionIndex() == 0) {
			cSymPosChoice.deselectAll();
			cSymPosChoice.setEnabled(false);
			cUseSpace.setEnabled(false);
		} else {
			if (!cSymPosChoice.isEnabled()) {
				cSymPosChoice.setEnabled(true);
				cSymPosChoice.select(1);
			}
			cUseSpace.setEnabled(true);
		}
		if (fmtPattern.getUseBracket()) {
			cNegNumChoice.select(1);
		} else {
			cNegNumChoice.select(0);
		}
	}

	private void refreshFixedSetting(FormatFixedNumPattern fmtPattern) {
		fPlacesChoice.setText(String.valueOf(fmtPattern.getDecPlaces()));
		fRoundgingChoice.select(FormatNumberPattern.getRoundingModeIndexByValue(fmtPattern.getRoundingMode()));
		fUseSep.setSelection(fmtPattern.getUseSep());
		// fUseZero.setSelection( fmtPattern.getUseZero( ) );
		if (fmtPattern.getUseBracket()) {
			fNegNumChoice.select(1);
		} else {
			fNegNumChoice.select(0);
		}
	}

	private void refreshPercentSetting(FormatPercentNumPattern fmtPattern) {
		pPlacesChoice.setText(String.valueOf(fmtPattern.getDecPlaces()));
		pRoundgingChoice.select(FormatNumberPattern.getRoundingModeIndexByValue(fmtPattern.getRoundingMode()));
		pUseSep.setSelection(fmtPattern.getUseSep());
		// pUseZero.setSelection( fmtPattern.getUseZero( ) );
		pSymPosChoice.setText(fmtPattern.getSymPos());
		if (fmtPattern.getUseBracket()) {
			pNegNumChoice.select(1);
		} else {
			pNegNumChoice.select(0);
		}
	}

	private void refreshScientificSetting(FormatScientificNumPattern fmtPattern) {
		sPlacesChoice.setText(String.valueOf(fmtPattern.getDecPlaces()));
		sRoundgingChoice.select(FormatNumberPattern.getRoundingModeIndexByValue(fmtPattern.getRoundingMode()));
	}

	private void refreshCustomSetting(FormatCustomNumPattern fmtPattern) {
		customFormatCodeTextBox.setText(fmtPattern.getPattern() == null ? "" //$NON-NLS-1$
				: fmtPattern.getPattern());
	}

	private void setFmtPatternFromControls() {
		if (categoryPatternMaps == null) {
			return;
		}
		String category = formatAdapter.getCategory4DisplayName(typeChoicer.getText());

		if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY)) {
			FormatCurrencyNumPattern pattern = (FormatCurrencyNumPattern) categoryPatternMaps.get(category);
			String places = cPlacesChoice.getText();
			pattern.setDecPlaces(DEUtil.isValidInteger(places) ? Integer.parseInt(places) : 0);
			pattern.setRoundingMode(FormatNumberPattern.getRoundingModeByName(cRoundgingChoice.getText()));
			pattern.setUseSep(cUseSep.getSelection());
			pattern.setUseSpace(cUseSpace.getSelection());
			pattern.setSymbol(cSymbolChoice.getText());
			pattern.setSymPos(cSymPosChoice.getText());
			pattern.setUseBracket(cNegNumChoice.getSelectionIndex() == 1);
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED)) {
			FormatFixedNumPattern pattern = (FormatFixedNumPattern) categoryPatternMaps.get(category);
			String places = fPlacesChoice.getText();
			pattern.setDecPlaces(DEUtil.isValidInteger(places) ? Integer.parseInt(places) : 0);
			pattern.setRoundingMode(FormatNumberPattern.getRoundingModeByName(fRoundgingChoice.getText()));
			pattern.setUseSep(fUseSep.getSelection());
			// pattern.setUseZero( fUseZero.getSelection( ) );
			pattern.setUseBracket(fNegNumChoice.getSelectionIndex() == 1);
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT)) {
			FormatPercentNumPattern pattern = (FormatPercentNumPattern) categoryPatternMaps.get(category);
			String places = pPlacesChoice.getText();
			pattern.setDecPlaces(DEUtil.isValidInteger(places) ? Integer.parseInt(places) : 0);
			pattern.setRoundingMode(FormatNumberPattern.getRoundingModeByName(pRoundgingChoice.getText()));
			pattern.setUseSep(pUseSep.getSelection());
			// pattern.setUseZero( pUseZero.getSelection( ) );
			pattern.setSymPos(pSymPosChoice.getText());
			pattern.setUseBracket(pNegNumChoice.getSelectionIndex() == 1);
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC)) {
			FormatScientificNumPattern pattern = (FormatScientificNumPattern) categoryPatternMaps.get(category);
			String places = sPlacesChoice.getText();
			pattern.setDecPlaces(DEUtil.isValidInteger(places) ? Integer.parseInt(places) : 0);
			pattern.setRoundingMode(FormatNumberPattern.getRoundingModeByName(sRoundgingChoice.getText()));
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM)) {
			FormatCustomNumPattern pattern = (FormatCustomNumPattern) categoryPatternMaps.get(category);
			pattern.setPattern(
					customFormatCodeTextBox.getText().length() == 0 ? null : customFormatCodeTextBox.getText());
		}
	}

	private Composite getGeneralPage(Composite parent) {
		if (generalPage == null) {
			generalPage = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(1, false);
			layout.marginHeight = 0;
			generalPage.setLayout(layout);

			gPreviewLabel = createGeneralPreviewPart4Page(generalPage);
		}
		return generalPage;
	}

	private Composite getCurrencyPage(Composite parent) {
		if (currencyPage == null) {
			currencyPage = new Composite(parent, SWT.NULL);
			currencyPage.setLayout(createGridLayout4Page());

			createCurrencySettingPart(currencyPage);
			cPreviewLabel = createGeneralPreviewPart4Page(currencyPage);
		}
		return currencyPage;
	}

	private Composite getFixedPage(Composite parent) {
		if (fixedPage == null) {
			fixedPage = new Composite(parent, SWT.NULL);
			fixedPage.setLayout(createGridLayout4Page());

			createFixedSettingPart(fixedPage);
			fPreviewLabel = createGeneralPreviewPart4Page(fixedPage);

		}
		return fixedPage;
	}

	private Composite getPercentPage(Composite parent) {
		if (percentPage == null) {
			percentPage = new Composite(parent, SWT.NULL);
			percentPage.setLayout(createGridLayout4Page());

			createPercentSettingPart(percentPage);
			pPreviewLabel = createGeneralPreviewPart4Page(percentPage);

		}
		return percentPage;
	}

	private Composite getScientificPage(Composite parent) {
		if (scientificPage == null) {
			scientificPage = new Composite(parent, SWT.NULL);
			scientificPage.setLayout(createGridLayout4Page());

			createScientificSettingPart(scientificPage);
			sPreviewLabel = createGeneralPreviewPart4Page(scientificPage);
		}
		return scientificPage;
	}

	private void createCurrencySettingPart(Composite parent) {
		Group setting;
		if (isFormStyle) {
			setting = FormWidgetFactory.getInstance().createGroup(parent, ""); //$NON-NLS-1$
		} else {
			setting = new Group(parent, SWT.NONE);
		}
		setting.setText(LABEL_CURRENCY_SETTINGS_GROUP);
		setting.setLayoutData(createGridData4Part());
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 4;
		layout.verticalSpacing = 5;
		setting.setLayout(layout);

		FormWidgetFactory.getInstance().createLabel(setting, isFormStyle).setText(LABEL_DECIMAL_PLACES);
		cPlacesChoice = new XCombo(setting, false, isFormStyle);

		cPlacesChoice.setItems(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		});
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		// data.heightHint = 20;
		cPlacesChoice.setLayoutData(data);
		cPlacesChoice.addSelectionListener(mySelectionListener);
		cPlacesChoice.addModifyListener(myModifyListener);
		cPlacesChoice.addFocusListener(myFocusListener);
		cPlacesChoice.select(2);

		FormWidgetFactory.getInstance().createLabel(setting, isFormStyle).setText(LABEL_ROUNDING_MODE);
		cRoundgingChoice = new XCombo(setting, true, isFormStyle);

		cRoundgingChoice.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		cRoundgingChoice.setItems(FormatNumberPattern.ROUNDING_MODES_NAMES);
		cRoundgingChoice.addSelectionListener(mySelectionListener);
		cRoundgingChoice.select(0);

		cUseSep = FormWidgetFactory.getInstance().createButton(setting, SWT.CHECK, isFormStyle);
		cUseSep.setText(LABEL_USE_1000S_SEPARATOR);
		data = new GridData();
		data.horizontalSpan = 2;
		cUseSep.setLayoutData(data);
		cUseSep.addSelectionListener(mySelectionListener);

		cUseSpace = FormWidgetFactory.getInstance().createButton(setting, SWT.CHECK, isFormStyle);
		cUseSpace.setText(LABEL_USE_SYMBOL_SPACE);
		data = new GridData();
		data.horizontalSpan = 2;
		cUseSpace.setLayoutData(data);
		cUseSpace.addSelectionListener(mySelectionListener);
		cUseSpace.setEnabled(false);

		FormWidgetFactory.getInstance().createLabel(setting, isFormStyle).setText(LABEL_CURRENCY_SYMBOL);
		cSymbolChoice = new XCombo(setting, true, isFormStyle);

		cSymbolChoice.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		cSymbolChoice.setItems(FormatCurrencyNumPattern.BUILT_IN_SYMBOLS);
		cSymbolChoice.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cSymbolChoice.getSelectionIndex() == 0) {
					cSymPosChoice.deselectAll();
					cSymPosChoice.setEnabled(false);
					cUseSpace.setEnabled(false);
				} else {
					if (!cSymPosChoice.isEnabled()) {
						cSymPosChoice.setEnabled(true);

						// update to default setting according to current locale
						String defaultPosition = FormatCurrencyNumPattern.getDefaultSymbolPosition(getLocale());
						if (defaultPosition != null) {
							cSymPosChoice.setText(defaultPosition);
						} else {
							cSymPosChoice.select(1);
						}
					}

					if (!cUseSpace.isEnabled()) {
						cUseSpace.setEnabled(true);

						// update to default setting according to current locale
						boolean defaultUseSpace = FormatCurrencyNumPattern.getDefaultUsingSymbolSpace(getLocale());
						cUseSpace.setSelection(defaultUseSpace);
					}
				}
				updatePreview();
				notifyFormatChange();
			}
		});
		cSymbolChoice.select(0);

		FormWidgetFactory.getInstance().createLabel(setting, isFormStyle).setText(LABEL_SYMBOL_POSITION);
		cSymPosChoice = new XCombo(setting, true, isFormStyle);
		cSymPosChoice.setItems(
				new String[] { FormatNumberPattern.SYMBOL_POSITION_AFTER, FormatNumberPattern.SYMBOL_POSITION_BEFORE });
		cSymPosChoice.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cSymPosChoice.addSelectionListener(mySelectionListener);
		cSymPosChoice.setEnabled(false);

		Label label = FormWidgetFactory.getInstance().createLabel(setting, isFormStyle);
		label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		label.setText(LABEL_NEGATIVE_NUMBERS);

		if (isFormStyle) {
			cNegNumChoice = FormWidgetFactory.getInstance().createList(setting, SWT.SINGLE);
		} else {
			cNegNumChoice = new List(setting, SWT.SINGLE | SWT.BORDER);
		}
		cNegNumChoice.add("-" + DEFAULT_PREVIEW_TEXT); //$NON-NLS-1$
		cNegNumChoice.add("(" + DEFAULT_PREVIEW_TEXT + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		data = new GridData(GridData.FILL_BOTH);
		cNegNumChoice.setLayoutData(data);
		cNegNumChoice.addSelectionListener(mySelectionListener);
		cNegNumChoice.select(0);
	}

	private void createFixedSettingPart(Composite parent) {
		Group setting;
		if (isFormStyle) {
			setting = FormWidgetFactory.getInstance().createGroup(parent, ""); //$NON-NLS-1$
		} else {
			setting = new Group(parent, SWT.NONE);
		}

		setting.setText(LABEL_FIXED_SETTINGS_GROUP);
		setting.setLayoutData(createGridData4Part());
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 4;
		layout.verticalSpacing = 5;
		setting.setLayout(layout);

		Label label = FormWidgetFactory.getInstance().createLabel(setting, isFormStyle);
		label.setText(LABEL_DECIMAL_PLACES);
		fPlacesChoice = new XCombo(setting, false, isFormStyle);
		fPlacesChoice.setItems(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		});
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		// data.heightHint = 20;
		fPlacesChoice.setLayoutData(data);
		fPlacesChoice.addSelectionListener(mySelectionListener);
		fPlacesChoice.addModifyListener(myModifyListener);
		fPlacesChoice.addFocusListener(myFocusListener);
		fPlacesChoice.select(2);

		FormWidgetFactory.getInstance().createLabel(setting, isFormStyle).setText(LABEL_ROUNDING_MODE);
		fRoundgingChoice = new XCombo(setting, true, isFormStyle);

		fRoundgingChoice.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		fRoundgingChoice.setItems(FormatNumberPattern.ROUNDING_MODES_NAMES);
		fRoundgingChoice.addSelectionListener(mySelectionListener);
		fRoundgingChoice.select(0);

		fUseSep = FormWidgetFactory.getInstance().createButton(setting, SWT.CHECK, isFormStyle);
		fUseSep.setText(LABEL_USE_1000S_SEPARATOR);
		GridData gData = new GridData();
		gData.horizontalSpan = 2;
		fUseSep.setLayoutData(gData);
		fUseSep.addSelectionListener(mySelectionListener);

		// fUseZero = new Button( setting, SWT.CHECK );
		// fUseZero.setText( LABEL_USE_LEADING_ZERO );
		// gData = new GridData( );
		// gData.horizontalSpan = 2;
		// fUseZero.setLayoutData( gData );
		// fUseZero.addSelectionListener( mySelectionListener );

		label = FormWidgetFactory.getInstance().createLabel(setting, isFormStyle);
		label.setText(LABEL_NEGATIVE_NUMBERS);
		label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		if (isFormStyle) {
			fNegNumChoice = FormWidgetFactory.getInstance().createList(setting, SWT.SINGLE | SWT.V_SCROLL);
		} else {
			fNegNumChoice = new List(setting, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		}
		fNegNumChoice.add("-" + DEFAULT_PREVIEW_TEXT); //$NON-NLS-1$
		fNegNumChoice.add("(" + DEFAULT_PREVIEW_TEXT + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		gData = new GridData(GridData.FILL_BOTH);
		fNegNumChoice.setLayoutData(gData);
		fNegNumChoice.addSelectionListener(mySelectionListener);
		fNegNumChoice.select(0);
	}

	private void createPercentSettingPart(Composite parent) {
		Group setting;
		if (isFormStyle) {
			setting = FormWidgetFactory.getInstance().createGroup(parent, ""); //$NON-NLS-1$
		} else {
			setting = new Group(parent, SWT.NONE);
		}
		setting.setText(LABEL_PERCENT_SETTINGS_GROUP);
		setting.setLayoutData(createGridData4Part());
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 4;
		layout.verticalSpacing = 5;
		setting.setLayout(layout);

		FormWidgetFactory.getInstance().createLabel(setting, isFormStyle).setText(LABEL_DECIMAL_PLACES);
		pPlacesChoice = new XCombo(setting, false, isFormStyle);
		pPlacesChoice.setItems(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		});
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		pPlacesChoice.setLayoutData(data);
		pPlacesChoice.addSelectionListener(mySelectionListener);
		pPlacesChoice.addModifyListener(myModifyListener);
		pPlacesChoice.addFocusListener(myFocusListener);
		pPlacesChoice.select(2);

		FormWidgetFactory.getInstance().createLabel(setting, isFormStyle).setText(LABEL_ROUNDING_MODE);
		pRoundgingChoice = new XCombo(setting, true, isFormStyle);

		pRoundgingChoice.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		pRoundgingChoice.setItems(FormatNumberPattern.ROUNDING_MODES_NAMES);
		pRoundgingChoice.addSelectionListener(mySelectionListener);
		pRoundgingChoice.select(0);

		pUseSep = FormWidgetFactory.getInstance().createButton(setting, SWT.CHECK, isFormStyle);
		pUseSep.setText(LABEL_USE_1000S_SEPARATOR);
		GridData gData = new GridData();
		gData.horizontalSpan = 2;
		pUseSep.setLayoutData(gData);
		pUseSep.addSelectionListener(mySelectionListener);

		// pUseZero = new Button( setting, SWT.CHECK );
		// pUseZero.setText( LABEL_USE_LEADING_ZERO );
		// gData = new GridData( );
		// gData.horizontalSpan = 2;
		// pUseZero.setLayoutData( gData );
		// pUseZero.addSelectionListener( mySelectionListener );

		FormWidgetFactory.getInstance().createLabel(setting, isFormStyle).setText(LABEL_SYMBOL_POSITION);
		pSymPosChoice = new XCombo(setting, true, isFormStyle);
		pSymPosChoice.setItems(
				new String[] { FormatNumberPattern.SYMBOL_POSITION_AFTER, FormatNumberPattern.SYMBOL_POSITION_BEFORE });
		pSymPosChoice.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pSymPosChoice.addSelectionListener(mySelectionListener);
		pSymPosChoice.select(0);

		Label label = FormWidgetFactory.getInstance().createLabel(setting, isFormStyle);
		label.setText(LABEL_NEGATIVE_NUMBERS);
		label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		if (isFormStyle) {
			pNegNumChoice = FormWidgetFactory.getInstance().createList(setting, SWT.SINGLE | SWT.V_SCROLL);
		} else {
			pNegNumChoice = new List(setting, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		}
		pNegNumChoice.add("-" + DEFAULT_PREVIEW_TEXT); //$NON-NLS-1$
		pNegNumChoice.add("(" + DEFAULT_PREVIEW_TEXT + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		pNegNumChoice.setLayoutData(new GridData(GridData.FILL_BOTH));
		pNegNumChoice.addSelectionListener(mySelectionListener);
		pNegNumChoice.select(0);
	}

	private void createScientificSettingPart(Composite parent) {
		Group setting;
		if (isFormStyle) {
			setting = FormWidgetFactory.getInstance().createGroup(parent, ""); //$NON-NLS-1$
		} else {
			setting = new Group(parent, SWT.NONE);
		}
		setting.setText(LABEL_SCIENTIFIC_SETTINGS_GROUP);
		setting.setLayoutData(createGridData4Part());
		setting.setLayout(new GridLayout(2, false));

		Label label = FormWidgetFactory.getInstance().createLabel(setting, isFormStyle);
		label.setText(LABEL_DECIMAL_PLACES);
		sPlacesChoice = new XCombo(setting, false, isFormStyle);
		sPlacesChoice.setItems(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		});
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 60;
		sPlacesChoice.setLayoutData(data);
		sPlacesChoice.addSelectionListener(mySelectionListener);
		sPlacesChoice.addModifyListener(myModifyListener);
		pPlacesChoice.addFocusListener(myFocusListener);
		sPlacesChoice.select(2);

		FormWidgetFactory.getInstance().createLabel(setting, isFormStyle).setText(LABEL_ROUNDING_MODE);
		sRoundgingChoice = new XCombo(setting, true, isFormStyle);

		sRoundgingChoice.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		sRoundgingChoice.setItems(FormatNumberPattern.ROUNDING_MODES_NAMES);
		sRoundgingChoice.addSelectionListener(mySelectionListener);
		sRoundgingChoice.select(0);

	}

	private Label createGeneralPreviewPart4Page(Composite parent) {
		Group group;
		if (isFormStyle) {
			group = FormWidgetFactory.getInstance().createGroup(parent, ""); //$NON-NLS-1$
		} else {
			group = new Group(parent, SWT.NONE);
		}
		group.setText(LABEL_GENERAL_PREVIEW_GROUP);
		GridData data;
		if (pageAlignment == PAGE_ALIGN_HORIZONTAL) {
			data = new GridData(GridData.FILL_BOTH);
		} else {
			data = new GridData(GridData.FILL_HORIZONTAL);
		}
		group.setLayoutData(data);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = 2;
		group.setLayout(layout);

		Label previewText = FormWidgetFactory.getInstance().createLabel(group,
				SWT.CENTER | SWT.HORIZONTAL | SWT.VERTICAL, isFormStyle);
		previewText.setLayoutData(new GridData(GridData.FILL_BOTH));
		return previewText;
	}

	@Override
	protected void createTable(Composite parent) {
		super.createTable(parent);

		ULocale locale = getLocale();
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		TableColumn tableColumValue = new TableColumn(customFormatTable, SWT.NONE);
		tableColumValue.setText(LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE);
		tableColumValue.setWidth(120);
		tableColumValue.setResizable(true);

		TableColumn tableColumnDisplay = new TableColumn(customFormatTable, SWT.NONE);
		tableColumnDisplay.setText(LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT);
		tableColumnDisplay.setWidth(120);
		tableColumnDisplay.setResizable(true);

		new TableItem(customFormatTable, SWT.NONE).setText(
				new String[] { formatAdapter.getDisplayName4Category(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY),
						new NumberFormatter(FormatNumberPattern
								.getPatternForCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY, locale))
										.format(DEFAULT_PREVIEW_NUMBER) });
		new TableItem(customFormatTable, SWT.NONE).setText(
				new String[] { formatAdapter.getDisplayName4Category(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED),
						new NumberFormatter(FormatNumberPattern
								.getPatternForCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED, locale))
										.format(DEFAULT_PREVIEW_NUMBER) });
		new TableItem(customFormatTable, SWT.NONE).setText(
				new String[] { formatAdapter.getDisplayName4Category(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT),
						new NumberFormatter(FormatNumberPattern
								.getPatternForCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT, locale))
										.format(DEFAULT_PREVIEW_NUMBER) });
		new TableItem(customFormatTable, SWT.NONE).setText(new String[] {
				formatAdapter.getDisplayName4Category(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC),
				new NumberFormatter(FormatNumberPattern
						.getPatternForCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC, locale))
								.format(DEFAULT_PREVIEW_NUMBER) });

		customFormatTable.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String displayName = ((TableItem) e.item).getText(FORMAT_TYPE_INDEX);

				String pattern = formatAdapter.getPattern4DisplayName(displayName, getLocale());

				customFormatCodeTextBox.setText(pattern);

				updatePreview();
				notifyFormatChange();
			}
		});
	}

	@Override
	protected void setControlsEnabled(boolean enabled) {
		typeChoicer.setEnabled(enabled);
		localeChoicer.setEnabled(enabled);
		cPlacesChoice.setEnabled(enabled);
		cRoundgingChoice.setEnabled(enabled);
		cUseSep.setEnabled(enabled);
		cUseSpace.setEnabled(enabled);
		cSymbolChoice.setEnabled(enabled);
		cSymPosChoice.setEnabled(enabled);
		if (enabled) {
			if (cSymbolChoice.getSelectionIndex() == 0) {
				// no symbol, disable relevant controls
				if (cSymPosChoice.isEnabled()) {
					cSymPosChoice.setEnabled(false);
				}
				if (cUseSpace.isEnabled()) {
					cUseSpace.setEnabled(false);
				}
			}
		}
		cNegNumChoice.setEnabled(enabled);

		fPlacesChoice.setEnabled(enabled);
		fRoundgingChoice.setEnabled(enabled);
		fUseSep.setEnabled(enabled);
		// fUseZero.setEnabled(enabledb );
		fNegNumChoice.setEnabled(enabled);

		pPlacesChoice.setEnabled(enabled);
		pRoundgingChoice.setEnabled(enabled);
		pUseSep.setEnabled(enabled);
		// pUseZero.setEnabled( enabled );
		pSymPosChoice.setEnabled(enabled);
		pNegNumChoice.setEnabled(enabled);

		sPlacesChoice.setEnabled(enabled);
		sRoundgingChoice.setEnabled(enabled);

		customFormatCodeTextBox.setEnabled(enabled);
		customPreviewTextBox.setEnabled(enabled);
		customFormatTable.setEnabled(enabled);
	}

	@Override
	protected void setDefaultPreviewText(String text) {
		if (text == null || StringUtil.isBlank(text) || !isValidNumber(text)) {
			previewText = null;
		} else {
			previewText = text;
		}
	}

	private boolean isValidNumber(String text) {
		ULocale locale = getLocaleByDisplayName(localeChoicer.getText());
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		try {
			NumberFormat.getNumberInstance(locale).parse(text);
			return true;
		} catch (ParseException e) {
		}
		return false;
	}

	private void doPreview(String category, String patternStr, String localeName) {
		ULocale locale = getLocaleByDisplayName(localeName);
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		String fmtStr;

		double num = DEFAULT_PREVIEW_NUMBER;
		if (getPreviewText() != null) {
			try {
				if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY)) {
					num = new NumberFormatter(DEFAULT_PATTERN, locale).parse(getPreviewText()).doubleValue();
				} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED)) {
					num = new NumberFormatter(DEFAULT_PATTERN, locale).parse(getPreviewText()).doubleValue();
				} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT)) {
					num = new NumberFormatter(DEFAULT_PATTERN, locale).parse(getPreviewText()).doubleValue();
				} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC)) {
					num = new NumberFormatter(null, locale).parse(getPreviewText()).doubleValue();
				} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM)) {
					num = new NumberFormatter(null, locale).parse(getPreviewText()).doubleValue();
				}
			} catch (ParseException e) {
				ExceptionHandler.handle(e);
				num = DEFAULT_PREVIEW_NUMBER;
			}
		}

		if (category == null) {
			fmtStr = new NumberFormatter(patternStr, locale).format(num);
			gPreviewLabel.setText(validatedFmtStr(fmtStr));
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED)) {
			fmtStr = new NumberFormatter(patternStr, locale).format(num);
			gPreviewLabel.setText(validatedFmtStr(fmtStr));
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER)) {
			fmtStr = new NumberFormatter(patternStr, locale).format(num);
			gPreviewLabel.setText(validatedFmtStr(fmtStr));
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY)) {
			fmtStr = new NumberFormatter(patternStr, locale).format(num);
			cPreviewLabel.setText(validatedFmtStr(fmtStr));
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED)) {
			fmtStr = new NumberFormatter(patternStr, locale).format(num);
			fPreviewLabel.setText(validatedFmtStr(fmtStr));
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT)) {
			fmtStr = new NumberFormatter(patternStr, locale).format(num);
			pPreviewLabel.setText(validatedFmtStr(fmtStr));
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC)) {
			fmtStr = new NumberFormatter(patternStr, locale).format(num);
			if (Double.isInfinite(num)) {
				BigDecimal tempDecimal = new BigDecimal(getPreviewText());
				fmtStr = new NumberFormatter(patternStr, locale).format(tempDecimal);
			}
			sPreviewLabel.setText(validatedFmtStr(fmtStr));
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM)) {
			if (StringUtil.isBlank(customPreviewTextBox.getText()) || isValidNumber(customPreviewTextBox.getText())) {
				fmtStr = new NumberFormatter(patternStr, locale).format(num);
			} else {
				fmtStr = PREVIEW_TEXT_INVALID_NUMBER_TO_PREVIEW;
			}
			customPreviewLabel.setText(validatedFmtStr(fmtStr));
		}
	}

	@Override
	protected void updatePreview() {
		markDirty(hasLoaded);

		if (hasLoaded) {// avoid setting pattern from controls when the typechoicer is
						// selected
						// before loading.
			setFmtPatternFromControls();
		}

		String category = formatAdapter.getCategory4DisplayName(typeChoicer.getText());
		if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED.equals(category)) {
			setCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED);
			setPattern(null);
		} else {
			FormatNumberPattern fmtPattern = (FormatNumberPattern) categoryPatternMaps.get(category);

			setCategory(fmtPattern.getCategory());
			setPattern(fmtPattern.getPattern());
		}
		doPreview(getCategory(), getPattern(), this.locale);
	}

	@Override
	protected void updateTextByLocale() {
		setLocale(localeChoicer.getText());

		ULocale locale = getLocaleByDisplayName(this.locale);
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		String category = formatAdapter.getCategory4DisplayName(typeChoicer.getText());
		String priviewText = ""; //$NON-NLS-1$

		if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY)) {
			priviewText = new NumberFormatter(DEFAULT_PATTERN, locale).format(DEFAULT_PREVIEW_NUMBER);
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED)) {
			priviewText = new NumberFormatter(DEFAULT_PATTERN, locale).format(DEFAULT_PREVIEW_NUMBER);
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT)) {
			priviewText = new NumberFormatter(DEFAULT_PATTERN, locale).format(DEFAULT_PREVIEW_NUMBER);
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC)) {
			priviewText = new NumberFormatter(null, locale).format(DEFAULT_PREVIEW_NUMBER);
		} else if (category.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM)) {
			priviewText = new NumberFormatter(null, locale).format(DEFAULT_PREVIEW_NUMBER);
		}

		setFmtPatternFromControls();

		setPreviewText(priviewText);

		if (fNegNumChoice != null && fNegNumChoice.getItemCount() > 0) {
			int index = fNegNumChoice.getSelectionIndex();
			fNegNumChoice.removeAll();
			fNegNumChoice.add("-" + priviewText + ""); //$NON-NLS-1$ //$NON-NLS-2$
			fNegNumChoice.add("(" + priviewText + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			if (index > -1 && index < 2) {
				fNegNumChoice.select(index);
			} else {
				fNegNumChoice.select(0);
			}
			GridData gd = (GridData) fNegNumChoice.getLayoutData();
			gd.heightHint = fNegNumChoice.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 2;
			fNegNumChoice.setLayoutData(gd);
		}

		if (cNegNumChoice != null && cNegNumChoice.getItemCount() > 0) {
			int index = cNegNumChoice.getSelectionIndex();
			cNegNumChoice.removeAll();
			cNegNumChoice.add("-" + priviewText + ""); //$NON-NLS-1$ //$NON-NLS-2$
			cNegNumChoice.add("(" + priviewText + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			if (index > -1 && index < 2) {
				cNegNumChoice.select(index);
			} else {
				cNegNumChoice.select(0);
			}
			GridData gd = (GridData) fNegNumChoice.getLayoutData();
			gd.heightHint = fNegNumChoice.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 2;
			cNegNumChoice.setLayoutData(gd);
		}

		if (pNegNumChoice != null && pNegNumChoice.getItemCount() > 0) {
			int index = pNegNumChoice.getSelectionIndex();
			pNegNumChoice.removeAll();
			pNegNumChoice.add("-" + priviewText + ""); //$NON-NLS-1$ //$NON-NLS-2$
			pNegNumChoice.add("(" + priviewText + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			if (index > -1 && index < 2) {
				pNegNumChoice.select(index);
			} else {
				pNegNumChoice.select(0);
			}
			GridData gd = (GridData) fNegNumChoice.getLayoutData();
			gd.heightHint = fNegNumChoice.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 2;
			pNegNumChoice.setLayoutData(gd);
		}

		customFormatTable.getItem(0)
				.setText(new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY),
						new NumberFormatter(FormatNumberPattern.getPatternForCategory(
								DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY, locale), locale)
										.format(DEFAULT_PREVIEW_NUMBER) });
		customFormatTable.getItem(1)
				.setText(new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED),
						new NumberFormatter(FormatNumberPattern
								.getPatternForCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED, locale), locale)
										.format(DEFAULT_PREVIEW_NUMBER) });
		customFormatTable.getItem(2)
				.setText(new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT),
						new NumberFormatter(FormatNumberPattern.getPatternForCategory(
								DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT, locale), locale)
										.format(DEFAULT_PREVIEW_NUMBER) });
		customFormatTable.getItem(3)
				.setText(new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC),
						new NumberFormatter(FormatNumberPattern.getPatternForCategory(
								DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC, locale), locale)
										.format(DEFAULT_PREVIEW_NUMBER) });
	}

	@Override
	public String getFormatString() {
		if (category == null && pattern == null) {
			return DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED;
		}
		if (category == null) {
			category = ""; //$NON-NLS-1$
		}
		if (pattern == null) {
			pattern = ""; //$NON-NLS-1$
		}
		if (category.equals(pattern)) {
			return category;
		}
		return category + ":" + pattern; //$NON-NLS-1$
	}

	@Override
	public void setPreviewText(String text) {
		if (text == null) {
			customPreviewTextBox.setText(DEFAULT_PREVIEW_TEXT);
		} else {
			customPreviewTextBox.setText(text);
		}
	}

}
