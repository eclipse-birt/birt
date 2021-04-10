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

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests all the new features about the tight coupling a structure reference.
 */

public class StructRefTest extends BaseTestCase {

	private static final String fileName = "StructRefTest.xml"; //$NON-NLS-1$
	private static final String fileName_1 = "StructRefTest_1.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testSemanticCheck() throws Exception {
		openDesign(fileName_1);

		// image item refers an embedded image that is not found in report
		// design
		assertEquals(1, designHandle.getErrorList().size());
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testStructReferenceBySet() throws Exception {
		openDesign(fileName);
		assertEquals(0, designHandle.getErrorList().size());

		EmbeddedImage emImageOne = design.findImage("image one"); //$NON-NLS-1$
		EmbeddedImage emImageTwo = design.findImage("image two"); //$NON-NLS-1$
		assertNotNull(emImageOne);
		assertNotNull(emImageTwo);
		List clients = emImageOne.getClientList();
		assertEquals(2, clients.size());
		assertEquals("Image1", ((BackRef) clients.get(0)).getElement().getName()); //$NON-NLS-1$
		assertEquals(IImageItemModel.IMAGE_NAME_PROP, ((BackRef) clients.get(0)).getPropertyName());
		assertEquals("Image2", ((BackRef) clients.get(1)).getElement().getName()); //$NON-NLS-1$
		assertEquals(IImageItemModel.IMAGE_NAME_PROP, ((BackRef) clients.get(1)).getPropertyName());
		clients = emImageTwo.getClientList();
		assertEquals(1, clients.size());
		assertEquals("Image3", ((BackRef) clients.get(0)).getElement().getName()); //$NON-NLS-1$
		assertEquals(IImageItemModel.IMAGE_NAME_PROP, ((BackRef) clients.get(0)).getPropertyName());

		// create a new image item to refers the embedded image

		ActivityStack stack = design.getActivityStack();
		ImageHandle imageHandle = designHandle.getElementFactory().newImage("Image4"); //$NON-NLS-1$
		SlotHandle body = designHandle.getBody();
		body.add(imageHandle);
		imageHandle.setImageName("image one"); //$NON-NLS-1$
		assertEquals(3, emImageOne.getClientList().size());
		stack.undo();
		assertEquals(2, emImageOne.getClientList().size());
		stack.redo();
		assertEquals(3, emImageOne.getClientList().size());

		// clear the image name of some image item, then the back reference is
		// removed

		imageHandle = (ImageHandle) designHandle.findElement("Image1"); //$NON-NLS-1$
		imageHandle.setImageName(null);
		assertEquals(2, emImageOne.getClientList().size());
		stack.undo();
		assertEquals(3, emImageOne.getClientList().size());
		stack.redo();
		assertEquals(2, emImageOne.getClientList().size());

		// set the image name with the undefined embedded image

		imageHandle.setImageName("wrong image"); //$NON-NLS-1$
		StructRefValue value = (StructRefValue) imageHandle.getElement().getProperty(design,
				IImageItemModel.IMAGE_NAME_PROP);
		assertFalse(value.isResolved());
		assertEquals("wrong image", imageHandle.getImageName()); //$NON-NLS-1$
		imageHandle.getElement().validateWithContents(design);
		assertTrue(imageHandle.getElement().getErrors().size() > 0);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testAddRemoveImageItem() throws Exception {
		openDesign(fileName);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testDeleteAndReplace() throws Exception {
		openDesign(fileName);
		EmbeddedImage emImageOne = design.findImage("image one"); //$NON-NLS-1$
		EmbeddedImage emImageTwo = design.findImage("image two"); //$NON-NLS-1$
		assertNotNull(emImageOne);
		assertNotNull(emImageTwo);
		PropertyHandle images = designHandle.getPropertyHandle(ReportDesignHandle.IMAGES_PROP);
		assertNotNull(images);

		// delete an embedded image, then the image items that refer
		// it will be unresolved and be sent the notification
		ActivityStack stack = design.getActivityStack();

		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("Image3"); //$NON-NLS-1$
		assertNotNull(imageHandle);
		MyListener listener = new MyListener();
		imageHandle.addListener(listener);
		images.removeItem(emImageTwo);
		// assertEquals( imageHandle.getName( ) + MyListener.STATUS,
		// listener.getNotification( ) );

		stack.undo();
		List imageList = images.getListValue();
		assertEquals(2, imageList.size());
		imageHandle = (ImageHandle) designHandle.findElement("Image3"); //$NON-NLS-1$

		imageHandle.removeListener(listener);
		listener = new MyListener();
		imageHandle.addListener(listener);
		EmbeddedImage newImage = (EmbeddedImage) emImageTwo.copy();
		newImage.setName("image new"); //$NON-NLS-1$
		images.replaceItem(emImageTwo, newImage);
		assertEquals(imageHandle.getName() + MyListener.STATUS, listener.getNotification());
		imageHandle.getElement().validateWithContents(design);
		assertTrue(imageHandle.getElement().getErrors().size() > 0);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testAdd() throws Exception {
		openDesign(fileName);
		EmbeddedImage emImageOne = design.findImage("image one"); //$NON-NLS-1$
		EmbeddedImage emImageTwo = design.findImage("image two"); //$NON-NLS-1$
		assertNotNull(emImageOne);
		assertNotNull(emImageTwo);
		PropertyHandle images = designHandle.getPropertyHandle(ReportDesignHandle.IMAGES_PROP);
		assertNotNull(images);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testSetProperty() throws Exception {
		openDesign(fileName);
		EmbeddedImage emImageOne = design.findImage("image one"); //$NON-NLS-1$
		EmbeddedImage emImageTwo = design.findImage("image two"); //$NON-NLS-1$
		assertNotNull(emImageOne);
		assertNotNull(emImageTwo);
		PropertyHandle images = designHandle.getPropertyHandle(ReportDesignHandle.IMAGES_PROP);
		assertNotNull(images);
		StructureHandle emImageTwoHandle = images.getAt(1);
		emImageTwoHandle.getMember(EmbeddedImage.NAME_MEMBER).setValue("image new"); //$NON-NLS-1$

		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("Image3"); //$NON-NLS-1$
		assertEquals("image new", imageHandle.getImageName()); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testSetMember() throws Exception {
		openDesign(fileName);
		FreeFormHandle element = designHandle.getElementFactory().newFreeForm("test"); //$NON-NLS-1$
		designHandle.getBody().add(element);
		PropertyHandle mapRules = element.getPropertyHandle(IStyleModel.MAP_RULES_PROP);
		MapRule mapRule = StructureFactory.createMapRule();
		assertNotNull(mapRules);
		mapRules.addItem(mapRule);
		assertEquals(1, mapRules.getListValue().size());
		assertEquals(mapRule, mapRules.getListValue().get(0));
		MapRuleHandle ruleHandle = (MapRuleHandle) mapRules.getAt(0);
		// the member has a default value
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ, ruleHandle.getOperator());
		MyListener listener = new MyListener();
		element.addListener(listener);
		// the local value of the operator member is null, so nothing is done
		ruleHandle.setOperator(null);
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ, ruleHandle.getOperator());
		assertNull(listener.getNotification());
		// set the value to the default value, something done
		ruleHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_EQ);
		assertEquals(element.getName() + MyListener.STATUS, listener.getNotification());
	}

	class MyListener implements Listener {

		String imageName = null;
		static final String STATUS = " : notification"; //$NON-NLS-1$

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.core.Listener#elementChanged(org
		 * .eclipse.birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			imageName = focus.getName() + STATUS;
		}

		/**
		 * 
		 * @return image name
		 */
		public String getNotification() {
			return imageName;
		}

	}
}