package org.moralsh.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // TODO (1) Agregar NetworkUtils para gestiona la comunicacion con TheMovieDB
    // TODO (2) Añadir REcyclerView con grid para gestionar la actividad principal de los posters de las películas
    // TODO (3) Generar una actividad nueva para el detalle de las películas
}
