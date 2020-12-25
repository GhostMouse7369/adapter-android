package top.laoshuzi.rvbaseadapter.paging

import android.content.Context
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineDispatcher
import top.laoshuzi.rvbaseadapter.base.ItemViewDelegate
import top.laoshuzi.rvbaseadapter.base.ItemViewDelegateManager
import top.laoshuzi.rvbaseadapter.base.ViewHolder

/**
 * Created by laoshuzi on 2020/3/6.
 */
abstract class MultiItemTypePagingDataAdapter<T : Any> : PagingDataAdapter<T, ViewHolder> {

    protected val mContext: Context

    protected val mItemViewDelegateManager: ItemViewDelegateManager<T>
    protected var mOnItemClickListener: OnItemClickListener? = null


    constructor(
        context: Context,
        diffCallback: DiffUtil.ItemCallback<T>
    ) : super(diffCallback) {
        this.mContext = context
        mItemViewDelegateManager = ItemViewDelegateManager()
    }

    constructor(
        context: Context,
        diffCallback: DiffUtil.ItemCallback<T>,
        mainDispatcher: CoroutineDispatcher,
        workerDispatcher: CoroutineDispatcher
    ) : super(diffCallback, mainDispatcher, workerDispatcher) {
        this.mContext = context
        mItemViewDelegateManager = ItemViewDelegateManager()
    }

    override fun getItemViewType(position: Int): Int {
        return if (!useItemViewDelegateManager())
            super.getItemViewType(position)
        else
            mItemViewDelegateManager.getItemViewType(getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType)
        val layoutId = itemViewDelegate.itemViewLayoutId
        val holder = ViewHolder.createViewHolder(mContext, parent, layoutId)
        onViewHolderCreated(holder, holder.convertView)
        setListener(parent, holder, viewType)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convert(holder, getItem(position)!!)
    }

    open fun onViewHolderCreated(holder: ViewHolder, itemView: View) {
    }

    open fun convert(holder: ViewHolder, t: T) {
        mItemViewDelegateManager.convert(holder, t, holder.adapterPosition)
    }

    fun getItemByPosition(position: Int): T {
        return super.getItem(position)!!
    }

    protected open fun isEnabled(viewType: Int): Boolean {
        return true
    }

    protected open fun setListener(parent: ViewGroup?, viewHolder: ViewHolder, viewType: Int) {
        if (!isEnabled(viewType))
            return
        viewHolder.convertView.setOnClickListener { v ->
            if (mOnItemClickListener != null) {
                val position = viewHolder.adapterPosition
                mOnItemClickListener!!.onItemClick(v, viewHolder, position)
            }
        }
        viewHolder.convertView.setOnLongClickListener(OnLongClickListener { v ->
            if (mOnItemClickListener != null) {
                val position = viewHolder.adapterPosition
                return@OnLongClickListener mOnItemClickListener!!.onItemLongClick(
                    v,
                    viewHolder,
                    position
                )
            }
            false
        })
    }


    open fun addItemViewDelegate(itemViewDelegate: ItemViewDelegate<T>): MultiItemTypePagingDataAdapter<T> {
        mItemViewDelegateManager.addDelegate(itemViewDelegate)
        return this
    }

    open fun addItemViewDelegate(
        viewType: Int,
        itemViewDelegate: ItemViewDelegate<T>
    ): MultiItemTypePagingDataAdapter<T> {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate)
        return this
    }

    protected open fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.itemViewDelegateCount > 0
    }

    open fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {

        fun onItemClick(
            view: View?,
            holder: RecyclerView.ViewHolder?,
            position: Int
        )

        fun onItemLongClick(
            view: View?,
            holder: RecyclerView.ViewHolder?,
            position: Int
        ): Boolean
    }


}