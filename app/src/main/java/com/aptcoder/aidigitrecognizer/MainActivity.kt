package com.aptcoder.aidigitrecognizer

import android.graphics.*
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aptcoder.aidigitrecognizer.ml.Mnist95
import com.divyanshu.draw.widget.DrawView
import org.tensorflow.lite.support.image.TensorImage
import java.nio.ByteBuffer


class MainActivity : AppCompatActivity() {
    private lateinit var byteBuffer:ByteBuffer

    private  lateinit var draw_view:DrawView
    private  lateinit var domagicButton:Button
    private  lateinit var claer:Button
    private  lateinit var finalres:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        draw_view=findViewById(R.id.draw_view)
        domagicButton=findViewById(R.id.domagicButton)
        finalres=findViewById(R.id.finalres)
        claer=findViewById(R.id.clear)
        claer.setOnClickListener {
            draw_view.clearCanvas()

        }
        draw_view.setStrokeWidth(100f)

        domagicButton.setOnClickListener {
            val model = Mnist95.newInstance(this)
            val image = TensorImage.fromBitmap(toGrayscale( draw_view.getBitmap()!!)!!)


            val outputs = model.process(image)
            val probability = outputs.probabilityAsCategoryList
           var mx=0f;
            var l=""
            for (i in probability){
               if(mx<i.score){
                   mx=i.score
                   l=i.label
               }
            }
            var res=""
            if(mx>0.60f){
                res= "$l-->${mx*100} % confident"
            }else{
                res="Not Sure but probably ${mx*100} % chances is that it is $l"
            }
           finalres.text=res



        }










    }

    fun toGrayscale(bmpOriginal: Bitmap): Bitmap? {
        Toast.makeText(this, "working", Toast.LENGTH_SHORT).show()

        val height: Int = bmpOriginal.height
        val width: Int = bmpOriginal.width
        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val f = ColorMatrixColorFilter(cm)
        paint.colorFilter = f
        c.drawBitmap(bmpOriginal, 0f, 0f, paint)
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()
        return bmpGrayscale
    }


}