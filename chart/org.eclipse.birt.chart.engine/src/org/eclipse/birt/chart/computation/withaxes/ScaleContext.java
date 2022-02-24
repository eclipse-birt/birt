/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import java.util.Date;

import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * Scale context for min/max computation.
 * 
 * @TODO only support computation for linear value, to add support DataTime
 *       value if need be
 */

public class ScaleContext extends Methods {

	// Min/Max value in the dataset
	private Object oMinAuto;
	private Object oMaxAuto;

	// Min/Max/Step value specified in model
	private Object oMinFixed;
	private Object oMaxFixed;
	private Integer oStepNumber;

	// Percentage of margin area for special charts, such as bubble
	private final int iMarginPercent;

	private final int iType;

	private Object oUnit;
	private Object oMin;
	private Object oMax;
	private Object oStep;

	// if uses the specified minimum value
	private boolean bMinimumFixed = false;
	// if uses the specified maximum value
	private boolean bMaximumFixed = false;
	private boolean bStepFixed = false;
	private boolean bMargin = false;

	private boolean bExpandMinmax = true;

	public ScaleContext copy() {
		ScaleContext dest = new ScaleContext(iMarginPercent, iType);

		dest.oMinAuto = oMinAuto;
		dest.oMaxAuto = oMaxAuto;
		dest.oMinFixed = oMinFixed;
		dest.oMaxFixed = oMaxFixed;
		dest.oStepNumber = oStepNumber;
		dest.oUnit = oUnit;
		dest.oMin = oMin;
		dest.oMax = oMax;
		dest.oStep = oStep;
		dest.bMinimumFixed = bMinimumFixed;
		dest.bMaximumFixed = bMaximumFixed;
		dest.bStepFixed = bStepFixed;
		dest.bMargin = bMargin;
		dest.bExpandMinmax = bExpandMinmax;

		return dest;
	}

	public ScaleContext(int iMarginPercent, int iType) {
		this.iMarginPercent = iMarginPercent;
		this.iType = iType;
	}

	public ScaleContext(int iMarginPercent, int iType, Object oUnit, Object oMinValue, Object oMaxValue, Object oStep) {
		this.iMarginPercent = iMarginPercent;
		this.iType = iType;
		this.oUnit = oUnit;

		this.oMinAuto = oMinValue;
		this.oMaxAuto = oMaxValue;
		this.oStep = oStep;

		this.bMargin = iMarginPercent > 0;
	}

	public ScaleContext(int iMarginPercent, int iType, Object oMinAuto, Object oMaxAuto, Object oStep) {
		this(iMarginPercent, iType, 0, oMinAuto, oMaxAuto, oStep);
	}

	/**
	 * 
	 * @param that
	 */
	public void updateShared(ScaleContext that) {
		this.oUnit = that.oUnit;
		this.oStep = that.oStep;
	}

	/**
	 * Creates a simple instance of scale. Note that this instance is just used to
	 * store min/max and can not be computed directly.
	 * 
	 * @param oMin
	 * @param oMax
	 * @since 2.3
	 */
	public static ScaleContext createSimpleScale(Object oMin, Object oMax) {
		ScaleContext scale = new ScaleContext(0, 0, oMin, oMax, null);
		if (oMin instanceof Date) {
			scale.oMin = new CDateTime((Date) oMin);
			scale.oMax = new CDateTime((Date) oMax);
		} else {
			scale.oMin = oMin;
			scale.oMax = oMax;
		}
		return scale;
	}

	public void setFixedValue(boolean bMinimumFixed, boolean bMaximumFixed, Object oMinFixed, Object oMaxFixed) {
		this.oMinFixed = oMinFixed;
		this.oMaxFixed = oMaxFixed;
		this.bMinimumFixed = bMinimumFixed;
		this.bMaximumFixed = bMaximumFixed;

		this.oMin = oMinFixed;
		this.oMax = oMaxFixed;
	}

	public void setFixedStep(boolean bStepFixed, Integer oStepNumber) {
		this.oStepNumber = oStepNumber;

		this.bStepFixed = bStepFixed || oStepNumber != null;
	}

