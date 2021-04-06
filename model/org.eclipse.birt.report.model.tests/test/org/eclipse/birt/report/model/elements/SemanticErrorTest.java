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

package org.eclipse.birt.report.model.elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by SemanticError.
 */

public class SemanticErrorTest extends BaseTestCase {

	/**
	 * Tests the error message.
	 * 
	 * @throws Exception
	 */

	public void testErrorMessages() throws Exception {

		DesignElement table = new TableItem();
		os = new ByteArrayOutputStream();
		table.setName("customerTable"); //$NON-NLS-1$

		print(table, SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT);
		print(table, SemanticError.DESIGN_EXCEPTION_INCONSITENT_GRID_COL_COUNT);
		print2(table, new String[] { ReportDesignConstants.TABLE_ITEM, "test table" }, //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS);
		print2(table, new String[] { ReportDesignConstants.TABLE_ITEM, null },
				SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS);
		print(table, SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_SIZE);
		print(table, SemanticError.DESIGN_EXCEPTION_MISSING_PAGE_SIZE);
		print(table, SemanticError.DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE);
		print(table, SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_MARGINS);
		print(table, SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE);
		print(table, SemanticError.DESIGN_EXCEPTION_INVALID_MULTI_COLUMN);
		print2(table, new String[] { ReportItem.DATA_SET_PROP, "unresolvedDataSet" }, //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF);
		print(table, SemanticError.DESIGN_EXCEPTION_MISSING_SQL_STMT);
		print(table, SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET);
		print1(table, "inexsistent.bmp", //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_IMAGE_FILE_NOT_EXIST);
		print1(table, "abc", //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_INVALID_PROPERTY_NAME);
		print(table, SemanticError.DESIGN_EXCEPTION_AT_LEAST_ONE_COLUMN);
		print(table, SemanticError.DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT);
		print1(table, "group1", SemanticError.DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME); //$NON-NLS-1$
		print1(table, "red", SemanticError.DESIGN_EXCEPTION_INVALID_CUSTOM_COLOR_NAME); //$NON-NLS-1$
		print1(table, "BgColor", SemanticError.DESIGN_EXCEPTION_DUPLICATE_CUSTOM_COLOR_NAME); //$NON-NLS-1$
		print2(table, new String[] { "test" }, //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_VALUE_FORBIDDEN);

		TemplateParameterDefinition param = new TemplateParameterDefinition("test"); //$NON-NLS-1$
		print1(param, param.getIdentifier(), SemanticError.DESIGN_EXCEPTION_MISSING_TEMPLATE_PARAMETER_TYPE);

		param.setAllowedType(ReportDesignConstants.FREE_FORM_ITEM);
		DesignElement label = new Label("label"); //$NON-NLS-1$
		param.getSlot(TemplateParameterDefinition.DEFAULT_SLOT).add(label);
		print2(param,
				new String[] { param.getIdentifier(), label.getIdentifier(), ReportDesignConstants.FREE_FORM_ITEM },
				SemanticError.DESIGN_EXCEPTION_INCONSISTENT_TEMPLATE_PARAMETER_TYPE);

		JointDataSet jointDataSet = new JointDataSet("JointDataSet"); //$NON-NLS-1$
		print2(jointDataSet, new String[] { "jointDataSet", "leftDataSet" }, //$NON-NLS-1$ //$NON-NLS-2$
				SemanticError.DESIGN_EXCEPTION_DATA_SET_MISSED_IN_JOINT_DATA_SET);
		os.close();

		assertTrue(compareFile("SemanticError.golden.txt")); //$NON-NLS-1$
	}

	private void print(DesignElement element, String errorCode) {
		SemanticError error = new SemanticError(element, errorCode);
		String code = error.getErrorCode();
		try {
			os.write(code.getBytes());
			for (int i = code.length(); i < 60; i++)
				os.write(' ');
			os.write(error.getMessage().getBytes());
			os.write('\n');
		} catch (IOException e) {
			assert false;
		}
	}

	private void print1(DesignElement element, String value, String errorCode) {
		SemanticError error = new SemanticError(element, new String[] { value }, errorCode);
		String code = error.getErrorCode();
		try {
			os.write(code.getBytes());
			for (int i = code.length(); i < 60; i++)
				os.write(' ');
			os.write(error.getMessage().getBytes());
			os.write('\n');
		} catch (IOException e) {
			assert false;
		}
	}

	private void print2(DesignElement element, String[] params, String errorCode) {
		SemanticError error = new SemanticError(element, params, errorCode);
		String code = error.getErrorCode();
		try {
			os.write(code.getBytes());
			for (int i = code.length(); i < 60; i++)
				os.write(' ');
			os.write(error.getMessage().getBytes());
			os.write('\n');
		} catch (IOException e) {
			assert false;
		}
	}
}