
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
package org.eclipse.birt.core.script.functionservice;

/**
 * The interface IScriptFunctionArgument defines the metadata/properties of an
 * argument that will be feed to a script function.
 * 
 */

public interface IScriptFunctionArgument extends INamedObject, IDescribable {
	/**
	 * Return the data type of this argument.
	 * 
	 * @return
	 */
	public String getDataTypeName();

	/**
	 * Return whether the argument is optional.
	 * 
	 * @return
	 */
	public boolean isOptional();

}
