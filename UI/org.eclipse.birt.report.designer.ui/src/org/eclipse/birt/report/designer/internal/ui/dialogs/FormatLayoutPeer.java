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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.ULocale;

/**
 * FormatLayoutPeers
 */
public abstract class FormatLayoutPeer implements IFormatPage {

	private static final String PREVIEW_TEXT_INVALID_FORMAT_CODE = Messages
			.getString("FormatNumberPage.preview.invalidFormatCode"); //$NON-NLS-1$
	private static final String LABEL_FORMAT_CATEGORY = Messages.getString("FormatNumberPage.label.format.number.page"); //$NON-NLS-1$
	private static final String LABEL_FORMAT_LOCALE = Messages.getString("FormatNumberPage.label.format.number.locale"); //$NON-NLS-1$
	private static final String LABEL_FORMAT_CODE = Messages.getString("FormatNumberPage.label.format.code"); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTINGS_GROUP = Messages
			.getString("FormatNumberPage.label.custom.settings"); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTINGS_LABEL = Messages
			.getString("FormatNumberPage.label.custom.settings.lable"); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_PREVIEW_GROUP = Messages
			.getString("FormatNumberPage.label.custom.preview.group"); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_PREVIEW_LABEL = Messages
			.getString("FormatNumberPage.label.custom.preview.label"); //$NON-NLS-1$

	protected static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_NAME = Messages
			.getString("FormatDateTimePage.label.table.column.format.name"); //$NON-NLS-1$
	protected static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT = Messages
			.getString("FormatDateTimePage.label.table.column.format.result"); //$NON-NLS-1$
	protected static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE = Messages
			.getString("FormatDateTimePage.label.table.column.format.code"); //$NON-NLS-1$

	protected static final int DEFAULT_CATEGORY_CONTAINER_WIDTH = 220;

	private List<IFormatChangeListener> listeners = new ArrayList<IFormatChangeListener>();

	protected int pageAlignment;

	protected String pattern = null;
	protected String category = null;
	protected String locale = null;
	protected String oldCategory = null;
	protected String oldPattern = null;
	protected String oldLocale = null;

	private Composite content;
	private Composite infoComp;
	private Composite formatCodeComp;

	private Composite generalFormatCodePage;
	private Composite customFormatCodePage;

	protected XCombo typeChoicer;
	protected XCombo localeChoicer;

	protected Composite generalPage;
	protected Composite customPage;

	protected Text customPreviewTextBox;
	protected Text customFormatCodeTextBox;
	protected Label customPreviewLabel;
	protected Table customFormatTable;
	protected Label customGuideLabel;

	protected HashMap<String, Control> categoryPageMaps;

	protected boolean hasLoaded = false;

	protected String previewText = null;

	protected FormatAdapter formatAdapter;

	private boolean isDirty = false;

	private boolean showLocale = true;

	protected boolean isFormStyle = false;

	protected FormatLayoutPeer(int pageAlignment, boolean isFormStyle, boolean showLocale) {
		this.isFormStyle = isFormStyle;
		this.showLocale = showLocale;
		this.pageAlignment = pageAlignment;
	}

	public Control createLayout(Composite parent) {
		content = new Composite(parent, SWT.NONE);

		formatAdapter.initChoiceArray();
		formatAdapter.getFormatTypes(null);

		if (pageAlignment == PAGE_ALIGN_HORIZONTAL) {
			createContentsHorizontally();
		} else {
			createContentsVirtically();
		}
		return content;
	}

