package com.capstone.momokas.ui.profile

class ImageModel {
    var image_url: String? = null
        private set

    //Konstruktor kosong, untuk data snapshot pada Firebase Realtime Database
    constructor() {}
    constructor(image_url: String?) {
        this.image_url = image_url
    }
}