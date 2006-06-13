package org.eclipse.birt.report.engine.internal.document;


public class DocumentExtension
{
	long index = -1;
	long parent = -1;
	long firstChild = -1;
	long lastChild = -1;
	long previous = -1;
	long next = -1;
	public DocumentExtension(long index)
	{
		this.index = index;
	}
	
	public long getIndex( )
	{
		return index;
	}
	
	public void setIndex( long index )
	{
		this.index = index;
	}


	public long getFirstChild( )
	{
		return firstChild;
	}
	
	public void setFirstChild( long firstChild )
	{
		this.firstChild = firstChild;
	}
	
	public long getLastChild( )
	{
		return lastChild;
	}
	
	public void setLastChild( long lastChild )
	{
		this.lastChild = lastChild;
	}
	
	public long getNext( )
	{
		return next;
	}
	
	public void setNext( long next )
	{
		this.next = next;
	}
	
	public long getParent( )
	{
		return parent;
	}
	
	public void setParent( long parent )
	{
		this.parent = parent;
	}
	
	public long getPrevious( )
	{
		return previous;
	}
	
	public void setPrevious( long previous )
	{
		this.previous = previous;
	}
	
	
}
