package gr.jkapsouras.butterfliesofgreece.fragments.families.modal

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.modal.adapter.ModalPhotoAdapter
import gr.jkapsouras.butterfliesofgreece.fragments.families.modal.components.ModalPhotoComponent
import kotlinx.android.synthetic.main.modal_fragment.*
import org.koin.android.ext.android.inject

class ModalFragment : BaseFragment<ModalPresenter>(), IModalPhotos {

    companion object {
        private val TAG = ModalFragment::class.java
    }

    private var onPhotoPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
//            updateCircleMarker(binding, position)
        }
    }

    override val presenter: ModalPresenter by inject()
    private lateinit var photosAdapter:ModalPhotoAdapter
    private var modalPhotosComponent: ModalPhotoComponent? = null
    private lateinit var viewPager: ViewPager2

     override val layoutResource: Int
        get() = R.layout.modal_fragment

     override  fun initView(view: View): View {
         photosAdapter = ModalPhotoAdapter(activity as AppCompatActivity)
         return view
    }

     override  fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
         modalPhotosComponent = ModalPhotoComponent(this)
         return listOf(modalPhotosComponent!!)
    }

    override fun setUpPagesStartingWith(index: Int, photos: List<String>) {
        photosAdapter.setData(photos)
        photosAdapter.notifyDataSetChanged()
        modal_view_pager.adapter = photosAdapter
        modal_view_pager.registerOnPageChangeCallback(onPhotoPageChangeCallback)
        photosAdapter.notifyDataSetChanged()
        modal_view_pager.setCurrentItem(index, true)
        photosAdapter.notifyDataSetChanged()
    }

//    private fun updateCircleMarker(binding: ActivityOnBoardingBinding, position: Int) {
//        when (position) {
//            0 -> {
//                binding.onBoardingInitialCircle.background = getDrawable(R.drawable.bg_green_circle)
//                binding.onBoardingMiddleCircle.background = getDrawable(R.drawable.bg_gray_circle)
//                binding.onBoardingLastCircle.background = getDrawable(R.drawable.bg_gray_circle)
//            }
//            1 -> {
//                binding.onBoardingInitialCircle.background = getDrawable(R.drawable.bg_gray_circle)
//                binding.onBoardingMiddleCircle.background = getDrawable(R.drawable.bg_green_circle)
//                binding.onBoardingLastCircle.background = getDrawable(R.drawable.bg_gray_circle)
//            }
//            2 -> {
//                binding.onBoardingInitialCircle.background = getDrawable(R.drawable.bg_gray_circle)
//                binding.onBoardingMiddleCircle.background = getDrawable(R.drawable.bg_gray_circle)
//                binding.onBoardingLastCircle.background = getDrawable(R.drawable.bg_green_circle)
//            }
//        }
//    }

}