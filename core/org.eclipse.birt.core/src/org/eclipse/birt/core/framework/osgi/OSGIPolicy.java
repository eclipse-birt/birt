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

package org.eclipse.birt.core.framework.osgi;

import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class OSGIPolicy extends Policy {

	// The policy that this EclipsePolicy is replacing
	private Policy policy;

	// The set of URLs to give AllPermissions to; this is the set of bootURLs
	private URL[] urls;

	// The AllPermissions collection
	private PermissionCollection allPermissions;

	// The AllPermission permission
	Permission allPermission = new AllPermission();

	OSGIPolicy(Policy policy, URL[] urls) {
		this.policy = policy;
		this.urls = urls;
		allPermissions = new PermissionCollection() {

			private static final long serialVersionUID = 3258131349494708277L;

			// A simple PermissionCollection that only has AllPermission
			public void add(Permission permission) {
				// no adding to this policy
			}

			public boolean implies(Permission permission) {
				return true;
			}

			public Enumeration elements() {
				return new Enumeration() {

					int cur = 0;

					public boolean hasMoreElements() {
						return cur < 1;
					}

					public Object nextElement() {
						if (cur == 0) {
							cur = 1;
							return allPermission;
						}
						throw new NoSuchElementException();
					}
				};
			}
		};
	}

	public PermissionCollection getPermissions(CodeSource codesource) {
		if (contains(codesource.getLocation()))
			return allPermissions;
		return policy == null ? allPermissions : policy.getPermissions(codesource);
	}

	public PermissionCollection getPermissions(ProtectionDomain domain) {
		if (contains(domain.getCodeSource().getLocation()))
			return allPermissions;
		return policy == null ? allPermissions : policy.getPermissions(domain);
	}

	public boolean implies(ProtectionDomain domain, Permission permission) {
		if (contains(domain.getCodeSource().getLocation()))
			return true;
		return policy == null ? true : policy.implies(domain, permission);
	}

	public void refresh() {
		if (policy != null)
			policy.refresh();
	}

	private boolean contains(URL url) {
		// Check to see if this URL is in our set of URLs to give AllPermissions
		// to.
		for (int i = 0; i < urls.length; i++) {
			// We do simple equals test here because we assume the URLs will be
			// the same objects.
			if (urls[i] == url)
				return true;
		}
		return false;
	}
}
