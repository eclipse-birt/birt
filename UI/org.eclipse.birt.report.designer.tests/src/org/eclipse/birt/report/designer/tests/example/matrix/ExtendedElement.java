/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.tests.example.matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IElementCommand;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.extension.ReportItem;
import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * Implements the peer extension element.
 */

public class ExtendedElement extends ReportItem {

	public static final String TEST1_PROP = "test1"; //$NON-NLS-1$
	public static final String TEST2_PROP = "test2"; //$NON-NLS-1$
	public static final String TEST3_PROP = "test3"; //$NON-NLS-1$
	public static final String TEST5_PROP = "test5"; //$NON-NLS-1$
	public static final String TEST6_PROP = "test6"; //$NON-NLS-1$
	public static final String TEST7_PROP = "test7"; //$NON-NLS-1$

	public static final String TYPE_RPOP = "type"; //$NON-NLS-1$
	public static final String RADIUS_RPOP = "radius"; //$NON-NLS-1$
	public static final String WIDTH_RPOP = "pieWidth"; //$NON-NLS-1$
	public static final String HEIGHT_RPOP = "pieHeight"; //$NON-NLS-1$
	public static final String X_SCALE_RPOP = "xScale"; //$NON-NLS-1$
	public static final String Y_SCALE_RPOP = "yScale"; //$NON-NLS-1$
	public static final String COMPANY_RPOP = "company"; //$NON-NLS-1$
	public static final String LINE_STYLE_PROP = "lineStyle"; //$NON-NLS-1$

	private static final String TYPE_PIE = "pie"; //$NON-NLS-1$
	private static final String TYPE_BAR = "bar"; //$NON-NLS-1$

	private static final String CHOICE_LINE_STYLE_THIN = "thin"; //$NON-NLS-1$
	private static final String CHOICE_LINE_STYLE_NORMAL = "normal"; //$NON-NLS-1$
	private static final String CHOICE_LINE_STYLE_THICK = "thick"; //$NON-NLS-1$

	// protected HashMap values = new HashMap( );
	protected IReportItemFactory cachedDefn = null;
	protected DesignElementHandle designHandle = null;
	public static String CHECK_PROPERTY_TAG = null;

	private ExtensionPropertyDefn[] piePropertyList = null;
	private ExtensionPropertyDefn[] barPropertyList = null;

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
	 * @param designHandle
	 */

