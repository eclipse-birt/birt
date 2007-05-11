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

import java.util.Iterator;
import java.util.Set;

import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.script.internal.instance.StyleInstance;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfOutline;

public class TOCHandler
{
	/**
	 * The TOC node list.
	 */
	private TOCNode root;
	/**
	 * The Pdf outline.
	 */
	private PdfOutline outline;
	/**
	 * All bookMarks created during PDF rendering.
	 */
	private Set bookmarks;
	/**
	 * The constructor.
	 * @param root 			The TOC node in which need to build PDF outline 
	 */
	public TOCHandler (TOCNode root, PdfOutline outline, Set bookmarks)
	{
		this.root = root;
		this.outline = outline;
		this.bookmarks = bookmarks;
	}
	
	/**
	 * @deprecated
	 * get the root of the TOC tree.
	 * @return				The TOC root node
	 */
	public TOCNode getTOCRoot()
	{
		return this.root;
	}
	
	public void createTOC()
	{
		createTOC(root, outline, bookmarks);
	}
	
	/**
	 * create a PDF outline for tocNode, using the pol as the parent PDF outline.
	 * @param tocNode		The tocNode whose kids need to build a PDF outline tree
	 * @param pol			The parent PDF outline for these kids
	 * @param bookmarks		All bookMarks created during rendering
	 */
	private void createTOC(TOCNode tocNode, PdfOutline pol, Set bookmarks)
	{
		if (null == tocNode || null == tocNode.getChildren())
			return;
		for (Iterator i = tocNode.getChildren().iterator(); i.hasNext();)
		{
			TOCNode node = (TOCNode)i.next();
			if (!bookmarks.contains(node.getBookmark()))
				continue;
			PdfOutline outline = new PdfOutline( pol,
            		PdfAction.gotoLocalPage(node.getBookmark(), false), node.getDisplayString()
            		);
			IScriptStyle style = node.getTOCStyle( );
			if(style instanceof StyleInstance)
			{
				StyleInstance instance = (StyleInstance)style;
				CSSValue color = instance.getProperty( StyleConstants.STYLE_COLOR );
				if(color!=null && !color.equals( CSSValueConstants.BLACK_VALUE ))
				{
					outline.setColor( PropertyUtil.getColor(color) );
				}
				int styleValue = Font.NORMAL;
				CSSValue fontStyle = instance.getProperty( StyleConstants.STYLE_FONT_STYLE );
				if ( CSSValueConstants.OBLIQUE_VALUE.equals( fontStyle )
						|| CSSValueConstants.ITALIC_VALUE.equals( fontStyle ) )
				{
					styleValue |= Font.ITALIC;
				}

				if ( PropertyUtil.isBoldFont( instance
						.getProperty( StyleConstants.STYLE_FONT_WEIGHT ) ) )
				{
					styleValue |= Font.BOLD;
				}
				outline.setStyle( styleValue );
				
			}
			createTOC( node, outline, bookmarks );
		}
	}
}
	
