/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.mock;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import junit.framework.AssertionFailedError;

/**
 * Mock a HttpServletResponse class for Viewer UnitTest
 *
 */
public class HttpServletResponseSimulator implements HttpServletResponse {

	private OutputStream out;
	private StringWriter stringWriter;
	private PrintWriter printWriter;
	private boolean isWriter = false;
	private boolean isOutputStream = false;

	private Hashtable headers;
	private Hashtable cookies;
	private Locale locale;
	private String contentType;
	private int contentLength;
	private String message;
	private int status = SC_OK;
	private String characterEncoding;

	public static final String HEADER_CONTENT_TYPE = "content-type"; //$NON-NLS-1$
	public static final String HEADER_CONTENT_LENGTH = "content-length"; //$NON-NLS-1$

	private boolean isCommitted = false;
	public static final int SC_CONTINUE = 100;
	public static final int SC_SWITCHING_PROTOCOLS = 101;
	public static final int SC_OK = 200;
	public static final int SC_CREATED = 201;
	public static final int SC_ACCEPTED = 202;
	public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;
	public static final int SC_NO_CONTENT = 204;
	public static final int SC_RESET_CONTENT = 205;
	public static final int SC_PARTIAL_CONTENT = 206;
	public static final int SC_MULTIPLE_CHOICES = 300;
	public static final int SC_MOVED_PERMANENTLY = 301;
	public static final int SC_MOVED_TEMPORARILY = 302;
	public static final int SC_SEE_OTHER = 303;
	public static final int SC_NOT_MODIFIED = 304;
	public static final int SC_USE_PROXY = 305;
	public static final int SC_BAD_REQUEST = 400;
	public static final int SC_UNAUTHORIZED = 401;
	public static final int SC_PAYMENT_REQUIRED = 402;
	public static final int SC_FORBIDDEN = 403;
	public static final int SC_NOT_FOUND = 404;
	public static final int SC_METHOD_NOT_ALLOWED = 405;
	public static final int SC_NOT_ACCEPTABLE = 406;
	public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
	public static final int SC_REQUEST_TIMEOUT = 408;
	public static final int SC_CONFLICT = 409;
	public static final int SC_GONE = 410;
	public static final int SC_LENGTH_REQUIRED = 411;
	public static final int SC_PRECONDITION_FAILED = 412;
	public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;
	public static final int SC_REQUEST_URI_TOO_LONG = 414;
	public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;
	public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
	public static final int SC_EXPECTATION_FAILED = 417;
	public static final int SC_INTERNAL_SERVER_ERROR = 500;
	public static final int SC_NOT_IMPLEMENTED = 501;
	public static final int SC_BAD_GATEWAY = 502;
	public static final int SC_SERVICE_UNAVAILABLE = 503;
	public static final int SC_GATEWAY_TIMEOUT = 504;
	public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;

