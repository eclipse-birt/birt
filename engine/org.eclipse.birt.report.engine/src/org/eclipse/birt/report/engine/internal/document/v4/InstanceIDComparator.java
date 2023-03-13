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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.util.Comparator;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;

public class InstanceIDComparator implements Comparator {

	public InstanceIDComparator() {
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		if (arg0 == arg1) {
			return 0;
		}

		InstanceID aid;
		InstanceID bid;
		if (arg0 instanceof InstanceIndex) {
			aid = ((InstanceIndex) arg0).getInstanceID();
		} else if (arg0 instanceof InstanceID) {
			aid = (InstanceID) arg0;
		} else {
			throw new IllegalArgumentException();
		}

		if (arg1 instanceof InstanceIndex) {
			bid = ((InstanceIndex) arg1).getInstanceID();
		} else if (arg1 instanceof InstanceID) {
			bid = (InstanceID) arg1;
		} else {
			throw new IllegalArgumentException();
		}

		long uid_a = aid.getUniqueID();
		long uid_b = bid.getUniqueID();
		if (uid_a == uid_b) {
			return 0;
		}
		if (uid_a < uid_b) {
			return -1;
		}
		return 1;

		// return compareInstanceID( aid, bid );
	}
}
