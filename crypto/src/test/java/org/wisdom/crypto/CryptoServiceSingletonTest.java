package org.wisdom.crypto;

import org.junit.Test;
import org.wisdom.api.crypto.Crypto;
import org.wisdom.api.crypto.Hash;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Checks the Crypto Service Implementation.
 */
public class CryptoServiceSingletonTest {

    public static final String SECRET = "1111111111111111111111111111111111111111111111111111111111111111";

    Crypto crypto = new CryptoServiceSingleton(SECRET,
            Hash.MD5);

    @Test
    public void testMD5() throws Exception {
        String s = crypto.hash("hello");
        assertThat(s).isEqualTo("XUFAKrxLKna5cZ2REBfFkg==");
    }

    @Test
    public void testSha1() throws Exception {
        String s = crypto.hash("hello", Hash.SHA1);
        assertThat(s).isEqualTo("qvTGHdzF6KLavt4PO0gs2a6pQ00=");
    }

    @Test
    public void testSha256() throws Exception {
        String s = crypto.hash("hello", Hash.SHA256);
        assertThat(s).isEqualTo("LPJNul+wow4m6DsqxbninhsWHlwfp0JecwQzYpOLmCQ=");
    }

    @Test
    public void testSha512() throws Exception {
        String s = crypto.hash("hello", Hash.SHA512);
        assertThat(s).isEqualTo("m3HSJL1i83hdltRq0+o9czGb+8KJDKra4t/3JRlnPKcjI8PZm6XBHXx6zG4UuMXaDEZjR1wuXDre9G9zvN7AQw==");
    }

    @Test
    public void testSign() {
        String s = crypto.sign("hello");
        assertThat(s).isEqualTo("0cc32715731d6658e33747d72977add9ec429ba3");
    }

    @Test
    public void testAES() {
        String s = crypto.encryptAES("hello");
        assertThat(s).isEqualTo("73b7d28724969d6467e8d8da71f828c7");

        String s2 = crypto.decryptAES(s);
        assertThat(s2).isEqualTo("hello");
    }

    @Test
    public void testTokenSignature() {
        String raw = "hello";
        String encrypted = crypto.signToken(raw);
        String extracted = crypto.extractSignedToken(encrypted);
        assertThat(extracted).isEqualTo(raw);
    }

    @Test
    public void testBase64() {
        String s = "hello";
        String s1 = crypto.encodeBASE64(s.getBytes());
        assertThat(s1).isEqualTo("aGVsbG8=");
        byte[] s2 = crypto.decodeBASE64(s1);
        assertThat(new String(s2)).isEqualTo(s);
    }

    @Test
    public void testHexMD5() {
        String s = "hello";
        String s1 = crypto.hexMD5(s);
        assertThat(s1).isEqualTo("5d41402abc4b2a76b9719d911017c592");
    }

    @Test
    public void testHexSha1() {
        String s = "hello";
        String s1 = crypto.hexSHA1(s);
        assertThat(s1).isEqualTo("aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d");
    }
}