package com.example.fromdeskhelper.util.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class infiniteScrollListener(
    val func: (finalitem:Boolean) -> (Unit),
    val layoutManager: LinearLayoutManager, val final:Int, val itemfinalfinal:Boolean) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    //    private var visibleThreshold = 2
//    private var firstVisibleItem = 0
//    private var visibleItemCount = 0
    private var totalItemCount = 0

    private  var finalitem=itemfinalfinal

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {

//            visibleItemCount = recyclerView.childCount
//            cuenta los items que tiene el layout
            totalItemCount = layoutManager.itemCount
//            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
//            firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
//            layoutManager.findFirstCompletelyVisibleItemPosition()
//            println("SE ISO SCROLL ${dx} ${dy} ${previousTotal} ${visibleItemCount} ${totalItemCount} ${firstVisibleItem}")

            if (loading ) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }

//            println("Final ${loading}  ${(totalItemCount - visibleItemCount)}  ${ (firstVisibleItem + visibleThreshold)} ")
            // true  y    (6  - 4 ) <= (2 + 2)
            // true  y    2 <= 5
            // true y    true
//            println(layoutManager.findLastCompletelyVisibleItemPosition())
//            println(final)

            if(layoutManager.findLastCompletelyVisibleItemPosition()==totalItemCount-1 && !finalitem){
                if(final!=totalItemCount) {
                    if(final<=(totalItemCount)){
                        func(true)
                    }else{
                        func(false)
                        loading=true
                    }
                }else{
                    finalitem=true
                    func(finalitem)
                }
            }
        }
    }
}