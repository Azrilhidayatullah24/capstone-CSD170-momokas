package com.capstone.momokas.ui.login

class RegistModel {
    var username: String? = null
    var nama: String? = null
    var alamat: String? = null
    var nohp: String? = null
    var email: String? = null

    constructor(username: String?, nama: String?, alamat: String?, nohp: String?, email: String?) {
        this.username = username
        this.nama = nama
        this.alamat = alamat
        this.nohp = nohp
        this.email = email
    }

    constructor() {}
}