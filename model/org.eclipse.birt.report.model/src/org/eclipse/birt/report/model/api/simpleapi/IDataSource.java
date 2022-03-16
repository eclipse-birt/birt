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
package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Script wrapper of OdaDataSourceHandle
 *
 */
public interface IDataSource {
	/**
	 * Returns ID of the extension which extends this ODA data source.
	 *
	 * @return the extension ID
	 */

	String getExtensionID();

	/**
	 * Returns a private driver property value with the given property name.
	 *
	 * @param name the name of a public driver property
	 *
	 * @return a public driver property value
	 */

	String getPrivateDriverProperty(String name);

	/**
	 * Sets a private driver property value with the given name and value. If the
	 * property does not exist, it will be added into the property list. If the
	 * property already exists, the value will be overwritten.
	 *
	 * @param name  the name of a public driver property
	 * @param value the value of a public driver property
	 *
	 * @throws SemanticException if <code>name</code> is <code>null</code> or an
	 *                           empty
	 *
	 */

	void setPrivateDriverProperty(String name, String value) throws SemanticException;
}
