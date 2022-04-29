package com.example.netflix


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.netflix.Repository.arrayOfVideos
import com.example.netflix.databinding.FragmentHomeBinding
import com.google.android.material.button.MaterialButton



class HomeFragment : Fragment() {
    lateinit var  binding : FragmentHomeBinding

    val array=  Array(30){""}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate (inflater, container, false)

        return binding.root

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title="Netflix"




        val pref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        getArrayFromShared()

        for (i in array.indices){
            if (array[i]!=""){
                arrayOfVideos[i].isFavorite=true
                //(arrayOfButtons[i] as MaterialButton).setIconTintResource(R.color.red)
            }
        }
        binding.videoListRecyclerView.adapter=VideoAdapter(arrayOfVideos,{onclickButton(it)})

//        for(i in arrayOfVideos.indices){
//            arrayOfButtons[i].setOnClickListener {
//                onclickButton()
//            }
//        }
    }

    private fun onclickButton(i:Int) {
        val pref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)

            if (pref.getString("name", "").isNullOrBlank()) {
                goToProfileFragment()
            } else if (array[i] != "") {
                arrayOfVideos[i].isFavorite = false
                //(arrayOfButtons[i] as MaterialButton).setIconTintResource(R.color.white)
                array[i] = ""
                saveArrayToShared()
                findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
            } else {
                arrayOfVideos[i].isFavorite = true
                //(arrayOfButtons[i] as MaterialButton).setIconTintResource(R.color.red)
                array[i] = arrayOfVideos[i].title
                //array[i] = arrayOfTextViews[i].text.toString()
                saveArrayToShared()
                findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
            }
    }

    private fun getArrayFromShared() {
        val pref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        val size: Int = pref.getInt("array_size", 0)

        if (size != 0 ) {
            for (i in 0 until size)
                array[i]= pref.getString("array_$i", null).toString()
        }

    }

    private fun saveArrayToShared() {
        val pref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        val edit= pref.edit()
        edit.putInt("array_size", array.size)
        for (j in array.indices)
            edit.putString("array_$j", array[j])
        edit.apply()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.profileMenu  -> {
                goToProfileFragment()
                true
            }
            R.id.favoriteMenu  -> {
                goToFavoriteFragment()
                true
            }
            R.id.comingSoonMenu->{
                goToComingSoonFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToProfileFragment() {
        findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
    }

    private fun goToFavoriteFragment() {
        val pref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        if (pref.getString("name","").isNullOrBlank()){
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }else {
            findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }
    }


    private fun goToComingSoonFragment() {
            findNavController().navigate(R.id.action_homeFragment_to_comingSoonFragment)
        }



}
