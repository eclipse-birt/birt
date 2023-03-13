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

package org.eclipse.birt.report.model.api;

/**
 * Applies logic customized to each type of report element. This is an
 * implementation of the classic visitor pattern. The application creates a
 * derived iterator, and overrides methods for the elements of interest. The
 * visitor can also walk the design tree, performing actions on each element
 * down the containment hierarchy.
 * <p>
 * By default, each method calls the method for its parent element. That is, a
 * DataItem method calls the ReportItem method which calls the DesignElement
 * method.
 */

public class DesignVisitor extends DesignVisitorImpl {
	/**
	 * Constructs a <code>DesignVisitor</code>, which is not related with the
	 * specific report.
	 */

	public DesignVisitor() {
		super();
	}

}
