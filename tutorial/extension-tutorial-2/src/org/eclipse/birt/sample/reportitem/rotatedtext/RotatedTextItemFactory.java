/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
