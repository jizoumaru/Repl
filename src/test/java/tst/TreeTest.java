package tst;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class TreeTest {
	@Test
	public void testAdd() {
		assertEquals("null",
			Tree.empty().toString());

		assertEquals("E(2$, null, null, null)",
			Tree.empty().add("2").toString());

		assertEquals("L(2$, E(1$, null, null, null), null, null)",
			Tree.empty().add("2").add("1").toString());

		assertEquals("E(2$, null, null, null)",
			Tree.empty().add("2").add("2").toString());

		assertEquals("R(2$, null, E(3$, null, null, null), null)",
			Tree.empty().add("2").add("3").toString());

		assertEquals("E(2, null, null, L(2$, E(1$, null, null, null), null, null))",
			Tree.empty().add("22").add("21").toString());

		assertEquals("E(22$, null, null, null)",
			Tree.empty().add("22").add("22").toString());

		assertEquals("E(2, null, null, R(2$, null, E(3$, null, null, null), null))",
			Tree.empty().add("22").add("23").toString());

		assertEquals("E(22$, null, null, E(11$, null, null, null))",
			Tree.empty().add("22").add("2211").toString());

		assertEquals("E(22$, null, null, E(22$, null, null, null))",
			Tree.empty().add("22").add("2222").toString());

		assertEquals("E(22$, null, null, E(33$, null, null, null))",
			Tree.empty().add("22").add("2233").toString());

		assertEquals("E(22$, null, null, E(11$, null, null, null))",
			Tree.empty().add("2211").add("22").toString());

		assertEquals("E(2$, E(1$, null, null, null), E(4$, null, null, null), null)",
			Tree.empty().add("4").add("2").add("1").toString());

		assertEquals("E(3$, E(2$, null, null, null), E(4$, null, null, null), null)",
			Tree.empty().add("4").add("2").add("3").toString());

		assertEquals("E(3$, E(2$, null, null, null), E(4$, null, null, null), null)",
			Tree.empty().add("2").add("4").add("3").toString());

		assertEquals("E(4$, E(2$, null, null, null), E(5$, null, null, null), null)",
			Tree.empty().add("2").add("4").add("5").toString());

		assertEquals("E(4$, L(2$, E(1$, null, null, null), null, null), E(8$, E(6$, null, null, null), E(A$, null, null, null), null), null)",
			Tree.empty().add("8").add("4").add("A").add("2").add("6").add("1").toString());

		assertEquals("E(4$, R(2$, null, E(3$, null, null, null), null), E(8$, E(6$, null, null, null), E(A$, null, null, null), null), null)",
			Tree.empty().add("8").add("4").add("A").add("2").add("6").add("3").toString());

		assertEquals("E(6$, E(4$, E(2$, null, null, null), E(5$, null, null, null), null), R(8$, null, E(A$, null, null, null), null), null)",
			Tree.empty().add("8").add("4").add("A").add("2").add("6").add("5").toString());

		assertEquals("E(6$, L(4$, E(2$, null, null, null), null, null), E(8$, E(7$, null, null, null), E(A$, null, null, null), null), null)",
			Tree.empty().add("8").add("4").add("A").add("2").add("6").add("7").toString());

		assertEquals("E(6$, E(4$, E(2$, null, null, null), E(5$, null, null, null), null), R(8$, null, E(A$, null, null, null), null), null)",
			Tree.empty().add("4").add("2").add("8").add("6").add("A").add("5").toString());

		assertEquals("E(6$, L(4$, E(2$, null, null, null), null, null), E(8$, E(7$, null, null, null), E(A$, null, null, null), null), null)",
			Tree.empty().add("4").add("2").add("8").add("6").add("A").add("7").toString());

		assertEquals("E(8$, E(4$, E(2$, null, null, null), E(6$, null, null, null), null), L(A$, E(9$, null, null, null), null, null), null)",
			Tree.empty().add("4").add("2").add("8").add("6").add("A").add("9").toString());

		assertEquals("E(8$, E(4$, E(2$, null, null, null), E(6$, null, null, null), null), R(A$, null, E(B$, null, null, null), null), null)",
			Tree.empty().add("4").add("2").add("8").add("6").add("A").add("B").toString());
	}

	@Test
	public void testIterator() {
		var src = IntStream.range(0, 100000)
			.boxed()
			.map(i -> String.format("%08d", i))
			.toList();

		var list = new ArrayList<String>(src);
		Collections.shuffle(list);

		var tree = Tree.empty();

		for (var e : list) {
			tree = tree.add(e);
		}

		var actual = new ArrayList<String>();

		for (var pair : tree) {
			actual.add(pair.key);
		}

		assertEquals(src, actual);
	}

	@Test
	public void testLongestCommonPrefix() {
		var tree = Tree.empty()
			.put("AA", "A2")
			.put("AAAA", "A4")
			.put("AAAAAA", "A6")
			.put("B", "B1")
			.put("BBB", "B3")
			.put("CCC", "C3");

		assertEquals(null, tree.longestCommonPrefix(""));

		assertEquals(null, tree.longestCommonPrefix("A"));
		assertEquals(KeyValue.of("AA", "A2"), tree.longestCommonPrefix("AA"));
		assertEquals(KeyValue.of("AA", "A2"), tree.longestCommonPrefix("AAA"));
		assertEquals(KeyValue.of("AAAA", "A4"), tree.longestCommonPrefix("AAAA"));
		assertEquals(KeyValue.of("AAAA", "A4"), tree.longestCommonPrefix("AAAAA"));
		assertEquals(KeyValue.of("AAAAAA", "A6"), tree.longestCommonPrefix("AAAAAA"));
		assertEquals(KeyValue.of("AAAAAA", "A6"), tree.longestCommonPrefix("AAAAAAA"));

		assertEquals(KeyValue.of("B", "B1"), tree.longestCommonPrefix("B"));
		assertEquals(KeyValue.of("B", "B1"), tree.longestCommonPrefix("BB"));
		assertEquals(KeyValue.of("BBB", "B3"), tree.longestCommonPrefix("BBB"));
		assertEquals(KeyValue.of("BBB", "B3"), tree.longestCommonPrefix("BBBB"));

		assertEquals(null, tree.longestCommonPrefix("C"));
		assertEquals(null, tree.longestCommonPrefix("CC"));
		assertEquals(KeyValue.of("CCC", "C3"), tree.longestCommonPrefix("CCC"));
		assertEquals(KeyValue.of("CCC", "C3"), tree.longestCommonPrefix("CCCC"));
	}
}
