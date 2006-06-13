
package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IReportContent;

public class ListGroupContent extends GroupContent implements IListGroupContent
{
	public ListGroupContent( IReportContent report )
	{
		super( report );
	}
	
	public int getContentType( )
	{
		return LIST_GROUP_CONTENT;
	}

	public Object accept( IContentVisitor visitor, Object value )
	{
		return visitor.visitListGroup(this, value);
	}

	
}
