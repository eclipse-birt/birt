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

package org.eclipse.birt.data.aggregation.impl.rank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.cache.BasicCachedList;

/**
 * A extention class of BasicCachedList. This class can save the instances of
 * NullObject and DummyObject.
 */

public class BasicCachedListExt extends BasicCachedList {

	protected static final int NULL_OBJECT = Integer.MAX_VALUE - 1;
	protected static final int DUMMY_OBJECT = Integer.MAX_VALUE - 2;

	public BasicCachedListExt(String tempDir) {
		super(tempDir, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.cache.BasicCachedList#writeObject(java.io.
	 * DataOutputStream, java.lang.Object)
	 */
	protected void writeObject(DataOutputStream oos, Object object) throws IOException {
		if (object == null) {
			IOUtil.writeInt(oos, NULL_VALUE);
			return;
		} else if (object instanceof DummyObject) {
			IOUtil.writeInt(oos, DUMMY_OBJECT);
			return;
		} else if (object instanceof NullObject) {
			IOUtil.writeInt(oos, NULL_OBJECT);
			return;
		}
		IOUtil.writeInt(oos, OBJECT_VALUE);
		IOUtil.writeObject(oos, object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.cache.BasicCachedList#readObject(java.io.
	 * DataInputStream)
	 */
	protected Object readObject(DataInputStream dis) throws IOException {
		int fieldCount = IOUtil.readInt(dis);
		if (fieldCount == NULL_VALUE) {
			return null;
		} else if (fieldCount == DUMMY_OBJECT) {
			return new DummyObject();
		} else if (fieldCount == NULL_OBJECT) {
			return new NullObject();
		}

		return IOUtil.readObject(dis);
	}
}

/**
 * 
 */

class DummyObject {

}

/**
 * 
 */

class NullObject {
	public String toString() {
		return "";//$NON-NLS-1$
	}
}
