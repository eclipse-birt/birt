/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public void setRunnable(IReportRunnable runnable) throws EngineException;

	/**
	 * close this document writer; this method might trigger updates of the report
	 * document
	 */
	public void close();
}
