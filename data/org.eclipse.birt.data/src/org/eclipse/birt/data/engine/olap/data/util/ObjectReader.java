
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;

/**
 * 
 */

public class ObjectReader implements IObjectReader {
	private IObjectReader reader = null;
	private int dataType = DataType.UNKNOWN_TYPE;

	/**
	 * 
	 */
	public Object read(BufferedRandomAccessFile file) throws IOException {
		int nullIndicator = file.read();
		if (nullIndicator == 0)
			return null;
		return reader.read(file);
	}

	/**
	 * 
	 * @param dataType
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
		this.reader = IOUtil.getRandomReader(dataType);
	}

	/**
	 * 
	 * @return
	 */
	public int getDataType() {
		return this.dataType;
	}

}
