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

package org.eclipse.birt.report.engine.emitter.postscript.truetypefont;

import java.io.IOException;

public interface ITrueTypeWriter {
	void close() throws IOException;

	void initialize(String fontName) throws IOException;

	String getDisplayName();

	void ensureGlyphAvailable(char c) throws IOException;

	void ensureGlyphsAvailable(String string) throws IOException;

	String toHexString(String text);
}
