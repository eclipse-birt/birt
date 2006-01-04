package org.eclipse.birt.report.engine.emitter.pdf;

import java.util.Iterator;

import org.eclipse.birt.report.engine.api.TOCNode;

import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfOutline;

public class TOCHandler
{
	/**
	 * The TOC node list.
	 */
	private TOCNode root;
	
	/**
	 * The constructor.
	 * @param root 			The TOC node in which need to build PDF outline 
	 */
	public TOCHandler (TOCNode root)
	{
		this.root = root;
	}
	
	/**
	 * get the root of the TOC tree.
	 * @return				The TOC root node
	 */
	public TOCNode getTOCRoot()
	{
		return this.root;
	}
	
	/**
	 * create a PDF outline for tocNode, using the pol as the parent PDF outline.
	 * @param tocNode		The tocNode whose kids need to build a PDF outline tree
	 * @param pol			The parent PDF outline for these kids
	 */
	public void createTOC(TOCNode tocNode, PdfOutline pol)
	{
		if (null == tocNode.getChildren())
			return;
		for (Iterator i = tocNode.getChildren().iterator(); i.hasNext();)
		{
			TOCNode node = (TOCNode)i.next();
			PdfOutline outline = new PdfOutline( pol,
            		PdfAction.gotoLocalPage(node.getBookmark(), false), 
            		node.getDisplayString());
			createTOC( node, outline );
		}
	}
}
	
