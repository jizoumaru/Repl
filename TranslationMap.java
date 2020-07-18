package translation;
import java.util.concurrent.ConcurrentHashMap;

public class TranslationMap {
	public static void main(String[] args) {
		var trans = TranslationMap.builder()
				.add("o", "お")
				.add("ha", "は")
				.add("yo", "よ")
				.add("u", "う")
				.add("go", "ご")
				.add("za", "ざ")
				.add("i", "い")
				.add("ma", "ま")
				.add("su", "す")
				.add(".", "。")
				.build();

		System.out.println(trans.translate("ohayougozaimasu."));
	}

	public static class Builder {
		final Node root = new Node(null);

		public Builder add(String key, String value) {
			var cs = key.toCharArray();
			var node = root;

			for (var i = 0; i < cs.length; i++) {
				var sub = node.map.get(cs[i]);

				if (sub == null) {
					var tail = new Node(value);

					for (var j = cs.length - 1; j > i; j--) {
						tail = new Node(cs[j], tail);
					}

					node.map.put(cs[i], tail);
					return this;
				}

				node = sub;
			}

			node.value = value;
			return this;
		}

		public TranslationMap build() {
			return new TranslationMap(root);
		}
	}

	static class Node {
		final ConcurrentHashMap<Character, Node> map = new ConcurrentHashMap<Character, Node>();
		String value;

		Node(String value) {
			this.value = value;
		}

		Node(Character key, Node node) {
			this.value = null;
			this.map.put(key, node);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	final Node root;

	TranslationMap(Node root) {
		this.root = root;
	}

	public String translate(String key) {
		var builder = (StringBuilder) null;
		var cs = key.toCharArray();
		var offset = 0;
		var from = 0;

		while (offset < cs.length) {
			var node = root;
			var count = 0;
			var value = (String) null;

			for (var i = offset; i < cs.length; i++) {
				node = node.map.get(cs[i]);

				if (node == null) {
					break;
				}

				if (node.value != null) {
					count = i - offset + 1;
					value = node.value;
				}
			}

			if (value == null) {
				offset += 1;
			} else {
				if (builder == null) {
					builder = new StringBuilder();
				}
				builder.append(cs, from, offset - from).append(value);
				offset += count;
				from = offset;
			}
		}

		if (from == 0) {
			return key;
		} else {
			return builder.append(cs, from, offset - from).toString();
		}
	}
}
