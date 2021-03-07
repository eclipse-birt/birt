
package org.eclipse.birt.core.script;

import java.util.ArrayList;
import java.util.HashMap;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class NativeNamedList implements Scriptable {

	Scriptable prototype;
	Scriptable parent;
	ArrayList names = new ArrayList();
	HashMap values = new HashMap();

	static final String JS_CLASS_NAME = "NamedList";

	@Override
	public String getClassName() {
		return JS_CLASS_NAME;
	}

	public NativeNamedList() {
	}

	public NativeNamedList(Scriptable parent, String[] names, HashMap values) {
		setParentScope(parent);
		for (String name : names) {
			this.names.add(name);
			Object value = Context.javaToJS(values.get(name), parent);
			NativeEntry entry = new NativeEntry(name, value);
			this.values.put(name, entry);
		}
	}

	@Override
	public Object get(String name, Scriptable start) {
		if ("length".equals(name)) {
			return Integer.valueOf(names.size());
		}
		Object value = values.get(name);
		if (value != null) {
			return value;
		}
		return NOT_FOUND;
	}

	@Override
	public Object get(int index, Scriptable start) {
		String name = (String) names.get(index);
		Object value = values.get(name);
		if (value != null) {
			return value;
		}
		return NOT_FOUND;
	}

	@Override
	public boolean has(String name, Scriptable start) {
		if ("length".equals(name)) {
			return true;
		}
		if (values.containsKey(name)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean has(int index, Scriptable start) {
		if (index >= 0 && index <= names.size()) {
			return true;
		}
		return false;
	}

	@Override
	public void put(String name, Scriptable start, Object value) {
		NativeEntry entry = (NativeEntry) values.get(name);
		if (entry != null) {
			entry.value = value;
		}
	}

	@Override
	public void put(int index, Scriptable start, Object value) {
		String name = (String) names.get(index);
		put(name, start, value);
	}

	@Override
	public void delete(String name) {
	}

	@Override
	public void delete(int index) {
	}

	@Override
	public Scriptable getPrototype() {
		return prototype;
	}

	@Override
	public void setPrototype(Scriptable prototype) {
		this.prototype = prototype;
	}

	@Override
	public Scriptable getParentScope() {
		return parent;
	}

	@Override
	public void setParentScope(Scriptable parent) {
		this.parent = parent;
	}

	@Override
	public Object[] getIds() {
		return names.toArray();
	}

	@Override
	public Object getDefaultValue(Class hint) {
		return null;
	}

	@Override
	public boolean hasInstance(Scriptable instance) {
		return false;
	}

	static class NativeEntry implements Scriptable {

		Scriptable prototype;
		Scriptable parent;
		String name;
		Object value;
		static final String JS_CLASS_NAME = "Entry";

		@Override
		public String getClassName() {
			return JS_CLASS_NAME;
		}

		public NativeEntry(String name, Object value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public Object get(String name, Scriptable start) {
			if ("name".equals(name)) {
				return this.name;
			}
			if ("value".equals(name)) {
				return this.value;
			}
			return NOT_FOUND;
		}

		@Override
		public Object get(int index, Scriptable start) {
			return NOT_FOUND;
		}

		@Override
		public boolean has(String name, Scriptable start) {
			if ("name".equals(name) || "value".equals(name)) {
				return true;
			}
			return false;
		}

		@Override
		public boolean has(int index, Scriptable start) {
			return false;
		}

		@Override
		public void put(String name, Scriptable start, Object value) {
			if ("value".equals(name)) {
				this.value = value;
			}
		}

		@Override
		public void put(int index, Scriptable start, Object value) {
		}

		@Override
		public void delete(String name) {
		}

		@Override
		public void delete(int index) {
		}

		@Override
		public Scriptable getPrototype() {
			return prototype;
		}

		@Override
		public void setPrototype(Scriptable prototype) {
			this.prototype = prototype;
		}

		@Override
		public Scriptable getParentScope() {
			return parent;
		}

		@Override
		public void setParentScope(Scriptable parent) {
			this.parent = parent;
		}

		@Override
		public Object[] getIds() {
			return null;
		}

		@Override
		public Object getDefaultValue(Class hint) {
			return value;
		}

		@Override
		public boolean hasInstance(Scriptable instance) {
			return false;
		}
	}

}
