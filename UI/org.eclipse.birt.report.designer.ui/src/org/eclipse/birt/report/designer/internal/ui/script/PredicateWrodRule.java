/* Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.script;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

class PredicateWordRule extends WordRule implements IPredicateRule {

	public PredicateWordRule(IWordDetector detector, IToken defaultToken) {
		super(detector, defaultToken);
	}

	@Override
	public IToken getSuccessToken() {
		return Token.UNDEFINED;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return null;
	}

	void addWords(String[] tokens, IToken token) {
		for (int i = 0; i < tokens.length; i++) {
			addWord(tokens[i], token);
		}
	}
}
