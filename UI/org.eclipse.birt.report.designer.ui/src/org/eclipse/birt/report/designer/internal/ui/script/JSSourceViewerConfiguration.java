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
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * Sets JS configuration the editor needs
 * 
 */
public class JSSourceViewerConfiguration extends SourceViewerConfiguration
{

	private RuleBasedScanner scanner;
	private JSSyntaxContext context;
	private IPreferenceStore preferenceStore;
	private Color foregroundColor;

	public JSSourceViewerConfiguration( )
	{
		this( new JSSyntaxContext( ),
				new ScopedPreferenceStore( new InstanceScope( ),
						"org.eclipse.ui.editors" ) );
	}

	public JSSourceViewerConfiguration( IPreferenceStore preferenceStore )
	{
		this( new JSSyntaxContext( ), preferenceStore );
	}

	public JSSourceViewerConfiguration( JSSyntaxContext context )
	{
		this( context, new ScopedPreferenceStore( new InstanceScope( ),
				"org.eclipse.ui.editors" ) );
	}

	public JSSourceViewerConfiguration( JSSyntaxContext context,
			IPreferenceStore preferenceStore )
	{
		this.context = context;
		this.preferenceStore = preferenceStore;
	}

	/**
	 * gets color for a given category
	 * 
	 * @param categoryColor
	 * @return Color
	 */
	public static Color getColorByCategory( String categoryColor )
	{
		// String rgbString = getRgbString( categoryColor );
		// if ( rgbString.length( ) <= 0 )
		// {
		//			rgbString = "0,0,0"; //$NON-NLS-1$
		// }
		// RGB rgbVal = StringConverter.asRGB( rgbString );
		// return ColorManager.getColor( rgbVal );
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
			scanner.setDefaultReturnToken( new Token( new TextAttribute( getForegroundColor( preferenceStore ) ) ) );
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

		NonRuleBasedDamagerRepairer commentRepairer = new NonRuleBasedDamagerRepairer( new TextAttribute( getColorByCategory( PreferenceNames.P_COMMENT_COLOR ) ) );
		reconciler.setDamager( commentRepairer, JSPartitionScanner.JS_COMMENT );
		reconciler.setRepairer( commentRepairer, JSPartitionScanner.JS_COMMENT );

		NonRuleBasedDamagerRepairer stringRepairer = new NonRuleBasedDamagerRepairer( new TextAttribute( getColorByCategory( PreferenceNames.P_STRING_COLOR ) ) );
		reconciler.setDamager( stringRepairer, JSPartitionScanner.JS_STRING );
		reconciler.setRepairer( stringRepairer, JSPartitionScanner.JS_STRING );

		NonRuleBasedDamagerRepairer keywordRepairer = new NonRuleBasedDamagerRepairer( new TextAttribute( getColorByCategory( PreferenceNames.P_KEYWORD_COLOR ),
				null,
				SWT.BOLD ) );
		reconciler.setDamager( keywordRepairer, JSPartitionScanner.JS_KEYWORD );
		reconciler.setRepairer( keywordRepairer, JSPartitionScanner.JS_KEYWORD );

		return reconciler;
	}

	private static Color getRgbString( String name )
	{
		String rgbStr = null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.source.SourceViewerConfiguration#getContentAssistant
	 * (org.eclipse.jface.text.source.ISourceViewer)
	 */
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

	public void resetScannerColoer( )
	{
		if ( scanner != null && preferenceStore != null )
		{
			scanner.setDefaultReturnToken( new Token( new TextAttribute( getForegroundColor( preferenceStore ) ) ) );
		}
	}

	private Color getForegroundColor( IPreferenceStore preferenceStore )
	{
		Color color = preferenceStore.getBoolean( AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT ) ? null
				: createColor( preferenceStore,
						AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND,
						Display.getCurrent( ) );
		if ( foregroundColor != null )
		{
			foregroundColor.dispose( );
		}
		foregroundColor = color;
		return color;
	}

	/**
	 * Creates a color from the information stored in the given preference
	 * store. Returns <code>null</code> if there is no such information
	 * available.
	 */
	private Color createColor( IPreferenceStore store, String key,
			Display display )
	{
		RGB rgb = null;
		if ( store.contains( key ) )
		{
			if ( store.isDefault( key ) )
			{
				rgb = PreferenceConverter.getDefaultColor( store, key );
			}
			else
			{
				rgb = PreferenceConverter.getColor( store, key );
			}
			if ( rgb != null )
			{
				return new Color( display, rgb );
			}
		}
		return null;
	}

	public void dispose( )
	{
		if ( foregroundColor != null )
		{
			foregroundColor.dispose( );
		}
	}
}