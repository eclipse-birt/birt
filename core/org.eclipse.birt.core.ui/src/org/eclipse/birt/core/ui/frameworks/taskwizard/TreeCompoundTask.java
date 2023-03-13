/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
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

package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.ui.frameworks.taskwizard.composites.NavTree;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.internal.SubtaskHistory;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.TreeItem;

/**
 *
 * Compound task realized for tree navigator.
 *
 */

public class TreeCompoundTask extends CompoundTask {

	protected Composite cmpSubtaskContainer;
	protected NavTree navTree;
	private SubtaskHistory history;

	private boolean needHistory = true;

	protected static final String INDEX_SEPARATOR = " - "; //$NON-NLS-1$

	// Cache for subtask selection next time
	private Map<String, String> lastSubtaskRegistry = new HashMap<>();

	// Cache for popup selection next time. This will override the subtask
	// selection to the popup.
	private Map<String, String> lastPopupRegistry = new HashMap<>();

	/**
	 * Constructor
	 *
	 * @param sLabel      Title of this task
	 * @param needHistory Indicates whether history and navigation bar are needed
	 */
	public TreeCompoundTask(String sLabel, boolean needHistory) {
		super(sLabel);
		this.needHistory = needHistory;
		if (needHistory) {
			history = new SubtaskHistory(this);
		}
	}

