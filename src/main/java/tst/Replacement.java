package tst;

import java.util.Iterator;

public final class Replacement implements Iterable<KeyValue<String, String>> {
	public static final Replacement EMPTY = new Replacement(null);

	public static Replacement empty() {
		return EMPTY;
	}

	public final Node<String> node;

	public Replacement(Node<String> node) {
		this.node = node;
	}

	public Replacement put(String key, String value) {
		return new Replacement(Node.put(node, key, value));
	}
	
	@Override
	public Iterator<KeyValue<String, String>> iterator() {
		return Node.iterator(node);
	}

	public String replace(String text) {
		var b = (StringBuilder) null;
		var a = text.toCharArray();
		var f = 0;
		var o = 0;

		while (o < a.length) {
			var n = node;
			var l = 0;
			var v = (String) null;
			var i = o;

			while (i < a.length && n != null) {
				if (a[i] < n.k[0]) {
					n = n.l;
					continue;
				}

				if (n.k[0] < a[i]) {
					n = n.r;
					continue;
				}

				var j = 1;

				while (i + j < a.length && j < n.k.length && a[i + j] == n.k[j]) {
					j++;
				}

				if (j < n.k.length) {
					break;
				}

				if (n.e) {
					l = i + j;
					v = n.v;
				}

				i += j;
				n = n.m;
			}

			if (l == 0) {
				o++;
			} else {
				if (b == null) {
					b = new StringBuilder();
				}
				b.append(a, f, o - f).append(v);
				f = l;
				o = l;
			}
		}

		if (b == null) {
			return text;
		} else {
			return b.append(a, f, o - f).toString();
		}
	}

	public void dump() {
		Node.dump(node);
	}
}
