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
/*
 * Java(TM) OLAP Interface
 */

package javax.olap;

public class OLAPWarning extends OLAPException {

	private static final long serialVersionUID = 1L;

	public OLAPWarning(String reason) {
		super(reason);
	}

	public OLAPWarning(String reason, String OLAPState) {
		super(reason, OLAPState);
	}

	public OLAPWarning(String reason, String OLAPState, int vendorCode) {
		super(reason, OLAPState, vendorCode);
	}
}