	private void createContentsVirtically() {
		content.setLayout(UIUtil.createGridLayoutWithoutMargin());

		Composite topContainer = new Composite(content, SWT.NONE);
		topContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		topContainer.setLayout(new GridLayout(2, false));

		FormWidgetFactory.getInstance().createLabel(topContainer, isFormStyle).setText(LABEL_FORMAT_CATEGORY);
		typeChoicer = new XCombo(topContainer, true, isFormStyle);
		typeChoicer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		typeChoicer.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				reLayoutSubPages();
				updateTextByLocale();
				updatePreview();
				notifyFormatChange();
			}
		});
		typeChoicer.setItems(formatAdapter.getFormatTypes(null));

		Label localeLabel = FormWidgetFactory.getInstance().createLabel(topContainer, isFormStyle);
		localeLabel.setText(LABEL_FORMAT_LOCALE);
		localeChoicer = new XCombo(topContainer, true, isFormStyle);
		localeChoicer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		localeChoicer.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateDefaultsByLocale();
				updateTextByLocale();
				updatePreview();
				notifyFormatChange();
			}
		});
		localeChoicer.setItems(FormatAdapter.getLocaleDisplayNames());
		if (localeChoicer.getItemCount() > 0) {
			localeChoicer.select(0);
		}

		WidgetUtil.setExcludeGridData(localeLabel, !showLocale);
		WidgetUtil.setExcludeGridData(localeChoicer.getControl(), !showLocale);

		infoComp = new Composite(content, SWT.NONE);
		infoComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		infoComp.setLayout(new StackLayout());

		createCategoryPages(infoComp);

		createCategoryPatterns();

		setInput(null, null);
		setPreviewText(getDefaultPreviewText());
	}

	private void createContentsHorizontally() {
		content.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));

		// create format type choicer
		Composite container = new Composite(content, SWT.NONE);
		GridData data = new GridData();
		data.widthHint = DEFAULT_CATEGORY_CONTAINER_WIDTH;
		container.setLayoutData(data);
		container.setLayout(new GridLayout(1, false));

		FormWidgetFactory.getInstance().createLabel(container, isFormStyle).setText(LABEL_FORMAT_CATEGORY);
		typeChoicer = new XCombo(container, true, isFormStyle);
		typeChoicer.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				reLayoutSubPages();
				updateTextByLocale();
				updatePreview();
				notifyFormatChange();
			}
		});
		typeChoicer.setItems(formatAdapter.getFormatTypes(null));

		Label localeLabel = FormWidgetFactory.getInstance().createLabel(container, isFormStyle);
		localeLabel.setText(LABEL_FORMAT_LOCALE);
		localeChoicer = new XCombo(container, true, isFormStyle);
		localeChoicer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		localeChoicer.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateDefaultsByLocale();
				updateTextByLocale();
				updatePreview();
				notifyFormatChange();
			}
		});
		localeChoicer.setItems(FormatAdapter.getLocaleDisplayNames());
		if (localeChoicer.getItemCount() > 0) {
			localeChoicer.select(0);
		}

		WidgetUtil.setExcludeGridData(localeLabel, !showLocale);
		WidgetUtil.setExcludeGridData(localeChoicer.getControl(), !showLocale);

		// create the right part setting pane
		infoComp = new Composite(content, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH);
		data.verticalSpan = 2;
		infoComp.setLayoutData(data);
		infoComp.setLayout(new StackLayout());

		createCategoryPages(infoComp);

		// create left bottom part format code pane
		formatCodeComp = new Composite(content, SWT.NONE);
		data = new GridData(GridData.FILL_VERTICAL);
		data.widthHint = DEFAULT_CATEGORY_CONTAINER_WIDTH;
		formatCodeComp.setLayoutData(data);
		formatCodeComp.setLayout(new StackLayout());

		createFormatCodePages(formatCodeComp);

		createCategoryPatterns();

		setInput(null, null);
		setPreviewText(getDefaultPreviewText());
	}

	protected abstract void createCategoryPages(Composite parent);

	protected void createCategoryPatterns() {
	};

	private void createFormatCodePages(Composite parent) {
		createHorizontalGeneralFormatCodePage(parent);
		createHorizontalCustomFormatCodePage(parent);
	}

	private Composite createHorizontalGeneralFormatCodePage(Composite parent) {
		if (generalFormatCodePage == null) {
			generalFormatCodePage = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(1, false);
			layout.marginHeight = 1;
			generalFormatCodePage.setLayout(layout);

			Label l = FormWidgetFactory.getInstance().createLabel(generalFormatCodePage, SWT.SEPARATOR | SWT.HORIZONTAL,
					isFormStyle);
			l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		return generalFormatCodePage;
	}

	private Composite createHorizontalCustomFormatCodePage(Composite parent) {
		if (customFormatCodePage == null) {
			customFormatCodePage = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout(1, false);
			layout.marginHeight = 1;
			customFormatCodePage.setLayout(layout);

			Label l = FormWidgetFactory.getInstance().createLabel(customFormatCodePage, SWT.SEPARATOR | SWT.HORIZONTAL,
					isFormStyle);
			l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			Composite container = new Composite(customFormatCodePage, SWT.NONE);
			container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			container.setLayout(new GridLayout(2, false));

			FormWidgetFactory.getInstance().createLabel(container, isFormStyle).setText(LABEL_FORMAT_CODE);
			if (isFormStyle) {
				customFormatCodeTextBox = FormWidgetFactory.getInstance().createText(container, "", SWT.SINGLE); //$NON-NLS-1$
			} else {
				customFormatCodeTextBox = new Text(container, SWT.SINGLE | SWT.BORDER);
			}
			customFormatCodeTextBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			customFormatCodeTextBox.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					if (hasLoaded) {
						updatePreview();
					}
				}
			});
			customFormatCodeTextBox.addFocusListener(new FocusListener() {

				public void focusLost(FocusEvent e) {
					notifyFormatChange();
				}

				public void focusGained(FocusEvent e) {
				}
			});
		}
		return customFormatCodePage;
	}

	final protected void notifyFormatChange() {
		if (hasLoaded) {
			fireFormatChanged(getCategory(), getPattern(), this.locale);
		}
	}

	public void setInput(String formatString) {
		if (formatString == null) {
			setInput(null, null);
			return;
		}
		String fmtStr = formatString;
		int pos = fmtStr.indexOf(":"); //$NON-NLS-1$
		if (StringUtil.isBlank(fmtStr)) {
			setInput(null, null);
			return;
		} else if (pos == -1) {
			setInput(fmtStr, fmtStr);
			return;
		}
		String category = fmtStr.substring(0, pos);
		String patternStr = fmtStr.substring(pos + 1);

		setInput(category, patternStr, null);

	}

	public void setInput(String categoryStr, String patternStr) {
		setInput(categoryStr, patternStr, null);
	}

	public void setInput(String categoryStr, String patternStr, ULocale locale) {
		hasLoaded = false;

		String localeStr = FormatAdapter.getLocaleDisplayName(locale);

		initiatePageLayout(categoryStr, patternStr, localeStr);
		reLayoutSubPages();
		updateTextByLocale();
		updatePreview();

		// set initial.
		oldCategory = categoryStr;
		oldPattern = patternStr;
		oldLocale = localeStr;

		hasLoaded = true;
		return;
	}

	protected abstract void initiatePageLayout(String categoryStr, String patternStr, String localeStr);

	protected abstract void reLayoutSubPages();

	/**
	 * This updates the default settings on current page when locale is changed.
	 */
	protected void updateDefaultsByLocale() {
	};

	/**
	 * This updates the all the page based on current settings.
	 */
	protected abstract void updateTextByLocale();

	protected abstract void updatePreview();

	final protected void initiatePageLayout(String customCategoryName, String categoryStr, String patternStr,
			String localeStr) {
		if (localeStr != null) {
			localeChoicer.setText(localeStr);
		} else {
			localeChoicer.select(0);
		}

		if (categoryStr == null) {
			typeChoicer.select(0);
		} else {
			if (categoryStr.equals(customCategoryName)) {
				customFormatCodeTextBox.setText(patternStr == null ? "" : patternStr); //$NON-NLS-1$
			}
			typeChoicer.select(formatAdapter.getIndexOfCategory(categoryStr));
		}
	}

	final protected void reLayoutSubPages(String customCategoryName) {
		String category = formatAdapter.getCategory4DisplayName(typeChoicer.getText());

		Control control = categoryPageMaps.get(category);

		((StackLayout) infoComp.getLayout()).topControl = control;

		infoComp.layout();

		if (formatCodeComp != null) {
			if (category.equals(customCategoryName)) {
				((StackLayout) formatCodeComp.getLayout()).topControl = createHorizontalCustomFormatCodePage(
						formatCodeComp);
			} else {
				((StackLayout) formatCodeComp.getLayout()).topControl = createHorizontalGeneralFormatCodePage(
						formatCodeComp);
			}
			formatCodeComp.layout();
		}
	}

	final protected Composite getCustomPage(Composite parent, boolean needGuideLabel, String customPreviewLabelText) {
		if (customPage == null) {
			customPage = new Composite(parent, SWT.NULL);
			customPage.setLayout(createGridLayout4Page());

			createCustomSettingsPart(customPage);

			if (pageAlignment == PAGE_ALIGN_VIRTICAL) {
				Composite container = new Composite(customPage, SWT.NONE);
				container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				container.setLayout(new GridLayout(2, false));

				FormWidgetFactory.getInstance().createLabel(container, isFormStyle).setText(LABEL_FORMAT_CODE);
				if (isFormStyle) {
					customFormatCodeTextBox = FormWidgetFactory.getInstance().createText(container, "", SWT.SINGLE); //$NON-NLS-1$
				} else {
					customFormatCodeTextBox = new Text(container, SWT.SINGLE | SWT.BORDER);
				}
				customFormatCodeTextBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				customFormatCodeTextBox.addModifyListener(new ModifyListener() {

					public void modifyText(ModifyEvent e) {
						if (hasLoaded) {
							updatePreview();
						}
					}
				});
				customFormatCodeTextBox.addFocusListener(new FocusListener() {

					public void focusLost(FocusEvent e) {
						notifyFormatChange();
					}

					public void focusGained(FocusEvent e) {
					}
				});
			}

			createCustomPreviewPart(customPage, needGuideLabel, customPreviewLabelText);
		}

		return customPage;
	}

	private void createCustomSettingsPart(Composite parent) {
		Group group;
		if (isFormStyle) {
			group = FormWidgetFactory.getInstance().createGroup(parent, ""); //$NON-NLS-1$
		} else {
			group = new Group(parent, SWT.NONE);
		}
		group.setText(LABEL_CUSTOM_SETTINGS_GROUP);
		group.setLayoutData(createGridData4Part());
		group.setLayout(new GridLayout(1, false));

		Label label = FormWidgetFactory.getInstance().createLabel(group, isFormStyle);
		label.setText(LABEL_CUSTOM_SETTINGS_LABEL);
		label.setLayoutData(new GridData());

		createTable(group);
	}

	private void createCustomPreviewPart(Composite parent, boolean needGuideLabel, String customPreviewLabelText) {
		Group group;
		if (isFormStyle) {
			group = FormWidgetFactory.getInstance().createGroup(parent, ""); //$NON-NLS-1$
		} else {
			group = new Group(parent, SWT.NONE);
		}
		group.setText(LABEL_CUSTOM_PREVIEW_GROUP);
		if (pageAlignment == PAGE_ALIGN_HORIZONTAL) {
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			group.setLayout(new GridLayout(1, false));
		} else {
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(new GridLayout(2, false));
		}

		FormWidgetFactory.getInstance().createLabel(group, isFormStyle).setText(customPreviewLabelText);
		if (isFormStyle) {
			customPreviewTextBox = FormWidgetFactory.getInstance().createText(group, "", SWT.SINGLE); //$NON-NLS-1$
		} else {
			customPreviewTextBox = new Text(group, SWT.SINGLE | SWT.BORDER);
		}
		customPreviewTextBox.setText(getDefaultPreviewText());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		if (pageAlignment == PAGE_ALIGN_HORIZONTAL) {
			data.horizontalIndent = 10;
		}
		customPreviewTextBox.setLayoutData(data);
		customPreviewTextBox.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				setDefaultPreviewText(customPreviewTextBox.getText());
				if (hasLoaded) {
					updatePreview();
				}

				if (customGuideLabel != null) {
					if (StringUtil.isBlank(customPreviewTextBox.getText())) {
						customGuideLabel.setText(""); //$NON-NLS-1$
					} else {
						customGuideLabel.setText(getCustomGuideText());
					}
				}
			}
		});

		if (needGuideLabel) {
			if (pageAlignment == PAGE_ALIGN_VIRTICAL) {
				FormWidgetFactory.getInstance().createLabel(group, isFormStyle);
			}

			customGuideLabel = FormWidgetFactory.getInstance().createLabel(group, isFormStyle);
			customGuideLabel.setText(""); //$NON-NLS-1$
			Font font = JFaceResources.getDialogFont();
			FontData fData = font.getFontData()[0];
			fData.setHeight(fData.getHeight() - 1);
			customGuideLabel.setFont(FontManager.getFont(fData));

			data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalIndent = 10;
			customGuideLabel.setLayoutData(data);
		}

		Label label = FormWidgetFactory.getInstance().createLabel(group, isFormStyle);
		label.setText(LABEL_CUSTOM_PREVIEW_LABEL);
		label.setLayoutData(new GridData());

		customPreviewLabel = FormWidgetFactory.getInstance().createLabel(group,
				SWT.CENTER | SWT.HORIZONTAL | SWT.VIRTUAL, isFormStyle);
		customPreviewLabel.setText(""); //$NON-NLS-1$
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 1;
		customPreviewLabel.setLayoutData(data);
	}

	protected void createTable(Composite parent) {
		if (isFormStyle) {
			customFormatTable = FormWidgetFactory.getInstance().createTable(parent,
					SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		} else {
			customFormatTable = new Table(parent, SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.BORDER);
		}
		customFormatTable.setLayoutData(new GridData(GridData.FILL_BOTH));

		customFormatTable.setLinesVisible(true);
		customFormatTable.setHeaderVisible(true);

	}

	public Control getControl() {
		return content;
	}

	/**
	 * Returns the category resulted from the page.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the patternStr from the page.
	 */

	public String getPattern() {
		return pattern;
	}

	public ULocale getLocale() {
		return getLocaleByDisplayName(locale);
	}

	final protected ULocale getLocaleByDisplayName(String name) {
		return FormatAdapter.getLocaleByDisplayName(name);
	}

	/**
	 * Determines the format string is modified or not from the page.
	 * 
	 * @return Returns true if the format string is modified.
	 */
	public boolean isFormatModified() {
		String c = getCategory();
		String p = getPattern();
		String l = this.locale;
		if (oldCategory == null) {
			if (c != null) {
				return true;
			}
		} else if (!oldCategory.equals(c)) {
			return true;
		}
		if (oldPattern == null) {
			if (p != null) {
				return true;
			}
		} else if (!oldPattern.equals(p)) {
			return true;
		}
		if (oldLocale == null) {
			if (l != null) {
				return true;
			}
		} else if (!oldLocale.equals(l)) {
			return true;
		}
		return false;
	}

	final protected void markDirty(boolean dirty) {
		isDirty = dirty;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setEnabled(boolean enabled) {
		if (content.isEnabled() != enabled) {
			content.setEnabled(enabled);
		}
		setControlsEnabled(enabled);
	}

	protected abstract void setControlsEnabled(boolean enabled);

	protected abstract String getDefaultPreviewText();

	protected abstract void setDefaultPreviewText(String text);

	protected String getCustomGuideText() {
		return null;
	};

	final protected String getPreviewText() {
		return previewText;
	}

	final protected void setPattern(String pattern) {
		this.pattern = pattern;
	}

	final protected void setCategory(String category) {
		this.category = category;
	}

	final protected void setLocale(String locale) {
		this.locale = locale;
	}

	final protected String validatedFmtStr(String fmtStr) {
		String text = fmtStr;
		if (text == null) {
			text = PREVIEW_TEXT_INVALID_FORMAT_CODE;
		}
		return text;
	}

	final protected GridLayout createGridLayout4Page() {
		GridLayout layout;
		if (pageAlignment == PAGE_ALIGN_HORIZONTAL) {
			layout = new GridLayout(2, false);
			layout.marginHeight = 0;
		} else {
			layout = new GridLayout(1, false);
			layout.marginHeight = 0;
		}
		return layout;
	}

	final protected GridData createGridData4Part() {
		GridData data;
		if (pageAlignment == PAGE_ALIGN_HORIZONTAL) {
			data = new GridData(GridData.FILL_VERTICAL);
		} else {
			data = new GridData(GridData.FILL_HORIZONTAL);
		}
		return data;
	}

	public void addFormatChangeListener(IFormatChangeListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	protected abstract void fireFormatChanged(String newCategory, String newPattern, String newLocale);

	final protected void fireFormatChanged(String formatType, String newCategory, String newPattern, String newLocale) {
		if (listeners.isEmpty()) {
			return;
		}

		FormatChangeEvent event = new FormatChangeEvent(this, formatType, newCategory, newPattern, newLocale);

		for (Iterator<IFormatChangeListener> iter = listeners.iterator(); iter.hasNext();) {
			IFormatChangeListener listener = iter.next();
			if (listener != null) {
				listener.formatChange(event);
			}
		}
	}

	/**
	 * XCombo
	 */
	protected static class XCombo {

		private Combo cba;
		private CCombo cbb;

		protected XCombo(Composite parent, boolean isReadonly, boolean isFormStyle) {
			if (isFormStyle) {
				int style = SWT.FLAT;
				if (isReadonly) {
					style |= SWT.READ_ONLY;
				}
				cbb = FormWidgetFactory.getInstance().createCCombo(parent, style);
			} else {
				int style = SWT.BORDER;
				if (isReadonly) {
					style |= SWT.READ_ONLY;
				}
				cba = new Combo(parent, style);
				cba.setVisibleItemCount(30);
			}
		}

		public void addSelectionListener(SelectionListener listener) {
			if (cba != null) {
				cba.addSelectionListener(listener);
			} else {
				cbb.addSelectionListener(listener);
			}
		}

		public void addModifyListener(ModifyListener listener) {
			if (cba != null) {
				cba.addModifyListener(listener);
			} else {
				cbb.addModifyListener(listener);
			}
		}

		public void addFocusListener(FocusListener listener) {
			if (cba != null) {
				cba.addFocusListener(listener);
			} else {
				cbb.addFocusListener(listener);
			}
		}

		public void select(int index) {
			if (cba != null) {
				cba.select(index);
			} else {
				cbb.select(index);
			}
		}

		public void deselectAll() {
			if (cba != null) {
				cba.deselectAll();
			} else {
				cbb.deselectAll();
			}
		}

		public int getSelectionIndex() {
			return cba != null ? cba.getSelectionIndex() : cbb.getSelectionIndex();
		}

		public String getText() {
			return cba != null ? cba.getText() : cbb.getText();
		}

		public int getItemCount() {
			return cba != null ? cba.getItemCount() : cbb.getItemCount();
		}

		public void setText(String string) {
			if (cba != null) {
				cba.setText(string);
			} else {
				cbb.setText(string);
			}
		}

		public void setItems(String[] items) {
			if (cba != null) {
				cba.setItems(items);
			} else {
				cbb.setItems(items);
			}
		}

		public void setLayoutData(Object layoutData) {
			if (cba != null) {
				cba.setLayoutData(layoutData);
			} else {
				cbb.setLayoutData(layoutData);
			}
		}

		public boolean isEnabled() {
			return cba != null ? cba.isEnabled() : cbb.isEnabled();
		}

		public void setEnabled(boolean enabled) {
			if (cba != null) {
				if (cba.getEnabled() != enabled)
					cba.setEnabled(enabled);
			} else {
				if (cbb.getEnabled() != enabled)
					cbb.setEnabled(enabled);
			}
		}

		Control getControl() {
			return cba == null ? cbb : cba;
		}
	}
}
