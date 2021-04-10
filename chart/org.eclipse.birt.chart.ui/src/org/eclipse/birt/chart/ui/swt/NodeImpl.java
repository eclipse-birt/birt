/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.swt.interfaces.INode;
import org.eclipse.swt.graphics.Image;

/**
 * @author Actuate Corporation
 */
public class NodeImpl implements INode {

	private HelpContentImpl help = null;

	private String sLabel = ""; //$NON-NLS-1$

	private String sPath = ""; //$NON-NLS-1$

	private Image imgIcon = null;

	public IHelpContent getHelp() {
		return help;
	}

	public String getLabel() {
		return sLabel;
	}

	public String getPath() {
		return sPath;
	}

	public Image getIcon() {
		return imgIcon;
	}

	public void setLabel(String sLabel) {
		this.sLabel = sLabel;
	}

	public void setPath(String sPath) {
		this.sPath = sPath;
	}

	public void setIcon(Image iImage) {
		this.imgIcon = iImage;
	}

	public void setHelp(HelpContentImpl help) {
		this.help = help;
	}
}