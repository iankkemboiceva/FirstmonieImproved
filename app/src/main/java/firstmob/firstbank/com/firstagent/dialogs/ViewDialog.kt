package firstmob.firstbank.com.firstagent.dialogs

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import firstmob.firstbank.com.firstagent.Activity.R


class ViewDialog(internal var activity: Activity) {
    internal lateinit var dialog: Dialog

    fun showDialog() {

        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_loading_layout)

        val gifImageView = dialog.findViewById<ImageView>(R.id.custom_loading_imageView)

        val imageViewTarget = GlideDrawableImageViewTarget(gifImageView)
        Glide.with(activity)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()

                .into(imageViewTarget)

        dialog.show()
    }

    fun hideDialog() {
        if(dialog!=null){
            dialog.dismiss()
        }

    }
}