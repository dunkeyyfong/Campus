package com.example.campusexpensemanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.campusexpensemanager.DatabaseSQLite.DatabaseHandler;
import com.example.campusexpensemanager.Models.incomeModel; // Đảm bảo import đúng gói
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private DrawerLayout drawerLayout;

    private  NavigationView navigationView;
    FloatingActionButton fab;
    private TextView tv_incomeAmount, tv_incomeType, tv_incomeNote;
    private EditText et_incomeAmount, et_incomeType, et_incomeNote;
    private Button btn_save;
    public DatabaseHandler databaseHandler;
    @SuppressLint({"NonConstantResourceId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fab = findViewById(R.id.fab);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_Home);
        }

        replaceFragment(new HomeFragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.menu_Home){
                replaceFragment(new HomeFragment());
            }
            else  if(item.getItemId() == R.id.menu_Expense){
                replaceFragment(new ExpenseFragment());
            }
            else  if(item.getItemId() == R.id.menu_Budget){
                replaceFragment(new BudgetFragment());
            }
            else  if(item.getItemId() == R.id.menu_Profile){
                replaceFragment(new ProfileFragment());
            }

            return true;
        });

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showBottomDialog();
           }
       });


    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout ExpenseLayout = dialog.findViewById(R.id.layoutExpense);
        LinearLayout BudgetLayout = dialog.findViewById(R.id.layoutBudget);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        ExpenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        BudgetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBudgetDialog();
                dialog.dismiss();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    @Override
    public boolean onNavigationItemSelected( MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.nav_Home){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();

        }
        else  if(menuItem.getItemId() == R.id.nav_Expense){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ExpenseFragment()).commit();

        }
        else  if(menuItem.getItemId() == R.id.nav_Budget){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new BudgetFragment()).commit();

        }
        else  if(menuItem.getItemId() == R.id.nav_Profile){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ProfileFragment()).commit();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showBudgetDialog (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.add_budgets, null);
        EditText et_incomeAmount = customLayout.findViewById(R.id.et_incomeAmount);
        EditText et_incomeType = customLayout.findViewById(R.id.et_incomeType);
        EditText et_incomeNote = customLayout.findViewById(R.id.et_incomeNote);
        Button btn_save = customLayout.findViewById(R.id.btn_save);
        Button btn_cancel = customLayout.findViewById(R.id.btn_cancel);

        builder.setView(customLayout);
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
        databaseHandler = new DatabaseHandler(MenuActivity.this);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = et_incomeAmount.getText().toString().trim();
                String type = et_incomeType.getText().toString().trim();
                String note = et_incomeNote.getText().toString().trim();
                long date = System.currentTimeMillis();
                databaseHandler = new DatabaseHandler(MenuActivity.this);
                if (amount.isEmpty()) {
                    et_incomeAmount.setError("Amount cannot be empty");
                    return;
                }
                if (type.isEmpty()) {
                    et_incomeType.setError("Type cannot be empty");
                    return;
                }
                // Add the transaction to the database
                boolean insert = databaseHandler.addData(amount, type, note, String.valueOf(date));

                if (insert == false) {
                    Toast.makeText(MenuActivity.this, "Insert Failed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MenuActivity.this, "Insert Successfully", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void openBudgetFragment(incomeModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER_KEY", (Serializable) model);

        BudgetFragment budgetFragment = new BudgetFragment();
        budgetFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, budgetFragment)
                .addToBackStack(null)
                .commit();
    }


}
