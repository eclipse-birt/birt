/* Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public IToken getSuccessToken() {
		return Token.UNDEFINED;
	}

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return null;
	}

	void addWords(String[] tokens, IToken token) {
		for (int i = 0; i < tokens.length; i++) {
			addWord(tokens[i], token);
		}
	}
}