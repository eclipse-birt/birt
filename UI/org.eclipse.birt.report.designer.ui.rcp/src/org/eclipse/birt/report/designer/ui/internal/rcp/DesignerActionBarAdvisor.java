/*******************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.internal.rcp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.internal.rcp.actions.IExtensionFile;
import org.eclipse.birt.report.designer.ui.internal.rcp.actions.INewExtensionFileWorkbenchAction;
import org.eclipse.birt.report.designer.ui.internal.rcp.actions.NewLibraryAction;
import org.eclipse.birt.report.designer.ui.internal.rcp.actions.NewReportAction;
import org.eclipse.birt.report.designer.ui.internal.rcp.actions.NewReportTemplateAction;
import org.eclipse.birt.report.designer.ui.internal.rcp.actions.OpenFileAction;
import org.eclipse.birt.report.designer.ui.rcp.nls.DesignerWorkbenchMessages;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.NewWizardDropDownAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * Class for configuring the action part of the BIRT RCP designer.
 */
public class DesignerActionBarAdvisor extends ActionBarAdvisor {

	private final IWorkbenchWindow window;

	// generic actions
	private IWorkbenchAction openFileAction;

	private IWorkbenchAction newReportAction;

	private IWorkbenchAction newLibraryAction;

	private IWorkbenchAction[] newActions;

	private IWorkbenchAction newReportTemplateAction;

	private IWorkbenchAction closeAction;

	private IWorkbenchAction closeAllAction;

	private IWorkbenchAction saveAction;

	private IWorkbenchAction saveAllAction;

	private IWorkbenchAction helpContentsAction;

	private IWorkbenchAction aboutAction;

	private IWorkbenchAction openPreferencesAction;

	private IWorkbenchAction saveAsAction;

	private IWorkbenchAction lockToolBarAction;

	private IWorkbenchAction backwardHistoryAction;

	private IWorkbenchAction forwardHistoryAction;

	// generic re-target actions
	private IWorkbenchAction undoAction;

	private IWorkbenchAction redoAction;

	private IWorkbenchAction cutAction;

	private IWorkbenchAction copyAction;

	private IWorkbenchAction pasteAction;

	private IWorkbenchAction deleteAction;

	private IWorkbenchAction selectAllAction;

	private IWorkbenchAction findAction;

	private IWorkbenchAction quitAction;

	// IDE-specific actions
	// private IWorkbenchAction quickStartAction;

	// private IWorkbenchAction tipsAndTricksAction;

	private IWorkbenchAction introAction;

	// contribution items
	private IContributionItem pinEditorContributionItem;

	/**
	 * Indicates if the action builder has been disposed
	 */
	private boolean isDisposed = false;

