
package org.eclipse.birt.report.engine.content;

import org.eclipse.birt.report.engine.ir.BandDesign;

public interface IBandContent extends IContainerContent
{

	public static final int BAND_HEADER = BandDesign.BAND_HEADER;
	public static final int BAND_FOOTER = BandDesign.BAND_FOOTER;
	public static final int BAND_GROUP_HEADER = BandDesign.GROUP_HEADER;
	public static final int BAND_GROUP_FOOTER = BandDesign.GROUP_FOOTER;
	public static final int BAND_DETAIL = BandDesign.BAND_DETAIL;

	int getBandType( );

	void setBandType( int bandType );

	String getGroupID( );
}
