/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.build.framework;

public class FrameworkException extends Exception {

	private static final long serialVersionUID = -5458164264848066689L;

	public FrameworkException(String message) {
		super(message);
	}

	public FrameworkException(String message, Exception cause) {
		super(message, cause);
	}

	public FrameworkException(Exception cause) {
		super(cause);
	}

}