	/**
	 * Returns the minimum of the scale
	 * 
	 * @return the minimum of the scale
	 */
	public Object getMin() {
		return oMin;
	}

	public void setMin(Object oMin) {
		this.oMin = oMin;
	}

	/**
	 * Returns the maximum of the scale
	 * 
	 * @return the maximum of the scale
	 */
	public Object getMax() {
		return oMax;
	}

	public void setMax(Object oMax) {
		this.oMax = oMax;
	}

	/**
	 * Returns the minimum plus margin. Margin means extra space for rendering and
	 * clipping. If margin is 0, or no margin needed, return null.
	 * 
	 * @return the minimum plus margin. If no margin, return null.
	 */
	public Object getMinWithMargin() {
		return oMinAuto;
	}

	public void setMinWithMargin(Object oMinAuto) {
		this.oMinAuto = oMinAuto;
	}

	/**
	 * Returns the maximum plus margin. Margin means extra space for rendering and
	 * clipping. If margin is 0, or no margin needed, return null.
	 * 
	 * @return the maximum plus margin. If no margin, return null.
	 */
	public Object getMaxWithMargin() {
		return oMaxAuto;
	}

	public void setMaxWithMargin(Object oMaxAuto) {
		this.oMaxAuto = oMaxAuto;
	}

	public Object getStep() {
		return oStep;
	}

	public void setStep(Object oStep) {
		this.oStep = oStep;
	}

	public Integer getStepNumber() {
		return oStepNumber;
	}

	public void setStepNumber(Integer oStepNumber) {
		this.oStepNumber = oStepNumber;
	}

	public void computeMinMax() {
		computeMinMax(false);
	}

	public void computeMinMax(boolean bAlignZero) {
		if ((iType & LINEAR) == LINEAR) {
			computeLinearMinMax(bAlignZero);
		} else if ((iType & DATE_TIME) == DATE_TIME) {
			computeDateTimeMinMax();
		} else if ((iType & LOGARITHMIC) == LOGARITHMIC) {
			computeLogMinMax();
		}
	}

