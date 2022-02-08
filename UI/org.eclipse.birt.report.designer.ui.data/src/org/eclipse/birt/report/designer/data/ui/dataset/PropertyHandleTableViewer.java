/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * TODO: Please document
 * 
 * @version $Revision$ $Date$
 */
public class PropertyHandleTableViewer {

	protected TableViewer viewer;
	private Composite mainControl;
	private MenuItem itmRemove;
	private MenuItem itmRemoveAll;
	private Menu menu;
	private Button[] buttonArray;

	private static final int IDX_NEW = 0;
	private static final int IDX_EDIT = 1;
	private static final int IDX_REMOVE = 2;
	private static final int IDX_UP = 4;
	private static final int IDX_DOWN = 5;

	private static final String TEXT_NEW = Messages.getString("PropertyHandleTableViewer.Button.New");//$NON-NLS-1$
	private static final String TEXT_EDIT = Messages.getString("PropertyHandleTableViewer.Button.Edit");//$NON-NLS-1$
	private static final String TEXT_REMOVE = Messages.getString("PropertyHandleTableViewer.Button.Remove");//$NON-NLS-1$
	private static final String TEXT_UP = Messages.getString("PropertyHandleTableViewer.Button.Up");//$NON-NLS-1$
	private static final String TEXT_DOWN = Messages.getString("PropertyHandleTableViewer.Button.Down");//$NON-NLS-1$

	private String[] buttonLabels = { TEXT_NEW, TEXT_EDIT, TEXT_REMOVE, null, TEXT_UP, TEXT_DOWN };
	private int defaultButtonWidth = 70;

	public PropertyHandleTableViewer(Composite parent, boolean showMenus, boolean showButtons,
			boolean enableKeyStrokes) {
		this(parent, showMenus, showButtons, enableKeyStrokes, true);
	}

	/**
	 * 
	 */
	public PropertyHandleTableViewer(Composite parent, boolean showMenus, boolean showButtons, boolean enableKeyStrokes,
			boolean editable) {
		mainControl = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		mainControl.setLayout(layout);

		viewer = new TableViewer(mainControl, SWT.FULL_SELECTION | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		viewer.getControl().setLayoutData(data);

		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		if (showButtons) {
			localizeButtonWidth();
			createButtonComposite(editable);
		}

		if (showMenus)
			createMenuComposite();

		if (enableKeyStrokes)
			enableKeyStrokes();
	}

	private void createButtonComposite(boolean editable) {
		Composite composite = new Composite(mainControl, SWT.NONE);
		GridData gd = new GridData();
		gd.verticalIndent = -5;
		gd.verticalAlignment = SWT.BEGINNING;
		composite.setLayoutData(gd);
		composite.setLayout(new GridLayout());

		SelectionAdapter listener = null;
		if (editable)
			listener = new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					doButtonSelected(e);
				}
			};

