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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.FormatDateTimePattern;
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
 * FormatDateTimeLayoutPeer
 */
public class FormatDateTimeLayoutPeer extends FormatLayoutPeer {

	private static final String LABEL_GENERAL_PREVIEW_GROUP = Messages
			.getString("FormatDateTimePage.label.general.preview.group"); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_PREVIEW_DATETIME = Messages
			.getString("FormatDateTimePage.label.preview.dateTime"); //$NON-NLS-1$

	private static final String ENTER_DATE_TIME_GUIDE_FORMAT = Messages
			.getString("FormatDateTimePage.label.guide.format"); //$NON-NLS-1$
	private static final String ENTER_DATE_TIME_GUIDE_TEXT = Messages.getString("FormatDateTimePage.label.guide.text"); //$NON-NLS-1$

	private static final String PREVIEW_TEXT_INVALID_DATETIME_TO_PREVIEW = Messages
			.getString("FormatDateTimePage.preview.invalid.dateTime"); //$NON-NLS-1$

	private static final int FORMAT_CODE_INDEX = 2;

	private Label generalPreviewLabel;

	private Date defaultDate = new Date();

	private String defaultPreviewText = new DateFormatter(ENTER_DATE_TIME_GUIDE_FORMAT, ULocale.getDefault())
			.format(defaultDate);

	public FormatDateTimeLayoutPeer(int dateTimeType, int pageAlignment, boolean isFormStyle, boolean showLocale) {
		super(pageAlignment, isFormStyle, showLocale);

		this.formatAdapter = new FormatDateTimeAdapter(dateTimeType);
	}

	@Override
	protected void fireFormatChanged(String newCategory, String newPattern, String newLocale) {
		fireFormatChanged(StyleHandle.DATE_TIME_FORMAT_PROP, newCategory, newPattern, newLocale);
	}

	@Override
	protected void createCategoryPages(Composite parent) {
		categoryPageMaps = new HashMap<>();

		categoryPageMaps.put(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME, getGeneralPage(parent));

		categoryPageMaps.put(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM,
				getCustomPage(parent, true, LABEL_CUSTOM_PREVIEW_DATETIME));
	}

	@Override
	protected String getDefaultPreviewText() {
		return defaultPreviewText == null ? "" : defaultPreviewText; //$NON-NLS-1$
	}

	@Override
	protected String getCustomGuideText() {
		return ENTER_DATE_TIME_GUIDE_TEXT;
	}

	@Override
	protected void initiatePageLayout(String categoryStr, String patternStr, String localeStr) {
		initiatePageLayout(((FormatDateTimeAdapter) formatAdapter).getCustomCategoryName(), categoryStr, patternStr,
				localeStr);
	}

	@Override
	protected void reLayoutSubPages() {
		reLayoutSubPages(((FormatDateTimeAdapter) formatAdapter).getCustomCategoryName());
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

		Label previewLabel = FormWidgetFactory.getInstance().createLabel(group,
				SWT.CENTER | SWT.HORIZONTAL | SWT.VERTICAL, isFormStyle);
		previewLabel.setLayoutData(new GridData(GridData.FILL_BOTH));

		return previewLabel;
	}

