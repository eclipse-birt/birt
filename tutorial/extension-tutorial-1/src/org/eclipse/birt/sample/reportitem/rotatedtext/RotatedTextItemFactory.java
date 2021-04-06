/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.sample.reportitem.rotatedtext;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItemFactory;

/**
 * RotatedTextItemFactory
 */
public class RotatedTextItemFactory extends ReportItemFactory {

	public IReportItem newReportItem(DesignElementHandle modelHanlde) {
		if (modelHanlde instanceof ExtendedItemHandle
				&& RotatedTextItem.EXTENSION_NAME.equals(((ExtendedItemHandle) modelHanlde).getExtensionName())) {
			return new RotatedTextItem((ExtendedItemHandle) modelHanlde);
		}
		return null;
	}

	public IMessages getMessages() {
		return null;
	}

}
