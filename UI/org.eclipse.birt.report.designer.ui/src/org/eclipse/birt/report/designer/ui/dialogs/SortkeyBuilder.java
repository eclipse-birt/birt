/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatAdapter;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * SortkeyBuilder
 */
public class SortkeyBuilder extends BaseTitleAreaDialog {

	protected Logger logger = Logger.getLogger(SortkeyBuilder.class.getName());

	public static final String DLG_TITLE_NEW = Messages.getString("SortkeyBuilder.DialogTitle.New"); //$NON-NLS-1$
	public static final String DLG_MESSAGE_NEW = Messages.getString("SortkeyBuilder.DialogMessage.New"); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString("SortkeyBuilder.DialogTitle.Edit"); //$NON-NLS-1$
	public static final String DLG_MESSAGE_EDIT = Messages.getString("SortkeyBuilder.DialogMessage.Edit"); //$NON-NLS-1$
	protected SortKeyHandle sortKey;

	protected DesignElementHandle handle;

	protected IChoiceSet choiceSet;

	protected Combo comboDirection;
	private Combo comboKey;

	protected List<ComputedColumnHandle> columnList;

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = new String[0];

	public static final Map<String, Integer> STRENGTH_MAP;

	static {
		STRENGTH_MAP = new HashMap<String, Integer>();
		STRENGTH_MAP.put(Messages.getString("SortkeyBuilder.Strength.ASCII"), ISortDefinition.ASCII_SORT_STRENGTH);
		STRENGTH_MAP.put(Messages.getString("SortkeyBuilder.Strength.PRIMARY"), Collator.PRIMARY);
		STRENGTH_MAP.put(Messages.getString("SortkeyBuilder.Strength.SECONDARY"), Collator.SECONDARY);
		STRENGTH_MAP.put(Messages.getString("SortkeyBuilder.Strength.TERTIARY"), Collator.TERTIARY);
		STRENGTH_MAP.put(Messages.getString("SortkeyBuilder.Strength.QUATERNARY"), Collator.QUATERNARY);
		STRENGTH_MAP.put(Messages.getString("SortkeyBuilder.Strength.IDENTICAL"), Collator.IDENTICAL);
	}

	/**
	 * @param title
	 */
	public SortkeyBuilder(String title, String message) {
		this(UIUtil.getDefaultShell(), title, message);
	}

	/**
	 * @param parentShell
	 * @param title
	 */
	protected String title, message;

	private Combo comboLocale;

	private Combo comboStrength;

