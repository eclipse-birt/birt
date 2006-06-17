
package org.eclipse.birt.report.engine.toc;

import org.eclipse.birt.report.engine.api.TOCNode;

public class TOCEntry
{

	TOCEntry parent;
	TOCNode root;
	TOCNode node;

	public TOCEntry( TOCEntry parent, TOCNode root, TOCNode node )
	{
		this.parent = parent;
		this.root = root;
		this.node = node;
	}

	/**
	 * @return the node
	 */
	public TOCNode getNode( )
	{
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode( TOCNode node )
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
	public TOCNode getRoot( )
	{
		return root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot( TOCNode root )
	{
		this.root = root;
	}
}
