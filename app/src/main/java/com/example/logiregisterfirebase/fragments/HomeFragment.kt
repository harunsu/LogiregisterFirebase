package com.example.logiregisterfirebase.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.logiregisterfirebase.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    val adapter=GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        adapter.add(HomeItem())
        adapter.add(HomeItem())
        adapter.add(HomeItem())
        recylerview_home.adapter=adapter


        val imageList = ArrayList<SlideModel>() // Create image list

        // imageList.add(SlideModel("String Url" or R.drawable)
        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        imageList.add(SlideModel("https://www.azbibak.com/wp-content/uploads/2019/09/palu-kalesi-hakkinda-bilgi-1280x720.jpg", "And people do that Palu is fantastic"))
        imageList.add(SlideModel("https://i12.haber7.net//haber/haber7/photos/2019/09/doguda_bir_sonbahar_cenneti_palu_1551085356_1022.jpg", "I Love Palu."))
        imageList.add(SlideModel("https://www.ikolsoftware.com/public/resized/high/image_data/original/570572df50d16d10a291454551d3e9b155b7ce4c/58dfa13b1a948.jpg", "Palu city"))

        image_slider_1.setImageList(imageList)
    }
}

class HomeItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout(): Int {
        return R.layout.home_new_item
    }

}