	public HttpServletResponseSimulator() {
		this.headers = new Hashtable();
		this.cookies = new Hashtable();
		this.locale = Locale.US;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * jakarta.servlet.http.HttpServletResponse#addCookie(jakarta.servlet.http.Cookie)
	 */
	@Override
	public void addCookie(Cookie cookie) {
		if (cookie == null) {
			return;
		}

		this.cookies.put(cookie.getName(), cookie);
	}

	/**
	 * Get Cookie by Name
	 *
	 * @param name
	 * @return
	 */
	public Cookie getCookie(String name) {
		Object cookie = this.cookies.get(name);
		if (cookie != null) {
			return (Cookie) cookie;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#addDateHeader(java.lang.String,
	 * long)
	 */
	@Override
	public void addDateHeader(String name, long header) {
		throw new UnsupportedOperationException("Do not support addDateHeader operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#addHeader(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addHeader(String name, String header) {
		if (name == null) {
			return;
		}

		this.headers.put(name, header);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#addIntHeader(java.lang.String,
	 * int)
	 */
	@Override
	public void addIntHeader(String name, int header) {
		throw new UnsupportedOperationException("Do not support addIntHeader operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
	 */
	@Override
	public boolean containsHeader(String name) {
		return this.headers.containsKey(name);
	}

	/**
	 * Get Header by Name
	 *
	 * @param name
	 * @return
	 */
	@Override
	public String getHeader(String name) {
		Object header = this.headers.get(name);
		if (header != null) {
			return (String) header;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * jakarta.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
	 */
	@Override
	public String encodeRedirectURL(String url) {
		return url;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * jakarta.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
	 */
	@Override
	public String encodeRedirectUrl(String url) {
		return encodeRedirectURL(url);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
	 */
	@Override
	public String encodeURL(String url) {
		return url;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
	 */
	@Override
	public String encodeUrl(String url) {
		return encodeURL(url);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#sendError(int)
	 */
	@Override
	public void sendError(int status) throws IOException {
		setStatus(status);
		throw new AssertionFailedError(" Response error :" + status); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
	 */
	@Override
	public void sendError(int status, String message) throws IOException {
		setStatus(status, message);
		throw new AssertionFailedError(" Response error :" + status + " " //$NON-NLS-1$//$NON-NLS-2$
				+ message);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
	 */
	@Override
	public void sendRedirect(String url) throws IOException {
		throw new UnsupportedOperationException("Do not support sendRedirect operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#setDateHeader(java.lang.String,
	 * long)
	 */
	@Override
	public void setDateHeader(String name, long header) {
		this.addDateHeader(name, header);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#setHeader(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setHeader(String name, String header) {
		if (name == null) {
			return;
		}

		if (HEADER_CONTENT_TYPE.equalsIgnoreCase(name)) {
			setContentType(header);
			return;
		} else if (HEADER_CONTENT_LENGTH.equalsIgnoreCase(name)) {
			setContentLength(Integer.parseInt(header));
			return;
		}

		this.addHeader(name, header);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#setIntHeader(java.lang.String,
	 * int)
	 */
	@Override
	public void setIntHeader(String name, int header) {
		this.addIntHeader(name, header);
	}

	/**
	 * Remove header by name
	 *
	 * @param name
	 */
	public void removeHeader(String name) {
		this.headers.remove(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#setStatus(int)
	 */
	@Override
	public void setStatus(int status) {
		setStatus(status, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
	 */
	@Override
	public void setStatus(int status, String message) {
		this.status = status;
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#flushBuffer()
	 */
	@Override
	public void flushBuffer() throws IOException {
		throw new UnsupportedOperationException("Do not support flushBuffer operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#getBufferSize()
	 */
	@Override
	public int getBufferSize() {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#getCharacterEncoding()
	 */
	@Override
	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#getContentType()
	 */
	@Override
	public String getContentType() {
		return this.contentType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#getLocale()
	 */
	@Override
	public Locale getLocale() {
		return this.locale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#getOutputStream()
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (this.isWriter) {
			throw new IllegalStateException("Has called getWriter method !"); //$NON-NLS-1$
		}

		ServletOutputStream servletOutputStream = null;
		if (this.out == null) {
			servletOutputStream = new ServletOutputStreamSimulator();
		} else {
			servletOutputStream = new ServletOutputStreamSimulator(this.out);
		}

		this.out = null;
		this.isOutputStream = true;
		return servletOutputStream;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#getWriter()
	 */
	@Override
	public PrintWriter getWriter() throws IOException {
		if (this.isOutputStream) {
			throw new IllegalStateException("Has called getOutputStream method !"); //$NON-NLS-1$
		}

		this.stringWriter = new StringWriter();
		this.printWriter = new PrintWriter(this.stringWriter);

		this.isWriter = true;
		return this.printWriter;
	}

	/**
	 * Return StringBuffer for test
	 *
	 * @return
	 */
	public StringBuffer getStringBuffer() {
		if (this.stringWriter == null) {
			return null;
		}

		return this.stringWriter.getBuffer();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#isCommitted()
	 */
	@Override
	public boolean isCommitted() {
		return this.isCommitted;
	}

	/**
	 * Set isCommitted flag
	 *
	 * @param isCommitted
	 */
	public void setIsCommitted(boolean isCommitted) {
		this.isCommitted = isCommitted;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#reset()
	 */
	@Override
	public void reset() {
		this.headers = new Hashtable();
		this.cookies = new Hashtable();
		this.stringWriter = null;
		this.printWriter = null;
		this.isWriter = false;
		this.isOutputStream = false;
		this.contentLength = 0;
		this.contentType = null;
		this.locale = Locale.US;
		this.characterEncoding = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#resetBuffer()
	 */
	@Override
	public void resetBuffer() {
		throw new UnsupportedOperationException("Do not support resetBuffer operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#setBufferSize(int)
	 */
	@Override
	public void setBufferSize(int size) {
		throw new UnsupportedOperationException("Do not support setBufferSize operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
	 */
	@Override
	public void setCharacterEncoding(String encoding) {
		this.characterEncoding = encoding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#setContentLength(int)
	 */
	@Override
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletResponse#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the contentLength
	 */
	public int getContentLength() {
		return contentLength;
	}

	/**
	 * Return the status code.
	 *
	 * @return the status code.
	 */
	@Override
	public int getStatus() {
		return this.status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param out the out to set
	 */
	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

	@Override
	public Collection<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContentLengthLong(long arg0) {
		// TODO Auto-generated method stub

	}

}
