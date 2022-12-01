import java.util.Arrays;
import java.util.function.BiConsumer;

public class Main {
	public static void main(String[] args) {
		var srcs = Arrays.asList(
				"",
				"a",
				"ab",
				"abc",
				"\n",
				"\n\n",
				"\n\n\n",
				"a\n",
				"\na",
				"a\na",
				"\na\n");

		for (var src : srcs) {
			var dst = JsonEscapement.escape(src);
			System.out.println(dst);
		}
	}

	static class JsonEscapement {
		public static final Replacement esc = new Replacement()
				.add("\u0000", "\\u0000")
				.add("\u0001", "\\u0001")
				.add("\u0002", "\\u0002")
				.add("\u0003", "\\u0003")
				.add("\u0004", "\\u0004")
				.add("\u0005", "\\u0005")
				.add("\u0006", "\\u0006")
				.add("\u0007", "\\u0007")
				.add("\b", "\\b")
				.add("\t", "\\t")
				.add("\n", "\\n")
				.add("\u000b", "\\u000b")
				.add("\f", "\\f")
				.add("\r", "\\r")
				.add("\u000e", "\\u000e")
				.add("\u000f", "\\u000f")
				.add("\u0010", "\\u0010")
				.add("\u0011", "\\u0011")
				.add("\u0012", "\\u0012")
				.add("\u0013", "\\u0013")
				.add("\u0014", "\\u0014")
				.add("\u0015", "\\u0015")
				.add("\u0016", "\\u0016")
				.add("\u0017", "\\u0017")
				.add("\u0018", "\\u0018")
				.add("\u0019", "\\u0019")
				.add("\u001a", "\\u001a")
				.add("\u001b", "\\u001b")
				.add("\u001c", "\\u001c")
				.add("\u001d", "\\u001d")
				.add("\u001e", "\\u001e")
				.add("\u001f", "\\u001f")
				.add("\\", "\\\\")
				.add("\"", "\\\"");

		public static final Replacement unesc = esc.reverse();

		public static String escape(String value) {
			return esc.replace(value);
		}

		public static String unescape(String value) {
			return unesc.replace(value);
		}
	}

	static class Replacement {
		public final Tree<String> tree;

		public Replacement() {
			this(new Tree<String>());
		}

		public Replacement(Main.Tree<String> tree) {
			this.tree = tree;
		}

		public Replacement add(String key, String value) {
			return new Replacement(tree.add(key, value));
		}

		public Replacement reverse() {
			var r = new Replacement[] { new Replacement() };
			tree.each((k, v) -> r[0] = r[0].add(v, k));
			return r[0];
		}

		public String replace(String text) {
			var sb = (StringBuilder) null;
			var cs = text.toCharArray();
			var from = 0;
			var f = 0;

			while (f < cs.length) {
				var t = 0;
				var v = (String) null;

				var i = f;
				var n = tree.r;

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
						n = n.c;
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
				return text;
			} else {
				return sb.append(cs, from, cs.length - from).toString();
			}
		}

	}

	static class Tree<T> {
		public final Node<T> r;

		public Tree() {
			this(null);
		}

		private Tree(Node<T> root) {
			this.r = root;
		}

		public Tree<T> add(String key, T value) {
			return new Tree<T>(add(r, key.toCharArray(), 0, value).n);
		}

		private static <T> Node<T> L(Node<T> n, Node<T> l, Node<T> r) {
			return new Node<T>(Tilt.L, n.k, n.v, n.c, l, r);
		}

		private static <T> Node<T> E(Node<T> n, Node<T> l, Node<T> r) {
			return new Node<T>(Tilt.E, n.k, n.v, n.c, l, r);
		}

		private static <T> Node<T> R(Node<T> n, Node<T> l, Node<T> r) {
			return new Node<T>(Tilt.R, n.k, n.v, n.c, l, r);
		}