	public ExtendedElement(IReportItemFactory extDefn, DesignElementHandle designHandle) {
		this.cachedDefn = extDefn;
		assert designHandle != null;
		this.designHandle = designHandle;

		piePropertyList = new ExtensionPropertyDefn[4];

		piePropertyList[0] = new ExtensionPropertyDefn();
		piePropertyList[0].setName(RADIUS_RPOP); // $NON-NLS-1$
		piePropertyList[0].setType(PropertyType.INTEGER_TYPE);
		piePropertyList[0].setDisplayNameID("TestingBall.test3.radius"); //$NON-NLS-1$

		piePropertyList[1] = new ExtensionPropertyDefn();
		piePropertyList[1].setName(WIDTH_RPOP); // $NON-NLS-1$
		piePropertyList[1].setType(PropertyType.INTEGER_TYPE);
		piePropertyList[1].setDisplayNameID("TestingBall.test3.width"); //$NON-NLS-1$

		piePropertyList[2] = new ExtensionPropertyDefn();
		piePropertyList[2].setName(HEIGHT_RPOP);
		piePropertyList[2].setType(PropertyType.INTEGER_TYPE);
		piePropertyList[2].setDisplayNameID("TestingBall.test3.height"); //$NON-NLS-1$

		piePropertyList[3] = new ExtensionPropertyDefn();
		piePropertyList[3].setName(TYPE_RPOP); // $NON-NLS-1$
		piePropertyList[3].setType(PropertyType.STRING_TYPE);
		piePropertyList[3].setDisplayNameID("TestingBall.test3.type"); //$NON-NLS-1$

		barPropertyList = new ExtensionPropertyDefn[5];

		barPropertyList[0] = new ExtensionPropertyDefn();
		barPropertyList[0].setName(X_SCALE_RPOP); // $NON-NLS-1$
		barPropertyList[0].setType(PropertyType.INTEGER_TYPE);
		barPropertyList[0].setDisplayNameID("TestingBall.test3.xScale"); //$NON-NLS-1$

		barPropertyList[1] = new ExtensionPropertyDefn();
		barPropertyList[1].setName(Y_SCALE_RPOP); // $NON-NLS-1$
		barPropertyList[1].setType(PropertyType.INTEGER_TYPE);
		barPropertyList[1].setDisplayNameID("TestingBall.test3.yScale"); //$NON-NLS-1$

		barPropertyList[2] = new ExtensionPropertyDefn();
		barPropertyList[2].setName(TYPE_RPOP); // $NON-NLS-1$
		barPropertyList[2].setType(PropertyType.STRING_TYPE);
		barPropertyList[2].setDisplayNameID("TestingBall.test3.type"); //$NON-NLS-1$

		barPropertyList[3] = new ExtensionPropertyDefn();
		barPropertyList[3].setName(COMPANY_RPOP); // $NON-NLS-1$
		barPropertyList[3].setType(PropertyType.STRING_TYPE);
		barPropertyList[3].setDisplayNameID("TestingBall.test3.company"); //$NON-NLS-1$

		barPropertyList[4] = new ExtensionPropertyDefn();
		barPropertyList[4].setName(LINE_STYLE_PROP); // $NON-NLS-1$
		barPropertyList[4].setType(PropertyType.CHOICE_TYPE);
		barPropertyList[4].setDisplayNameID("TestingBall.test3.lineStyle"); //$NON-NLS-1$

		List choices = new ArrayList();
		ChoiceDefn choice = new ChoiceDefn();
		choice.setName(CHOICE_LINE_STYLE_THIN);
		choice.setValue(Integer.valueOf("1")); //$NON-NLS-1$
		choice.setDisplayNameID("Choice.lineStyle.thin"); //$NON-NLS-1$
		choices.add(choice);

		choice = new ChoiceDefn();
		choice.setName(CHOICE_LINE_STYLE_NORMAL);
		choice.setValue(Integer.valueOf("2")); //$NON-NLS-1$
		choice.setDisplayNameID("Choice.lineStyle.normal"); //$NON-NLS-1$
		choices.add(choice);

		choice = new ChoiceDefn();
		choice.setName(CHOICE_LINE_STYLE_THICK);
		choice.setValue(Integer.valueOf("3")); //$NON-NLS-1$
		choice.setDisplayNameID("Choice.lineStyle.thick"); //$NON-NLS-1$
		choices.add(choice);

		barPropertyList[4].setChoices(choices);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.model.extension.IExtendedElement#serialize(java.lang.String)
	 */
	public ByteArrayOutputStream serialize(String propName) {
		if (TEST3_PROP.equalsIgnoreCase(propName)) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			StringBuffer sb = new StringBuffer();

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
			}

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
	 * @see org.eclipse.birt.report.model.extension.IElement#deserialize(java.lang.
	 * String, java.io.ByteArrayInputStream)
	 */
	public void deserialize(String propName, ByteArrayInputStream data) throws ExtendedElementException {
		if (TEST3_PROP.equalsIgnoreCase(propName)) {
			assert data != null;

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int b;
			while ((b = data.read()) > -1)
				out.write(b);

			String allValue = out.toString();
			if (allValue == null || allValue.length() == 0) {
				// clear all values
				refreshNeeded = true;
				return;
			}
			String[] values = allValue.split(","); //$NON-NLS-1$
			for (int i = 0; i < values.length; i++) {
				String string = values[i];

				String name = string.substring(0, string.indexOf('=')); // $NON-NLS-1$
				String value = string.substring(string.indexOf('=') + 1); // $NON-NLS-1$

				if (TYPE_RPOP.equalsIgnoreCase(name))
					type = value;
				else if (RADIUS_RPOP.equalsIgnoreCase(name))
					radius = Integer.parseInt(value);
				else if (WIDTH_RPOP.equalsIgnoreCase(name))
					width = Integer.parseInt(value);
				else if (HEIGHT_RPOP.equalsIgnoreCase(name))
					height = Integer.parseInt(value);
				else if (X_SCALE_RPOP.equalsIgnoreCase(name))
					xScale = Integer.parseInt(value);
				else if (Y_SCALE_RPOP.equalsIgnoreCase(name))
					yScale = Integer.parseInt(value);
				else if (COMPANY_RPOP.equalsIgnoreCase(name))
					company = value;
				else if (LINE_STYLE_PROP.equalsIgnoreCase(name))
					lineStyle = value;
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
	public void checkProperty(String propName, Object value) throws ExtendedElementException {
		CHECK_PROPERTY_TAG = propName;

		if (LINE_STYLE_PROP.equalsIgnoreCase(propName)) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#setProperty(java.lang.
	 * String, java.lang.Object)
	 */
	public void setProperty(String propName, Object value) {
		execute(this.getElementCommand(propName, value));
	}

	public void doSetProperty(String propName, Object value) {
		if (value == null)
			return;

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
		} else if (COMPANY_RPOP.equalsIgnoreCase(propName)) // $NON-NLS-1$
		{
			company = value.toString();
		} else if (LINE_STYLE_PROP.equalsIgnoreCase(propName)) {
			lineStyle = value.toString();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#copy()
	 */
	public IReportItem copy() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.model.extension.IExtendedElement#getElementCommand(java.lang
	 * .String, java.lang.Object)
	 */
	public IElementCommand getElementCommand(String propName, Object value) {
		return new ExtendedElementCommand(designHandle, this, propName, value);
	}

	private void execute(IElementCommand command) {
		// designHandle.getDesign( ).getActivityStack( ).execute( command );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#getPropertyDefinitions()
	 */
	public IPropertyDefinition[] getPropertyDefinitions() {
		if (TYPE_PIE.equalsIgnoreCase(type))
			return piePropertyList;

		return barPropertyList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#refreshPropertyDefinition()
	 */
	public boolean refreshPropertyDefinition() {
		if (refreshNeeded) {
			refreshNeeded = false;
			return true;
		}

		return false;
	}

	public IPropertyDefinition[] getMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPropertyDefinition getScriptPropertyDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	public List validate() {
		// TODO Auto-generated method stub
		return null;
	}

}