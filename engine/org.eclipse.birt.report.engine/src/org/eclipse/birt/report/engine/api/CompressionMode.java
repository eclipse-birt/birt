/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import java.util.zip.Deflater;

/**
 * Specify the compression mode to generate ooxml file.
 */
public enum CompressionMode {

	BEST_COMPRESSION(Deflater.BEST_COMPRESSION), BEST_SPEED(Deflater.BEST_SPEED),
	NO_COMPRESSION(Deflater.NO_COMPRESSION);

	private int value;

	CompressionMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
