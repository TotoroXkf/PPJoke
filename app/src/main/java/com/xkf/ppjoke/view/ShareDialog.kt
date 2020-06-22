package com.xkf.ppjoke.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xkf.libcommon.RADIUS_TOP
import com.xkf.libcommon.RoundFrameLayout
import com.xkf.libcommon.Utils
import com.xkf.ppjoke.databinding.LayoutShareItemBinding

class ShareDialog(
    context: Context,
    val shareContent: String
) : AlertDialog(context) {
    private val shareItemList = mutableListOf<ResolveInfo>()
    private lateinit var adapter: ShareDialogAdapter
    var onClickItemListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        val layout = RoundFrameLayout(context)
        layout.setRoundRect(Utils.dpToPx(20), RADIUS_TOP)
        val recyclerView = RecyclerView(context)
        adapter = ShareDialogAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        val layoutParam = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        val margin = Utils.dpToPx(20)
        layoutParam.leftMargin = margin
        layoutParam.topMargin = margin
        layoutParam.bottomMargin = margin
        layoutParam.rightMargin = margin
        layoutParam.gravity = Gravity.CENTER
        layout.addView(recyclerView, layoutParam)

        setContentView(layout)

        window?.apply {
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        queryShareItems()
    }

    private fun queryShareItems() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        shareItemList.clear()
        val list = context.packageManager.queryIntentActivities(intent, 0)
        for (info in list) {
            val packageName = info.activityInfo.packageName
            if (packageName == "com.tencent.mm" || packageName == "com.tencent.mobileqq") {
                shareItemList.add(info)
            }
        }
        if (this::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    inner class ShareDialogAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ShareItemViewHolder(
                LayoutShareItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).root
            )
        }

        override fun getItemCount(): Int = shareItemList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ShareItemViewHolder).bind()
        }
    }

    inner class ShareItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewBinding: LayoutShareItemBinding = DataBindingUtil.findBinding(itemView)!!

        init {
            itemView.setOnClickListener {
                val resolveInfo = shareItemList[adapterPosition]
                val pkg = resolveInfo.activityInfo.packageName
                val cls = resolveInfo.activityInfo.name
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.component = ComponentName(pkg, cls)
                intent.putExtra(Intent.EXTRA_TEXT, shareContent)
                context.startActivity(intent)

                onClickItemListener?.invoke()
            }
        }

        fun bind() {
            val resolveInfo = shareItemList[adapterPosition]
            val drawable = resolveInfo.loadIcon(context.packageManager)
            viewBinding.shareIconView.setImageDrawable(drawable)
            val text = resolveInfo.loadLabel(context.packageManager)
            viewBinding.shareTextView.text = text
        }
    }
}