	private void computeLinearMinMax(boolean bAlignZero) {
		// These min/max is the value for the real boundary. If users
		// set the fixed value, to clip it.
		final double dMinReal, dMaxReal;
		// These min/max is the value that displays on axis after
		// considering the fixed value
		double dMinValue, dMaxValue;
		double dMargin = 0;
		if (bMargin) {
			// Margin for client area to render chart, such as bubbles
			dMargin = Math.abs(asDouble(oMaxAuto).doubleValue() - asDouble(oMinAuto).doubleValue()) * iMarginPercent
					/ 100;
		}
		dMinReal = asDouble(oMinAuto).doubleValue() - dMargin;
		dMaxReal = asDouble(oMaxAuto).doubleValue() + dMargin;
		dMinValue = bMinimumFixed ? asDouble(oMinFixed).doubleValue() : dMinReal;
		dMaxValue = bMaximumFixed ? asDouble(oMaxFixed).doubleValue() : dMaxReal;

		if (dMaxValue < dMinValue) {
			if (bMaximumFixed && dMaxValue >= 0) {
				dMinValue = 0;
			}

			if (bMinimumFixed && dMinValue <= 0) {
				dMaxValue = 0;
			}
		}

		// These min/max is the value after auto adjusting
		double dMinAxis = dMinValue;
		double dMaxAxis = dMaxValue;
		double dStep = 0;

		if (bStepFixed && oStepNumber != null) {
			// Compute step size
			oStep = new Double(Math.abs(dMaxValue - dMinValue) / (oStepNumber.intValue()));
			// dStep = asDouble( oStep ).doubleValue( );
		} else {
			dStep = asDouble(oStep).doubleValue();

			if (bMargin) {
				dMinAxis = ((dStep >= 1) ? Math.floor(dMinAxis / dStep) : Math.round(dMinAxis / dStep)) * dStep;
				dMaxAxis = (((dStep >= 1) ? Math.floor(dMaxAxis / dStep) : Math.round(dMaxAxis / dStep)) + 1) * dStep;
				if (dMaxAxis - dMaxValue >= dStep) {
					// To minus extra step because of Math.floor
					dMaxAxis -= dStep;
				}

				// // To set 0 when all values are positive or negative
				// according to MS Excel behavior
				// if ( dMinAxis > 0 && dMaxAxis > 0 )
				// {
				// dMinAxis = 0;
				// }
				// else if ( dMinAxis < 0 && dMaxAxis < 0 )
				// {
				// dMaxAxis = 0;
				// }
			} else if (!bExpandMinmax) {
				double dMinAxis1 = ((dStep >= 1) ? Math.floor(dMinAxis / dStep) : Math.round(dMinAxis / dStep)) * dStep;
				dMinAxis = dMinAxis < dMinAxis1 ? dMinAxis1 - dStep : dMinAxis1;

				double dMaxAxis1 = ((dStep >= 1) ? Math.floor(dMaxAxis / dStep) : Math.round(dMaxAxis / dStep)) * dStep;
				dMaxAxis = dMaxAxis > dMaxAxis1 ? dMaxAxis1 + dStep : dMaxAxis1;
			} else {
				// Auto adjust min and max by step if step number is not fixed
				final double dAbsMax = Math.abs(dMaxValue);
				final double dAbsMin = Math.abs(dMinValue);

				dMinAxis = ((dStep >= 1) ? Math.floor(dAbsMin / dStep) : Math.round(dAbsMin / dStep)) * dStep;
				dMaxAxis = ((dStep >= 1) ? Math.floor(dAbsMax / dStep) : Math.round(dAbsMax / dStep)) * dStep;

				if (ChartUtil.mathEqual(dMinAxis, dAbsMin)) {
					dMinAxis += dStep;
					if (dMinValue < 0) {
						dMinAxis = -dMinAxis;
					} else if (dMinValue == 0) {
						dMinAxis = 0;
					}
				} else {
					if (dMinValue < 0) {
						dMinAxis = -(dMinAxis + dStep);
					} else if (dMinAxis >= dMinValue && dMinAxis != 0) {
						dMinAxis -= dStep;
					}
				}

				if (ChartUtil.mathEqual(dMaxAxis, dAbsMax)) {
					dMaxAxis += dStep;
					if (dMaxValue < 0) {
						dMaxAxis = -dMaxAxis;
					} else if (dMaxValue == 0) {
						dMaxAxis = 0;
					}
				} else if (!ChartUtil.mathEqual(dMinAxis, dMaxValue)) {
					if (dMaxValue < 0) {
						dMaxAxis = -(dMaxAxis - dStep);
					} else if (dMaxValue > 0) {
						if (dMaxAxis < dMaxValue) {
							dMaxAxis += dStep;
						}
					}

					if (dMaxAxis - dMaxValue < (dMaxAxis - dMinAxis) / 30) {
						dMaxAxis += dStep;
					}
				}

				if (dMinValue < 0 && dMaxValue < 0) {
					if (dMaxAxis <= dMaxValue - dStep) {
						dMaxAxis += 2 * dStep;
					}
				}
				if (dMinValue > 0 && dMaxValue > 0) {
					if (dMinAxis >= dMinValue + dStep) {
						dMinAxis -= 2 * dStep;
					}
				}

				if (dMaxAxis - dMaxValue < (dMaxAxis - dMinAxis) / 50) {
					dMaxAxis += dStep;
				}

			}

		}

		// handle special case for min/max are both zero
		if (dMinValue == 0 && dMaxValue == 0) {
			if (dMinAxis >= 0) {
				dMinAxis = -1;
			}
			if (dMaxAxis <= 0) {
				dMaxAxis = 1;
			}
		}

		// To make sure the boundary is always 100, -100 in percent type
		if ((iType & PERCENT) == PERCENT) {
			if (dMaxAxis > 0) {
				dMaxAxis = 100;
			}
			if (dMinAxis < 0) {
				dMinAxis = -100;
			}
		}

		if (bAlignZero && dMinAxis < 0 && dMaxAxis > 0) {
			double abs = Math.max(Math.abs(dMinAxis), Math.abs(dMaxAxis));
			dMinAxis = -abs;
			dMaxAxis = abs;
		}

		if (!bMaximumFixed) {
			oMax = new Double(dMaxAxis);
		}
		if (!bMinimumFixed) {
			oMin = new Double(dMinAxis);
		}

		if (bMargin) {
			// If users specify a smaller range, to save the real range for
			// clipping later
			if (bMinimumFixed && dMinValue > dMinReal) {
				oMinAuto = new Double(dMinReal);
			} else {
				oMinAuto = null;
			}

			if (bMaximumFixed && dMaxValue < dMaxReal) {
				oMaxAuto = new Double(dMaxReal);
			} else {
				oMaxAuto = null;
			}
		} else {
			oMinAuto = null;
			oMaxAuto = null;
		}
	}

