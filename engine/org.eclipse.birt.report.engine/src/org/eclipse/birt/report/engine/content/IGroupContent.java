package org.eclipse.birt.report.engine.content;


public interface IGroupContent extends IContainerContent
{
	boolean isHeaderRepeat( );

	void setHeaderRepeat( boolean repeat );

	IBandContent getHeader( );

	IBandContent getFooter( );

	String getGroupID();
	
	void setGroupID(String groupId);
	
	int getGroupLevel();
}
