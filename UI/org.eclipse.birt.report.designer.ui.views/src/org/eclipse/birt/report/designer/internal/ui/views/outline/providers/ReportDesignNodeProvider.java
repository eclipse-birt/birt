/*************************************************************************************
 * Copyright (c) 2004 2007 Actuate Corporation and others.
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

import java.io.File;
import java.util.ArrayList;

import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.core.model.views.outline.LibraryNode;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptsNode;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ExportToLibraryAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PublishTemplateViewAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshModuleHandleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ReloadCssStyleAction;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Root node - Report design node provider - Implements the getChildren -
 * Implements the getNodeDiplayName
 *
 *
 */
public class ReportDesignNodeProvider extends DefaultNodeProvider {

	/**
	 * Gets the children of the given model. The default children element include
	 * following: Body,Styles,MasterPage
	 *
	 * @param model the given report design
	 * @return the result list that contains the model
	 */
	@Override
	public Object[] getChildren(Object model) {

		// Report design may not be the current, use model to get.
		ReportDesignHandle handle = ((ReportDesignHandle) model);
		ArrayList list = new ArrayList();

		list.add(handle.getParameters());

		list.add(((ReportDesignHandle) handle).getPropertyHandle(IReportDesignModel.PAGE_VARIABLES_PROP));

		// Add the children handle - Body
		list.add(handle.getBody());
		// Add the children handle - Master Pages
		list.add(handle.getMasterPages());
		// Add the children handle - Styles

		SlotHandle themeSlot = handle.getSlot(ReportDesignHandle.THEMES_SLOT);
		if (themeSlot != null) {
			Object[] adapter = ElementAdapterManager.getAdapters(themeSlot, INodeProvider.class);
			if (adapter != null && adapter.length > 0) {
				list.add(themeSlot);
			}
		}

		list.add(handle.getStyles());
		// Add the children handle - Embedded Images
		list.add(new EmbeddedImageNode(handle));

		// list.add( new ReportElementModel( ) );
		// if ( handle.getTheme( ) != null )
		// {
		// list.add( handle.getTheme( ) );
		// }

		list.add(new LibraryNode(handle));

		list.add(new ScriptsNode(handle));

		return list.toArray();
	}

	/**
	 * Gets the node display name of the given model
	 *
	 * @param model the model
	 * @return the display name
	 */
	@Override
	public String getNodeDisplayName(Object model) {
		ModuleHandle handle = (ModuleHandle) model;
		Object obj = handle.getProperty(ModuleHandle.TITLE_PROP);
		if (obj instanceof String) {
			return (String) obj;
		} else if (handle.getFileName() != null) {
			return handle.getFileName().substring(handle.getFileName().lastIndexOf(File.separator) + 1);
		}
		return super.getNodeDisplayName(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeTooltip(java.lang.Object)
	 */
	@Override
	public String getNodeTooltip(Object model) {
		return ((ModuleHandle) model).getFileName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName
	 * (java.lang.Object)
	 */
	@Override
	public String getIconName(Object model) {
		if (model instanceof ReportDesignHandle && ((ReportDesignHandle) model).isEnableACL()) {
			return IReportGraphicConstants.ICON_REPORT_LOCK;
		}
		return IReportGraphicConstants.ICON_REPORT_FILE;
	}

	/**
	 * Creates the context menu for the given object.
	 *
	 * @param object the object
	 * @param menu   the menu
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		super.createContextMenu(sourceViewer, object, menu);
		menu.add(new ReloadCssStyleAction(object));
		menu.add(new RefreshModuleHandleAction(object));
		menu.add(new ExportToLibraryAction(object));
		ReportDesignHandle report = (ReportDesignHandle) object;
		if (report.getModuleHandle().getFileName().endsWith(".rpttemplate") //$NON-NLS-1$
				|| ReportPlugin.getDefault().isReportDesignFile(report.getModuleHandle().getFileName())) {
			menu.add(new PublishTemplateViewAction(object));
		}

	}
}