		private static <T> Ret<T> add(Node<T> n, char[] k, int i, T v) {
			if (n == null) {
				var m = (Node<T>) null;
				for (var j = k.length - 1; j >= i; j--) {
					m = new Node<T>(Tilt.E, k[j], j == k.length - 1 ? v : null, m, null, null);
				}
				return Ret.notAdjusted(m);
			}

			if (k[i] == n.k) {
				if (i < k.length - 1) {
					return Ret.adjusted(new Node<T>(n.t, n.k, n.v, add(n.c, k, i + 1, v).n, n.l, n.r));
				} else {
					return Ret.adjusted(new Node<T>(n.t, n.k, v, n.c, n.l, n.r));
				}
			}

			if (k[i] < n.k) {
				var ret = add(n.l, k, i, v);
				var l = ret.n;

				if (ret.adjusted()) {
					return Ret.adjusted(new Node<T>(n.t, n.k, n.v, n.c, l, n.r));
				}

				return switch (n.t) {
					case L -> switch (l.t) {
						case L -> Ret.adjusted(E(l, l.l, E(n, l.r, n.r)));
						case E -> throw new RuntimeException();
						case R -> switch (l.r.t) {
							case L -> Ret.adjusted(E(l.r, E(l, l.l, l.r.l), R(n, l.r.r, n.r)));
							case E -> Ret.adjusted(E(l.r, E(l, l.l, l.r.l), E(n, l.r.r, n.r)));
							case R -> Ret.adjusted(E(l.r, L(l, l.l, l.r.l), E(n, l.r.r, n.r)));
						};
					};
					case E -> Ret.notAdjusted(L(n, ret.n, n.r));
					case R -> Ret.adjusted(E(n, ret.n, n.r));
				};
			} else {
				var ret = add(n.r, k, i, v);
				var r = ret.n;

				if (ret.adjusted()) {
					return Ret.adjusted(new Node<T>(n.t, n.k, n.v, n.c, n.l, r));
				}

				return switch (n.t) {
					case R -> switch (r.t) {
						case R -> Ret.adjusted(E(r, E(n, n.l, r.l), r.r));
						case E -> throw new RuntimeException();
						case L -> switch (r.l.t) {
							case R -> Ret.adjusted(E(r.l, L(n, n.l, r.l.l), E(r, r.l.r, r.r)));
							case E -> Ret.adjusted(E(r.l, E(n, n.l, r.l.l), E(r, r.l.r, r.r)));
							case L -> Ret.adjusted(E(r.l, E(n, n.l, r.l.l), R(r, r.l.r, r.r)));
						};
					};
					case E -> Ret.notAdjusted(R(n, n.l, r));
					case L -> Ret.adjusted(E(n, n.l, r));
				};
			}
		}

		public void each(BiConsumer<String, T> consumer) {
			each(consumer, r, "");
		}

		public void each(BiConsumer<String, T> consumer, Node<T> node, String prefix) {
			if (node == null) {
				return;
			}
			each(consumer, node.l, prefix);
			if (node.v != null) {
				consumer.accept(prefix + node.k, node.v);
			}
			each(consumer, node.c, prefix + node.k);
			each(consumer, node.r, prefix);
		}

		@Override
		public String toString() {
			return String.valueOf(r);
		}

		private static class Ret<T> {
			public enum Adjustment {
				NotAdjusted, Adjusted
			}

			public static <T> Ret<T> notAdjusted(Node<T> node) {
				return new Ret<T>(node, Adjustment.NotAdjusted);
			}

			public static <T> Ret<T> adjusted(Node<T> node) {
				return new Ret<T>(node, Adjustment.Adjusted);
			}

			public final Node<T> n;
			public final Adjustment a;

			private Ret(Node<T> node, Adjustment adjustment) {
				this.n = node;
				this.a = adjustment;
			}

			public boolean adjusted() {
				return a == Adjustment.Adjusted;
			}
		}

		private enum Tilt {
			L, E, R
		}

		private static class Node<T> {
			public final Tilt t;
			public final char k;
			public final T v;
			public final Node<T> c;
			public final Node<T> l;
			public final Node<T> r;

			public Node(Tilt t, char k, T v, Node<T> c, Node<T> l, Node<T> r) {
				this.t = t;
				this.k = k;
				this.v = v;
				this.c = c;
				this.l = l;
				this.r = r;
			}

		}
	}

}
