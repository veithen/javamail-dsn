package net.sf.javamaildsn.type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import net.sf.javamaildsn.DeliveryStatus;
import net.sf.javamaildsn.PerRecipientDeliveryStatus;

/**
 * Parser for typed fields as described in section 2.1.2 of RFC1894. 
 * 
 * <table>
 *   <tr><th>Field(s)</th><th>Value class</th></tr>
 *   <tr>
 *     <td>
 *       <tt>Final-Recipient</tt><br>
 *       <tt>Original-Recipient</tt>
 *     </td>
 *     <td>{@link javax.mail.Address}</td>
 *   </tr>
 *   <tr>
 *     <td>
 *       <tt>DSN-Gateway</tt><br>
 *       <tt>Received-From-MTA</tt><br>
 *       <tt>Reporting-MTA</tt><br>
 *       <tt>Remote-MTA</tt>
 *     </td>
 *     <td>{@link net.sf.javamaildsn.MtaName}</td>
 *   </tr>
 *   <tr>
 *     <td><tt>Diagnostic-Code</tt></td>
 *     <td>{@link net.sf.javamaildsn.Diagnostic}</td>
 *   </tr>
 * </table>
 * 
 * @author Andreas Veithen
 */
public class TypedFieldParser<T> {
    private static final Map<Class<?>,TypedFieldParser<?>> parsers = new HashMap<Class<?>,TypedFieldParser<?>>();
    
	private final Map<String,FieldType<? extends T>> types = new HashMap<String,FieldType<? extends T>>();
	private FieldType<? extends T> defaultType;
	
	private TypedFieldParser(Class<T> valueClass) throws MessagingException {
        String resource = "META-INF/" + valueClass.getName();
        Enumeration<URL> urls;
        try {
            urls = TypedFieldParser.class.getClassLoader().getResources(resource);
        }
        catch (IOException ex) {
            throw new MessagingException("Unable to load resource '" + resource + "'", ex);
        }
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            try {
                InputStream in = url.openConnection().getInputStream();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.length() > 0 && line.charAt(0) != '#') {
                            String[] parts = line.split("\\s*=\\s*");
                            if (parts.length == 2) {
                                String identifier = parts[0];
                                Class<?> typeClass;
                                try {
                                    typeClass = Class.forName(parts[1]);
                                }
                                catch (ClassNotFoundException ex) {
                                    throw new MessagingException("Unable to load class " + parts[1]);
                                }
                                FieldType<? extends T> type;
                                try {
                                    type = (FieldType<? extends T>)typeClass.newInstance();
                                }
                                catch (Exception ex) {
                                    throw new MessagingException("Unable to instantiate " + typeClass.getName(), ex);
                                }
                                if (identifier.equals("*")) {
                                    defaultType = type;
                                } else {
                                    types.put(normalizeIdentifier(identifier), type);
                                }
                            } else {
                                throw new MessagingException("Invalid format in '" + url + "', line '" + line + "'");
                            }
                        }
                    }
                }
                finally {
                    in.close();
                }
            }
            catch (IOException ex) {
                throw new MessagingException("Error loading " + url);
            }
        }
	}
	
    @SuppressWarnings("unchecked")
    public static <T> TypedFieldParser<T> getInstance(Class<T> valueClass) throws MessagingException {
        synchronized (parsers) {
            TypedFieldParser<T> parser = (TypedFieldParser<T>)parsers.get(valueClass);
            if (parser == null) {
                parser = new TypedFieldParser<T>(valueClass);
                parsers.put(valueClass, parser);
            }
            return parser;
        }
    }
    
	private String normalizeIdentifier(String identifier) {
		return identifier.toLowerCase();
	}
	
	public T parse(String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException {
		int index = value.indexOf(';');
		if (index == -1) {
			throw new MessagingException("Invalid format for typed field");
		} else {
			String identifier = normalizeIdentifier(value.substring(0, index).trim());
			FieldType<? extends T> type = types.get(identifier);
			if (type == null) {
			    if (defaultType == null) {
			        throw new MessagingException("Unknown type '" + identifier + "' and no default type defined");
			    }
				type = defaultType;
			}
			return type.parse(identifier, MimeUtility.unfold(value.substring(index+1).trim()), ds, rds);
		}
	}
}
