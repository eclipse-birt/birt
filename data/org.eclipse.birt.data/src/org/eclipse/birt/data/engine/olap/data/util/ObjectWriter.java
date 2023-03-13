
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

public class ObjectWriter implements IObjectWriter {
	private IObjectWriter writer = null;
	private int dataType = DataType.UNKNOWN_TYPE;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.util.IObjectWriter#write(org.eclipse.
	 * birt.data.engine.olap.data.util.BufferedRandomAccessFile, java.lang.Object)
	 */
	@Override
	public void write(BufferedRandomAccessFile file, Object obj) throws IOException {
		if (obj == null) {
			file.write(0);
		} else {
			file.write(1);
			if (writer == null) {
				dataType = DataType.getDataType(obj.getClass());
				writer = IOUtil.getRandomWriter(dataType);
			}
			writer.write(file, obj);
		}

	}

	/**
	 *
	 * @return
	 */
	public int getDataType() {
		return dataType;
	}

}
