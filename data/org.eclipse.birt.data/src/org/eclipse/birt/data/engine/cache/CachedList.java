/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.impl.DataEngineSession;

/**
 * A List class providing the service of reading/writing objects from one file
 * when cache is not enough . It makes the reading/writing objects transparent.
 */

public class CachedList extends BasicCachedList {
	private ICachedObjectCreator creator;

	/**
	 *
	 *
	 */
	public CachedList(String tempDir, ClassLoader loader, ICachedObjectCreator creator) {
		super(tempDir, loader);
		this.creator = creator;
	}

	/**
	 *
	 * @param list
	 */
	public CachedList(String tempDir, ClassLoader loader, ICachedObjectCreator creator, List list) {
		super(tempDir, loader, list);
		this.creator = creator;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.cache.BasicCachedList#writeObject(java.io.
	 * DataOutputStream, java.lang.Object)
	 */
	@Override
	protected void writeObject(DataOutputStream oos, Object object) throws IOException {
		if (object == null) {
			IOUtil.writeInt(oos, NULL_VALUE);
			return;
		}
		ICachedObject cachedObject = (ICachedObject) object;
		Object[] objects = cachedObject.getFieldValues();
		IOUtil.writeInt(oos, objects.length);
		for (int i = 0; i < objects.length; i++) {
			IOUtil.writeObject(oos, objects[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.cache.BasicCachedList#readObject(java.io.
	 * DataInputStream)
	 */
	@Override
	protected Object readObject(DataInputStream dis) throws IOException {
		int fieldCount = IOUtil.readInt(dis);
		if (fieldCount == NULL_VALUE) {
			return null;
		}
		Object[] objects = new Object[fieldCount];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = IOUtil.readObject(dis, DataEngineSession.getCurrentClassLoader());
		}
		return creator.createInstance(objects);
	}

}
