package com


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import com.cachmanager.adapter.PinBoardAdapter
import com.cachmanager.model.User
import com.cachmanager.view.SpacesItemDecoration
import com.cachmanager.viewmodel.PinBoardViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var adapter: PinBoardAdapter? = null
    private var model: PinBoardViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.cachemanager.R.layout.activity_main)
        // toolbar set
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(com.cachemanager.R.string.app_name)

        model = ViewModelProviders.of(this).get(PinBoardViewModel::class.java!!)

        recyclerViewUser.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewUser.addItemDecoration(SpacesItemDecoration(16))

        getPinboardList()
        swipeToRefresh()
    }

    private fun swipeToRefresh() {
        //  observer refresh dialog
        model!!.isSwipeRefresh
            .observe(this, Observer<Boolean> {
                swipeRefreshLayout.isRefreshing = it!!

            })


        // Swipe to refresh
        swipeRefreshLayout!!.setOnRefreshListener {
            getPinboardList()

        }
    }

    private fun getPinboardList() {

        // observer pinboard data
        model!!.getPinboardList(this)
            .observe(this, Observer<ArrayList<User>> {
                if (adapter == null) {
                    adapter = PinBoardAdapter(this@MainActivity, it!!)
                    recyclerViewUser.adapter = adapter
                } else {
                    adapter!!.setList(it!!)
                    adapter!!.notifyDataSetChanged()
                }

            })
    }
}
