package com.catinthedark.touchcontrols

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.badlogic.gdx.backends.android.AndroidApplication
import com.catinthedark.touchcontrols.R
import com.catinthedark.touchcontrols.pointer.KnobActivity
import com.catinthedark.touchcontrols.pointer.PointerActivity

class MainActivity : AppCompatActivity() {

    val examples: Map<String, Class<out AndroidApplication>> = mapOf(
            "Pointer control" to PointerActivity::class.java,
            "Knob control" to KnobActivity::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter: ArrayAdapter<Pair<String, Class<out AndroidApplication>>> = ControlExamplesAdapter(this, R.layout.examples_list_item)
        adapter.addAll(examples.map { example ->
            Pair<String, Class<out AndroidApplication>>(example.key, example.value)
        })
        val view = findViewById(R.id.examples_listview) as ListView
        view.adapter = adapter
    }

    class ControlExamplesAdapter(context: Context?, resource: Int) : ArrayAdapter<Pair<String, Class<out AndroidApplication>>>(context, resource) {
        val mContext = context
        val mResource = resource

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(mResource, parent, false);
            } else {
                view = convertView
            }
            val example = getItem(position)
            val exampleNameTV = view.findViewById(R.id.example_list_textView) as TextView
            exampleNameTV.text = example.first
            exampleNameTV.setOnClickListener { view ->
                mContext?.startActivity(Intent(mContext, example.second))
            }
            return view
        }
    }
}
