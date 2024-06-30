/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.ui.impl.contols;

import java.io.File;

import org.eclipse.birt.data.oda.pojo.ui.impl.models.ClassPathElement;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.MenuButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

public class ClassSelectionButton {

	private MenuButton button;

	private IMenuButtonHelper helper;
	private IMenuButtonProvider provider;

	private Menu menu;
	private POJOClassTabFolderPage folderPage;

	private SelectionAdapter listener = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			Widget widget = e.widget;
			if (widget instanceof MenuItem) {
				String exprType = (String) widget.getData();
				provider.handleSelectionEvent(exprType);
				refresh();
			}
			if (widget instanceof MenuButton) {
				provider.handleSelectionEvent(((MenuButtonProvider) provider).getDefaultOptionType());
			}
		}

	};

	public ClassSelectionButton(Composite parent, int style, IMenuButtonProvider provider) {
		button = new MenuButton(parent, style);
		button.addSelectionListener(listener);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshMenuItems();
			}

		});

		menu = new Menu(parent.getShell(), SWT.POP_UP);
		button.setDropDownMenu(menu);

		setMenuButtonProvider(provider);
		refresh();
	}

	protected void refreshMenuItems() {
		((MenuButtonProvider) this.provider).resetProperties();
		populateMenuItems();
	}

	public void setContainer(POJOClassTabFolderPage folderPage) {
		this.folderPage = folderPage;
	}

	public void setEnabled(boolean enable) {
		button.setEnabled(enable);
	}

	public boolean isEnabled() {
		return button.isEnabled();
	}

	public MenuButton getControl() {
		return button;
	}

	public IMenuButtonProvider getProvider() {
		return this.provider;
	}

	public void notifyExpressionChangeEvent(String oldExpression, String newExpression) {
		if (helper != null) {
			helper.notifyExpressionChangeEvent(oldExpression, newExpression);
		}
	}

	public void setMenuButtonHelper(IMenuButtonHelper helper) {
		this.helper = helper;
	}

	public IMenuButtonHelper getMenuButtonHelper() {
		return helper;
	}

	public void refresh() {
	}

	public void setMenuButtonProvider(IMenuButtonProvider provider) {
		if (provider != null && provider != this.provider) {
			this.provider = provider;

			provider.setInput(this);

			populateMenuItems();
		}
	}

	private void populateMenuItems() {
		for (int i = 0; i < menu.getItemCount(); i++) {
			menu.getItem(i).dispose();
			i--;
		}

		String[] types = this.provider.getMenuItems();
		for (int i = 0; i < types.length; i++) {
			MenuItem item = new MenuItem(menu, SWT.PUSH);
			item.setText(provider.getMenuItemText(types[i]));
			item.setData(types[i]);
			item.setImage(this.provider.getMenuItemImage(types[i]));
			item.addSelectionListener(listener);
		}

		if (menu.getItemCount() <= 0) {
			button.setDropDownMenu(null);
		}

		button.setText(provider.getButtonText());
		refresh();
	}

	public void handleSelection(String[] paths, String rootPath, boolean isRelative) {
		ClassPathElement[] elements = createClassPathElements(paths, rootPath, isRelative);
		((MenuButtonHelper) helper).updateTableElementsList();
		helper.addClassPathElements(elements, true);

		if (folderPage != null) {
			folderPage.updateWizardPageStatus();
			synchronizeClassPath();
		}
	}

	private void synchronizeClassPath() {
		if (!folderPage.getTabFriendClassTabFolderPage().isPageEditable()) {
			folderPage.getTabFriendClassTabFolderPage().resetJarElements(folderPage.getJarElements());
			folderPage.getTabFriendClassTabFolderPage().updateWizardPageStatus();
		}
	}

	private ClassPathElement[] createClassPathElements(String[] paths, String rootPath, boolean isRelative) {
		ClassPathElement[] elements = new ClassPathElement[paths.length];

		for (int i = 0; i < paths.length; i++) {
			String fullPath = rootPath == null ? paths[i] : rootPath + File.separator + paths[i];
			elements[i] = new ClassPathElement(new File(paths[i]).getName(), fullPath, isRelative);
		}

		return elements;
	}

}
