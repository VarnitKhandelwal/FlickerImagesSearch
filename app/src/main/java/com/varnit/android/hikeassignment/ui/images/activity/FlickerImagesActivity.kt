package com.varnit.android.hikeassignment.ui.images.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import com.varnit.android.hikeassignment.data.images.FlickerPhotosResponseModel
import com.varnit.android.hikeassignment.domain.model.Status
import com.varnit.android.hikeassignment.ui.images.adapter.FlickerImageAdapter
import com.varnit.android.hikeassignment.ui.images.viewmodel.FlickerPhotosViewModel
import com.varnit.android.hikeassignment.utils.PaginationScrollListener
import com.varnit.android.hikeassignment.utils.getValue
import com.varnit.android.hikeassignment.utils.lifecycleAwareLazy
import com.varnit.android.hikeassignment.utils.obtainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class FlickerImagesActivity : AppCompatActivity() {

    private var flickerPhotosResponseModel: FlickerPhotosResponseModel? = null
    private lateinit var flickerPhotosViewModel: FlickerPhotosViewModel
    private val flickerImageAdapter by lifecycleAwareLazy(lifecycle) { FlickerImageAdapter() }
    private val progressBar by lifecycleAwareLazy(lifecycle) { findViewById<ProgressBar>(com.varnit.android.hikeassignment.R.id.progressBar) }
    private var isLoading = false
    private var isLastPage = false
    private var isClear = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.varnit.android.hikeassignment.R.layout.activity_main)

        createViewModel()
        initRecyclerView()
        observeFlickerImagesList()
        flickerPhotosViewModel.setAutoCompleteQuery("tesla")
    }

    private fun createViewModel() {
        flickerPhotosViewModel = obtainViewModel(FlickerPhotosViewModel::class.java)
    }

    private fun initRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 3)

        imagesList.apply {
            adapter = flickerImageAdapter
            layoutManager = gridLayoutManager
            addOnScrollListener(OnScrollListener(layoutManager as GridLayoutManager))
        }
    }

    private fun observeFlickerImagesList() {
        progressBar.visibility = View.VISIBLE
        flickerPhotosViewModel.getImagesList()
        .observe(this, Observer { it ->
                    it?.let {
                        when (it.status) {
                            Status.LOADING -> {
                                isLoading = true
                                progressBar.visibility = View.VISIBLE
                                imagesList.visibility = View.GONE
                                handleError(false)
                            }
                            Status.SUCCESS -> {
                                isLoading = false
                                flickerPhotosResponseModel = it.data
                                if (flickerPhotosResponseModel != null) {
                                    progressBar.visibility = View.GONE
                                    handleError(false)
                                    imagesList.visibility = View.VISIBLE
                                    isLastPage = (flickerPhotosResponseModel?.photos?.page == flickerPhotosResponseModel?.photos?.pages)
                                    if(isClear) {
                                        flickerImageAdapter.clearList()
                                        isClear = !isClear
                                        flickerPhotosViewModel.pageNum = 1
                                    }
                                    flickerImageAdapter.updateData(flickerPhotosResponseModel?.photos?.photo!!)
                                } else {
                                    imagesList.visibility = View.GONE
                                    handleError(true)
                                }
                            }
                            Status.ERROR -> {
                                isLoading = false
                                progressBar.visibility = View.GONE
                                imagesList.visibility = View.GONE
                                handleError(true)
                            }
                        }
                    }
                })
    }

    private fun handleError(isShow: Boolean) {
        if (isShow)
            no_internet_text.visibility = View.VISIBLE
        else
            no_internet_text.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(com.varnit.android.hikeassignment.R.menu.menu_toolbar, menu)
        val mSearch = menu?.findItem(com.varnit.android.hikeassignment.R.id.action_search)

        val mSearchView = mSearch?.actionView as SearchView
        mSearchView.setQueryHint("Search Topic")
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                flickerPhotosViewModel.setAutoCompleteQuery(newQuery)
                isClear = true
                flickerPhotosViewModel.pageNum = 1
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }


    private fun loadNextPage() {
        imagesList.post {
            flickerPhotosViewModel.loadNextPage()
        }
    }

    inner class OnScrollListener(layoutManager: GridLayoutManager) : PaginationScrollListener(layoutManager) {
        override fun isLoading() = isLoading
        override fun loadMoreItems() = loadNextPage()
        override fun isLastPage() = isLastPage
    }
}
