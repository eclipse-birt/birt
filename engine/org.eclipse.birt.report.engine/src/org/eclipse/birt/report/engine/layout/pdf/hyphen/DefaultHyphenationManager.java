/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.pdf.hyphen;

public class DefaultHyphenationManager implements IHyphenationManager {

	@Override
	public Hyphenation getHyphenation(String word) {
		int length = word.length();
		int[] indexes = new int[length + 1];
		for (int i = 0; i <= length; i++) {
			indexes[i] = i;
		}
		return new Hyphenation(word, indexes);
	}

}
