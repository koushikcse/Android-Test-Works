package com.arc.azuredemo

import com.jaiselrahman.filepicker.model.MediaFile
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import kotlinx.coroutines.CoroutineScope
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * Created by Koushik on 4/7/22.
 */
object AzureUtils {
    private var account: CloudStorageAccount? = null
    private var updateCount = 1
    private var container: CloudBlobContainer? = null

    fun init(connectionString: String) {
        try {
            account = CloudStorageAccount.parse(connectionString)
        } catch (e: Exception) {
        }
    }

    fun init(): CloudStorageAccount? {

        try {
        /*azure key = DefaultEndpointsProtocol=https;AccountName=arcdemostore;AccountKey=2mui3wrYDjXx4rUhQN/Dx5axmmbmyYu8HgClYv/Ihxox+ggaxeFmX/rkiC8FXaDlDl2vHWc9NyPDAiBroqAWjw==;EndpointSuffix=core.windows.net*/
            account =
                CloudStorageAccount.parse("azure key")
        } catch (e: Exception) {
        }
        account?.let { getContainerWithPermission(it) }
        return account
    }


//    fun getContainerList(account: CloudStorageAccount): ArrayList<String> {
//        val list = ArrayList<String>()
//        try {
//            // Create a blob service client
//            val blobClient = account.createCloudBlobClient()
//            val clist = blobClient.listContainers()
//            for (item in clist) {
//                list.add(item.name)
//            }
//        } catch (e: java.lang.Exception) {
//        }
//        return list
//    }

    fun getContainerWithPermission(account: CloudStorageAccount): CloudBlobContainer {
        // Create a blob service client
        val blobClient = account.createCloudBlobClient()

        // Get a reference to a container
        // The container name must be lower case
        // Append a random UUID to the end of the container name so that
        // this sample can be run more than once in quick succession.
        val container = blobClient.getContainerReference("test")

        // Make the container public
        // Create a permissions object
        val containerPermissions = BlobContainerPermissions()

        // Include public access in the permissions object
        containerPermissions.publicAccess = BlobContainerPublicAccessType.CONTAINER

        // Set the permissions on the container

        // Set the permissions on the container
        container.uploadPermissions(containerPermissions)
        this.container = container
        return container
    }


    fun upload(file: MediaFile) {
        container?.let {
            val blob1: CloudBlockBlob = it.getBlockBlobReference("blob" + updateCount++)
            blob1.uploadFromFile(file.path)
        }
    }

    fun download(file: File) {
        container?.let {
            val blob1: CloudBlockBlob = it.getBlockBlobReference(file.name)
            blob1.downloadToFile(file.path)
        }
    }
}
