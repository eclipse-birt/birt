
package org.eclipse.birt.report.engine.content;

import java.util.List;

public interface IElement
{

	public IElement getParent( );

	public void setParent( IElement parent );
	
	public List getChildren();
	
}
