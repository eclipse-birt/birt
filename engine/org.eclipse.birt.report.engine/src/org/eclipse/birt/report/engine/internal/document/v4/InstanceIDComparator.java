
package org.eclipse.birt.report.engine.internal.document.v4;

import java.util.Comparator;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;

public class InstanceIDComparator implements Comparator {

	public InstanceIDComparator() {
	}

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
