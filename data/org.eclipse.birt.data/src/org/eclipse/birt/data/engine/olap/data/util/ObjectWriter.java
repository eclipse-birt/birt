
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
