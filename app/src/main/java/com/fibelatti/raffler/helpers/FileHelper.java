package com.fibelatti.raffler.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.fibelatti.raffler.Constants;
import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class FileHelper {
    private Context context;

    private final String jsonPropertyGroupName = "name";
    private final String jsonPropertyGroupItems = "items";

    public FileHelper(Context context) {
        this.context = context;
    }

    public String getGroupFilePath() {
        return context.getFilesDir() + File.separator + Constants.FILE_PATH_EXPORTED_GROUP;
    }

    public Intent createFileShareIntent(Uri uri) {
        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setDataAndType(uri, "*/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return shareIntent;
    }

    public boolean createFileFromGroup(Group group) {

        JSONObject groupJson = new JSONObject();
        try {
            groupJson.put(jsonPropertyGroupName, group.getName());
            groupJson.put(jsonPropertyGroupItems, new JSONArray(group.getItemNames()));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        writeToFile(groupJson.toString());

        return true;
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(Constants.FILE_PATH_EXPORTED_GROUP, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
//            Crashlytics.log("File write failed: " + e.toString());
        }
    }

    public Group readFromFile(Uri uri) {
        String json = readJsonStringFromUri(uri);

        return convertJsonToGroup(json);
    }

    private String readJsonStringFromUri(Uri data) {
        final String scheme = data.getScheme();

        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            try {
                ContentResolver contentResolver = context.getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(data);
                if (inputStream != null) {
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];

                    if (inputStream.read(buffer) > 0) {
                        return new String(buffer, "UTF-8");
                    }
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    private Group convertJsonToGroup(String json) {
        Group.Builder groupBuilder = new Group.Builder();

        try {
            JSONObject groupJson = new JSONObject(json);
            JSONArray groupItemsJson = groupJson.getJSONArray(jsonPropertyGroupItems);

            groupBuilder.setName(groupJson.getString(jsonPropertyGroupName));

            for (int i = 0; i < groupItemsJson.length(); i++) {
                groupBuilder.addItem(new GroupItem.Builder().setName(groupItemsJson.get(i).toString()).build());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return groupBuilder.build();
    }
}
