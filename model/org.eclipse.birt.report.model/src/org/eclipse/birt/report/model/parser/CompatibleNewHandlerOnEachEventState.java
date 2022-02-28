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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.xml.sax.SAXException;

/**
 * This state is for handling the compatibility problem for
 * newHandlerOnEachEvent property. When the report version is less than 3.2.18
 * and the eventHandlerClass has been set, the newHandlerOnEachEvent will be set
 * as true.
 *
 *
 */
public class CompatibleNewHandlerOnEachEventState extends CompatiblePropertyState {

	/**
	 * Constructor
	 *
	 * @param theHandler the parser handle
	 * @param element    the element that holds the obsolete property
	 */
	public CompatibleNewHandlerOnEachEventState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */
	@Override
	public void end() throws SAXException {
		// When the report version is less than 3.2.18 , the eventHandlerClass
		// property value is not empty string and the newHandlerOnEachEvent
		// property value is not be set, the newHandlerOnEachEvent will be set
		// as true.
		Object value = element.getLocalProperty(element.getRoot(), IDesignElementModel.NEW_HANDLER_ON_EACH_EVENT_PROP);
		String txtValue = text.toString();
		if (!StringUtil.isBlank(txtValue) && value == null) {
			setProperty(IDesignElementModel.NEW_HANDLER_ON_EACH_EVENT_PROP, Boolean.TRUE.toString());
		}

		doEnd(txtValue);
	}
}
