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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

public class SpecDumper {
	PrintStream out;

	public void dump(LegacyLoader loader) throws IOException {
		FileOutputStream stream = new FileOutputStream("log/dump.txt");
		out = new PrintStream(stream);
		Iterator iter = loader.getElements().iterator();
		while (iter.hasNext()) {
			SpecElement element = (SpecElement) iter.next();
			dump(element);
		}
		stream.close();
	}

	private void println(String s) {
		out.println(s);
	}

	private void print(String s) {
		out.print(s);
	}

	void dump(SpecElement element) {
		println("\n=========================================================\n");
		println("Element:"); //$NON-NLS-1$
		print("  Name: "); //$NON-NLS-1$
		println(element.name);
		print("  Display Name: "); //$NON-NLS-1$
		println(element.displayName);
		print("  Summary: "); //$NON-NLS-1$
		println(element.summary);
		print("  Design Object: "); //$NON-NLS-1$
		println(element.designObjName);
		print("  Runtime Object: "); //$NON-NLS-1$
		println(element.stateObjName);
		print("  Style Names: "); //$NON-NLS-1$
		println(element.styleNames);
		print("  Since: "); //$NON-NLS-1$
		println(element.since);
		print("  XML Element: "); //$NON-NLS-1$
		println(element.xmlElement);
		print("  XML Summary: "); //$NON-NLS-1$
		println(element.xmlSummary);
		print("  Description: "); //$NON-NLS-1$
		println(element.description);
		print("    See Also: "); //$NON-NLS-1$
		println(element.seeAlso);
		for (int i = 0; i < element.inheritedProperties.size(); i++) {
			dumpInheritedProperty((SpecInheritedProperty) element.inheritedProperties.get(i));
		}
		for (int i = 0; i < element.properties.size(); i++) {
			dumpProperty((SpecProperty) element.properties.get(i));
		}
		for (int i = 0; i < element.methods.size(); i++) {
			dumpMethod((SpecMethod) element.methods.get(i));
		}
		for (int i = 0; i < element.slots.size(); i++) {
			dumpSlot((SpecSlot) element.slots.get(i));
		}
	}

	private void dumpInheritedProperty(SpecInheritedProperty prop) {
		println("\n  Property:"); //$NON-NLS-1$
		print("    Name: "); //$NON-NLS-1$
		println(prop.name);
		print("    Description: "); //$NON-NLS-1$
		println(prop.description);
	}

	private void dumpTristate(String label, int value) {
		if (value == SpecObject.TRI_UNKNOWN) {
			return;
		}
		print(label);
		println(value == SpecObject.TRI_TRUE ? "True" : "False");
	}

	private void dumpProperty(SpecProperty prop) {
		println("\n  Property:"); //$NON-NLS-1$
		print("    Name: "); //$NON-NLS-1$
		println(prop.name);
		print("    Display Name: "); //$NON-NLS-1$
		println(prop.displayName);
		print("    Summary: "); //$NON-NLS-1$
		println(prop.summary);
		print("    Short Descrip: "); //$NON-NLS-1$
		println(prop.shortDescrip);
		print("    Since: "); //$NON-NLS-1$
		println(prop.since);
		print("    JS Type: "); //$NON-NLS-1$
		println(prop.jsType);
		print("    ROM Type: "); //$NON-NLS-1$
		println(prop.romType);
		print("    Expression Type: "); //$NON-NLS-1$
		println(prop.exprType);
		print("    Default Value: "); //$NON-NLS-1$
		println(prop.defaultValue);
		print("    Expression Context: "); //$NON-NLS-1$
		println(prop.exprContext);
		dumpTristate("    Inherited: ", prop.inherited);
		dumpTristate("    Array: ", prop.isArray);
		dumpTristate("    Hidden: ", prop.hidden);
		dumpTristate("    Runtime Settable: ", prop.runtimeSettable);
		dumpTristate("    Required: ", prop.required);
		print("  Description: "); //$NON-NLS-1$
		println(prop.description);
		print("    See Also: "); //$NON-NLS-1$
		println(prop.seeAlso);

		dumpChoices(prop);
	}

	private void dumpChoices(SpecProperty prop) {
		if (prop.choices.isEmpty()) {
			return;
		}
		println("    Choices:");
		Iterator iter = prop.choices.iterator();
		while (iter.hasNext()) {
			SpecChoice choice = (SpecChoice) iter.next();
			println("      Choice:");
			print("        Name:");
			println(choice.name);
			print("        Display Name:");
			println(choice.displayName);
			print("        Description:");
			println(choice.description);
		}
	}

	private void dumpMethod(SpecMethod method) {
		println("\n  Method:"); //$NON-NLS-1$
		print("    Name: "); //$NON-NLS-1$
		println(method.name);
		print("    Display Name: "); //$NON-NLS-1$
		println(method.displayName);
		print("    Summary: "); //$NON-NLS-1$
		println(method.summary);
		print("    Short Descrip: "); //$NON-NLS-1$
		println(method.shortDescrip);
		print("    Since: "); //$NON-NLS-1$
		println(method.since);
		print("    Returns: "); //$NON-NLS-1$
		println(method.returns);
		print("    Context: "); //$NON-NLS-1$
		println(method.context);
		print("    Description: "); //$NON-NLS-1$
		println(method.description);
		print("    See Also: "); //$NON-NLS-1$
		println(method.seeAlso);
	}

	private void dumpSlot(SpecSlot slot) {
		println("\n  Slot:"); //$NON-NLS-1$
		print("    Name: "); //$NON-NLS-1$
		println(slot.name);
		print("    Display Name: "); //$NON-NLS-1$
		println(slot.displayName);
		print("    Summary: "); //$NON-NLS-1$
		println(slot.summary);
		print("    Short Descrip: "); //$NON-NLS-1$
		println(slot.shortDescrip);
		print("    Style: "); //$NON-NLS-1$
		println(slot.styleNames);
		print("    Since: "); //$NON-NLS-1$
		println(slot.since);
		print("    Contents: "); //$NON-NLS-1$
		println(slot.contents);
		if (slot.cardinality == SpecSlot.SINGLE) {
			println("    Cardinality: Single");
		} else if (slot.cardinality == SpecSlot.MULTIPLE) {
			println("    Cardinality: Multiple");
		}
		print("    XML Element: "); //$NON-NLS-1$
		println(slot.xmlElement);
		print("    Description: "); //$NON-NLS-1$
		println(slot.description);
		print("    See Also: "); //$NON-NLS-1$
		println(slot.seeAlso);
	}

}
