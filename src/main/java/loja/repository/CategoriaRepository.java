package loja.repository; // estoque 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import loja.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	// JPA é um framework do spring p/ gravar(persistir) dados do banco. Uso herança
	// p/ acessá-lo.
	// a porta do estoque (repository), fica entre ele e o garçom (controller).

	public List<Categoria> findAllByIdContainingIgnoreCase(@Param("id") Long id);

	// findAllByIdContainingIgnoreCase - procure pelo o ID tudo o que contenha e
	// ignore o case
	// @Param - o parâmetro direto, no caso o ID

}
