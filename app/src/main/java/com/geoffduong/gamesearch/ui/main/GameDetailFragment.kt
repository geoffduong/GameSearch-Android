package com.geoffduong.gamesearch.ui.main

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geoffduong.gamesearch.databinding.DetailFragmentBinding
import java.io.File

class GameDetailFragment : Fragment() {
    private lateinit var binding: DetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = GameDetailFragmentArgs.fromBundle(requireArguments())
        args.gameDescription?.let { description ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.detailFragmentGameDescription.text = Html.fromHtml(
                    description,
                    Html.FROM_HTML_MODE_COMPACT and Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
                )
            } else {
                binding.detailFragmentGameDescription.text = Html.fromHtml(description)
            }
        }

        args.gameIcon?.let { fileName ->
            val file = File(view.context.filesDir, fileName)
            val bitmap =
                BitmapFactory.decodeStream(file.inputStream())
            binding.detailFragmentGameIcon.setImageBitmap(bitmap)
        }
    }
}