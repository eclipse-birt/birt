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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/05 07:25:00 $
 */

public class SQLSourceViewerConfiguration extends SourceViewerConfiguration
{

	/**
	 *  
	 */
	public SQLSourceViewerConfiguration( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer )
	{
		PresentationReconciler reconciler = new PresentationReconciler( );
		DefaultDamagerRepairer df = new DefaultDamagerRepairer( new SQLKeywordScanner( ) );
		reconciler.setDamager( df, IDocument.DEFAULT_CONTENT_TYPE );
		reconciler.setRepairer( df, IDocument.DEFAULT_CONTENT_TYPE );
		df = new DefaultDamagerRepairer( new SQLCommentScanner( ) );
		reconciler.setDamager( df, SQLPartitionScanner.SINGLE_LINE_COMMENT1 );
		reconciler.setRepairer( df, SQLPartitionScanner.SINGLE_LINE_COMMENT1 );
		df = new DefaultDamagerRepairer( new SQLCommentScanner( ) );
		reconciler.setDamager( df, SQLPartitionScanner.SINGLE_LINE_COMMENT2 );
		reconciler.setRepairer( df, SQLPartitionScanner.SINGLE_LINE_COMMENT2 );
		df = new DefaultDamagerRepairer( new SQLCommentScanner( ) );
		reconciler.setDamager( df, SQLPartitionScanner.MULTI_LINE_COMMENT );
		reconciler.setRepairer( df, SQLPartitionScanner.MULTI_LINE_COMMENT );
		return reconciler;
	}
}