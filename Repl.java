import java.util.ArrayList;

public class Repl {
	public static class ReplBuilder {
		private static class Pair {
			final String key;
			final String value;

			Pair(String key, String value) {
				this.key = key;
				this.value = value;
			}
		}

		private final ArrayList<Pair> list = new ArrayList<Pair>();

		public ReplBuilder add(String key, String value) {
			list.add(new Pair(key, value));
			return this;
		}

		public Repl build() {
			list.sort((a, b) -> a.key.compareTo(b.key));

			var k = new char[list.size()][];
			var v = new String[list.size()];

			for (var i = 0; i < list.size(); i++) {
				var p = list.get(i);
				k[i] = p.key.toCharArray();
				v[i] = p.value;
			}

			return new Repl(k, v);
		}
	}

	public static ReplBuilder builder() {
		return new ReplBuilder();
	}

	private final char[][] keys;
	private final String[] values;

	public Repl(char[][] keys, String[] values) {
		this.keys = keys;
		this.values = values;
	}

	public String replace(String s) {
		var res = new StringBuilder(s.length());
		var cs = s.toCharArray();
		var f = 0;
		var i = 0;

		while (i < cs.length) {
			var idx = indexOf(keys, cs, i, cs.length);

			if (idx >= 0) {
				res.append(cs, f, i - f).append(values[idx]);
				i += keys[idx].length;
				f = i;
			} else {
				i += 1;
			}
		}

		return res.append(cs, f, i - f).toString();
	}

	private static int indexOf(char[][] keys, char[] target, int from, int to) {
		var res = -1;
		var rf = 0;
		var rt = keys.length;
		var c = 0;
		
		for (var i = from; i < to; i++, c++) {
			var f = indexOf(keys, rf, rt, c, target, i);
			var t = lastIndexOf(keys, f, rt, c, target, i);
			
			if (f >= t) {
				break;
			}

			rf = f;
			rt = t;

			if (keys[rf].length - 1 == c) {
				res = rf;
				rf += 1;
			}
		}

		return res;
	}

	private static int indexOf(char[][] keys, int from, int to, int c, char[] target, int i) {
		var f = from;
		var t = to;

		while (f < t) {
			var m = f + ((t - f) >>> 1);

			if (target[i] <= keys[m][c]) {
				t = m;
			} else {
				f = m + 1;
			}
		}

		return f;
	}
	
	private static int lastIndexOf(char[][] keys, int from, int to, int c, char[] target, int i) {
		var f = from;
		var t = to;

		while (f < t) {
			var m = f + ((t - f) >>> 1);

			if (target[i] < keys[m][c]) {
				t = m;
			} else {
				f = m + 1;
			}
		}

		return t;
	}

}
