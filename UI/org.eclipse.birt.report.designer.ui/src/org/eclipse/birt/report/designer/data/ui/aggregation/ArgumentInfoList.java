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

package org.eclipse.birt.report.designer.data.ui.aggregation;

import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;

/**
 * Represents an optional argument list of a method.
 * 
 */

public class ArgumentInfoList extends org.eclipse.birt.report.model.api.metadata.ArgumentInfoList {

	/**
	 * Adds argument to this method definition. Hold argumentNameConflict exception
	 * 
	 * @param argument the argument definition to add
	 */

	protected void addArgument(IArgumentInfo argument) {
		super.addArgument(argument);
	}
}