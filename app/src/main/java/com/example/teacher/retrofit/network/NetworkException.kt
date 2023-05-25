package com.example.teacher.retrofit.network

import com.example.teacher.retrofit.network.error.Failure

class NetworkException(val failure: Failure): Throwable()