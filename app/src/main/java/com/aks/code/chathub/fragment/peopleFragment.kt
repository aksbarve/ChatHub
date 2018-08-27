package com.aks.code.chathub.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aks.code.chathub.AppConstant
import com.aks.code.chathub.ChatActivity

import com.aks.code.chathub.R
import com.aks.code.chathub.recycleview.item.personItem
import com.aks.code.chathub.util.FirestoreUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_people.*
import org.jetbrains.anko.support.v4.startActivity


class peopleFragment : Fragment() {

    private lateinit var user_ListenerRegistration: ListenerRegistration

    private var InitRecycleView = true

    private lateinit var peopleSection: Section

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        user_ListenerRegistration = FirestoreUtil.addUserListener(this.activity!!, this::updateRecycler)

        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirestoreUtil.removeListner(user_ListenerRegistration)
        InitRecycleView = true
    }

    private fun updateRecycler (items: List<com.xwray.groupie.kotlinandroidextensions.Item>){

        fun init(){
            recycler_viewPeople.apply {
                layoutManager = LinearLayoutManager(this@peopleFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            InitRecycleView = false
        }

        fun update() = peopleSection.update(items)

        if (InitRecycleView)
            init()
        else
            update()
    }

    private val onItemClick = OnItemClickListener{ item , view ->
        if(item is personItem){
            startActivity<ChatActivity>(
                    AppConstant.USER_NAME to item.person.name,
                    AppConstant.USER_ID to item.userId
            )
        }
    }

}
