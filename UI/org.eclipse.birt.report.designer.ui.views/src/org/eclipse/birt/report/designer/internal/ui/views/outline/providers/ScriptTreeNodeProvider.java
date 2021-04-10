/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import org.eclipse.birt.report.designer.core.model.views.outline.IScriptTreeNode;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptElementNode;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptObjectNode;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptsNode;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

/**
 * Node provider for the script tree node
 */
public class ScriptTreeNodeProvider implements INodeProvider {

	private static final String SCRIPS_NODE_NAME = Messages.getString("Scripts.Node.DisplayName"); //$NON-NLS-1$
	private static final Object[] EMPTY = new Object[0];
	private static final String EMPTY_STR = ""; //$NON-NLS-1$

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		if (object instanceof IMenuListener) {
			((IMenuListener) object).menuAboutToShow(menu);
		}
	}

	public Object[] getChildren(Object object) {
		if (object != null && object instanceof IScriptTreeNode) {
			return ((IScriptTreeNode) object).getChildren();
		}
		return EMPTY;
	}

	public String getNodeDisplayName(Object model) {
		if (model instanceof ScriptsNode) {
			return SCRIPS_NODE_NAME;
		} else if (model instanceof ScriptElementNode) {
			Object designElementModel = ((ScriptElementNode) model).getParent();
			return DEUtil.getFlatHirarchyPathName(designElementModel);
		}
		if (model instanceof ScriptObjectNode) {
			return ((ScriptObjectNode) model).getText();
		}
		return EMPTY_STR;
	}

	public Image getNodeIcon(Object model) {
		if (model instanceof ScriptsNode) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_SCRIPTS_NODE);
		} else if (model instanceof ScriptElementNode) {
			Object designElementModel = ((ScriptElementNode) model).getParent();
			return ProviderFactory.createProvider(designElementModel).getNodeIcon(designElementModel);
		} else if (model instanceof ScriptObjectNode) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_SCRIPTS_METHOD_NODE);
		}
		return null;
	}

	public String getNodeTooltip(Object model) {
		if (model instanceof IScriptTreeNode) {
			// return ( (ITreeNode) model ).getText( );
			getNodeDisplayName(model);
		}
		return EMPTY_STR;
	}

	public Object getParent(Object model) {
		if (model instanceof IScriptTreeNode) {
			return ((IScriptTreeNode) model).getParent();
		}
		return null;
	}

	public boolean hasChildren(Object object) {
		return getChildren(object).length > 0;
	}

	public boolean performRequest(Object model, Request request) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isReadOnly(Object model) {
		// TODO Auto-generated method stub
		return false;
	}

}
