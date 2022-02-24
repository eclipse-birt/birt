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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Framework {

	File pluginFolder;

	ArrayList<Bundle> bundles = new ArrayList<Bundle>();
	ArrayList<Bundle> fragments = new ArrayList<Bundle>();

	public Bundle addBundle(File bundleFile) throws FrameworkException {
		Bundle bundle = new Bundle(bundleFile);
		if (bundle.isFragment()) {
			fragments.add(bundle);
			Bundle host = getBundle(bundle.getHostID());
			if (host != null) {
				host.addFragment(bundle);
			}
		} else {
			bundles.add(bundle);
			for (Bundle fragment : fragments) {
				if (fragment.getHostID().equals(bundle.getBundleID())) {
					bundle.addFragment(fragment);
				}
			}
		}
		return bundle;
	}

	private Bundle getBundle(String bundleId) {
		for (Bundle bundle : bundles) {
			if (bundle.getBundleFile().equals(bundleId)) {
				return bundle;
			}
		}
		return null;
	}

	public List<Bundle> getAllBundles() {
		ArrayList<Bundle> allBundles = new ArrayList<Bundle>(bundles);
		for (Bundle fragment : fragments) {
			String hostId = fragment.getHostID();
			if (getBundle(hostId) == null) {
				allBundles.add(fragment);
			}
		}
		return allBundles;
	}

	public void close() {
		for (Bundle bundle : bundles) {
			bundle.close();
		}
		bundles.clear();
		for (Bundle fragment : fragments) {
			fragment.close();
		}
		fragments.clear();

	}
}
