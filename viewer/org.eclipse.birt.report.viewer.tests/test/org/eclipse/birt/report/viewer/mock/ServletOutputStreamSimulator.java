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

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * This class extends and mocks an abstract ServletOutputStream class for Viewer
 * UnitTest
 *
 */
public class ServletOutputStreamSimulator extends ServletOutputStream {
	/**
	 * Servlet Output Stream
	 */
	private OutputStream out;

	/**
	 * Constructor
	 *
	 */
	public ServletOutputStreamSimulator() {
		this.out = System.out;
	}

	/**
	 * Constructor
	 *
	 * @param out
	 */
	public ServletOutputStreamSimulator(OutputStream out) {
		this.out = out;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {
		// TODO Auto-generated method stub

	}
}
