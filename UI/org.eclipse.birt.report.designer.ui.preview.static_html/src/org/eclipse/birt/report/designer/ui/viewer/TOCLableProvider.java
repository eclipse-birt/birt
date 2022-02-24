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

package org.eclipse.birt.report.designer.ui.viewer;

import org.eclipse.birt.report.designer.ui.preview.static_html.StaticHTMLPrviewPlugin;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class TOCLableProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof TOCNode) {
			return ((TOCNode) element).getDisplayString();
		}
		return super.getText(element);
	}

	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return StaticHTMLPrviewPlugin.getDefault().getImageRegistry().get(StaticHTMLPrviewPlugin.IMG_TOC_LEAF);
	}

}
