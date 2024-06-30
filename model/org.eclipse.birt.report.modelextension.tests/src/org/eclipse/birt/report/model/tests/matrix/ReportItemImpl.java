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

package org.eclipse.birt.report.model.tests.matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.ICompatibleReportItem;
import org.eclipse.birt.report.model.api.extension.IElementCommand;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.extension.ReportItem;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.metadata.MethodInfo;
import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * Implements <code>IReportItem</code> for testing
 */

public class ReportItemImpl extends ReportItem implements IReportItem, ICompatibleReportItem, Cloneable {

	private static final String TEST1_PROP = "test1"; //$NON-NLS-1$
	private static final String TEST2_PROP = "test2"; //$NON-NLS-1$
	private static final String TEST3_PROP = "test3"; //$NON-NLS-1$
	private static final String TEST5_PROP = "test5"; //$NON-NLS-1$
	private static final String TEST6_PROP = "test6"; //$NON-NLS-1$
	private static final String TEST7_PROP = "test7"; //$NON-NLS-1$

	private static final String SCRIPT_PROP = "script"; //$NON-NLS-1$

	private static final String TYPE_RPOP = "type"; //$NON-NLS-1$
	private static final String RADIUS_RPOP = "radius"; //$NON-NLS-1$
	private static final String WIDTH_RPOP = "pieWidth"; //$NON-NLS-1$
	private static final String HEIGHT_RPOP = "pieHeight"; //$NON-NLS-1$
	private static final String X_SCALE_RPOP = "xScale"; //$NON-NLS-1$
	private static final String Y_SCALE_RPOP = "yScale"; //$NON-NLS-1$
	private static final String COMPANY_RPOP = "company"; //$NON-NLS-1$
	private static final String LINE_STYLE_PROP = "lineStyle"; //$NON-NLS-1$
	private static final String ON_PREPARE_RPOP = "firstMethod"; //$NON-NLS-1$
	private static final String ON_CREATE_PROP = "sencondMethod"; //$NON-NLS-1$
	private static final String WIDTH_PROP = "width";//$NON-NLS-1$

	private static final String TYPE_PIE = "pie"; //$NON-NLS-1$
	private static final String TYPE_BAR = "bar"; //$NON-NLS-1$

	private static final String CHOICE_LINE_STYLE_THIN = "thin"; //$NON-NLS-1$
	private static final String CHOICE_LINE_STYLE_NORMAL = "normal"; //$NON-NLS-1$
	private static final String CHOICE_LINE_STYLE_THICK = "thick"; //$NON-NLS-1$

	private ModuleHandle moduleHandle = null;
	private DesignElementHandle extItemHandle = null;
	private PropertyDefinitionImpl[] piePropertyList = null;
	private PropertyDefinitionImpl[] barPropertyList = null;
	private PropertyDefinitionImpl[] methods = null;

	// test1
	private String test1;

	// test2
	private int test2;

	// test3
	private String type = TYPE_BAR;
	private int radius = 0;
	private int width = 0;
	private int height = 0;
	private int xScale = 0;
	private int yScale = 0;
	private String company = null;
	private String lineStyle = CHOICE_LINE_STYLE_NORMAL;

	// width property value
	private String widthValue = null;

	// script
	private String script;

	// test5
	private String test5;

	// test6
	private String test6;

	// test7
	private String test7;

	private boolean refreshNeeded = false;

	/**
	 * Constructs an element.
	 *
	 * @param extDefn
	 * @param elementHandle
	 */

