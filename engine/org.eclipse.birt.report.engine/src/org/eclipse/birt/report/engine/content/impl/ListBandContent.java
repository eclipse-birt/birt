
package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IReportContent;

public class ListBandContent extends AbstractBandContent
		implements
			IListBandContent
{
	public ListBandContent( IReportContent report )
	{
		super( report );
	}

	public int getContentType( )
	{
		return LIST_BAND_CONTENT;
	}
	
	public Object accept(IContentVisitor visitor, Object value)
	{
		return visitor.visitListBand( this, value );
	}
}
