package gr.jkapsouras.butterfliesofgreece.fragments.modal.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import gr.jkapsouras.butterfliesofgreece.fragments.modal.ModalPhotoFragment

class ModalPhotoAdapter(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {

    private var itemsCount: Int = 0
    private lateinit var photos: List<String>

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return ModalPhotoFragment.getInstance(position, photos)
    }

    fun setData(photos: List<String>){
        this.photos = photos
        itemsCount = photos.count()
    }
}