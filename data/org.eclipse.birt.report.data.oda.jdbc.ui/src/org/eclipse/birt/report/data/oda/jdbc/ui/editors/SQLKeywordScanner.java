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

import java.util.ArrayList;

import org.eclipse.birt.report.data.oda.jdbc.ui.util.ColorManager;
import org.eclipse.birt.report.data.oda.jdbc.utils.ISQLSyntax;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * TODO: Please document
 *
 * @version $Revision: 1.4 $ $Date: 2009/07/07 06:50:16 $
 */

public class SQLKeywordScanner extends RuleBasedScanner implements ISQLSyntax {
	/**
	 *
	 */
	public SQLKeywordScanner() {
		super();
		IToken sqlKeywordsToken = new Token(new TextAttribute(ColorManager.getColor(127, 0, 85), null, SWT.BOLD));
		ArrayList rules = new ArrayList();
		rules.add(new SQLKeywordRule(sqlKeywordsToken, reservedwords));
		rules.add(new SQLKeywordRule(sqlKeywordsToken, types));
		rules.add(new SQLKeywordRule(sqlKeywordsToken, constants));
		rules.add(new SQLKeywordRule(sqlKeywordsToken, functions));
		rules.add(new SQLKeywordRule(sqlKeywordsToken, predicates));

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new IWhitespaceDetector() {

			@Override
			public boolean isWhitespace(char c) {
				return Character.isWhitespace(c);
			}
		}));

		setRules((IRule[]) rules.toArray(new IRule[rules.size()]));
		this.setDefaultReturnToken(
				new Token(new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND))));
	}

}
