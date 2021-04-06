/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api;

import java.util.Map;

/**
 * Describes the static design of an ODA (Open Data Access) Data Source. The
 * data source is to be accessed via an underlying data access driver, whose
 * name and properties are defined in this specialized interface.
 */
public interface IOdaDataSourceDesign extends IBaseDataSourceDesign {
	/**
	 * Gets the unique id of this data source type, assigned by the extension
	 * providing the implementation of this ODA data source. An extension id is
	 * required in a data source design.
	 * 
	 * @return The data source extension id
	 */
	public abstract String getExtensionID();

	/**
	 * Gets the public connection property, in the form of a (name, value) pair. The
	 * property name is of String type. The property value is of string values.
	 * 
	 * @return Public properties as a Map of name-set pairs. Null if none is
	 *         defined.
	 */
	public abstract Map getPublicProperties();

	/**
	 * Gets the private connection property, in the form of a (name, setValue) pair.
	 * A named property can be mapped to more than one values. The property name is
	 * of String type. The property value is a Set interface of string values.
	 * 
	 * @return Private properties as a Map of name-set pairs. Null if none is
	 *         defined.
	 */
	public abstract Map getPrivateProperties();

}
