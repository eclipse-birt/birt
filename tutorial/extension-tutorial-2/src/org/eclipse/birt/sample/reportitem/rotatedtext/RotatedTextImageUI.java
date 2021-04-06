/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.sample.reportitem.rotatedtext;

import org.eclipse.birt.report.designer.ui.extensions.IReportItemImageProvider;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.sample.reportitem.rotatedtext.util.SwtGraphicsUtil;
import org.eclipse.swt.graphics.Image;

/**
 * RotatedTextImageUI
 */
public class RotatedTextImageUI implements IReportItemImageProvider {

	public void disposeImage(ExtendedItemHandle handle, Image image) {
		if (image != null && !image.isDisposed()) {
			image.dispose();
		}

	}

	public Image getImage(ExtendedItemHandle handle) {
		try {
			IReportItem item = handle.getReportItem();

			if (item instanceof RotatedTextItem) {
				int angle = ((RotatedTextItem) item).getRotationAngle();
				String text = ((RotatedTextItem) item).getText();

				return SwtGraphicsUtil.createRotatedTextImage(text, angle, null);
			}
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}
		return null;
	}

}
