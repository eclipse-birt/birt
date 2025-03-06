/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Class of dimension type
 */
public class DimensionType {

	private static Logger log = Logger.getLogger(DimensionType.class.getName());

	/** property: dimension type */
	public final static int TYPE_DIMENSION = 1;

	/** property: type choice */
	public final static int TYPE_CHOICE = 0;

	/** property: unit "cm" */
	public final static String UNITS_CM = DesignChoiceConstants.UNITS_CM;

	/** property: unit "em" */
	public final static String UNITS_EM = DesignChoiceConstants.UNITS_EM;

	/** property: unit "ex" */
	public final static String UNITS_EX = DesignChoiceConstants.UNITS_EX;

	/** property: unit "in" */
	public final static String UNITS_IN = DesignChoiceConstants.UNITS_IN;

	/** property: unit "mm" */
	public final static String UNITS_MM = DesignChoiceConstants.UNITS_MM;

	/** property: unit "pc" */
	public final static String UNITS_PC = DesignChoiceConstants.UNITS_PC;

	/** property: unit "%" */
	public final static String UNITS_PERCENTAGE = DesignChoiceConstants.UNITS_PERCENTAGE;

	/** property: unit "pt" */
	public final static String UNITS_PT = DesignChoiceConstants.UNITS_PT;

	/** property: unit "px" */
	public final static String UNITS_PX = DesignChoiceConstants.UNITS_PX;

	protected int type = -1;
	protected String unitType;
	protected double measure = -1;
	protected String choice;

	/**
	 * Constructor 1
	 */
	public DimensionType() {
	}

	/**
	 * Constructor 2
	 *
	 * @param choice type choice
	 */
	public DimensionType(String choice) {
		this.type = DimensionType.TYPE_CHOICE;
		this.choice = choice;
		this.measure = 0;
		this.unitType = null;
	}

	/**
	 * Constructor 3
	 *
	 * @param value dimension value
	 * @param units dimension unit
	 */
	public DimensionType(double value, String units) {
		this.type = DimensionType.TYPE_DIMENSION;
		this.unitType = units;
		this.measure = value;
		this.choice = null;
	}

	/**
	 * Get the value type
	 *
	 * @return the value type
	 */
	public int getValueType() {
		return type;
	}

	/**
	 * Get the dimension measure
	 *
	 * @return the dimension measure
	 */
	public double getMeasure() {
		assert this.type == DimensionType.TYPE_DIMENSION;
		return this.measure;
	}

	/**
	 * Get the dimension unit
	 *
	 * @return the dimension unit
	 */
	public String getUnits() {
		assert this.type == DimensionType.TYPE_DIMENSION;
		return this.unitType;
	}

	/**
	 * Get the dimension choice
	 *
	 * @return the dimension choice
	 */
	public String getChoice() {
		return this.choice;
	}

	@Override
	public String toString() {
		if (type == TYPE_DIMENSION) {
			// Copy from DimensionValue
			String value = Double.toString(measure);

			// Eliminate the ".0" that the default implementation tacks onto
			// the end of integers.
			if (value.substring(value.length() - 2).equals(".0")) { //$NON-NLS-1$
				value = value.substring(0, value.length() - 2);
			}
			return value + this.unitType;
		}
		return choice;
	}

	/**
	 * Convert dimension measure to given target unit
	 *
	 * @param targetUnit target unit
	 * @return converted dimension value
	 */
	public double convertTo(String targetUnit) {
		assert type == DimensionType.TYPE_DIMENSION;
		DimensionValue value = DimensionUtil.convertTo(this.measure, this.unitType, targetUnit);
		if (value != null) {
			return value.getMeasure();
		}
		return 0;
	}

	/**
	 * Implement the subtract operation of type <Code>DimensionType</Code>.
	 *
	 * @param subtrahend the subtrahend
	 * @return the result whose unit is <Code>CM_UNIT</Code>
	 */
	public DimensionType subtract(DimensionType subtrahend) {
		assert (getValueType() == DimensionType.TYPE_DIMENSION);
		assert (subtrahend != null && subtrahend.getValueType() == DimensionType.TYPE_DIMENSION);

		double measure = convertTo(DimensionType.UNITS_CM);
		measure -= subtrahend.convertTo(DimensionType.UNITS_CM);
		DimensionType ret = new DimensionType(measure, DimensionType.UNITS_CM);
		return ret;
	}

