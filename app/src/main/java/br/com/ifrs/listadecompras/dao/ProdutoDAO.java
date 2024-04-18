package br.com.ifrs.listadecompras.dao;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;
import java.util.List;

import br.com.ifrs.listadecompras.model.Produto;

@Dao
public interface ProdutoDAO {
    @Query("SELECT * FROM Produto")
    List<Produto> getAllProdutos();

    @Query("SELECT * FROM Produto WHERE id = :id")
    Produto getProdutoById(int id);

    @Insert(onConflict = REPLACE)
    void insert(Produto produto);

    @Update
    public void update(Produto produto);

    @Delete
    public void delete(Produto produto);
}
