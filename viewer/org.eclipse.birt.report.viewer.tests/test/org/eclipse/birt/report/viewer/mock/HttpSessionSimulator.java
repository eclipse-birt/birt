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

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.eclipse.birt.report.viewer.util.RandomUtil;

/**
 * Mock a HttpSession class for Viewer UnitTest
 * 
 */
public class HttpSessionSimulator implements HttpSession {

	private Hashtable attributes;
	private ServletContext context;
	private boolean isValid = true;
	private long creationTime;
	private int maxInactiveInterval;
	private String sessionId;

	public HttpSessionSimulator(ServletContext context) {
		this.context = context;
		this.attributes = new Hashtable();
		this.creationTime = System.currentTimeMillis();
		this.sessionId = new RandomUtil().get(32);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		verify();
		return this.attributes.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		verify();
		return this.attributes.keys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	public void setAttribute(String name, Object value) {
		verify();
		this.attributes.put(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		verify();
		this.attributes.remove(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
	 */
	public Object getValue(String name) {
		verify();
		return this.getAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getValueNames()
	 */
	public String[] getValueNames() {
		verify();
		return (String[]) this.attributes.keySet().toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#putValue(java.lang.String,
	 * java.lang.Object)
	 */
	public void putValue(String name, Object value) {
		verify();
		this.setAttribute(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
	 */
	public void removeValue(String name) {
		verify();
		this.removeAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 */
	public long getCreationTime() {
		verify();
		return this.creationTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	public String getId() {
		verify();
		return this.sessionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 */
	public long getLastAccessedTime() {
		verify();
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	public int getMaxInactiveInterval() {
		verify();
		return this.maxInactiveInterval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	public ServletContext getServletContext() {
		verify();
		return this.context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getSessionContext()
	 */
	public HttpSessionContext getSessionContext() {
		verify();
		throw new UnsupportedOperationException("Do not support getSessionContext operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	public void invalidate() {
		verify();
		this.isValid = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	public boolean isNew() {
		verify();
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	public void setMaxInactiveInterval(int interval) {
		verify();
		this.maxInactiveInterval = interval;
	}

	/**
	 * @return the isValid
	 */
	public boolean isValid() {
		verify();
		return isValid;
	}

	/**
	 * Verify current session if invalid
	 * 
	 * @throws IllegalStateException
	 */
	private void verify() throws IllegalStateException {
		if (!isValid)
			throw new IllegalStateException("Session has been invalid!"); //$NON-NLS-1$
	}
}
