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
 * Search dialog to search elements based on element properties
 *
 * @since 4.20
 *
 */
public class SearchInputDialog extends BaseDialog {

	private static final int SEARCH_ID = IDialogConstants.CLIENT_ID + 1;

	private static final int BACK_ID = IDialogConstants.CLIENT_ID + 2;

	private static final String ANY = "(any)";

	private static final String PROPERTY_NAME_ID = "id";

	private static final String VALUE_LABEL = Messages.getString("SearchInputDialog.ValueLabel"); //$NON-NLS-1$

	private static final String PROP_LABEL = Messages.getString("SearchInputDialog.PropLabel"); //$NON-NLS-1$

	private final String value;

	private final String helpContextID;

	private Text valueText;

	private Combo propCombo;

	private Button ignoreCaseButton;

	private Button wholeWordButton;

	private Button regexButton;

	private Button recursiveButton;

	private Table table;

	private String errorMessage;

	private Label errorMessageText;

	private final SearchActionServices services;

	/**
	 * Creates an instance.
	 *
	 * @param parentShell
	 * @param initialValue
	 * @param helpContextID
	 * @param services
	 */
	public SearchInputDialog(Shell parentShell, String initialValue, String helpContextID,
			SearchActionServices services) {
		super(Messages.getString("SearchInputDialog.DialogTitle")); //$NON-NLS-1$
		this.setShellStyle(this.getShellStyle() & ~SWT.APPLICATION_MODAL);
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
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) composite.getLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 0;

		Composite textContainer = new Composite(composite, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		textContainer.setLayoutData(gd);

		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = layout.marginHeight = 0;
		textContainer.setLayout(layout);

		Label label = new Label(textContainer, SWT.WRAP);
		label.setText(VALUE_LABEL);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		label.setLayoutData(gd);
		label.setFont(parent.getFont());

		valueText = new Text(textContainer, SWT.BORDER);
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
		label.setText(PROP_LABEL);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		label.setLayoutData(gd);
		label.setFont(parent.getFont());

		propCombo = new Combo(textContainer, SWT.BORDER/* | SWT.DROP_DOWN | SWT.READ_ONLY */);

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

		Composite optionContainer = new Composite(composite, SWT.NONE);
		gd = new GridData(SWT.NONE, SWT.NONE, false, false);
		optionContainer.setLayoutData(gd);

		ignoreCaseButton = new Button(optionContainer, SWT.CHECK);
		ignoreCaseButton.setText(Messages.getString("SearchInputDialog.Message.IgnoreCase")); //$NON-NLS-1$
		ignoreCaseButton.setToolTipText(Messages.getString("SearchInputDialog.Message.IgnoreCase.ToolTip")); //$NON-NLS-1$

		wholeWordButton = new Button(optionContainer, SWT.CHECK);
		wholeWordButton.setText(Messages.getString("SearchInputDialog.Message.WholeWord")); //$NON-NLS-1$
		wholeWordButton.setToolTipText(Messages.getString("SearchInputDialog.Message.WholeWord.ToolTip")); //$NON-NLS-1$

		regexButton = new Button(optionContainer, SWT.CHECK);
		regexButton.setText(Messages.getString("SearchInputDialog.Message.RegularExpression")); //$NON-NLS-1$
		regexButton.setToolTipText(Messages.getString("SearchInputDialog.Message.RegularExpression.ToolTip")); //$NON-NLS-1$

		recursiveButton = new Button(optionContainer, SWT.CHECK);
		recursiveButton.setText(Messages.getString("SearchInputDialog.Message.Recursive")); //$NON-NLS-1$
		recursiveButton.setToolTipText(Messages.getString("SearchInputDialog.Message.Recursive.ToolTip")); //$NON-NLS-1$
		recursiveButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateProperties();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				updateProperties();
			}
		});

		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = layout.marginHeight = 0;
		optionContainer.setLayout(layout);

		errorMessageText = new Label(composite, SWT.NONE);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.verticalIndent = 5;
		gd.horizontalSpan = 2;
		errorMessageText.setLayoutData(gd);

		Composite tableContainer = new Composite(composite, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 250;
		gd.horizontalSpan = 2;
		gd.verticalIndent = 5;
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
		pathColumn.setText(Messages.getString("SearchInputDialog.ResultTableColumn.Property")); //$NON-NLS-1$
		table.getColumn(0).pack();

		TableColumn propColumn = new TableColumn(table, SWT.NONE);
		propColumn.setText(Messages.getString("SearchInputDialog.ResultTableColumn.Element")); //$NON-NLS-1$
		table.getColumn(1).pack();

		TableColumn typeColumn = new TableColumn(table, SWT.NONE);
		typeColumn.setText(Messages.getString("SearchInputDialog.ResultTableColumn.ElementType")); //$NON-NLS-1$
		table.getColumn(2).pack();

		TableColumn idColumn = new TableColumn(table, SWT.NONE);
		idColumn.setText(Messages.getString("SearchInputDialog.ResultTableColumn.ElementId")); //$NON-NLS-1$
		table.getColumn(3).pack();

		table.addListener(SWT.Selection, event -> {
			this.itemSelected(event.item.getData());
		});
		this.table = table;

		setErrorMessage(errorMessage);

		applyDialogFont(composite);

		UIUtil.bindHelp(parent, helpContextID);
		updateProperties();

		return composite;
	}

	/**
	 * Updates the available properties as provided by the services.
	 */
	public void updateProperties() {
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

	private void setErrorMessage(String errorMessage) {
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
	 * Records details about the search dialog state used for matching.
	 */
	public static class Search {
		private final String searchProp;
		private final String searchValue;
		private final Pattern pattern;
		private final boolean ignoreCase;
		private final boolean wholeWord;
		private final boolean recursive;

		private Search(String searchValue, String searchProp, boolean ignoreCase, boolean wholeWord, boolean regex,
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
		 * Return the name of the property that the search matches in the given handle.
		 *
		 * @param designElementHandle the handle to match.
		 * @return the name of the property that the search matches in the given handle.
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
				// validation of the element id
				if (matches(element.getID())) {
					return PROPERTY_NAME_ID;
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
		 * Whether the search is recursive.
		 *
		 * @return whether the search is recursive.
		 */
		public boolean isRecursive() {
			return recursive;
		}
	}

	/**
	 * Abstracts the behavior the dialog.
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
			setErrorMessage(Messages.getFormattedString("SearchInputDialog.Message.InvalidRegularExpression",
					new Object[] { e.getLocalizedMessage() }));
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
				item.setText(0, searchResult.getPropertyName());
				item.setText(1, searchResult.getElementName());
				item.setText(2, searchResult.getElementType());
				item.setText(3, searchResult.getElementId().toString());
				item.setData(searchResult);
			}
			for (TableColumn tableColumn : table.getColumns()) {
				tableColumn.pack();
			}
			table.redraw();
		}
	}

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
