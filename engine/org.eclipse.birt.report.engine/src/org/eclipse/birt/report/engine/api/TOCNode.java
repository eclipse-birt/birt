
package org.eclipse.birt.report.engine.api;

import java.util.ArrayList;
import java.util.List;

/**
 * A node that wraps around a TOC entry. To navigate to a specific TOC entry, one starts
 * with the TOC root and traverse down the TOC tree, obtaining list of TOC nodes.
 * 
 * For each node, the user can go to a specific page by following the bookmark.  
 */
public class TOCNode
{
	/**
	 * the string to be displayed for the TOC entry
	 */
	protected String displayString;
	
	/**
	 * A bookmark that is stored for the TOC
	 */
	protected String bookmark;
	
	/**
	 * the parent node
	 */
	protected TOCNode parent;
	
	/**
	 * identifier for the current node
	 */
	protected String nodeId;
	
	/**
	 * A list of children for the TOC node
	 */
	protected ArrayList children;

	/**
	 * @return the unique ID for the TOC node
	 */
	public String getNodeID( )
	{
		return nodeId;
	}

	/**
	 * @return returns the parent node of the current TOC node
	 */
	public TOCNode getParent( )
	{
		return parent;
	}

	/**
	 * @return the list of child TOC nodes
	 */
	public List getChildren( )
	{
		if ( children == null )
		{
			children = new ArrayList( );
		}
		return children;
	}

	/**
	 * @return the display string for the TOC entry
	 */
	public String getDisplayString( )
	{
		return displayString;
	}

	/**
	 * @param display the display string
	 */
	public void setDisplayString( String displayStr )
	{
		this.displayString = displayStr;
	}
	
	/**
	 * @return the bookmark string that the TOC item points to.
	 */
	public String getBookmark( )
	{
		return bookmark;
	}

	public void setBookmark( String bookmark )
	{
		this.bookmark = bookmark;
	}

	public void setNodeID( String id )
	{
		this.nodeId = id;
	}

	public void setParent( TOCNode parent )
	{
		this.parent = parent;
	}
}
