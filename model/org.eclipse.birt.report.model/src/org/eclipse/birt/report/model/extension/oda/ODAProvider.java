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
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;

public interface ODAProvider
{

	/**
	 * Returns the property definitions on the ODA element.
	 * 
	 */
	public List getPropertyDefns( );

	/**
	 * Returns the corresponding property defnition from the ODA element based
	 * on the property name.
	 */
	public IPropertyDefn getPropertyDefn( String propName );

	/**
	 * Checks wether the ODA element has correct extend relation ship.
	 * 
	 * @param parent
	 *            Parent element.
	 * @throws ExtendsException
	 */
	public void checkExtends( DesignElement parent ) throws ExtendsException;;

	/**
	 * Returns the ODA extension element definition.
	 * 
	 * @return the extension element definition.
	 */
	public ExtensionElementDefn getExtDefn( );

	/**
	 * Checks whether the extension ID can return a valid ODA datasource
	 * dataType.
	 * 
	 * @param extensionID
	 *            the extension ID of the element.
	 * @return ture if the extension ID returns the dataType is not null,
	 *         otherwise, false.
	 */
	public boolean isValidODADataSourceExtensionID( String extensionID );

	/**
	 * Checks whether the extension ID can return a valid ODA dataset dataType.
	 * 
	 * @param extensionID
	 *            the extension ID of the element.
	 * @return ture if the extension ID returns the dataType is not null,
	 *         otherwise, false.
	 */
	public boolean isValidODADataSetExtensionID( String extensionID );
	
	/**
	 * Converts deprecated data source extension id to new one.
	 * 
	 * @param extensionID
	 *            the data source extension id.
	 * @return the new data source extension id.
	 */

	public String convertDataSourceExtensionID( String extensionID );

	/**
	 * Converts deprecated data set extension id to new one.
	 * 
	 * @param extensionID
	 *            the data set extension id.
	 * @return the new data set extension id.
	 */

	public String convertDataSetExtensionID( String extensionID );

}
