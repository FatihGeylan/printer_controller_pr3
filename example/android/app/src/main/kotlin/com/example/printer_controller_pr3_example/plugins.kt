/*package com.example.printer_controller_pr3

import android.app.Activity
import android.content.res.AssetManager
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import com.example.printer_controller_pr3_example.MainActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import com.honeywell.mobility.print.*
import com.honeywell.mobility.print.Printer.ExtraSettings;
import io.flutter.embedding.android.FlutterActivity

/** PrinterControllerPr3Plugin */
class PrinterControllerPr3Plugin: FlutterPlugin, MethodCallHandler, FlutterActivity() {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel : MethodChannel
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

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "IntermecPrint")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${Build.VERSION.RELEASE}")
        }
        if (call.method == "CreatePrinter"){
            val PrinterID: String? = call.argument<String>("PrinterID")
            val PrinterUri: String? = call.argument<String>("PrinterUri")

            if (PrinterUri != null) {
                if (PrinterID != null) {
                    PrinterTask().execute(PrinterID,PrinterUri)
                }
            }
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
        else if (call.method == "WriteTicket"){
            val ticketDesign: String? = call.argument<String>("ticketDesign")

            WriteTicketTask().execute(ticketDesign)
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
            exSettings.setContext(this)

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
                    lp?.removePrintProgressListener(progressListener) // Stop listening for printer events.
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
    inner class WriteTicketTask : AsyncTask<String, Int, String>() {
        override fun doInBackground(vararg args: String?): String? {
            val ticketDesign = args[0]
            try{
                lp?.newLine(4)

                lp?.writeGraphicBase64(
                    // TRC Logo base64
                    ticketDesign,
                    LinePrinter.GraphicRotationDegrees.DEGREE_0,
                    180, // Offset in printhead dots from the left of the page
                    220, // Desired graphic width on paper in printhead dots
                    70 // Desired graphic height on paper in printhead dots
                )
                lp?.newLine(1)
                lp?.write("                 SHIRIKA LA TELI TANZANIA         ")
                lp?.newLine(2)

                lp?.setBold(true)

                lp?.write("Trip No" + " : " + "TrainID")
                lp?.newLine(2)

                lp?.write("Journey Name" + " : " + "Station name" + " - " + "Landing station name")
                lp?.newLine(2)

                lp?.write("PassengerID" + " : " + "PenaltyID")
                lp?.newLine(2)

                lp?.write("PassengerName" + " : " + "Username")
                lp?.newLine(2)

                lp?.write("PenaltyDate: Time-TrainID")
                lp?.newLine(2)

                lp?.write("PenaltyType: PenaltyReason")
                lp?.newLine(2)

                lp?.write("PenaltyAmount: X TL")
                lp?.newLine(2)

                //val description = if(activity_penalty_dialog_description_edt.text.toString().isEmpty()) "-" else activity_penalty_dialog_description_edt.text.toString()
                lp?.write("Description: Penalty Description" )
                lp?.newLine(2)

                lp?.write("Issued By: " + "IssuedByXXX")
                lp?.newLine(3)
                lp?.newLine(4)

            }catch (ex: Exception){
                wResult = "Unexpected exception: " + ex.message
            }
            return wResult
        }
    }

    private fun readAssetFiles() {
        var input: InputStream? = null
        var output: ByteArrayOutputStream? = null
        val assetManager: AssetManager = assets
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

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
*/