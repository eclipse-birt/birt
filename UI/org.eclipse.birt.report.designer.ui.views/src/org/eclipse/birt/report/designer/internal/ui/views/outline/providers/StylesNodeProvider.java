/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
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

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportCSSStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ReloadCssStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.UseCssStyleAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.dialogs.StyleBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with the styles node
 *
 *
 */
public class StylesNodeProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object.
	 *
	 * @param menu   the menu
	 * @param object the object
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		menu.add(new InsertAction(object, Messages.getString("StylesNodeProvider.action.New"))); //$NON-NLS-1$
		super.createContextMenu(sourceViewer, object, menu);

		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new Separator());

		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new ImportCSSStyleAction(object)); // $NON-NLS-1$

		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new ReloadCssStyleAction(object));
		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new UseCssStyleAction(object));

	}

	/**
	 * Gets the node display name of the given object.
	 *
	 * @param object the object
	 * @return the display name
	 */
	@Override
	public String getNodeDisplayName(Object object) {
		return STYLES;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName(
	 * java.lang.Object)
	 */
	@Override
	public String getIconName(Object model) {
		return IReportGraphicConstants.ICON_NODE_STYLES;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * createElement(java.lang.String)
	 */
	@Override
	protected DesignElementHandle createElement(String type) throws Exception {
		// ElementFactory factory = SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( )
		// .getElementFactory( );
		DesignElementFactory factory = DesignElementFactory.getInstance();
		if (ReportDesignConstants.STYLE_ELEMENT.equals(type)) {
			StyleHandle handle = factory.newStyle(null);
			StyleBuilder builder = new StyleBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(), handle,
					StyleBuilder.DLG_TITLE_NEW);
			if (builder.open() == Dialog.CANCEL) {
				return null;
			}
			return handle;
		}
		return super.createElement(type);
	}

	@Override
	public Object[] getChildren(Object model) {
		ModuleHandle moduleHandle = ((SlotHandle) model).getElementHandle().getModuleHandle();
		Object[] styles = moduleHandle.getStyles().getContents().toArray();
		Arrays.sort(styles, new AlphabeticallyComparator());

		// StylesNodeProvider should be fit for ReportDesignHandle
		assert (moduleHandle instanceof ReportDesignHandle);
		List cssList = new ArrayList();
		for (Iterator iter = ((ReportDesignHandle) moduleHandle).getAllCssStyleSheets().iterator(); iter.hasNext();) {
			CssStyleSheetHandle cssStyleHandle = (CssStyleSheetHandle) iter.next();
			cssList.add(cssStyleHandle);
		}
		Object[] csses = cssList.toArray(new Object[cssList.size()]);
		Object[] stylesAndCsses = new Object[styles.length + csses.length];
		for (int i = 0; i < styles.length; i++) {
			stylesAndCsses[i] = styles[i];
		}
		for (int i = 0; i < csses.length; i++) {
			stylesAndCsses[i + styles.length] = csses[i];
		}
		return stylesAndCsses;

	}
}
