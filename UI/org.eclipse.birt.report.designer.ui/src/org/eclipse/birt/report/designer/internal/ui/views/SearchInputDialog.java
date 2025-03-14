/*******************************************************************************
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.birt.report.designer.internal.ui.views.actions.SearchAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @since 4.20
 *
 */
public class SearchInputDialog extends BaseDialog {
	private static final int SEARCH_ID = IDialogConstants.INTERNAL_ID - 1;
	private static final int BACK_ID = IDialogConstants.INTERNAL_ID - 2;
	private static final String ANY = "(any)";
	/**
	 * The message of the dialog.
	 */
	private String valueLabel;
	private String propLabel;

	/**
	 * The input value.
	 */
	private final String value;

	/**
	 * The help context ID.
	 */
	private String helpContextID;

	/**
	 * Input text widget.
	 */
	private Text valueText;
	private Combo propCombo;
	private Button ignoreCaseButton;
	private Button wholeWordButton;
	private Button regexButton;
	private Button recursiveButton;
	private Table table;

	/**
	 * Error message label widget.
	 */
	private String errorMessage;

	/**
	 * Error message label widget.
	 */
	private Text errorMessageText;
	private final SearchActionServices services;

	/**
	 * @param parentShell
	 * @param initialValue
	 * @param helpContextID
	 * @param services
	 */
	public SearchInputDialog(Shell parentShell, String initialValue, String helpContextID,
			SearchActionServices services) {
		super(Messages.getString("SearchInputDialog.DialogTitle")); //$NON-NLS-1$
		this.setShellStyle(this.getShellStyle() & ~SWT.APPLICATION_MODAL);
		this.valueLabel = Messages.getString("SearchInputDialog.ValueLabel"); //$NON-NLS-1$
		this.propLabel = Messages.getString("SearchInputDialog.PropLabel"); //$NON-NLS-1$
		this.value = initialValue == null ? "" : initialValue;
		this.helpContextID = helpContextID;
		this.services = services;
	}

