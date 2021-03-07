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

package org.eclipse.birt.core.exception;

import java.util.Locale;
import java.util.ResourceBundle;

import com.ibm.icu.text.MessageFormat;

/**
 * Define BIRT's Exception framework. Every BIRT exception has to include an
 * error code, which is a string. Different BIRT modules use different prefix
 * for error codes. For example,
 *
 * <li>DE uses DESIGN_EXCEPTION_
 * <li>DtE uses DATA_EXCEPTION_
 * <li>FPE uses GENERATION_EXCEPTION_ and VIEW_EXCEPTION_
 * <li>UI uses UI_EXCEPTION_
 * <li>Chart used CHART_EXCEPTION_
 * <li>viewer uses VIERER_EXCEPTION_</li>
 *
 * as prefix. An error code is used for retrieving error message, which is
 * externalizable, and can be seen by end users. The error code itself allows
 * the identification of the subcomponent that generates the exception, avoiding
 * the need to create exceltion subclasses such as BirtEngineException,
 * BirtDtEException, etc.
 *
 * Note that the resource key (or error code), message arguments and resource
 * bundle are immutable.
 *
 */
public class BirtException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -2152858415308815725L;

	/**
	 * The resource key that represents the internal error code used in fetching an
	 * externalized message
	 */
	protected final String sResourceKey;

	/**
	 * Optional arguments to be used with a resource key to build the error message
	 */
	protected final Object[] oaMessageArguments;

	/**
	 * The resource bundle that holds a collection of messages for a specific locale
	 */
	protected final transient ResourceBundle rb;

	/**
	 * The unique identifier of the plug-in associated with this exception
	 */
	protected String pluginId;

	/**
	 * The severity of this exception. One of
	 * <ul>
	 * <li><code>CANCEL</code></li>
	 * <li><code>ERROR</code> (default value)</li>
	 * <li><code>WARNING</code></li>
	 * <li><code>INFO</code></li>
	 * <li>or <code>OK</code> (0)</li>
	 * </ul>
	 */
	protected int severity = ERROR;

	/**
	 * Status severity constant (value 0) indicating this exception represents the
	 * nominal case. This constant is also used as the status code representing the
	 * nominal case.
	 *
	 * @see #getSeverity()
	 */
	public static final int OK = 0;

	/**
	 * Status type severity (bit mask, value 1) indicating this exception is
	 * informational only.
	 *
	 * @see #getSeverity()
	 */
	public static final int INFO = 0x01;

	/**
	 * Status type severity (bit mask, value 2) indicating this exception represents
	 * a warning.
	 *
	 * @see #getSeverity()
	 */
	public static final int WARNING = 0x02;

	/**
	 * Status type severity (bit mask, value 4) indicating this exception represents
	 * an error.
	 *
	 * @see #getSeverity()
	 */
	public static final int ERROR = 0x04;

	/**
	 * Status type severity (bit mask, value 8) indicating this exception represents
	 * a cancelation
	 *
	 * @see #getSeverity()
	 */
	public static final int CANCEL = 0x08;

	/**
	 * @param mesage error message
	 */
	public BirtException(String message) {
		this.sResourceKey = message;
		this.rb = null;
		this.oaMessageArguments = null;
	}

	/**
	 * @deprecated Constructs a new Birt exception with no cause object.
	 *
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 */
	@Deprecated
	public BirtException(String errorCode, ResourceBundle bundle) {
		this.sResourceKey = errorCode;
		this.rb = bundle;
		this.oaMessageArguments = null;
	}

	/**
	 * @deprecated
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param cause          the nested exception
	 */
	@Deprecated
	public BirtException(String errorCode, ResourceBundle bundle, Throwable cause) {
		super(cause);
		this.sResourceKey = errorCode;
		this.rb = bundle;
		this.oaMessageArguments = null;
	}

	/**
	 * @deprecated
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param args           string arguments used to format error messages
	 */
	@Deprecated
	public BirtException(String errorCode, Object[] args, ResourceBundle bundle, Throwable cause) {
		super(cause);
		this.sResourceKey = errorCode;
		this.oaMessageArguments = args;
		this.rb = bundle;
	}

	/**
	 * @deprecated
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param cause          the nested exception
	 * @param arg0           first argument used to format error messages
	 */
	@Deprecated
	public BirtException(String errorCode, Object arg0, ResourceBundle bundle, Throwable cause) {
		super(cause);
		this.sResourceKey = errorCode;
		this.rb = bundle;

		this.oaMessageArguments = new Object[] { arg0 };
	}

	/**
	 * @deprecated
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param args           string arguments used to format error messages
	 */
	@Deprecated
	public BirtException(String errorCode, Object[] args, ResourceBundle bundle) {
		this.sResourceKey = errorCode;
		this.oaMessageArguments = args;
		this.rb = bundle;
	}

	/**
	 * @deprecated
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param cause          the nested exception
	 * @param arg0           first argument used to format error messages
	 */
	@Deprecated
	public BirtException(String errorCode, Object arg0, ResourceBundle bundle) {
		this.sResourceKey = errorCode;
		this.rb = bundle;
		this.oaMessageArguments = new Object[] { arg0 };
	}

	/**
	 * @deprecated
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param arg0      first argument used to format error messages
	 */
	@Deprecated
	public BirtException(String errorCode, Object arg0) {
		this.sResourceKey = errorCode;
		this.oaMessageArguments = new Object[] { arg0 };
		this.rb = null;
	}

	/**
	 * @deprecated
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param cause     the nested exception
	 * @param args      string arguments used to format error messages
	 */
	@Deprecated
	public BirtException(String errorCode, Object[] args, Throwable cause) {
		super(cause);
		this.sResourceKey = errorCode;
		this.oaMessageArguments = args;
		this.rb = null;
	}

	/**
	 * Constructs a new Birt exception with no cause object.
	 *
	 * @param pluginId       Returns the unique identifier of the plug-in associated
	 *                       with this exception *
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 */
	public BirtException(String pluginId, String errorCode, ResourceBundle bundle) {
		this.pluginId = pluginId;
		this.sResourceKey = errorCode;
		this.rb = bundle;
		this.oaMessageArguments = null;
	}

	public BirtException() {
		this.sResourceKey = null;
		this.rb = null;
		this.oaMessageArguments = null;
	}

	/**
	 * @param pluginId       Returns the unique identifier of the plug-in associated
	 *                       with this exception
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param cause          the nested exception
	 */
	public BirtException(String pluginId, String errorCode, ResourceBundle bundle, Throwable cause) {
		super(cause);
		this.pluginId = pluginId;
		this.sResourceKey = errorCode;
		this.rb = bundle;
		this.oaMessageArguments = null;
	}

	/**
	 * @param pluginId       Returns the unique identifier of the plug-in associated
	 *                       with this exception
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param args           string arguments used to format error messages
	 */
	public BirtException(String pluginId, String errorCode, Object[] args, ResourceBundle bundle, Throwable cause) {
		super(cause);
		this.pluginId = pluginId;
		this.sResourceKey = errorCode;
		this.oaMessageArguments = args;
		this.rb = bundle;
	}

	/**
	 * @param pluginId       Returns the unique identifier of the plug-in associated
	 *                       with this exception
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param cause          the nested exception
	 * @param arg0           first argument used to format error messages
	 */
	public BirtException(String pluginId, String errorCode, Object arg0, ResourceBundle bundle, Throwable cause) {
		super(cause);
		this.pluginId = pluginId;
		this.sResourceKey = errorCode;
		this.rb = bundle;

		this.oaMessageArguments = new Object[] { arg0 };
	}

	/**
	 * @param pluginId       Returns the unique identifier of the plug-in associated
	 *                       with this exception
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param args           string arguments used to format error messages
	 */
	public BirtException(String pluginId, String errorCode, Object[] args, ResourceBundle bundle) {
		this.pluginId = pluginId;
		this.sResourceKey = errorCode;
		this.oaMessageArguments = args;
		this.rb = bundle;
	}

	/**
	 * @param pluginId       Returns the unique identifier of the plug-in associated
	 *                       with this exception
	 * @param errorCode      used to retrieve a piece of externalized message
	 *                       displayed to end user.
	 * @param resourceBundle the resourceBundle used to translate the message.
	 * @param cause          the nested exception
	 * @param arg0           first argument used to format error messages
	 */
	public BirtException(String pluginId, String errorCode, Object arg0, ResourceBundle bundle) {
		this.pluginId = pluginId;
		this.sResourceKey = errorCode;
		this.rb = bundle;
		this.oaMessageArguments = new Object[] { arg0 };
	}

	/**
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param severity
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param arg0      first argument used to format error messages
	 */
	public BirtException(String pluginId, String errorCode, Object arg0) {
		this.pluginId = pluginId;
		this.sResourceKey = errorCode;
		this.oaMessageArguments = new Object[] { arg0 };
		this.rb = null;
	}

	/**
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param cause     the nested exception
	 * @param args      string arguments used to format error messages
	 */
	public BirtException(String pluginId, String errorCode, Object[] args, Throwable cause) {
		super(cause);
		this.pluginId = pluginId;
		this.sResourceKey = errorCode;
		this.oaMessageArguments = args;
		this.rb = null;
	}

	/**
	 * @return Returns the errorCode.
	 */
	public String getErrorCode() {
		return sResourceKey;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	@Override
	public String getLocalizedMessage() {
		return getLocalizedMessage(sResourceKey);
	}

	@Override
	public String getMessage() {
		return getLocalizedMessage(sResourceKey);
	}

	/**
	 * Returns a localized message based on an error code. Overwrite this method if
	 * you do not want to pass in the resource bundle
	 *
	 * @param errorCode the error code
	 * @return Localized display message.
	 */
	protected String getLocalizedMessage(String errorCode) {
		String localizedMessage;
		Locale locale = null;
		if (rb == null) {
			localizedMessage = errorCode;
		} else {
			locale = rb.getLocale();
			try {
				localizedMessage = rb.getString(errorCode);
			} catch (Exception e) {
				localizedMessage = errorCode;
			}
		}

		try {
			MessageFormat form = new MessageFormat(localizedMessage, locale == null ? Locale.getDefault() : locale);
			return form.format(oaMessageArguments);
		} catch (Throwable ex) {
			return localizedMessage;
		}
	}

	/**
	 * Returns the unique identifier of the plug-in associated with this exception
	 * (this is the plug-in that defines the meaning of the error code).
	 *
	 * @return the unique identifier of the relevant plug-in
	 */
	public String getPluginId() {
		return pluginId;
	}

	/**
	 * Returns the severity. The severities are as follows (in descending order):
	 * <ul>
	 * <li><code>CANCEL</code>- cancelation occurred</li>
	 * <li><code>ERROR</code>- a serious error (most severe)</li>
	 * <li><code>WARNING</code>- a warning (less severe)</li>
	 * <li><code>INFO</code>- an informational ("fyi") message (least severe)</li>
	 * <li><code>OK</code>- everything is just fine</li>
	 * </ul>
	 *
	 * @return the severity: one of <code>OK</code>,<code>ERROR</code>,
	 *         <code>INFO</code>,<code>WARNING</code>, or <code>CANCEL</code>
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * Sets the severity of the exception.
	 *
	 * @param severity the severity; one of <code>OK</code>,<code>ERROR</code>,
	 *                 <code>INFO</code>,<code>WARNING</code>, or
	 *                 <code>CANCEL</code>
	 */
	public void setSeverity(int severity) {
		assert severity == OK || severity == ERROR || severity == WARNING || severity == INFO || severity == CANCEL;
		this.severity = severity;
	}
}
