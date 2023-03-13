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

package org.eclipse.birt.report.engine.emitter.postscript.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtil {

	protected static Logger log = Logger.getLogger(FileUtil.class.getName());

	public static void load(String file, OutputStream out) {
		InputStream input = null;
		try {
			input = FileUtil.class.getClassLoader().getResourceAsStream(file);
			byte[] buffer = new byte[1024];
			int length = -1;
			do {
				length = input.read(buffer);
				if (length < 0) {
					break;
				}
				out.write(buffer, 0, length);

			} while (true);
		} catch (IOException e) {
			log.log(Level.WARNING, "load file: " + file);
//			e.printStackTrace( );
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.log(Level.WARNING, "close file: " + file);
//					e.printStackTrace( );
				}
				input = null;
			}
		}
	}
}
