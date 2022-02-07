/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.property;

import java.util.LinkedList;

import org.eclipse.birt.report.designer.internal.ui.views.memento.MementoElement;
import org.eclipse.birt.report.designer.internal.ui.views.memento.Memento;
import org.eclipse.birt.report.model.api.DesignElementHandle;

public class PropertyMementoUtil {

	public static boolean addNode(Memento element, MementoElement[] nodePath) {
		if (nodePath != null && nodePath.length > 0) {
			MementoElement memento = element.getMementoElement();
			if (!memento.equals(nodePath[0]))
				return false;
			for (int i = 1; i < nodePath.length; i++) {
				MementoElement child = getChild(memento, nodePath[i]);
				if (child != null)
					memento = child;
				else {
					memento.addChild(nodePath[i]);
					return true;
				}
			}
			return true;
		}
		return false;
	}

	public static boolean removeNode(Memento element, MementoElement[] nodePath) {
		if (nodePath != null && nodePath.length > 0) {
			MementoElement memento = element.getMementoElement();
			if (!memento.equals(nodePath[0]))
				return false;
			for (int i = 1; i < nodePath.length; i++) {
				MementoElement child = getChild(memento, nodePath[i]);
				if (child != null)
					memento = child;
				else
					return false;
			}
			memento.getParent().removeChild(memento);
			return true;
		}
		return false;
	}

	private static MementoElement getChild(MementoElement parent, MementoElement key) {
		MementoElement[] children = parent.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i].equals(key))
				return children[i];
		}
		return null;
	};

	public static MementoElement[] getNodePath(MementoElement node) {
		LinkedList pathList = new LinkedList();
		MementoElement memento = node;
		pathList.add(node);// add root
		while (memento.getChildren().length > 0) {
			pathList.add(memento.getChild(0));
			memento = memento.getChild(0);
		}
		MementoElement[] paths = new MementoElement[pathList.size()];
		pathList.toArray(paths);
		return paths;
	}

	public static String getElementType(DesignElementHandle handle) {
		String displayName = handle.getDefn().getDisplayName();

		if (displayName == null || "".equals(displayName))//$NON-NLS-1$
		{
			displayName = handle.getDefn().getName();
		}

		return displayName;
	}
}
