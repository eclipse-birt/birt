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

package org.eclipse.birt.data.engine.impl.document.viewing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.util.IOUtil;

/**
 * Wrap simple I/O for row index information.
 */
public class RowIndexUtil {
	private DataOutputStream rowDos;
	private DataInputStream rowDis;
	private static Logger logger = Logger.getLogger(RowIndexUtil.class.getName());

	/**
	 * @param rowOs
	 */
	public RowIndexUtil(OutputStream rowOs) {
		rowDos = new DataOutputStream(rowOs);
	}

	/**
	 * @param rowIs
	 */
	public RowIndexUtil(InputStream rowIs) {
		rowDis = new DataInputStream(rowIs);
	}

	/**
	 * @param rowId
	 * @param filterHint
	 */
	public void write(int rowId) {
		try {
			IOUtil.writeInt(rowDos, rowId);
		} catch (IOException e) {
			logger.log(Level.FINE, e.getMessage(), e);
		}
	}

	/**
	 * @return
	 */
	public int read() {
		try {
			return IOUtil.readInt(rowDis);
		} catch (IOException e) {
			return -1;
		}
	}

	/**
	 *
	 */
	public void close() {
		try {
			if (rowDos != null) {
				rowDos.close();
			}
			if (rowDis != null) {
				rowDis.close();
			}
		} catch (IOException e) {
			// ignore
		}
	}

}
