package net.sf.javamaildsn.heuristics;

import java.io.InputStream;

import junit.framework.TestCase;
import net.sf.javamaildsn.DeliveryStatus;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

public class RuleBasedHeuristicsTest extends TestCase {
	public void testDefault() throws Exception {
		Heuristics heuristics = RuleBasedHeuristics.getDefaultInstance();
		FileSystemManager fsm = VFS.getManager();
		FileObject root = fsm.resolveFile(RuleBasedHeuristicsTest.class.getResource("/heuristics/.root").toExternalForm()).getParent();
		for (Reason reason : Reason.values()) {
			for (FileObject file : root.resolveFile(reason.toString().toLowerCase()).getChildren()) {
				InputStream is = file.getContent().getInputStream();
				DeliveryStatus ds = new DeliveryStatus(is);
				is.close();
				assertEquals("Reason for " + file, reason, heuristics.getReason(ds.getPerRecipientParts()[0].getDiagnostic()));
			}
		}
	}
}
