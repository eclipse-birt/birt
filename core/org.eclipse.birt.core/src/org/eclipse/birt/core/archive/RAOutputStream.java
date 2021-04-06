/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RAOutputStream extends OutputStream {

	public abstract void seek(long localPos) throws IOException;

	public abstract void writeLong(long value) throws IOException;

	public abstract void writeInt(int value) throws IOException;

	public abstract long getOffset() throws IOException;

	public abstract long length() throws IOException;
}