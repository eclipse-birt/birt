/*************************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ContextMenuProvider;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.AddElementtoReport;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.AddResourceAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.AddSelectedLibToCurrentReportDesignAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.ApplyThemeAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.CopyResourceAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.DeleteResourceAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.FilterResourceAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.MoveResourceAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.NewFolderAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.NewLibraryAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.PasteResourceAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.RefreshResourceExplorerAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.RenameResourceAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.UseCSSAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ResourceEntryWrapper;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;

/**
 * This class provides the context menu for the single selection and multiple
 * selection
 * 
 * 
 */
public class LibraryExplorerContextMenuProvider extends ContextMenuProvider {

	// Defines actions
	private final IAction filterAction;
	private final IAction refreshExplorerAction;
	private final IAction useCSSAction;
	private final IAction useLibraryAction;
	private final IAction deleteResourceAction;
	private final IAction renameResourceAction;
	private final IAction pasteResourceAction;
	private final IAction copyResourceAction;
	private final IAction moveResourceAction;
	private final IAction addResourceAction;
	private final IAction newFolderAction;
	private final IAction newLibraryAction;

	private final LibraryExplorerTreeViewPage page;
	private Clipboard clipboard;

	/**
	 * constructor
	 * 
	 * @param page     the viewer
	 * @param registry the registry
	 */
	public LibraryExplorerContextMenuProvider(LibraryExplorerTreeViewPage page) {
		super(page.getTreeViewer());
		this.page = page;

		clipboard = new Clipboard(page.getSite().getShell().getDisplay());

		filterAction = new FilterResourceAction(page);
		refreshExplorerAction = new RefreshResourceExplorerAction(page);
		useCSSAction = new UseCSSAction(page);
		useLibraryAction = new AddSelectedLibToCurrentReportDesignAction(page.getTreeViewer());
		deleteResourceAction = new DeleteResourceAction(page);
		addResourceAction = new AddResourceAction(page);
		renameResourceAction = new RenameResourceAction(page);
		newFolderAction = new NewFolderAction(page);
		moveResourceAction = new MoveResourceAction(page);
		newLibraryAction = new NewLibraryAction(page);
		copyResourceAction = new CopyResourceAction(page, clipboard);
		pasteResourceAction = new PasteResourceAction(page, clipboard);

		handleGlobalAction();
		page.addSelectionChangedListener(new ISelectionChangedListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged
			 * (org.eclipse.jface.viewers.SelectionChangedEvent)
			 */
			public void selectionChanged(SelectionChangedEvent event) {
				resetActionStatus();
				updateActionBars();
			}
		});
	}

	/**
	 * Handles all global actions
	 */
	private void handleGlobalAction() {
		IPageSite pageSite = page == null ? null : page.getSite();
		IActionBars actionBars = pageSite == null ? null : pageSite.getActionBars();

		if (actionBars != null) {
			String copyID = ActionFactory.COPY.getId();
			String pasteID = ActionFactory.PASTE.getId();
			String deleteID = ActionFactory.DELETE.getId();
			String moveID = ActionFactory.MOVE.getId();
			String renameID = ActionFactory.RENAME.getId();
			String refreshID = ActionFactory.REFRESH.getId();

			actionBars.setGlobalActionHandler(copyID, copyResourceAction);
			actionBars.setGlobalActionHandler(pasteID, pasteResourceAction);
			actionBars.setGlobalActionHandler(deleteID, deleteResourceAction);
			actionBars.setGlobalActionHandler(moveID, moveResourceAction);
			actionBars.setGlobalActionHandler(renameID, renameResourceAction);
			actionBars.setGlobalActionHandler(refreshID, refreshExplorerAction);

			IMenuManager menuManager = actionBars.getMenuManager();
			IToolBarManager toolBarManager = actionBars.getToolBarManager();

			if (menuManager != null) {
				menuManager.add(filterAction);
			}
			if (toolBarManager != null) {
				toolBarManager.add(refreshExplorerAction);
			}
		}
	}

	/**
	 * Updates the action bars for this page site.
	 */
	private void updateActionBars() {
		IPageSite site = page == null ? null : page.getSite();
		IActionBars actionBars = site == null ? null : site.getActionBars();

		if (actionBars != null) {
			actionBars.updateActionBars();
		}
	}

	@Override
	public void dispose() {
		if (clipboard != null) {
			clipboard.dispose();
			clipboard = null;
		}
		super.dispose();
	}

	/**
	 * Builds the context menu. Single selection menu and multiple selection menu
	 * are created while selecting just single element or multiple elements
	 * 
	 * 
	 * @param menu the menu
	 */
	public void buildContextMenu(IMenuManager menu) {
		if (Policy.TRACING_MENU_SHOW) {
			System.out.println("Menu(for Views) >> Shows for library"); //$NON-NLS-1$
		}

		resetActionStatus();

		menu.removeAll();
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new Separator());

		IStructuredSelection selection = (IStructuredSelection) page.getSelection();
		IMenuManager newMenuGroup = new MenuManager(Messages.getString("NewResource.MenuGroup.Text")); //$NON-NLS-1$

		newMenuGroup.add(newFolderAction);
		newMenuGroup.add(newLibraryAction);

		if (selection != null && selection.getFirstElement() != null) {
			Object selected = selection.getFirstElement();

			if (selected instanceof ReportResourceEntry) {
				selected = ((ReportResourceEntry) selected).getReportElement();
			}

			if (selected instanceof ResourceEntryWrapper) {
				int type = ((ResourceEntryWrapper) selected).getType();

				if (type == ResourceEntryWrapper.LIBRARY) {
					menu.add(useLibraryAction);
				} else if (type == ResourceEntryWrapper.CSS_STYLE_SHEET) {
					menu.add(useCSSAction);
				}

				if (((ResourceEntryWrapper) selected).getParent() instanceof PathResourceEntry) {
					menu.add(new Separator());
					menu.add(newMenuGroup);
					menu.add(addResourceAction);
				}

				menu.add(new Separator());
				menu.add(copyResourceAction);

				if (((ResourceEntryWrapper) selected).getParent() instanceof PathResourceEntry) {
					menu.add(pasteResourceAction);
					menu.add(deleteResourceAction);
					menu.add(moveResourceAction);
					menu.add(renameResourceAction);
					menu.add(new Separator());
				}
			} else if (selected instanceof LibraryHandle) {
				menu.add(useLibraryAction);
				menu.add(new Separator());
			} else if (selected instanceof CssStyleSheetHandle) {
				menu.add(useCSSAction);
				menu.add(new Separator());
			} else if (selected instanceof PathResourceEntry) {
				menu.add(newMenuGroup);
				menu.add(addResourceAction);
				menu.add(new Separator());
				menu.add(copyResourceAction);
				menu.add(pasteResourceAction);
				menu.add(deleteResourceAction);
				menu.add(moveResourceAction);
				menu.add(renameResourceAction);
				menu.add(new Separator());
			} else if (selected instanceof FragmentResourceEntry) {
				if (copyResourceAction.isEnabled()) {
					menu.add(copyResourceAction);
					menu.add(new Separator());
				}
			}

			if (canAddtoReport(selected)) {
				if (selection.size() == 1) {
					AddElementtoReport addElementAction = new AddElementtoReport((StructuredViewer) getViewer());
					addElementAction.setSelectedElement(selected);
					menu.add(addElementAction);
					menu.add(new Separator());
				}
			} else if (isTheme(selected)) {
				if (selection.size() == 1) {
					ApplyThemeAction applyThemeAction = new ApplyThemeAction();
					applyThemeAction.setSelectedTheme(selected);
					menu.add(applyThemeAction);
					menu.add(new Separator());
				}

			}
			menu.add(new Separator());
			menu.add(refreshExplorerAction);
		} else {
			menu.add(addResourceAction);
			menu.add(new Separator());
			menu.add(refreshExplorerAction);
		}
	}

	/**
	 * Resets all action status.
	 */
	private void resetActionStatus() {
		// Resets actions status.
		filterAction.setEnabled(isEnabled());
		refreshExplorerAction.setEnabled(refreshExplorerAction.isEnabled());
		useCSSAction.setEnabled(useCSSAction.isEnabled());
		useLibraryAction.setEnabled(useLibraryAction.isEnabled());
		deleteResourceAction.setEnabled(deleteResourceAction.isEnabled());
		addResourceAction.setEnabled(addResourceAction.isEnabled());
		renameResourceAction.setEnabled(renameResourceAction.isEnabled());
		newFolderAction.setEnabled(newFolderAction.isEnabled());
		moveResourceAction.setEnabled(moveResourceAction.isEnabled());
		newLibraryAction.setEnabled(newLibraryAction.isEnabled());
		copyResourceAction.setEnabled(copyResourceAction.isEnabled());
		pasteResourceAction.setEnabled(pasteResourceAction.isEnabled());
	}

	protected boolean isTheme(Object transfer) {
		if (transfer instanceof ReportResourceEntry) {
			transfer = ((ReportResourceEntry) transfer).getReportElement();
		}

		if (transfer instanceof ThemeHandle) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean canAddtoReport(Object transfer) {
		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		if (!(moduleHandle instanceof ReportDesignHandle || moduleHandle instanceof LibraryHandle)) {
			return false;
		}
		if (transfer instanceof ReportResourceEntry)
			transfer = ((ReportResourceEntry) transfer).getReportElement();
		if (transfer instanceof ReportElementHandle || transfer instanceof EmbeddedImageHandle) {
			if (transfer instanceof ScalarParameterHandle
					&& ((ScalarParameterHandle) transfer).getContainer() instanceof CascadingParameterGroupHandle) {
				return false;
			} else if (transfer instanceof StyleHandle
					&& ((StyleHandle) transfer).getContainer() instanceof ThemeHandle) {
				return false;
			} else if (transfer instanceof ThemeHandle) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
}
