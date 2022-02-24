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

package org.eclipse.birt.report.designer.ui.editors.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.birt.report.designer.internal.ui.extension.EditorContributorManager;
import org.eclipse.birt.report.designer.internal.ui.extension.EditorContributorManager.EditorContributor;
import org.eclipse.birt.report.designer.internal.ui.extension.FormPageDef;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction;
import org.eclipse.birt.report.designer.ui.actions.NoneAction;
import org.eclipse.birt.report.designer.ui.editors.AbstractMultiPageEditor;
import org.eclipse.birt.report.designer.ui.editors.IMultiPageEditorActionBarContributor;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * Abstract editor action bar contributor for designers
 */

public abstract class MultiPageEditorActionBarContributor extends EditorActionBarContributor
		implements IMultiPageEditorActionBarContributor {

	/**
	 * The name of the page menu;
	 */
	public static final String M_PAGE = "birtPage"; //$NON-NLS-1$

	/**
	 * The name of page menu group
	 */
	public static final String PAGE_SET_GROUP = "pageSetGroup"; //$NON-NLS-1$

	/**
	 * The end of page menu group
	 */
	public static final String PAGE_SET_GROUP_END = "pageSetGroupEnd"; //$NON-NLS-1$

	private SubActionBarDef currentActionBarDef;
	private Map subBarMap;

	private static class SubActionBarDef {

		private SubActionBars subActionBar;
		private IEditorActionBarContributor actionBarContrubutor;

		public SubActionBarDef(IActionBars rootBar, IEditorActionBarContributor actionBarContrubutor) {
			this.subActionBar = new SubActionBars(rootBar);
			this.actionBarContrubutor = actionBarContrubutor;
		}

		public void init(IWorkbenchPage page) {
			actionBarContrubutor.init(subActionBar, page);
		}

		public SubActionBars getSubActionBar() {
			return subActionBar;
		}

		public void setActiveEditor(IEditorPart editor) {
			actionBarContrubutor.setActiveEditor(editor);
		}

		public void activate() {
			subActionBar.activate();
		}

		public void deactivate() {
			subActionBar.deactivate();
		}

		public void updateActionBars() {
			subActionBar.updateActionBars();
			subActionBar.getToolBarManager().update(true);
		}

		public void dispose() {
			subActionBar.deactivate();
			subActionBar.dispose();

			actionBarContrubutor.dispose();
		}
	}

	public void setActiveEditor(IEditorPart targetEditor) {
		super.setActiveEditor(targetEditor);
		if (targetEditor instanceof IReportEditor) {
			targetEditor = ((IReportEditor) targetEditor).getEditorPart();
		}
		if (targetEditor instanceof AbstractMultiPageEditor) {
			AbstractMultiPageEditor editor = (AbstractMultiPageEditor) targetEditor;
			if (editor.getActivePageInstance() != null) {
				setActivePage(editor.getActivePageInstance());
			}
			if (currentActionBarDef != null) {
				currentActionBarDef.updateActionBars();
			}
		} else {
			return;
		}
		getActionBars().getToolBarManager().update(true);
		getActionBars().updateActionBars();

	}

	public void setActivePage(IFormPage page) {
		if (page == null) {
			return;
		}
		if (subBarMap == null) {
			subBarMap = new HashMap();
		}
		if (currentActionBarDef != null) {
			currentActionBarDef.deactivate();
			currentActionBarDef.dispose();
			currentActionBarDef = null;
		}
		IActionBars rootBar = getActionBars();
		if (page != null) {
			// currentActionBarDef = (SubActionBarDef) subBarMap.get(
			// page.getId( ) );
			if (currentActionBarDef == null) {
				FormEditor editor = page.getEditor();
				if (editor != null) {
					EditorContributor contributor = EditorContributorManager.getInstance()
							.getEditorContributor(editor.getSite().getId());
					if (contributor != null) {
						FormPageDef pageDef = contributor.getPage(page.getId());
						if (pageDef != null) {
							IEditorActionBarContributor actionBarContributor = pageDef.createActionBarContributor();

							if (actionBarContributor != null) {
								currentActionBarDef = new SubActionBarDef(rootBar, actionBarContributor);
								currentActionBarDef.init(getPage());
								// subBarMap.put( page.getId( ),
								// currentActionBarDef
								// );
							}
						}
					}
				}
			}
		}
		rootBar.clearGlobalActionHandlers();
		if (currentActionBarDef != null) {
			currentActionBarDef.setActiveEditor(page);
			Map handlers = currentActionBarDef.getSubActionBar().getGlobalActionHandlers();
			if (handlers != null) {
				for (Iterator iter = handlers.entrySet().iterator(); iter.hasNext();) {
					Map.Entry entry = (Map.Entry) iter.next();
					rootBar.setGlobalActionHandler(entry.getKey().toString(), (IAction) entry.getValue());
				}
			}
			currentActionBarDef.activate();
			currentActionBarDef.updateActionBars();
		}

		rootBar.getToolBarManager().update(true);
		rootBar.updateActionBars();

	}

	public void dispose() {
		if (subBarMap != null) {
			for (Iterator iter = subBarMap.values().iterator(); iter.hasNext();) {
				SubActionBarDef def = (SubActionBarDef) iter.next();
				def.dispose();
			}
			subBarMap.clear();
		}

		if (currentActionBarDef != null) {
			currentActionBarDef.deactivate();
			currentActionBarDef.dispose();
			currentActionBarDef = null;
		}
		super.dispose();
	}

	public void contributeToMenu(IMenuManager menuManager) {
		super.contributeToMenu(menuManager);
		// Page Menu
		menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT, createPageMenu());

		menuManager.update();
	};

	/**
	 * Returns the id of the editor to contribute
	 * 
	 * @return the editor id
	 */
	abstract public String getEditorId();

	protected IMenuManager createPageMenu() {
		MenuManager menuManager = new MenuManager(Messages.getString("DesignerActionBarContributor.menu.page"), M_PAGE); //$NON-NLS-1$
		menuManager.add(new Separator(PAGE_SET_GROUP));

		final ArrayList updateActions = new ArrayList();
		EditorContributor editorContruContributor = EditorContributorManager.getInstance()
				.getEditorContributor(getEditorId());
		for (int i = editorContruContributor.formPageList.size() - 1; i >= 0; i--) {
			FormPageDef page = editorContruContributor.getPage(i);
			final IAction action = page.pageAction;
			if (action instanceof UpdateAction) {
				updateActions.add(action);
			}
			if (action instanceof MenuUpdateAction) {
				final MenuManager subMenu = new MenuManager(page.displayName);
				subMenu.add(new NoneAction());
				subMenu.addMenuListener(new IMenuListener() {

					public void menuAboutToShow(IMenuManager manager) {
						((MenuUpdateAction) action).updateMenu(subMenu);

					}
				});
				menuManager.insertAfter(PAGE_SET_GROUP, subMenu);
			} else {
				menuManager.insertAfter(PAGE_SET_GROUP, action);
			}
		}
		menuManager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				for (Iterator iter = updateActions.iterator(); iter.hasNext();) {
					((UpdateAction) iter.next()).update();
				}
			}

		});

		menuManager.add(new Separator(PAGE_SET_GROUP_END));
		return menuManager;
	}

}
