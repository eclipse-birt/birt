
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	String getDataTypeName();

	/**
	 * Return whether the argument is optional.
	 *
	 * @return
	 */
	boolean isOptional();

}
