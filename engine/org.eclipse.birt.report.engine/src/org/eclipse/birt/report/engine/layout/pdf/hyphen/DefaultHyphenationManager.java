/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.pdf.hyphen;

public class DefaultHyphenationManager implements IHyphenationManager {

	public Hyphenation getHyphenation(String word) {
		int length = word.length();
		int[] indexes = new int[length + 1];
		for (int i = 0; i <= length; i++) {
			indexes[i] = i;
		}
		return new Hyphenation(word, indexes);
	}

}
