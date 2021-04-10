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

package org.eclipse.birt.report.model.api.metadata;

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.MetaDataParserException;

/**
 * Exception thrown if an error occurs when reading the meta-data description
 * file or the building the meta-data description.
 */

public class MetaDataReaderException extends ModelException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = -6052803773978695357L;

	private MetaDataParserException e = null;

	/**
	 * Indicates there are ROM errors.
	 */

	public final static String DESIGN_EXCEPTION_META_DATA_ERROR = MessageConstants.META_DATA_READER_EXCEPTION_META_DATA_ERROR;

	/**
	 * Constructs the meta-data reader exception with the error code and the caused
	 * parser exception.
	 * 
	 * @param errCode used to retrieve a piece of externalized message displayed to
	 *                end user
	 * @param cause   the nested exception
	 */
	public MetaDataReaderException(String errCode, MetaDataParserException cause) {
		super(PLUGIN_ID, errCode, null, null, cause);
		assert cause != null;
		this.e = cause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		return e.getLocalizedMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */

	public String getMessage() {
		return getLocalizedMessage();
	}
}