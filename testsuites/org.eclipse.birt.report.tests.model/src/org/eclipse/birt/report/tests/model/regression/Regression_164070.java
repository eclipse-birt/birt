/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Bug Description:</b>
 * <p>
 * Support widows/orphans properties on block text item(label, text, data etc),
 * About the definition of widows/orphans, please refer to CSS spec.
 * <p>
 * <b>Test Description:</b>
 * <ol>
 * <li>Set/Get widows/orphans properties on label
 * <li>Set/Get widows/orphans properties on text
 * <li>Set/Get widows/orphans properties on data
 * <li>Set/Get widows/orphans properties on dynamic text
 * </ol>
 */
public class Regression_164070 extends BaseTestCase {

	/**
	 * <li>Set/Get widows/orphans properties on label
	 * <li>Set/Get widows/orphans properties on text
	 * <li>Set/Get widows/orphans properties on data
	 * <li>Set/Get widows/orphans properties on dynamic text
	 *
	 * @throws SemanticException
	 */
	public void test_regression_164070() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();

		// Set/Get pwidows/orphans properties on Label
		LabelHandle label = factory.newLabel("label");
		designHandle.getBody().add(label);
		label.setStringProperty(Style.WIDOWS_PROP, DesignChoiceConstants.WIDOWS_INHERIT);

		assertEquals(DesignChoiceConstants.WIDOWS_INHERIT, label.getStringProperty(Style.WIDOWS_PROP));

		label.setStringProperty(Style.ORPHANS_PROP, DesignChoiceConstants.ORPHANS_INHERIT);

		assertEquals(DesignChoiceConstants.ORPHANS_INHERIT, label.getStringProperty(Style.ORPHANS_PROP));

		// Set/Get widows/orphans properties on text
		TextItemHandle text = factory.newTextItem("text");
		designHandle.getBody().add(text);
		text.setStringProperty(Style.WIDOWS_PROP, DesignChoiceConstants.WIDOWS_INHERIT);

		assertEquals(DesignChoiceConstants.WIDOWS_INHERIT, text.getStringProperty(Style.WIDOWS_PROP));

		text.setStringProperty(Style.ORPHANS_PROP, DesignChoiceConstants.ORPHANS_INHERIT);

		assertEquals(DesignChoiceConstants.ORPHANS_INHERIT, text.getStringProperty(Style.ORPHANS_PROP));

		// Set/Get widows/orphans properties on Data
		DataItemHandle data = factory.newDataItem("data");
		designHandle.getBody().add(data);
		data.setStringProperty(Style.WIDOWS_PROP, DesignChoiceConstants.WIDOWS_INHERIT);

		assertEquals(DesignChoiceConstants.WIDOWS_INHERIT, data.getStringProperty(Style.WIDOWS_PROP));

		data.setStringProperty(Style.ORPHANS_PROP, DesignChoiceConstants.ORPHANS_INHERIT);

		assertEquals(DesignChoiceConstants.ORPHANS_INHERIT, data.getStringProperty(Style.ORPHANS_PROP));

		// Set/Get widows/orphans properties on Dynamic text
		TextDataHandle dtext = factory.newTextData("dtext");
		designHandle.getBody().add(dtext);
		dtext.setStringProperty(Style.WIDOWS_PROP, DesignChoiceConstants.WIDOWS_INHERIT);

		assertEquals(DesignChoiceConstants.WIDOWS_INHERIT, dtext.getStringProperty(Style.WIDOWS_PROP));

		dtext.setStringProperty(Style.ORPHANS_PROP, DesignChoiceConstants.ORPHANS_INHERIT);

		assertEquals(DesignChoiceConstants.ORPHANS_INHERIT, dtext.getStringProperty(Style.ORPHANS_PROP));
	}
}