	@Override
	protected void createTable(Composite parent) {
		super.createTable(parent);

		ULocale locale = getLocale();
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		TableColumn tableColumValue = new TableColumn(customFormatTable, SWT.NONE);
		tableColumValue.setText(LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_NAME);
		tableColumValue.setWidth(90);
		tableColumValue.setResizable(true);

		TableColumn tableColumnDisplay = new TableColumn(customFormatTable, SWT.NONE);
		tableColumnDisplay.setText(LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT);
		tableColumnDisplay.setWidth(120);
		tableColumnDisplay.setResizable(true);

		TableColumn tableColumnFormatCode = new TableColumn(customFormatTable, SWT.NONE);
		tableColumnFormatCode.setText(LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE);
		tableColumnFormatCode.setWidth(120);
		tableColumnFormatCode.setResizable(true);

		String[][] items = getTableItems(locale);
		for (int i = 0; i < items.length; i++) {
			new TableItem(customFormatTable, SWT.NONE).setText(items[i]);
		}

		customFormatTable.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				customFormatCodeTextBox.setText(((TableItem) e.item).getText(FORMAT_CODE_INDEX));
				updatePreview();
				notifyFormatChange();
			}
		});
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

		String category = formatAdapter.getCategory4DisplayName(typeChoicer.getText());
		setCategory(category);

		ULocale locale = FormatAdapter.getLocaleByDisplayName(this.locale);
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		boolean invalidPreviewText = false;
		Date sampleDateTime = defaultDate;
		if (getPreviewText() != null && !getPreviewText().equals(defaultPreviewText)) {
			try {
				sampleDateTime = new DateFormatter(ENTER_DATE_TIME_GUIDE_FORMAT, locale).parse(getPreviewText());
			} catch (Exception e) {
				invalidPreviewText = true;
				// do nothing, leave sampleDate to be defaultDate.
			}
		}

		if (((FormatDateTimeAdapter) formatAdapter).getCustomCategoryName().equals(category)) {
			String pattern = customFormatCodeTextBox.getText();
			String fmtStr;

			if (invalidPreviewText) {
				fmtStr = PREVIEW_TEXT_INVALID_DATETIME_TO_PREVIEW;
			} else {
				try {
					if (pattern == null || pattern.length() == 0) {
						fmtStr = defaultPreviewText;
					} else {
						fmtStr = new DateFormatter(pattern, locale).format(sampleDateTime);
					}
				} catch (Exception e) {
					fmtStr = PREVIEW_TEXT_INVALID_DATETIME_TO_PREVIEW;
				}
			}

			customPreviewLabel.setText(validatedFmtStr(fmtStr));
			setPattern(pattern);
		} else {
			String pattern = null;
			if (!((FormatDateTimeAdapter) formatAdapter).getUnformattedCategoryDisplayName().equals(category)) {
				pattern = FormatDateTimePattern.getPatternForCategory(category);
				setPattern(pattern);
			} else {
				pattern = ((FormatDateTimeAdapter) formatAdapter).getUnformattedCategoryName();
				setPattern(null);
			}
			String fmtStr = new DateFormatter(pattern, locale).format(sampleDateTime);
			generalPreviewLabel.setText(validatedFmtStr(fmtStr));
		}
	}

	@Override
	protected void updateTextByLocale() {
		ULocale oldLocale = FormatAdapter.getLocaleByDisplayName(this.locale);
		Date sampleDateTime = defaultDate;
		try {
			if (getPreviewText() != null) {
				sampleDateTime = new DateFormatter(ENTER_DATE_TIME_GUIDE_FORMAT, oldLocale).parse(getPreviewText());
			}
		} catch (ParseException e) {
			// do nothing.
		}

		setLocale(localeChoicer.getText());

		ULocale locale = FormatAdapter.getLocaleByDisplayName(this.locale);
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		int index = typeChoicer.getSelectionIndex();
		typeChoicer.setItems(formatAdapter.getFormatTypes(locale));
		if (index >= 0 && index < typeChoicer.getItemCount()) {
			typeChoicer.select(index);
		}

		String[][] items = getTableItems(locale);
		for (int i = 0; i < items.length; i++) {
			customFormatTable.getItem(i).setText(items[i]);
		}

		if (customFormatTable.getSelectionCount() == 1) {
			customFormatCodeTextBox.setText(customFormatTable.getSelection()[0].getText(FORMAT_CODE_INDEX));
		}

		String sampleDateTimeString = new DateFormatter(ENTER_DATE_TIME_GUIDE_FORMAT, locale).format(sampleDateTime);
		customPreviewTextBox.setText(sampleDateTimeString);

		setPreviewText(sampleDateTimeString);
	}

	private String[][] getTableItems(ULocale locale) {
		List<String[]> itemList = new ArrayList<>();
		String[][] items = {
				new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE), locale)
										.format(defaultDate),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE), locale)
										.getFormatCode() },
				new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE), locale)
										.format(defaultDate),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE), locale)
										.getFormatCode() },
				new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE), locale)
										.format(defaultDate),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE), locale)
										.getFormatCode() },
				new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE), locale)
										.format(defaultDate),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE), locale)
										.getFormatCode() },
				new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME), locale)
										.format(defaultDate),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME), locale)
										.getFormatCode() },
				new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME), locale)
										.format(defaultDate),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME), locale)
										.getFormatCode() },
				new String[] {
						formatAdapter.getDisplayName4Category(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME), locale)
										.format(defaultDate),
						new DateFormatter(FormatDateTimePattern
								.getPatternForCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME), locale)
										.getFormatCode() } };
		itemList.addAll(Arrays.asList(items));
		String[] customPatterns = FormatDateTimePattern.getCustormPatternCategorys();
		for (int i = 0; i < customPatterns.length; i++) {
			itemList.add(new String[] { FormatDateTimePattern.getDisplayName4CustomCategory(customPatterns[i]),
					new DateFormatter(FormatDateTimePattern.getCustormFormatPattern(customPatterns[i], locale), locale)
							.format(defaultDate),
					FormatDateTimePattern.getCustormFormatPattern(customPatterns[i], locale) });
		}
		return itemList.toArray(new String[0][3]);
	}

	@Override
	public String getFormatString() {
		if (category == null && pattern == null) {
			return ((FormatDateTimeAdapter) formatAdapter).getUnformattedCategoryDisplayName();
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
		setDefaultPreviewText(text);
		updatePreview();
	}

}
