package com.example.campusexpensemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Adapter.budgetAdapter2;
import com.example.campusexpensemanager.Adapter.expenseAdapter;
import com.example.campusexpensemanager.DataBase.ExpensesData;
import com.example.campusexpensemanager.Model.Expenses;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView rv_income, rv_expense;
    private TextView tv_income, tv_expense;

    private expenseAdapter expenseAdapter;
    private budgetAdapter2 budgetAdapter2;

    private List<Expenses> expensesList = new ArrayList<>();
    private ExpensesData expensesData;

    private List<incomeModel> incomeModelList = new ArrayList<>();
    private DatabaseHandler1 databaseHandler1;

    private String totalIncome, totalExpense;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);
        expensesData = new ExpensesData(getContext());
        databaseHandler1 = new DatabaseHandler1(getContext());

        fillExpenseModel();
        fillIncomeModel();

        return view;
    }

    private void init(View view) {
        rv_income = view.findViewById(R.id.rv_income);
        rv_expense = view.findViewById(R.id.rv_expense);
        tv_income = view.findViewById(R.id.tv_income);
        tv_expense = view.findViewById(R.id.tv_expense);
    }

    private void fillExpenseModel() {
        expensesList = expensesData.getAllIncome();
        int total = 0;
        for (Expenses model : expensesList) {
            total += Integer.parseInt(model.getAmount());
        }
        totalExpense = String.valueOf(total);
        tv_expense.setText("$" + totalExpense);

        expenseAdapter = new expenseAdapter(getContext(), expensesList);
        rv_expense.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_expense.setHasFixedSize(true);
        rv_expense.setAdapter(expenseAdapter);
    }

    private void fillIncomeModel() {
        incomeModelList = databaseHandler1.getAllIncome();

        int total = 0;
        for (incomeModel model : incomeModelList) {
            total += Integer.parseInt(model.getAmount());
        }
        totalIncome = String.valueOf(total);
        tv_income.setText("₹" + totalIncome);

        budgetAdapter2 = new budgetAdapter2(getContext(), incomeModelList);
        rv_income.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_income.setHasFixedSize(true);

        rv_income.setAdapter(budgetAdapter2);

    }
}
