package com.xkf.ppjoke.ui.sofa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xkf.libannotation.FragmentDestination
import com.xkf.ppjoke.databinding.FragmentSofaBinding

@FragmentDestination(pageUrl = "main/tabs/sofa")
class SofaFragment : Fragment() {
    private lateinit var viewBinding: FragmentSofaBinding
    private lateinit var tabPresenter: TabPresenter
    private lateinit var viewPagerPresenter: ViewPagerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewBinding = FragmentSofaBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabPresenter = TabPresenter(requireActivity(), viewBinding)
        tabPresenter.setupTabs()

        viewPagerPresenter = ViewPagerPresenter(requireActivity(), viewBinding, tabPresenter)
        viewPagerPresenter.setupViewPager(childFragmentManager, lifecycle)
    }
}
