/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.emf.ecore.EObject;

/**
 * This class provides an internal implementation of the axis class used by the
 * renderer. Note that positions, orientations, rotation angles, etc could be
 * transposed and may not reflect the same values maintained by the model.
 */
public final class OneAxis {

	private AutoScale sc;

	private double dAxisRenderingCoordinate;

	private Location3D locAxisRenderingCoordinate;

	private double dTitleRenderingCoordinate;

	private int iOrientation;

	private int iLabelPosition, iTitlePosition;

	private LineAttributes lia = null;

	private Label la = null; // FOR AXIS LABELS

	private Label laTitle = null; // FOR AXIS TITLE

	private IntersectionValue iv = null;

	private Grid gr = null;

	private boolean bCategoryScale = false;

	private boolean bTickBwteenCategories = true;

	private final Axis axModel;

	private final int axisType;

	// if there is place to show labels
	private boolean bShowLabels = true;

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * The constructor.
	 */
	OneAxis(Axis axModel) {
		this.axModel = axModel;
		gr = new Grid();
		axisType = IConstants.UNDEFINED;
	}

	/**
	 * The constructor.
	 * 
	 * @param axModel
	 * @param axisType
	 */
	OneAxis(Axis axModel, int axisType) {
		this.axModel = axModel;
		gr = new Grid();
		this.axisType = axisType;
	}

	/**
	 * @return
	 */
	public int getAxisType() {
		return this.axisType;
	}

	/**
	 * 
	 * @param _iOrientation
	 * @param _iLabelLocation
	 * @param _iLabelRotation
	 * @param _iTickStyle
	 * @param _iAxisLocation
	 */
	void set(int _iOrientation, int _iLabelPosition, int _iTitlePosition, boolean _bCategoryScale,
			boolean _bTickBwteenCategories) {
		iOrientation = _iOrientation;
		iLabelPosition = _iLabelPosition;
		iTitlePosition = _iTitlePosition;
		bCategoryScale = _bCategoryScale;
		bTickBwteenCategories = _bTickBwteenCategories;
	}

	public void setAxisCoordinate(double _dAxisRenderingCoordinate) {
		dAxisRenderingCoordinate = _dAxisRenderingCoordinate;
	}

	void setAxisCoordinate3D(Location3D loc3d) {
		locAxisRenderingCoordinate = loc3d;
	}

	public void setTitleCoordinate(double _dTitleRenderingCoordinate) {
		dTitleRenderingCoordinate = _dTitleRenderingCoordinate;
	}

	/**
	 * @return
	 */
	public int getCombinedTickStyle() {
		return gr.getTickStyle(IConstants.MAJOR) | gr.getTickStyle(IConstants.MINOR);
	}

	/**
	 * @return
	 */
	public final double getAxisCoordinate() {
		return dAxisRenderingCoordinate;
	}

	/**
	 * @return
	 */
	public final Location3D getAxisCoordinate3D() {
		return locAxisRenderingCoordinate;
	}

	/**
	 * @return
	 */
	public final double getTitleCoordinate() {
		return dTitleRenderingCoordinate;
	}

	/**
	 * @return
	 */
	public final int getLabelPosition() {
		return iLabelPosition;
	}

	/**
	 * @return
	 */
	public final int getTitlePosition() {
		return iTitlePosition;
	}

	/**
	 * @return
	 */
	public final Axis getModelAxis() {
		return axModel;
	}

	final void setGridProperties(LineAttributes laMajorGrid, LineAttributes laMinorGrid, LineAttributes laMajorTicks,
			LineAttributes laMinorTicks, int iMajorTickStyle, int iMinorTickStyle, int iMinorUnitsPerMajorUnit) {
		gr.laMajorGrid = laMajorGrid;
		gr.laMinorGrid = laMinorGrid;
		gr.laMajorTicks = laMajorTicks;
		gr.laMinorTicks = laMinorTicks;
		gr.iMajorTickStyle = iMajorTickStyle;
		gr.iMinorTickStyle = iMinorTickStyle;
		gr.iMinorUnitsPerMajorUnit = iMinorUnitsPerMajorUnit;
	}

	/**
	 * @return
	 */
	public final Grid getGrid() {
		return gr;
	}

	/**
	 * @return
	 */
	public final int getOrientation() {
		return iOrientation;
	}

	/**
	 * @return
	 */
	public final boolean isCategoryScale() {
		return bCategoryScale;
	}

	public final boolean isTickBwtweenCategories() {
		// The default value of TickBwtweenCategories is true, and it should
		// only take effect for category scale. Which means
		// TickBwtweenCategories can only be false when
		// bCategoryScale is true.
		return !bCategoryScale || bTickBwteenCategories;
	}

	public final Chart getChartModel() {
		if (axModel == null) {
			return null;
		}

		EObject ct = axModel.eContainer();

		while (ct != null) {
			if (ct instanceof ChartWithAxes) {
				return (Chart) ct;
			}

			ct = ct.eContainer();
		}

		return null;
	}

	/**
	 * @return
	 */
	final boolean isAxisLabelStaggered() {
		if (axModel == null) {
			return false;
		}

		ChartDimension dim = null;
		Chart cm = getChartModel();
		if (cm != null) {
			dim = cm.getDimension();
		}

		if (dim == ChartDimension.THREE_DIMENSIONAL_LITERAL) {
			return false;
		}

		return axModel.isStaggered();
	}

	final int getLableShowingInterval() {
		if (axModel == null) {
			return 1;
		}

		int i = axModel.getInterval();

		if (i < 1) {
			return 1;
		}

		return i;
	}

	/**
	 * 
	 * @param _sc
	 */
	void set(AutoScale _sc) {
		sc = _sc;
	}

	/**
	 * @return
	 */
	public AutoScale getScale() {
		return sc;
	}

	void set(IntersectionValue _iv) {
		iv = _iv;
	}

	void set(Label _laAxisLabels, Label _laAxisTitle) {
		la = goFactory.copyOf(_laAxisLabels);
		laTitle = goFactory.copyOf(_laAxisTitle);
	}

	void set(LineAttributes _la) {
		lia = _la;
	}

	/**
	 * @return
	 */
	public final LineAttributes getLineAttributes() {
		return lia;
	}

	/**
	 * @return
	 */
	public final IntersectionValue getIntersectionValue() {
		return iv;
	}

	/**
	 * @return
	 */
	public final Label getLabel() {
		return la;
	}

	/**
	 * @return
	 */
	public final Label getTitle() {
		return laTitle;
	}

	/**
	 * @return
	 */
	public final FormatSpecifier getFormatSpecifier() {
		return axModel.getFormatSpecifier();
	}

	/**
	 * @return
	 */
	public final RunTimeContext getRunTimeContext() {
		return sc.getRunTimeContext();
	}

	/**
	 * @return Returns the bShowLabels.
	 */
	public final boolean isShowLabels() {
		return bShowLabels;
	}

	/**
	 * @param bShowLabels The bShowLabels to set.
	 */
	public final void setShowLabels(boolean bShowLabels) {
		this.bShowLabels = bShowLabels;
	}
}
