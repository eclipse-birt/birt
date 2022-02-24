/*
 *************************************************************************
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
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api.querydefn;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.api.IOdaDataSourceDesign} interface.
 * <p>
 */
public class OdaDataSourceDesign extends BaseDataSourceDesign implements IOdaDataSourceDesign {
	private String extensionID;
	private Map publicProps;
	private Map privateProps;

	/**
	 * Constructs a data source with the given name
	 */
	public OdaDataSourceDesign(String name) {
		super(name);
	}

	/**
	 * Gets the data source extension id as defined by ODA driver This property is
	 * required in a data source design.
	 * 
	 * @return The data source extension id
	 */
	public String getExtensionID() {
		return extensionID;
	}

	/**
	 * Sets the data source extension id as defined by ODA driver
	 * 
	 * @param id The data source extension id
	 */
	public void setExtensionID(String id) {
		extensionID = id;
	}

	/**
	 * Gets the public properties for the data source.
	 * 
	 * @return public properties as a map. Null if no public property is defined for
	 *         the data source
	 */
	public Map getPublicProperties() {
		if (publicProps == null)
			publicProps = new HashMap();
		return publicProps;
	}

	/**
	 * Gets the private properties for the data source.
	 * 
	 * @return private properties as a map. Null if no public property is defined
	 *         for the data source
	 */
	public Map getPrivateProperties() {
		if (privateProps == null)
			privateProps = new HashMap();
		return privateProps;
	}

	/**
	 * Adds a public connection property, in the form of a (Name, value) string
	 * pair. Each data source property name must be unique within the data source,
	 * and has a single value.
	 */
	public void addPublicProperty(String name, String value) throws BirtException {
		addProperty(getPublicProperties(), name, value);
	}

	/**
	 * Adds a private connection property, in the form of a (Name, value) string
	 * pair. Each data source property name must be unique within the data source,
	 * and has a single value.
	 */
	public void addPrivateProperty(String name, String value) throws BirtException {
		addProperty(getPrivateProperties(), name, value);
	}

	/**
	 * Adds given value to the named property in the given properties map. Each
	 * named property must be unique.
	 */
	protected void addProperty(Map properties, String name, String value) throws BirtException {
		if (properties.containsKey(name))
			throw new DataException(ResourceConstants.DUPLICATE_PROPERTY_NAME, name);

		// since the ultimate destination used is a java.util.Properties,
		// a null property value is not accepted
		if (value != null)
			properties.put(name, value);
	}

}
