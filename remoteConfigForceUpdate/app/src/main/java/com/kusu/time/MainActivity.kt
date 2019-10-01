package com.kusu.time

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val forceUpdateChecker =
            ForceUpdateChecker(this, object : ForceUpdateChecker.OnUpdateNeededListener {
                override fun onUpdateNeeded(updateUrl: String) {
                    val dialog = AlertDialog.Builder(this@MainActivity)
                        .setTitle("New version available")
                        .setMessage("Please, update app to new version to continue reposting.")
                        .setPositiveButton("Update",
                            DialogInterface.OnClickListener { dialog, which ->
                                redirectStore(
                                    updateUrl
                                )
                            })
                        .setNegativeButton("No, thanks",
                            DialogInterface.OnClickListener { dialog, which -> finish() }).create()
                    dialog.show()
                }
            })
        forceUpdateChecker.check()
    }


    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
