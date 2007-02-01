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
 * @version $Revision: 1.2 $ $Date: 2007/01/05 07:24:59 $
 */

public class SQLPartitionScanner extends RuleBasedPartitionScanner
{

	public static final String SINGLE_LINE_COMMENT1 = "single_line_comment1"; //$NON-NLS-1$

	public static final String MULTI_LINE_COMMENT = "multi_line_comment"; //$NON-NLS-1$

	public static final String SINGLE_LINE_COMMENT2 = "single_line_comment2"; //$NON-NLS-1$

	/**
	 *  
	 */
	public SQLPartitionScanner( )
	{
		super( );
		IToken sqlSingleLineComment1 = new Token( SINGLE_LINE_COMMENT1 );
		IToken sqlSingleLineComment2 = new Token( SINGLE_LINE_COMMENT2 );
		IToken sqlMultiLineComment = new Token( MULTI_LINE_COMMENT );

		ArrayList rules = new ArrayList( );
		rules.add( new EndOfLineRule( "//", sqlSingleLineComment1 ) ); //$NON-NLS-1$
		rules.add( new EndOfLineRule( "--", sqlSingleLineComment2 ) ); //$NON-NLS-1$
		rules.add( new MultiLineRule( "/*", "*/", sqlMultiLineComment ) ); //$NON-NLS-1$ //$NON-NLS-2$

		setPredicateRules( (IPredicateRule[]) rules.toArray( new IPredicateRule[rules.size( )] ) );

	}

}