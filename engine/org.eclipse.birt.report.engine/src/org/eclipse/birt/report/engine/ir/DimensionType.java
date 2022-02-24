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
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;

/**
 * 
 */
public class DimensionType {

	private static Logger log = Logger.getLogger(DimensionType.class.getName());

	public final static int TYPE_DIMENSION = 1;
	public final static int TYPE_CHOICE = 0;
	public final static String UNITS_CM = EngineIRConstants.UNITS_CM;
	public final static String UNITS_EM = EngineIRConstants.UNITS_EM;
	public final static String UNITS_EX = EngineIRConstants.UNITS_EX;
	public final static String UNITS_IN = EngineIRConstants.UNITS_IN;
	public final static String UNITS_MM = EngineIRConstants.UNITS_MM;
	public final static String UNITS_PC = EngineIRConstants.UNITS_PC;
	public final static String UNITS_PERCENTAGE = EngineIRConstants.UNITS_PERCENTAGE;
	public final static String UNITS_PT = EngineIRConstants.UNITS_PT;
	public final static String UNITS_PX = EngineIRConstants.UNITS_PX;
	protected int type = -1;
	protected String unitType;
	protected double measure = -1;
	protected String choice;

	public DimensionType() {
	}

	public DimensionType(String choice) {
		this.type = TYPE_CHOICE;
		this.choice = choice;
		this.measure = 0;
		this.unitType = null;
	}

	public DimensionType(double value, String units) {
		this.type = TYPE_DIMENSION;
		this.unitType = units;
		this.measure = value;
		this.choice = null;
	}

	public int getValueType() {
		return type;
	}

	public double getMeasure() {
		assert this.type == TYPE_DIMENSION;
		return this.measure;
	}

	public String getUnits() {
		assert this.type == TYPE_DIMENSION;
		return this.unitType;
	}

	public String getChoice() {
		return this.choice;
	}

	public String toString() {
		if (type == TYPE_DIMENSION) {
			// Copy from DimensionValue
			String value = Double.toString(measure);

			// Eliminate the ".0" that the default implementation tacks onto
			// the end of integers.
			if (value.substring(value.length() - 2).equals(".0")) //$NON-NLS-1$
				value = value.substring(0, value.length() - 2);
			return value + this.unitType;
		}
		return choice;
	}

	public double convertTo(String targetUnit) {
		assert type == TYPE_DIMENSION;
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
	 */
	public static DimensionType parserUnit(String value) {
		if (value != null) {
			try {
				DimensionValue val = DimensionValue.parse(value);
				return new DimensionType(val.getMeasure(), val.getUnits());
			} catch (PropertyValueException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}

	public static DimensionType parserUnit(String value, String defaultUnits) {
		if (value != null) {
			try {
				DimensionValue val = DimensionValue.parse(value);

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

	public void readObject(DataInputStream in) throws IOException {
		int version = IOUtil.readInt(in);
		int filedId = IOUtil.readInt(in);
		while (filedId != FIELD_NONE) {
			readField(version, filedId, in);
			filedId = IOUtil.readInt(in);
		}
	}

	public void writeObject(DataOutputStream out) throws IOException {
		IOUtil.writeInt(out, VERSION);
		writeFields(out);
		IOUtil.writeInt(out, FIELD_NONE);
	}
}
