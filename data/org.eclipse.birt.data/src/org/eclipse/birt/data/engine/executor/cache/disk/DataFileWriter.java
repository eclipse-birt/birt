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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * A utility file writer handler, which keeps the file stream and output stream
 * for reading data.
 */
class DataFileWriter {
	private File file;
	private boolean isOpen;

	private FileOutputStream fos;
	private BufferedOutputStream bos;

	private ResultObjectUtil resultObjectUtil;

	/**
	 * A util method to new instance of DataFileWriter
	 *
	 * @param file
	 * @return DataFileWriter instance
	 */
	static DataFileWriter newInstance(File file, ResultObjectUtil resultObjectUtil) {
		return new DataFileWriter(file, resultObjectUtil);
	}

	/**
	 * Construction
	 *
	 * @param file
	 */
	private DataFileWriter(File file, ResultObjectUtil resultObjectUtil) {
		this.resultObjectUtil = resultObjectUtil;
		setWriteFile(file);
	}

	/**
	 * Set which file to be written. This method is mainly used to new less
	 * instance.
	 *
	 * @param file
	 */
	void setWriteFile(File file) {
		if (isOpen) {
			close();
		}

		this.file = file;
		this.isOpen = false;
	}

	/**
	 * Write the specified length of objects from file. Notice to improve the
	 * efficienly of reading, the order of writing only can be sequencial. The
	 * caller has responsibility to design a good algorithm to achive this goal.
	 *
	 * @param resultObjects
	 * @param count
	 * @param stopSign
	 * @throws IOException,  exception of writing file
	 * @throws DataException
	 */
	void write(IResultObject[] resultObjects, int count) throws IOException, DataException {
		if (!isOpen) {
			try {
				fos = FileSecurity.createFileOutputStream(file);
			} catch (Exception e) {
				// normally this exception will never be thrown
				// since file will always exist
			}
			bos = new BufferedOutputStream(fos);
			isOpen = true;
		}

		resultObjectUtil.writeData(bos, resultObjects, count);
	}

	/**
	 * Close current output file
	 *
	 * @throws IOException, file close exception
	 */
	void close() {
		if (isOpen) {
			try {
				bos.close();
				fos.close();
				isOpen = false;
			} catch (IOException e) {
				// normally this exception will never be thrown
			}
		}
	}

}
