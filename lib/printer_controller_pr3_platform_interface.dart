import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'printer_controller_pr3_method_channel.dart';

abstract class PrinterControllerPr3Platform extends PlatformInterface {
  /// Constructs a PrinterControllerPr3Platform.
  PrinterControllerPr3Platform() : super(token: _token);

  static final Object _token = Object();

  static PrinterControllerPr3Platform _instance = MethodChannelPrinterControllerPr3();

  /// The default instance of [PrinterControllerPr3Platform] to use.
  ///
  /// Defaults to [MethodChannelPrinterControllerPr3].
  static PrinterControllerPr3Platform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [PrinterControllerPr3Platform] when
  /// they register themselves.
  static set instance(PrinterControllerPr3Platform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<dynamic> createPrinter(String PrinterID, String PrinterUri) =>
      throw UnimplementedError('Has not been implemented.');

  Future<dynamic> write(String index) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> newLine(int lineSpace) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> writeGraphicBase64(String aBase64Image, int aRotation, int aXOffset, int aWidth, int aHeight) =>
      throw UnimplementedError('Has not been implemented.');

  Future<dynamic> writeTicket(String ticketDesign) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> setBold(bool isTrue) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> setCompress(bool isTrue) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> setItalic(bool isTrue) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> setDoubleHigh(bool isTrue) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> setUnderline(bool isTrue) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> setStrikeout(bool isTrue) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> setDoubleWide(bool isTrue) => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> disconnect() => throw UnimplementedError('Has not been implemented.');

  Future<dynamic> close() => throw UnimplementedError('Has not been implemented.');
}
