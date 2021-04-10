/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.data.ui.property.PropertyNode;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;

/**
 * History toolBar to switch between dataset editor tree node pages. It will
 * save the history of jumped pages, and user could switch between tree viwer.
 * 
 */
public class HistoryToolBar extends Composite {
	// toolBar manager
	private ToolBarManager toolbarManager = null;
	// jump history
	private List history = new ArrayList();
	// start index
	private int historyIndex = -1;
	private TreeViewer viewer;

	/**
	 * 
	 * @param parent parent composite
	 * @param viewer the dataset editor's tree viewer
	 * @param style
	 */
	public HistoryToolBar(Composite parent, TreeViewer viewer, int style) {
		super(parent, SWT.NONE);

		GridLayout toolbarLayout = new GridLayout();
		toolbarLayout.marginHeight = 0;
		toolbarLayout.verticalSpacing = 0;
		setLayout(toolbarLayout);
		setLayoutData(new GridData(SWT.END, SWT.FILL, false, true));

		ToolBar toolBar = new ToolBar(this, style);
		toolBar.setLayoutData(new GridData(SWT.END, SWT.FILL, false, true));
		toolbarManager = new ToolBarManager(toolBar);

		this.viewer = viewer;
		createHistoryControls(toolBar);
		toolbarManager.update(false);

		initAccessible();
	}

	/**
	 * Toolbar with backward and forward button
	 * 
	 * @param historyBar
	 */
	private void createHistoryControls(ToolBar historyBar) {
		abstract class HistoryNavigationAction extends Action implements IMenuCreator {

			private Menu lastMenu;

			protected final static int MAX_ENTRIES = 5;

			HistoryNavigationAction() {
				super("", IAction.AS_DROP_DOWN_MENU); //$NON-NLS-1$
			}

			public IMenuCreator getMenuCreator() {
				return this;
			}

			public void dispose() {
				if (lastMenu != null) {
					lastMenu.dispose();
					lastMenu = null;
				}
			}

			public Menu getMenu(Control parent) {
				if (lastMenu != null) {
					lastMenu.dispose();
				}
				lastMenu = new Menu(parent);
				createEntries(lastMenu);
				return lastMenu;
			}

			public Menu getMenu(Menu parent) {
				return null;
			}

			protected void addActionToMenu(Menu parent, IAction action) {
				ActionContributionItem item = new ActionContributionItem(action);
				item.fill(parent, -1);
			}

			protected abstract void createEntries(Menu menu);
		}

		class HistoryItemAction extends Action {

			private final int index;

			HistoryItemAction(int index, String label) {
				super(label, IAction.AS_PUSH_BUTTON);
				this.index = index;
			}

			public void run() {
				jumpToHistory(index);
			}
		}

		HistoryNavigationAction backward = new HistoryNavigationAction() {

			public void run() {
				jumpToHistory(historyIndex - 1);
			}

			public boolean isEnabled() {
				boolean enabled = historyIndex > 0;
				if (enabled)
					setToolTipText(getHistoryToolTip(this.getText(), historyIndex - 1));
				else
					setToolTipText(this.getText());

				return enabled;
			}

			protected void createEntries(Menu menu) {
				int limit = Math.max(0, historyIndex - MAX_ENTRIES);
				for (int i = historyIndex - 1; i >= limit; i--) {
					IAction action = new HistoryItemAction(i, getHistoryPropertyNode(i).getNodeLabel());
					addActionToMenu(menu, action);
				}
			}
		};
		backward.setText(Messages.getString("dataset.editor.historybar.backward")); //$NON-NLS-1$
		if (isBidi()) {
			backward.setImageDescriptor(ReportPlatformUIImages.getImageDescriptor("ForwardEnabled")); //$NON-NLS-1$
			backward.setDisabledImageDescriptor(ReportPlatformUIImages.getImageDescriptor("ForwardDisabled")); //$NON-NLS-1$
		} else {
			backward.setImageDescriptor(ReportPlatformUIImages.getImageDescriptor("BackwardEnabled")); //$NON-NLS-1$
			backward.setDisabledImageDescriptor(ReportPlatformUIImages.getImageDescriptor("BackwardDisabled")); //$NON-NLS-1$
		}

		toolbarManager.add(backward);

		HistoryNavigationAction forward = new HistoryNavigationAction() {

			public void run() {
				jumpToHistory(historyIndex + 1);
			}

			public boolean isEnabled() {
				boolean enabled = historyIndex < history.size() - 1;
				if (enabled)
					setToolTipText(getHistoryToolTip(this.getText(), historyIndex + 1));
				else
					setToolTipText(this.getText());

				return enabled;
			}

			protected void createEntries(Menu menu) {
				int limit = Math.min(history.size(), historyIndex + MAX_ENTRIES + 1);
				for (int i = historyIndex + 1; i < limit; i++) {
					IAction action = new HistoryItemAction(i, getHistoryPropertyNode(i).getNodeLabel());
					addActionToMenu(menu, action);
				}
			}
		};
		forward.setText(Messages.getString("dataset.editor.historybar.forward")); //$NON-NLS-1$
		if (isBidi()) {
			forward.setImageDescriptor(ReportPlatformUIImages.getImageDescriptor("BackwardEnabled")); //$NON-NLS-1$
			forward.setDisabledImageDescriptor(ReportPlatformUIImages.getImageDescriptor("BackwardDisabled")); //$NON-NLS-1$
		} else {
			forward.setImageDescriptor(ReportPlatformUIImages.getImageDescriptor("ForwardEnabled")); //$NON-NLS-1$
			forward.setDisabledImageDescriptor(ReportPlatformUIImages.getImageDescriptor("ForwardDisabled")); //$NON-NLS-1$
		}

		toolbarManager.add(forward);
	}

