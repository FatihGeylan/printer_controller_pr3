import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'printer_controller_pr3_platform_interface.dart';

/// An implementation of [PrinterControllerPr3Platform] that uses method channels.
class MethodChannelPrinterControllerPr3 extends PrinterControllerPr3Platform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('IntermecPrint');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future createPrinter(String PrinterID, String PrinterUri) async {
    return await methodChannel.invokeMethod('CreatePrinter', {
      "PrinterID": PrinterID,
      "PrinterUri": PrinterUri,
    });
  }

  @override
  Future newLine(int lineSpace) async {
    return await methodChannel.invokeMethod('NewLine', {
      "lineSpace": lineSpace,
    });
  }

  @override
  Future write(String index) async {
    return await methodChannel.invokeMethod('Write', {
      "index": index,
    });
  }

  @override
  Future writeGraphicBase64(String aBase64Image, int aRotation, int aXOffset, int aWidth, int aHeight) async {
    return await methodChannel.invokeMethod('WriteGraphicBase64', {
      "aBase64Image": aBase64Image,
      "aRotation": aRotation,
      "aXOffset": aXOffset,
      "aWidth": aWidth,
      "aHeight": aHeight,
    });
  }

  @override
  Future writeTicket(String ticketDesign) async {
    return await methodChannel.invokeMethod('WriteTicket', {
      "ticketDesign": ticketDesign,
    });
  }

  @override
  Future close() async {
    return await methodChannel.invokeMethod('Close');
  }

  @override
  Future disconnect() async {
    return await methodChannel.invokeMethod('Disconnect');
  }

  @override
  Future setBold(bool isTrue) async {
    return await methodChannel.invokeMethod('setBold', {
      "isTrue": isTrue,
    });
  }

  @override
  Future setCompress(bool isTrue) async {
    return await methodChannel.invokeMethod('setCompress', {
      "isTrue": isTrue,
    });
  }

  @override
  Future setDoubleHigh(bool isTrue) async {
    return await methodChannel.invokeMethod('setDoubleHigh', {
      "isTrue": isTrue,
    });
  }

  @override
  Future setDoubleWide(bool isTrue) async {
    return await methodChannel.invokeMethod('setDoubleWide', {
      "isTrue": isTrue,
    });
  }

  @override
  Future setItalic(bool isTrue) async {
    return await methodChannel.invokeMethod('setItalic', {
      "isTrue": isTrue,
    });
  }

  @override
  Future setStrikeout(bool isTrue) async {
    return await methodChannel.invokeMethod('setStrikeout', {
      "isTrue": isTrue,
    });
  }

  @override
  Future setUnderline(bool isTrue) async {
    return await methodChannel.invokeMethod('setUnderline', {
      "isTrue": isTrue,
    });
  }
}
