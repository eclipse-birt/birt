/*
 *************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * An extended DataException that may be thrown by the ODA consumer component.
 * Its {@link #getMessage()} appends the messages of its chained OdaException
 * cause(s), if any.
 */
public class OdaDataException extends DataException {
	private static final long serialVersionUID = 8168973411276620205L;
	private static final String MSG_INDENT_NEW_LINE = "\n    "; //$NON-NLS-1$
	static final String EMPTY_STRING = ""; //$NON-NLS-1$

	public OdaDataException(String errorCode) {
		super(errorCode);
	}

	public OdaDataException(String errorCode, Object argv) {
		super(errorCode, argv);
	}

	public OdaDataException(String errorCode, Object argv[]) {
		super(errorCode, argv);
	}

	public OdaDataException(String errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public OdaDataException(String errorCode, Throwable cause, Object argv) {
		super(errorCode, cause, argv);
	}

	public OdaDataException(String errorCode, Throwable cause, Object argv[]) {
		super(errorCode, cause, argv);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.core.DataException#getMessage()
	 */
	@Override
	public String getMessage() {
		String message = null;
		if (getCause() instanceof OdaException) {
			// appends messages of the chained cause(s) to the error code message
			message = getErrorCodeMessage();
			if (message.length() > 0)
				message += MSG_INDENT_NEW_LINE;
			message += getCause().toString();
		} else
			message = super.getMessage();

		return message;
	}

	String getErrorCodeMessage() {
		String errorCode = getErrorCode();
		return (errorCode != null) ? getLocalizedMessage(errorCode) : EMPTY_STRING;
	}

}
