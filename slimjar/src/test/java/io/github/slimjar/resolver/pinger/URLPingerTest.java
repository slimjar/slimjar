//
// MIT License
//
// Copyright (c) 2021 Vaishnav Anil
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package io.github.slimjar.resolver.pinger;

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