	@Override
	public boolean close() {
		if (services != null) {
			services.close();
		}
		return super.close();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// create a composite with standard margins and spacing
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		GridData gd = new GridData(GridData.FILL_BOTH);
		Composite composite = WidgetFactory.composite(SWT.NONE).layout(layout).layoutData(gd).create(parent);
		applyDialogFont(composite);

		Composite textContainer = new Composite(composite, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		textContainer.setLayoutData(gd);

		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = layout.marginHeight = 0;
		textContainer.setLayout(layout);

		if (valueLabel == null) {
			valueLabel = "";
		}

		if (propLabel == null) {
			propLabel = "";
		}

		Label label = new Label(textContainer, SWT.WRAP);
		label.setText(valueLabel);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		label.setLayoutData(gd);
		label.setFont(parent.getFont());

		valueText = new Text(textContainer, SWT.BORDER | SWT.SINGLE);
		valueText.setText(value);
		valueText.selectAll();

		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.widthHint = 250;
		valueText.setLayoutData(gd);
		valueText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String text = valueText.getText().trim();
				if (text.length() == 0) {
					getButton(SEARCH_ID).setEnabled(false);
					setErrorMessage(Messages.getString("SearchInputDialog.Message.BlankName")); //$NON-NLS-1$
				} else {
					getButton(SEARCH_ID).setEnabled(true);
					setErrorMessage(null);
				}
				getButton(BACK_ID).setEnabled(false);
			}
		});

		label = new Label(textContainer, SWT.WRAP);
		label.setText(propLabel);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		label.setLayoutData(gd);
		label.setFont(parent.getFont());

		propCombo = new Combo(textContainer, SWT.BORDER | SWT.SINGLE /* | SWT.DROP_DOWN | SWT.READ_ONLY */);

		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.widthHint = 250;
		propCombo.setLayoutData(gd);
		propCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String text = propCombo.getText().trim();
				if (text.length() == 0 || ANY.equals(text)) {
					setErrorMessage(Messages.getString("SearchInputDialog.Message.AllProps")); //$NON-NLS-1$
				} else {
					setErrorMessage(null);
				}
			}
		});

		errorMessageText = new Text(textContainer, SWT.READ_ONLY | SWT.WRAP);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		errorMessageText.setLayoutData(gd);
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		Composite optionContainer = new Composite(composite, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.NONE, false, false);
		optionContainer.setLayoutData(gd);

		ignoreCaseButton = new Button(optionContainer, SWT.CHECK);
		Label ignoreCaseButtonLabel = new Label(optionContainer, SWT.SINGLE);
		ignoreCaseButtonLabel.setText(Messages.getString("SearchInputDialog.Message.IgnoreCase")); //$NON-NLS-1$

		wholeWordButton = new Button(optionContainer, SWT.CHECK);
		Label wholeWordButtonLabel = new Label(optionContainer, SWT.SINGLE);
		wholeWordButtonLabel.setText(Messages.getString("SearchInputDialog.Message.WholeWord")); //$NON-NLS-1$

		regexButton = new Button(optionContainer, SWT.CHECK);
		Label regexButtonLabel = new Label(optionContainer, SWT.SINGLE);
		regexButtonLabel.setText(Messages.getString("SearchInputDialog.Message.RegularExpression")); //$NON-NLS-1$

		recursiveButton = new Button(optionContainer, SWT.CHECK);
		Label recursiveButtonLabel = new Label(optionContainer, SWT.SINGLE);
		recursiveButtonLabel.setText(Messages.getString("SearchInputDialog.Message.Recursive")); //$NON-NLS-1$
		recursiveButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				loadPropCombo();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				loadPropCombo();
			}
		});

		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = layout.marginHeight = 0;
		optionContainer.setLayout(layout);

		Composite tableContainer = new Composite(composite, SWT.NONE);
		// gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 250;
		gd.horizontalSpan = 2;
		tableContainer.setLayoutData(gd);

		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = layout.marginHeight = 0;
		tableContainer.setLayout(layout);

		final Table table = new Table(tableContainer, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 100;
		table.setLayoutData(gd);

		TableColumn pathColumn = new TableColumn(table, SWT.NONE);
		pathColumn.setText("Property");
		table.getColumn(0).pack();

		TableColumn propColumn = new TableColumn(table, SWT.NONE);
		propColumn.setText("Element");
		table.getColumn(1).pack();

		table.addListener(SWT.Selection, event -> {
			this.itemSelected(event.item.getData());
		});
		this.table = table;

		setErrorMessage(errorMessage);

		applyDialogFont(composite);

		UIUtil.bindHelp(parent, helpContextID);
		loadPropCombo();

		return composite;
	}

	/**
	 *
	 */
	private void loadPropCombo() {
		String text = propCombo.getText();
		propCombo.removeAll();
		propCombo.add(ANY);
		boolean found = text.equals(ANY);
		if (services != null) {
			boolean recursive = this.recursiveButton.getSelection();
			List<String> list = services.getPropertyNames(recursive);
			for (String name : list) {
				propCombo.add(name);
				if (!found && text.trim().length() > 0 && name.equals(text)) {
					found = true;
				}
			}
		}
		if (!found) {
			text = ANY;
		}
		propCombo.setText(text);
	}

	/**
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		if (errorMessageText != null && !errorMessageText.isDisposed()) {
			errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$

			boolean hasError = errorMessage != null && (StringConverter.removeWhiteSpaces(errorMessage)).length() > 0;
			errorMessageText.setEnabled(hasError);
			errorMessageText.setVisible(hasError);
			errorMessageText.getParent().update();
		}
	}

	@Override
	protected Control createContents(Composite parent) {
		Control composite = super.createContents(parent);
		if (valueText.getText().trim().length() == 0) {
			getButton(SEARCH_ID).setEnabled(false);
		}
		getButton(BACK_ID).setEnabled(false);
		return composite;
	}

	/**
	 * @since 4.20
	 *
	 */
	public static class Search {
		private final String searchProp;
		private final String searchValue;
		private final Pattern pattern;
		private final boolean ignoreCase;
		private final boolean wholeWord;
		private final boolean recursive;

		/**
		 * @param searchValue
		 * @param searchProp
		 * @param ignoreCase
		 * @param wholeWord
		 * @param regex
		 * @param recursive
		 */
		public Search(String searchValue, String searchProp, boolean ignoreCase, boolean wholeWord, boolean regex,
				boolean recursive) {
			this.searchProp = searchProp;
			this.searchValue = searchValue;
			this.ignoreCase = ignoreCase;
			this.wholeWord = wholeWord;
			this.recursive = recursive;
			Pattern pattern = null;
			if (regex) {
				int flags = 0;
				if (ignoreCase) {
					flags |= Pattern.CASE_INSENSITIVE;
				}
				String patternString = searchValue;
				if (wholeWord) {
					if (!patternString.startsWith("^")) {
						patternString = "^" + patternString;
					}
					if (!patternString.endsWith("$")) {
						patternString = patternString + "$";
					}
				}
				pattern = Pattern.compile(patternString, flags);
			}
			this.pattern = pattern;
		}

		/**
		 * @param designElementHandle
		 * @return boolean
		 */
		public String matches(DesignElementHandle designElementHandle) {
			if (searchProp == null) {
				DesignElement element = designElementHandle.getElement();
				List<IElementPropertyDefn> defns = element.getPropertyDefns();
				for (IElementPropertyDefn defn : defns) {
					String name = defn.getName();
					Object value = element.getProperty(designElementHandle.getModule(), name);
					if (value != null) {
						if (matches(value)) {
							return name;
						}
					}
				}
			} else {
				Object value = designElementHandle.getProperty(searchProp);
				if (value != null) {
					if (matches(value)) {
						return searchProp;
					}
				}
			}
			return null;
		}

		private boolean matches(Object value) {
			String valueString = value.toString();
			if (pattern != null) {
				Matcher matcher = pattern.matcher(valueString);
				if (matcher.find()) {
					return true;
				}
			} else {
				if (ignoreCase) {
					if (wholeWord) {
						if (valueString.equalsIgnoreCase(searchValue)) {
							return true;
						}
					} else {
						if (valueString.toLowerCase().contains(searchValue.toLowerCase())) {
							return true;
						}
					}
				} else {
					if (wholeWord) {
						if (valueString.equals(searchValue)) {
							return true;
						}
					} else {
						if (valueString.contains(searchValue)) {
							return true;
						}
					}
				}
			}
			return false;
		}

		/**
		 * @return Returns the searchProp.
		 */
		public String getSearchProp() {
			return searchProp;
		}

		/**
		 * @return Returns the searchValue.
		 */
		public String getSearchValue() {
			return searchValue;
		}

		/**
		 * @return Returns the pattern.
		 */
		public Pattern getPattern() {
			return pattern;
		}

		/**
		 * @return Returns the ignoreCase.
		 */
		public boolean isIgnoreCase() {
			return ignoreCase;
		}

		/**
		 * @return Returns the wholeWord.
		 */
		public boolean isWholeWord() {
			return wholeWord;
		}

		/**
		 * @return Returns the recursive.
		 */
		public boolean isRecursive() {
			return recursive;
		}
	}

	/**
	 * @since 4.20
	 *
	 */
	public interface SearchActionServices {
		/**
		 * @param search
		 * @return List
		 */
		List<SearchAction.SearchResult> search(Search search);

		/**
		 *
		 */
		void close();

		/**
		 * @param recursive
		 * @return List<String>
		 */
		List<String> getPropertyNames(boolean recursive);

		/**
		 * @return boolean
		 *
		 */
		boolean back();

		/**
		 * @param data
		 */
		void select(Object data);
	}

	private void searchPressed() {
		String searchProp = propCombo.getText();
		if (searchProp != null) {
			searchProp = searchProp.trim();
			if (searchProp.length() == 0 || ANY.equals(searchProp)) {
				searchProp = null;
			}
		}
		Search search;
		try {
			search = new Search(valueText.getText().trim(), searchProp, ignoreCaseButton.getSelection(),
					wholeWordButton.getSelection(), regexButton.getSelection(), this.recursiveButton.getSelection());
		} catch (PatternSyntaxException e) {
			setErrorMessage(Messages.getString("SearchInputDialog.Message.InvalidRegularExpression"));
			search = null;
		}
		if (services != null && search != null) {
			List<SearchAction.SearchResult> searchResults = services.search(search);

			int count = searchResults.size();
			setErrorMessage(count + " " + Messages.getString("SearchInputDialog.Message.Found")); //$NON-NLS-1$
			getButton(BACK_ID).setEnabled(count > 0);

			table.setItemCount(0);
			for (SearchAction.SearchResult searchResult : searchResults) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(1, searchResult.getElementName());
				item.setText(0, searchResult.getPropertyName());
				item.setData(searchResult);
			}
			for (TableColumn tableColumn : table.getColumns()) {
				tableColumn.pack();
			}
		}
	}

	/**
	 * @param data
	 */
	protected void itemSelected(Object data) {
		if (services != null) {
			services.select(data);
			getButton(BACK_ID).setEnabled(true);
		}
	}

	private void backPressed() {
		if (services != null) {
			boolean isEmpty = services.back();
			getButton(BACK_ID).setEnabled(!isEmpty);
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, SEARCH_ID, Messages.getString("SearchInputDialog.Label.SearchButton"), true);
		createButton(parent, BACK_ID, Messages.getString("SearchInputDialog.Label.BackButton"), false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (SEARCH_ID == buttonId) {
			searchPressed();
		} else if (BACK_ID == buttonId) {
			backPressed();
		} else {
			super.buttonPressed(buttonId);
		}
	}
}
