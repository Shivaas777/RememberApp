package com.fospe.remember.ui.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fospe.remember.R
import com.fospe.remember.adapters.PostListAdapter
import com.fospe.remember.adapters.ProductListAdapter
import com.remember.api.models.post.PostItem
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_store.view.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem


class StoreFragment : Fragment() {

    private lateinit var viewForLayout : View
    private lateinit var list :MutableList<CarouselItem>
    private lateinit var adapter : ProductListAdapter
    private lateinit var productList: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForLayout= inflater.inflate(R.layout.fragment_store, container, false)
        getCoruselImage()
        initilaiseAdapter()
        return viewForLayout
    }
    fun initilaiseAdapter()
    {
        viewForLayout.rv_productList.layoutManager = GridLayoutManager(requireContext(),2)

        productList = ArrayList<Int>()
        productList.add(1)
        productList.add(2)
        productList.add(1)
        productList.add(2)
        adapter = ProductListAdapter(productList,requireContext())
        viewForLayout.rv_productList.adapter=adapter
    }

    fun getCoruselImage()
    {
        list = mutableListOf<CarouselItem>()
        list.add(CarouselItem(imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8iA_ONG2uFOjbS3j38u-6-plTEOlbzYywQ5jKbP2uHzXYmTe-7t8Dhvo4Rl3b5pRnhY0&usqp=CAU"))
        list.add(CarouselItem(imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8iA_ONG2uFOjbS3j38u-6-plTEOlbzYywQ5jKbP2uHzXYmTe-7t8Dhvo4Rl3b5pRnhY0&usqp=CAU"))
        list.add(CarouselItem(imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8iA_ONG2uFOjbS3j38u-6-plTEOlbzYywQ5jKbP2uHzXYmTe-7t8Dhvo4Rl3b5pRnhY0&usqp=CAU"))
        list.add(CarouselItem(imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8iA_ONG2uFOjbS3j38u-6-plTEOlbzYywQ5jKbP2uHzXYmTe-7t8Dhvo4Rl3b5pRnhY0&usqp=CAU"))
        viewForLayout.carousel.addData(list);
    }

}