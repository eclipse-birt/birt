
package org.eclipse.birt.report.engine.toc;


public class TOCEntry
{

	TOCEntry parent;
	TOCTreeNode root;
	TOCTreeNode node;
	String hiddenFormats;

	public TOCEntry( TOCEntry parent, TOCTreeNode root, TOCTreeNode node )
	{
		this.parent = parent;
		this.root = root;
		this.node = node;
	}

	/**
	 * @return the node
	 */
	public TOCTreeNode getNode( )
	{
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode( TOCTreeNode node )
	{
		this.node = node;
	}

	/**
	 * @return the parent
	 */
	public TOCEntry getParent( )
	{
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent( TOCEntry parent )
	{
		this.parent = parent;
	}

	/**
	 * @return the root
	 */
	public TOCTreeNode getRoot( )
	{
		return root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot( TOCTreeNode root )
	{
		this.root = root;
	}

	public String getHideFormats( )
	{
		return hiddenFormats;
	}
	
	public void setHideFormats( String hiddenFormats )
	{
		this.hiddenFormats = hiddenFormats;
	}
}
