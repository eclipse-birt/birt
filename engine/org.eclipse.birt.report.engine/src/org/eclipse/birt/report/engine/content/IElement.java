
package org.eclipse.birt.report.engine.content;

import java.util.Collection;

public interface IElement
{

	public IElement getParent( );

	public void setParent( IElement parent );
	
	public Collection getChildren();
	
}
