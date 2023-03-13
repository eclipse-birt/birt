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

import java.util.logging.Level;

/**
 * Internal class for common exception handling behavior shared by the
 * odaconsumer package implementation. This may also be used as a base class of
 * the odaconsumer classes to throw and log exceptions.
 */
public class ExceptionHandler {
	protected static final String sm_packageName = "org.eclipse.birt.data.engine.odaconsumer"; //$NON-NLS-1$
	protected static final String EMPTY_STRING = OdaDataException.EMPTY_STRING;

	// trace logging variables
	protected static final String sm_loggerName = sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance(sm_loggerName);

	private String m_loggingClassName;

	ExceptionHandler(String loggingClassName) {
		m_loggingClassName = loggingClassName;
	}

	static OdaDataException newException(String errorCode, Object errMsgArgv) {
		if (errMsgArgv == null) {
			return new OdaDataException(errorCode);
		}
		if (errMsgArgv.getClass().isArray()) {
			return new OdaDataException(errorCode, (Object[]) errMsgArgv);
		}
		return new OdaDataException(errorCode, errMsgArgv);
	}

	static OdaDataException newException(String errorCode, Object errMsgArgv, Throwable cause) {
		if (errMsgArgv == null) {
			return new OdaDataException(errorCode, cause);
		}
		if (errMsgArgv.getClass().isArray()) {
			return new OdaDataException(errorCode, cause, (Object[]) errMsgArgv);
		}
		return new OdaDataException(errorCode, cause, errMsgArgv);
	}

	static OdaDataException newException(String errorCode, Throwable cause) {
		return newException(errorCode, null, cause);
	}

	protected static LogHelper getLogger() {
		if (sm_logger == null) {
			synchronized (ExceptionHandler.class) {
				if (sm_logger == null) {
					sm_logger = LogHelper.getInstance(sm_loggerName);
				}
			}
		}

		return sm_logger;
	}

	/*
	 * Throws exception with cause, and an error message with no message argument.
	 * Also logs a Severe-level message.
	 */
	protected void throwException(Throwable cause, final String errorCode, final String methodName)
			throws OdaDataException {
		throwException(cause, errorCode, null, methodName);
	}

	/*
	 * Throws exception with cause, and an error message with an index message
	 * argument. Also logs a Severe-level message.
	 */
	protected void throwException(Throwable cause, final String errorCode, int index, final String methodName)
			throws OdaDataException {
		Object errMsgArgv = Integer.valueOf(index);
		throwException(cause, errorCode, errMsgArgv, methodName);
	}

	/*
	 * Throws exception with cause, and an error message with argument(s);
	 * errMsgArgv may be null, an Object, or an array of Object. Also logs a
	 * Severe-level message.
	 */
	protected void throwException(Throwable cause, final String errorCode, Object errMsgArgv, final String methodName)
			throws OdaDataException {
		OdaDataException dataEx = newException(errorCode, errMsgArgv, cause);
		sm_logger.logp(Level.SEVERE, m_loggingClassName, methodName, dataEx.getErrorCodeMessage(), cause);
		throw dataEx;
	}

	/*
	 * Throws exception with UnsupportedOperationException cause, and an error
	 * message with no message argument. Also logs a Warning-level message.
	 */
	protected void throwUnsupportedException(final String errorCode, final String methodName) throws OdaDataException {
		throwUnsupportedException(errorCode, null, methodName);
	}

	/*
	 * Throws exception with UnsupportedOperationException cause, and an error
	 * message with argument(s); errMsgArgv may be null, an Object, or an array of
	 * Object. Also logs a Warning-level message.
	 */
	protected void throwUnsupportedException(final String errorCode, Object errMsgArgv, final String methodName)
			throws OdaDataException {
		Throwable cause = new UnsupportedOperationException();
		OdaDataException dataEx = newException(errorCode, errMsgArgv, cause);
		sm_logger.logp(Level.WARNING, m_loggingClassName, methodName, dataEx.getErrorCodeMessage(), cause);
		throw dataEx;
	}

	/*
	 * Throws exception with no known cause, and an error message with argument(s);
	 * errMsgArgv may be null, an Object, or an array of Object. Also logs a
	 * Severe-level message.
	 */
	protected void throwError(final String errorCode, Object errMsgArgv, final String methodName)
			throws OdaDataException {
		OdaDataException dataEx = newException(errorCode, errMsgArgv);
		sm_logger.logp(Level.SEVERE, m_loggingClassName, methodName, dataEx.getErrorCodeMessage());
		throw dataEx;
	}

}
