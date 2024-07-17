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

import org.eclipse.birt.report.viewer.util.RandomUtil;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

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
	 * @see jakarta.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) {
		verify();
		return this.attributes.get(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#getAttributeNames()
	 */
	@Override
	public Enumeration getAttributeNames() {
		verify();
		return this.attributes.keys();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setAttribute(String name, Object value) {
		verify();
		this.attributes.put(name, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(String name) {
		verify();
		this.attributes.remove(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#getCreationTime()
	 */
	@Override
	public long getCreationTime() {
		verify();
		return this.creationTime;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#getId()
	 */
	@Override
	public String getId() {
		verify();
		return this.sessionId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#getLastAccessedTime()
	 */
	@Override
	public long getLastAccessedTime() {
		verify();
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	@Override
	public int getMaxInactiveInterval() {
		verify();
		return this.maxInactiveInterval;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#getServletContext()
	 */
	@Override
	public ServletContext getServletContext() {
		verify();
		return this.context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#invalidate()
	 */
	@Override
	public void invalidate() {
		verify();
		this.isValid = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#isNew()
	 */
	@Override
	public boolean isNew() {
		verify();
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	@Override
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
		if (!isValid) {
			throw new IllegalStateException("Session has been invalid!"); //$NON-NLS-1$
		}
	}
}
