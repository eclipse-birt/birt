/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.HashSet;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ITextLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.pdf.text.Compositor;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

/**
 * 
 * This layout mananger implements formatting and locating of text chunk.
 * <p>
 * A text chunk can contain hard line break(such as "\n", "\n\r"). This layout
 * manager splits a text content to many text chunk due to different actual
 * font, soft line break etc.
 */
public class PDFTextLM extends PDFLeafItemLM implements ITextLayoutManager
{

	private PDFLineAreaLM lineLM;

	/**
	 * Checks if the compositor needs to pause.
	 */
	private boolean pause = false;

	private Compositor comp = null;

	private ITextContent textContent = null;

	public PDFTextLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );
		lineLM = (PDFLineAreaLM) getParent( );

		ITextContent textContent = (ITextContent) content;
		String text = textContent.getText( );
		if ( !PropertyUtil.isInlineElement( content ) )
		{
			if ( text == null && "".equals( text ) ) //$NON-NLS-1$
			{
				textContent.setText( " " ); //$NON-NLS-1$
			}
		}
		if ( text != null && text.length( ) != 0 )
		{
			this.textContent = textContent;
			comp = new Compositor( textContent, lineLM.getMaxAvaWidth( ), this );
		}
	}

	protected boolean layoutChildren( )
	{
		if ( null == textContent )
			return false;
		transform( textContent );
		pause = false;
		return comp.compose( );
	}

	public void addSpaceHolder( IArea con )
	{
		lineLM.addArea( con );
	}

	public boolean needPause( )
	{
		return this.pause;
	}

	public void addTextLine( IArea textLine )
	{
		lineLM.addArea( textLine );
	}

	public void newLine( )
	{
		if ( lineLM.endLine( ) )
			pause = false;
		else
			pause = true;
	}

	public int getFreeSpace( )
	{
		int freeSpace = lineLM.getMaxAvaWidth( ) - lineLM.getCurrentIP( );
		return freeSpace;
	}

	public void setBaseLevel( int baseLevel )
	{
		lineLM.setBaseLevel( baseLevel );
	}

	/*
	 * private String collapseWhiteSpace(String text) { StringBuffer str = new
	 * StringBuffer(); boolean whiteSpace = false; for (int i = 0; i <
	 * text.length(); i++) { char c = text.charAt(i); if (whiteSpace) { if (c != ' ') {
	 * whiteSpace = false; str.append(c); } } else { if (c == ' ') { whiteSpace =
	 * true; } str.append(c); } } return str.toString(); }
	 */

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
	}

	private String capitalize( String text )
	{
		HashSet splitChar = new HashSet( );
		splitChar.add( new Character( ' ' ) );
		splitChar.add( new Character( (char) 0x0A ) );
		char[] array = text.toCharArray( );
		int index = 0;
		while ( index < array.length )
		{
			Character c = new Character( text.charAt( index ) );
			while ( splitChar.contains( c ) )
			{
				index++;
				if ( index == array.length )
					return new String( array );
				c = new Character( text.charAt( index ) );
			}
			array[index] = Character.toUpperCase( array[index] );
			while ( !splitChar.contains( c ) )
			{
				index++;
				if ( index == array.length )
					break;
				c = new Character( text.charAt( index ) );
			}
		}
		return new String( array );
	}

}
