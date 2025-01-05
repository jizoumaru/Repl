package tst;

public final class Concat {
	public static Concat EMPTY = new Concat(new char[][] {});

	public static Concat empty() {
		return EMPTY;
	}

	public final char[][] values;

	public Concat(char[][] values) {
		this.values = values;
	}

	public Concat add(char[] value) {
		var vals = new char[values.length + 1][];
		System.arraycopy(values, 0, vals, 0, values.length);
		vals[vals.length - 1] = value;
		return new Concat(vals);
	}

	public String toString() {
		var len = 0;

		for (var v : values) {
			len += v.length;
		}

		var val = new char[len];
		var off = 0;

		for (var v : values) {
			System.arraycopy(v, 0, val, off, v.length);
			off += v.length;
		}

		return new String(val);
	}
}
