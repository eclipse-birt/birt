
package org.eclipse.birt.report.engine.emitter.pptx;

/**
 * utility used to find row in a table
 */
public class TreeVisitor<T> {

	public interface ITreeNode<T> {

		T getValue();

		ITreeNode<T> getParent();

		ITreeNode<T> getChild();

		ITreeNode<T> getNext();
	}

	//
	public interface IFilter<T> {

		boolean matches(T value);

		public int getRowCount();
	}

	public TreeVisitor() {
	}

	/**
	 * iterator down a tree and return the matched according to filter.
	 * 
	 * @param node
	 * @param filter
	 * @return
	 */
	public ITreeNode<T> forEach(ITreeNode<T> node, IFilter<T> filter) {
		ITreeNode<T> ret = visitChild(node, filter);
		if (ret != null) {
			return ret;
		}
		do {
			ITreeNode<T> next = node.getNext();
			while (next != null) {
				ret = visitChild(next, filter);
				if (ret != null) {
					return ret;
				}
				next = next.getNext();
			}
			node = node.getParent();
		} while (node != null);
		return null;
	}

	private ITreeNode<T> visitChild(ITreeNode<T> node, IFilter<T> filter) {
		if (node == null) {
			return null;
		}
		if (filter.matches(node.getValue())) {
			return node;
		}

		ITreeNode<T> child = node.getChild();
		while (child != null) {
			ITreeNode<T> ret = visitChild(child, filter);
			if (ret != null) {
				return ret;
			}
			child = child.getNext();
		}
		return null;
	}
}
