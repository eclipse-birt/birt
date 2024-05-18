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

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * TODO: Please document
 *
 * @version $Revision: 1.4 $ $Date: 2008/08/21 09:42:14 $
 */

public class SQLKeywordRule implements IPredicateRule {

	protected IToken token = null;

	protected String[] keywords;

	StringBuffer buf = null;

	/**
	 * Constructor
	 *
	 * @param _token   token
	 * @param keywords keywords
	 *
	 */
	public SQLKeywordRule(IToken _token, String[] keywords) {
		super();
		token = _token;
		buf = new StringBuffer();
		this.keywords = keywords;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.
	 * rules.ICharacterScanner, boolean)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		int column = scanner.getColumn();
		int iCh = ' ';// Default it to space. This will be checked if the column
		// is zero
		// First check whether we are at the first column
		if (column > 0) {
			// if not unread and read the character
			scanner.unread();
			iCh = scanner.read();
		}
		IToken tokenToReturn = Token.UNDEFINED;
		buf.setLength(0);
		// We should only apply this rule if we have a valid preceding character
		if (isValidPrecedingCharacter(iCh)) {
			do {
				// Read the character
				iCh = scanner.read();
				// append it to the buffer
				buf.append(Character.toLowerCase((char) iCh));
			} while (isKeywordStart(buf.toString()) && iCh != ICharacterScanner.EOF);
		}

		// Check whether there is anything in the buffer
		if (buf.length() > 0) {
			// System.out.println("buffer contains " + buf.toString());
			// Check whether the last character read was the EOF character
			// or a space character
			if (isValidTerminatingCharacter(iCh)) {
				// If the length of the buffer is greater than 1
				if (buf.length() > 1) {
					// Strip out the last character
					String sToCompare = buf.substring(0, buf.length() - 1);
					// System.out.println("String is " + sToCompare);

					// Now check whether it is a keyword
					if (isKeyword(sToCompare)) {
						scanner.unread();
						tokenToReturn = token;
					}
				}
			}

			if (tokenToReturn.isUndefined()) {
				// if the token is undefined
				// then just unread the buffer
				unreadBuffer(scanner);
			}
		}

		return tokenToReturn;
	}

	private final boolean isValidPrecedingCharacter(int iCh) {
		return (iCh == ' ' || iCh == '\t' || iCh == '\r' || iCh == '\n' || iCh == '(');
	}

	private final boolean isValidTerminatingCharacter(int iCh) {
		return (isValidPrecedingCharacter(iCh) || iCh == ICharacterScanner.EOF);
	}

	/**
	 * @param scanner
	 */
	private void unreadBuffer(ICharacterScanner scanner) {
		for (int i = buf.length() - 1; i >= 0; i--) {
			scanner.unread();
		}
	}

	private boolean isKeywordStart(String keyword) {
		for (int n = 0; n < keywords.length; n++) {
			// System.out.println("comparing " + keywords[n] + " = " + keyword);
			if (keywords[n].startsWith(keyword)) {
				return true;
			}
		}
		return false;
	}

	private boolean isKeyword(String keyword) {
		for (int n = 0; n < keywords.length; n++) {
			if (keyword.equals(keywords[n])) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
	 */
	@Override
	public IToken getSuccessToken() {
		return token;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.
	 * ICharacterScanner)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}
}
