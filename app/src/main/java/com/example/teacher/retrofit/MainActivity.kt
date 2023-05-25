package com.example.teacher.retrofit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teacher.retrofit.network.KtorClient
import com.example.teacher.retrofit.network.KtorClient.requestAndCatch
import com.example.teacher.retrofit.network.error.Failure

import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    val textView: TextView by lazy {
        findViewById(R.id.textView)
    }

    val editText: EditText by lazy {
        findViewById(R.id.editText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d(TAG, "beforeTextChanged")
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d(TAG, "onTextChanged")
            }

            override fun afterTextChanged(editable: Editable) {
                Log.d(TAG, "afterTextChanged")
                loadPred(editable)
            }
        })
    }

    private fun loadPred(editable: Editable) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                KtorClient.httpClient.requestAndCatch({
                    val modelResp = GetRepo().getPred(editable.toString())
                    withContext(Dispatchers.Main) {
                        textView.text = modelResp.text[0]
                    }
                }, {
                        when (failure) {
                            is Failure.NetworkError -> {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(baseContext, "No connection", Toast.LENGTH_SHORT).show()
                                }
                            }
                            is Failure.HttpError -> {
                                withContext(Dispatchers.Main) {
                                    textView.text = failure.message
                                }
                            }
                            else -> throw this
                        }
                    }
                )
            } catch (cause: Throwable) {
                cause.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(baseContext, "wtf???", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}