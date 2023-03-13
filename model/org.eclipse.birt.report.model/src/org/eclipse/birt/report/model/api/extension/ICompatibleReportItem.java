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

package org.eclipse.birt.report.model.api.extension;

import java.util.List;
import java.util.Map;

/**
 * Represents an instance of a extended report element that can work with the
 * bound data columns.
 * <p>
 * ICompatibleReportItem is responsible for providing backward compatibility for
 * the extended item. Parts of migration work from BIRT 2.1M5 to BIRT 2.1.0 for
 * bound data columns.
 */

public interface ICompatibleReportItem {

	/**
	 * Returns a list containing the possible JavaScript expressions. During parsing
	 * the design file, this method is automatically called to add bound data
	 * columns so that the design file before BIRT 2.1.0 can be compatible with BIRT
	 * 2.1.0 or later.
	 *
	 * @return a list containing the possible expressions.
	 */

	List getRowExpressions();

	/**
	 * Updates existed expression with the given expressions. The keys in
	 * <code>newExpressions</code> are existed expressions, while, the values are
	 * the new expressions to replace existed ones.
	 *
	 * @param newExpressions a map containing the updated expressions.
	 */

	void updateRowExpressions(Map newExpressions);

	/**
	 * Checks the parser compatibilities for this report item and return the status.
	 *
	 * @return the compatibility status.
	 */
	CompatibilityStatus checkCompatibility();

	/**
	 * handle the compatibility issue
	 */
	void handleCompatibilityIssue();

}
