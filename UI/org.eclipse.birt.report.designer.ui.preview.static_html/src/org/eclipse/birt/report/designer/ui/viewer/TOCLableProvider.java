/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
