package com.allanlin97.ui.extinguisher;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

// Set up multipart request for posting the images
public class SimpleMultiPartRequest extends Request<String> {

    private MultipartEntityBuilder entity = MultipartEntityBuilder.create();

    private static final String FILE_PART_NAME = "file";

    private final Response.Listener<String> mListener;
    private final File mFilePart;
    private final HttpEntity httpEntity;

    public SimpleMultiPartRequest(String url, File file, Response.ErrorListener errorListener, Response.Listener<String> listener)
    {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mFilePart = file;
        buildMultipartEntity();
        httpEntity = entity.build();
    }

    private void buildMultipartEntity() {
        entity.addBinaryBody(FILE_PART_NAME, mFilePart, ContentType.create("image/jpeg"), mFilePart.getName());
        entity.setMode(HttpMultipartMode.EXTENDED);
    }

    @Override
    public String getBodyContentType() {
        return httpEntity.getContentType();
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            httpEntity.writeTo(bos);
        }
        catch (IOException e)
        {
            Log.i("Exception", e.getLocalizedMessage());
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        return Response.success("Uploaded", getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }
}