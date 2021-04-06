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

package org.eclipse.birt.report.model.library;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the usage of structures in libraries.
 */

public class LibraryStructureTest extends BaseTestCase {

	/**
	 * Tests the usage of "libReference" in design file.
	 * 
	 * @throws Exception
	 */

	public void testLibReference() throws Exception {
		openDesign("LibraryStructureTest.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		// get the design embedded image

		List images = designHandle.getListProperty(ReportDesign.IMAGES_PROP);
		assertEquals(2, images.size());
		EmbeddedImage image = designHandle.findImage("design image2"); //$NON-NLS-1$
		assertNotNull(image);
		StructRefValue libReference = (StructRefValue) image.getProperty(design,
				ReferencableStructure.LIB_REFERENCE_MEMBER);
		assertNotNull(libReference);
		assertTrue(libReference.isResolved());

		// look up the library embedded image, and check the reference

		LibraryHandle includeLib = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		assertNotNull(includeLib);
		EmbeddedImage includeImage = includeLib.findImage("image3"); //$NON-NLS-1$
		assertNotNull(includeImage);
		assertEquals(includeImage, libReference.getTargetStructure());

		// check the data and type of the design image -- equal those of the
		// library image

		assertNull(image.getLocalProperty(design, EmbeddedImage.DATA_MEMBER));
		assertTrue(image.getProperty(design, EmbeddedImage.DATA_MEMBER) == includeImage
				.getProperty(includeLib.getModule(), EmbeddedImage.DATA_MEMBER));
		assertTrue(Arrays.equals(image.getData(design), includeImage.getData(includeLib.getModule())));

		save();

		// check the "imageName" of the image items in design

		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("Image1"); //$NON-NLS-1$
		assertNotNull(imageHandle);
		assertEquals("design image1", imageHandle.getEmbeddedImage().getQualifiedName()); //$NON-NLS-1$

		imageHandle = (ImageHandle) designHandle.findElement("Image2"); //$NON-NLS-1$
		assertNotNull(imageHandle);
		assertEquals("design image2", imageHandle.getEmbeddedImage().getQualifiedName()); //$NON-NLS-1$

	}

	/**
	 * Tests the resolve and unresolve of the structures.
	 * 
	 * @throws Exception
	 */

	public void testResolveForStructure() throws Exception {
		openDesign("LibraryStructureTest_1.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		// test the unresolve status of some embedded image and image item

		EmbeddedImage image = designHandle.findImage("design image4"); //$NON-NLS-1$
		assertNotNull(image);
		StructRefValue libReference = (StructRefValue) image.getProperty(design,
				ReferencableStructure.LIB_REFERENCE_MEMBER);
		assertNotNull(libReference);
		assertFalse(libReference.isResolved());
		assertEquals("Lib2.image3", libReference.getQualifiedReference()); //$NON-NLS-1$

		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("Image2"); //$NON-NLS-1$
		assertNotNull(imageHandle);
		StructRefValue imageName = (StructRefValue) imageHandle.getElement().getProperty(design,
				ImageHandle.IMAGE_NAME_PROP);
		assertNotNull(imageName);
		assertFalse(imageName.isResolved());
		assertEquals("Lib2.image3", imageName.getName()); //$NON-NLS-1$
		assertNull(imageHandle.getEmbeddedImage());

		// add the library_2 to the design

		designHandle.includeLibrary("Library_2.xml", "Lib2"); //$NON-NLS-1$//$NON-NLS-2$
		LibraryHandle includeLib = designHandle.getLibrary("Lib2"); //$NON-NLS-1$
		assertNotNull(includeLib);
		assertEquals(2, designHandle.getLibraries().size());

		// check the unresolved embedded image and image item again

		libReference = (StructRefValue) image.getProperty(design, ReferencableStructure.LIB_REFERENCE_MEMBER);
		assertNotNull(libReference);
		assertTrue(libReference.isResolved());
		assertEquals("Lib2.image3", libReference.getQualifiedReference()); //$NON-NLS-1$

		imageHandle = (ImageHandle) designHandle.findElement("Image2"); //$NON-NLS-1$
		assertNotNull(imageHandle);
		imageName = (StructRefValue) imageHandle.getElement().getProperty(design, ImageHandle.IMAGE_NAME_PROP);
		assertNotNull(imageName);
		assertTrue(imageName.isResolved());
		assertEquals("Lib2.image3", imageName.getQualifiedReference()); //$NON-NLS-1$
		assertEquals("image3", imageName.getName()); //$NON-NLS-1$
		assertEquals("Lib2", imageName.getLibraryNamespace()); //$NON-NLS-1$

		assertNotNull(imageHandle.getEmbeddedImage());

		// drop the library and check the embedded image and image item again

		designHandle.dropLibrary(includeLib);
		libReference = (StructRefValue) image.getProperty(design, ReferencableStructure.LIB_REFERENCE_MEMBER);
		assertNotNull(libReference);
		assertFalse(libReference.isResolved());
		assertEquals("Lib2.image3", libReference.getQualifiedReference()); //$NON-NLS-1$

		imageHandle = (ImageHandle) designHandle.findElement("Image2"); //$NON-NLS-1$
		assertNotNull(imageHandle);
		imageName = (StructRefValue) imageHandle.getElement().getProperty(design, ImageHandle.IMAGE_NAME_PROP);
		assertNotNull(imageName);
		assertFalse(imageName.isResolved());
		assertEquals("Lib2.image3", imageName.getName()); //$NON-NLS-1$
		assertNull(imageHandle.getEmbeddedImage());

		// after undo, it is resolved again.

		designHandle.getCommandStack().undo();
		imageName = (StructRefValue) imageHandle.getElement().getProperty(design, ImageHandle.IMAGE_NAME_PROP);
		assertNotNull(imageName);
		assertTrue(imageName.isResolved());
		assertEquals("Lib2.image3", imageName.getQualifiedReference()); //$NON-NLS-1$
		assertEquals("image3", imageName.getName()); //$NON-NLS-1$
		assertEquals("Lib2", imageName.getLibraryNamespace()); //$NON-NLS-1$

	}

	/**
	 * Tests the visibility of the library structures.
	 * 
	 * @throws Exception
	 */

	public void testVisibilityForStructure() throws Exception {
		openDesign("LibraryStructureTest_2.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		LibraryHandle visibleLib = designHandle.getLibrary("CompositeLib"); //$NON-NLS-1$
		assertNotNull(visibleLib);

		EmbeddedImage image = designHandle.findImage("design image3"); //$NON-NLS-1$
		assertNotNull(image);
		StructRefValue libReference = (StructRefValue) image.getProperty(design,
				ReferencableStructure.LIB_REFERENCE_MEMBER);
		assertNotNull(libReference);
		assertTrue(libReference.isResolved());
		assertEquals("Lib1.image3", libReference.getQualifiedReference()); //$NON-NLS-1$

		LibraryHandle invisibleLib = visibleLib.getLibrary("Lib1"); //$NON-NLS-1$
		assertNotNull(invisibleLib);
		EmbeddedImage invisibleImage = invisibleLib.findImage("image3"); //$NON-NLS-1$
		assertNotNull(invisibleImage);
	}

	/**
	 * Tests the getEmbeddedImage() in ImageHandle when extending or virtual
	 * extending.
	 * 
	 * @throws Exception
	 */

	public void testEmbeddedImageInImageItem() throws Exception {
		openDesign("LibraryStructureTest_3.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		libraryHandle = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		assertNotNull(libraryHandle);
		PropertyHandle images = libraryHandle.getPropertyHandle(Module.IMAGES_PROP);

		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("image"); //$NON-NLS-1$
		assertNotNull(imageHandle);
		assertEquals("Lib1.image1", imageHandle.getImageName()); //$NON-NLS-1$
		assertEquals("Lib1.image1", imageHandle //$NON-NLS-1$
				.getProperty(IImageItemModel.IMAGE_NAME_PROP));

		assertEquals(images.getAt(0).getStructure(), imageHandle.getEmbeddedImage().getStructure());
		GridHandle gridHandle = (GridHandle) designHandle.findElement("grid"); //$NON-NLS-1$
		assertNotNull(gridHandle);
		imageHandle = (ImageHandle) gridHandle.getCell(1, 1).getContent().get(0);
		assertNotNull(imageHandle);
		assertEquals(images.getAt(1).getStructure(), imageHandle.getEmbeddedImage().getStructure());

		assertEquals("Lib1.image2", imageHandle.getImageName()); //$NON-NLS-1$
		assertEquals("Lib1.image2", imageHandle //$NON-NLS-1$
				.getProperty(IImageItemModel.IMAGE_NAME_PROP));
	}

	/**
	 * Test cases for Embedded images.
	 * 
	 * @throws Exception
	 */

	public void testMultiExtendedElements() throws Exception {
		openDesign("LibraryStructureTest_4.xml"); //$NON-NLS-1$

		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("image1"); //$NON-NLS-1$
		assertEquals("Lib1.image1", imageHandle.getImageName()); //$NON-NLS-1$
		assertEquals("Lib1.image1", imageHandle //$NON-NLS-1$
				.getProperty(IImageItemModel.IMAGE_NAME_PROP));

		GridHandle gridHandle = (GridHandle) designHandle.findElement("grid1"); //$NON-NLS-1$
		assertNotNull(gridHandle);
		imageHandle = (ImageHandle) gridHandle.getCell(1, 1).getContent().get(0);

		assertEquals("Lib1.image2", imageHandle //$NON-NLS-1$
				.getProperty(IImageItemModel.IMAGE_NAME_PROP));
		assertEquals("Lib1.image2", imageHandle.getImageName()); //$NON-NLS-1$

		EmbeddedImage emImage = designHandle.findImage("Lib1.image1"); //$NON-NLS-1$
		assertNotNull(emImage);

		emImage = designHandle.findImage("Lib1.image2"); //$NON-NLS-1$
		assertNotNull(emImage);
	}

	/**
	 * Tests the library include library.
	 */

	public void testLibraryIncludeLibrary() {
		try {
			openLibrary("LibraryIncludingTwoLibraries.xml"); //$NON-NLS-1$
		} catch (DesignFileException e) {
			fail();
		}
		assertNotNull(libraryHandle);
	}

	/**
	 * To create an embedded image from an existed embeded image.
	 * 
	 * <ul>
	 * <li>1. create an embedded image. but the target module does not include the
	 * library.
	 * <li>2. create an embedded image. the target module includes the library.
	 * <li>3. if the base embedded image is on the design tree, the create image is
	 * null.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testCreateImageFrom() throws Exception {
		openLibrary("Library_1.xml"); //$NON-NLS-1$

		Iterator iter1 = libraryHandle.imagesIterator();
		EmbeddedImageHandle baseImage = (EmbeddedImageHandle) iter1.next();

		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$

		// design not includes the library.

		try {
			StructureFactory.newEmbeddedImageFrom(baseImage, "image1", //$NON-NLS-1$
					designHandle);
			fail();
		} catch (SemanticException e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND, e.getErrorCode());
		}

		designHandle.includeLibrary("Library_1.xml", "Lib1"); //$NON-NLS-1$ //$NON-NLS-2$
		EmbeddedImage newImage = StructureFactory.newEmbeddedImageFrom(baseImage, "image1", designHandle); //$NON-NLS-1$

		assertEquals("image1", newImage.getName()); //$NON-NLS-1$
		assertNotNull(newImage.getData(design));
		assertNull(newImage.getLocalProperty(design, EmbeddedImage.DATA_MEMBER));
		StructRefValue refValue = (StructRefValue) newImage.getLocalProperty(design,
				EmbeddedImage.LIB_REFERENCE_MEMBER);
		assertNotNull(refValue);
		assertEquals("Lib1.image1", refValue.getQualifiedReference()); //$NON-NLS-1$

		designHandle.addImage(newImage);

		// if the module of the base image is report design, do nothing.

		iter1 = designHandle.imagesIterator();
		baseImage = (EmbeddedImageHandle) iter1.next();

		assertNull(StructureFactory.newEmbeddedImageFrom(baseImage, "image3", //$NON-NLS-1$
				designHandle));

		save();
		assertTrue(compareFile("LibraryStructure_golden.xml")); //$NON-NLS-1$

	}
}
