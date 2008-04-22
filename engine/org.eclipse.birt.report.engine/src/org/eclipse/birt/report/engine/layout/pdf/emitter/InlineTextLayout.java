/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.HashSet;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.TextArea;
import org.eclipse.birt.report.engine.layout.pdf.emitter.TextCompositor;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;


public class InlineTextLayout extends ContainerLayout
{	
	private LineLayout lineLM;
	
	private InlineContainerLayout inlineContainerLM = null;
	
	/**
	 * Checks if the compositor needs to pause.
	 */
	private boolean pause = false;

	private TextCompositor comp = null;

	private ITextContent textContent = null;
	
	private boolean isInline;
	
	private static HashSet splitChar = new HashSet();
	
	static 
	{
		splitChar.add( new Character( ' ' ) );
		splitChar.add( new Character( '\r') );
		splitChar.add( new Character( '\n') );
	};

	public InlineTextLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content )
	{
		super( context, parentContext, content );
		lineLM = (LineLayout) parentContext;

		ITextContent textContent = (ITextContent) content;
		isInline = PropertyUtil.isInlineElement( content );
		lineLM.setTextIndent( textContent );
		String text = textContent.getText( );
		if ( text != null && text.length( ) != 0 )
		{
			transform( textContent );
			this.textContent = textContent;
			comp = new TextCompositor( textContent, context.getFontManager( ),
					context.getBidiProcessing( ),
					context.getFontSubstitution( ), context.getTextWrapping( ),
					context.isEnableHyphenation( ), context.getLocale( ) );
		}
	}
	
	public boolean addArea(AbstractArea area)
	{
		return false;
	}
	
	protected void createRoot( )
	{
		
	}

	public void layout( )
	{
		while(layoutChildren());
	}

	protected boolean layoutChildren( )
	{
		if ( null == textContent )
			return false;
		while ( comp.hasNextArea( ) )
		{
			TextArea area = comp.getNextArea( getFreeSpace( ) );
			//for a textArea which just has a line break. We should not add TextArea into the line.
			if ( !area.isBlankLine( ) )
			{
				addTextArea( area );
				comp.setNewLineStatus( false );
			}
			if ( area.isLineBreak( ) )
			{
				if ( newLine( ) )
				{
					comp.setNewLineStatus( true );
				}
				else
				{
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean checkAvailableSpace( )
	{
		return false;
	}

	public void addTextArea( AbstractArea textArea )
	{
		//FIXME support inline text border.
//		if ( isInline )
//		{
//			if ( inlineContainerLM == null )
//			{
//				inlineContainerLM = new InlineContainerLayout( context,
//						(ContainerLayout) lineLM, content );
//			}
//			inlineContainerLM.initialize( );
//			inlineContainerLM.addArea( textArea );
//		}
//		else
		{
			lineLM.addArea( textArea );	
		}
	}
	
	/**
	 * true if succeed to new a line.
	 */
	public boolean newLine( )
	{
//		if ( isInline )
//		{
//			if ( inlineContainerLM != null )
//			{
//				return inlineContainerLM.endLine( );
//			}
//			return false;
//		}
//		else
		{
			return lineLM.endLine( );
		}
	}

	public int getFreeSpace( )
	{
		return lineLM.getCurrentMaxContentWidth( );
	}


	public void transform( ITextContent textContent )
	{
		String transformType = textContent.getComputedStyle( )
				.getTextTransform( );
		if ( transformType.equalsIgnoreCase( "uppercase" ) ) //$NON-NLS-1$
		{
			textContent.setText( textContent.getText( ).toUpperCase( ) );
		}
		else if ( transformType.equalsIgnoreCase( "lowercase" ) ) //$NON-NLS-1$
		{
			textContent.setText( textContent.getText( ).toLowerCase( ) );
		}
		else if ( transformType.equalsIgnoreCase( "capitalize" ) ) //$NON-NLS-1$
		{
			textContent.setText( capitalize( textContent.getText( ) ) );
		}
		
		ArabicShaping shaping = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
		try
		{
			String shapingText =  shaping.shape( textContent.getText( ));
			textContent.setText(shapingText);
		}
		catch ( ArabicShapingException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	private String capitalize( String text )
	{
		boolean capitalizeNextChar = true;
		char[] array = text.toCharArray( );
		for ( int i = 0; i < array.length; i++ )
		{
			Character c = new Character( text.charAt( i ) );
			if ( splitChar.contains( c ) )
				capitalizeNextChar = true;
			else if (capitalizeNextChar)
			{
				array[i] = Character.toUpperCase( array[i] );
				capitalizeNextChar = false;
			}
		}
		return new String(array);
	}

	protected void closeLayout( )
	{
	}

	protected void initialize( )
	{
	}

}
