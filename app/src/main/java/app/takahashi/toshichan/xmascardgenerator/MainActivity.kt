package app.takahashi.toshichan.xmascardgenerator

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import app.takahashi.toshichan.xmascardgenerator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        //binding.topRelativeLayout.isVisible = false
        //binding.middleRelativeLayout.isVisible = false
        //binding.bottomRelativeLayout.isVisible = false
        //set range for random numbers
        val range = (-30 .. 30)
        //set 3 angles for photos
        var topAngle: Int = range.random()
        var middleAngle: Int = range.random()
        var bottomAngle: Int = range.random()


        //test
        println(topAngle.toString())
        println(middleAngle.toString())
        println(bottomAngle.toString())

        binding.topRelativeLayout.rotation = topAngle.toFloat()
        binding.middleRelativeLayout.rotation = middleAngle.toFloat()
        binding.bottomRelativeLayout.rotation = bottomAngle.toFloat()


        binding.button.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            resultLauncher.launch(intent)
        }

    }

    //counter for the # of photos
    var photoCount: Int = 0
    val resultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                val bitmap: Bitmap?
                // cancelしたケースも含む
                if (data.extras == null) {
                    Log.d("debug", "cancel ?")
                    return@registerForActivityResult
                } else {
                    bitmap = data.extras!!["data"] as Bitmap?
                    if (bitmap != null) {
                        // 画像サイズを計測
                        val bmpWidth = bitmap.width
                        val bmpHeight = bitmap.height
                        Log.d("debug", String.format("w= %d", bmpWidth))
                        Log.d("debug", String.format("h= %d", bmpHeight))
                    }
                }

                if (photoCount == 0){
                    binding.topImageView.setImageBitmap(bitmap)
                    //binding.topRelativeLayout.isVisible = true
                    photoCount++
                }
                else if (photoCount == 1){
                    binding.middleImageView.setImageBitmap(bitmap)
                    //binding.middleRelativeLayout.isVisible = true
                    photoCount++
                }
                else if (photoCount == 2){
                    binding.bottomImageView.setImageBitmap(bitmap)
                    //binding.bottomRelativeLayout.isVisible = true
                    //binding.button.isVisible = false
                    photoCount++
                    binding.button.isEnabled = false
                }else if (photoCount == 3){

                }

            }
        }
    }
}