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

package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * A utility file reader handler, which keeps the file stream and input stream
 * for reading data.
 */
class DataFileReader {
	private File file;
	private boolean isOpen;

	private FileInputStream fis;
	private BufferedInputStream bis;

	private ResultObjectUtil resultObjectUtil;

	/**
	 * A util method to new instance of DataFileReader
	 * 
	 * @param file
	 * @return DataFileReader instance
	 */
	static DataFileReader newInstance(File file, ResultObjectUtil resultObjectUtil) {
		return new DataFileReader(file, resultObjectUtil);
	}

	/**
	 * Construction
	 * 
	 * @param file
	 */
	private DataFileReader(File file, ResultObjectUtil resultObjectUtil) {
		this.resultObjectUtil = resultObjectUtil;
		setReadFile(file);
	}

	/**
	 * Set which file to be read. This method is mainly used to new less instance.
	 * 
	 * @param file
	 */
	void setReadFile(File file) {
		if (isOpen)
			close();

		this.file = file;
		this.isOpen = false;
	}

	/**
	 * Read the specified length of objects from file. Notice to improve the
	 * efficienly of reading, the order of reading only can be sequencial. The
	 * caller has responsibility to design a good algorithm to achive this goal.
	 * 
	 * @param length
	 * @param stopSign
	 * @throws IOException, exception of reading file
	 * @return ResultObject array
	 * @throws DataException
	 */
	IResultObject[] read(int length) throws IOException, DataException {
		if (isOpen == false) {
			try {
				fis = FileSecurity.createFileInputStream(file);
			} catch (Exception e) {
				// normally this exception will never be thrown
				// since file will always exist
			}
			bis = new BufferedInputStream(fis);
			isOpen = true;
		}

		return resultObjectUtil.readData(bis, null, length);
	}

	/**
	 * Close current input file.
	 * 
	 * @throws IOException, file close exception
	 */
	void close() {
		if (isOpen) {
			try {
				bis.close();
				fis.close();
				isOpen = false;
			} catch (IOException e) {
				// normally this exception will never be thrown
			}
		}
	}

}
