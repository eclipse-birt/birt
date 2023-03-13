/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

public interface IDocumentWriter {

	/**
	 * set a report runnable which can be used to update a report document
	 *
	 * @param runnable a report runnable
	 */
	void setRunnable(IReportRunnable runnable) throws EngineException;

	/**
	 * close this document writer; this method might trigger updates of the report
	 * document
	 */
	void close();
}
