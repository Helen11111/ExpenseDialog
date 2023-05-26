package ru.myitschool.lab23;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<ExpenseModel> modelArrayList = new ArrayList<>();

    TextView efCurrentBalanceText;
    Button addFab;
    AlertDialog dialog;
    String type;
    Double editFinance = 0.;
    EditText editText;
    ExpenseModel model;
    ExpenseAdapter adapter;
    Bundle b;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        b = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.ef_expenses_rv);


        adapter = new ExpenseAdapter(modelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        efCurrentBalanceText = findViewById(R.id.ef_current_balance_text);
        addFab = findViewById(R.id.add_fab);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        Spinner spinner = view.findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.Finance, android.R.layout.simple_spinner_item);
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterS);
        spinner.setOnItemSelectedListener(this);

        editText = view.findViewById(R.id.expense_amount_edit_text);
        Button buttonAdd = view.findViewById(R.id.add_button);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Income")){
                    editFinance += Double.parseDouble(editText.getText().toString());
                }
                if(type.equals("Expense")){
                    editFinance -= Double.parseDouble(editText.getText().toString());
                }


                efCurrentBalanceText.setText(numberFormat(editFinance));

                getData();

                editText.setText("");

                dialog.dismiss();
            }
        });

        builder.setView(view);

        dialog = builder.create();

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private void getData() {
        Date date2 = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String date = format.format(date2);
        model = new ExpenseModel(type, date, editText.getText().toString());
        modelArrayList.add(model);
    }

    private String numberFormat(Double d) {
        int i = ((int) (d * 10));
        d = i / 10.0;
        /*DecimalFormat round = new DecimalFormat ("###.#");
        String formatted = String.valueOf(round.format(d));*/
        String formatted = String.valueOf(d);
        return formatted;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        type = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        type = parent.getItemAtPosition(0).toString();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        String s = "";
        Double d = Double.valueOf(efCurrentBalanceText.getText().toString());
        switch (item.getItemId())
        {
            case 101:
                s = numberFormat(adapter.remove(d));
                break;
            case 102:
                s = numberFormat(adapter.copy(d));
                break;
            default:
                return super.onContextItemSelected(item);
        }
        efCurrentBalanceText.setText(s);

        return true;
    }
}
