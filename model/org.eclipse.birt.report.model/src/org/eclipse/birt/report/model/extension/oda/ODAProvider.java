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

package org.eclipse.birt.report.model.extension.oda;

import java.util.List;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;

/**
 * Provider class for oda extension.
 */
public interface ODAProvider {

	/**
	 * Returns the property definitions on the ODA element.
	 * 
	 * @return all the defined property definition
	 * 
	 */
	public List<IElementPropertyDefn> getPropertyDefns();

	/**
	 * Returns the corresponding property defnition from the ODA element based on
	 * the property name.
	 * 
	 * @param propName
	 * @return the property definition if found, otherwise null
	 */
	public IPropertyDefn getPropertyDefn(String propName);

	/**
	 * Checks wether the ODA element has correct extend relation ship.
	 * 
	 * @param parent Parent element.
	 * @throws ExtendsException
	 */
	public void checkExtends(DesignElement parent) throws ExtendsException;;

	/**
	 * Returns the ODA extension element definition.
	 * 
	 * @return the extension element definition.
	 */
	public ExtensionElementDefn getExtDefn();

	/**
	 * Checks whether the extension ID can return a valid ODA dataset dataType.
	 * 
	 * @return ture if the extension ID returns the dataType is not null, otherwise,
	 *         false.
	 */
	public boolean isValidExtensionID();

	/**
	 * Converts deprecated data source or data set extension id to new one.
	 * 
	 * @return the new data source or data set extension id.
	 */

	public String convertExtensionID();
}
