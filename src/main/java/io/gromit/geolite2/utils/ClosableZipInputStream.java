package io.gromit.geolite2.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipInputStream;

/**
 * The Class ClosableZipInputStream.
 */
public class ClosableZipInputStream extends ZipInputStream {

    /**
     * Instantiates a new closable zip input stream.
     *
     * @param in the in
     * @param charset the charset
     */
    public ClosableZipInputStream(InputStream in, Charset charset) {
        super(in, charset);
    }

    /**
     * Close.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public void close() throws IOException {
    }

    /**
     * My close.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void manualClose() throws IOException {
        super.close();
    }

}
