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

import java.util.LinkedHashMap;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class ExtendedPropertyEditorComposite extends Composite implements SelectionListener {

	private LinkedHashMap<String, ExtendedProperty> propMap = null;
	private LinkedHashMap<String, Boolean> propDisabledMap = null;

	private Table table = null;

	private TableEditor editorValue = null;

	private Text txtNewKey = null;

	private Button btnAdd = null;

	private Button btnRemove = null;

	private EList<ExtendedProperty> extendedProperties;
	private ChartWizardContext context;

	public ExtendedPropertyEditorComposite(Composite parent, int style, EList<ExtendedProperty> extendedProperties,
			ChartWizardContext context) {
		super(parent, style);
		this.extendedProperties = extendedProperties;
		this.context = context;
		init();
		placeComponents();
	}

	private void init() {
		propMap = new LinkedHashMap<String, ExtendedProperty>(6);
		for (ExtendedProperty property : extendedProperties) {
			propMap.put(property.getName(), property);
		}
		propDisabledMap = new LinkedHashMap<String, Boolean>(2);
		for (String disabledName : ChartModelHelper.instance().getBuiltInExtendedProperties()) {
			propDisabledMap.put(disabledName, Boolean.TRUE);
		}
	}

	private void placeComponents() {
		GridLayout glContent = new GridLayout();
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		this.setLayout(glContent);

		table = new Table(this, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gdTable = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gdTable);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tcKey = new TableColumn(table, SWT.CENTER);
		tcKey.setWidth(186);
		tcKey.setText(Messages.getString("PropertyEditorDialog.Lbl.Key")); //$NON-NLS-1$

		TableColumn tcValue = new TableColumn(table, SWT.LEFT);
		tcValue.setWidth(186);
		tcValue.setText(Messages.getString("PropertyEditorDialog.Lbl.Value")); //$NON-NLS-1$

		editorValue = new TableEditor(table);
		editorValue.setColumn(1);
		editorValue.grabHorizontal = true;
		editorValue.minimumWidth = 30;

		table.addSelectionListener(this);

		// Layout for buttons panel
		GridLayout glButtons = new GridLayout();
		glButtons.numColumns = 3;
		glButtons.horizontalSpacing = 5;
		glButtons.verticalSpacing = 5;
		glButtons.marginWidth = 0;
		glButtons.marginHeight = 0;

		Composite cmpButtons = new Composite(this, SWT.NONE);
		GridData gdCMPButtons = new GridData(GridData.FILL_HORIZONTAL);
		cmpButtons.setLayoutData(gdCMPButtons);
		cmpButtons.setLayout(glButtons);

		txtNewKey = new Text(cmpButtons, SWT.SINGLE | SWT.BORDER);
		GridData gdTXTNewKey = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTNewKey.grabExcessHorizontalSpace = true;
		txtNewKey.setLayoutData(gdTXTNewKey);

		btnAdd = new Button(cmpButtons, SWT.PUSH);
		GridData gdBTNAdd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gdBTNAdd.grabExcessHorizontalSpace = false;
		btnAdd.setLayoutData(gdBTNAdd);
		btnAdd.setText(Messages.getString("PropertyEditorDialog.Lbl.Add")); //$NON-NLS-1$
		btnAdd.addSelectionListener(this);

		btnRemove = new Button(cmpButtons, SWT.PUSH);
		GridData gdBTNRemove = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gdBTNRemove.grabExcessHorizontalSpace = false;
		btnRemove.setLayoutData(gdBTNRemove);
		btnRemove.setText(Messages.getString("PropertyEditorDialog.Lbl.Remove")); //$NON-NLS-1$
		btnRemove.addSelectionListener(this);

		populateTable();
	}

	private void populateTable() {
		for (String propName : propMap.keySet()) {
			ExtendedProperty property = propMap.get(propName);
			String[] sProperty = new String[2];
			sProperty[0] = property.getName();
			sProperty[1] = property.getValue();

			TableItem tiProp = new TableItem(table, SWT.CHECK);
			tiProp.setText(sProperty);
		}
		if (table.getItemCount() > 0) {
			table.select(0);
			btnRemove.setEnabled(!propDisabledMap.containsKey(table.getItem(0).getText()));
		} else {
			txtNewKey.forceFocus();
			btnRemove.setEnabled(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(btnAdd)) {
			String sKey = txtNewKey.getText();
			if (sKey.length() > 0 && !propMap.containsKey(sKey)) {
				String[] sProperty = new String[2];
				sProperty[0] = sKey;
				sProperty[1] = ""; //$NON-NLS-1$

				TableItem tiProp = new TableItem(table, SWT.NONE);
				tiProp.setText(sProperty);
				table.select(table.getItemCount() - 1);

				updateModel(sProperty[0], sProperty[1]);
				txtNewKey.setText(""); //$NON-NLS-1$
			}
		} else if (e.getSource().equals(btnRemove)) {
			if (table.getSelection().length != 0) {
				int index = table.getSelectionIndex();
				String key = table.getSelection()[0].getText(0);
				ExtendedProperty property = propMap.get(key);
				if (property != null) {
					extendedProperties.remove(property);
					propMap.remove(key);
					table.remove(table.getSelectionIndex());
					table.select(index < table.getItemCount() ? index : table.getItemCount() - 1);
				}
				Control editor = editorValue.getEditor();
				if (editor != null) {
					editor.dispose();
				}
			}
		} else if (e.getSource().equals(table)) {
			Control oldEditor = editorValue.getEditor();
			if (oldEditor != null)
				oldEditor.dispose();

			// Identify the selected row
			final TableItem item = (TableItem) e.item;
			if (item == null) {
				return;
			}

			// The control that will be the editor must be a child of the Table
			Text newEditor = new Text(table, SWT.NONE);
			newEditor.setText(item.getText(1));
			newEditor.addListener(SWT.FocusOut, new Listener() {

				public void handleEvent(Event event) {
					Text text = (Text) event.widget;
					editorValue.getItem().setText(1, text.getText());
					updateModel(item.getText(0), text.getText());
				}
			});
			newEditor.selectAll();
			newEditor.setFocus();
			editorValue.setEditor(newEditor, item, 1);
		}
		btnRemove.setEnabled(!propDisabledMap.containsKey(table.getSelection()[0].getText(0)));
	}

	private void updateModel(String key, String value) {
		ExtendedProperty property = propMap.get(key);
		if (property == null) {
			property = AttributeFactory.eINSTANCE.createExtendedProperty();
			property.setName(key);
			property.setValue(value);
			property.eAdapters().addAll(context.getModel().eAdapters());
			extendedProperties.add(property);
			propMap.put(key, property);
		} else {
			property.setValue(value);
		}
	}
}
