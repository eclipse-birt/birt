
package org.eclipse.birt.report.designer.ui.preview.toc;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

public abstract class TOCHtmlFactory
{

	/**
	 * Gets TOC html value.
	 * 
	 * @param document
	 * @param locale
	 * @return toc html value.
	 */

	public String getTOCHtml( IReportDocument document, ULocale locale )
	{
		ITOCTree tree = document.getTOCTree(
				DesignChoiceConstants.FORMAT_TYPE_VIEWER, locale );
		TOCNode root = tree.getRoot( );

		String preValue = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tbody> ";
		String postValue = "</tbody></table>";

		String value = buildTOCHtml( root, 0 );
		value = preValue + value + postValue;

		return value;
	}

	/**
	 * Gets toc html value.
	 * 
	 * @param document
	 * @return toc html value.
	 */

	public String getTOCHtml( IReportDocument document )
	{
		ULocale locale = ThreadResources.getLocale( );
		return getTOCHtml( document, locale );
	}

	/**
	 * Gets toc script html value.
	 * 
	 * @return toc script html value.
	 */

	protected abstract String getTOCHtmlScript( );

	/**
	 * Gets toc style html value.
	 * 
	 * @return toc style html value
	 */

	protected abstract String getTOCHtmlStyle( );

	/**
	 * Build TOC structure tree to TOC html.
	 * 
	 * @param node
	 * @return toc html.
	 */

	private String buildTOCHtml( TOCNode node, int level )
	{
		assert node != null;
		
		StringBuffer buffer = new StringBuffer( );
		buffer.append( "<tr><td " );
		
		String id = node.getNodeID( );
		String displayValue = node.getDisplayString( );
		String bookmark = node.getBookmark( );

		if ( displayValue != null )
		{
			String blank = printBlank( level );
			displayValue = blank + displayValue;
		}

		// add id

		buffer.append( "id=\"" );
		buffer.append( id );
		buffer.append( "\" " );

		// add bookmark

		if ( bookmark != null )
		{
			buffer.append( "bookmark=\"" );
			buffer.append( bookmark );
			buffer.append( "\" " );
		}

		// add toc style

		String styleValue = getTOCHtmlStyle( );
		if ( styleValue != null )
		{
			buffer.append( "style=\"" );
			buffer.append( styleValue );
			buffer.append( "\"" );
		}
		buffer.append( " >" );

		// add html script

		String scriptValue = getTOCHtmlScript( );
		if ( scriptValue == null )
		{
			buffer.append( displayValue );

		}
		else
		{
			buffer.append( scriptValue );
			buffer.append( "('" );
			buffer.append( displayValue );
			buffer.append( "') " );
		}
		
		buffer.append( "</td></tr>" );
		
		List nodeList = node.getChildren( );
		if ( nodeList.size( ) == 0 )
		{	
			return buffer.toString();
		}
		
		Iterator iterator = nodeList.iterator( );
		while ( iterator.hasNext( ) )
		{
			TOCNode childNode = (TOCNode) iterator.next( );
		
			// recursively build child.
			
			String childHtml = buildTOCHtml( childNode, level + 1 );
			if( childHtml != null && childHtml.length( ) > 0 )
			{
				buffer.append( childHtml );
			}
		}
		return buffer.toString( );
	}

	/**
	 * Prints blank value.
	 * 
	 * @param level
	 * @return blank value.
	 */

	private String printBlank( int level )
	{
		StringBuffer buffer = new StringBuffer( );
		for ( int i = 0; i < level; ++i )
		{
			buffer.append( "&nbsp;" );
		}
		return buffer.toString( );
	}

}
