package com.sachin.example.simplegroovydsl.core

class AdapterRequest {

    def _url = ''
    Closure<String> _urlClosure
    def _header = new LinkedHashMap<String, String>()
    Closure<Map<String, String>> _headerClosure
    def _method = 'POST'
    def _trace = false


    def url(String url) {
        this._url = url
        return this
    }

    def url(Closure closure) {
        this._urlClosure = closure
        return this
    }

    def header(Map<String, Object> header) {
        this._header = header
        return this
    }

    def header(Closure closure) {
        this._headerClosure = closure
        return closure
    }

    def method(String method) {
        this._method = method.toUpperCase()
        return this
    }

    def trace(boolean trace) {
        _trace = trace
        return this
    }
}
