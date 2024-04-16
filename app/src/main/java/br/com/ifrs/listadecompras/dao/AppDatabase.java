package br.com.ifrs.listadecompras.dao;

import android.content.Context;
import androidx.room.Room;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import br.com.ifrs.listadecompras.model.Produto;

@Database(entities = {Produto.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;
    public abstract ProdutoDAO createProdutoDAO();

    public static AppDatabase getInstance(Context ctx){
        if (appDatabase == null){
            appDatabase = Room.databaseBuilder(ctx.getApplicationContext(), AppDatabase.class, "produto_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }
}
