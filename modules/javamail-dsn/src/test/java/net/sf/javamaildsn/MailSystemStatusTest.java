package net.sf.javamaildsn;

import junit.framework.TestCase;

public class MailSystemStatusTest extends TestCase {
    public void testUndefinedCanonicalMessage() {
        assertNull(new MailSystemStatus(5, 1, 1234).getCanonicalMessage());
    }
}