	@Override
	public void createControl(Composite parent) {
		if (topControl == null || topControl.isDisposed()) {
			topControl = new Composite(parent, SWT.NONE);
			{
				GridLayout layout = new GridLayout(2, false);
				layout.marginHeight = 0;
				layout.marginWidth = 0;
				layout.horizontalSpacing = 0;
				topControl.setLayout(layout);
				GridData gridData = new GridData();
				gridData.horizontalAlignment = SWT.FILL;
				gridData.verticalAlignment = SWT.FILL;
				topControl.setLayoutData(gridData);
			}
			navTree = new NavTree(topControl, SWT.BORDER);
			{
				final GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_VERTICAL);
				gridData.widthHint = 127;
				navTree.setLayoutData(gridData);
				navTree.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event event) {
						switchToTreeItem((TreeItem) event.item);
					}
				});
			}
			cmpSubtaskContainer = createContainer(topControl);
		}

		updateTree();
		switchToDefaultItem();
	}

	protected void switchToDefaultItem() {
		TreeItem defaultSelection = getDefaultSelection();
		if (defaultSelection != null) {
			switchToTreeItem(defaultSelection);
		}
	}

	protected TreeItem getDefaultSelection() {
		TreeItem lastselection = navTree.findTreeItem(getSubtaskSelection());
		if (navTree.getSelection().length == 0) {
			if (lastselection != null) {
				return lastselection;
			} else if (navTree.getItems().length > 0) {
				return navTree.getItems()[0];
			}
			return null;
		} else {
			return navTree.getSelection()[0];
		}
	}

	/**
	 * Creates the UI in the right of tree navigator
	 *
	 * @param parent parent composite
	 * @return top composite of the right part
	 */
	protected Composite createContainer(Composite parent) {
		Composite cmpTask = new Composite(parent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.marginWidth = 10;
			cmpTask.setLayout(layout);
			GridData gridData = new GridData(GridData.FILL_BOTH);
			cmpTask.setLayoutData(gridData);
		}

		createTitleArea(cmpTask);
		return cmpTask;
	}

	/**
	 * Creates the compound task's title area.
	 *
	 * @param parent the SWT parent for the title area composite.
	 * @return the created title area composite.
	 */
	protected Composite createTitleArea(Composite parent) {
		Composite cmpTitle = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		cmpTitle.setLayout(layout);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		cmpTitle.setLayoutData(gridData);

		Label label = new Label(cmpTitle, SWT.NONE);
		{
			label.setFont(JFaceResources.getBannerFont());
			label.setText(getTitleAreaString());
		}

		if (needHistory) {
			ToolBar historyBar = new ToolBar(cmpTitle, SWT.HORIZONTAL | SWT.FLAT);
			{
				GridData gd = new GridData();
				gd.horizontalAlignment = SWT.END;
				historyBar.setLayoutData(gd);
				ToolBarManager historyManager = new ToolBarManager(historyBar);
				history.createHistoryControls(historyBar, historyManager);
				historyManager.update(false);
			}
		} else {
			new Label(cmpTitle, SWT.NONE);
		}
		return cmpTitle;
	}

	protected String getTitleAreaString() {
		return ""; //$NON-NLS-1$
	}

	protected void populateSubtasks() {
		// Do nothing
	}

	protected void updateTreeItem() {
		// Do nothing
	}

	/**
	 * Updates left tree which contains all page nodes.
	 *
	 * @since 2.3
	 */
	public void updateTree() {
		populateSubtasks();
		updateTreeItem();

		// Select default selection
		TreeItem defaultSelection = getDefaultSelection();
		if (defaultSelection != null) {
			navTree.setSelection(new TreeItem[] { defaultSelection });
		}

	}

	@Override
	protected ISubtaskSheet getSubtask(String sSubtaskPath) {
		int separatorIndex = sSubtaskPath.indexOf(INDEX_SEPARATOR);
		int subtaskIndex = 0;
		// If the subtask is present several times, need the node index to
		// distinguish between them
		if (separatorIndex > -1) {
			subtaskIndex = Integer.parseInt(sSubtaskPath.substring(separatorIndex + INDEX_SEPARATOR.length()).trim())
					- 1;
			sSubtaskPath = sSubtaskPath.substring(0, separatorIndex).trim();
		}
		ISubtaskSheet itask = super.getSubtask(sSubtaskPath);
		itask.setIndex(subtaskIndex);
		return itask;
	}

	@Override
	protected boolean containSubtask(String sSubtaskPath) {
		int separatorIndex = sSubtaskPath.indexOf(INDEX_SEPARATOR);
		// If the subtask is present several times, need the node index to
		// distinguish between them
		if (separatorIndex > -1) {
			sSubtaskPath = sSubtaskPath.substring(0, separatorIndex).trim();
		}
		return super.containSubtask(sSubtaskPath);
	}

	protected void switchTo(String sSubtaskPath, boolean needSelection) {
		super.switchTo(sSubtaskPath);

		if (needSelection) {
			// Select the node in the left tree
			TreeItem treeItem = navTree.findTreeItem(sSubtaskPath);
			if (treeItem != null) {
				navTree.setSelection(new TreeItem[] { treeItem });
			}
		}

		// Update detail UI
		createSubtaskArea(cmpSubtaskContainer, getSubtask(sSubtaskPath));
		cmpSubtaskContainer.layout();
		if (container != null) {
			container.packWizard();
		}

		// Save subtask selection
		setSubtaskSelection(sSubtaskPath);
		// Refresh help tray
		if (container != null) {
			container.firePageChanged(getCurrentSubtask());
		}
		// Add to
		if (needHistory) {
			history.addHistoryEntry(sSubtaskPath);
		}
	}

	@Override
	public void switchTo(String sSubtaskPath) {
		switchTo(sSubtaskPath, true);
	}

	/**
	 * Switches to the specified subtask and sets the selection
	 *
	 * @param treeItem Tree item corresponded to the subtask
	 */
	public void switchToTreeItem(TreeItem treeItem) {
		if (treeItem == null) {
			return;
		}
		navTree.setSelection(new TreeItem[] { treeItem });
		switchTo(navTree.getNodePath(treeItem), false);
	}

	protected void createSubtaskArea(Composite parent, ISubtaskSheet subtask) {
		if (subtask != null) {
			subtask.createControl(parent);
			subtask.attachPopup(getPopupSelection());
		}
	}

	public NavTree getNavigatorTree() {
		return navTree;
	}

	/**
	 * Stores the last popup selection to open in the next time. If this selection
	 * is not existent in current subtask, to open the popup stored in the subtask.
	 *
	 * @param popupName popup key registered in the subtask.
	 */
	public void setPopupSelection(String popupName) {
		lastPopupRegistry.put(getContext().getWizardID(), popupName);
	}

	protected String getPopupSelection() {
		return lastPopupRegistry.get(getContext().getWizardID());
	}

	protected void setSubtaskSelection(String subtaskPath) {
		lastSubtaskRegistry.put(getContext().getWizardID(), subtaskPath);
	}

	protected String getSubtaskSelection() {
		return lastSubtaskRegistry.get(getContext().getWizardID());
	}

	@Override
	public void dispose() {
		super.dispose();
		if (needHistory) {
			history.clearHistory();
		}
	}

}
