package com.pcandroiddev.notesapp.api

import com.pcandroiddev.notesapp.models.user.UserRequest
import com.pcandroiddev.notesapp.models.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/users/register")
    suspend fun register(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("/users/login")
    suspend fun login(@Body userRequest: UserRequest): Response<UserResponse>

}
