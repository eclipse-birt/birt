/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.HashMap;

import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.FormatStringPattern;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.ibm.icu.util.ULocale;

/**
 * FormatStringLayoutPeer
 */
public class FormatStringLayoutPeer extends FormatLayoutPeer {

	private static final String LABEL_GENERAL_PREVIEW_GROUP = Messages
			.getString("FormatStringPage.label.previewWithFormat"); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_PREVIEW_STRING = Messages
			.getString("FormatStringPage.label.preview.string"); //$NON-NLS-1$

	private static final String SAMPLE_TEXT_ZIP_CODE = Messages.getString("FormatStringPage.SimpleTextZipCode"); //$NON-NLS-1$
	private static final String SAMPLE_TEXT_ZIP_C0DE4 = Messages.getString("FormatStringPage.SimpleTextZipCode4"); //$NON-NLS-1$
	private static final String SAMPLE_TEXT_PHONE_NUMBER = Messages.getString("FormatStringPage.PhoneNumber"); //$NON-NLS-1$
	private static final String SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER = Messages
			.getString("FormatStringPage.SocialSecurityNumber"); //$NON-NLS-1$
	private static final String SAMPLE_TEXT_PRESERVE_SPACE = Messages
			.getString("FormatStringPage.Preview.PreserveWhiteSpaces"); //$NON-NLS-1$

	private static final String DEFAULT_PREVIEW_TEXT = Messages.getString("FormatStringPage.default.preview.text"); //$NON-NLS-1$

	private static final int FORMAT_TYPE_INDEX = 0;

	private Label generalPreviewLabel;

	public FormatStringLayoutPeer(int pageAlignment, boolean isFormStyle, boolean showLocale) {
		super(pageAlignment, isFormStyle, showLocale);

		this.formatAdapter = new FormatStringAdapter();
	}

	@Override
	protected void fireFormatChanged(String newCategory, String newPattern, String newLocale) {
		fireFormatChanged(StyleHandle.STRING_FORMAT_PROP, newCategory, newPattern, newLocale);
	}

	@Override
	protected void createCategoryPages(Composite parent) {
		categoryPageMaps = new HashMap<String, Control>();

		categoryPageMaps.put(DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM,
				getCustomPage(parent, false, LABEL_CUSTOM_PREVIEW_STRING));
	}

	private Composite getGeneralPage(Composite parent) {
		if (generalPage == null) {
			generalPage = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(1, false);
			layout.marginHeight = 0;
			generalPage.setLayout(layout);

			generalPreviewLabel = createGeneralPreviewPart(generalPage);
		}
		return generalPage;
	}

