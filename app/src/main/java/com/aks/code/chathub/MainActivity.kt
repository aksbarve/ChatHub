package com.aks.code.chathub

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.aks.code.chathub.fragment.AccFragment
import com.aks.code.chathub.fragment.peopleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFrag(peopleFragment())

        navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navigation_hub -> {
                    replaceFrag(peopleFragment())
                    true
                }

                R.id.navigation_info -> {
                    replaceFrag(AccFragment())
                    true
                }
                else -> false
            }
        }
    }


    private fun replaceFrag(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.frag_Layout, fragment)
                .commit()
        }
    }
