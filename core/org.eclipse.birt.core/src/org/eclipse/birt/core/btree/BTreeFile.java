/*******************************************************************************
 * Copyright (c) 2008,2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.btree;

import java.io.IOException;

public interface BTreeFile extends BTreeConstants {

	Object lock() throws IOException;

	void unlock(Object lock) throws IOException;

	void readBlock(int blockId, byte[] bytes) throws IOException;

	void writeBlock(int blockId, byte[] bytes) throws IOException;

	int allocBlock() throws IOException;

	int getTotalBlock() throws IOException;

	void close() throws IOException;
}
