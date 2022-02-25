/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.service.api;

import java.io.ByteArrayOutputStream;

public interface IViewerReportDesignHandle {
	String RPT_DESIGN_OBJECT = "rptDesignObject";
	String RPT_DESIGN_FILE = "rptDesignFile";
	String RPT_RUNNABLE_OBJECT = "rptRunnableObject";

	/**
	 * Get the content type
	 *
	 * Two types are supported: rptDesignFile and rptDesignObject
	 *
	 * @return
	 */
	String getContentType();

	/**
	 * Get the file name
	 *
	 * @return
	 */
	String getFileName();

	/**
	 * Set the filename
	 *
	 * @param name
	 */
	void setFileName(String name);

	/**
	 * Get the design object
	 *
	 * @return
	 */
	Object getDesignObject() throws ReportServiceException;

	/**
	 * Set the design object
	 *
	 * @param obj
	 */
	void setDesignObject(Object obj);

	/**
	 * Return a stream of the design
	 *
	 * @return
	 */
	ByteArrayOutputStream getObjectStream();

	/**
	 * Get the document name this design is extracted from
	 *
	 * @return document name
	 */
	String getDocumentName();

	/**
	 * set the document name this design is extracted from
	 *
	 * @param document name
	 */
	void setDocumentName(String documentName);

}
