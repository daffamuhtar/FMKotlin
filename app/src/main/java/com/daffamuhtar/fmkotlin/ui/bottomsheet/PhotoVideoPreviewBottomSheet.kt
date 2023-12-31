package com.daffamuhtar.fmkotlin.ui.bottomsheet

import android.app.Dialog
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.MediaController
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.bumptech.glide.Glide
import com.daffamuhtar.fmkotlin.constants.Constants
import com.daffamuhtar.fmkotlin.databinding.BsDocumentPreviewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class PhotoVideoPreviewBottomSheet : BottomSheetDialogFragment() {

    private lateinit var userid: String
    private lateinit var workshopid: String
    private lateinit var spkid: String
    private lateinit var partid: String
    private lateinit var reqcode: String
    private lateinit var resultfrombg: String
    private lateinit var filePath: String
    private lateinit var token: String

    private lateinit var behavior: BottomSheetBehavior<View>

    private val matrix = Matrix()
    private val savedMatrix = Matrix()

    private val NONE = 0
    private val DRAG = 1
    private val ZOOM = 2
    private var mode = NONE

    private val start = PointF()
    private val mid = PointF()
    private var oldDist = 1f
    private lateinit var savedItemClicked: String

    private lateinit var binding: BsDocumentPreviewBinding
    lateinit var mediaController :MediaController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsDocumentPreviewBinding.inflate(inflater, container, false)

        val who = arguments?.getString("WHO")
        binding.pbLoading.visibility = View.GONE

        if (who == "Video") {
            binding.ivBsp.visibility = View.GONE

            val player = ExoPlayer.Builder(requireContext()).build()

            binding.videoView.player=player

            // create a media item.
            val mediaItem = MediaItem.Builder()
                .setUri(arguments?.getString(Constants.EXTRA_PREVIEW))
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build()

            // Create a media source and pass the media item
            val mediaSource = ProgressiveMediaSource.Factory(
                DefaultDataSource.Factory(requireContext()) // <- context
            )
                .createMediaSource(mediaItem)

            // Build the media item.
            //   val mediaItem = MediaItem.fromUri(videoUri)
// Set the media item to be played.
            player.setMediaItem(mediaItem)
// Prepare the player.
            player.prepare()
// Start the playback.
            player.play()

        } else {
            binding.videoView.visibility = View.GONE
            binding.ivBsp.visibility = View.VISIBLE
            Glide.with(requireActivity())
                .load(arguments?.getString(Constants.EXTRA_PREVIEW))
                .into(binding.ivBsp)
        }

        return binding.root

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {

//                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
//                setupFullHeight(bottomSheetDialog);
            val params = (binding.root.parent as View).layoutParams as CoordinatorLayout.LayoutParams
            val behaviorc = params.behavior
            val parent = binding.root.parent as View
            parent.fitsSystemWindows = true
            behavior = BottomSheetBehavior.from(parent)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.root.measure(0, 0)
            val displaymetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displaymetrics)
            val screenHeight = displaymetrics.heightPixels
            behavior.peekHeight = screenHeight

            params.height = screenHeight
            parent.layoutParams = params
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val windowParams = window?.attributes
        windowParams?.dimAmount = 0.0f
        windowParams?.flags = windowParams?.flags?.or(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window?.attributes = windowParams
    }
}