	/**
	 * 
	 * @return
	 */
	private static boolean isBidi() {
		String lang = (String) System.getProperties().get("osgi.nl.user"); //$NON-NLS-1$
		if ("iw".equals(lang) || "ar".equals(lang) || "fa".equals(lang) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				|| "ur".equals(lang)) //$NON-NLS-1$
			return true;
		else
			return false;
	}

	/**
	 * jump to the indexed page node
	 * 
	 * @param index
	 */
	private void jumpToHistory(int index) {
		if (canLeave() && index >= 0 && index < history.size()) {
			historyIndex = index;
			viewer.setSelection(new StructuredSelection((PropertyNode) history.get(historyIndex)));
		}
		updateHistoryControls();
	}

	/**
	 * whether could leave the current page node
	 * 
	 * @return
	 */
	private boolean canLeave() {
		if (history.get(historyIndex) != null) {
			return ((PropertyNode) history.get(historyIndex)).getPage().canLeave();
		} else
			return false;
	}

	/**
	 * add history page node in history list
	 * 
	 * @param node
	 */
	public void addHistoryNode(PropertyNode node) {
		if (historyIndex == -1 || !history.get(historyIndex).equals(node)) {
			history.subList(historyIndex + 1, history.size()).clear();
			history.add(node);
			historyIndex++;
			updateHistoryControls();
		}
	}

	/**
	 * update history control
	 *
	 */
	private void updateHistoryControls() {
		toolbarManager.update(false);
		IContributionItem[] items = toolbarManager.getItems();
		for (int i = 0; i < items.length; i++) {
			items[i].update(IAction.ENABLED);
			items[i].update(IAction.TOOL_TIP_TEXT);
		}
	}

	/**
	 * get history toolbar's tooltip
	 * 
	 * @param toolTipPrefix
	 * @param index
	 * @return
	 */
	private String getHistoryToolTip(String toolTipPrefix, int index) {
		return toolTipPrefix + " " + Messages.getString("dataset.editor.historybar.to") //$NON-NLS-1$ //$NON-NLS-2$
				+ " " + getHistoryPropertyNode(index).getNodeLabel(); //$NON-NLS-1$
	}

	/**
	 * get indexed history page node
	 * 
	 * @param index
	 * @return
	 */
	private PropertyNode getHistoryPropertyNode(int index) {
		return (PropertyNode) history.get(index);
	}

	/**
	 * make custom control accessible
	 *
	 */
	void initAccessible() {
		getAccessible().addAccessibleListener(new AccessibleAdapter() {

			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(new Point(e.x, e.y));
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(new Point(location.x, location.y));
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
			}

			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}
		});
	}
}
