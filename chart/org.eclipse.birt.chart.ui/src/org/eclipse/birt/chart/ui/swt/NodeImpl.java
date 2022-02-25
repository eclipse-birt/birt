/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
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

	@Override
	public IHelpContent getHelp() {
		return help;
	}

	@Override
	public String getLabel() {
		return sLabel;
	}

	@Override
	public String getPath() {
		return sPath;
	}

	@Override
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
