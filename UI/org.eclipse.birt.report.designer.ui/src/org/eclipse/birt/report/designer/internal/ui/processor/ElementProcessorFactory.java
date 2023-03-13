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

package org.eclipse.birt.report.designer.internal.ui.processor;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * The factory used to create IElementProcessor instances
 */

public class ElementProcessorFactory {

	public static IElementProcessor createProcessor(String elementType) {
		if (ReportDesignConstants.IMAGE_ITEM.equals(elementType)) {
			return new ImageItemProcessor();
		}
		if (ReportDesignConstants.GRID_ITEM.equals(elementType)
				|| ReportDesignConstants.TABLE_ITEM.equals(elementType)) {
			return new TableGridProcessor(elementType);
		}
		if (ReportDesignConstants.TEXT_DATA_ITEM.equals(elementType)) {
			return new DynamicTextProcessor(elementType);
		}
		if (DEUtil.getMetaDataDictionary().getExtension(elementType) != null) {
			return new ExtenedElementProcessor(elementType);
		}
		return new DefaultElementProcessor(elementType);
	}

	public static IElementProcessor createProcessor(DesignElementHandle handle) {
		return createProcessor(handle.getDefn().getName());
	}

}
