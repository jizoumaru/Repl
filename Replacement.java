package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Main {

	public static void main(String[] args) {
		var tr = Replacement.builder()
				.add("foo", "ふー")
				.add("foobar", "ふうばあ")
				.add("fool", "フール")
				.add("foolish", "フーリッシュ")
				.build();

		System.out.println(tr.replace("yafoo"));
		System.out.println(tr.replace("abcfoobarxyz"));
		System.out.println(tr.replace("fooligan"));
		System.out.println(tr.replace("foolish"));
		System.out.println(tr.replace("ochiai"));
		System.out.println(tr.replace("tategami"));
		System.out.println(tr.replace("tatemono"));
		System.out.println(tr.replace("tombo"));

		tr.root.dump();
	}

	static class Replacement {
		static Builder builder() {
			return new Builder();
		}

		static class Builder {
			ArrayList<String[]> list = new ArrayList<String[]>();

			Builder add(String key, String value) {
				list.add(new String[] { key, value });
				return this;
			}

			Replacement build() {
				var a = unique(list.stream()
						.sorted(Comparator.comparing($_ -> $_[0]))
						.toArray(String[][]::new));

				var keys = new String[a.length];
				var values = new String[a.length];

				for (var i = 0; i < a.length; i++) {
					keys[i] = a[i][0];
					values[i] = a[i][1];
				}

				return new Replacement(create(keys, values));
			}

			static String[][] unique(String[][] a) {
				if (a.length == 0) {
					return a;
				}
				
				var i = 1;

				for (var j = 1; j < a.length; j++) {
					if (!a[i - 1][0].equals(a[j][0])) {
						a[i] = a[j];
						i++;
					}
				}
				
				return Arrays.copyOf(a, i);
			}

			static Node create(String[] keys, String[] values) {
				var a = new char[keys.length][];

				for (var i = 0; i < a.length; i++) {
					a[i] = keys[i].toCharArray();
				}

				return create(a, values, 0, a.length, 0);
			}

			static Node create(char[][] keys, String[] values, int from, int to, int x) {
				if (to - from < 1) {
					return Node.empty;
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

		final Node root;

		Replacement(Node root) {
			this.root = root;
		}

		public String replace(String key) {
			var sb = (StringBuilder) null;
			var cs = key.toCharArray();
			var from = 0;
			var f = 0;

			while (f < cs.length) {
				var to = 0;
				var value = (String) null;

				var i = f;
				var n = root;

				while (i < cs.length && n != Node.empty) {
					if (cs[i] < n.k) {
						n = n.l;
					} else if (n.k < cs[i]) {
						n = n.r;
					} else {
						if (n.v != null) {
							to = i + 1;
							value = n.v;
						}
						i++;
						n = n.b;
					}
				}

				if (to == 0) {
					f += 1;
				} else {
					if (sb == null) {
						sb = new StringBuilder();
					}
					sb.append(cs, from, f - from).append(value);
					f = to;
					from = to;
				}
			}

			if (from == 0) {
				return key;
			} else {
				return sb.append(cs, from, cs.length - from).toString();
			}
		}

	}
}

class Node {
	public static final Node empty = new Node('$', null, null, null, null) {
		@Override
		void dump() {
		}

		@Override
		void dumpR(String p) {
			System.out.println(p + "　┌" + k);
		}

		@Override
		void dumpB(String s, String lp, String rp) {
			System.out.println(s + "┼" + k);
		}

		@Override
		void dumpL(String p) {
			System.out.println(p + "　└" + k);
		}
	};

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

	void dump() {
		r.dumpR("");
		b.dumpB("" + k, "", "");
		l.dumpL("");
	}

	void dumpR(String p) {
		r.dumpR(p + "　　");
		b.dumpB(p + "　┌" + k, p + "　│", p + "　　");
		l.dumpL(p + "　│");
	}

	void dumpL(String p) {
		r.dumpR(p + "　│");
		b.dumpB(p + "　└" + k, p + "　　", p + "　│");
		l.dumpL(p + "　　");
	}

	void dumpB(String s, String lp, String rp) {
		r.dumpR(rp + "　│");
		b.dumpB(s + "┼" + k, lp + "　│", rp + "　│");
		l.dumpL(lp + "　│");
	}
}
