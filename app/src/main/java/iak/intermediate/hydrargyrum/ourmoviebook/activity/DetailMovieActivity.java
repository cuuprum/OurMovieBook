package iak.intermediate.hydrargyrum.ourmoviebook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import iak.intermediate.hydrargyrum.ourmoviebook.R;
import iak.intermediate.hydrargyrum.ourmoviebook.model.AppVar;
import iak.intermediate.hydrargyrum.ourmoviebook.model.BaseActivity;
import iak.intermediate.hydrargyrum.ourmoviebook.model.Movies;

public class DetailMovieActivity extends BaseActivity {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    AlertDialogManager alert = new AlertDialogManager();
    private Movies movies;
    public TextView id;
    private TextView title;
    private TextView releaseDate;
    private TextView popularity;
    private TextView voteCount;
    private TextView voteAverage;
    private TextView overview;
    private ImageView movieImage;
    private TextView markAsFavorite;
    private TextView delete;
    private String JSONSTRING;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Detail Movie");

        movies = (Movies) getIntent().getSerializableExtra("movie");
        String log = "Id: " + movies.getId() + " Title: " + movies.getTitle() +  " ,Image: " + movies.getPoster_path() + " ,favorite: " + movies.getFavorite();
        Log.d(TAG, log);

        title = (TextView) findViewById(R.id.detail_title);
        id = (TextView) findViewById(R.id.id);
        releaseDate = (TextView) findViewById(R.id.release_date);
        popularity = (TextView) findViewById(R.id.popularity);
        voteCount = (TextView) findViewById(R.id.vote_count);
        voteAverage = (TextView) findViewById(R.id.vote_average);
        overview = (TextView) findViewById(R.id.overview);
        movieImage = (ImageView) findViewById(R.id.gambar_movie);
        markAsFavorite = (TextView) findViewById(R.id.mark_as_favourite);
        delete = (TextView) findViewById(R.id.delete);

        int intId = (int) movies.getId();
        id.setText(String.valueOf(intId));
        title.setText(movies.getTitle());

        String[] dateParts = movies.getRelease_date().split("-");
        String day = dateParts[0];
        releaseDate.setText(day);

        double dblPop = (double) movies.getPopularity();
        String strPop = String.valueOf(dblPop);
        popularity.setText(strPop);

        float dblVoteCount = (float) movies.getVote_count();
        String strVoteCount = String.valueOf(dblVoteCount);
        voteCount.setText(strVoteCount);

        float dblVoteAvg = (float) movies.getVote_average();
        String strVoteAvg = String.valueOf(dblVoteAvg);
        voteAverage.setText(strVoteAvg);

        String strFavorite = (String) movies.getFavorite();

        Log.d(TAG, movies.getTitle() + " fav = " + strFavorite);

        if(strFavorite == "TRUE") {
            delete.setVisibility(View.VISIBLE);
        }
        else if(strFavorite == "FALSE"){
            delete.setVisibility(View.GONE);
        }
        else{
            markAsFavorite.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }


        markAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movies.setFavorite("TRUE");
                int result = getDB().updateMovie(movies);
                Log.d(TAG, "Title " + movies.getTitle() + "Fav " + movies.getFavorite() + " Result " + String.valueOf(result));
                Toast.makeText(DetailMovieActivity.this, "Done, " + movies.getTitle().toString() + " now your favorite.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailMovieActivity.this);
                alertDialogBuilder.setMessage("Are You Sure To Uncheck This Movie as your favorite?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                getDB().unFavoriteMovie(movies);
                                Toast.makeText(DetailMovieActivity.this, "Done ," + movies.getTitle().toString() + " Movie Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        Picasso.with(this).load(AppVar.BASE_IMAGE + movies.getPoster_path()).into(movieImage);

        overview.setText(movies.getOverview());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.exitapp)
            exit();
        return super.onOptionsItemSelected(item);
    }

    private void exit(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Exit Movie App?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        alert.showAlertDialog(DetailMovieActivity.this, "Closing program....", "Please Wait...", false);
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
