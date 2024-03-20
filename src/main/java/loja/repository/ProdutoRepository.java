package loja.repository; // meu estoque hein 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import loja.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	public List<Produto> findAllByTituloContainingIgnoreCase(@Param("título") String titulo);

}
