/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ooxml;

public class PartAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PartAlreadyExistsException(IPart part) {
		super("Part " + part.getRelativeUri() + "/" + part + " already exists!");
	}
}
