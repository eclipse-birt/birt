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

package org.eclipse.birt.core.archive.compound;

import java.io.IOException;
import java.util.List;

public interface IArchiveFile {

	String getName();

	void close() throws IOException;

	void flush() throws IOException;

	void refresh() throws IOException;

	boolean exists(String name);

	void setCacheSize(long cacheSize);

	long getUsedCache();

	ArchiveEntry openEntry(String name) throws IOException;

	List<String> listEntries(String namePattern);

	ArchiveEntry createEntry(String name) throws IOException;

	boolean removeEntry(String name) throws IOException;

	Object lockEntry(String entry) throws IOException;

	void unlockEntry(Object locker) throws IOException;

	String getSystemId();

	String getDependId();

	void save() throws IOException;

	long getLength();
}
