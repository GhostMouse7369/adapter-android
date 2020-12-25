package top.laoshuzi.rvbaseadapter.paging

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.CoroutineDispatcher
import top.laoshuzi.rvbaseadapter.base.ItemViewDelegate
import top.laoshuzi.rvbaseadapter.base.ViewHolder

/**
 * Created by laoshuzi on 2020/3/6.
 */
abstract class CommonPagingDataListAdapter<T : Any> : MultiItemTypePagingDataAdapter<T> {

    protected val mLayoutId: Int
    protected val mInflater: LayoutInflater

    constructor(
        context: Context,
        layoutId: Int,
        diffCallback: DiffUtil.ItemCallback<T>
    ) : super(context, diffCallback) {
        this.mLayoutId = layoutId
        this.mInflater = LayoutInflater.from(context)
        init()
    }

    constructor(
        context: Context,
        layoutId: Int,
        diffCallback: DiffUtil.ItemCallback<T>,
        mainDispatcher: CoroutineDispatcher,
        workerDispatcher: CoroutineDispatcher
    ) : super(context, diffCallback, mainDispatcher, workerDispatcher) {
        this.mLayoutId = layoutId
        this.mInflater = LayoutInflater.from(context)
        init()
    }

    private fun init() {
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return mLayoutId
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return true
            }

            override fun convert(holder: ViewHolder, t: T, position: Int) {
                this@CommonPagingDataListAdapter.convert(holder, t, position)
            }
        })
    }

    protected abstract fun convert(holder: ViewHolder, t: T, position: Int)

}