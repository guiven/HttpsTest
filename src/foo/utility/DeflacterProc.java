/**
 * DeflacterProc.java - 2010-9-16
 *
 * Licensed Property to China UnionPay Co., Ltd.
 *
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * Project Info: China UnionPay Internet Acquiring Project
 *
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *
 * =============================================================================
 */
package foo.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

/**
 * Descriptor： 使用流行的 ZLIB 压缩程序库为通用压缩提供支持
 * @author tobba
 * 
 */
public class DeflacterProc{

//	/**
//	 * Descriptor： 压缩输入流
//	 * @param inStream
//	 * @param outStream
//	 * @return PipedOutputStream
//	 */
//	public PipedOutputStream deflater(PipedInputStream inStream, PipedOutputStream outStream)
//			throws IOException {
//		byte[] inputByte = new byte[inStream.available()];
//		Deflater compresser = new Deflater();
//		compresser.setInput(inputByte, 0, inputByte.length);
//		byte[] result = new byte[inputByte.length];
//		while (!compresser.finished()) {
//			compresser.deflate(result);
//		}
//		compresser.end();
//		outStream.write(result, 0, result.length);
//		return outStream;
//	}

	/**
	 * Descriptor： 压缩直字节数组
	 * @param inputByte
	 * @return byte[]
	 */
	public byte[] deflater(byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Deflater compresser = new Deflater();
		compresser.setInput(inputByte);
		compresser.finish();
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.deflate(result);
				o.write(result, 0, compressedDataLength);
			}
		} finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}

}
