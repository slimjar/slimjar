package io.github.slimjar.resolver.pinger;

import io.github.slimjar.resolver.reader.FileDependencyDataProvider;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, HttpURLPinger.class})
public class URLPingerTest extends TestCase {

    public void testHttpURLPingerHttp() throws IOException {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final HttpURLConnection httpURLConnection = PowerMockito.mock(HttpURLConnection.class);
        PowerMockito.when(mockUrl.openConnection()).thenReturn(httpURLConnection);
        PowerMockito.when(mockUrl.getProtocol()).thenReturn("HTTP");
        PowerMockito.doNothing().when(httpURLConnection).addRequestProperty("","");
        PowerMockito.doNothing().when(httpURLConnection).connect();
        PowerMockito.doReturn(HttpURLConnection.HTTP_OK).when(httpURLConnection).getResponseCode();
        final URLPinger urlPinger = new HttpURLPinger();
        boolean result = urlPinger.ping(mockUrl);
        assertTrue("Valid http URL", result);
    }

    public void testHttpURLPingerHttps() throws IOException {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final HttpsURLConnection httpsURLConnection = PowerMockito.mock(HttpsURLConnection.class);
        PowerMockito.when(mockUrl.openConnection()).thenReturn(httpsURLConnection);
        PowerMockito.when(mockUrl.getProtocol()).thenReturn("HTTPS");
        PowerMockito.doNothing().when(httpsURLConnection).addRequestProperty("","");
        PowerMockito.doNothing().when(httpsURLConnection).connect();
        PowerMockito.doReturn(HttpURLConnection.HTTP_OK).when(httpsURLConnection).getResponseCode();
        final URLPinger urlPinger = new HttpURLPinger();
        boolean result = urlPinger.ping(mockUrl);
        assertTrue("Valid https URL", result);
    }

    public void testHttpURLPingerFailIfNotOk() throws IOException {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final HttpsURLConnection httpsURLConnection = PowerMockito.mock(HttpsURLConnection.class);
        PowerMockito.when(mockUrl.openConnection()).thenReturn(httpsURLConnection);
        PowerMockito.when(mockUrl.getProtocol()).thenReturn("HTTPS");
        PowerMockito.doNothing().when(httpsURLConnection).addRequestProperty("","");
        PowerMockito.doNothing().when(httpsURLConnection).connect();
        PowerMockito.doReturn(HttpURLConnection.HTTP_BAD_REQUEST).when(httpsURLConnection).getResponseCode();
        final URLPinger urlPinger = new HttpURLPinger();
        boolean result = urlPinger.ping(mockUrl);
        assertFalse("Non-OK should fail", result);
    }

    public void testHttpURLPingerExceptionOnPing() throws IOException {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final HttpsURLConnection httpsURLConnection = PowerMockito.mock(HttpsURLConnection.class);
        PowerMockito.when(mockUrl.openConnection()).thenReturn(httpsURLConnection);
        PowerMockito.when(mockUrl.getProtocol()).thenReturn("HTTPS");
        PowerMockito.doNothing().when(httpsURLConnection).addRequestProperty("","");
        PowerMockito.doThrow(new IOException()).when(httpsURLConnection).connect();
        PowerMockito.doReturn(HttpURLConnection.HTTP_BAD_REQUEST).when(httpsURLConnection).getResponseCode();
        final URLPinger urlPinger = new HttpURLPinger();
        boolean result = urlPinger.ping(mockUrl);
        assertFalse("Exception should fail", result);
    }

    public void testHttpURLPingerUnsupportedProtocol() {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final URLPinger urlPinger = new HttpURLPinger();

        PowerMockito.doReturn("NON-EXISTENT-PROTOCOL").when(mockUrl).getProtocol();
        boolean result = urlPinger.ping(mockUrl);
        assertFalse("Non-OK should fail", result);
    }

}