package tst;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Node<T> {
	public static <T> Node<T> E(Node<T> n, Node<T> l, Node<T> r) {
		return new Node<T>(NodeState.E, n.k, l, r, n.m, n.v, n.e);
	}

	public static <T> Node<T> L(Node<T> n, Node<T> l, Node<T> r) {
		return new Node<T>(NodeState.L, n.k, l, r, n.m, n.v, n.e);
	}

	public static <T> Node<T> R(Node<T> n, Node<T> l, Node<T> r) {
		return new Node<T>(NodeState.R, n.k, l, r, n.m, n.v, n.e);
	}

	public static <T> Node<T> put(Node<T> n, String k, T v) {
		return put(n, k.toCharArray(), 0, v).n;
	}

	public static <T> NodeResult<T> put(Node<T> n, char[] k, int o, T v) {
		if (n == null) {
			return new NodeResult<T>(false, new Node<T>(NodeState.E, Arrays.copyOfRange(k, o, k.length), null, null, null, v, true));
		}

		if (k[o] < n.k[0]) {
			var res = put(n.l, k, o, v);
			var l = res.n;

			if (res.b) {
				return new NodeResult<T>(true, new Node<T>(n.s, n.k, l, n.r, n.m, n.v, n.e));
			}

			return switch (n.s) {
				case E -> new NodeResult<T>(false, L(n, l, n.r));
				case R -> new NodeResult<T>(true, E(n, l, n.r));
				case L -> switch (l.s) {
					case L -> new NodeResult<T>(true, E(l, l.l, E(n, l.r, n.r)));
					case R -> switch (l.r.s) {
						case E -> new NodeResult<T>(true, E(l.r, E(l, l.l, l.r.l), E(n, l.r.r, n.r)));
						case R -> new NodeResult<T>(true, E(l.r, L(l, l.l, l.r.l), E(n, l.r.r, n.r)));
						case L -> new NodeResult<T>(true, E(l.r, E(l, l.l, l.r.l), R(n, l.r.r, n.r)));
					};
					case E -> throw new IllegalStateException();
				};
			};
		}

		if (n.k[0] < k[o]) {
			var res = put(n.r, k, o, v);
			var r = res.n;

			if (res.b) {
				return new NodeResult<T>(true, new Node<T>(n.s, n.k, n.l, r, n.m, n.v, n.e));
			}

			return switch (n.s) {
				case E -> new NodeResult<T>(false, R(n, n.l, r));
				case L -> new NodeResult<T>(true, E(n, n.l, r));
				case R -> switch (r.s) {
					case R -> new NodeResult<T>(true, E(r, E(n, n.l, r.l), r.r));
					case L -> switch (r.l.s) {
						case E -> new NodeResult<T>(true, E(r.l, E(n, n.l, r.l.l), E(r, r.l.r, r.r)));
						case L -> new NodeResult<T>(true, E(r.l, E(n, n.l, r.l.l), R(r, r.l.r, r.r)));
						case R -> new NodeResult<T>(true, E(r.l, L(n, n.l, r.l.l), E(r, r.l.r, r.r)));
					};
					case E -> throw new IllegalStateException();
				};
			};
		}

		var i = 0;

		while (o + i < k.length && i < n.k.length && k[o + i] == n.k[i]) {
			i++;
		}

		if (o + i < k.length) {
			if (i < n.k.length) {
				if (k[o + i] < n.k[i]) {
					var l = new Node<T>(NodeState.E, Arrays.copyOfRange(k, o + i, k.length), null, null, null, v, true);
					var c = new Node<T>(NodeState.L, Arrays.copyOfRange(n.k, i, n.k.length), l, null, n.m, n.v, n.e);
					var p = new Node<T>(n.s, Arrays.copyOfRange(k, o, o + i), n.l, n.r, c, null, false);
					return new NodeResult<T>(true, p);
				} else {
					var r = new Node<T>(NodeState.E, Arrays.copyOfRange(k, o + i, k.length), null, null, null, v, true);
					var c = new Node<T>(NodeState.R, Arrays.copyOfRange(n.k, i, n.k.length), null, r, n.m, n.v, n.e);
					var p = new Node<T>(n.s, Arrays.copyOfRange(k, o, o + i), n.l, n.r, c, null, false);
					return new NodeResult<T>(true, p);
				}
			} else {
				return new NodeResult<T>(true, new Node<T>(n.s, n.k, n.l, n.r, put(n.m, k, o + i, v).n, n.v, n.e));
			}
		} else {
			if (i < n.k.length) {
				var c = new Node<T>(NodeState.E, Arrays.copyOfRange(n.k, i, n.k.length), null, null, n.m, n.v, n.e);
				var p = new Node<T>(n.s, Arrays.copyOfRange(k, o, o + i), n.l, n.r, c, v, true);
				return new NodeResult<T>(true, p);
			} else {
				var p = new Node<T>(n.s, n.k, n.l, n.r, n.m, v, true);
				return new NodeResult<T>(true, p);
			}
		}
	}

	public static <T> KeyValue<String, T> longestCommonPrefix(Node<T> node, char[] cs, int off) {
		var n = node;
		var l = (Node<T>) null;
		var o = off;
		var t = off;

		while (n != null && o < cs.length) {
			if (cs[o] < n.k[0]) {
				n = n.l;
				continue;
			}

			if (n.k[0] < cs[o]) {
				n = n.r;
				continue;
			}

			var i = 1;

			while (o + i < cs.length && i < n.k.length && cs[o + i] == n.k[i]) {
				i++;
			}

			if (i < n.k.length) {
				break;
			}

			if (n.e) {
				l = n;
				t = o + i;
			}

			n = n.m;
			o += i;
		}

		if (l != null) {
			return KeyValue.of(new String(cs, off, t - off), l.v);
		}

		return null;
	}

	public static <T> Iterator<KeyValue<String, T>> iterator(Node<T> node) {
		enum State {
			LEFT, MID
		}

		record Frame<T>(State s, Node<T> n, Concat p) {
		}

		return new Iterator<>() {
			final Object defaultValue = new Object();
			Object value = defaultValue;
			ArrayDeque<Frame<T>> stack;

			@Override
			public boolean hasNext() {
				if (value == defaultValue) {
					if (stack == null) {
						stack = new ArrayDeque<Frame<T>>();
						stack.push(new Frame<T>(State.LEFT, node, Concat.EMPTY));
					}

					while (!stack.isEmpty()) {
						var frame = stack.pop();

						if (frame.n == null) {
							continue;
						}

						if (frame.s == State.LEFT) {
							stack.push(new Frame<T>(State.MID, frame.n, frame.p));
							stack.push(new Frame<T>(State.LEFT, frame.n.l, frame.p));
						} else {
							stack.push(new Frame<T>(State.LEFT, frame.n.r, frame.p));
							stack.push(new Frame<T>(State.LEFT, frame.n.m, frame.p.add(frame.n.k)));

							if (frame.n.e) {
								value = KeyValue.<String, T>of(frame.p.add(frame.n.k).toString(), frame.n.v);
								break;
							}
						}
					}

					if (value == defaultValue) {
						value = null;
					}
				}

				return value != null;
			}

			@Override
			public KeyValue<String, T> next() {
				if (hasNext()) {
					@SuppressWarnings("unchecked")
					var ret = (KeyValue<String, T>) value;
					value = defaultValue;
					return ret;
				}
				throw new NoSuchElementException();
			}
		};
	}

	public static <T> String toString(Node<T> n) {
		if (n == null) {
			return "null";
		}
		return n.s + "(" + new String(n.k) + (n.e ? "$" : "") + ", " + toString(n.l) + ", " + toString(n.r) + ", " + toString(n.m) + ")";
	}

	public static <T> void dump(Node<T> n) {
		dump(n, "", "", "", "");
	}

	public static <T> void dump(Node<T> n, String indent, String p, String lp, String rp) {
		if (n == null) {
			return;
		}

		dump(n.l, indent + lp, "┌", "  ", "│");

		var k = new String(n.k) + (n.e ? "＄" : "");

		if (n.m == null) {
			System.out.println(indent + p + k);
		} else {
			var s = " ".repeat(k.length() * 2);
			dump(n.m, indent, p + k + "─", lp + (n.l == null ? "  " : "│") + s, rp + (n.r == null ? "  " : "│") + s);
		}

		dump(n.r, indent + rp, "└", "│", "  ");
	}

	public final NodeState s;
	public final char[] k;
	public final Node<T> l;
	public final Node<T> r;
	public final Node<T> m;
	public final T v;
	public final boolean e;

	public Node(NodeState s, char[] k, Node<T> l, Node<T> r, Node<T> m, T v, boolean e) {
		this.s = s;
		this.k = k;
		this.l = l;
		this.r = r;
		this.m = m;
		this.v = v;
		this.e = e;
	}
}
