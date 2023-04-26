package com.example.fromdeskhelper.ui.View.fragment.Root

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentNotificationsUsersBinding
import com.example.fromdeskhelper.ui.View.ViewModel.Root.NotificationsViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerRootClientAdapter
import com.example.fromdeskhelper.util.TabletPageTransformer
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
private const val LOGFRAGMENT: String = "NotificationsFragment"
@AndroidEntryPoint
class NotificationsRoot : Fragment() {

    companion object {
        fun newInstance() = NotificationsRoot()
    }

    private lateinit var viewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsUsersBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsUsersBinding.inflate(inflater, container, false)
        activity?.title=baseActivity.resources.getString(R.string.menu_notificactions)

        val adapter by lazy { ViewPagerRootClientAdapter(baseActivity) }
        binding.paggeclientid.adapter=adapter;
        binding.paggeclientid.setPageTransformer(TabletPageTransformer())


        TabLayoutMediator(binding.TLMainRootClient,binding.paggeclientid,
            TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
                when(position){
                    0->{
                        tab.text=baseActivity.resources.getString(R.string.home_tab_menus_notification_product)
                        tab.setIcon(R.drawable.baseline_person_24)
                        val Badged: BadgeDrawable =tab.orCreateBadge
                        Badged.backgroundColor= ContextCompat.getColor(baseActivity, R.color.md_green_400)
                        Badged.number=77
                        Badged.isVisible=true
                    }
                    1->{
                        tab.text=baseActivity.resources.getString(R.string.home_tab_menus_notification_providers)
                        tab.setIcon(R.drawable.ic_baseline_broadcast_on_personal_24)
                        val Badged: BadgeDrawable =tab.orCreateBadge
                        Badged.backgroundColor= ContextCompat.getColor(baseActivity, R.color.md_green_400)
                        Badged.number=120
                        Badged.isVisible=true

                    }

                }
            }).attach()
        return binding.root
    }


    override fun onStart() {
        if(baseActivity.binding.appBarMain.toolbarParentClient!!.visibility==View.VISIBLE){
            baseActivity.binding.appBarMain.toolbarParentClient!!.visibility=View.GONE
        }
        if(baseActivity.binding.appBarMain.toolbarParent.visibility==View.GONE){
            baseActivity.binding.appBarMain.toolbarParent.visibility=View.VISIBLE
        }
        if(baseActivity.binding.appBarMain.fab.visibility==View.GONE){
            baseActivity.binding.appBarMain.fab.visibility=View.VISIBLE
        }
        if(baseActivity.binding.appBarMain.refreshFab.visibility==View.GONE){
            baseActivity.binding.appBarMain.refreshFab.visibility=View.VISIBLE
        }
        super.onStart()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}