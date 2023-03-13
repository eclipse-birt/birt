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

package org.eclipse.birt.report.engine.api;

/**
 * Base interface that provides accessible information for a report part
 */
public interface IReportPart {
	/**
	 * returns the runnable report design
	 *
	 * @return the runnable report design
	 */
	IReportRunnable getReportRunnable();

	/**
	 * returns the rendering options
	 *
	 * @return the rendering options
	 */
	IRenderOption getRenderOption();

}
