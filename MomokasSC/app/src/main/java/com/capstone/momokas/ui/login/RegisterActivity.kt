package com.capstone.momokas.ui.login;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.capstone.momokas.MainActivity;
import com.capstone.momokas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    //Deklarasi Variable
    private TextInputEditText myEmail, myPassword,username,nama,alamat,notelp,confirm;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String getEmail, getPassword, getUsername,getnama, getalamat,getnohp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Inisialisasi Widget dan Membuat Objek dari Firebae Authenticaion
        username = (TextInputEditText)findViewById(R.id.inputUsername);
        nama = (TextInputEditText)findViewById(R.id.inputName);
        alamat = (TextInputEditText)findViewById(R.id.inputAddress);
        notelp = (TextInputEditText)findViewById(R.id.inputPhonenumber);
        confirm = (TextInputEditText)findViewById(R.id.inputRetype_Password);
        myEmail = (TextInputEditText)findViewById(R.id.inputEmail);
        myPassword = (TextInputEditText)findViewById(R.id.inputPassword);
        Button regButtton = findViewById(R.id.btn_daftar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();


        regButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekDataUser();
            }

            //Method ini digunakan untuk mengecek dan mendapatkan data yang dimasukan user
            private void cekDataUser() {
                //Mendapatkan dat yang diinputkan User
                getEmail = myEmail.getText().toString();
                getPassword = myPassword.getText().toString();
                String getemail = myEmail.getText().toString();
                String getUsername = username.getText().toString();
                String getnama = nama.getText().toString();
                String getalamat = alamat.getText().toString();
                String getnohp = notelp.getText().toString();


                // Mendapatkan Referensi dari Database
                auth = FirebaseAuth.getInstance();


                //Mengecek apakah email dan sandi kosong atau tidak
                if (isEmpty(getUsername) || isEmpty(getnama) || isEmpty(getalamat)
                        || isEmpty(getnohp) || isEmpty(getemail)) {
                    //Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
                    Toast.makeText(getApplicationContext(), "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                }
                else if (isEmpty(getEmail) || isEmpty(getPassword)) {
                    Toast.makeText(getApplicationContext(), "Email Atau Sandi Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    //Mengecek panjang karakter password baru yang akan didaftarkan
                    if (getPassword.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Sandi Terlalu Pendek, MIN 6 Karakter", Toast.LENGTH_SHORT).show();
                    }else{
                        progressBar.setVisibility(View.VISIBLE);

                        createUserAccount();


                    }
                }

            }

            //Method ini digunakan untuk membuat akun baru user
            private void createUserAccount() {
                getEmail = myEmail.getText().toString();
                getPassword = myPassword.getText().toString();
                final String getUsername = username.getText().toString();
                final String getnama = nama.getText().toString();
                final String getalamat = alamat.getText().toString();
                final String getnohp = notelp.getText().toString();


                auth.createUserWithEmailAndPassword(getEmail, getPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //Mendapatkan Instance dari Database
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();


                                //Mengecek status keberhasilan saat medaftarkan email dan sandi baru

                                if (task.isSuccessful()) {

                                    //Mendapatkan UserID dari pengguna yang Terautentikasi
                                    String getUserID = auth.getCurrentUser().getUid();

                                     /*
        Jika Tidak, maka data dapat diproses dan meyimpannya pada Database
        Menyimpan data referensi pada Database berdasarkan User ID dari masing-masing Akun
        */
                                    database.child("User").child(getUserID)
                                            .setValue(new RegistModel(getUsername, getnama, getalamat,
                                                    getnohp, getEmail))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                                                    username.setText("");
                                                    nama.setText("");
                                                    alamat.setText("");
                                                    notelp.setText("");
                                                    myEmail.setText("");
                                                    Toast.makeText(RegisterActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Toast.makeText(RegisterActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();
                                }else {
                                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }


        });
    }
    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }
}
