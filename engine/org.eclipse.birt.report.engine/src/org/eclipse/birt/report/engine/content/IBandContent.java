package org.eclipse.birt.report.engine.content;

public interface IBandContent extends IContainerContent
{
	public static final int BAND_HEADER = 0;
	public static final int BAND_FOOTER = 2;
	public static final int BAND_GROUP_HEADER = 3;
	public static final int BAND_GROUP_FOOTER = 4;
	public static final int BAND_DETAIL = 5;

	int getBandType( );
	
	void setBandType(int bandType);
	
	String getGroupID( );
}