	/**
	 * Implement the compare operation of type <Code>DimensionType</Code>.
	 *
	 * @param subtrahend the subtrahend operand
	 * @return a negative double, zero, or a positive double as the first operand is
	 *         less than, equal to, or greater than the second.
	 */
	public double compare(DimensionType subtrahend) {
		assert (getValueType() == DimensionType.TYPE_DIMENSION);
		assert (subtrahend != null && subtrahend.getValueType() == DimensionType.TYPE_DIMENSION);

		double measure = convertTo(DimensionType.UNITS_CM);
		measure -= subtrahend.convertTo(DimensionType.UNITS_CM);

		return measure;
	}

	/**
	 * Parses a dimension string. The string must match the following:
	 * <ul>
	 * <li>null</li>
	 * <li>[1-9][0-9]*[.[0-9]*[ ]*[in|cm|mm|pt|pc|em|ex|px|%]]</li>
	 * </ul>
	 *
	 * If the error exists, return the result whose measure is 0.
	 *
	 * @param value value to be parsed
	 * @return parsed value
	 */
	public static DimensionType parserUnit(String value) {
		if (value != null) {
			try {
				DimensionValue val = StringUtil.parse(value);
				return new DimensionType(val.getMeasure(), val.getUnits());
			} catch (PropertyValueException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Parses a dimension string. The string must match the following:
	 * <ul>
	 * <li>null</li>
	 * <li>[1-9][0-9]*[.[0-9]*[ ]*[in|cm|mm|pt|pc|em|ex|px|%]]</li>
	 * </ul>
	 *
	 * If the error exists, return the result whose measure is 0.
	 *
	 * @param value        value to be parsed
	 * @param defaultUnits default unit
	 * @return parsed value
	 */
	public static DimensionType parserUnit(String value, String defaultUnits) {
		if (value != null) {
			try {
				DimensionValue val = StringUtil.parse(value);

				String units = val.getUnits();
				if (null == units || "".equals(units)) {
					units = defaultUnits;
				}
				return new DimensionType(val.getMeasure(), units);
			} catch (PropertyValueException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}

	/**
	 * object document dimension version
	 */
	static final protected int VERSION = 0;

	final static int FIELD_NONE = -1;
	final static int FIELD_TYPE = 0;
	final static int FIELD_UNITTYPE = 1;
	final static int FIELD_MEASURE = 2;
	final static int FIELD_CHOICE = 3;

	protected void writeFields(DataOutputStream out) throws IOException {
		if (type != -1) {
			IOUtil.writeInt(out, FIELD_TYPE);
			IOUtil.writeInt(out, type);
		}
		if (unitType != null) {
			IOUtil.writeInt(out, FIELD_UNITTYPE);
			IOUtil.writeString(out, unitType);
		}
		if (measure != -1) {
			IOUtil.writeInt(out, FIELD_MEASURE);
			IOUtil.writeDouble(out, measure);
		}
		if (choice != null) {
			IOUtil.writeInt(out, FIELD_CHOICE);
			IOUtil.writeString(out, choice);
		}
	}

	protected void readField(int version, int filedId, DataInputStream in) throws IOException {
		switch (filedId) {
		case FIELD_TYPE:
			type = IOUtil.readInt(in);
			break;
		case FIELD_UNITTYPE:
			unitType = IOUtil.readString(in);
			break;
		case FIELD_MEASURE:
			measure = IOUtil.readDouble(in);

			break;
		case FIELD_CHOICE:
			choice = IOUtil.readString(in);
			break;
		}
	}

	/**
	 * Read the object
	 *
	 * @param in data input stream to be read
	 * @throws IOException
	 */
	public void readObject(DataInputStream in) throws IOException {
		int version = IOUtil.readInt(in);
		int filedId = IOUtil.readInt(in);
		while (filedId != FIELD_NONE) {
			readField(version, filedId, in);
			filedId = IOUtil.readInt(in);
		}
	}

	/**
	 * Write an object
	 *
	 * @param out data output stream to be written
	 * @throws IOException
	 */
	public void writeObject(DataOutputStream out) throws IOException {
		IOUtil.writeInt(out, VERSION);
		writeFields(out);
		IOUtil.writeInt(out, FIELD_NONE);
	}
}
