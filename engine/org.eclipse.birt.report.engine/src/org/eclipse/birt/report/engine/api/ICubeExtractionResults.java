/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.archive.IDocArchiveReader;

/**
 * 
 *
 */
public interface ICubeExtractionResults extends IExtractionResults {

	/**
	 * get the archive reader of the report document.
	 * 
	 * @return
	 */
	public IDocArchiveReader getReportDocReader();

}
