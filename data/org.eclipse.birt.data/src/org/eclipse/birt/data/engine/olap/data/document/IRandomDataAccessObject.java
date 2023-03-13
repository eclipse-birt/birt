
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.util.Bytes;

/**
 *
 */

public interface IRandomDataAccessObject extends DataInput, DataOutput, IRandomAccessObject {
	/**
	 *
	 * @return
	 * @throws IOException
	 */
	BigDecimal readBigDecimal() throws IOException;

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	Date readDate() throws IOException;

	/**
	 *
	 * @param value
	 * @throws IOException
	 */
	void writeBigDecimal(BigDecimal value) throws IOException;

	/**
	 *
	 * @param value
	 * @throws IOException
	 */
	void writeDate(Date value) throws IOException;

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	Bytes readBytes() throws IOException;

	/**
	 *
	 * @param b
	 * @throws IOException
	 */
	void writeBytes(Bytes b) throws IOException;

	Object readObject() throws IOException;

	void writeObject(Object o) throws IOException;

}
