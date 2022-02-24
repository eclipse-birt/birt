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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please document
 *
 * @version $Revision$ $Date$
 */
public final class PropertyHandleTableViewer {
	private TableViewer viewer;
	private Composite mainControl;
	private Button btnRemove;
	private Button btnUp;
	private Button btnDown;
	private MenuItem itmRemove;
	private MenuItem itmRemoveAll;
	private Menu menu;
	protected Logger logger = Logger.getLogger(PropertyHandleTableViewer.class.getName());

	/**
	 *
	 */
	public PropertyHandleTableViewer(Composite parent, boolean showMenus, boolean showButtons,
			boolean enableKeyStrokes) {
		mainControl = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		mainControl.setLayout(layout);

		GridData data;
		viewer = new TableViewer(mainControl, SWT.FULL_SELECTION | SWT.BORDER);
		data = new GridData(GridData.FILL_BOTH);
		viewer.getControl().setLayoutData(data);

		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		if (showButtons) {
			Composite btnComposite = new Composite(mainControl, SWT.NONE);
			data = new GridData();
			data.verticalAlignment = SWT.CENTER;
			btnComposite.setLayoutData(data);
			GridLayout btnLayout = new GridLayout();
			layout.verticalSpacing = 20;
			btnComposite.setLayout(btnLayout);

			GridData btnData = new GridData(GridData.CENTER);
			btnData.widthHint = 20;
			btnData.heightHint = 20;
			btnUp = new Button(btnComposite, SWT.ARROW | SWT.UP);
			btnUp.setLayoutData(btnData);
			btnUp.setToolTipText(Messages.getString("PropertyHandleTableViewer.Menu.Up")); //$NON-NLS-1$
			btnUp.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					// Get the current selection and delete that row
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
						viewer.getTable().select(index - 1);
					}

				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

			});

			btnData = new GridData(GridData.CENTER);
			btnData.widthHint = 20;
			btnData.heightHint = 20;
			btnRemove = new Button(btnComposite, SWT.PUSH);
			btnRemove.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));
			btnRemove.setLayoutData(btnData);
			btnRemove.setToolTipText(Messages.getString("PropertyHandleTableViewer.Menu.Remove")); //$NON-NLS-1$
			btnRemove.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					removeSelectedItem();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

			});

			btnData = new GridData(GridData.CENTER);
			btnData.widthHint = 20;
			btnData.heightHint = 20;
			btnDown = new Button(btnComposite, SWT.ARROW | SWT.DOWN);
			btnDown.setLayoutData(btnData);
			btnDown.setToolTipText(Messages.getString("PropertyHandleTableViewer.Menu.Down")); //$NON-NLS-1$
			btnDown.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					// Get the current selection and delete that row
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
						viewer.getTable().select(index + 1);
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

			});
		}

		if (showMenus) {
			menu = new Menu(viewer.getTable());
			menu.addMenuListener(new MenuAdapter() {
				@Override
				public void menuShown(MenuEvent e) {
					viewer.cancelEditing();
				}
			});
			itmRemove = new MenuItem(menu, SWT.NONE);
			itmRemove.setText(Messages.getString("PropertyHandleTableViewer.Menu.Remove")); //$NON-NLS-1$
			itmRemove.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					removeSelectedItem();
				}

			});
			itmRemoveAll = new MenuItem(menu, SWT.NONE);
			itmRemoveAll.setText(Messages.getString("PropertyHandleTableViewer.Menu.RemoveAll")); //$NON-NLS-1$
			itmRemoveAll.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						PropertyHandle handle = (PropertyHandle) viewer.getInput();
						handle.clearValue();
						viewer.refresh();
					} catch (SemanticException e1) {
						// TODO Auto-generated catch block
						logger.log(Level.SEVERE, e1.getMessage(), e1);
					}
				}
			});

			viewer.getTable().setMenu(menu);
		}

		if (enableKeyStrokes) {
			viewer.getTable().addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (e.keyCode == SWT.DEL) {
						removeSelectedItem();
					}
				}

			});
		}
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public Composite getControl() {
		return mainControl;
	}

	public Button getUpButton() {
		return btnUp;
	}

	public Menu getMenu() {
		return menu;
	}

	public Button getDownButton() {
		return btnDown;
	}

	public Button getRemoveButton() {
		return btnRemove;
	}

	public MenuItem getRemoveMenuItem() {
		return itmRemove;
	}

	public MenuItem getRemoveAllMenuItem() {
		return itmRemoveAll;
	}

	private void removeSelectedItem() {
		int index = viewer.getTable().getSelectionIndex();
		PropertyHandle handle = (PropertyHandle) viewer.getInput();
		int count = (handle.getListValue() == null) ? 0 : handle.getListValue().size();
		// Do not allow deletion of the last item.
		if (index > -1 && index < count) {
			try {
				handle.removeItem(index);
			} catch (PropertyValueException e1) {
				ExceptionHandler.handle(e1);
			}
			viewer.refresh();
			viewer.getTable().select(index);
		}
	}
}
