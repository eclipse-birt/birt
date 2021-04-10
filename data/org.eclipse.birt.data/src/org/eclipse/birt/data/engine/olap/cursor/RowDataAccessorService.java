package org.eclipse.birt.data.engine.olap.cursor;

import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;

/**
 * This class provide the available information when populating edgeInfo.
 * 
 */
public class RowDataAccessorService {
	private DimensionAxis[] dimAxis;
	private BirtEdgeView view;

	/**
	 * 
	 * @param rs
	 * @param isPage
	 * @param dimAxis
	 * @param mirrorStartPosition
	 */
	public RowDataAccessorService(DimensionAxis[] dimAxis, BirtEdgeView view) {
		this.dimAxis = dimAxis;
		this.view = view;
	}

	public DimensionAxis[] getDimensionAxis() {
		return this.dimAxis;
	}

	public int getPagePosition() {
		return this.view.getPageEndingIndex();
	}
}