package ru.myitschool.lab23;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder>  {

    private List<ExpenseModel> expenseModelList;
    private int pos, amount;
    ExpenseModel model;

    public static final int IDM_OPEN = 101;
    public static final int IDM_SAVE = 102;

    public ExpenseAdapter(List<ExpenseModel> expenseModelList) {
        this.expenseModelList = expenseModelList;
    }

    public void add(ExpenseModel expenseModel) {
        expenseModelList.add(expenseModel);
        notifyDataSetChanged();
    }

    public synchronized Double remove(Double d) {
        if(expenseModelList.get(pos).getType().equals("Income")) {
            d -= Double.valueOf(expenseModelList.get(pos).getAmount());
        }
        if(expenseModelList.get(pos).getType().equals("Expense")) {
            d += Double.valueOf(expenseModelList.get(pos).getAmount());
        }
        expenseModelList.remove(pos);
        notifyDataSetChanged();
        return d;
    }


    public synchronized Double copy(Double d) {
        ExpenseModel expenseModel2 = new ExpenseModel(expenseModelList.get(pos).getType(), expenseModelList.get(pos).getDate(), expenseModelList.get(pos).getAmount());
        expenseModelList.add(expenseModel2);
        if(expenseModelList.get(pos).getType().equals("Income")){
            d += Double.valueOf(expenseModel2.getAmount());
        }
        if(expenseModelList.get(pos).getType().equals("Expense")){
            d -= Double.valueOf(expenseModel2.getAmount());
        }

        notifyDataSetChanged();
        return d;
    }


    @NonNull
    @Override
    public ExpenseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.MyViewHolder holder, int position) {
        model = expenseModelList.get(position);
        holder.type.setText(model.getType());
        holder.date.setText(model.getDate());
        holder.amount.setText(model.getAmount());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                //onExpenseClickListener.onExpenseClick(model, position);
                pos = holder.getAdapterPosition();
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return expenseModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener {
        final TextView type;
        final TextView date;
        final TextView amount;


        MyViewHolder(View view){
            super(view);
            type = view.findViewById(R.id.expense_type_text);
            date = view.findViewById(R.id.expense_date_text);
            amount = view.findViewById(R.id.expense_amount_text);
            view.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, IDM_OPEN, Menu.NONE, "Delete");
            menu.add(Menu.NONE, IDM_SAVE, Menu.NONE, "Duplicate");
        }
    }

    public int getPosition() {
        return pos;
    }

    public void setPosition(int position) {
        this.pos = position;
    }

}
