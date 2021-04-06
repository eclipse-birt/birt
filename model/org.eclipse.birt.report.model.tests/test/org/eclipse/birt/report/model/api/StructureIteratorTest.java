package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.IncludedLibraryHandle;
import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.api.PropertyHandleImpl;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;
import com.ibm.icu.util.ULocale;

public class StructureIteratorTest extends BaseTestCase {

	private SimpleValueHandle createSimpleValueHandle(int numOfItem) throws ModelException {
		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		ReportDesignHandle rdh = sessionHandle.createDesign();
		SimpleValueHandle svh = new PropertyHandleImpl(rdh, ReportDesign.LIBRARIES_PROP);

		for (int i = 0; i < numOfItem; i++) {
			IncludedLibrary inc = new IncludedLibrary();
			inc.setFileName("filename" + i);
			inc.setNamespace("lib" + i);
			svh.addItem(inc);
		}

		return svh;
	}

	public void testZeroItem() throws ModelException {
		SimpleValueHandle svh = createSimpleValueHandle(0);
		Iterator si = svh.iterator();

		assertFalse(si.hasNext());
	}

	public void testOneItem() throws ModelException {
		SimpleValueHandle svh = createSimpleValueHandle(1);
		Iterator si = svh.iterator();

		assertTrue(si.hasNext());
		si.next();
		si.remove();

		si = svh.iterator();
		assertFalse(si.hasNext());
	}

	public void testRemoveFirstFromMultipleItem() throws ModelException {
		int itemNum = 5;
		SimpleValueHandle svh = createSimpleValueHandle(itemNum);
		Iterator si = svh.iterator();

		// remove 1st item
		si.next();
		si.remove();
		itemNum--;

		IncludedLibrary inc = (IncludedLibrary) ((IncludedLibraryHandle) si.next()).getStructure();
		assertTrue(inc.getFileName().equals("filename1"));

		int count = 0;
		si = svh.iterator();
		while (si.hasNext()) {
			count++;
			inc = (IncludedLibrary) ((IncludedLibraryHandle) si.next()).getStructure();
			assertFalse(inc.getFileName().equals("filename0"));
		}
		assertEquals(count, itemNum);
	}

	public void testRemoveLastFromMultipleItem() throws ModelException {
		int itemNum = 5;
		SimpleValueHandle svh = createSimpleValueHandle(itemNum);
		Iterator si = svh.iterator();

		// remove last item
		while (si.hasNext())
			si.next();
		si.remove();
		itemNum--;

		int count = 0;
		si = svh.iterator();
		while (si.hasNext()) {
			count++;
			IncludedLibrary inc = (IncludedLibrary) ((IncludedLibraryHandle) si.next()).getStructure();
			assertFalse(inc.getFileName().equals("filename4"));
		}
		assertEquals(count, itemNum);
	}

	public void testRemoveMidFromMultipleItem() throws ModelException {
		int itemNum = 5;
		SimpleValueHandle svh = createSimpleValueHandle(itemNum);
		Iterator si = svh.iterator();
		int count;

		// remove the 3rd item
		si.next();
		si.next();
		si.next();
		si.remove();
		itemNum--;
		IncludedLibrary inc = (IncludedLibrary) ((IncludedLibraryHandle) si.next()).getStructure();
		assertTrue(inc.getFileName().equals("filename3"));

		count = 0;
		si = svh.iterator();
		while (si.hasNext()) {
			count++;
			inc = (IncludedLibrary) ((IncludedLibraryHandle) si.next()).getStructure();
			assertFalse(inc.getFileName().equals("filename2"));
		}
		assertEquals(count, itemNum);
	}
}
