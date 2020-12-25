import java.util.ArrayList;
import java.util.Comparator;

public final class Replace {
	public static void main(String[] args) {
		var r = Replace.builder()
				.add("\\", "\\\\")
				.add("\t", "\\t")
				.add("\r", "\\r")
				.add("\n", "\\n")
				.build();
		
		System.out.println(r.replace("a\tb\t\r\n"));
	}
	
	private static final class Node {
		final char k;
		final String v;
		final Node l;
		final Node b;
		final Node r;

		Node(char k, String v, Node left, Node bottom, Node right) {
			this.k = k;
			this.v = v;
			this.l = left;
			this.b = bottom;
			this.r = right;
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private final ArrayList<String[]> list = new ArrayList<String[]>();

		public Builder add(String key, String value) {
			list.add(new String[] { key, value });
			return this;
		}

		public Replace build() {
			var a = list.stream()
					.filter($_ -> !$_[0].equals($_[1]))
					.sorted(Comparator.comparing($_ -> $_[0]))
					.toArray(String[][]::new);

			var k = new char[a.length][];
			var v = new String[a.length];

			for (var i = 0; i < a.length; i++) {
				k[i] = a[i][0].toCharArray();
				v[i] = a[i][1];
			}

			return new Replace(create(k, v, 0, k.length, 0));
		}

		private static Node create(char[][] keys, String[] values, int from, int to, int x) {
			if (to - from < 1) {
				return null;
			}

			var y = from + (to - from) / 2;

			var f = y;

			while (f - 1 >= from && keys[f - 1][x] == keys[y][x]) {
				f--;
			}

			var t = y + 1;

			while (t < to && keys[t][x] == keys[y][x]) {
				t++;
			}

			if (x < keys[f].length - 1) {
				return new Node(keys[y][x], null,
						create(keys, values, from, f, x),
						create(keys, values, f, t, x + 1),
						create(keys, values, t, to, x));
			} else {
				return new Node(keys[y][x], values[f],
						create(keys, values, from, f, x),
						create(keys, values, f + 1, t, x + 1),
						create(keys, values, t, to, x));
			}
		}
	}

	private final Node root;

	private Replace(Node root) {
		this.root = root;
	}

	public String replace(String key) {
		var sb = (StringBuilder) null;
		var cs = key.toCharArray();
		var from = 0;
		var f = 0;

		while (f < cs.length) {
			var t = 0;
			var v = (String) null;

			var i = f;
			var n = root;

			while (i < cs.length && n != null) {
				if (cs[i] < n.k) {
					n = n.l;
				} else if (n.k < cs[i]) {
					n = n.r;
				} else {
					if (n.v != null) {
						t = i + 1;
						v = n.v;
					}
					i++;
					n = n.b;
				}
			}

			if (t == 0) {
				f += 1;
			} else {
				if (sb == null) {
					sb = new StringBuilder();
				}
				sb.append(cs, from, f - from).append(v);
				f = t;
				from = t;
			}
		}

		if (from == 0) {
			return key;
		} else {
			return sb.append(cs, from, cs.length - from).toString();
		}
	}

}
