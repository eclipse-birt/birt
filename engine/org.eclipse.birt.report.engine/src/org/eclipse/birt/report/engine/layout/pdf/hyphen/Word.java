/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.pdf.hyphen;

public class Word {
	protected int start;
	protected int end;
	protected String text;

	private boolean keepLastSHY = true;

	/**
	 * Should a trailing SHY symbol be kept or omitted?
	 */
	public boolean isKeepLastSHY() {
		return keepLastSHY;
	}

	public void setKeepLastSHY(boolean keepLastSHY) {
		this.keepLastSHY = keepLastSHY;
	}

	public Word(String text, int start, int end) {
		this.text = text;
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return this.start;
	}

	public int getEnd() {
		return this.end;
	}

	public String getValue() {
		return text.substring(start, end);
	}

	public int getLength() {
		return end - start;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("value: [" + getValue() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		str.append(" StartIndex:" + start); //$NON-NLS-1$
		str.append(" EndIndex:" + end); //$NON-NLS-1$
		return str.toString();
	}
}
