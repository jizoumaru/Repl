package tst;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ReplacementTest {
	@Test
	public void testReplace() {
		assertEquals("", Replacement.empty().replace(""));
		assertEquals("", Replacement.empty().put("A", "B").replace(""));
		assertEquals("T", Replacement.empty().replace("T"));
		assertEquals("B", Replacement.empty().put("A", "B").replace("A"));
		assertEquals("BA", Replacement.empty().put("A", "B").put("B", "A").replace("AB"));
		assertEquals("XXBBXXAAXX", Replacement.empty().put("A", "B").put("B", "A").replace("XXAAXXBBXX"));
		assertEquals("B2B1", Replacement.empty().put("A", "B1").put("AA", "B2").replace("AAA"));
		
		assertEquals("BCA", Replacement.empty().put("A", "B").put("B", "C").put("C", "A").replace("ABC"));
		assertEquals("AB", Replacement.empty().put("AA", "A").put("AABB", "AB").replace("AAB"));
		assertEquals("XABX", Replacement.empty().put("AABB", "AB").put("AACC", "AC").replace("XAABBX"));
		assertEquals("XAADDX", Replacement.empty().put("AABB", "A").put("AACC", "AB").replace("XAADDX"));
	}
	
	@Test
	public void testIterator() {
		assertEquals(false, Replacement.empty().iterator().hasNext());
		
		var iter = Replacement.empty().put("A", "VA").put("B", "VB").put("C", "VC").iterator();
		assertEquals(KeyValue.of("A", "VA"), iter.next());
		assertEquals(KeyValue.of("B", "VB"), iter.next());
		assertEquals(KeyValue.of("C", "VC"), iter.next());
	}
}
