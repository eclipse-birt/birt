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

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

public class DynamicTextProcessor extends AbstractElementProcessor {

	protected DynamicTextProcessor(String elementType) {
		super(elementType);
	}

	@Override
	public DesignElementHandle createElement(Object extendedData) {
		TextDataHandle handle = DesignElementFactory.getInstance().newTextData(null);
		try {
			handle.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			return null;
		}
		return handle;
	}

	@Override
	public boolean editElement(DesignElementHandle handle) {
		// do nothing.
		return false;
	}

}
