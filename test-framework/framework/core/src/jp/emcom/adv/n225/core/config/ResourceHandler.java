package jp.emcom.adv.n225.core.config;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import org.w3c.dom.Document;

/**
 * 
 * @author alex
 *
 */
public interface ResourceHandler extends Serializable {
    public String getLoaderName();

    public String getLocation();

    public Document getDocument() throws GenericConfigException;

    public InputStream getStream() throws GenericConfigException;

    public URL getURL() throws GenericConfigException;

    public boolean isFileResource() throws GenericConfigException;

    public String getFullLocation() throws GenericConfigException;
}
