package org.eclipse.birt.report.engine.api;

import java.util.ArrayList;
import java.util.List;

/**
 * A node that wraps around a TOC entry
 */
public class TOCNode {
	protected String displayString;
	protected String bookmark;
	protected TOCNode parent;
	protected String nodeId;
	protected ArrayList children;

	public String getNodeID()
	{
		return nodeId;
	}
	
	/**
	 * @return returns the parent node of the current TOC node
	 */
	public TOCNode getParent()
	{
		return parent;
	}
	
	/**
	 * @return the list of child TOC nodes
	 */
	public List getChildren( )
	{
		if (children == null)
		{
			children = new ArrayList();
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
	 * @return the bookmark string that the TOC item points to.
	 */
	public String getBookmark( )
	{
		return bookmark;
	}
	
	public void setBookmark(String bookmark)
	{
		this.bookmark = bookmark;
	}
	
	public void setDisplayString(String display)
	{
		this.displayString = display;
	}
	
	public void setNodeID(String id)
	{
		this.nodeId = id;
	}
	
	public void setParent(TOCNode parent)
	{
		this.parent = parent;
	}
}
