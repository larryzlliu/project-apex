package connected_coop.projectapex

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class PageAdapter(
        fragmentManager: FragmentManager,
        private val numTabs: Int
): FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 ->  RecordingPageFragment()
            1 ->  OldRecordingsPageFragment()
            else ->  RecordingPageFragment() //TODO : ERROR PAGE THIS CASE SHOULD NEVER HAPPEN
        }
    }

    override fun getCount(): Int {
        return numTabs
    }

}