	private void computeDateTimeMinMax() {
		int iStep = asInteger(oStep);
		CDateTime cdtMinValue = bMinimumFixed ? asDateTime(oMinFixed) : asDateTime(oMinAuto);
		CDateTime cdtMaxValue = bMaximumFixed ? asDateTime(oMaxFixed) : asDateTime(oMaxAuto);

		int iUnit = ((Integer) oUnit).intValue();

		if (bMaximumFixed && !bMinimumFixed) {
			oMax = cdtMaxValue;
			double diff = CDateTime.computeDifference(cdtMaxValue, cdtMinValue, iUnit) / iStep;
			int count = (int) diff * iStep;

			if (!ChartUtil.mathEqual(diff, Math.floor(diff))) {
				count += iStep;
			}

			CDateTime cdtMinValue_new = (CDateTime) cdtMaxValue.clone();
			cdtMinValue_new.add(iUnit, -count);
			oMin = cdtMinValue_new;
		} else {
			if (bExpandMinmax) {
				if (!bMinimumFixed) {
					cdtMinValue = cdtMinValue.backward(iUnit, iStep);
					cdtMinValue.clearBelow(iUnit, true);

					if (!bMaximumFixed) {
						cdtMaxValue = cdtMaxValue.forward(iUnit, 1);
						cdtMaxValue.clearBelow(iUnit, true);
					}
				} else if (!bMaximumFixed) {
					double diff = CDateTime.computeDifference(cdtMaxValue, cdtMinValue, iUnit) / iStep;
					int count = (int) diff * iStep;

					if (!ChartUtil.mathEqual(diff, Math.floor(diff))) {
						count += iStep;
					}

					CDateTime cdtMaxValue_new = (CDateTime) cdtMinValue.clone();
					cdtMaxValue_new.add(iUnit, count);
					cdtMaxValue = cdtMaxValue_new;
				}
			}
			oMin = cdtMinValue;
			oMax = cdtMaxValue;
		}

		// Not support margin computation for Datetime type
		oMinAuto = null;
		oMaxAuto = null;
	}

	private void computeLogMinMax() {
		double dMinValue = asDouble(oMinAuto).doubleValue();
		double dMaxValue = asDouble(oMaxAuto).doubleValue();

		final double dAbsMax = Math.abs(dMaxValue);
		final double dAbsMin = Math.abs(dMinValue);
		final double dStep = asDouble(oStep).doubleValue();
		final double dStepLog = Math.log(dStep);

		int iPow = (int) Math.floor(Math.log(dAbsMax) / dStepLog) + 1;
		double dMaxAxis = Math.pow(dStep, iPow);
		iPow = (int) Math.floor(Math.log(dAbsMin) / dStepLog) - 1;
		double dMinAxis = Math.pow(dStep, iPow + 1);

		if (!bMaximumFixed) {
			oMax = new Double(dMaxAxis);
		}
		if (!bMinimumFixed) {
			oMin = new Double(dMinAxis);
		}

		// Not support margin computation for Log type
		oMinAuto = null;
		oMaxAuto = null;
	}

	/**
	 * @return Returns the iUnit.
	 */
	public Object getUnit() {
		return oUnit;
	}

	public void setUnit(Object oUnit) {
		this.oUnit = oUnit;
	}

	/**
	 * @return Returns the bExpandMinmax.
	 */
	public boolean isExpandMinmax() {
		return bExpandMinmax;
	}

	/**
	 * @param expandMinmax The bExpandMinmax to set.
	 */
	public void setExpandMinmax(boolean expandMinmax) {
		bExpandMinmax = expandMinmax;
	}
}
