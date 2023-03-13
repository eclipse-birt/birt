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

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents a master page in the scripting environment
 */

public interface IMasterPage extends IReportElement {
	/**
	 * Returns page size type.
	 *
	 * @return page size type
	 */

	String getPageType();

	/**
	 * Sets page size type.
	 *
	 * @param pageType page size type
	 * @throws SemanticException
	 */

	void setPageType(String pageType) throws SemanticException;
}
