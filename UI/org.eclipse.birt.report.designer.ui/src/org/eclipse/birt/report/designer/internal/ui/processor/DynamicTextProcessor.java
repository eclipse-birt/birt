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

	public boolean editElement(DesignElementHandle handle) {
		// do nothing.
		return false;
	}

}
