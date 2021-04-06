/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.2 $ $Date: 2007/02/01 10:58:58 $
 */

public class SQLPartitionScanner extends RuleBasedPartitionScanner {
	public static final String COMMENT = "sql_comment"; //$NON-NLS-1$

	public static final String QUOTE_STRING = "sql_quote_string1";

	/**
	 *  
	 */
	public SQLPartitionScanner() {
		super();
		IToken sqlComment = new Token(COMMENT);
		IToken sqlQuoteString = new Token(QUOTE_STRING);

		ArrayList rules = new ArrayList();
		rules.add(new MultiLineRule("\"", "\"", sqlQuoteString, '\\')); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new MultiLineRule("\'", "\'", sqlQuoteString, '\\')); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new EndOfLineRule("//", sqlComment)); //$NON-NLS-1$
		rules.add(new EndOfLineRule("--", sqlComment)); //$NON-NLS-1$
		rules.add(new MultiLineRule("/*", "*/", sqlComment)); //$NON-NLS-1$ //$NON-NLS-2$

		setPredicateRules((IPredicateRule[]) rules.toArray(new IPredicateRule[rules.size()]));

	}

}