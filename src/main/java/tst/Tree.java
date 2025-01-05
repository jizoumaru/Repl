package tst;

import java.util.Iterator;

public final class Tree<T> implements Iterable<KeyValue<String, T>> {
	public static final Tree<?> EMPTY = new Tree<Object>();

	@SuppressWarnings("unchecked")
	public static <T> Tree<T> empty() {
		return (Tree<T>) EMPTY;
	}

	public final Node<T> node;

	public Tree() {
		this(null);
	}

	public Tree(Node<T> node) {
		this.node = node;
	}

	public Tree<T> add(String key) {
		return new Tree<T>(Node.put(node, key, null));
	}

	public Tree<T> put(String key, T value) {
		return new Tree<T>(Node.put(node, key, value));
	}

	public KeyValue<String, T> longestCommonPrefix(String key) {
		return Node.longestCommonPrefix(node, key.toCharArray(), 0);
	}

	@Override
	public Iterator<KeyValue<String, T>> iterator() {
		return Node.iterator(node);
	}

	@Override
	public String toString() {
		return Node.toString(node);
	}

	public void dump() {
		Node.dump(node);
	}
}