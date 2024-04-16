package br.com.ifrs.listadecompras.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.ifrs.listadecompras.model.Produto;

@Dao
public interface ProdutoDAO {

    @Query("SELECT * FROM Produto")
    List<Produto> todos();

    @Insert
    void salva(Produto... produto);

    @Update
    void edita(Produto produto);
}
