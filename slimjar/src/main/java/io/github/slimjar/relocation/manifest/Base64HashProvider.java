package io.github.slimjar.relocation.manifest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class Base64HashProvider implements HashProvider {
    @Override
    public String hash(final InputStream inputStream, final String algorithm) throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        final DigestInputStream digestStream = new DigestInputStream(inputStream, digest);
        final OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) {

            }
        };
        copyData(digestStream, outputStream);
        digestStream.close();
        return new String(Base64.getEncoder().encode(digest.digest()));
    }

    private static void copyData(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[8 * 1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
}
