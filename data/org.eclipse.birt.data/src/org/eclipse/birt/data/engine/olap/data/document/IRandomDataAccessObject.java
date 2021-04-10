
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public BigDecimal readBigDecimal() throws IOException;

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Date readDate() throws IOException;

	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeBigDecimal(BigDecimal value) throws IOException;

	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeDate(Date value) throws IOException;

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Bytes readBytes() throws IOException;

	/**
	 * 
	 * @param b
	 * @throws IOException
	 */
	public void writeBytes(Bytes b) throws IOException;

	public Object readObject() throws IOException;

	public void writeObject(Object o) throws IOException;

}
