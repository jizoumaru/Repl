
class Trim {
	private final char[] values;

	Trim(char[] values) {
		var a = values.clone();
		Arrays.sort(a);
		this.values = a;
	}

	String trim(String src) {
		var cs = src.toCharArray();
		var f = 0;

		while (f < cs.length
				&& Arrays.binarySearch(values, cs[f]) >= 0) {
			f++;
		}

		var l = cs.length;

		while (l - 1 > f
				&& Arrays.binarySearch(values, cs[l - 1]) >= 0) {
			l--;
		}

		if (l - f == cs.length) {
			return src;
		}

		if (f < l) {
			return new String(cs, f, l - f);
		} else {
			return "";
		}
	}

}
