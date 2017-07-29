package layout

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicfol.duplici.R

import kotlinx.android.synthetic.main.fragment_dialog.*

class DialogFragmento : Fragment() {
    val TAG = javaClass.canonicalName

    companion object {
        fun newInstance(): DialogFragmento {
            return DialogFragmento()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



}