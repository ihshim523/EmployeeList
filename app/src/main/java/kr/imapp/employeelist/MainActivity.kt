package kr.imapp.employeelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.imapp.employeelist.databinding.ActivityMainBinding
import kr.imapp.employeelist.ui.home.HomeFragment
import kr.imapp.employeelist.util.fragmentTransaction

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            fragmentTransaction {
                val fragment = HomeFragment.newInstance()
                replace(R.id.container, fragment)
            }
        }
    }
}
