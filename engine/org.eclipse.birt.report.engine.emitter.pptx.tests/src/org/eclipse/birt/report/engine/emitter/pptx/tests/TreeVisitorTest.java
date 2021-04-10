
package org.eclipse.birt.report.engine.emitter.pptx.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.birt.report.engine.emitter.pptx.TreeVisitor;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TreeVisitorTest extends TreeVisitor<String> {

	public TreeNode initTree(String xmldoc) throws ParserConfigurationException, SAXException, IOException {
		File tree = new File(xmldoc);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(tree);
		doc.getDocumentElement().normalize();
		TreeNode parent = new TreeNode(null, 0, doc.getDocumentElement().getNodeName());
		NodeList nodes = doc.getElementsByTagName("node");

		constructNodes(nodes, parent);
		return parent;

	}

	private void constructNodes(NodeList nodes, TreeNode parent) {
		int numberOfNodes = nodes.getLength();

		if (numberOfNodes == 0) {
			return;
		}
		ArrayList<TreeNode> childrenlist = new ArrayList<TreeNode>();
		int idx = 0;
		for (int i = 0; i < numberOfNodes; i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				TreeNode tnode = new TreeNode(parent, idx, node.getNodeName());
				childrenlist.add(tnode);
				NodeList nodelist = node.getChildNodes();
				constructNodes(nodelist, tnode);
				idx++;
			}
		}
		parent.addChildren(childrenlist);
	}

	class TreeNode implements TreeVisitor.ITreeNode<String> {

		TreeNode parent;
		int index;
		ArrayList<TreeNode> children;
		String value;

		public TreeNode(TreeNode parent, int index, String value) {
			this.parent = parent;
			this.index = index;
			this.value = value;
		}

		public TreeNode skipRow(TreeNode node, final int count) {
			IFilter<String> ifilter = new IFilter<String>() {

				int rowCount = 0;

				public int getRowCount() {
					return rowCount;
				}

				@Override
				public boolean matches(String value) {
					if (value != null && value.contains("RowArea")) {
						rowCount++;
						if (rowCount > count) {
							return true;
						}
					}
					return false;
				}
			};
			return (TreeNode) new TreeVisitor<String>().forEach(node, ifilter);
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public TreeNode getParent() {
			return parent;
		}

		@Override
		public TreeNode getChild() {
			return getChild(0);
		}

		public TreeNode getChild(int index) {
			if (children == null || children.isEmpty() || index >= children.size()) {
				return null;
			}
			return children.get(index);
		}

		@Override
		public TreeNode getNext() {
			int numOfSibling = 0;
			if (parent == null) {
				return null;
			}
			if (parent.children == null) {
				numOfSibling = 0;
			} else {
				numOfSibling = parent.children.size() - 1;
			}
			if (index >= numOfSibling) {
				return null;
			}
			return parent.getChild(index + 1);
		}

		public void addChildren(ArrayList<TreeNode> children) {
			this.children = children;
		}
	}

	@Test
	public void test() throws ParserConfigurationException, SAXException, IOException {
		TreeVisitorTest tvt = new TreeVisitorTest();

		TreeNode root = tvt.initTree("src/org/eclipse/birt/report/engine/emitter/pptx/tests/treevisitortest.xml");
		TreeNode startNode = root.getChild().getChild();
		String third = root.skipRow(startNode, 2).getValue();
		assertEquals("RowArea3", third);
		String fourth = root.skipRow(startNode, 4).getValue();
		assertEquals("RowArea5", fourth);
		ITreeNode<String> nullstring = root.skipRow(startNode, 8);
		assertNull(nullstring);
		String second = root.skipRow(startNode, 1).getValue();
		assertEquals("RowArea2", second);

		assertEquals("RowArea6", root.skipRow(getTreeNode(root, "RowArea5"), 1).getValue());
		assertEquals("RowArea7", root.skipRow(getTreeNode(root, "RowArea5"), 2).getValue());
		assertNull(root.skipRow(getTreeNode(root, "RowArea5"), 3));
	}

	private TreeNode getTreeNode(TreeNode root, String value) {
		if (value.equals(root.getValue())) {
			return root;
		}
		TreeNode child = root.getChild();
		while (child != null) {
			TreeNode ret = getTreeNode(child, value);
			if (ret != null) {
				return ret;
			}
			child = child.getNext();
		}
		return null;
	}

}
