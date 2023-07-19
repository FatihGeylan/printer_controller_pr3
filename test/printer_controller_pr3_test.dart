import 'package:flutter_test/flutter_test.dart';
import 'package:printer_controller_pr3/printer_controller_pr3.dart';
import 'package:printer_controller_pr3/printer_controller_pr3_platform_interface.dart';
import 'package:printer_controller_pr3/printer_controller_pr3_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockPrinterControllerPr3Platform
    with MockPlatformInterfaceMixin
    implements PrinterControllerPr3Platform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final PrinterControllerPr3Platform initialPlatform = PrinterControllerPr3Platform.instance;

  test('$MethodChannelPrinterControllerPr3 is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelPrinterControllerPr3>());
  });

  test('getPlatformVersion', () async {
    PrinterControllerPr3 printerControllerPr3Plugin = PrinterControllerPr3();
    MockPrinterControllerPr3Platform fakePlatform = MockPrinterControllerPr3Platform();
    PrinterControllerPr3Platform.instance = fakePlatform;

    expect(await printerControllerPr3Plugin.getPlatformVersion(), '42');
  });
}
