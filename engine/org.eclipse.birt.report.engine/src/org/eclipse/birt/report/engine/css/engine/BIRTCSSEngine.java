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

package org.eclipse.birt.report.engine.css.engine;

import org.apache.batik.css.parser.Parser;
import org.eclipse.birt.report.engine.css.engine.value.css.FontFamilyManager;
import org.w3c.dom.css.CSSValue;

public class BIRTCSSEngine extends CSSEngine {

	/**
	 * Creates a new SVGCSSEngine.
	 *
	 * @param doc The associated document.
	 * @param uri The document URI.
	 * @param p   The CSS parser to use.
	 * @param ctx The CSS context.
	 */
	public BIRTCSSEngine() {
		super(new Parser(), new BIRTPropertyManagerFactory(), new BIRTContext());
	}

	static class BIRTContext implements CSSContext {

		@Override
		public CSSValue getSystemColor(String ident) {
			return SystemColorSupport.getSystemColor(ident);
		}

		/**
		 * Returns the default font family.
		 */
		@Override
		public CSSValue getDefaultFontFamily() {
			return FontFamilyManager.DEFAULT_VALUE;
		}

		/**
		 * Returns the size of a px CSS unit in millimeters.
		 */
		@Override
		public float getPixelUnitToMillimeter() {
			return 0.26458333333333333333333333333333f; // 96dpi
		}

		/**
		 * Returns the medium font size.
		 */
		@Override
		public float getMediumFontSize() {
			// 12pt (72pt == 1in)
			return 12f;
		}

		/**
		 * Returns a lighter font-weight.
		 */
		@Override
		public float getLighterFontWeight(float f) {
			// Round f to nearest 100...
			int weight = ((int) ((f + 50) / 100)) * 100;
			switch (weight) {
			case 100:
				return 100;
			case 200:
				return 100;
			case 300:
				return 200;
			case 400:
				return 300;
			case 500:
				return 400;
			case 600:
				return 400;
			case 700:
				return 400;
			case 800:
				return 400;
			case 900:
				return 400;
			default:
				throw new IllegalArgumentException("Bad Font Weight: " + f); //$NON-NLS-1$
			}
		}

		/**
		 * Returns a bolder font-weight.
		 */
		@Override
		public float getBolderFontWeight(float f) {
			// Round f to nearest 100...
			int weight = ((int) ((f + 50) / 100)) * 100;
			switch (weight) {
			case 100:
				return 600;
			case 200:
				return 600;
			case 300:
				return 600;
			case 400:
				return 600;
			case 500:
				return 600;
			case 600:
				return 700;
			case 700:
				return 800;
			case 800:
				return 900;
			case 900:
				return 900;
			default:
				throw new IllegalArgumentException("Bad Font Weight: " + f); //$NON-NLS-1$
			}
		}

	}
}
