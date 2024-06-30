/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.chart.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * This class loads and registers extended classes/implementations of chart
 * model element.
 *
 * @since 3.7
 */

public class ChartDynamicExtension {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/trace"); //$NON-NLS-1$

	protected static final List<EPackage> extendedPackages;

	protected static final Set<EClass> extendedEClasses;

	static {
		// Registers extended chart packages.
		registerExtendedChartPackages();
		extendedPackages = Collections.unmodifiableList(findExtendedPackages());
		extendedEClasses = getEClasses(extendedPackages);
	}

	private static List<EPackage> findExtendedPackages() {
		List<EPackage> pkgs = new ArrayList<>();

		try {
			for (Map.Entry<String, Object> e : PluginSettings.instance().getExtChartModelPackages().entrySet()) {
				pkgs.add((EPackage) e.getValue());
			}
		} catch (ChartException e) {
			logger.log(e);
		}

		return pkgs;
	}

	/**
	 * Checks if specified chart element is an extended classes of chart model.
	 *
	 * @param eObj the instance of chart element.
	 * @return
	 */
	public static boolean isExtended(EObject eObj) {
		return eObj != null && extendedEClasses.contains(eObj.eClass());
	}

	private static Set<EClass> getEClasses(List<EPackage> packages) {
		Set<EClass> set = new HashSet<>();
		for (EPackage pkg : packages) {
			for (EClassifier eClassifier : pkg.getEClassifiers()) {
				if (eClassifier instanceof EClass) {
					set.add((EClass) eClassifier);
				}
			}
		}

		return set;
	}

	private static void registerExtendedChartPackages() {
		try {
			for (Map.Entry<String, Object> e : PluginSettings.instance().getExtChartModelPackages().entrySet()) {
				EPackage.Registry.INSTANCE.put(e.getKey(), e.getValue());
			}
		} catch (ChartException e) {
			logger.log(e);
		}
	}
}
