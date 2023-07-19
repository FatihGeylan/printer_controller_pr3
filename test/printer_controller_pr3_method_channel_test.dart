import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:printer_controller_pr3/printer_controller_pr3_method_channel.dart';

void main() {
  MethodChannelPrinterControllerPr3 platform = MethodChannelPrinterControllerPr3();
  const MethodChannel channel = MethodChannel('printer_controller_pr3');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
