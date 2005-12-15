package org.eclipse.birt.report.engine.api.script.instance;

public interface IDataItemInstance extends IReportItemInstance
{

	Object getValue( );

	void setValue( Object value );
}