package com.gmail.luizjmfilho.sevenwonders

import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import com.google.firebase.perf.metrics.Trace

inline fun <T> firebasePerformanceTrace(name: String, block: (Trace) -> T): T {
    val trace = Firebase.performance.newTrace(name)

    trace.start()
    val result = block(trace)
    trace.stop()

    return result
}
