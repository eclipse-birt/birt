/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.Stack;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.wpml.writer.DocWriter;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;

public class DocEmitterImpl extends AbstractEmitterImpl
{

	private static final String OUTPUT_FORMAT = "doc";

	private Stack<IStyle> inlineStyles = new Stack<IStyle>( );

	private boolean inForeign = false;

	private boolean hasPInside = false;

	public DocEmitterImpl( ContentEmitterVisitor contentVisitor )
	{
		this.contentVisitor = contentVisitor;
	}

	public void initialize( IEmitterServices service ) throws EngineException
	{
		super.initialize( service );
		wordWriter = new DocWriter( out );
	}

	public String getOutputFormat( )
	{
		return OUTPUT_FORMAT;
	}

	public void endContainer( IContainerContent container )
	{
		boolean flag = hasForeignParent( container );

		if ( flag )
		{
			if ( !CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase( container
					.getComputedStyle( ).getDisplay( ) ) )
			{
				adjustInline( );
			}
			if ( !styles.isEmpty( ) )
			{
				styles.pop( );
			}
			if ( !inlineStyles.isEmpty( ) )
			{
				inlineStyles.pop( );
			}
			
			if ( !CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase( container
					.getComputedStyle( ).getDisplay( ) ) )
			{
				if ( inForeign && hasPInside )
				{
					context.addContainer( false );
					hasPInside = false;
				}
				else
				{
					context.addContainer( true );
				}
				context.setLastIsTable( true );
			}
		}
	}

	public void startContainer( IContainerContent container )
	{
		boolean flag = hasForeignParent( container );

		if ( flag )
		{
			if ( !CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase( container
					.getComputedStyle( ).getDisplay( ) ) )
			{
				adjustInline( );
			}

			if ( context.isLastTable( ) )
			{
				wordWriter.insertHiddenParagraph( );
			}

			if ( !CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase( container
					.getComputedStyle( ).getDisplay( ) ) )
			{
				styles.push( container.getComputedStyle( ) );
			}
			else
			{
				inlineStyles.push( container.getComputedStyle( ) );
			}
		}
	}

	private boolean hasForeignParent( IContainerContent container )
	{
		IContainerContent con = container;
		while ( con != null )
		{
			if ( con.getParent( ) instanceof IForeignContent )
			{
				return true;
			}
			con = (IContainerContent) con.getParent( );
		}
		return false;
	}

	public void endTable( ITableContent table )
	{
		hasPInside = false;
		endTable( );
		decreaseTOCLevel( table );
	}

	public void startForeign( IForeignContent foreign ) throws BirtException
	{
		if ( IForeignContent.HTML_TYPE.equalsIgnoreCase( foreign.getRawType( ) ) )
		{
			inForeign = true;
			// store the inline state before the HTML foreign.
			boolean inlineBrother = !context.isFirstInline( );
			// the inline state needs be recalculated in the HTML foreign.
			context.endInline( );

			writeToc( foreign );
			HTML2Content.html2Content( foreign );

			context.startCell( );

			if ( context.isLastTable( ) )
			{
				wordWriter.insertHiddenParagraph( );
			}

			wordWriter.startTable( foreign.getComputedStyle( ), context
					.getCurrentWidth( ) );
			wordWriter.startTableRow( -1 );
			wordWriter.startTableCell( context.getCurrentWidth( ), foreign
					.getComputedStyle( ), null );

			contentVisitor.visitChildren( foreign, null );

			adjustInline( );

			wordWriter.endTableCell( context.needEmptyP( ) );

			context.endCell( );
			wordWriter.endTableRow( );
			wordWriter.endTable( );
			context.setLastIsTable( true );
			context.addContainer( true );
			hasPInside = false;
			// restore the inline state after the HTML foreign.
			if ( inlineBrother )
			{
				context.startInline( );
			}
			inForeign = false;
		}
		else
		{
			Object rawValue = foreign.getRawValue( );
			String text = rawValue == null ? "" : rawValue.toString( );
			writeContent( DocEmitterImpl.NORMAL, text, foreign );
		}
	}

	protected void writeContent( int type, String txt, IContent content )
	{
		if ( inForeign )
		{
			hasPInside = true;
		}
		context.addContainer( false );

		InlineFlag inlineFlag = InlineFlag.BLOCK;
		IStyle computedStyle = content.getComputedStyle( );
		IStyle inlineStyle = null;

		if ( "inline".equalsIgnoreCase( content.getComputedStyle( )
				.getDisplay( ) ) )
		{
			if ( context.isFirstInline( ) )
			{
				context.startInline( );
				inlineFlag = InlineFlag.FIRST_INLINE;
				if ( !styles.isEmpty( ) )
				{
					computedStyle = styles.peek( );
				}
			}
			else
				inlineFlag = InlineFlag.MIDDLE_INLINE;
			if ( !inlineStyles.isEmpty( ) )
			{
				inlineStyle = mergeStyles( inlineStyles );
			}
		}
		else
		{
			adjustInline( );
		}

		writeBookmark( content );
		writeToc( content ); // element with Toc contains bookmark
		writeText( type, txt, content, inlineFlag, computedStyle, inlineStyle );
		context.setLastIsTable( false );
	}


	private IStyle mergeStyles( Stack<IStyle> inlineStyles )
	{
		IStyle style = inlineStyles.peek( );

		for ( int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++ )
		{
			if ( isNullValue( style.getProperty( i ) ) )
			{
				style.setProperty( i, null );

				for ( int p = inlineStyles.size( ) - 1; p >= 0; p-- )
				{
					IStyle pstyle = (IStyle) inlineStyles.get( p );

					if ( !isNullValue( pstyle.getProperty( i ) ) )
					{
						style.setProperty( i, pstyle.getProperty( i ) );
						break;
					}
				}
			}
		}
		return style;
	}
}
