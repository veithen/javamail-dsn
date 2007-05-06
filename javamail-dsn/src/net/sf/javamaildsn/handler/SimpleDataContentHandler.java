package net.sf.javamaildsn.handler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.activation.UnsupportedDataTypeException;

/**
 * @author Andreas Veithen
 */
public abstract class SimpleDataContentHandler<C> implements DataContentHandler {
	private final ActivationDataFlavor supportedDataFlavor;
	private final Class<C> representationClass;
	
	public SimpleDataContentHandler(Class<C> representationClass, String mimeType, String humanPresentableName) {
		supportedDataFlavor = new ActivationDataFlavor(representationClass, mimeType, humanPresentableName);
		this.representationClass = representationClass;
	}
	
	public final C getTransferData(DataFlavor dataFlavor, DataSource dataSource) throws UnsupportedFlavorException, IOException {
		if (supportedDataFlavor.equals(dataFlavor)) {
			return getContent(dataSource);
		} else {
			throw new UnsupportedFlavorException(dataFlavor);
		}
	}
	
	public final DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { supportedDataFlavor };
	}
	
	public final void writeTo(Object _object, String mimeType, OutputStream os) throws IOException {
		if (!supportedDataFlavor.getMimeType().equals(mimeType)) {
			throw new UnsupportedDataTypeException("Unsupported MIME type " + mimeType);
		}
		C object;
		try {
			object = representationClass.cast(_object);
		}
		catch (ClassCastException ex) {
			throw new UnsupportedDataTypeException("Unsupported representation class " + _object.getClass());
		}
		writeTo(representationClass.cast(object), os);
	}
	
	public abstract C getContent(DataSource dataSource) throws IOException;
	public abstract void writeTo(C object, OutputStream os) throws IOException;
}
