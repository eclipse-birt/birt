
package org.eclipse.birt.report.model.tests.matrix;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.MultiRowItem;

public class Matrix extends MultiRowItem implements IMatrix
{

	public Matrix( ExtendedItemHandle handle )
	{
		super( handle );
	}
	
	public String getMethod1()
	{
		return "matrix"; //$NON-NLS-1$
	}
}
