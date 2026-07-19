package gadget.component.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gadget.basic.fragment.GadgetFragment
import gadget.basic.http.BASE_URL
import gadget.basic.http.HTTP
import gadget.basic.theme.GlobalTheme
import gadget.basic.theme.subscribeTheme
import gadget.basic.ui.dsl.PlayerView
import gadget.basic.ui.dsl.ViewPager2
import gadget.basic.ui.dsl.marginLayoutParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request

class ComponentMainFragment : GadgetFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ViewPager2(requireContext(), { marginLayoutParams {
            orientation = ViewPager2.ORIENTATION_VERTICAL
            offscreenPageLimit = 1
            subscribeTheme(GlobalTheme.current) {
                setBackgroundColor(backgroundColor)
            }
            lifecycleScope.launch {
                val response: List<String> = withContext(Dispatchers.IO) {
                    while (BASE_URL.isNullOrBlank()) {
                        delay(7000L)
                    }
                    HTTP.newCall(
                        Request.Builder()
                            .url("${BASE_URL}/videos")
                            .get()
                            .build()
                    ).execute().let { rep ->
                        if (rep.isSuccessful) {
                            val json = rep.body.string() ?: ""
                            Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
                        } else emptyList()
                    }
                }
                adapter = object : FragmentStateAdapter(this@ComponentMainFragment) {
                    private val videos: List<String> = response.map {
                        "${BASE_URL}/video/${it}"
                    }
                    override fun createFragment(position: Int): Fragment = PlayerFragment(videos[position])
                    override fun getItemCount(): Int = videos.size
                }
            }
        }}) {
        }

    class PlayerFragment(
        val link: String,
    ) : GadgetFragment() {

        private val player: ExoPlayer by lazy {
            ExoPlayer.Builder(requireContext()).build().also { player ->
                player.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                player.setMediaItem(MediaItem.fromUri(link))
            }
        }

        val playerView: PlayerView by lazy {
            PlayerView(requireContext(), { marginLayoutParams {
                controllerAutoShow = false
            }}) {
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = playerView

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            this.playerView.player = this@PlayerFragment.player
            this@PlayerFragment.player.prepare()
        }

        override fun onResume() {
            super.onResume()
            player.playWhenReady = true
            tryToPlay()
        }

        override fun onPause() {
            super.onPause()
            player.playWhenReady = false
            tryToPause()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            (view as PlayerView).player = null
            player.stop()
            player.clearMediaItems()
            player.release()
        }

        private fun tryToPlay() {
            val player = this.player ?: return
            if (player.isPlaying) {
                return
            }
            player.play()
        }

        private fun tryToPause() {
            val player = this.player ?: return
            if (!player.isPlaying) {
                return
            }
            player.pause()
        }
    }
}