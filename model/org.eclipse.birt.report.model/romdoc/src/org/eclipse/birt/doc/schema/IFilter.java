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

package org.eclipse.birt.doc.schema;

import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;

/**
 * Style property filter
 *
 */
public interface IFilter {
	/**
	 * filter style property
	 * 
	 * @param propDefn
	 * @return true is allowed , else return false
	 */

	public boolean filter(IPropertyDefn propDefn);
}
