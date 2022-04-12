package com.example.pr_sqlite;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText name, phone, dateOfBirth;
    Button insert, select;
    DatabaseHelper databaseHelper;

    int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.txtName);
        phone = findViewById(R.id.txtNumber);
        dateOfBirth = findViewById(R.id.txtDate);
        insert = findViewById(R.id.btnInsert);
        select = findViewById(R.id.btnSelect);
        databaseHelper = new DatabaseHelper(this);

        insert.setOnClickListener(view -> {
            Boolean checkInsertData = databaseHelper.insert(name.getText().toString(),
                    phone.getText().toString(), dateOfBirth.getText().toString());
            if (checkInsertData) {
                Toast.makeText(getApplicationContext(),
                        "Данные успешно добавленны", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Произошла ошибка!", Toast.LENGTH_LONG).show();
            }
        });

        select.setOnClickListener(view -> {
            selectedItem = -1;
            Cursor res = databaseHelper.getData();
            //Проверка на наличие данных
            if (res.getCount() == 0) {
                Toast.makeText(MainActivity.this,
                        "Данные отсутствуют", Toast.LENGTH_LONG).show();
                return;
            }
            //Цикл для перебора и объединения данных
            String[] users1 = new String[res.getCount()];
            ArrayList<User> users = new ArrayList<>();
            while (res.moveToNext()) {
                users.add(new User(res.getString(0), res.getString(1),
                        res.getString(2)));
                Log.d("NAME", res.getString(0));
                users1[res.getPosition()] = users.get(res.getPosition()).toString();
            }
            //Диалоговое окно
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Данные пользователей");
            builder.setSingleChoiceItems(users1, -1,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int item) {
                            selectedItem = item;
                        }
                    });
            builder.setNegativeButton("Назад", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });

            builder.setNeutralButton("Удалить", (dialogInterface, i) -> {
                if (selectedItem != -1) {
                    databaseHelper.removeAt(users.get(selectedItem).getName());
                    Toast.makeText(getApplicationContext(),
                            "Запись удалена!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Запись не выбрана!", Toast.LENGTH_SHORT).show();
                }

            });
            builder.setPositiveButton("Изменить", (dialogInterface, i) -> {
                if (selectedItem != -1) {
                    AlertDialog.Builder editDialog = new AlertDialog.Builder(MainActivity.this);
                    editDialog.setCancelable(true);
                    editDialog.setTitle("Изменение пользователя");
                    LayoutInflater inflater = getLayoutInflater();
                    View dialog = inflater.inflate(R.layout.edit_dialog, null);

                    EditText editName = dialog.findViewById(R.id.txtEditName);
                    EditText editPhone = dialog.findViewById(R.id.txtEditNumber);
                    EditText editDate = dialog.findViewById(R.id.txtEditDate);

                    editName.setText(users.get(selectedItem).getName());
                    editPhone.setText(users.get(selectedItem).getPhone());
                    editDate.setText(users.get(selectedItem).getDateOfBirth());


                    editDialog.setPositiveButton("Сохранить изменения", (dialogInterface1, i1) -> {
                        if (!editDate.getText().toString().equals("") || !editName.getText().toString().equals("") ||
                                !editPhone.getText().toString().equals("")) {
                            databaseHelper.update(users.get(selectedItem).getName(),
                                    editName.getText().toString(),
                                    editPhone.getText().toString(),
                                    editDate.getText().toString());
                            dialogInterface1.cancel();
                        } else {
                            Toast.makeText(getApplicationContext(), "Заполните поля!", Toast.LENGTH_LONG).show();
                        }
                    });
                    editDialog.setNegativeButton("Отмена", (dialogInterface1, i1) -> {
                        dialogInterface1.cancel();
                    });
                    editDialog.setView(dialog);
                    editDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Запись не выбрана!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        });
    }
}