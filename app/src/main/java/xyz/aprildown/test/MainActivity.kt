package xyz.aprildown.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.list).run {
            val adapter = Adapter()
            val itemTouchHelperCallback = ItemTouchHelperCallback { from, to ->
                adapter.move(from, to)
            }
            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            this.adapter = adapter
            itemTouchHelper.attachToRecyclerView(this)
        }
    }
}

class Adapter : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = position.toString()
    }

    override fun getItemCount(): Int = 20

    fun move(from: Int, to: Int) {
        notifyItemMoved(from, to)
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView = view.findViewById<TextView>(R.id.text)
}

class ItemTouchHelperCallback(
    private val onMove: (from: Int, to: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        onMove.invoke(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean = false
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.findViewById<MaterialCardView>(R.id.card)?.isDragged = true
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.findViewById<MaterialCardView>(R.id.card).isDragged = false
    }
}