	private Label createGeneralPreviewPart(Composite parent) {
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
		group.setLayout(new GridLayout(1, false));

		Label previewText = FormWidgetFactory.getInstance().createLabel(group,
				SWT.CENTER | SWT.HORIZONTAL | SWT.VERTICAL, isFormStyle);
		previewText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
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
		tableColumValue.setWidth(150);
		tableColumValue.setResizable(true);

		TableColumn tableColumnDisplay = new TableColumn(customFormatTable, SWT.NONE);
		tableColumnDisplay.setText(LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT);
		tableColumnDisplay.setWidth(200);
		tableColumnDisplay.setResizable(true);

		new TableItem(customFormatTable, SWT.NONE).setText(new String[] {
				formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE),
				new StringFormatter(FormatStringPattern
						.getPatternForCategory(DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE, locale), locale)
								.format(DEFAULT_PREVIEW_TEXT) });
		new TableItem(customFormatTable, SWT.NONE).setText(new String[] {
				formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE),
				new StringFormatter(FormatStringPattern
						.getPatternForCategory(DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE, locale), locale)
								.format(DEFAULT_PREVIEW_TEXT) });
		new TableItem(customFormatTable, SWT.NONE).setText(new String[] {
				formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4),
				new StringFormatter(FormatStringPattern
						.getPatternForCategory(DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4, locale), locale)
								.format(SAMPLE_TEXT_ZIP_C0DE4) });
		new TableItem(customFormatTable, SWT.NONE).setText(new String[] {
				formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER),
				new StringFormatter(FormatStringPattern
						.getPatternForCategory(DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER, locale), locale)
								.format(SAMPLE_TEXT_PHONE_NUMBER) });
		new TableItem(customFormatTable, SWT.NONE).setText(new String[] {
				formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER),
				new StringFormatter(FormatStringPattern.getPatternForCategory(
						DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER, locale), locale)
								.format(SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER) });
		new TableItem(customFormatTable, SWT.NONE).setText(new String[] {
				formatAdapter.getDisplayName4Category(FormatStringAdapter.STRING_FORMAT_TYPE_PRESERVE_SPACE),
				new StringFormatter(FormatStringPattern
						.getPatternForCategory(FormatStringAdapter.STRING_FORMAT_TYPE_PRESERVE_SPACE, locale), locale)
								.format(SAMPLE_TEXT_PRESERVE_SPACE) });

		customFormatTable.addSelectionListener(new SelectionAdapter() {

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
	protected String getDefaultPreviewText() {
		return DEFAULT_PREVIEW_TEXT;
	}

	@Override
	protected void initiatePageLayout(String categoryStr, String patternStr, String localeStr) {
		initiatePageLayout(DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM, categoryStr, patternStr, localeStr);
	}

	@Override
	protected void reLayoutSubPages() {
		reLayoutSubPages(DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM);
	}

	@Override
	protected void setControlsEnabled(boolean enabled) {
		typeChoicer.setEnabled(enabled);
		localeChoicer.setEnabled(enabled);
		customFormatCodeTextBox.setEnabled(enabled);
		customPreviewTextBox.setEnabled(enabled);
		customFormatTable.setEnabled(enabled);
	}

	@Override
	protected void setDefaultPreviewText(String text) {
		if (text == null || StringUtil.isBlank(text)) {
			previewText = null;
		} else {
			previewText = text;
		}
	}

	@Override
	protected void updatePreview() {
		markDirty(hasLoaded);

		ULocale locale = getLocaleByDisplayName(this.locale);
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		String gText;
		if (getPreviewText() == null) {
			gText = DEFAULT_PREVIEW_TEXT;
		} else {
			gText = getPreviewText();
		}

		String category = formatAdapter.getCategory4DisplayName(typeChoicer.getText());
		setCategory(category);

		if (DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED.equals(category)) {
			String pattern = null;
			String fmtStr = new StringFormatter(pattern, locale).format(gText);
			generalPreviewLabel.setText(validatedFmtStr(fmtStr));
			setPattern(null);
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE.equals(category)) {
			String pattern = FormatStringPattern.getPatternForCategory(category, locale);
			String fmtStr = new StringFormatter(pattern, locale).format(gText);
			generalPreviewLabel.setText(validatedFmtStr(fmtStr));
			setPattern(pattern);
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE.equals(category)) {
			String pattern = FormatStringPattern.getPatternForCategory(category, locale);
			String fmtStr = new StringFormatter(pattern, locale).format(gText);
			generalPreviewLabel.setText(validatedFmtStr(fmtStr));
			setPattern(pattern);
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE.equals(category)) {
			String pattern = FormatStringPattern.getPatternForCategory(category, locale);
			String fmtStr = new StringFormatter(pattern, locale).format(SAMPLE_TEXT_ZIP_CODE);
			generalPreviewLabel.setText(validatedFmtStr(fmtStr));
			setPattern(pattern);
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4.equals(category)) {
			String pattern = FormatStringPattern.getPatternForCategory(category, locale);
			String fmtStr = new StringFormatter(pattern, locale).format(SAMPLE_TEXT_ZIP_C0DE4);
			generalPreviewLabel.setText(validatedFmtStr(fmtStr));
			setPattern(pattern);
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER.equals(category)) {
			String pattern = FormatStringPattern.getPatternForCategory(category, locale);
			String fmtStr = new StringFormatter(pattern, locale).format(SAMPLE_TEXT_PHONE_NUMBER);
			generalPreviewLabel.setText(validatedFmtStr(fmtStr));
			setPattern(pattern);
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER.equals(category)) {
			String pattern = FormatStringPattern.getPatternForCategory(category, locale);
			String fmtStr = new StringFormatter(pattern, locale).format(SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER);
			generalPreviewLabel.setText(validatedFmtStr(fmtStr));
			setPattern(pattern);
		} else if (DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM.equals(category)) {
			String pattern = customFormatCodeTextBox.getText();
			String fmtStr;
			if (StringUtil.isBlank(customPreviewTextBox.getText())) {
				fmtStr = new StringFormatter(pattern, locale).format(gText);
			} else {
				fmtStr = new StringFormatter(pattern, locale).format(customPreviewTextBox.getText());
			}

			customPreviewLabel.setText(validatedFmtStr(fmtStr));
			setPattern(pattern);
		}
	}

	@Override
	protected void updateTextByLocale() {
		setLocale(localeChoicer.getText());

		ULocale locale = getLocaleByDisplayName(this.locale);
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		customFormatTable.getItem(0)
				.setText(new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE),
						new StringFormatter(FormatStringPattern.getPatternForCategory(
								DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE, locale), locale)
										.format(DEFAULT_PREVIEW_TEXT) });
		customFormatTable.getItem(1)
				.setText(new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE),
						new StringFormatter(FormatStringPattern.getPatternForCategory(
								DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE, locale), locale)
										.format(DEFAULT_PREVIEW_TEXT) });
		customFormatTable.getItem(2)
				.setText(new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4),
						new StringFormatter(FormatStringPattern.getPatternForCategory(
								DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4, locale), locale)
										.format(SAMPLE_TEXT_ZIP_C0DE4) });
		customFormatTable.getItem(3)
				.setText(new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER),
						new StringFormatter(FormatStringPattern.getPatternForCategory(
								DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER, locale), locale)
										.format(SAMPLE_TEXT_PHONE_NUMBER) });
		customFormatTable.getItem(4).setText(new String[] {
				formatAdapter.getDisplayName4Category(DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER),
				new StringFormatter(FormatStringPattern.getPatternForCategory(
						DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER, locale), locale)
								.format(SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER) });
		customFormatTable.getItem(5)
				.setText(new String[] {
						formatAdapter.getDisplayName4Category(FormatStringAdapter.STRING_FORMAT_TYPE_PRESERVE_SPACE),
						new StringFormatter(FormatStringPattern.getPatternForCategory(
								FormatStringAdapter.STRING_FORMAT_TYPE_PRESERVE_SPACE, locale), locale)
										.format(SAMPLE_TEXT_PRESERVE_SPACE) });
	}

	public String getFormatString() {
		if (category == null && pattern == null) {
			return DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED;
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

	public void setPreviewText(String text) {
		if (text == null) {
			customPreviewTextBox.setText(DEFAULT_PREVIEW_TEXT);
		} else {
			customPreviewTextBox.setText(text);
		}
	}

}
