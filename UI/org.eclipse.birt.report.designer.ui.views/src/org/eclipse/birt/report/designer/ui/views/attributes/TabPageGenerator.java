/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

/**
 * The default PageGenerator implementation, only creates an empty
 * <code>TabFolder</code>.
 */
public class TabPageGenerator implements IPageGenerator {

	public final static String ACTIVE_PAGE = "ActivePage"; //$NON-NLS-1$

	protected int tabIndex = 0;

	protected String selectedTabText;

	protected List input;

	protected CTabFolder tabFolder;

	protected FolderSelectionAdapter listener;

	/**
	 * Creates the tab items for the page
	 *
	 * @param tabFolder The attribute tabFolder.
	 * @param input     The current selection.
	 */

	public void createTabItems(final List input) {
		ISafeRunnable runnable = new ISafeRunnable() {

			@Override
			public void run() throws Exception {
				CTabItem[] oldPages = tabFolder.getItems();
				int index = tabFolder.getSelectionIndex();
				for (int i = 0; i < oldPages.length; i++) {
					if (oldPages[i].isDisposed() || (index == i)) {
						continue;
					}
					if (oldPages[i].getControl() != null) {
						oldPages[i].getControl().dispose();
					}
					oldPages[i].dispose();
				}
				if (index > -1 && !oldPages[index].isDisposed()) {
					oldPages[index].getControl().dispose();
					oldPages[index].dispose();
				}
			}

			@Override
			public void handleException(Throwable exception) {
				/* not used */
			}
		};
		SafeRunner.run(runnable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.views.IPageGenerator#createControl
	 * (org.eclipse.swt.widgets.Composite, java.lang.Object)
	 */
	@Override
	public void createControl(Composite parent, Object input) {
		this.input = (List) input;
		if (tabFolder == null || tabFolder.isDisposed()) {
			tabFolder = new CTabFolder(parent, SWT.TOP);
			tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
			createTabItems(this.input);
		}
		showPropertiesPage();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.views.IPageGenerator#getControl()
	 */
	@Override
	public Control getControl() {
		return tabFolder;
	}

	protected void showPropertiesPage() {
		if (SessionHandleAdapter.getInstance().getReportDesignHandle() != null) {
			if (tabFolder == null || tabFolder.isDisposed()) {
				return;
			}
			selectStickyTab();
			tabFolder.getParent().layout(true);
		}
	}

	/**
	 * Sticky tab behaviour. We try to set the default selection on the previous
	 * chosen tab by the user or the nearest one.
	 */
	private void selectStickyTab() {
		CTabItem[] items = tabFolder.getItems();
		boolean tabFound = false;
		for (int i = 0; i < items.length; i++) {
			if (items[i].getText().equals(selectedTabText)) {
				tabFolder.setSelection(i);
				tabFound = true;
				break;
			}
		}
		// we didn't find the tab, select the one with the closest tabIndex
		// instead
		if (!tabFound) {
			if (tabIndex > tabFolder.getItemCount() - 1) {
				tabFolder.setSelection(tabFolder.getItemCount() - 1);
			} else {
				tabFolder.setSelection(tabIndex);
			}
		}
	}

	protected void addSelectionListener(TabPageGenerator generator) {
		if (listener == null) {
			listener = new FolderSelectionAdapter(generator);
			tabFolder.addSelectionListener(listener);
		} else {
			tabFolder.removeSelectionListener(listener);
			tabFolder.addSelectionListener(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.views.IPageGenerator#getInput()
	 */
	@Override
	public Object getInput() {
		return input;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.views.IPageGenerator#refresh()
	 */
	@Override
	public void refresh() {
		// doing nothing
	}

	/**
	 * Returns the text of current selected tab.
	 *
	 * @return
	 */
	public String getSelectedTabText() {
		return selectedTabText;
	}

	/**
	 * Sets the text of current selected tab.
	 *
	 * @param selectedTabText
	 */
	public void setSelectedTabText(String selectedTabText) {
		this.selectedTabText = selectedTabText;
	}

	/**
	 * FolderSelectionAdapter
	 */
	class FolderSelectionAdapter extends SelectionAdapter {

		TabPageGenerator generator;

		public FolderSelectionAdapter(TabPageGenerator generator) {
			this.generator = generator;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (tabFolder != null) {
				tabIndex = tabFolder.getSelectionIndex();
				if (tabFolder.getSelection() != null) {
					selectedTabText = tabFolder.getSelection().getText();
					generator.createTabItems(input);
				}
			}
		}
	}

	public void selectTabItem(String tabKey) {
		selectedTabText = tabKey;
		showPropertiesPage();
		tabFolder.notifyListeners(SWT.Selection, new Event());
	}

	public boolean isChange(Object element) {
		return true;
	}
}
