package com.example.mygilingan.model

import android.icu.text.CaseMap

data class NotifPesanan(var title: String = "", var description : String = "", var data : DataPesananPemesan) : DataPesananPemesan() {
}