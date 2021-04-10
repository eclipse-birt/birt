/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportCSSStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ReloadCssStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.UseCssStyleAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.StyleBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementDetailHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with theme node
 * 
 */
public class ThemeNodeProvider extends DefaultNodeProvider {

	protected static final String NEW_STYLE_ACTION_ID = "org.eclipse.birt.report.designer.internal.ui.action.NewStyleAction"; //$NON-NLS-1$

	/**
	 * Creates the context menu for the given object.
	 * 
	 * @param object the object
	 * @param menu   the menu
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		if (canContain(object)) {
			InsertAction newStyleAction = new InsertAction(object,
					Messages.getString("StylesNodeProvider.action.New")) { //$NON-NLS-1$

				@Override
				public String getId() {
					return NEW_STYLE_ACTION_ID;
				}
			};
			menu.add(newStyleAction);
		}

		super.createContextMenu(sourceViewer, object, menu);

		if (canContain(object)) {
			menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new Separator());

			menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new ImportCSSStyleAction(object));
		}

		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new ReloadCssStyleAction(object));
		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new UseCssStyleAction(object));
	}

	public String getNodeDisplayName(Object model) {
		return ((ThemeHandle) model).getDisplayLabel();
	}

	protected DesignElementHandle createElement(ElementDetailHandle slotHandle, String type) throws Exception {
		ThemeHandle theme = (ThemeHandle) slotHandle.getElementHandle();
		DesignElementFactory factory = DesignElementFactory
				.getInstance(slotHandle.getElementHandle().getModuleHandle());
		SharedStyleHandle handle = factory.newStyle(theme, null);

		StyleBuilder builder = new StyleBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(), handle, theme,
				StyleBuilder.DLG_TITLE_NEW);
		if (builder.open() == Dialog.CANCEL) {
			return null;
		}

		return handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.views.INodeProvider#getChildren(java
	 * .lang.Object)
	 */
	public Object[] getChildren(Object model) {

		if (model instanceof ThemeHandle) {
			ThemeHandle theme = (ThemeHandle) model;
			int count, styleCount = 0;
			styleCount += (super.getChildrenBySlotHandle(((ThemeHandle) model).getStyles())).length;
			count = styleCount;
			count += theme.getAllCssStyleSheets().size();
			Object obj[] = new Object[count];

			for (int i = 0; i < styleCount; i++) {
				obj[i] = (super.getChildrenBySlotHandle(((ThemeHandle) model).getStyles()))[i];
			}

			for (int i = 0; i < theme.getAllCssStyleSheets().size(); i++) {
				obj[styleCount + i] = theme.getAllCssStyleSheets().get(i);
			}
			Arrays.sort(obj, new AlphabeticallyComparator());
			return obj;

		}
		return super.getChildren(model);
	}

	private boolean canContain(Object object) {
		if (object instanceof ThemeHandle) {
			return ((ThemeHandle) object).canContain(LibraryHandle.THEMES_SLOT, ReportDesignConstants.STYLE_ELEMENT);
		}
		return true;
	}
}