import 'package:flutter/material.dart';

import 'package:printer_controller_pr3/printer_controller_pr3.dart';
import 'package:printer_controller_pr3_example/constants.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Printer Controller Example',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Printer Controller Example'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final TextEditingController _writeController = TextEditingController();

  final _printerControllerPr3Plugin = PrinterControllerPr3();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: SingleChildScrollView(
          child: SizedBox(
            width: MediaQuery.of(context).size.width,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                const _Divider(),
                SizedBox(
                  height: MediaQuery.of(context).size.height * 0.01,
                ),
                const _Divider(),
                Container(
                  height: MediaQuery.of(context).size.height / 9,
                  width: MediaQuery.of(context).size.width,
                  color: Colors.lightBlueAccent.shade100,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text("Match device on bluetooth settings then connect"),
                      ElevatedButton(
                        onPressed: () async {
                          final a = await _printerControllerPr3Plugin.createPrinter(
                            printerName: Constants.deviceName,
                            printerAddress: Constants.deviceAddress,
                          );
                          print('THİS İS RESULT -- $a');
                        },
                        child: const Text('Connect'),
                      ),
                    ],
                  ),
                ),
                const SizedBox(
                  height: 10,
                ),
                const _Divider(),
                ElevatedButton(
                    onPressed: () {
                      //CreatePrinter(Constants.deviceName, "bt://00:1D:DF:58:0D:3D");
                      _printerControllerPr3Plugin.newLine(4);
                      _printerControllerPr3Plugin.writeGraphicBase64(Constants.graphic64Code, 0, 180, 220, 70);
                      _printerControllerPr3Plugin.newLine(1);
                      _printerControllerPr3Plugin.write("                 Train Ticket         ");
                      _printerControllerPr3Plugin.newLine(2);
                      _printerControllerPr3Plugin.setBold(true);

                      _printerControllerPr3Plugin.write("Trip No : TrainID");
                      _printerControllerPr3Plugin.newLine(2);

                      _printerControllerPr3Plugin.write("Journey Name : Station name - Landing station name");
                      _printerControllerPr3Plugin.newLine(2);

                      _printerControllerPr3Plugin.write("PassengerID : PenaltyID");
                      _printerControllerPr3Plugin.newLine(2);

                      _printerControllerPr3Plugin.write("PassengerName : Username");
                      _printerControllerPr3Plugin.newLine(2);

                      _printerControllerPr3Plugin.write("PenaltyDate: Time-TrainID");
                      _printerControllerPr3Plugin.newLine(2);

                      _printerControllerPr3Plugin.write("PenaltyType: PenaltyReason");
                      _printerControllerPr3Plugin.newLine(2);

                      _printerControllerPr3Plugin.write("PenaltyAmount: X TL");
                      _printerControllerPr3Plugin.newLine(2);

                      _printerControllerPr3Plugin.write("Description: Penalty Description");
                      _printerControllerPr3Plugin.newLine(2);

                      _printerControllerPr3Plugin.write("Issued By: " "IssuedByXXX");
                      _printerControllerPr3Plugin.newLine(3);
                      _printerControllerPr3Plugin.newLine(4);
                    },
                    child: const Text("Print Ticket")),
                const _Divider(),
                ElevatedButton(
                    onPressed: () {
                      _printerControllerPr3Plugin.newLine(3);
                    },
                    child: const Text("Add New Line")),
                const _Divider(),
                ElevatedButton(
                    onPressed: () {
                      _printerControllerPr3Plugin.writeGraphicBase64(
                          // PRINT IMAGE FUNCTION
                          Constants.graphic64Code, //BASE64 CODE OF THE IMAGE
                          0, // ROTATION DEGREE MUST BE 0,90,180 OR 270
                          180, // OFFSET FROM THE LEFT HAND SIDE OF THE PAGE
                          220, // GRAPHIC WIDTH
                          250 // GRAPHIC HEIGHT
                          );
                    },
                    child: const Text("Write Graphic Base 64")),
                const _Divider(),
                const SizedBox(
                  height: 10,
                ),
                Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 15),
                      width: MediaQuery.of(context).size.width * 0.80,
                      child: TextFormField(
                        controller: _writeController,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Please enter text';
                          }
                          return null;
                        },
                        onChanged: (String text) {},
                        decoration: const InputDecoration(
                          hintText: 'Enter text to print..',
                        ),
                      ),
                    ),
                    ElevatedButton(
                        onPressed: () {
                          _printerControllerPr3Plugin.write(_writeController.text);
                          _printerControllerPr3Plugin.newLine(2);
                        },
                        child: const Text("Write")),
                  ],
                ),
                const SizedBox(height: 10),
                const _Divider(),
                SizedBox(
                  width: MediaQuery.of(context).size.width,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Expanded(flex: 1, child: Center(child: Text("Set Bold"))),
                      Expanded(
                        flex: 1,
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: [
                            ElevatedButton(
                                onPressed: () {
                                  _printerControllerPr3Plugin.setBold(true);
                                },
                                child: const Text("Yes")),
                            ElevatedButton(
                                onPressed: () {
                                  _printerControllerPr3Plugin.setBold(false);
                                },
                                child: const Text("No")),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
                const _Divider(),
                SizedBox(
                  width: MediaQuery.of(context).size.width,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Expanded(flex: 1, child: Center(child: Text("Set Compress"))),
                      Expanded(
                        flex: 1,
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: [
                            ElevatedButton(
                                onPressed: () {
                                  _printerControllerPr3Plugin.setCompress(true);
                                },
                                child: const Text("Yes")),
                            ElevatedButton(
                                onPressed: () {
                                  _printerControllerPr3Plugin.setCompress(false);
                                },
                                child: const Text("No")),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
                const _Divider(),
                const _Divider(),
                SizedBox(
                  width: MediaQuery.of(context).size.width,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Expanded(flex: 1, child: Center(child: Text("Set DoubleHigh"))),
                      Expanded(
                        flex: 1,
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: [
                            ElevatedButton(
                                onPressed: () {
                                  _printerControllerPr3Plugin.setDoubleHigh(true);
                                },
                                child: const Text("Yes")),
                            ElevatedButton(
                                onPressed: () {
                                  _printerControllerPr3Plugin.setDoubleHigh(false);
                                },
                                child: const Text("No")),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
                const _Divider(),
                SizedBox(
                  width: MediaQuery.of(context).size.width,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Expanded(flex: 1, child: Center(child: Text("Set DoubleWide"))),
                      Expanded(
                        flex: 1,
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: [
                            ElevatedButton(
                                onPressed: () {
                                  _printerControllerPr3Plugin.setDoubleWide(true);
                                },
                                child: const Text("Yes")),
                            ElevatedButton(
                                onPressed: () {
                                  _printerControllerPr3Plugin.setDoubleWide(false);
                                },
                                child: const Text("No")),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
                const _Divider(),
                const SizedBox(
                  height: 20,
                ),
                ElevatedButton(
                  onPressed: () => _printerControllerPr3Plugin.disconnect(),
                  child: const Text("Disconnect"),
                ),
                const _Divider(),
                ElevatedButton(
                  onPressed: () => _printerControllerPr3Plugin.close(),
                  child: const Text("Close"),
                ),
                const _Divider(),
                ElevatedButton(
                  onPressed: () async => print(await _printerControllerPr3Plugin.isConnected()),
                  child: const Text("Is Connected?"),
                ),
              ],
            ),
          ),
        ),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}

class _Divider extends StatelessWidget {
  const _Divider();

  @override
  Widget build(BuildContext context) {
    return const Divider(
      thickness: 3,
      indent: 10,
      endIndent: 10,
    );
  }
}
