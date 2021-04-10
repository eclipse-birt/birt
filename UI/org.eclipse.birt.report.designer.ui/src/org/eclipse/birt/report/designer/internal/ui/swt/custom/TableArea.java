/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * 
 */

public class TableArea extends Composite {

	private static final String BUTTON_NEW = Messages.getString("TableArea.Button.New"); //$NON-NLS-1$
	private static final String BUTTON_EDIT = Messages.getString("TableArea.Button.Edit"); //$NON-NLS-1$
	private static final String BUTTON_REMOVE = Messages.getString("TableArea.Button.Remove"); //$NON-NLS-1$
	private static final String BUTTON_REMOVE_ALL = Messages.getString("TableArea.Button.RemoveAll"); //$NON-NLS-1$
	private static final String BUTTON_UP = Messages.getString("TableArea.Button.Up"); //$NON-NLS-1$
	private static final String BUTTON_DOWN = Messages.getString("TableArea.Button.Down"); //$NON-NLS-1$

	private TableViewer tableViewer;
	private IBaseTableAreaModifier modifier;

	private Button newButton, editButton, removeButton, upButton, downButton, removeAllButton;
	private Table table;
	private Composite buttonBar;

	public TableArea(Composite parent, int tableStyle, IBaseTableAreaModifier modifier) {
		super(parent, SWT.NONE);
		Assert.isNotNull(modifier);
		setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
		this.modifier = modifier;
		createTableViewer(tableStyle);
		createButtonBar();
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point tableSize = table.computeSize(wHint, hHint, changed);
		Point buttonsSize = buttonBar.computeSize(wHint, hHint, changed);

		int x = tableSize.x + buttonsSize.x + 5;
		int y = Math.max(tableSize.y, buttonsSize.y);

		return new Point(x, y);
	}

	protected void createTableViewer(int tableStyle) {
		table = new Table(this, tableStyle | SWT.FULL_SELECTION | SWT.BORDER);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		if ((tableStyle & SWT.CHECK) != 0) {
			tableViewer = new CheckboxTableViewer(table);
		} else {
			tableViewer = new TableViewer(table);
		}
		if (modifier instanceof ITableAreaModifier) {
			table.addKeyListener(new KeyAdapter() {

				/**
				 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
				 */
				public void keyReleased(KeyEvent e) {
					if (e.keyCode == SWT.DEL && e.stateMask == 0 && !getSelection().isEmpty()) {
						doRemove();
					}
				}
			});
		}
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				if (getSelection().size() == 1) {
					doEdit();
				}
			}
		});
	}

	protected Composite createButtonBar() {
		buttonBar = new Composite(this, SWT.NONE);
		buttonBar.setLayout(UIUtil.createGridLayoutWithoutMargin());

		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		buttonBar.setLayoutData(gd);

		if (modifier instanceof ITableAreaModifier) {
			newButton = new Button(buttonBar, SWT.PUSH);
			newButton.setText(BUTTON_NEW);
			setButtonLayout(newButton);
			newButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					if (((ITableAreaModifier) modifier).newItem()) {
						tableViewer.refresh();
						updateButtons();
					}
				}
			});
		}

		editButton = new Button(buttonBar, SWT.PUSH);
		editButton.setText(BUTTON_EDIT);
		setButtonLayout(editButton);
		editButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doEdit();
			}
		});
		editButton.setEnabled(false);

		if (modifier instanceof ITableAreaModifier) {
			removeButton = new Button(buttonBar, SWT.PUSH);
			removeButton.setText(BUTTON_REMOVE);
			setButtonLayout(removeButton);
			removeButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					doRemove();
				}
			});
			removeButton.setEnabled(false);

			removeAllButton = new Button(buttonBar, SWT.PUSH);
			removeAllButton.setText(BUTTON_REMOVE_ALL);
			setButtonLayout(removeAllButton);
			removeAllButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					doRemoveAll();
				}
			});
			removeButton.setEnabled(false);
		}

		if (modifier instanceof ISortedTableAreaModifier) {
			upButton = new Button(buttonBar, SWT.PUSH);
			upButton.setText(BUTTON_UP);
			setButtonLayout(upButton);
			upButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
					if (((ISortedTableAreaModifier) modifier).moveUp(selection.getFirstElement())) {
						tableViewer.refresh();
						updateButtons();
					}
				}
			});
			upButton.setEnabled(false);

			downButton = new Button(buttonBar, SWT.PUSH);
			downButton.setText(BUTTON_DOWN);
			setButtonLayout(downButton);
			downButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
					if (((ISortedTableAreaModifier) modifier).moveDown(selection.getFirstElement())) {
						tableViewer.refresh();
						updateButtons();
					}
				}
			});
			downButton.setEnabled(false);

		}

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				updateButtons();
			}

		});
		return buttonBar;

	}

	protected void setButtonLayout(Button button) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		button.setLayoutData(gd);
	}

	private IStructuredSelection getSelection() {
		return (IStructuredSelection) tableViewer.getSelection();
	}

	public void updateButtons() {
		boolean enable = (getSelection().size() == 1);
		editButton.setEnabled(enable);
		if (modifier instanceof ISortedTableAreaModifier) {
			int index = tableViewer.getTable().getSelectionIndex();
			upButton.setEnabled(enable && index != 0);
			downButton.setEnabled(enable && index != tableViewer.getTable().getItemCount() - 1);
		}
		if (modifier instanceof ITableAreaModifier) {
			removeButton.setEnabled(!getSelection().isEmpty());
			removeAllButton.setEnabled(tableViewer.getTable().getItemCount() > 0);
		}
	}

	public Table getTable() {
		if (tableViewer != null) {
			return tableViewer.getTable();
		}
		return null;
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public void setInput(Object input) {
		tableViewer.setInput(input);
		updateButtons();
	}

	private void doEdit() {
		if (modifier.editItem(getSelection().getFirstElement())) {
			tableViewer.refresh();
			updateButtons();
		}
	}

	private void doRemove() {
		int selectIndex = tableViewer.getTable().getSelectionIndex();
		if (((ITableAreaModifier) modifier).removeItem(getSelection().toArray())) {
			tableViewer.refresh();
			int count = tableViewer.getTable().getItemCount();
			if (count > 0) {
				if (selectIndex >= count) {
					selectIndex = count - 1;
				}

				tableViewer.getTable().select(selectIndex);

			}

			updateButtons();
		}
	}

	private void doRemoveAll() {
		if (((ITableAreaModifier) modifier).removeItemAll()) {
			tableViewer.refresh();
			updateButtons();
		}

	}

}
