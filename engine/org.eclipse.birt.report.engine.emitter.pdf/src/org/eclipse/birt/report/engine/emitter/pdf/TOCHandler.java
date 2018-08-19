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

package org.eclipse.birt.report.engine.emitter.pdf;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfOutline;

public class TOCHandler
{

	/**
	 * The TOC node list.
	 */
	protected TOCNode root;
	/**
	 * The Pdf outline.
	 */
	protected PdfOutline outline;
	/**
	 * All bookMarks created during PDF rendering.
	 */
	protected Set<String> bookmarks;
	/**
	 * The counter to indicate how many pdf outline has been created.
	 */
	private long counter = 0;
	/**
	 * The max number of pdf outline.
	 */
	private static final long MAX_COUNT = 70000l;

	/**
	 * The constructor.
	 *
	 * @param root
	 *            The TOC node in which need to build PDF outline
	 */
	public TOCHandler( TOCNode root, PdfOutline outline, Set<String> bookmarks )
	{
		this.root = root;
		this.outline = outline;
		this.bookmarks = bookmarks;
	}

	/**
	 * @deprecated get the root of the TOC tree.
	 * @return The TOC root node
	 */
	public TOCNode getTOCRoot( )
	{
		return this.root;
	}

	public void createTOC( )
	{
		createTOC( root, outline, bookmarks );
	}

	/**
	 * create a PDF outline for tocNode, using the pol as the parent PDF
	 * outline.
	 *
	 * @param tocNode
	 *            The tocNode whose kids need to build a PDF outline tree
	 * @param pol
	 *            The parent PDF outline for these kids
	 * @param bookmarks
	 *            All bookMarks created during rendering
	 */
	protected void createTOC( TOCNode tocNode, PdfOutline pol,
			Set<String> bookmarks )
	{
		if ( isOutlineSizeOverflow( ) )
			return;
		if ( null == tocNode || null == tocNode.getChildren( ) )
			return;
		for ( Iterator i = tocNode.getChildren( ).iterator( ); i.hasNext( ); )
		{
			TOCNode node = (TOCNode) i.next( );
			if ( !bookmarks.contains( node.getBookmark( ) ) )
			{
				createTOC( node, outline, bookmarks );
				continue;
			}
			PdfOutline outline = new PdfOutline( pol, PdfAction.gotoLocalPage(
					node.getBookmark( ), false ), node.getDisplayString( ) );
			countOutlineSize( node.getBookmark( ).length( ) );
			IScriptStyle style = node.getTOCStyle( );
			String color = style.getColor( );
			if ( color != null )
			{
				color = color.toLowerCase( );
			}
			Color awtColor = PropertyUtil.getColor( color );
			if ( awtColor != null )
			{
                outline.setColor(new BaseColor( awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue()) );
			}
			String fontStyle = style.getFontStyle( );
			String fontWeight = style.getFontWeight( );
			int styleValue = PropertyUtil.getFontStyle( fontStyle, fontWeight );
			outline.setStyle( styleValue );
			createTOC( node, outline, bookmarks );
		}
	}

	protected boolean isOutlineSizeOverflow( )
	{
		return counter > MAX_COUNT;
	}

	protected void countOutlineSize( long size )
	{
		counter++;
	}
}