	/**
	 * Constructs a new action builder which contributes actions to the given
	 * window.
	 *
	 * @param configurer the action bar configurer for the window
	 */
	public DesignerActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		window = configurer.getWindowConfigurer().getWindow();
	}

	/**
	 * Returns the window to which this action builder is contributing.
	 */
	private IWorkbenchWindow getWindow() {
		return window;
	}

	/**
	 * Fills the coolbar with the workbench actions.
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		{
			// Set up the context Menu
			IMenuManager popUpMenu = new MenuManager();
			popUpMenu.add(new ActionContributionItem(lockToolBarAction));
			coolBar.setContextMenuManager(popUpMenu);
		}
		coolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_FILE));
		{
			// File Group
			IToolBarManager fileToolBar = new ToolBarManager(coolBar.getStyle());

			fileToolBar.add(new NewWizardDropDownAction(window));

//			fileToolBar.add( newReportAction );
//			fileToolBar.add( newLibraryAction );
//			fileToolBar.add( newReportTemplateAction );
			fileToolBar.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
			fileToolBar.add(new GroupMarker(IWorkbenchActionConstants.SAVE_GROUP));
			fileToolBar.add(saveAction);
			fileToolBar.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));

			fileToolBar.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

			// Add to the cool bar manager
			coolBar.add(new ToolBarContributionItem(fileToolBar, IWorkbenchActionConstants.TOOLBAR_FILE));
		}

		coolBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		// coolBar.add( new GroupMarker( IWorkbenchConstants.GROUP_NAV ) );
		{
			// Navigate group
			IToolBarManager navToolBar = new ToolBarManager(coolBar.getStyle());
			navToolBar.add(new Separator(IWorkbenchActionConstants.HISTORY_GROUP));
			navToolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_APP));
			navToolBar.add(backwardHistoryAction);
			navToolBar.add(forwardHistoryAction);
			navToolBar.add(new Separator(IWorkbenchActionConstants.PIN_GROUP));
			navToolBar.add(pinEditorContributionItem);

			// Add to the cool bar manager
			coolBar.add(new ToolBarContributionItem(navToolBar, IWorkbenchActionConstants.TOOLBAR_NAVIGATE));
		}

		coolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_EDITOR));

		coolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_HELP));

		{
			// Help group
			IToolBarManager helpToolBar = new ToolBarManager(coolBar.getStyle());
			helpToolBar.add(new Separator(IWorkbenchActionConstants.GROUP_HELP));

			// Add the group for applications to contribute
			helpToolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_APP));
			// Add to the cool bar manager
			coolBar.add(new ToolBarContributionItem(helpToolBar, IWorkbenchActionConstants.TOOLBAR_HELP));
		}

	}

	/**
	 * Fills the menu bar with the workbench actions.
	 */
	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		menuBar.add(createFileMenu());
		menuBar.add(createEditMenu());
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(createWindowMenu());
		menuBar.add(createHelpMenu());
	}

	/**
	 * Creates and returns the File menu.
	 */
	private MenuManager createFileMenu() {
		MenuManager menu = new MenuManager(DesignerWorkbenchMessages.Workbench_file, IWorkbenchActionConstants.M_FILE);

		MenuManager newMenu = new MenuManager(DesignerWorkbenchMessages.Workbench_new, ActionFactory.NEW.getId());
		newMenu.add(newReportAction);
		newMenu.add(newLibraryAction);
		newMenu.add(newReportTemplateAction);
		for (int i = 0; i < newActions.length; i++) {
			newMenu.add(newActions[i]);
		}
		menu.add(newMenu);

		menu.add(openFileAction);
		menu.add(new Separator());

		menu.add(closeAction);
		menu.add(closeAllAction);
		menu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
		menu.add(new Separator());
		menu.add(saveAction);
		menu.add(saveAsAction);
		menu.add(saveAllAction);
		menu.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));

		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		menu.add(ContributionItemFactory.REOPEN_EDITORS.create(getWindow()));
		menu.add(new GroupMarker(IWorkbenchActionConstants.MRU));
		menu.add(new Separator());
		menu.add(quitAction);
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		return menu;
	}

	/**
	 * Creates and returns the Edit menu.
	 */
	private MenuManager createEditMenu() {
		MenuManager menu = new MenuManager(DesignerWorkbenchMessages.Workbench_edit, IWorkbenchActionConstants.M_EDIT);
		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));

		menu.add(undoAction);
		menu.add(redoAction);
		menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
		menu.add(new Separator());

		menu.add(cutAction);
		menu.add(copyAction);
		menu.add(pasteAction);
		menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
		menu.add(new Separator());

		menu.add(deleteAction);
		menu.add(selectAllAction);
		menu.add(new Separator());

		menu.add(findAction);
		menu.add(new GroupMarker(IWorkbenchActionConstants.FIND_EXT));
		menu.add(new Separator());

		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		return menu;
	}

	/**
	 * Creates and returns the Window menu.
	 */
	private MenuManager createWindowMenu() {
		MenuManager menu = new MenuManager(DesignerWorkbenchMessages.Workbench_window,
				IWorkbenchActionConstants.M_WINDOW);

		addPerspectiveActions(menu);
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(openPreferencesAction);
		return menu;
	}

	/**
	 * Adds the perspective actions to the specified menu.
	 */
	private void addPerspectiveActions(MenuManager menu) {
		{
			MenuManager showViewMenuMgr = new MenuManager(DesignerWorkbenchMessages.Workbench_showView, "showView"); //$NON-NLS-1$
			IContributionItem showViewMenu = ContributionItemFactory.VIEWS_SHORTLIST.create(getWindow());
			showViewMenuMgr.add(showViewMenu);
			menu.add(showViewMenuMgr);
		}
	}

	/**
	 * Creates and returns the Help menu.
	 */
	private MenuManager createHelpMenu() {
		MenuManager menu = new MenuManager(DesignerWorkbenchMessages.Workbench_help, IWorkbenchActionConstants.M_HELP);

		// See if a welcome or introduction page is specified
		if (introAction != null) {
			menu.add(introAction);
		}
//		else if ( quickStartAction != null )
//			menu.add( quickStartAction );
		menu.add(helpContentsAction);

//		if ( tipsAndTricksAction != null )
//			menu.add( tipsAndTricksAction );

		menu.add(new GroupMarker("group.tutorials")); //$NON-NLS-1$
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new Separator("group.about")); //$NON-NLS-1$
		menu.add(aboutAction);
		menu.add(new GroupMarker("group.about.ext")); //$NON-NLS-1$

		return menu;
	}

	/**
	 * Disposes any resources and unhooks any listeners that are no longer needed.
	 * Called when the window is closed.
	 */
	@Override
	public void dispose() {
		if (isDisposed) {
			return;
		}
		isDisposed = true;

		pinEditorContributionItem.dispose();

		// null out actions to make leak debugging easier
		openFileAction = null;
		newReportAction = null;
		newLibraryAction = null;
		newReportTemplateAction = null;

		Arrays.fill(newActions, null);
		closeAction = null;
		closeAllAction = null;
		saveAction = null;
		saveAllAction = null;
		helpContentsAction = null;
		aboutAction = null;
		openPreferencesAction = null;
		saveAsAction = null;
		lockToolBarAction = null;
		backwardHistoryAction = null;
		forwardHistoryAction = null;
		undoAction = null;
		redoAction = null;
		cutAction = null;
		copyAction = null;
		pasteAction = null;
		deleteAction = null;
		selectAllAction = null;
		findAction = null;
		quitAction = null;
		// quickStartAction = null;
		// tipsAndTricksAction = null;
		pinEditorContributionItem = null;
		introAction = null;

		super.dispose();
	}

	/**
	 * Returns true if the menu with the given ID should be considered as an OLE
	 * container menu. Container menus are preserved in OLE menu merging.
	 */
	@Override
	public boolean isApplicationMenu(String menuId) {
		if (menuId.equals(IWorkbenchActionConstants.M_FILE) || menuId.equals(IWorkbenchActionConstants.M_WINDOW)) {
			return true;
		}
		return false;
	}

	/**
	 * Return whether or not given id matches the id of the cool items that the
	 * workbench creates.
	 */
	public boolean isWorkbenchCoolItemId(String id) {
		if (IWorkbenchActionConstants.TOOLBAR_FILE.equalsIgnoreCase(id) || IWorkbenchActionConstants.TOOLBAR_NAVIGATE.equalsIgnoreCase(id)) {
			return true;
		}
		return false;
	}

	/**
	 * Fills the status line with the workbench contribution items.
	 */
	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		// Do nothing
	}

	/**
	 * Creates actions (and contribution items) for the menu bar, toolbar and status
	 * line.
	 */
	@Override
	public void makeActions(final IWorkbenchWindow window) {
		openFileAction = new OpenFileAction(window);
		register(openFileAction);

		newReportAction = new NewReportAction(window);
		register(newReportAction);

		newLibraryAction = new NewLibraryAction(window);
		register(newLibraryAction);

		newReportTemplateAction = new NewReportTemplateAction(window);
		register(newReportTemplateAction);

		saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);

		saveAsAction = ActionFactory.SAVE_AS.create(window);
		register(saveAsAction);

		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		register(saveAllAction);

		undoAction = ActionFactory.UNDO.create(window);
		register(undoAction);

		redoAction = ActionFactory.REDO.create(window);
		register(redoAction);

		cutAction = ActionFactory.CUT.create(window);
		register(cutAction);

		copyAction = ActionFactory.COPY.create(window);
		register(copyAction);

		pasteAction = ActionFactory.PASTE.create(window);
		register(pasteAction);

		selectAllAction = ActionFactory.SELECT_ALL.create(window);
		register(selectAllAction);

		findAction = ActionFactory.FIND.create(window);
		register(findAction);

		closeAction = ActionFactory.CLOSE.create(window);
		register(closeAction);

		closeAllAction = ActionFactory.CLOSE_ALL.create(window);
		register(closeAllAction);

		helpContentsAction = ActionFactory.HELP_CONTENTS.create(window);
		register(helpContentsAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		aboutAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor("IMG_OBJS_DEFAULT_PROD")); //$NON-NLS-1$
		register(aboutAction);

		openPreferencesAction = ActionFactory.PREFERENCES.create(window);
		register(openPreferencesAction);

		deleteAction = ActionFactory.DELETE.create(window);
		register(deleteAction);

		makeFeatureDependentActions(window);

		lockToolBarAction = ActionFactory.LOCK_TOOL_BAR.create(window);
		register(lockToolBarAction);

		forwardHistoryAction = ActionFactory.FORWARD_HISTORY.create(window);
		register(forwardHistoryAction);

		backwardHistoryAction = ActionFactory.BACKWARD_HISTORY.create(window);
		register(backwardHistoryAction);

		quitAction = ActionFactory.QUIT.create(window);
		register(quitAction);

		// register the new actions
		Object[] adapters = ElementAdapterManager.getAdapters(this, IExtensionFile.class);
		List<IWorkbenchAction> tempList = new ArrayList<>();

		if (adapters != null) {
			for (int i = 0; i < adapters.length; i++) {
				IExtensionFile newFile = (IExtensionFile) adapters[i];
				INewExtensionFileWorkbenchAction action = newFile.getNewAction();
				if (action == null) {
					continue;
				}
				action.init(window);
				register(action);
				tempList.add(action);
			}

			newActions = tempList.toArray(new IWorkbenchAction[tempList.size()]);
		} else {
			newActions = new IWorkbenchAction[0];
		}

		if (window.getWorkbench().getIntroManager().hasIntro()) {
			introAction = ActionFactory.INTRO.create(window);
			register(introAction);
		}

		pinEditorContributionItem = ContributionItemFactory.PIN_EDITOR.create(window);
	}

	/**
	 * Creates the feature-dependent actions for the menu bar.
	 */
	private void makeFeatureDependentActions(IWorkbenchWindow window) {
		IPreferenceStore prefs = ReportPlugin.getDefault().getPreferenceStore();

		String stateKey = "platformState"; //$NON-NLS-1$
		String prevState = prefs.getString(stateKey);
		String currentState = String.valueOf(Platform.getStateStamp());
		boolean sameState = currentState.equals(prevState);
		if (!sameState) {
			prefs.putValue(stateKey, currentState);
		}
	}
}
