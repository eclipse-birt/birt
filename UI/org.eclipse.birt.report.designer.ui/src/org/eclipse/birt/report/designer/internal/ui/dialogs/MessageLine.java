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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * A message line displaying a status.
 * 
 */
public class MessageLine extends CLabel {

	private Color fNormalMsgAreaBackground;

	/**
	 * Creates a new message line as a child of the given parent.
	 */
	public MessageLine(Composite parent) {
		this(parent, SWT.LEFT);
	}

	/**
	 * Creates a new message line as a child of the parent and with the given SWT
	 * stylebits.
	 */
	public MessageLine(Composite parent, int style) {
		super(parent, style);
		fNormalMsgAreaBackground = getBackground();
	}

	private Image findImage(IStatus status) {
		if (status.isOK()) {
			return null;
		} else if (status.getCode() == IStatus.ERROR) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_STATUS_ERROR); // $NON-NLS-1$
		}
		return null;
	}

	/**
	 * Sets the message and image to the given status. <code>null</code> is a valid
	 * argument and will set the empty text and no image
	 */
	public void setErrorStatus(IStatus status) {
		if (status != null && !status.isOK()) {
			String message = status.getMessage();
			if (message != null && message.length() > 0) {
				setText(message);
				setImage(findImage(status));
				setBackground(JFaceColors.getErrorBackground(getDisplay()));
				return;
			}
		}
		setText(""); //$NON-NLS-1$
		setImage(null);
		setBackground(fNormalMsgAreaBackground);
	}
}
