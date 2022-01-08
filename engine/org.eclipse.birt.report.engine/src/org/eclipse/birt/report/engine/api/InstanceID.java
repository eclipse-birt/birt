/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

/**
 * a class that wraps around an identifier for a report element instance
 */
public class InstanceID {

	protected InstanceID parentId;
	protected long uid;
	protected long designId;
	protected DataID dataId;

	/**
	 * Constructor.
	 * 
	 * @param parent   instance id of parent
	 * @param designId design id
	 * @param dataId   data id
	 */
	public InstanceID(InstanceID parent, long designId, DataID dataId) {
		this.parentId = parent;
		this.uid = -1;
		this.designId = designId;
		this.dataId = dataId;
	}

	public InstanceID(InstanceID parent, InstanceID iid) {
		this.parentId = parent;
		this.uid = iid.uid;
		this.designId = iid.designId;
		this.dataId = iid.dataId;
	}

	/**
	 * Constructor.
	 * 
	 * @param parent   instance id of parent
	 * @param uid
	 * @param designId
	 * @param dataId
	 */
	public InstanceID(InstanceID parent, long uid, long designId, DataID dataId) {
		this.parentId = parent;
		this.uid = uid;
		this.designId = designId;
		this.dataId = dataId;
	}

	/**
	 * Get parent id of this report element instance.
	 * 
	 * @return parentId
	 */
	public InstanceID getParentID() {
		return parentId;
	}

	/**
	 * Get unique id of this report element instance.
	 * 
	 * @return unique id
	 */
	public long getUniqueID() {
		return uid;
	}

	/**
	 * returns the component id of the element
	 */
	public long getComponentID() {
		return designId;
	}

	/**
	 * Get data id of the element.
	 * 
	 * @return dataId
	 */
	public DataID getDataID() {
		return dataId;
	}

	/**
	 * Append unique id, designId, dataId to buffer.
	 * 
	 * @param buffer
	 */
	protected void append(StringBuffer buffer) {
		buffer.append('/');
		if (uid != -1) {
			buffer.append(uid);
			buffer.append('.');
		}
		buffer.append(designId);
		if (dataId != null) {
			buffer.append('(');
			dataId.append(buffer);
			buffer.append(')');
		}
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		append(buffer);
		return buffer.toString();
	}

	/**
	 * Append uniqueID to buffer.
	 * 
	 * @param buffer
	 */
	protected void appendUniqueID(StringBuffer buffer) {
		InstanceID pid = parentId;
		if (pid != null) {
			pid.appendUniqueID(buffer);
		}
		append(buffer);
	}

	/**
	 * Returns a string representation of the uniqueID.
	 * 
	 * @return
	 */
	public String toUniqueString() {
		StringBuffer buffer = new StringBuffer();
		appendUniqueID(buffer);
		return buffer.toString();
	}

	/**
	 * Parse the input string into an InstanceId object.
	 * 
	 * @param instanceId the input string to parse
	 * @return InstantceID object
	 */
	public static InstanceID parse(String instanceId) {
		if (instanceId == null) {
			return null;
		}
		return parse(instanceId.toCharArray(), 0, instanceId.length());
	}

	/**
	 * Parse the input char buffer into an InstanceId object.
	 * 
	 * @param buffer the input char buffer to parse
	 * @param offset offset of the buffer
	 * @param length length of the buffer
	 * @return InstanceID object
	 */
	public static InstanceID parse(char[] buffer, int offset, int length) {
		DataID dataId = null;
		// search the last '(' to see if we have data id
		int ptr = offset + length - 1;
		if (ptr >= offset && buffer[ptr] == ')') {
			ptr--; // skip the first ')'
			while (ptr >= offset && buffer[ptr] != '(') {
				ptr--;
			}
			if (ptr < offset || buffer[ptr] != '(') {
				// it should be a data Id but it isn't return null
				return null;
			}
			// we found the data Id
			dataId = DataID.parse(buffer, ptr + 1, offset + length - ptr - 2);
			if (dataId == null) {
				// it should be an data id, but it returns null,
				return null;
			}
			ptr--; // skip the current '('
			length = ptr - offset + 1;
		}
		// the remain characters are instance id.
		// search the parent
		while (ptr >= offset && buffer[ptr] != '/') {
			ptr--;
		}
		if (ptr >= offset && buffer[ptr] == '/') {
			long uid = -1;
			long designId = -1;
			String strId = new String(buffer, ptr + 1, offset + length - ptr - 1);
			int dotPos = strId.indexOf('.');
			if (dotPos != -1) {
				uid = Long.parseLong(strId.substring(0, dotPos));
				designId = Long.parseLong(strId.substring(dotPos + 1));
			} else {
				designId = Long.parseLong(strId);
			}
			ptr--; // skip the current '/'
			if (ptr >= offset) {
				length = ptr - offset + 1;
				InstanceID parent = InstanceID.parse(buffer, offset, length);
				if (parent != null) {
					return new InstanceID(parent, uid, designId, dataId);
				}
			} else {
				return new InstanceID(null, uid, designId, dataId);
			}
		}
		return null;
	}
}
