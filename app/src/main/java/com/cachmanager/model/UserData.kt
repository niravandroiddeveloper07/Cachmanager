package com.cachmanager.model

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class User {

    var name: String
    var profilePicUrl: String
    var uploadedImageUrl: String
    var isLikedByUser: Boolean = false
    var userName: String
    var numberOfLikes: Int = 0
    var categories: List<String>
    var urlToSend: String


    constructor(name: String, profilePicUrl: String, uploadedImageUrl: String, isLikedByUser: Boolean, userName: String, numberOfLikes: Int, categories: List<String>, UrltoSend: String) {
        this.name = name
        this.profilePicUrl = profilePicUrl
        this.uploadedImageUrl = uploadedImageUrl
        this.isLikedByUser = isLikedByUser
        this.userName = userName
        this.numberOfLikes = numberOfLikes
        this.categories = categories
        urlToSend = UrltoSend

    }
}
