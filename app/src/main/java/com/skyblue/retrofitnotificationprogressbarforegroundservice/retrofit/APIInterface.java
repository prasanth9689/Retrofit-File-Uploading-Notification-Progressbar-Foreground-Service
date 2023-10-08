package com.skyblue.retrofitnotificationprogressbarforegroundservice.retrofit;

import com.skyblue.retrofitnotificationprogressbarforegroundservice.model.UploadVideo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @Multipart
    @POST("/prasanth/skyblue/test.php")
    Call<UploadVideo> uploadVideo( @Part MultipartBody.Part video);
}
