/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.script;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * Sets JS configuration the editor needs
 * 
 */
public class JSSourceViewerConfiguration extends SourceViewerConfiguration
{

	private RuleBasedScanner scanner;
	protected JSSyntaxContext context;

	public JSSourceViewerConfiguration( )
	{
		this( new JSSyntaxContext( ) );
	}

	public JSSourceViewerConfiguration( JSSyntaxContext context )
	{
		this.context = context;
	}

	/**
	 * gets color for a given category
	 * 
	 * @param categoryColor
	 * @return Color
	 */
	public static Color getColorByCategory( String categoryColor )
	{
		return getRgbString( categoryColor );
	}

	/**
	 * @see SourceViewerConfiguration#getConfiguredContentTypes(ISourceViewer)
	 */
	public String[] getConfiguredContentTypes( ISourceViewer sourceViewer )
	{
		return new String[]{
				IDocument.DEFAULT_CONTENT_TYPE,
				JSPartitionScanner.JS_COMMENT,
				JSPartitionScanner.JS_KEYWORD,
				JSPartitionScanner.JS_STRING
		};
	}

	/**
	 * Gets default scanner
	 * 
	 * @return scanner
	 */
	protected RuleBasedScanner getDefaultScanner( )
	{
		if ( scanner == null )
		{
			scanner = new JSScanner( );
			scanner.setDefaultReturnToken( new Token( UIUtil.getAttributeFor( ReportPlugin.EXPRESSION_CONTENT_COLOR_PREFERENCE ) ) );
		}
		return scanner;
	}

	/**
	 * @see SourceViewerConfiguration#getPresentationReconciler(ISourceViewer)
	 */
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer )
	{
		PresentationReconciler reconciler = new PresentationReconciler( );

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer( getDefaultScanner( ) );
		reconciler.setDamager( dr, IDocument.DEFAULT_CONTENT_TYPE );
		reconciler.setRepairer( dr, IDocument.DEFAULT_CONTENT_TYPE );

		NonRuleBasedDamagerRepairer commentRepairer = new NonRuleBasedDamagerRepairer( UIUtil.getAttributeFor( ReportPlugin.EXPRESSION_COMMENT_COLOR_PREFERENCE ) );
		reconciler.setDamager( commentRepairer, JSPartitionScanner.JS_COMMENT );
		reconciler.setRepairer( commentRepairer, JSPartitionScanner.JS_COMMENT );

		NonRuleBasedDamagerRepairer stringRepairer = new NonRuleBasedDamagerRepairer( UIUtil.getAttributeFor( ReportPlugin.EXPRESSION_STRING_COLOR_PREFERENCE ) );
		reconciler.setDamager( stringRepairer, JSPartitionScanner.JS_STRING );
		reconciler.setRepairer( stringRepairer, JSPartitionScanner.JS_STRING );

		NonRuleBasedDamagerRepairer keywordRepairer = new NonRuleBasedDamagerRepairer( UIUtil.getAttributeFor( ReportPlugin.EXPRESSION_KEYWORD_COLOR_PREFERENCE ) );
		reconciler.setDamager( keywordRepairer, JSPartitionScanner.JS_KEYWORD );
		reconciler.setRepairer( keywordRepairer, JSPartitionScanner.JS_KEYWORD );

		return reconciler;
	}

	private static Color getRgbString( String name )
	{
		if ( PreferenceNames.P_COMMENT_COLOR.equals( name ) )
		{
			//rgbStr = "63,127,95"; //$NON-NLS-1$
			return ReportColorConstants.JSCOMMENTCOLOR;
		}
		else if ( PreferenceNames.P_STRING_COLOR.equals( name ) )
		{
			//rgbStr = "42,0,255"; //$NON-NLS-1$
			return ReportColorConstants.JSSTRINGCOLOR;
		}
		else if ( PreferenceNames.P_KEYWORD_COLOR.equals( name ) )
		{
			//rgbStr = "127,0,85"; //$NON-NLS-1$
			return ReportColorConstants.JSKEYWORDCOLOR;
		}
		else if ( PreferenceNames.P_LINENUMBER_COLOR.equals( name ) )
		{
			//rgbStr = "127,127,127"; //$NON-NLS-1$
			return ReportColorConstants.JSLINENUMBERCOLOR;
		}
		return ReportColorConstants.ReportForeground;
	}

	public IContentAssistant getContentAssistant( ISourceViewer sourceViewer )
	{
		ContentAssistant assistant = new ContentAssistant( );
		assistant.setContentAssistProcessor( new JSCompletionProcessor( context ),
				IDocument.DEFAULT_CONTENT_TYPE );
		assistant.enableAutoActivation( true );
		assistant.setAutoActivationDelay( 500 );
		assistant.setProposalPopupOrientation( IContentAssistant.PROPOSAL_OVERLAY );
		return assistant;
	}

	public void resetScannerColor( )
	{
		if ( scanner != null )
		{
			scanner.setDefaultReturnToken( new Token( UIUtil.getAttributeFor( ReportPlugin.EXPRESSION_CONTENT_COLOR_PREFERENCE ) ) );
		}
	}
	
	public static void updateSourceFont( SourceViewer sourceViewer )
	{
		if ( Platform.getOS( ).equals( Platform.WS_WIN32 ) )
		{
			Font font = sourceViewer.getTextWidget( ).getFont( );
			FontData data = font.getFontData( )[0];
			// BIRT-1113 Always use monospaced font
			Font newFont = FontManager.getFont( "Courier", //$NON-NLS-1$
					data.getHeight( ),
					data.getStyle( ) );
			sourceViewer.getTextWidget( ).setFont( newFont );
		}
	}
}