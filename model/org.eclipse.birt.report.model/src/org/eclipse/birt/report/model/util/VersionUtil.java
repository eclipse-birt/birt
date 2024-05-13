/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Utility class to do the operations of the design file version.
 */

public class VersionUtil {

	private static final int SUPPORTED_VERSION_TOKEN_LENGTH = 4;

	private static final int[] expoArray = { 1000000, 10000, 100, 1 };

	/** property: report design file version 0 */
	public final static int VERSION_0 = 0;

	/** property: report design file version 1.0.0 */
	public final static int VERSION_1_0_0 = 1000000;

	/** property: report design file version 3.0.0 */
	public final static int VERSION_3_0_0 = 3000000;

	/** property: report design file version 3.1.0 */
	public final static int VERSION_3_1_0 = 3010000;

	/** property: report design file version 3.2.0 */
	public final static int VERSION_3_2_0 = 3020000;

	/** property: report design file version 3.2.1 */
	public final static int VERSION_3_2_1 = 3020100;

	/** property: report design file version 3.2.2 */
	public final static int VERSION_3_2_2 = 3020200;

	/** property: report design file version 3.2.3 */
	public final static int VERSION_3_2_3 = 3020300;

	/** property: report design file version 3.2.4 */
	public final static int VERSION_3_2_4 = 3020400;

	/** property: report design file version 3.2.6 */
	public final static int VERSION_3_2_6 = 3020600;

	/** property: report design file version 3.2.7 */
	public final static int VERSION_3_2_7 = 3020700;

	/** property: report design file version 3.2.8 */
	public final static int VERSION_3_2_8 = 3020800;

	/** property: report design file version 3.2.9 */
	public final static int VERSION_3_2_9 = 3020900;

	/** property: report design file version 3.2.10 */
	public final static int VERSION_3_2_10 = 3021000;

	/** property: report design file version 3.2.11 */
	public final static int VERSION_3_2_11 = 3021100;

	/** property: report design file version 3.2.12 */
	public final static int VERSION_3_2_12 = 3021200;

	/** property: report design file version 3.2.13 */
	public final static int VERSION_3_2_13 = 3021300;

	/** property: report design file version 3.2.14 */
	public final static int VERSION_3_2_14 = 3021400;

	/** property: report design file version 3.2.15 */
	public final static int VERSION_3_2_15 = 3021500;

	/** property: report design file version 3.2.16 */
	public final static int VERSION_3_2_16 = 3021600;

	/** property: report design file version 3.2.17 */
	public final static int VERSION_3_2_17 = 3021700;

	/** property: report design file version 3.2.18 */
	public final static int VERSION_3_2_18 = 3021800;

	/** property: report design file version 3.2.19 */
	public final static int VERSION_3_2_19 = 3021900;

	/** property: report design file version 3.2.20 */
	public final static int VERSION_3_2_20 = 3022000;

	/** property: report design file version 3.2.21 */
	public static final int VERSION_3_2_21 = 3022100;

	/** property: report design file version 3.2.22 */
	public static final int VERSION_3_2_22 = 3022200;

	/** property: report design file version 3.2.23 */
	public static final int VERSION_3_2_23 = 3022300;

	/** property: report design file version 3.2.24 */
	public static final int VERSION_3_2_24 = 3022400;

	/** property: report design file version 3.2.25 */
	public static final int VERSION_3_2_25 = 3022500;

	/**
	 *
	 * @param version
	 * @return the parsed version number
	 * @throws IllegalArgumentException thrown when the version string is illegal
	 */

	public static int parseVersion(String version) throws IllegalArgumentException {
		if (StringUtil.isBlank(version)) {
			return 0;
		}

		// parse the version string, for example
		// 3.1.2(.0) -- 3010200, two byte for one version token

		String[] versionTokers = version.split("\\."); //$NON-NLS-1$
		int parsedVersionNumber = 0;
		for (int i = 0; i < versionTokers.length; i++) {
			if (i > SUPPORTED_VERSION_TOKEN_LENGTH) {
				break;
			}

			byte versionShort;
			try {
				versionShort = Byte.parseByte(versionTokers[i]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("the version string is wrong!"); //$NON-NLS-1$
			}
			if (versionShort > 99) {
				throw new IllegalArgumentException("the version string is wrong!"); //$NON-NLS-1$
			}
			parsedVersionNumber += versionShort * expoArray[i];
		}
		// add the parsed version to the cache map

		return parsedVersionNumber;
	}

}
