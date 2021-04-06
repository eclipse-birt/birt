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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.CustomMsgEvent;
import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * 
 * TestCases for CustomMsgCommand.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testAddTranslation()}</td>
 * <td>ULocale duplicate.</td>
 * <td>Command failed, exception is thrown.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Resource key is missing.</td>
 * <td>Command failed, exception is thrown.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Command success.</td>
 * <td>The translation is added to the report.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testDropTranslation()}</td>
 * <td>Command success.</td>
 * <td>The translation is removed from the design.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Resource key is not provided.</td>
 * <td>Exception should be thrown.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetULocale()}</td>
 * <td>command success.</td>
 * <td>the old translation is dropped, the new translation with new locale is
 * added.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>the new locale duplicate exists ones</td>
 * <td>command failed, the old translation is not changed.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetText()}</td>
 * <td>command success.</td>
 * <td>the old translation is dropped, the new translation with new text is
 * added</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>the translation is not find in the report</td>
 * <td>command should failed, exception thrown</td>
 * </tr>
 * 
 * 
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Test writer.</td>
 * <td></td>
 * </tr>
 * 
 */

public class CustomMsgCommandTest extends BaseTestCase {

	CustomMsgCommand command = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("CustomMsgCommandTest.xml"); //$NON-NLS-1$
		command = new CustomMsgCommand(design);
	}

	/**
	 * test add a new translation to the design. 1. locale duplicate, command
	 * failed. 2. if resource key is missing, command failed. 3. if command success,
	 * the translation is added to the report.
	 * <p>
	 * 
	 * @throws CustomMsgException
	 */
	public void testAddTranslation() throws CustomMsgException {
		assertEquals(6, design.getTranslations().size());

		String resourceKey = "ResourceKey.Element1.Title"; //$NON-NLS-1$
		String locale = "zh_CN"; //$NON-NLS-1$
		String text = "New element"; //$NON-NLS-1$

		// success;
		command.addTranslation(resourceKey, locale, text);

		assertNotNull(design.findTranslation(resourceKey, locale));
		assertEquals(7, design.getTranslations().size());

		// duplicate locale

		try {
			command.addTranslation(resourceKey, locale, text);
			fail();
		} catch (CustomMsgException e) {
			assertEquals(CustomMsgException.DESIGN_EXCEPTION_DUPLICATE_LOCALE, e.getErrorCode());
		}

		// resource key required
		resourceKey = ""; //$NON-NLS-1$
		locale = "en"; //$NON-NLS-1$
		try {
			command.addTranslation(resourceKey, locale, text);
			fail();
		} catch (CustomMsgException e) {
			assertEquals(CustomMsgException.DESIGN_EXCEPTION_RESOURCE_KEY_REQUIRED, e.getErrorCode());
		}

		// invalid locale
		// Note: This case can not be tested for StringUtil.isValidULocale()
		// always return true.
		//
		// translation = new Translation( "resourceKey", "bad locale", text );
		// //$NON-NLS-1$ //$NON-NLS-2$
		// try
		// {
		// command.addTranslation( translation );
		// fail( );
		// }
		// catch ( CustomMsgException e )
		// {
		// assertEquals( CustomMsgException.INVALID_LOCALE, e.getErrorCode( ) );
		// }
	}

	/**
	 * test remove a translation from the design. 1. command success, the
	 * translation is removed from the design. 2. if resource key is not provided,
	 * exception should be thrown.
	 * <p>
	 * 
	 * @throws CustomMsgException
	 * 
	 */
	public void testDropTranslation() throws CustomMsgException {
		assertEquals(6, design.getTranslations().size());

		String resourceKey = "ResourceKey.ReportDesign.Title"; //$NON-NLS-1$
		String locale = null;

		command.dropTranslation(resourceKey, locale);
		assertEquals(5, design.getTranslations().size());

		assertNull(design.findTranslation(resourceKey, locale));

		try {
			command.dropTranslation(null, locale);
			fail();
		} catch (CustomMsgException e) {
			assertEquals(CustomMsgException.DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND, e.getErrorCode());
		}

	}

	/**
	 * test set locale for a translation. 1. command success, the old translation is
	 * dropped, the new translation with new locale is added.
	 * 
	 * @throws CustomMsgException
	 */
	public void testSetULocale() throws CustomMsgException {

		Translation translation = design.findTranslation("ResourceKey.ReportDesign.Title", //$NON-NLS-1$
				"zh_CN"); //$NON-NLS-1$

		assertNotNull(translation);

		command.setLocale(translation, "fr"); //$NON-NLS-1$
		assertEquals("fr", translation.getLocale()); //$NON-NLS-1$

		try {
			command.setLocale(translation, "en"); //$NON-NLS-1$
			fail();
		} catch (CustomMsgException e) {
			assertEquals(CustomMsgException.DESIGN_EXCEPTION_DUPLICATE_LOCALE, e.getErrorCode());
		}
	}

	/**
	 * test set text for a translation. 1. command success, the old translation is
	 * dropped, the new translation with new text is added. 2. the translation is
	 * not find in the report, command should failed.
	 * 
	 * @throws CustomMsgException
	 */

	public void testSetText() throws CustomMsgException {
		String resourceKey = "ResourceKey.ReportDesign.Title"; //$NON-NLS-1$
		String locale = "zh_CN"; //$NON-NLS-1$
		String oldText = "zh_CN:\u7b80\u5355\u62a5\u8868."; //$NON-NLS-1$
		String newText = "new Text"; //$NON-NLS-1$

		// 1

		Translation translation = design.findTranslation(resourceKey, locale);
		assertEquals(oldText, translation.getText());

		command.setText(translation, newText);
		translation = design.findTranslation(resourceKey, locale);
		assertEquals(newText, translation.getText());

		// The resource key is not found

		try {
			command.setText(new Translation("none-exsit-resourceKey", "zh_CN"), newText); //$NON-NLS-1$//$NON-NLS-2$
			fail();
		} catch (CustomMsgException e) {
			assertEquals(CustomMsgException.DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND, e.getErrorCode());
		}
	}

	/**
	 * test designWriter.
	 * 
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		save();
		assertTrue(compareFile("CustomMsgCommandTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test event notification.
	 * 
	 * @throws Exception if any exception
	 */

	public void testNotification() throws Exception {
		String resourceKey = "ResourceKey.Element1.TestKey"; //$NON-NLS-1$
		String locale = "zh_CN"; //$NON-NLS-1$
		String text = "New element"; //$NON-NLS-1$

		MyCustomMsgListener listener = new MyCustomMsgListener();
		design.addListener(listener);

		// Add translation

		command.addTranslation(resourceKey, locale, text);
		assertEquals(MyCustomMsgListener.ADDED, listener.action);

		// Drop translation

		command.dropTranslation(resourceKey, locale);
		assertEquals(MyCustomMsgListener.REMOVED, listener.action);
	}

	class MyCustomMsgListener implements Listener {

		static final int NA = 0;
		static final int ADDED = 1;
		static final int REMOVED = 2;

		CustomMsgEvent event = null;

		int action = NA;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt.report.
		 * model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			CustomMsgEvent event = (CustomMsgEvent) ev;

			switch (event.getAction()) {
			case CustomMsgEvent.ADD:
				this.event = event;
				action = ADDED;
				break;

			case CustomMsgEvent.DROP:
				this.event = event;
				action = REMOVED;
				break;

			case CustomMsgEvent.CHANGE:
				break;
			}

		}

	}

}