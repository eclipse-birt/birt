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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSourceModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.xml.sax.SAXException;

/**
 * Represents the state to parser the ODA converted private property. The
 * converted property is defined in ODA plugin.xml. If its visibilty is "hide",
 * it is a private property. Otherwise, it is public property. Thus, for public
 * properties in the design file, they may be converted to private driver
 * properties.
 * <p>
 * This change is made from BIRT 2.2. The design file version is "3.2.10".
 * 
 */

class CompatibleODAPrivatePropertyState extends CompatiblePropertyState {

	private IPropertyDefn privatePropDefn = null;

	/**
	 * Constructs a <code>CompatibleODAPrivatePropertyState</code> to parse an
	 * obsolete property.
	 * 
	 * @param theHandler the parser handle
	 * @param element    the element that holds the private properties
	 */

	public CompatibleODAPrivatePropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);

		String privatePropName = null;
		if (element instanceof IOdaDataSetModel) {
			privatePropName = IOdaDataSetModel.PRIVATE_DRIVER_PROPERTIES_PROP;
		} else if (element instanceof IOdaDataSourceModel) {
			privatePropName = IOdaDataSourceModel.PRIVATE_DRIVER_PROPERTIES_PROP;
		} else
			assert false;

		privatePropDefn = element.getPropertyDefn(privatePropName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */

	public void end() throws SAXException {
		ExtendedProperty tmpStruct = new ExtendedProperty();

		setMember(tmpStruct, privatePropDefn.getName(), ExtendedProperty.NAME_MEMBER, name);
		setMember(tmpStruct, privatePropDefn.getName(), ExtendedProperty.VALUE_MEMBER, text.toString());

		List privateProps = (List) element.getLocalProperty(handler.module, (ElementPropertyDefn) privatePropDefn);
		if (privateProps == null)
			privateProps = new ArrayList();

		// should first add the structure to list then set the list value so as
		// to setup the structure context

		privateProps.add(tmpStruct);

		element.setProperty((ElementPropertyDefn) privatePropDefn, privateProps);
	}
}
