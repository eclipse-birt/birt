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

package org.eclipse.birt.data.engine.impl.document.stream;

/**
 * 
 */

public class StreamID {

	//
	private String startStream;
	private String subQueryStream;
	private int hashCode;

	/**
	 * 
	 * @param startStream
	 * @param subQueryStream
	 */
	public StreamID(String startStream, String subQueryStream) {
		this.startStream = startStream;
		this.subQueryStream = subQueryStream;
		this.hashCode = (startStream + "_" + subQueryStream).hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (!(o instanceof StreamID))
			return false;
		StreamID temp = (StreamID) o;
		if (twoStringEqual(temp.startStream, this.startStream)
				&& twoStringEqual(temp.subQueryStream, this.subQueryStream))
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.hashCode;
	}

	/**
	 * 
	 * @return
	 */
	public String getStartStream() {
		return this.startStream;
	}

	/**
	 * 
	 * @return
	 */
	public String getSubQueryStream() {
		return this.subQueryStream;
	}

	/**
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	private boolean twoStringEqual(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		if (s1 == null && s2 != null)
			return false;
		if (s1 != null && s2 == null)
			return false;
		return s1.equals(s2);
	}
}
