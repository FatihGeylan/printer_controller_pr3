import 'printer_controller_pr3_platform_interface.dart';

class PrinterControllerPr3 {
  Future<String?> getPlatformVersion() {
    return PrinterControllerPr3Platform.instance.getPlatformVersion();
  }

  //Example: createPrinter(printerName: "PrinterName", printerAddress: "00:11:22:33:44:55");
  Future<dynamic> createPrinter({required String printerName, required String printerAddress}) {
    return PrinterControllerPr3Platform.instance.createPrinter(printerName, printerAddress);
  }

  Future<dynamic> write(String index) {
    return PrinterControllerPr3Platform.instance.write(index);
  }

  Future<dynamic> newLine(int lineSpace) {
    return PrinterControllerPr3Platform.instance.newLine(lineSpace);
  }

  // PRINT IMAGE FUNCTION
  //BASE64 CODE OF THE IMAGE
  // ROTATION DEGREE MUST BE 0,90,180 OR 270
  // OFFSET FROM THE LEFT HAND SIDE OF THE PAGE
  // GRAPHIC WIDTH
  // GRAPHIC HEIGHT
  Future<dynamic> writeGraphicBase64(String aBase64Image, int aRotation, int aXOffset, int aWidth, int aHeight) {
    return PrinterControllerPr3Platform.instance.writeGraphicBase64(aBase64Image, aRotation, aXOffset, aWidth, aHeight);
  }

  Future<dynamic> printPenaltyTicket({
    required String printerName,
    required String printerAddress,
    required String journeyNo,
    required String stationsInfo,
    required String passengerId,
    required String passengerName,
    required String penaltyDate,
    required String penaltyType,
    required String penaltyAmount,
    required String description,
    required String issuedBy,
  }) {
    return PrinterControllerPr3Platform.instance.printPenaltyTicket(
      printerName: printerName,
      printerAddress: printerAddress,
      journeyNo: journeyNo,
      stationsInfo: stationsInfo,
      passengerId: passengerId,
      passengerName: passengerName,
      penaltyDate: penaltyDate,
      penaltyType: penaltyType,
      penaltyAmount: penaltyAmount,
      description: description,
      issuedBy: issuedBy,
    );
  }

  Future<dynamic> printCitReport({
    required String printerName,
    required String printerAddress,
    required String shiftOpenDate,
    required String shiftCloseDate,
    required String totalPenaltyAmount,
    required String staffInfo,
    required String gepgNumber,
    required String billExpireDate,
    required String signature,
    required String citReportTitle,
  }) {
    return PrinterControllerPr3Platform.instance.printCitReport(
      printerName: printerName,
      printerAddress: printerAddress,
      shiftOpenDate: shiftOpenDate,
      shiftCloseDate: shiftCloseDate,
      totalPenaltyAmount: totalPenaltyAmount,
      staffInfo: staffInfo,
      gepgNumber: gepgNumber,
      billExpireDate: billExpireDate,
      signature: signature,
      citReportTitle: citReportTitle,
    );
  }

  Future<dynamic> setBold(bool isTrue) {
    return PrinterControllerPr3Platform.instance.setBold(isTrue);
  }

  Future<dynamic> setCompress(bool isTrue) {
    return PrinterControllerPr3Platform.instance.setCompress(isTrue);
  }

  Future<dynamic> setItalic(bool isTrue) {
    return PrinterControllerPr3Platform.instance.setItalic(isTrue);
  }

  Future<dynamic> setDoubleHigh(bool isTrue) {
    return PrinterControllerPr3Platform.instance.setDoubleHigh(isTrue);
  }

  Future<dynamic> setUnderline(bool isTrue) {
    return PrinterControllerPr3Platform.instance.setUnderline(isTrue);
  }

  Future<dynamic> setStrikeout(bool isTrue) {
    return PrinterControllerPr3Platform.instance.setStrikeout(isTrue);
  }

  Future<dynamic> setDoubleWide(bool isTrue) {
    return PrinterControllerPr3Platform.instance.setDoubleWide(isTrue);
  }

  Future<dynamic> disconnect() {
    return PrinterControllerPr3Platform.instance.disconnect();
  }

  Future<dynamic> isConnected() {
    return PrinterControllerPr3Platform.instance.isConnected();
  }

  Future<dynamic> close() {
    return PrinterControllerPr3Platform.instance.close();
  }
}
