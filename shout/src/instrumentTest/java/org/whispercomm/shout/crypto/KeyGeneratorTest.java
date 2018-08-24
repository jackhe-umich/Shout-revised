
package org.whispercomm.shout.crypto;

import java.math.BigInteger;
import java.security.spec.InvalidKeySpecException;

import org.junit.Before;
import org.junit.Test;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.encoders.Hex;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class KeyGeneratorTest {

	private KeyGenerator generator;

	@Before
	public void setup() {
		generator = new KeyGenerator();
	}

	@Test
	public void testGenerateKeyPair() {
		ECKeyPair pair = generator.generateKeyPair();
		ECPublicKeyParameters publicKey = pair.getPublicKey().getECPublicKeyParameters();
		ECPrivateKeyParameters privateKey = pair.getPrivateKey().getECPrivateKeyParameters();

		assertThat(publicKey.getParameters(), is(equalTo(CryptoParams.DOMAIN_PARAMS)));
		assertThat(privateKey.getParameters(), is(equalTo(CryptoParams.DOMAIN_PARAMS)));
	}

	@Test
	public void testGeneratePublicKeyFromXAndY() {
		ECDomainParameters domain = CryptoParams.DOMAIN_PARAMS;
		BigInteger x = new BigInteger(
				Hex.decode("008101ece47464a6ead70cf69a6e2bd3d88691a3262d22cba4f7635eaff26680a8"));
		BigInteger y = new BigInteger(
				Hex.decode("00d8a12ba61d599235f67d9cb4d58f1783d3ca43e78f0a5abaa624079936c0c3a9"));

		ECPoint point = domain.getCurve().createPoint(x, y, false);
		ECPublicKey key = new ECPublicKey(new ECPublicKeyParameters(point,
				domain));

		assertThat(generator.generatePublic(x, y), is(key));
	}

	@Test
	public void testPublicKeyEncoding() {
		ECDomainParameters domain = CryptoParams.DOMAIN_PARAMS;
		BigInteger x = new BigInteger(
				Hex.decode("008101ece47464a6ead70cf69a6e2bd3d88691a3262d22cba4f7635eaff26680a8"));
		BigInteger y = new BigInteger(
				Hex.decode("00d8a12ba61d599235f67d9cb4d58f1783d3ca43e78f0a5abaa624079936c0c3a9"));

		ECPoint point = domain.getCurve().createPoint(x, y, false);
		ECPublicKey key = new ECPublicKey(new ECPublicKeyParameters(point,
				domain));

		byte[] encoded = Hex
				.decode("308201333081EC06072A8648CE3D02013081E0020101302C06072A8648CE3D0101022100FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF30440420FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC04205AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B0441046B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2964FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5022100FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551020101034200048101ECE47464A6EAD70CF69A6E2BD3D88691A3262D22CBA4F7635EAFF26680A8D8A12BA61D599235F67D9CB4D58F1783D3CA43E78F0A5ABAA624079936C0C3A9");
		assertThat(KeyGenerator.encodePublic(key), is(encoded));
	}

	@Test
	public void testPublicKeyDecoding() throws InvalidKeySpecException {
		byte[] encoded = Hex
				.decode("308201333081EC06072A8648CE3D02013081E0020101302C06072A8648CE3D0101022100FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF30440420FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC04205AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B0441046B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2964FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5022100FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551020101034200048101ECE47464A6EAD70CF69A6E2BD3D88691A3262D22CBA4F7635EAFF26680A8D8A12BA61D599235F67D9CB4D58F1783D3CA43E78F0A5ABAA624079936C0C3A9");

		ECDomainParameters domain = CryptoParams.DOMAIN_PARAMS;
		BigInteger x = new BigInteger(1,
				Hex.decode("8101ece47464a6ead70cf69a6e2bd3d88691a3262d22cba4f7635eaff26680a8"));
		BigInteger y = new BigInteger(1,
				Hex.decode("d8a12ba61d599235f67d9cb4d58f1783d3ca43e78f0a5abaa624079936c0c3a9"));

		ECPoint point = domain.getCurve().createPoint(x, y, false);
		ECPublicKey key = new ECPublicKey(new ECPublicKeyParameters(point,
				domain));

		assertThat(KeyGenerator.generatePublic(encoded), is(key));
	}

	@Test(expected = InvalidKeySpecException.class)
	public void testPublicKeyDecodingThrowsInvalidKeySpecException() throws InvalidKeySpecException {
		byte[] encoded = Hex
				.decode("308201333081EC06072A8648CE3D02013081E0020101302C06072A864FCE3D0101022100FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF30440420FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC04205AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B0441046B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2964FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5022100FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551020101034200048101ECE47464A6EAD70CF69A6E2BD3D88691A3262D22CBA4F7635EAFF26680A8D8A12BA61D599235F67D9CB4D58F1783D3CA43E78F0A5ABAA624079936C0C3A9");
		KeyGenerator.generatePublic(encoded);
	}

	@Test
	public void testPrivateKeyEncoding() {
		ECDomainParameters domain = CryptoParams.DOMAIN_PARAMS;
		BigInteger d = new BigInteger(1,
				Hex.decode("70a12c2db16845ed56ff68cfc21a472b3f04d7d6851bf6349f2d7d5b3452b38a"));

		ECPrivateKey key = new ECPrivateKey(new ECPrivateKeyParameters(d, domain));

		byte[] encoded = Hex
				.decode("308202050201003081EC06072A8648CE3D02013081E0020101302C06072A8648CE3D0101022100FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF30440420FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC04205AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B0441046B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2964FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5022100FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC6325510201010482010F3082010B020101042070A12C2DB16845ED56FF68CFC21A472B3F04D7D6851BF6349F2D7D5B3452B38AA081E33081E0020101302C06072A8648CE3D0101022100FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF30440420FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC04205AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B0441046B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2964FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5022100FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551020101");
		assertThat(KeyGenerator.encodePrivate(key), is(encoded));
	}

	@Test
	public void testPrivateKeyDecoding() throws InvalidKeySpecException {
		byte[] encoded = Hex
				.decode("308202050201003081EC06072A8648CE3D02013081E0020101302C06072A8648CE3D0101022100FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF30440420FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC04205AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B0441046B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2964FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5022100FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC6325510201010482010F3082010B020101042070A12C2DB16845ED56FF68CFC21A472B3F04D7D6851BF6349F2D7D5B3452B38AA081E33081E0020101302C06072A8648CE3D0101022100FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF30440420FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC04205AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B0441046B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2964FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5022100FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551020101");

		ECDomainParameters domain = CryptoParams.DOMAIN_PARAMS;
		BigInteger d = new BigInteger(1,
				Hex.decode("70a12c2db16845ed56ff68cfc21a472b3f04d7d6851bf6349f2d7d5b3452b38a"));

		ECPrivateKey key = new ECPrivateKey(new ECPrivateKeyParameters(d, domain));
		assertThat(KeyGenerator.generatePrivate(encoded), is(key));
	}

	@Test(expected = InvalidKeySpecException.class)
	public void testPrivateKeyDecodingThrowsInvalidKeySpecException()
			throws InvalidKeySpecException {
		byte[] badEncoded = Hex
				.decode("308202050201003081EC06072A8648FE3D02013081E0020101302C06072A8648CE3D0101022100FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF30440420FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC04205AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B0441046B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2964FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5022100FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC6325510201010482010F3082010B020101042070A12C2DB16845ED56FF68CFC21A472B3F04D7D6851BF6349F2D7D5B3452B38AA081E33081E0020101302C06072A8648CE3D0101022100FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF30440420FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC04205AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B0441046B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C2964FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5022100FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551020101");
		KeyGenerator.generatePrivate(badEncoded);
	}

}
