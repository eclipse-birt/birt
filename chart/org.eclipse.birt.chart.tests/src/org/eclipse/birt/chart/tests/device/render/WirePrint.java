package org.eclipse.birt.chart.tests.device.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

/**
 * @since 3.3
 *
 */
public class WirePrint {

	/**
	 * @param bundle
	 */
	public static void printTree(Bundle bundle) {
		printTree(bundle, new ArrayList<>(), 0);
	}

	private static void printTree(Bundle bundle, List<String> ids, int level) {
		Set<Bundle> dependencies = getBundlePackageImportDependencies(bundle);
		for (Bundle b : dependencies) {
			for (int i = 0; i < level; i++) {
				System.out.print(" "); //$NON-NLS-1$
			}
			System.out.print(b.getSymbolicName() + "." + b.getVersion()); //$NON-NLS-1$
			if (ids.contains(b.getSymbolicName() + "." + b.getVersion())) { //$NON-NLS-1$
				System.out.println(" (R)"); //$NON-NLS-1$
			} else {
				ids.add(b.getSymbolicName() + "." + b.getVersion()); //$NON-NLS-1$
				System.out.println(""); //$NON-NLS-1$
				printTree(bundle, ids, level++);
			}
		}
	}

	/**
	 * @param bundle
	 * @return the wired bundles
	 */
	public static Set<Bundle> getBundlePackageImportDependencies(Bundle bundle) {

		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		if (bundleWiring == null) {
			return Collections.emptySet();
		}

		List<BundleWire> bundleWires = bundleWiring.getRequiredWires(BundleRevision.PACKAGE_NAMESPACE);

		if (bundleWires == null) {
			return Collections.emptySet();
		}

		Set<Bundle> bundleDependencies = new HashSet<Bundle>();

		for (BundleWire bundleWire : bundleWires) {

			BundleRevision provider = bundleWire.getProvider();

			if (provider == null) {
				continue;
			}

			Bundle providerBundle = provider.getBundle();
			BundleRequirement requirement = bundleWire.getRequirement();

			if (requirement != null) {

				Map<String, String> directives = requirement.getDirectives();
				String resolution = directives.get("resolution"); //$NON-NLS-1$

				if ("dynamic".equalsIgnoreCase(resolution)) { //$NON-NLS-1$
					continue;
				}
			}

			bundleDependencies.add(providerBundle);
		}

		return Collections.unmodifiableSet(bundleDependencies);
	}

}
