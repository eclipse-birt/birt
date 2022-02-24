/*******************************************************************************
 * Copyright (c) 2007,2010 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;

public class RunStatusWriter {
	protected RAOutputStream runStatusStream;

	static protected Logger logger = Logger.getLogger(RunStatusWriter.class.getName());

	public RunStatusWriter(IDocArchiveWriter writer) {
		try {
			runStatusStream = writer.createRandomAccessStream(ReportDocumentConstants.RUN_STATUS_STREAM);
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Unable to create stream to write run task status"); //$NON-NLS-1$
			close();
		}
	}

	public void close() {
		try {
			if (runStatusStream != null) {
				runStatusStream.close();
				runStatusStream = null;
			}
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Unable to close the stream used to write run task status"); //$NON-NLS-1$
		}
	}

	private ByteArrayOutputStream writeBuffer = new ByteArrayOutputStream();
	private DataOutputStream out = new DataOutputStream(writeBuffer);

	public void writeRunTaskStatus(ArrayList<String> messages) {
		if (runStatusStream == null) {
			return;
		}
		try {
			writeBuffer.reset();
			runStatusStream.writeInt(messages.size());
			for (String message : messages) {
				writeBuffer.reset();
				IOUtil.writeString(out, message);
				runStatusStream.write(writeBuffer.toByteArray());
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, "Exception occured during writing run task status"); //$NON-NLS-1$
		}
	}

}
