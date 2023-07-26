package com.example.printer_controller_pr3

import android.app.Activity
import android.content.res.AssetManager
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import com.honeywell.mobility.print.*
import com.honeywell.mobility.print.Printer.ExtraSettings
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** PrinterControllerPr3Plugin */
class PrinterControllerPr3Plugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context : Context
  private lateinit var activity: Activity
  var lp: LinePrinter? = null
  private var jsonCmdAttribStr: String? = null
  private val TAG = "MyActivity"
  var sResult: String? = null
  var wResult: String? = null
  var nResult: String? = null
  var cResult: String? = null
  var wgResult: String? = null
  var sbResult: String? = null
  var scResult: String? = null
  var siResult: String? = null
  var sdwResult: String? = null
  var sdhResult: String? = null
  var journeyNo: String? = null
  var boardingStation: String? = null
  var landingStation: String? = null
  var passengerId: String? = null
  var passengerName: String? = null
  var penaltyDate: String? = null
  var penaltyType: String? = null
  var penaltyAmount: String? = null
  var description: String? = null
  var issuedBy: String? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "IntermecPrint")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${Build.VERSION.RELEASE}")
    }
    else if(call.method == "printPenaltyTicket"){
      val printerID: String? = call.argument<String>("printerID")
      val printerUri: String? = call.argument<String>("printerUri")
      journeyNo = call.argument<String>("journeyNo")
      boardingStation = call.argument<String>("boardingStation")
      landingStation = call.argument<String>("landingStation")
      passengerId = call.argument<String>("passengerId")
      passengerName = call.argument<String>("passengerName")
      penaltyDate = call.argument<String>("penaltyDate")
      penaltyType = call.argument<String>("penaltyType")
      penaltyAmount = call.argument<String>("penaltyAmount")
      description = call.argument<String>("description")
      issuedBy = call.argument<String>("issuedBy")

      PrintPenaltyTicketTask().execute(printerID,printerUri)
    }
    else if (call.method == "CreatePrinter"){
      val printerID: String? = call.argument<String>("PrinterID")
      val printerUri: String? = call.argument<String>("PrinterUri")

      if (printerUri != null && printerID != null) {
        //try {
          PrinterTask().execute(printerID,printerUri)
          //result.success(res)
          //} catch (e: Exception){
          //return result.error(e.toString(), e.message, e.localizedMessage)
          // }
        }
    }
    else if (call.method == "isConnected"){
      return result.success(lp != null)
    }

    else if (call.method == "Write"){
      val index: String? = call.argument<String>("index")
      if (index != null) {
        WriteTask().execute(index)
      }
    }

    else if (call.method == "NewLine"){
      val lineSpace: Int? = call.argument<Int>("lineSpace")
      if (lineSpace != null) {
        NewLineTask().execute(lineSpace)
      }
    }

    else if (call.method == "WriteGraphicBase64"){
      val aBase64Image: String? = call.argument<String>("aBase64Image")
      val aRotation: Int? = call.argument<Int>("aRotation")
      val aXOffset: Int? = call.argument<Int>("aXOffset")
      val aWidth: Int? = call.argument<Int>("aWidth")
      val aHeight: Int? = call.argument<Int>("aHeight")

      if (aBase64Image != null && aXOffset != null && aWidth != null && aHeight != null) {
        WriteGraphicBase64Task().execute(
          aBase64Image, aRotation.toString(), aXOffset.toString(), aWidth.toString(), aHeight.toString()
        )
      }
    }

    else if (call.method == "Disconnect"){

      DisconnectTask().execute()

    }
    else if (call.method == "setBold"){
      val isTrue: Boolean? = call.argument<Boolean>("isTrue")

      SetBoldTask().execute(isTrue)

    }
    else if (call.method == "setCompress"){
      val isTrue: Boolean? = call.argument<Boolean>("isTrue")

      SetCompressTask().execute(isTrue)

    }
    else if (call.method == "setItalic"){
      val isTrue: Boolean? = call.argument<Boolean>("isTrue")

      setItalicTask().execute(isTrue)

    }
    else if (call.method == "setUnderline"){
      val isTrue: Boolean? = call.argument<Boolean>("isTrue")

      setUnderlineTask().execute(isTrue)

    }
    else if (call.method == "setStrikeout"){
      val isTrue: Boolean? = call.argument<Boolean>("isTrue")

      setStrikeoutTask().execute(isTrue)

    }
    else if (call.method == "setDoubleHigh"){
      val isTrue: Boolean? = call.argument<Boolean>("isTrue")

      setDoubleHighTask().execute(isTrue)

    }
    else if (call.method == "setDoubleWide"){
      val isTrue: Boolean? = call.argument<Boolean>("isTrue")

      setDoubleWideTask().execute(isTrue)

    }
    else if (call.method == "Close"){

      CloseTask().execute()

    }
  }

  class BadPrinterStateException(message: String?) : Exception(message) {
    companion object {
      const val serialVersionUID: Long = 1
    }
  }

  inner class PrintPenaltyTicketTask : AsyncTask<String, Int, String>(){

    override fun doInBackground(vararg args: String): String? {
      val printerID = args[0]
      var printerUri = args[1]

      readAssetFiles()

      if ((!printerUri!!.contains(":")) && printerUri.length == 12) {
        val addressChars = CharArray(17)
        var i = 0
        var j = 0
        while (i < 12) {
          printerUri.toCharArray(addressChars, j, i, i + 2)
          j += 2
          if (j < 17) {
            addressChars[j++] = ':'
          }
          i += 2
        }
        printerUri = String(addressChars)
      }
      val blPrinterUri = "bt://$printerUri"

      val exSettings = LinePrinter.ExtraSettings()
      exSettings.setContext(context)

      val progressListener: PrintProgressListener = object : PrintProgressListener {
        override fun receivedStatus(aEvent: PrintProgressEvent) {
          // Publishes updates on the UI thread.
          Log.d(TAG, aEvent.getMessageType().toString())
        }
      }
      Log.d(TAG, blPrinterUri)
      Log.d(TAG, printerID)
      try {
        mLinePrinter = LinePrinter(
          jsonCmdAttribStr,
          printerID,
          blPrinterUri,
          exSettings
        )

        // Registers to listen for the print progress events.
        mLinePrinter?.addPrintProgressListener(progressListener)


        //A retry sequence in case the bluetooth socket is temporarily not ready
        var numtries = 0
        val maxretry = 2
        while (numtries < maxretry) {
          try {
            mLinePrinter?.connect() // Connects to the printer
            break
          } catch (ex: LinePrinterException) {
            numtries++
            Thread.sleep(1000)
          }
        }
        if (numtries == maxretry) mLinePrinter?.connect() //Final retry

        // Check the state of the printer and abort printing if there are
        // any critical errors detected.
        val results: IntArray? = mLinePrinter?.status
        if (results != null) {
          for (err in results.indices) {
            if (results[err] == 223) {
              // Paper out.
              throw BadPrinterStateException("Paper out")
            } else if (results[err] == 227) {
              // Lid open.
              throw BadPrinterStateException("Printer lid open")
            }
          }
        }
        printTicket()
        sResult = "Number of bytes sent to printer: " + mLinePrinter?.bytesWritten

      } catch (ex: Exception) {
        if (mLinePrinter != null) {
          Log.d(TAG, ex.message!!)
          mLinePrinter?.removePrintProgressListener(progressListener) // Stop listening for printer events.
        }

        sResult = "Unexpected exception: " + ex.message
        Log.d(TAG, sResult!!)
        Log.d(TAG, ex.stackTraceToString())

      } finally {
        try {
          if (mLinePrinter != null)
            Log.d(TAG, "Connection Succesful!")
          //lp?.disconnect()
        } catch (e: PrinterException) {
          e.printStackTrace()
          Log.e(TAG, " Error! LP object is null")
        }
      }
      return sResult
    }

  }

  private fun printTicket(){
    try {
      mLinePrinter?.newLine(4)

      mLinePrinter?.writeGraphicBase64(
        // TRC Logo base64
        Constants.TRC_LOGO,
        LinePrinter.GraphicRotationDegrees.DEGREE_0,
        180, // Offset in printhead dots from the left of the page
        220, // Desired graphic width on paper in printhead dots
        70 // Desired graphic height on paper in printhead dots
      )
      mLinePrinter?.newLine(1)
      mLinePrinter?.write("                 SHIRIKA LA TELI TANZANIA         ")
      mLinePrinter?.newLine(2)
      mLinePrinter?.setBold(true)

      mLinePrinter?.write("Journey No : $journeyNo")
      mLinePrinter?.newLine(2)

      mLinePrinter?.write("Journey Name : $boardingStation - $landingStation")
      mLinePrinter?.newLine(2)

      mLinePrinter?.write("PassengerID : $passengerId")
      mLinePrinter?.newLine(2)

      mLinePrinter?.write("PassengerName : $passengerName")
      mLinePrinter?.newLine(2)

      mLinePrinter?.write("PenaltyDate: $penaltyDate")
      mLinePrinter?.newLine(2)

      mLinePrinter?.write("PenaltyType: $penaltyType")
      mLinePrinter?.newLine(2)

      mLinePrinter?.write("PenaltyAmount: $penaltyAmount")
      mLinePrinter?.newLine(2)

      mLinePrinter?.write("Description: $description")
      mLinePrinter?.newLine(2)

      mLinePrinter?.write("Issued By: $issuedBy")
      mLinePrinter?.newLine(3)
      mLinePrinter?.newLine(4)
      mLinePrinter?.disconnect()

    } catch (ex: Exception) {
      Log.v("TAG", "${ex.message}")
    }
  }

  inner class CloseTask : AsyncTask<Int, Int, String>() {

    override fun doInBackground(vararg args: Int?): String? {
      try{
        lp?.close()
      }catch (ex: Exception){
        cResult = "Unexpected exception: " + ex.message
      }
      return cResult
    }
  }

  inner class SetBoldTask : AsyncTask<Boolean, Int, String>() {

    override fun doInBackground(vararg args: Boolean?): String? {
      val isTrue = args[0]
      try{
        lp?.setBold(isTrue!!)
      }catch (ex: Exception){
        sbResult = "Unexpected exception: " + ex.message
      }
      return sbResult
    }
  }
  inner class SetCompressTask : AsyncTask<Boolean, Int, String>() {

    override fun doInBackground(vararg args: Boolean?): String? {
      val isTrue = args[0]
      try{
        lp?.setCompress(isTrue!!)
      }catch (ex: Exception){
        scResult = "Unexpected exception: " + ex.message
      }
      return scResult
    }
  }
  inner class setItalicTask : AsyncTask<Boolean, Int, String>() {

    override fun doInBackground(vararg args: Boolean?): String? {
      val isTrue = args[0]
      try{
        lp?.setItalic(isTrue!!)
      }catch (ex: Exception){
        siResult = "Unexpected exception: " + ex.message
      }
      return siResult
    }
  }
  inner class setUnderlineTask : AsyncTask<Boolean, Int, String>() {

    override fun doInBackground(vararg args: Boolean?): String? {
      val isTrue = args[0]
      try{
        lp?.setUnderline(isTrue!!)
      }catch (ex: Exception){
        siResult = "Unexpected exception: " + ex.message
      }
      return siResult
    }
  }
  inner class setStrikeoutTask : AsyncTask<Boolean, Int, String>() {

    override fun doInBackground(vararg args: Boolean?): String? {
      val isTrue = args[0]
      try{
        lp?.setStrikeout(isTrue!!)
      }catch (ex: Exception){
        siResult = "Unexpected exception: " + ex.message
      }
      return siResult
    }
  }
  inner class setDoubleHighTask : AsyncTask<Boolean, Int, String>() {

    override fun doInBackground(vararg args: Boolean?): String? {
      val isTrue = args[0]
      try{
        lp?.setDoubleHigh(isTrue!!)
      }catch (ex: Exception){
        sdhResult = "Unexpected exception: " + ex.message
      }
      return sdhResult
    }
  }
  inner class setDoubleWideTask : AsyncTask<Boolean, Int, String>() {

    override fun doInBackground(vararg args: Boolean?): String? {
      val isTrue = args[0]
      try{
        lp?.setDoubleWide(isTrue!!)
      }catch (ex: Exception){
        sdwResult = "Unexpected exception: " + ex.message
      }
      return sdwResult
    }
  }

  inner class DisconnectTask : AsyncTask<Int, Int, String>() {

    override fun doInBackground(vararg args: Int?): String? {
      try{
        Log.d(TAG,"${lp?.bytesWritten.toString()} bytes has written." )
        lp?.disconnect()
      }catch (ex: Exception){
        nResult = "Unexpected exception: " + ex.message
      }
      return nResult
    }
  }

  inner class WriteGraphicBase64Task : AsyncTask<String, Int, String>() {
    override fun doInBackground(vararg args: String?): String? {
      val aBase64Image = args[0]
      val aRotation = args[1]
      val aXOffset = args[2]
      val aWidth = args[3]
      val aHeight = args[4]

      try{
        lp?.newLine(1)
        lp?.writeGraphicBase64(aBase64Image, aRotation!!.toInt(), aXOffset!!.toInt(), aWidth!!.toInt(), aHeight!!.toInt())
        lp?.newLine(1)
      }catch (ex: Exception){
        wgResult = "Unexpected exception: " + ex.message
        Log.d(TAG, wgResult!!)
      }
      return wgResult
    }
  }

  inner class NewLineTask : AsyncTask<Int, Int, String>() {

    override fun doInBackground(vararg args: Int?): String? {
      val index = args[0]
      try{
        lp?.newLine(index!!)
      }catch (ex: Exception){
        nResult = "Unexpected exception: " + ex.message
      }
      return nResult
    }
  }

  inner class WriteTask : AsyncTask<String, Int, String>() {
    override fun doInBackground(vararg args: String?): String? {
      val index = args[0]
      try{
        lp?.write(index)
//                lp?.newLine(1)
      }catch (ex: Exception){
        wResult = "Unexpected exception: " + ex.message
      }
      return wResult
    }
  }

  inner class PrinterTask : AsyncTask<String, Int, String>() {

    private val PROGRESS_CANCEL_MSG = "Printing cancelled\n"
    private val PROGRESS_COMPLETE_MSG = "Printing completed\n"
    private val PROGRESS_END_DOC_MSG = "End of document\n"
    private val PROGRESS_FINISHED_MSG = "Printer connection closed\n"
    private val PROGRESS_NONE_MSG = "Unknown progress message\n"
    private val PROGRESS_START_DOC_MSG = "Start printing document\n"

    override fun doInBackground(vararg args: String): String? {
      val PrinterID = args[0]
      var PrinterUri = args[1]

      readAssetFiles()

      if ((!PrinterUri!!.contains(":")) && PrinterUri.length == 12) {
        val addressChars = CharArray(17)
        var i = 0
        var j = 0
        while (i < 12) {
          PrinterUri.toCharArray(addressChars, j, i, i + 2)
          j += 2
          if (j < 17) {
            addressChars[j++] = ':'
          }
          i += 2
        }
        PrinterUri = String(addressChars)
      }
      val blPrinterUri = "bt://$PrinterUri"

      val exSettings = LinePrinter.ExtraSettings()
      exSettings.setContext(context)

      val progressListener: PrintProgressListener = object : PrintProgressListener {
        override fun receivedStatus(aEvent: PrintProgressEvent) {
          // Publishes updates on the UI thread.
          Log.d(TAG, aEvent.getMessageType().toString())
        }
      }
      Log.d(TAG, blPrinterUri)
      Log.d(TAG, PrinterID)
      try {
        lp = LinePrinter(
          jsonCmdAttribStr,
          PrinterID,
          blPrinterUri,
          exSettings
        )

        // Registers to listen for the print progress events.
        lp?.addPrintProgressListener(progressListener)


        //A retry sequence in case the bluetooth socket is temporarily not ready
        var numtries = 0
        val maxretry = 2
        while (numtries < maxretry) {
          try {
            lp?.connect() // Connects to the printer
            break
          } catch (ex: LinePrinterException) {
            numtries++
            Thread.sleep(1000)
          }
        }
        if (numtries == maxretry) lp?.connect() //Final retry

        // Check the state of the printer and abort printing if there are
        // any critical errors detected.
        val results: IntArray? = lp?.status
        if (results != null) {
          for (err in results.indices) {
            if (results[err] == 223) {
              // Paper out.
              throw BadPrinterStateException("Paper out")
            } else if (results[err] == 227) {
              // Lid open.
              throw BadPrinterStateException("Printer lid open")
            }
          }
        }
      } catch (ex: Exception) {
        if (lp != null) {
          Log.d(TAG, ex.message!!)
         //lp?.removePrintProgressListener(progressListener) // Stop listening for printer events.
        }

        sResult = "Unexpected exception: " + ex.message
        Log.d(TAG, sResult!!)
        Log.d(TAG, ex.stackTraceToString())

      } finally {
        try {
          if (lp != null)
            Log.d(TAG, "Connection Succesful!")
          //lp?.disconnect()
        } catch (e: PrinterException) {
          e.printStackTrace()
          Log.e(TAG, " Error! LP object is null")
        }
      }
      return sResult
    }
    override fun onPostExecute(result: String?) {
      super.onPostExecute(result)

      if (result != null) {
        Log.e(TAG,result)
      }

    }
    override fun onProgressUpdate(vararg values: Int?) {
      super.onProgressUpdate(*values)
      val progress = values[0]

      when (progress) {
        PrintProgressEvent.MessageTypes.CANCEL -> Log.d(TAG,PROGRESS_CANCEL_MSG)
        PrintProgressEvent.MessageTypes.COMPLETE -> Log.d(TAG,PROGRESS_COMPLETE_MSG)
        PrintProgressEvent.MessageTypes.ENDDOC -> Log.d(TAG,PROGRESS_END_DOC_MSG)
        PrintProgressEvent.MessageTypes.FINISHED -> Log.d(TAG,PROGRESS_FINISHED_MSG)
        PrintProgressEvent.MessageTypes.STARTDOC -> Log.d(TAG,PROGRESS_START_DOC_MSG)
        else -> Log.d(TAG,PROGRESS_NONE_MSG)
      }
    }
  }

  private fun readAssetFiles() {
    var input: InputStream? = null
    var output: ByteArrayOutputStream? = null
    val assetManager: AssetManager = context.assets
    val files = arrayOf("printer_profiles.JSON", "honeywell_logo.bmp")
    var fileIndex = 0
    var initialBufferSize: Int
    try {
      for (filename in files) {
        input = assetManager.open(filename)
        initialBufferSize = if (fileIndex == 0) 8000 else 2500
        output = ByteArrayOutputStream(initialBufferSize)
        val buf = ByteArray(1024)
        var len: Int
        while (input.read(buf).also { len = it } > 0) {
          output.write(buf, 0, len)
        }
        input.close()
        input = null
        output.flush()
        output.close()
        when (fileIndex) {
          0 -> jsonCmdAttribStr = output.toString()
        }
        fileIndex++
        output = null
      }
    } catch (ex: Exception) {
      Log.w(TAG,"ERROR while reading asset file")
    } finally {
      try {
        if (input != null) {
          input.close()
          input = null
        }
        if (output != null) {
          output.close()
          output = null
        }
      } catch (e: IOException) {
      }
    }
  }
  override fun onDetachedFromActivity() {
    TODO("Not yet implemented")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    TODO("Not yet implemented")
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity;
  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
