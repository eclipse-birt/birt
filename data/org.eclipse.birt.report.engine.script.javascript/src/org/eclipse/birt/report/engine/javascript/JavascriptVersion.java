package org.eclipse.birt.report.engine.javascript;

/*******************************************************************************
 * Copyright (c) 2024 Thomas Gutmann.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Handling of the JavaScript version evaluation
 *
 * Contributors: Thomas Gutmann - initial implementation
 *
 * @since 4.16
 *
 *******************************************************************************/
public class JavascriptVersion {

	/**
	 * Constructor
	 */
	public JavascriptVersion() {
		evaluateEcmaScriptVersion();
		evaluateEcmaScriptSecurity();
	}

	/** Valid JavaScript versions */
	/** JavaScript 1.0 */
	private static final int ECMA_SCRIPT_VERSION_1_0 = 100;
	/** JavaScript 1.1 */
	private static final int ECMA_SCRIPT_VERSION_1_1 = 110;
	/** JavaScript 1.2 */
	private static final int ECMA_SCRIPT_VERSION_1_2 = 120;
	/** JavaScript 1.3 */
	private static final int ECMA_SCRIPT_VERSION_1_3 = 130;
	/** JavaScript 1.4 */
	private static final int ECMA_SCRIPT_VERSION_1_4 = 140;
	/** JavaScript 1.5 */
	private static final int ECMA_SCRIPT_VERSION_1_5 = 150;
	/** JavaScript 1.6 */
	private static final int ECMA_SCRIPT_VERSION_1_6 = 160;
	/** JavaScript 1.7 */
	private static final int ECMA_SCRIPT_VERSION_1_7 = 170;
	/** JavaScript 1.8 */
	private static final int ECMA_SCRIPT_VERSION_1_8 = 180;
	/** JavaScript 1.8 */
	private static final int ECMA_SCRIPT_VERSION_ES6 = 200;

	/** System property of the JavaScript version */
	private static final String ECMA_SCRIPT_VERSION_PROPERTY_KEY = "birt.ecmascript.version"; //$NON-NLS-1$

	/** System property of the JavaScript version */
	private static final String ECMA_SCRIPT_SECURITY_PROPERTY_KEY = "birt.ecmascript.security"; //$NON-NLS-1$

	/** Valid keys of the system property */
	private static final String ECMA_SCRIPT_VERSION_1_0_KEY = "1.0"; //$NON-NLS-1$
	private static final String ECMA_SCRIPT_VERSION_1_1_KEY = "1.1"; //$NON-NLS-1$
	private static final String ECMA_SCRIPT_VERSION_1_2_KEY = "1.2"; //$NON-NLS-1$
	private static final String ECMA_SCRIPT_VERSION_1_3_KEY = "1.3"; //$NON-NLS-1$
	private static final String ECMA_SCRIPT_VERSION_1_4_KEY = "1.4"; //$NON-NLS-1$
	private static final String ECMA_SCRIPT_VERSION_1_5_KEY = "1.5"; //$NON-NLS-1$
	private static final String ECMA_SCRIPT_VERSION_1_6_KEY = "1.6"; //$NON-NLS-1$
	private static final String ECMA_SCRIPT_VERSION_1_7_KEY = "1.7"; //$NON-NLS-1$
	private static final String ECMA_SCRIPT_VERSION_1_8_KEY = "1.8"; //$NON-NLS-1$
	private static final String ECMA_SCRIPT_VERSION_ES6_KEY = "ES6"; //$NON-NLS-1$

	private int valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_ES6;

	/** Valid keys of the system property */
	private static final String ECMA_SCRIPT_SECURITY_ENABLED = "on"; //$NON-NLS-1$

	private String configuredECMAScriptVersion;

	private boolean configuredEcmaScriptSecurity = false;

	/**
	 * Get the EMCAScript version number
	 *
	 * @return the ECMAScript version number
	 */
	public int getECMAScriptVersion() {
		return this.valueEcmaScriptVersion;
	}

	/**
	 * Get the configured EMCAScript version of JVM
	 *
	 * @return the ECMAScript version configured at JVM
	 */
	public String getConfiguredECMAScriptVersion() {
		return this.configuredECMAScriptVersion;
	}

	/**
	 * Get the EMCAScript security in use
	 *
	 * @return the ECMAScript security in use
	 */
	public boolean isECMAScriptSecurityEnabled() {
		return this.configuredEcmaScriptSecurity;
	}

	/**
	 * Evaluate the system property to use the javascript security based on
	 * certificates
	 */
	private void evaluateEcmaScriptSecurity() {
		/* System property: -Dbirt.ecmascript.security */
		String configuredEcmaScriptSecurityProperty = System.getProperty(ECMA_SCRIPT_SECURITY_PROPERTY_KEY);
		if (configuredEcmaScriptSecurityProperty != null
				&& configuredEcmaScriptSecurityProperty.equalsIgnoreCase(ECMA_SCRIPT_SECURITY_ENABLED)) {
			this.configuredEcmaScriptSecurity = true;
		}
	}

	/**
	 * Evaluate the system property to set the version number of the Rhino engine
	 */
	private void evaluateEcmaScriptVersion() {

		/* System property: -Dbirt.ecmascript.version */
		configuredECMAScriptVersion = System.getProperty(ECMA_SCRIPT_VERSION_PROPERTY_KEY);
		if (configuredECMAScriptVersion != null) {
			switch (configuredECMAScriptVersion) {
			case ECMA_SCRIPT_VERSION_1_0_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_1_0;
				break;
			case ECMA_SCRIPT_VERSION_1_1_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_1_1;
				break;
			case ECMA_SCRIPT_VERSION_1_2_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_1_2;
				break;
			case ECMA_SCRIPT_VERSION_1_3_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_1_3;
				break;
			case ECMA_SCRIPT_VERSION_1_4_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_1_4;
				break;
			case ECMA_SCRIPT_VERSION_1_5_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_1_5;
				break;
			case ECMA_SCRIPT_VERSION_1_6_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_1_6;
				break;
			case ECMA_SCRIPT_VERSION_1_7_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_1_7;
				break;
			case ECMA_SCRIPT_VERSION_1_8_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_1_8;
				break;
			case ECMA_SCRIPT_VERSION_ES6_KEY:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_ES6;
				break;
			default:
				this.valueEcmaScriptVersion = ECMA_SCRIPT_VERSION_ES6;
			}
		}
	}
}
