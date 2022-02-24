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

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.CssSharedStyleHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class CssSharedStyleNodeProvider extends StyleNodeProvider {

	/**
	 * Creates the context menu for the given object.
	 * 
	 * @param object the object
	 * @param menu   the menu
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	protected boolean performEdit(ReportElementHandle handle) {
		return false;
	}

	public Image getNodeIcon(Object model) {
		CssSharedStyleHandle handle = (CssSharedStyleHandle) model;
		if (handle.getCssStyleSheetHandle() != null && handle.getCssStyleSheetHandle().getContainerHandle() != null) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_STYLE_LINK);
		}
		return super.getNodeIcon(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeTooltip(java.lang.Object)
	 */
	public String getNodeTooltip(Object model) {

		CssSharedStyleHandle handle = (CssSharedStyleHandle) model;
		CssStyleSheetHandle css = handle.getCssStyleSheetHandle();

		DesignElementHandle container = css.getContainerHandle();
		if (container == null) {
			return handle.getName();
		}
		String cssURITooltip = Messages.getString("CssStyleSheetNodeProvider.Tooltip.URI");

		String cssURI = null;
		IncludedCssStyleSheetHandle includedCssStyleSheet = null;
		CssStyleSheetHandle uriCss = null;
		if (container instanceof ReportDesignHandle) {
			includedCssStyleSheet = ((ReportDesignHandle) container)
					.findIncludedCssStyleSheetHandleByFileName(css.getFileName());

		} else if (container instanceof ThemeHandle) {
			includedCssStyleSheet = ((ThemeHandle) container).findIncludedCssStyleSheetHandleByName(css.getFileName());
		}

		if (includedCssStyleSheet == null || includedCssStyleSheet.getExternalCssURI() == null
				|| includedCssStyleSheet.getExternalCssURI().length() == 0) {
			return handle.getName();
		}

		try {
			uriCss = SessionHandleAdapter.getInstance().getReportDesignHandle()
					.openCssStyleSheet(includedCssStyleSheet.getExternalCssURI());
			if (uriCss == null) {
				return handle.getName();
			}

		} catch (StyleSheetException e) {
			// TODO Auto-generated catch block
			if (uriCss == null) {
				return handle.getName();
			}
		}

		if (uriCss != null && uriCss.findStyle(handle.getName()) != null) {
			return handle.getName() + " " + cssURITooltip;
		} else {
			return handle.getName();
		}

	}

}