		if (buttonLabels != null) {
			buttonArray = new Button[buttonLabels.length];
			for (int i = 0; i < buttonLabels.length; i++) {
				String currLabel = buttonLabels[i];
				if (currLabel != null) {
					buttonArray[i] = createButton(composite, currLabel, listener);
					if (!editable)
						buttonArray[i].setEnabled(editable);
				} else {
					buttonArray[i] = null;
					createSeparator(composite);
				}
			}
		}
	}

	private Button createButton(Composite parent, String label, SelectionListener listener) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);

		if (listener != null)
			button.addSelectionListener(listener);

		GridData gd = new GridData();
		gd.widthHint = defaultButtonWidth;
		button.setLayoutData(gd);

		return button;
	}

	private void localizeButtonWidth() {
		defaultButtonWidth = Math.max(getMaxStringWidth(buttonLabels), defaultButtonWidth);
	}

	private int getMaxStringWidth(String[] strArray) {
		int maxWidth = -1;
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i] != null)
				maxWidth = Math.max(maxWidth, strArray[i].length());
		}

		return maxWidth * getStandardCharWidth();
	}

	private int getStandardCharWidth() {
		GC gc = new GC(Display.getCurrent());
		int width = gc.textExtent("X").x; //$NON-NLS-1$
		gc.dispose();

		return width;
	}

	private void createSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setVisible(false);
		GridData gd = new GridData();
		gd.verticalIndent = 20;
		separator.setLayoutData(gd);
	}

	protected void doButtonSelected(SelectionEvent e) {
		if (buttonArray != null) {
			for (int i = 0; i < buttonArray.length; i++) {
				if (e.widget == buttonArray[i]) {
					doButtonPressed(i);
				}
			}
		}
	}

	protected void doButtonPressed(int index) {
		switch (index) {
		case IDX_REMOVE:
			doRemove();
			break;

		case IDX_UP:
			doUp();
			break;

		case IDX_DOWN:
			doDown();
			break;
		}
	}

	protected void doRemove() {
		int index = viewer.getTable().getSelectionIndex();
		PropertyHandle handle = (PropertyHandle) viewer.getInput();
		int count = (handle.getListValue() == null) ? 0 : handle.getListValue().size();

		if (index > -1 && index < count) {
			try {
				if (count == 1) {
					try {
						handle.setValue(new ArrayList());
					} catch (SemanticException e) {
						ExceptionHandler.handle(e);
					}
				} else {
					handle.removeItem(index);
				}
			} catch (PropertyValueException e1) {
				ExceptionHandler.handle(e1);
			}

			viewer.refresh();
			viewer.getTable().setFocus();
			viewer.getTable().select(index);
			updateButtons();
		}
	}

	private void doUp() {
		int index = viewer.getTable().getSelectionIndex();
		PropertyHandle handle = (PropertyHandle) viewer.getInput();
		if (index > 0 && handle.getListValue() != null && index < handle.getListValue().size()) {
			viewer.cancelEditing();
			try {
				handle.moveItem(index, index - 1);
			} catch (PropertyValueException e1) {
				ExceptionHandler.handle(e1);
			}
			viewer.refresh();
			viewer.getTable().setFocus();
			viewer.getTable().select(index - 1);
			updateButtons();
		}
	}

	private void doDown() {
		int index = viewer.getTable().getSelectionIndex();
		PropertyHandle handle = (PropertyHandle) viewer.getInput();
		if (index > -1 && handle.getListValue() != null && index < handle.getListValue().size() - 1) {
			viewer.cancelEditing();
			try {
				handle.moveItem(index, index + 1);
			} catch (PropertyValueException e1) {
				ExceptionHandler.handle(e1);
			}
			viewer.refresh();
			viewer.getTable().setFocus();
			viewer.getTable().select(index + 1);
			updateButtons();
		}
	}

	private void createMenuComposite() {
		menu = new Menu(viewer.getTable());
		menu.addMenuListener(new MenuAdapter() {

			public void menuShown(MenuEvent e) {
				viewer.cancelEditing();
			}
		});
		itmRemove = new MenuItem(menu, SWT.NONE);
		itmRemove.setText(Messages.getString("PropertyHandleTableViewer.Menu.Remove")); //$NON-NLS-1$
		itmRemove.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doRemove();
			}

		});
		itmRemoveAll = new MenuItem(menu, SWT.NONE);
		itmRemoveAll.setText(Messages.getString("PropertyHandleTableViewer.Menu.RemoveAll")); //$NON-NLS-1$
		itmRemoveAll.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				try {
					if (viewer.getInput() instanceof PropertyHandle) {
						PropertyHandle handle = (PropertyHandle) viewer.getInput();
						handle.clearValue();
						viewer.refresh();
						updateButtons();
					}
				} catch (SemanticException e1) {
					ExceptionHandler.handle(e1);
				}
			}
		});

		viewer.getTable().setMenu(menu);
	}

	private void enableKeyStrokes() {
		viewer.getTable().addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					doRemove();
				}
			}

		});
	}

	protected void updateButtons() {
		int[] indices = getViewer().getTable().getSelectionIndices();
		getUpButton().setEnabled(getUpButton().isVisible() && viewer.getTable().getItemCount() > 1
				&& indices.length == 1 && indices[0] != 0);

		getDownButton().setEnabled(getDownButton().isVisible() && viewer.getTable().getItemCount() > 1
				&& indices.length == 1 && indices[0] != viewer.getTable().getItemCount() - 1);

		getEditButton().setEnabled(getEditButton().isVisible() && indices.length == 1);
		getRemoveButton().setEnabled(getRemoveButton().isVisible() && indices.length > 0);
		if (getRemoveMenuItem() != null) {
			getRemoveMenuItem().setEnabled(indices.length > 0);
			getRemoveAllMenuItem().setEnabled(viewer.getTable().getItemCount() > 0);
		}
	}

	/**
	 * 
	 * @return
	 */
	public TableViewer getViewer() {
		return viewer;
	}

	/**
	 * 
	 * @return
	 */
	public Composite getControl() {
		return mainControl;
	}

	/**
	 * 
	 * @return
	 */
	public Button getNewButton() {
		return buttonArray[IDX_NEW];
	}

	/**
	 * 
	 * @return
	 */
	public Button getEditButton() {
		return buttonArray[IDX_EDIT];
	}

	/**
	 * 
	 * @return
	 */
	public Button getRemoveButton() {
		return buttonArray[IDX_REMOVE];
	}

	/**
	 * 
	 * @return
	 */
	public Button getUpButton() {
		return buttonArray[IDX_UP];
	}

	/**
	 * 
	 * @return
	 */
	public Button getDownButton() {
		return buttonArray[IDX_DOWN];
	}

	/**
	 * 
	 * @return
	 */
	public Menu getMenu() {
		return menu;
	}

	/**
	 * 
	 * @return
	 */
	public MenuItem getRemoveMenuItem() {
		return itmRemove;
	}

	/**
	 * 
	 * @return
	 */
	public MenuItem getRemoveAllMenuItem() {
		return itmRemoveAll;
	}

}
