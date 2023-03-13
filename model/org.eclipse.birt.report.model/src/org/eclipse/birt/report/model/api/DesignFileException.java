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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Signals that a fatal error occurred when opening a design file. It includes
 * the error list. Each item in the list is an instance of
 * <code>ErrorDetail</code>.
 * <p>
 * Four types of error are defined:
 * <dl>
 * <dd>Design file is not found. <dn><code>INVALID_XML</code> </dn>
 * <dd>Design file is not a valid xml file. <dn><code>SYNTAX_ERROR</code> </dn>
 * <dd>Design file has something conflicting MetaData definition. <dn>
 * <code>SEMANTIC_ERROR</code> </dn>
 * <dd>Design file is opened with semantic error.
 * <ul>
 * </ul>
 *
 * @see ErrorDetail
 */

public class DesignFileException extends ModelException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 8790341685615483274L;

	/**
	 * The list containing errors encountered when opening the design file.
	 */

	private List<Exception> exceptionList = new ArrayList<>();

	/**
	 * The file name with the error.
	 */

	protected String fileName = null;

	/**
	 * Exception thrown by SAX.
	 */

	protected Exception e = null;

	/**
	 * The syntax error, when design file doesn't conform metadata definition.
	 */

	public static final String DESIGN_EXCEPTION_SYNTAX_ERROR = MessageConstants.DESIGN_FILE_EXCEPTION_SYNTAX_ERROR;

	/**
	 * The semantic error, when element doesn't conform semantic check.
	 */

	public static final String DESIGN_EXCEPTION_SEMANTIC_ERROR = MessageConstants.DESIGN_FILE_EXCEPTION_SEMANTIC_ERROR;

	/**
	 * The semantic warning, when element doesn't conform semantic check. However,
	 * the level of this error is warning.
	 */

	public static final String DESIGN_EXCEPTION_SEMANTIC_WARNING = MessageConstants.DESIGN_FILE_EXCEPTION_SEMANTIC_WARNING;

	/**
	 * Other exceptions thrown by SAX. Generally, it's caused when design file is
	 * not a valid xml file.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_XML = MessageConstants.DESIGN_FILE_EXCEPTION_INVALID_XML;

	/**
	 * Constructs a <code>DesignFileException</code> with the given design filename
	 * and the specified cause. It is for the exception thrown by SAX.
	 *
	 * @param fileName design file name.
	 * @param e        exception to wrap.
	 */

	public DesignFileException(String fileName, Exception e) {
		super(DESIGN_EXCEPTION_INVALID_XML, null, e);
		this.fileName = fileName;
		this.e = e;
		exceptionList.add(e);
	}

	/**
	 * Constructs a <code>DesignFileException</code> with the given design filename
	 * and a list of errors. Used when syntax error is found when parsing.
	 *
	 * @param fileName design file name.
	 * @param errList  exception list, each of them is the syntax error.
	 */

	public DesignFileException(String fileName, List<? extends Exception> errList) {
		super(DESIGN_EXCEPTION_SYNTAX_ERROR);
		this.fileName = fileName;

		exceptionList.addAll(errList);
	}

	/**
	 * Constructs a <code>DesignFileException</code> with the given design filename,
	 * a list of errors and the new exception to add. Used when syntax error is
	 * found when parsing.
	 *
	 *
	 * @param fileName design file name.
	 * @param errList  exception list, each of which is the syntax error.
	 * @param ex       the exception to add
	 *
	 */

	public DesignFileException(String fileName, List<? extends Exception> errList, Exception ex) {
		super(DESIGN_EXCEPTION_INVALID_XML, null, ex);
		this.fileName = fileName;

		exceptionList.addAll(errList);
		exceptionList.add(ex);
	}

	/**
	 * Returns the error list. Each item in the list is an instance of <code>
	 * ErrorDetail</code>.
	 *
	 * @return the error list.
	 */

	public List<ErrorDetail> getErrorList() {
		List<ErrorDetail> errorList = new ArrayList<>();
		Iterator<Exception> iter = exceptionList.iterator();
		while (iter.hasNext()) {
			Exception e = iter.next();

			errorList.add(new ErrorDetail(e));
		}

		return errorList;
	}

	/**
	 * Returns the exception list. Each item in the list is an instance of
	 * <code>Exception</code>.
	 *
	 * @return the exception list.
	 */

	public List<Exception> getExceptionList() {
		return exceptionList;
	}

	/**
	 * Returns the design file name.
	 *
	 * @return the design file name.
	 */

	public String getFileName() {
		return fileName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.exception.BirtException#getLocalizedMessage()
	 */

	@Override
	public String getLocalizedMessage() {
		if (sResourceKey == null) {
			return ""; //$NON-NLS-1$
		}

		return ModelMessages.getMessage(sResourceKey);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getMessage()
	 */

	@Override
	public String getMessage() {
		return getLocalizedMessage();
	}

	/**
	 * Returns a string representation of the exception. If the exception type is
	 * SYNTAX_ERROR or INVALID_XML, this method checks all errors in the
	 * <code>errorList</code> and assemble them into a string. The return string is
	 * assembled in the ways:
	 *
	 * <table border="1">
	 * <th width="20%">Error Type</th>
	 * <th width="40%">Message</th>
	 *
	 * <tr>
	 * <td>SYNTAX_ERROR and INVALID_XML</td>
	 * <td><code>[errorType]</code>- [numOfErrors] errors found. <br>
	 * 1.) [detail messages.] <br>
	 * 2.) [detail messages.] <br>
	 * ... <br>
	 * </td>
	 * </tr>
	 *
	 * <tr>
	 * <td>SEMANTIC_ERROR</td>
	 * <td>Impossible to occur.</td>
	 * </tr>
	 *
	 * </table>
	 *
	 * Note output message are locale independent. ONLY for debugging, not
	 * user-visible. Debugging messages are defined to be in English.
	 *
	 * @see java.lang.Object#toString()
	 * @see ErrorDetail#toString()
	 * @see #getLocalizedMessage()
	 *
	 */

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(sResourceKey);
		sb.append(" - "); //$NON-NLS-1$
		if (sResourceKey == DESIGN_EXCEPTION_SYNTAX_ERROR || sResourceKey == DESIGN_EXCEPTION_INVALID_XML) {
			List<ErrorDetail> errorList = getErrorList();
			if (errorList != null) {
				sb.append(errorList.size());
				sb.append(" errors found! \n"); //$NON-NLS-1$

				int i = 1;
				Iterator<ErrorDetail> iter = errorList.iterator();
				while (iter.hasNext()) {
					ErrorDetail e = iter.next();

					sb.append(i++);
					sb.append(".) "); //$NON-NLS-1$
					sb.append(e);
					sb.append("\n"); //$NON-NLS-1$
				}
			}
		} else {
			// SEMANTIC_ERROR does not occurs here.

			assert false;
			return super.toString();
		}

		return sb.toString();
	}
}
