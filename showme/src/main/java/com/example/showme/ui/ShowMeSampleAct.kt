package com.example.showme.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.showme.LogType
import com.example.showme.ShowMe
import com.example.showme.WatcherType
import kotlinx.android.synthetic.main.activity_show_me_sample.*


class ShowMeSampleAct : AppCompatActivity(), AdapterView.OnItemSelectedListener {

  private lateinit var mShowMeProduction : ShowMe // ShowMe("ShowMeSample")
  private lateinit var mShowMeDev : ShowMe // ShowMe("ShowMeSample")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(com.example.showme.R.layout.activity_show_me_sample)

    mShowMeProduction = ShowMe(true, "Sample-Production", LogType.WARNING.type, WatcherType.PUBLIC.type, mShowTimeInterval = false)
    mShowMeDev = ShowMe(true, "Sample-Dev", LogType.DEBUG.type, WatcherType.DEV.type, mShowTimeInterval = true, mWriteLog = true)

    mShowMeDev.injectContext(this)  //because we want to write Log, otherwise you don't need to add this
    mShowMeDev.SHOWME_FOLDER = "FOO"

    mShowMeProduction.mTAGPrefix = ""
    mShowMeDev.mTAGPrefix = ""
    setSpinners()
    setClickListeners()
  }


  private fun setSpinners() {
    val adapterLogCategory = ArrayAdapter.createFromResource(this, com.example.showme.R.array.spinner_log_category,
      android.R.layout.simple_expandable_list_item_1)
    adapterLogCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner_category.adapter = adapterLogCategory
    spinner_category.onItemSelectedListener = this

    val adapterWatcherCategory = ArrayAdapter.createFromResource(this, com.example.showme.R.array.spinner_watcher_category,
      android.R.layout.simple_expandable_list_item_1)
    adapterWatcherCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner_watcher.adapter = adapterWatcherCategory
    spinner_watcher.onItemSelectedListener = this
  }

  override fun onNothingSelected(p0: AdapterView<*>?) {
    //do nothing
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {
    //do nothing
//    val text = parent!!.getItemAtPosition(position).toString()
//    setLogText(text, 0,0)
  }

  private fun setClickListeners() {
    btn_clear.setOnClickListener {
      tv_log.text = "Log"
    }

    btn_example.setOnClickListener {
      setExampleLog()
    }

    btn_log.setOnClickListener {
      setLogText(et_log_text.text.toString(), spinner_category.selectedItemPosition, spinner_watcher.selectedItemPosition)
    }

    btn_read_log.setOnClickListener {
      tv_log.text = mShowMeDev.readLog() ?: "File is empty"
    }

  }


  private fun setLogText(msg: String?, category : Int, watcher : Int) {
    val logMsg = ShowMe().d(msg!!, watcherType = watcher, logType = category)
    tv_log.append("\n$logMsg")
  }


  private fun setExampleLog() {
    mShowMeDev.startLog()
    mShowMeDev.deleteLog()
    mShowMeProduction.startLog()
    mShowMeProduction.deleteLog()
    mShowMeProduction.mMaxLogSize = 100
    val sb = StringBuilder()

    //Log samples
    sb.append(mShowMeDev.title("All message Type", LogType.ALL.type, WatcherType.PUBLIC.type, logId = 0) + "\n")
    sb.append(mShowMeDev.d("This is an unfiltered message", LogType.ALL.type, WatcherType.PUBLIC.type, logId = 0)+ "\n")
    Thread.sleep(300)
    sb.append(mShowMeDev.d("This is a success message", LogType.SUCCESS.type, WatcherType.PUBLIC.type, logId = 1)+ "\n")
    Thread.sleep(300)
    sb.append(mShowMeDev.d("This is an error message", LogType.ERROR.type, WatcherType.PUBLIC.type, logId = 1 )+ "\n")
    sb.append(mShowMeDev.d("This is a warning message", LogType.WARNING.type, WatcherType.PUBLIC.type)+ "\n")
    sb.append(mShowMeDev.d("This is an event message", LogType.EVENT.type, WatcherType.PUBLIC.type)+ "\n")
    sb.append(mShowMeDev.d("This is an info message", LogType.INFO.type, WatcherType.PUBLIC.type)+ "\n")
    sb.append(mShowMeDev.d("This is a detail message", LogType.DETAIL.type, WatcherType.PUBLIC.type)+ "\n")
    sb.append(mShowMeDev.d("This is a debug message", LogType.DEBUG.type, WatcherType.PUBLIC.type)+ "\n")

    //Log samples
    mShowMeProduction.title("Only Error or higher messages", LogType.ALL.type, WatcherType.PUBLIC.type)
    mShowMeProduction.d("This is a not filtered message", LogType.ALL.type, WatcherType.PUBLIC.type)
    mShowMeProduction.d("This is a success message", LogType.SUCCESS.type, WatcherType.PUBLIC.type)
    mShowMeProduction.d("This is an error message", LogType.ERROR.type, WatcherType.PUBLIC.type)
    mShowMeProduction.d("This is a warning message", LogType.WARNING.type, WatcherType.PUBLIC.type)
    mShowMeProduction.d("This is an event message", LogType.EVENT.type, WatcherType.PUBLIC.type)
    mShowMeProduction.d("This is an info message", LogType.INFO.type, WatcherType.PUBLIC.type)
    mShowMeProduction.d("This is a detail message", LogType.DETAIL.type, WatcherType.PUBLIC.type)
    mShowMeProduction.d("This is a debug message", LogType.DEBUG.type, WatcherType.PUBLIC.type)


    mShowMeProduction.d("This is an error message from Guest Watcher", LogType.ERROR.type, WatcherType.GUEST.type, addSummary = true)
    mShowMeProduction.d("This is an error message from Public Watcher", LogType.ERROR.type, WatcherType.PUBLIC.type, addSummary = true)



    //Wrap
    mShowMeProduction.mMaxLogSize = 5
    mShowMeProduction.title("Wrap examples", LogType.ALL.type, WatcherType.PUBLIC.type)
    mShowMeProduction.d("1234567890", wrapMsg = false)
    mShowMeProduction.d("1234567890", wrapMsg = true)
    mShowMeProduction.d("12345678901", wrapMsg = false)
    mShowMeProduction.d("12345678901", wrapMsg = true)
    mShowMeProduction.d("123456789012", wrapMsg = false)
    mShowMeProduction.d("123456789012", wrapMsg = true)


    mShowMeProduction.skipLine(1, "*", 200)
    mShowMeProduction.mMaxLogSize = 100

    //Design by Contract
    mShowMeProduction.enableShowMe()
    mShowMeProduction.title("Design By Contract Example", LogType.ALL.type, WatcherType.PUBLIC.type)
    val a = 1
    var b = 0
    mShowMeProduction.dbc(a > b, "$a is not greater than $b")
    b = 3
    sb.append(mShowMeProduction.dbc(a > b, "$a is not greater than $b")+ "\n")

    //Special chars
    mShowMeProduction.title("Special chars", LogType.ALL.type, WatcherType.PUBLIC.type)
    sb.append(mShowMeProduction.d(ShowMe.getSpecialChars(), addSummary = false, wrapMsg = true)+ "\n")

    //Summary
    mShowMeProduction.showSummary()

    mShowMeProduction.finishLog()
    mShowMeDev.finishLog()
    tv_log.append("\n${sb.toString()}")
    tv_log.append("\n☕ See Full Log sample in Logcat")
  }


}