	public ReportItemImpl(IReportItemFactory extDefn, DesignElementHandle elementHandle) {
		assert elementHandle != null;
		this.moduleHandle = elementHandle.getModuleHandle();
		extItemHandle = elementHandle;

		piePropertyList = new PropertyDefinitionImpl[6];

		piePropertyList[0] = new PropertyDefinitionImpl();
		piePropertyList[0].setName(RADIUS_RPOP);
		piePropertyList[0].setType(PropertyType.INTEGER_TYPE);
		piePropertyList[0].setDisplayNameID("TestingMatrix.test3.radius"); //$NON-NLS-1$

		piePropertyList[1] = new PropertyDefinitionImpl();
		piePropertyList[1].setName(WIDTH_RPOP);
		piePropertyList[1].setType(PropertyType.INTEGER_TYPE);
		piePropertyList[1].setDisplayNameID("TestingMatrix.test3.width"); //$NON-NLS-1$

		piePropertyList[2] = new PropertyDefinitionImpl();
		piePropertyList[2].setName(HEIGHT_RPOP);
		piePropertyList[2].setType(PropertyType.INTEGER_TYPE);
		piePropertyList[2].setDisplayNameID("TestingMatrix.test3.height"); //$NON-NLS-1$

		piePropertyList[3] = new PropertyDefinitionImpl();
		piePropertyList[3].setName(TYPE_RPOP);
		piePropertyList[3].setType(PropertyType.STRING_TYPE);
		piePropertyList[3].setDisplayNameID("TestingMatrix.test3.type"); //$NON-NLS-1$

		// construct the property definition for method
		methods = new PropertyDefinitionImpl[2];

		piePropertyList[4] = new PropertyDefinitionImpl();
		piePropertyList[4].setName(ON_PREPARE_RPOP);
		piePropertyList[4].setType(PropertyType.SCRIPT_TYPE);
		piePropertyList[4].setDisplayNameID("TestingMatrix.test3.onPrepare"); //$NON-NLS-1$
		IMethodInfo methodInfo = new MethodInfo(true);
		piePropertyList[4].setMethdInfo(methodInfo);
		methods[0] = piePropertyList[4];

		piePropertyList[5] = new PropertyDefinitionImpl();
		piePropertyList[5].setName(ON_CREATE_PROP);
		piePropertyList[5].setType(PropertyType.SCRIPT_TYPE);
		piePropertyList[5].setDisplayNameID("TestingMatrix.test3.onCreate"); //$NON-NLS-1$
		methodInfo = new MethodInfo(true);
		piePropertyList[5].setMethdInfo(methodInfo);
		methods[1] = piePropertyList[5];

		barPropertyList = new PropertyDefinitionImpl[6];

		barPropertyList[0] = new PropertyDefinitionImpl();
		barPropertyList[0].setName(X_SCALE_RPOP);
		barPropertyList[0].setType(PropertyType.INTEGER_TYPE);
		barPropertyList[0].setDisplayNameID("TestingMatrix.test3.xScale"); //$NON-NLS-1$

		barPropertyList[1] = new PropertyDefinitionImpl();
		barPropertyList[1].setName(Y_SCALE_RPOP);
		barPropertyList[1].setType(PropertyType.INTEGER_TYPE);
		barPropertyList[1].setDisplayNameID("TestingMatrix.test3.yScale"); //$NON-NLS-1$

		barPropertyList[2] = new PropertyDefinitionImpl();
		barPropertyList[2].setName(TYPE_RPOP);
		barPropertyList[2].setType(PropertyType.STRING_TYPE);
		barPropertyList[2].setDisplayNameID("TestingMatrix.test3.type"); //$NON-NLS-1$

		barPropertyList[3] = new PropertyDefinitionImpl();
		barPropertyList[3].setName(COMPANY_RPOP);
		barPropertyList[3].setType(PropertyType.STRING_TYPE);
		barPropertyList[3].setDisplayNameID("TestingMatrix.test3.company"); //$NON-NLS-1$

		barPropertyList[4] = new PropertyDefinitionImpl();
		barPropertyList[4].setName(LINE_STYLE_PROP);
		barPropertyList[4].setType(PropertyType.CHOICE_TYPE);
		barPropertyList[4].setDisplayNameID("TestingMatrix.test3.lineStyle"); //$NON-NLS-1$

		List choices = new ArrayList();
		ChoiceDefinitionImpl choice = new ChoiceDefinitionImpl();
		choice.setName(CHOICE_LINE_STYLE_THIN);
		choice.setValue(Integer.valueOf("1")); //$NON-NLS-1$
		choice.setDisplayNameID("Choice.lineStyle.thin"); //$NON-NLS-1$
		choices.add(choice);

		choice = new ChoiceDefinitionImpl();
		choice.setName(CHOICE_LINE_STYLE_NORMAL);
		choice.setValue(Integer.valueOf("2")); //$NON-NLS-1$
		choice.setDisplayNameID("Choice.lineStyle.normal"); //$NON-NLS-1$
		choices.add(choice);

		choice = new ChoiceDefinitionImpl();
		choice.setName(CHOICE_LINE_STYLE_THICK);
		choice.setValue(Integer.valueOf("3")); //$NON-NLS-1$
		choice.setDisplayNameID("Choice.lineStyle.thick"); //$NON-NLS-1$
		choices.add(choice);

		barPropertyList[4].setChoices(choices);

		barPropertyList[5] = new PropertyDefinitionImpl();
		barPropertyList[5].setName(SCRIPT_PROP);
		barPropertyList[5].setType(PropertyType.SCRIPT_TYPE);
		barPropertyList[5].setDisplayNameID("TestingMatrix.test3.script"); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.model.extension.IExtendedElement#serialize(java.lang.String)
	 */
	@Override
	public ByteArrayOutputStream serialize(String propName) {
		if (TEST3_PROP.equalsIgnoreCase(propName)) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			StringBuilder sb = new StringBuilder();

			if (TYPE_PIE.equalsIgnoreCase(type)) {
				sb.append(TYPE_RPOP + "=" + TYPE_PIE); //$NON-NLS-1$
				sb.append(","); //$NON-NLS-1$
				sb.append(RADIUS_RPOP + "=" + radius); //$NON-NLS-1$
				sb.append(","); //$NON-NLS-1$
				sb.append(WIDTH_RPOP + "=" + width); //$NON-NLS-1$
				sb.append(","); //$NON-NLS-1$
				sb.append(HEIGHT_RPOP + "=" + height); //$NON-NLS-1$
			} else {
				sb.append(TYPE_RPOP + "=" + TYPE_BAR); //$NON-NLS-1$
				sb.append(","); //$NON-NLS-1$
				sb.append(X_SCALE_RPOP + "=" + xScale); //$NON-NLS-1$
				sb.append(","); //$NON-NLS-1$
				sb.append(Y_SCALE_RPOP + "=" + yScale); //$NON-NLS-1$
				sb.append(","); //$NON-NLS-1$
				if (company != null) {
					sb.append(COMPANY_RPOP + "=" + company); //$NON-NLS-1$
					sb.append(","); //$NON-NLS-1$
				}
				sb.append(LINE_STYLE_PROP + "=" + lineStyle); //$NON-NLS-1$
				if (script != null) {
					sb.append(","); //$NON-NLS-1$
					sb.append(SCRIPT_PROP + "=" + script); //$NON-NLS-1$
				}
			}

			try {
				stream.write(sb.toString().getBytes());
			} catch (IOException e1) {
				assert false;
			}

			return stream;
		} else if (WIDTH_PROP.equalsIgnoreCase(propName)) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			StringBuilder sb = new StringBuilder();

			sb.append(WIDTH_PROP + "=" + widthValue);//$NON-NLS-1$

			try {
				stream.write(sb.toString().getBytes());
			} catch (IOException e1) {
				assert false;
			}

			return stream;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.extension.IReportItem#deserialize(java.lang.
	 * String, java.io.ByteArrayInputStream)
	 */
	@Override
	public void deserialize(String propName, ByteArrayInputStream data) throws ExtendedElementException {
		if (TEST3_PROP.equalsIgnoreCase(propName)) {
			assert data != null;

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int b;
			while ((b = data.read()) > -1) {
				out.write(b);
			}

			String allValue = out.toString();
			if (allValue == null || allValue.length() == 0) {
				// clear all values
				refreshNeeded = true;
				return;
			}
			String[] values = allValue.split(","); //$NON-NLS-1$
			for (int i = 0; i < values.length; i++) {
				String string = values[i];

				String name = string.substring(0, string.indexOf('='));
				String value = string.substring(string.indexOf('=') + 1);

				if (TYPE_RPOP.equalsIgnoreCase(name)) {
					type = value;
				} else if (RADIUS_RPOP.equalsIgnoreCase(name)) {
					radius = Integer.parseInt(value);
				} else if (WIDTH_RPOP.equalsIgnoreCase(name)) {
					width = Integer.parseInt(value);
				} else if (HEIGHT_RPOP.equalsIgnoreCase(name)) {
					height = Integer.parseInt(value);
				} else if (X_SCALE_RPOP.equalsIgnoreCase(name)) {
					xScale = Integer.parseInt(value);
				} else if (Y_SCALE_RPOP.equalsIgnoreCase(name)) {
					yScale = Integer.parseInt(value);
				} else if (COMPANY_RPOP.equalsIgnoreCase(name)) {
					company = value;
				} else if (LINE_STYLE_PROP.equalsIgnoreCase(name)) {
					lineStyle = value;
				} else if (SCRIPT_PROP.equalsIgnoreCase(name)) {
					script = value;
				}
			}
			refreshNeeded = true;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.model.extension.IExtendedElement#getProperty(java.lang.
	 * String)
	 */
	@Override
	public Object getProperty(String propName) {
		if (TEST1_PROP.equalsIgnoreCase(propName)) {
			return test1;
		}
		if (TEST2_PROP.equalsIgnoreCase(propName)) {
			return new Integer(test2);
		} else if (TEST3_PROP.equalsIgnoreCase(propName)) {
			ByteArrayOutputStream stream = serialize(propName);
			return stream.toString();
		} else if (TEST5_PROP.equalsIgnoreCase(propName)) {
			return test5;
		} else if (TEST6_PROP.equalsIgnoreCase(propName)) {
			return test6;
		} else if (TEST7_PROP.equalsIgnoreCase(propName)) {
			return test7;
		} else if (TYPE_RPOP.equalsIgnoreCase(propName)) {
			return type;
		} else if (RADIUS_RPOP.equalsIgnoreCase(propName)) {
			return new Integer(radius);
		} else if (WIDTH_RPOP.equalsIgnoreCase(propName)) {
			return new Integer(width);
		} else if (HEIGHT_RPOP.equalsIgnoreCase(propName)) {
			return new Integer(height);
		} else if (X_SCALE_RPOP.equalsIgnoreCase(propName)) {
			return new Integer(xScale);
		} else if (Y_SCALE_RPOP.equalsIgnoreCase(propName)) {
			return new Integer(yScale);
		} else if (COMPANY_RPOP.equalsIgnoreCase(propName)) {
			return company;
		} else if (LINE_STYLE_PROP.equalsIgnoreCase(propName)) {
			return lineStyle;
		} else if (SCRIPT_PROP.equalsIgnoreCase(propName)) {
			return script;
		} else if (WIDTH_PROP.equalsIgnoreCase(propName)) {
			if (widthValue == null) {
				return "12pt";//$NON-NLS-1$
			} else {
				return widthValue;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.model.extension.IExtendedElement#checkProperty(java.lang.
	 * String, java.lang.Object)
	 */
	@Override
	public void checkProperty(String propName, Object value) throws ExtendedElementException {
		if (LINE_STYLE_PROP.equalsIgnoreCase(propName)) {

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.model.extension.IExtendedElement#setProperty(java.lang.
	 * String, java.lang.Object)
	 */

	@Override
	public void setProperty(String propName, Object value) {
		IElementCommand command = new ElementCommandImpl(this, propName, value, extItemHandle);
		moduleHandle.getCommandStack().execute(command);
	}

	/**
	 * Sets the property.
	 *
	 * @param propName the property name
	 * @param value    the value to set
	 */

	public void doSetProperty(String propName, Object value) {
		if (value == null) {
			return;
		}

		if (TEST1_PROP.equalsIgnoreCase(propName)) {
			test1 = value.toString();
		} else if (TEST2_PROP.equalsIgnoreCase(propName)) {
			test2 = ((Integer) value).intValue();
		} else if (TEST3_PROP.equalsIgnoreCase(propName)) {
			try {
				ByteArrayInputStream stream = new ByteArrayInputStream(value.toString().getBytes());
				deserialize(propName, stream);
				refreshNeeded = true;
			} catch (ExtendedElementException e) {
				e.printStackTrace();
				assert false;
			}
		} else if (TEST5_PROP.equalsIgnoreCase(propName)) {
			test5 = value.toString();
		} else if (TEST6_PROP.equalsIgnoreCase(propName)) {
			test6 = value.toString();
		} else if (TEST7_PROP.equalsIgnoreCase(propName)) {
			test7 = value.toString();
		} else if (TYPE_RPOP.equalsIgnoreCase(propName)) {
			type = value.toString();
			refreshNeeded = true;
		} else if (RADIUS_RPOP.equalsIgnoreCase(propName)) {
			radius = ((Integer) value).intValue();
		} else if (WIDTH_RPOP.equalsIgnoreCase(propName)) {
			width = ((Integer) value).intValue();
		} else if (HEIGHT_RPOP.equalsIgnoreCase(propName)) {
			height = ((Integer) value).intValue();
		} else if (X_SCALE_RPOP.equalsIgnoreCase(propName)) {
			xScale = ((Integer) value).intValue();
		} else if (Y_SCALE_RPOP.equalsIgnoreCase(propName)) {
			yScale = ((Integer) value).intValue();
		} else if (COMPANY_RPOP.equalsIgnoreCase(propName)) {
			company = value.toString();
		} else if (LINE_STYLE_PROP.equalsIgnoreCase(propName)) {
			lineStyle = value.toString();
		} else if (SCRIPT_PROP.equalsIgnoreCase(propName)) {
			script = value.toString();
		} else if (WIDTH_PROP.equalsIgnoreCase(propName)) {
			widthValue = value.toString();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.model.extension.IExtendedElement#validate()
	 */
	@Override
	public List validate() {

		ExtendedElementException exception = new ExtendedElementException(extItemHandle.getElement(),
				"test.testingmatrix.plugin", "Test.TestingMatrix.ExtendedElementException", null);//$NON-NLS-1$ //$NON-NLS-2$
		exception.setProperty(ExtendedElementException.LINE_NUMBER, "11"); //$NON-NLS-1$
		List list = new ArrayList();
		list.add(exception);

		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.model.extension.IExtendedElement#copy()
	 */
	@Override
	public IReportItem copy() {
		try {
			return (IReportItem) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#getPropertyDefinitions()
	 */
	@Override
	public IPropertyDefinition[] getPropertyDefinitions() {
		if (TYPE_PIE.equalsIgnoreCase(type)) {
			return piePropertyList;
		}

		return barPropertyList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#refreshPropertyDefinition()
	 */
	@Override
	public boolean refreshPropertyDefinition() {
		if (refreshNeeded) {
			refreshNeeded = false;
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IReportItem#getMethods()
	 */

	@Override
	public IPropertyDefinition[] getMethods() {
		return methods;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.ICompatibleReportItem#
	 * getRowExpressions()
	 */

	@Override
	public List getRowExpressions() {
		List exprs = new ArrayList();
		exprs.add("row[\"value1\"] = row[\"value2\"] + row[\"value3\"]"); //$NON-NLS-1$
		exprs.add("row[\"tmpValue\"]"); //$NON-NLS-1$
		exprs.add("row[\"tmpValue\"]"); //$NON-NLS-1$
		return exprs;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.ICompatibleReportItem#
	 * updateRowExpressions(java.util.Map)
	 */

	@Override
	public void updateRowExpressions(Map newExpressions) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#getPredefinedStyles()
	 */
	@Override
	public List getPredefinedStyles() {
		List styles = new ArrayList();

		DesignElementHandle e = extItemHandle;
		while (e != null) {
			DesignElementHandle container = e.getContainer();
			if (container instanceof ExtendedItemHandle
					&& ("TestingBox".equals(container.getStringProperty(ExtendedItemHandle.EXTENSION_NAME_PROP))) //$NON-NLS-1$
					&& e.getContainerPropertyHandle() != null
					&& e.getContainerPropertyHandle().getDefn().getName().equalsIgnoreCase("detail")) //$NON-NLS-1$
			{
				styles.add("testing-box-detail"); //$NON-NLS-1$
				StyleHandle style = this.moduleHandle.findStyle("testPredefinedStyle"); //$NON-NLS-1$
				if (style != null) {
					styles.add(new MatrixStyle(style));
				}
				break;
			}
			e = e.getContainer();
		}

		return styles;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#getSimpleElement()
	 */

	@Override
	public org.eclipse.birt.report.model.api.simpleapi.IReportItem getSimpleElement() {
		return new Matrix((ExtendedItemHandle) extItemHandle);
	}
}
