/**
 * Copyright Microsoft Corporation
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arc.azuredemo;

import static java.sql.DriverManager.println;

import java.io.File;
import java.util.UUID;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.TextView;

import com.jaiselrahman.filepicker.model.MediaFile;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.*;

/**
 * This sample illustrates basic usage of the various Blob Primitives provided
 * in the Storage Client Library including CloudBlobContainer, CloudBlockBlob
 * and CloudBlobClient.
 */
public class BlobGettingStartedTask extends AsyncTask<String, Void, Void> {

    private MainActivity act;
    private MediaFile file;

    public BlobGettingStartedTask(MainActivity act, MediaFile file) {
        this.act = act;
        this.file=file;
    }

    @Override
    protected Void doInBackground(String... arg0) {

        println("BlobBasics");

        try {
        //azure key = DefaultEndpointsProtocol=https;AccountName=storagecreateon23;AccountKey=Nf7aPs73IVKRFdhB/PDXJ7P0yyE0ig0W+Wka/lpuUM6hkpUjW6EP1NxqE78G/V5flxWJ90ZVMzj9+ASt918YYg==;EndpointSuffix=core.windows.net
        
            // Setup the cloud storage account.
            CloudStorageAccount account = CloudStorageAccount
                    .parse("azure key");

            // Create a blob service client
            CloudBlobClient blobClient = account.createCloudBlobClient();

            // Get a reference to a container
            // The container name must be lower case
            // Append a random UUID to the end of the container name so that
            // this sample can be run more than once in quick succession.
            CloudBlobContainer container = blobClient.getContainerReference("containerpublic");

            // Create the container if it does not exist
            container.createIfNotExists();

            // Make the container public
            // Create a permissions object
            BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

            // Include public access in the permissions object
            containerPermissions
                    .setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

            // Set the permissions on the container
            container.uploadPermissions(containerPermissions);

            // Get a reference to a blob in the container
            /*
            For text upload
            CloudBlockBlob blob1 = container
                    .getBlockBlobReference("blobAndroid");

            // Upload text to the blob
            blob1.uploadText("Hello, World android");
            */


            //For file upload
            CloudBlockBlob blob1 = container
                    .getBlockBlobReference("blobAndroidFile");

            // Upload text to the blob
//            blob1.uploadFromFile(file.getPath());

            //for download
            File file=new File(Environment.getExternalStorageDirectory(),
                    "koushik.jpg");
            blob1.downloadToFile(file.getPath());

        } catch (Throwable t) {
            println(t.getMessage());
        }

//        println("BlobBasics");

        return null;
    }
}
