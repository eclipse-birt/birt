/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.core.archive.compound.v3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Ext2Entry {

	int inode;
	String name;

	Ext2Entry(String name, int inode) {
		this.name = name;
		this.inode = inode;
	}

	public String getName() {
		return name;
	}

	void write(DataOutput out) throws IOException {

		out.writeInt(inode);
		out.writeUTF(name);
	}

	void read(DataInput in) throws IOException {
		this.inode = in.readInt();
		this.name = in.readUTF();
	}
}
