package org.eclipse.birt.report.engine.content;


public interface IListContent extends IContainerContent
{
	IListBandContent getHeader();
	
	/**
	 * @return
	 */
	public boolean isHeaderRepeat( );
	
	public void setHeaderRepeat(boolean repeat);
	
}
