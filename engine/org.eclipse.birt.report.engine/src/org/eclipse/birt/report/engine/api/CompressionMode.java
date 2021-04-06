/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	private CompressionMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
