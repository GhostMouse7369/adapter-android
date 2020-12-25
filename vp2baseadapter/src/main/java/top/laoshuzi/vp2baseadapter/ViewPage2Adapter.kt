package top.laoshuzi.vp2baseadapter

import android.util.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by mouse on 2020/3/5.
 */
open class ViewPage2Adapter : FragmentStateAdapter {

    var fragmentKey = ArrayList<String>()

    var fragmentMap = ArrayMap<String, Fragment>()
        private set

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

    override fun getItemCount(): Int {
        return fragmentMap.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentMap[fragmentKey[position]]!!
    }

    fun addFragment(title: String, fragment: Fragment) {
        fragmentKey.add(title)
        fragmentMap[title] = fragment
    }

    fun clear() {
        fragmentKey.clear()
        fragmentMap.clear()
    }

}