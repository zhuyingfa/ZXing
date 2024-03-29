/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yvan.zxing.lib.core;

import com.yvan.zxing.lib.core.aztec.AztecWriter;
import com.yvan.zxing.lib.core.common.BitMatrix;
import com.yvan.zxing.lib.core.datamatrix.DataMatrixWriter;
import com.yvan.zxing.lib.core.oned.CodaBarWriter;
import com.yvan.zxing.lib.core.oned.Code128Writer;
import com.yvan.zxing.lib.core.oned.Code39Writer;
import com.yvan.zxing.lib.core.oned.Code93Writer;
import com.yvan.zxing.lib.core.oned.EAN13Writer;
import com.yvan.zxing.lib.core.oned.EAN8Writer;
import com.yvan.zxing.lib.core.oned.ITFWriter;
import com.yvan.zxing.lib.core.oned.UPCAWriter;
import com.yvan.zxing.lib.core.oned.UPCEWriter;
import com.yvan.zxing.lib.core.pdf417.PDF417Writer;
import com.yvan.zxing.lib.core.qrcode.QRCodeWriter;

import java.util.Map;

/**
 * This is a factory class which finds the appropriate Writer subclass for the BarcodeFormat
 * requested and encodes the barcode with the supplied contents.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class MultiFormatWriter implements Writer {

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height) throws WriterException {
    return encode(contents, format, width, height, null);
  }

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width, int height,
                          Map<EncodeHintType,?> hints) throws WriterException {

    Writer writer;
    switch (format) {
      case EAN_8:
        writer = new EAN8Writer();
        break;
      case UPC_E:
        writer = new UPCEWriter();
        break;
      case EAN_13:
        writer = new EAN13Writer();
        break;
      case UPC_A:
        writer = new UPCAWriter();
        break;
      case QR_CODE:
        writer = new QRCodeWriter();
        break;
      case CODE_39:
        writer = new Code39Writer();
        break;
      case CODE_93:
        writer = new Code93Writer();
        break;
      case CODE_128:
        writer = new Code128Writer();
        break;
      case ITF:
        writer = new ITFWriter();
        break;
      case PDF_417:
        writer = new PDF417Writer();
        break;
      case CODABAR:
        writer = new CodaBarWriter();
        break;
      case DATA_MATRIX:
        writer = new DataMatrixWriter();
        break;
      case AZTEC:
        writer = new AztecWriter();
        break;
      default:
        throw new IllegalArgumentException("No encoder available for format " + format);
    }
    return writer.encode(contents, format, width, height, hints);
  }

}
