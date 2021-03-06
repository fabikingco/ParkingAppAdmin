package com.example.fabik.parkingapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fabik.parkingapp.BD_Utilidades.Utilidades;
import com.example.fabik.parkingapp.Printer.Presenter;
import com.example.fabik.parkingapp.Printer.PrintManager;
import com.example.fabik.parkingapp.Printer.viewInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IngresarVehiculos extends AppCompatActivity {

    EditText placa;
    private RadioButton rbAuto, rbMoto;

    private Presenter presenter;
    public static viewInterface listener; //Para la impresion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_vehiculos);

        presenter= new Presenter(callback,this); //Inicializar impresora

        setTitle("Ingresar Vehiculos");

        rbAuto = findViewById(R.id.rbAuto);
        rbMoto = findViewById(R.id.rbMoto);

    }

    public void Nuevo_Ingreso(View view) {

        placa = findViewById(R.id.et_i_Placa);
        Global.Placa = placa.getText().toString();

        if (rbAuto.isChecked()==true){
            Global.Tipo = "Automovil";
        } else
        if (rbMoto.isChecked()==true) {
            Global.Tipo = "Motocicleta";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingreso de Vehiculo");
        builder.setMessage("Esta seguro de ingresar "+Global.Tipo+" con placa "+Global.Placa);
        builder.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String outputPattern = "yyyy:MM:dd HH:mm:ss";
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                        Calendar c = Calendar.getInstance();
                        Global.FechaIngreso = outputFormat.format(c.getTime());
                        System.out.println("Prueba "+Global.FechaIngreso);
                        BaseDeDatos();
                        PrintManager.getInstance().ImpresionTiqueteIngreso(listener);
                    }
                });
        builder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(IngresarVehiculos.this, IngresarVehiculos.class);
                        startActivity(intent);
                    }
                });

        builder.show();
    }

    private void BaseDeDatos (){
        AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this, "administracion", null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues insetar = new ContentValues();
        insetar.put(Utilidades.CAMPO_PLACA, Global.Placa);
        insetar.put(Utilidades.CAMPO_FECHA_ING, Global.FechaIngreso);
        insetar.put(Utilidades.CAMPO_TIPO, Global.Tipo);

        bd.insert(Utilidades.TABLA_1, null, insetar);
        bd.close();
    }

    public void Sacar(View view) {
        Intent intent = new Intent(this, SacarVehiculos.class);
        startActivity(intent);
    }

    public void Imprimir (){

    }

    public viewInterface callback = new viewInterface() {
        @Override
        public void showMsg(String msg, int mode) {

        }
    };
}
