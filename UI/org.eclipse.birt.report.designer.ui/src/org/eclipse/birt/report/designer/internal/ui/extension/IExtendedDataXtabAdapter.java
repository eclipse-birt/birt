package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.model.api.ReportItemHandle;


public interface IExtendedDataXtabAdapter
{
	public boolean isExtendedDataColumn(Object element);
	
	public boolean setExtendedData(ReportItemHandle element, Object object);
	
	public String getExtendedDataName(ReportItemHandle element);
	
	public boolean contains(Object parent, Object child);
	
	public Object[] getSupportedTypes(Object element);

}
