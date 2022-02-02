/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.core.ui.frameworks.taskwizard.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.ui.frameworks.taskwizard.TreeCompoundTask;
import org.eclipse.birt.core.ui.i18n.Messages;
import org.eclipse.birt.core.ui.utils.UIHelper;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.TreeItem;

/**
 * History for navigating subtasks.
 * 
 * @since 2.2
 */

public class SubtaskHistory {

	/**
	 * The history toolbar.
	 */
	private ToolBarManager historyToolbar;

	/**
	 * A list of history domain elements that stores the history of the visited
	 * preference pages.
	 */
	private List historyList = new ArrayList();

	/**
	 * Stores the current entry into <code>history</code> and
	 * <code>historyLabels</code>.
	 */
	private int historyIndex = -1;

	/**
	 * The compound task we implement the history for.
	 */
	private final TreeCompoundTask parentTask;

	/**
	 * Creates a new history for the given dialog.
	 * 
	 * @param parentTask the parent task to create a history for
	 */
	public SubtaskHistory(TreeCompoundTask parentTask) {
		this.parentTask = parentTask;
	}

	/**
	 * Returns the subtask path (for now: its id) for the history at
	 * <code>index</code>.
	 * 
	 * @param index the index into the history
	 * @return the subtask node path at <code>index</code> or <code>null</code> if
	 *         <code>index</code> is not a valid history index
	 */
	private String getHistoryEntry(int index) {
		if (index >= 0 && index < historyList.size()) {
			return (String) historyList.get(index);
		}
		return null;
	}

	/**
	 * Adds the subtask path to the history.
	 * 
	 * @param entry the subtask history entry
	 */
	public void addHistoryEntry(String entry) {
		if (historyIndex == -1 || !historyList.get(historyIndex).equals(entry)) {
			historyList.subList(historyIndex + 1, historyList.size()).clear();
			historyList.add(entry);
			historyIndex++;
			updateHistoryControls();
		}
	}

	public void clearHistory() {
		historyList.clear();
		historyIndex = -1;
		updateHistoryControls();
	}

	/**
	 * Sets the current page to be the one corresponding to the given index in the
	 * page history.
	 * 
	 * @param index the index into the page history
	 */
	private void jumpToHistory(int index) {
		if (index >= 0 && index < historyList.size()) {
			historyIndex = index;
			parentTask.switchTo(getHistoryEntry(index));
		}
		updateHistoryControls();
	}

	/**
	 * Updates the history controls.
	 * 
	 */
	private void updateHistoryControls() {
		if (historyToolbar != null) {
			historyToolbar.update(false);
			IContributionItem[] items = historyToolbar.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].update(IAction.ENABLED);
				items[i].update(IAction.TOOL_TIP_TEXT);
			}
		}
	}

	/**
	 * Creates the history toolbar and initializes <code>historyToolbar</code>.
	 * 
	 * @param historyBar
	 * @param manager
	 * @return the control of the history toolbar
	 */
	public ToolBar createHistoryControls(ToolBar historyBar, ToolBarManager manager) {

		historyToolbar = manager;
		/**
		 * Superclass of the two for-/backward actions for the history.
		 */
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

		/**
		 * Menu entry for the toolbar dropdowns. Instances are direct-jump entries in
		 * the navigation history.
		 */
		class HistoryItemAction extends Action {

			private final int index;

			HistoryItemAction(int index, String label) {
				super(label, IAction.AS_PUSH_BUTTON);
				this.index = index;
			}

			public void run() {
				if (isCurrentEntryAvailable(index)) {
					jumpToHistory(index);
				}
			}
		}

		HistoryNavigationAction backward = new HistoryNavigationAction() {

			public void run() {
				int index = historyIndex - 1;
				while (!isCurrentEntryAvailable(index)) {
					index--;
				}
				jumpToHistory(index);
			}

			public boolean isEnabled() {
				boolean enabled = historyIndex > 0;
				if (enabled) {
					int index = historyIndex - 1;
					setToolTipText(isCurrentEntryAvailable(
							index) ? Messages.getFormattedString("SubtaskHistory.Tooltip.Back", //$NON-NLS-1$
									getItemText(index)) : ""); //$NON-NLS-1$
				}
				return enabled;
			}

			protected void createEntries(Menu menu) {
				for (int i = historyIndex - 1, j = 0; i >= 0 && j < MAX_ENTRIES; i--) {
					if (isCurrentEntryAvailable(i)) {
						IAction action = new HistoryItemAction(i, getItemText(i));
						addActionToMenu(menu, action);
						j++;
					}
				}
			}
		};
		backward.setText(Messages.getString("SubtaskHistory.Text.Back")); //$NON-NLS-1$
		backward.setActionDefinitionId("org.eclipse.ui.navigate.backwardHistory"); //$NON-NLS-1$
		backward.setImageDescriptor(ImageDescriptor.createFromURL(UIHelper.getURL(UIHelper.IMAGE_NAV_BACKWARD)));
		backward.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(UIHelper.getURL(UIHelper.IMAGE_NAV_BACKWARD_DIS)));
		historyToolbar.add(backward);

		HistoryNavigationAction forward = new HistoryNavigationAction() {

			public void run() {
				int index = historyIndex + 1;
				while (!isCurrentEntryAvailable(index)) {
					index++;
				}
				jumpToHistory(index);
			}

			public boolean isEnabled() {
				boolean enabled = historyIndex < historyList.size() - 1;
				if (enabled) {
					int index = historyIndex + 1;
					setToolTipText(isCurrentEntryAvailable(
							index) ? Messages.getFormattedString("SubtaskHistory.Tooltip.Forward", //$NON-NLS-1$
									getItemText(index)) : ""); //$NON-NLS-1$
				}
				return enabled;
			}

			protected void createEntries(Menu menu) {
				for (int i = historyIndex + 1, j = 0; i < historyList.size() && j < MAX_ENTRIES; i++) {
					if (isCurrentEntryAvailable(i)) {
						IAction action = new HistoryItemAction(i, getItemText(i));
						addActionToMenu(menu, action);
						j++;
					}
				}
			}
		};
		forward.setText(Messages.getString("SubtaskHistory.Text.Forward")); //$NON-NLS-1$
		forward.setActionDefinitionId("org.eclipse.ui.navigate.forwardHistory"); //$NON-NLS-1$
		forward.setImageDescriptor(ImageDescriptor.createFromURL(UIHelper.getURL(UIHelper.IMAGE_NAV_FORWARD)));
		forward.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(UIHelper.getURL(UIHelper.IMAGE_NAV_FORWARD_DIS)));
		historyToolbar.add(forward);

		return historyBar;
	}

	private boolean isCurrentEntryAvailable(int index) {
		return index >= 0 && index < historyList.size()
				&& parentTask.getNavigatorTree().findTreeItem(getHistoryEntry(index)) != null;
	}

	private String getItemText(int index) {
		TreeItem item = parentTask.getNavigatorTree().findTreeItem(getHistoryEntry(index));
		if (item != null) {
			return item.getText();
		}
		return Messages.getString("SubtaskHistory.Text.Invalid"); //$NON-NLS-1$
	}

}
