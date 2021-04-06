
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.util;

/**
 * 
 */

public class Bytes implements Comparable {
	private byte[] b;

	public Bytes(byte[] b) {
		this.b = b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		Bytes other = (Bytes) o;
		if (b.length != other.bytesValue().length) {
			return false;
		}
		for (int i = 0; i < b.length; i++) {
			if (b[i] != other.bytesValue()[i]) {
				return false;
			}
		}
		return true;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < b.length; i++) {
			result = 37 * result + b[i];
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public byte[] bytesValue() {
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		Bytes other = (Bytes) o;
		int minLength = Math.min(b.length, other.bytesValue().length);

		for (int i = 0; i < minLength; i++) {
			if (b[i] > other.bytesValue()[i]) {
				return 1;
			} else if (b[i] < other.bytesValue()[i]) {
				return -1;
			}
		}
		return 0;
	}
}