	public SortkeyBuilder(Shell parentShell, String title, String message) {
		super(parentShell);
		this.title = title;
		this.message = message;
		choiceSet = ChoiceSetFactory.getStructChoiceSet(SortKey.SORT_STRUCT, SortKey.DIRECTION_MEMBER);
	}

	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.INSERT_EDIT_SORTKEY_DIALOG_ID);

		Composite area = (Composite) super.createDialogArea(parent);
		Composite contents = new Composite(area, SWT.NONE);
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));
		contents.setLayout(new GridLayout());

		this.setTitle(title);
		this.setMessage(message);
		getShell().setText(title);

		applyDialogFont(contents);
		initializeDialogUnits(area);
		createInputContents(contents);

		Composite space = new Composite(contents, SWT.NONE);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.heightHint = 10;
		space.setLayoutData(gdata);

		Label lb = new Label(contents, SWT.SEPARATOR | SWT.HORIZONTAL);
		lb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return area;
	}

	protected Composite createInputContents(Composite parent) {
		Label lb = new Label(parent, SWT.NONE);
		lb.setText(Messages.getString("SortkeyBuilder.DialogTitle.Label.Prompt")); //$NON-NLS-1$

		Composite content = new Composite(parent, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout glayout = new GridLayout(3, false);
		content.setLayout(glayout);

		Label labelKey = new Label(content, SWT.NONE);
		labelKey.setText(Messages.getString("SortkeyBuilder.DialogTitle.Label.Key")); //$NON-NLS-1$
		comboKey = new Combo(content, SWT.BORDER);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.widthHint = 240;
		comboKey.setLayoutData(gdata);
		comboKey.setVisibleItemCount(30);
		comboKey.setItems(getDataSetColumns());
		if (comboKey.getItemCount() == 0) {
			comboKey.add(DEUtil.resolveNull(null));
		}
		comboKey.addListener(SWT.Selection, comboKeyModify);
		comboKey.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				updateButtons();
			}

		};
		ExpressionButtonUtil.createExpressionButton(content, comboKey, getExpressionProvider(), handle, listener);

		Label labelDirection = new Label(content, SWT.NONE);
		labelDirection.setText(Messages.getString("SortkeyBuilder.DialogTitle.Label.Direction")); //$NON-NLS-1$

		comboDirection = new Combo(content, SWT.READ_ONLY | SWT.BORDER);
		String[] displayNames = ChoiceSetFactory.getDisplayNamefromChoiceSet(choiceSet);
		comboDirection.setVisibleItemCount(30);
		comboDirection.setItems(displayNames);
		gdata = new GridData(GridData.FILL_HORIZONTAL);
		comboDirection.setLayoutData(gdata);

		new Label(content, SWT.NONE);
		Label labelLocale = new Label(content, SWT.NONE);
		labelLocale.setText(Messages.getString("SortkeyBuilder.Label.Locale"));
		comboLocale = new Combo(content, SWT.READ_ONLY | SWT.BORDER);
		gdata = new GridData(GridData.FILL_HORIZONTAL);
		comboLocale.setLayoutData(gdata);
		comboLocale.setVisibleItemCount(30);
		List<String> localeNames = new ArrayList<String>();
		localeNames.add(Messages.getString("SortkeyBuilder.Locale.Auto"));
		localeNames.addAll(FormatAdapter.LOCALE_TABLE.keySet());
		comboLocale.setItems(localeNames.toArray(new String[] {}));
		comboLocale.select(0);

		new Label(content, SWT.NONE);
		Label labelStrength = new Label(content, SWT.NONE);
		labelStrength.setText(Messages.getString("SortkeyBuilder.Label.Strength"));
		comboStrength = new Combo(content, SWT.READ_ONLY | SWT.BORDER);
		gdata = new GridData(GridData.FILL_HORIZONTAL);
		comboStrength.setLayoutData(gdata);
		comboStrength.setVisibleItemCount(30);
		List<String> strengthNames = new ArrayList<String>(STRENGTH_MAP.keySet());
		Collections.sort(strengthNames, new Comparator<String>() {

			public int compare(String o1, String o2) {
				return STRENGTH_MAP.get(o1) - STRENGTH_MAP.get(o2);
			}
		});
		comboStrength.setItems(strengthNames.toArray(new String[] {}));
		comboStrength.select(0);

		return content;
	}

	protected ExpressionProvider getExpressionProvider() {
		return new ExpressionProvider(handle);
	}

	protected Listener comboKeyModify = new Listener() {

		public void handleEvent(Event e) {
			assert e.widget instanceof Combo;
			Combo combo = (Combo) e.widget;
			String newValue = combo.getText();

			IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter(combo);
			if (converter != null) {
				String value = ExpressionUtility.getExpression(getResultSetColumn(newValue), converter);
				if (value != null)
					newValue = value;
			}

			combo.setText(newValue);
			updateButtons();
		}
	};

	private Object getResultSetColumn(String name) {
		if (columnList == null || columnList.isEmpty()) {
			return null;
		}
		for (int i = 0; i < columnList.size(); i++) {
			ComputedColumnHandle column = columnList.get(i);
			if (column.getName().equals(name)) {
				return column;
			}
		}
		return null;
	}

	public int open() {
		if (getShell() == null) {
			// create the window
			create();
		}
		if (initDialog()) {
			if (Policy.TRACING_DIALOGS) {
				String[] result = this.getClass().getName().split("\\."); //$NON-NLS-1$
				System.out.println("Dialog >> Open " //$NON-NLS-1$
						+ result[result.length - 1]);
			}
			return super.open();
		}

		return Dialog.CANCEL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog#initDialog ()
	 */
	protected boolean initDialog() {
		if (sortKey == null) {
			comboKey.setText(""); //$NON-NLS-1$
			comboDirection.select(0);
			return true;
		}

		ExpressionButtonUtil.initExpressionButtonControl(comboKey, sortKey, SortKey.KEY_MEMBER);

		if (sortKey.getDirection() != null && sortKey.getDirection().trim().length() != 0) {
			String value = sortKey.getDirection().trim();
			IChoice choice = choiceSet.findChoice(value);
			if (choice != null)
				value = choice.getDisplayName();
			int index;
			index = comboDirection.indexOf(value);
			index = index < 0 ? 0 : index;
			comboDirection.select(index);
		}

		if (sortKey.getLocale() != null) {
			String locale = null;
			for (Map.Entry<String, ULocale> entry : FormatAdapter.LOCALE_TABLE.entrySet()) {
				if (sortKey.getLocale().equals(entry.getValue())) {
					locale = entry.getKey();
				}
			}
			if (locale != null) {
				int index = comboLocale.indexOf(locale);
				comboLocale.select(index < 0 ? 0 : index);
			}
		}

		String strength = null;
		for (Map.Entry<String, Integer> entry : STRENGTH_MAP.entrySet()) {
			if (sortKey.getStrength() == entry.getValue()) {
				strength = entry.getKey();
			}
		}
		if (strength != null) {
			int index = comboStrength.indexOf(strength);
			comboStrength.select(index < 0 ? 0 : index);
		}
		updateButtons();
		return true;
	}

	protected String[] getDataSetColumns() {
		if (columnList.isEmpty()) {
			return EMPTY;
		}
		List<String> valueList = new ArrayList<String>();
		for (int i = 0; i < columnList.size(); i++) {
			ComputedColumnHandle columnHandle = columnList.get(i);
			if (columnHandle.getAggregateFunction() == null)
				valueList.add(columnHandle.getName());
		}
		return valueList.toArray(new String[valueList.size()]);
	}

	public void setHandle(DesignElementHandle handle) {
		this.handle = handle;
		inilializeColumnList(handle);
	}

	protected void inilializeColumnList(DesignElementHandle handle) {
		columnList = DEUtil.getVisiableColumnBindingsList(handle);
	}

	/**
	 * Notifies that the ok button of this dialog has been pressed.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method sets this
	 * dialog's return code to <code>Window.OK</code> and closes the dialog.
	 * Subclasses may override.
	 * </p>
	 */
	protected void okPressed() {
		String direction = comboDirection.getText();
		IChoice choice = choiceSet.findChoiceByDisplayName(direction);
		if (choice != null)
			direction = choice.getDisplayName();
		int index;
		index = comboDirection.indexOf(direction);
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(title);
		try {
			if (sortKey == null) {
				SortKey sortKey = StructureFactory.createSortKey();
				ExpressionButtonUtil.saveExpressionButtonControl(comboKey, sortKey, SortKey.KEY_MEMBER);
				if (index >= 0) {
					sortKey.setDirection(choice.getName());
				}

				String locale = comboLocale.getText();
				if (FormatAdapter.LOCALE_TABLE.containsKey(locale)) {
					sortKey.setLocale(FormatAdapter.LOCALE_TABLE.get(locale));
				} else {
					sortKey.setLocale(null);
				}

				String strength = comboStrength.getText();
				if (STRENGTH_MAP.containsKey(strength)) {
					sortKey.setStrength(STRENGTH_MAP.get(strength));
				} else {
					sortKey.setStrength(ISortDefinition.ASCII_SORT_STRENGTH);
				}

				PropertyHandle propertyHandle = handle.getPropertyHandle(ListingHandle.SORT_PROP);
				propertyHandle.addItem(sortKey);
			} else {
				// edit
				ExpressionButtonUtil.saveExpressionButtonControl(comboKey, sortKey, SortKey.KEY_MEMBER);

				if (index >= 0) {
					sortKey.setDirection(choice.getName());
				}

				String locale = comboLocale.getText();
				if (FormatAdapter.LOCALE_TABLE.containsKey(locale)) {
					sortKey.setLocale(FormatAdapter.LOCALE_TABLE.get(locale));
				} else {
					sortKey.setLocale(null);
				}

				String strength = comboStrength.getText();
				if (STRENGTH_MAP.containsKey(strength)) {
					sortKey.setStrength(STRENGTH_MAP.get(strength));
				} else {
					sortKey.setStrength(ISortDefinition.ASCII_SORT_STRENGTH);
				}
			}

			stack.commit();
		} catch (SemanticException e) {
			ExceptionHandler.handle(e, Messages.getString("SortkeyBuilder.DialogTitle.Error.SetSortKey.Title"), //$NON-NLS-1$
					e.getLocalizedMessage());
			stack.rollback();
		}
		super.okPressed();
	}

	public boolean performCancel() {
		return true;
	}

	public boolean performOk() {
		return true;
	}

	/**
	 * Sets the model input.
	 * 
	 * @param input
	 */
	public void setInput(Object input) {
		if (input instanceof SortKeyHandle) {
			this.sortKey = (SortKeyHandle) input;
		} else {
			this.sortKey = null;
		}

	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	protected void updateButtons() {
		getButton(IDialogConstants.OK_ID).setEnabled(isConditionOK());
	}

	protected boolean isConditionOK() {
		if (comboKey.getText().trim().length() == 0) {
			return false;
		}
		return true;
	}

}
