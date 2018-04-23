package com.example.usuario.proyecto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // alambramos el TextView
        TextView MiTextView = (TextView) findViewById(R.id.singup);

        //Programamos el evento onclick

        MiTextView.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                Intent intento = new Intent(getApplicationContext(), Main3Activity.class);
                startActivity(intento);
            }

        });

        // alambramos el Button
        Button MiButton = (Button) findViewById(R.id.login);

        //Programamos el evento onclick

        MiButton.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                EditText use = (EditText) findViewById(R.id.user);
                String data1 = use.getText().toString();
                EditText use2 = (EditText) findViewById(R.id.pass);
                String data2 = use2.getText().toString();
                List<User> bb =User.find(User.class, "user = ? and pass = ?", data1, data2);

                if(!bb.isEmpty()){
                    Intent intento = new Intent(getApplicationContext(), Main2Activity.class);
                    startActivity(intento);
                }else{
                    MensajeOK("Vuelva a intentarlo");
                    use.setText("");
                    use2.setText("");
                }
            }

        });
    } // Fin del Oncreate de la Actividad 01

    public void MensajeOK(String msg){
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder( v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {} });
        AlertDialog alert11 = builder1.create();
        alert11.show();
        ;};

}
