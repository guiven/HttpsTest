/**
 * IInflaterProc.java - 2010-9-16
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
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * 使用流行的 ZLIB 压缩程序库为通用解压缩提供支持 
 * @author tobba
 * 
 */
public class InflaterProc{

	/**
	 * Description: 解压缩
	 * @see api.commcommponent.msgprocess.IInflaterProc#inflater()
	 */
	static final int BUFFER = 128;

//	public InputStream inflater(InputStream inputStream,Logger log) throws IOException {
//		int compressedDataLength = 0;
//		byte[] inputByte = new byte[inputStream.available()];
//		int read_len = inputStream.read(inputByte, 0, inputStream.available());
//		if (read_len != inputStream.available()) {
//			throw new IOException("Inflacter read input stream error!\n");
//		} else {
//			Inflater decompresser = new Inflater();
//			decompresser.setInput(inputByte, 0, inputByte.length);
//			byte[] result = new byte[inputByte.length * 10];
//			try {
//				compressedDataLength = decompresser.inflate(result);
//				decompresser.end();
//			} catch (DataFormatException ex) {
//				System.err.println("Data format errlr!\n");
//				ex.printStackTrace();
//			}
//			if (compressedDataLength > 0) {
//				return new ByteArrayInputStream(result, 0, result.length);
//			} else {
//				throw new IOException("Inflater decompress data error!\n");
//			}
//		}
//	}
	/**
	 * Description: 解压缩
	 */
	public byte[] inflater(byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Inflater compresser = new Inflater(false);
		compresser.setInput(inputByte, 0, inputByte.length);
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.inflate(result);
				if (compressedDataLength == 0) {
					break;
				}
				o.write(result, 0, compressedDataLength);
			}
		} catch (DataFormatException e) {
			throw new IOException("decompress catch data format exception",e);
		}  finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}

//	/**
//	 * Description:  读取压缩文件的内容，解压缩zip文件
//	 * @param inputByte
//	 * @return
//	 * @throws IOException
//	 */
//	public byte[] UnZip(byte[] inputByte) throws IOException {
//
//		ByteArrayOutputStream dest = null;
//		ByteArrayInputStream bis = new ByteArrayInputStream(inputByte);
//		ZipInputStream zis = new ZipInputStream(bis);
//		System.out.println("zis available:" + zis.available());
//		zis.getNextEntry();
//		int count;
//		byte data[] = new byte[BUFFER];
//		dest = new ByteArrayOutputStream();
//		try {
//			while ((count = zis.read(data, 0, BUFFER)) != -1) {
//				dest.write(data, 0, count);
//			}
//		} catch (Exception ex) {
//			System.err.println("Data format error!\n");
//			ex.printStackTrace();
//		} finally {
//			dest.close();
//		}
//		zis.close();
//		System.out.println("data:\t" + dest.toString());
//		return dest.toByteArray();
//
//	}

}
