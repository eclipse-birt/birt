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

package org.eclipse.birt.doc.legacy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.doc.legacy.RomImage.RomException;
import org.eclipse.birt.doc.util.HTMLParser;

public class LegacyLoader {
	RomImage rom;
	ArrayList elements = new ArrayList();
	ArrayList structs = new ArrayList();

	public static void main(String[] argv) {
		LegacyLoader loader = new LegacyLoader();
		try {
			loader.load();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load() throws Exception {
		loadRom();
		loadSpecs();
		dumpSpecs();
		updateRom();
		writeDescripDocs();
		writeRom();
	}

	private void loadSpecs() {
		loadSpecsInDir("elements", SpecElement.ELEMENT);
		loadSpecsInDir("structs", SpecElement.STRUCTURE);
	}

	private void loadSpecsInDir(String dir, int type) {
		File elements = new File("orig/" + dir);
		String files[] = elements.list();
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i];
			if (fileName.endsWith(".html")) {
				System.out.println("Reading " + fileName);
				parseFile("orig/" + dir + "/" + fileName, type);
			}
		}
	}

	private void dumpSpecs() throws Exception {
		SpecDumper dumper = new SpecDumper();
		try {
			dumper.dump(this);
		} catch (IOException e) {
			System.err.println("Failed to dump spec info.");
			throw e;
		}
	}

	private void updateRom() throws Exception {
		RomUpdater updater = new RomUpdater(this);
		try {
			updater.update();
		} catch (Exception e) {
			System.out.println("Failed to load rom.def");
			throw e;
		}
	}

	private void writeDescripDocs() {
		try {
			DocWriter writer = new DocWriter();
			writer.startIndex();

			// Write Element descriptions

			writer.startElementIndex();
			Iterator iter = elements.iterator();
			while (iter.hasNext()) {
				SpecElement element = (SpecElement) iter.next();
				writer.write(element);
			}
			writer.startStructIndex();
			iter = structs.iterator();
			while (iter.hasNext()) {
				SpecElement element = (SpecElement) iter.next();
				writer.write(element);
			}

			writer.endIndex();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Failed to write element descriptions.");
		}
	}

	private void writeRom() {

		// Write ROM

		try {
			rom.write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Failed to write rom.def");
		}
	}

	public void parseFile(String fileName, int type) {
		try {
			LegacySpecParser parser = new LegacySpecParser();
			parser.parse(fileName, type);
			SpecElement element = parser.getElement();
			if (type == SpecElement.ELEMENT) {
				elements.add(element);
			} else {
				structs.add(element);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Could not load file " + fileName);
		}
	}

	static String elementTypes[] = { "Start", "End", "Single" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	};

	void loadRom() throws RomException {
		rom = new RomImage();
		try {
			rom.open();
		} catch (RomException e) {
			// TODO Auto-generated catch block
			System.err.println("ROM load failed");
			throw e;
		}
	}

	static void test(String fileName) throws FileNotFoundException {
		HTMLParser parser = new HTMLParser();
		parser.open(fileName);
		for (;;) {
			int token = parser.getToken();
			if (token == HTMLParser.EOF) {
				break;
			}
			switch (token) {
			case HTMLParser.SPECIAL_ELEMENT:
				System.out.print("Special Element: "); //$NON-NLS-1$
				System.out.println(parser.getTokenText());
				break;
			case HTMLParser.ELEMENT:
				System.out.print(elementTypes[parser.getElementType()]);
				System.out.print("Element: "); //$NON-NLS-1$
				System.out.println(parser.getFullElement());
				break;
			case HTMLParser.TEXT:
				System.out.print("Text: "); //$NON-NLS-1$
				System.out.println(parser.getTokenText());
				break;
			}
		}
	}

	public RomImage getRom() {
		return rom;
	}

	public AbstractList getElements() {
		return elements;
	}

	public AbstractList getStructures() {
		return structs;
	}

}
