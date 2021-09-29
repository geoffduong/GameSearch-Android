package com.geoffduong.gamesearch.data

class GiantBombResponse<T> {
    var status_code: Int? = null
    var error: String? = null
    var limit: Int? = null
    var offset: Int? = null
    var number_of_page_results: Int? = null
    var number_of_total_results: Int? = null
    var results: List<T>? = null
}
