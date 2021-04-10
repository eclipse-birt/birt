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
	public IReportRunnable getReportRunnable();

	/**
	 * returns the rendering options
	 * 
	 * @return the rendering options
	 */
	public IRenderOption getRenderOption();

}
