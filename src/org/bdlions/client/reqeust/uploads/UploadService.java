/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.client.reqeust.uploads;

import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author alamgir
 */
public class UploadService {

    private static final String SERVLET_URL = "FileUploadServlet";
    
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static String uploadImage(String serverRootURL, File image) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", image.getName(), RequestBody.create(MEDIA_TYPE_PNG, image))
                .build();

        Request request = new Request.Builder().url(serverRootURL + SERVLET_URL)
                .post(requestBody).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        
        return response.body().string();
    }
}
