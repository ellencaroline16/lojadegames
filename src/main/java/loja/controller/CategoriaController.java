package loja.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import loja.model.Categoria;
import loja.repository.CategoriaRepository;

@RestController
@RequestMapping("/categoria")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

	// Mapping:quando quero passar uma requisição.
	// GET:busca um objeto ou informação no banco.
	// POST:quando quero guardar uma entidade inteiro(objeto)no banco.
	// PUT:uso para fazer atualizações no banco.
	// DELETE: deletar o objeto no banco.

	@Autowired // cria o meu objeto com todas as dependências já injetadas
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<Object> getAll() {// getAll é o nome que dei p/ meu método pegar tudo o que todos os objetos
											// que tem no banco
		return ResponseEntity.ok(categoriaRepository.findAll()); // findAll método do JPA, trás todos os objetos do
																	// banco.
	}

	@GetMapping("/{id}") // capturo um objeto no banco pelo id
	public ResponseEntity<Categoria> getById(@PathVariable Long Id) {// @PathVariable é o parâmetro concreto "te entrego
																		// a garrafa p/ vc trazer cheia de água"
		return (ResponseEntity<Categoria>) categoriaRepository.findById(Id).map(resposta -> ResponseEntity.ok(resposta))// findById
																														// -
																														// método
																														// JPA
																														// |
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping // passo uma informação e busco outra complementar a primeira, ou apenas passe.
	// a diferença do post e do get(no cabeçalho), é que o post só passa o parâmetro
	// no corpo da reposição
	public ResponseEntity<Object> post(@Valid @RequestBody Categoria categoria) {// @Valid uso para marcar o que será
																					// validado || @RequestBody: indica
																					// que o parâmetro estará no corpo
																					// do objeto
		if (categoriaRepository.existsById(categoria.getId())) // existsById método jpa p/ ver se existe através do Id
			return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria)); // aqui eu estou
																										// dizendo que
																										// se existir o
																										// objeto é
																										// pr'eu salvar
																										// no id que
																										// existe no
																										// banco.

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Essa categoria não existe!", null);
		// se não existir, vou lançar uma exceção do tipo request com a mensagem em
		// questão sendo exibida.
	}

	@PutMapping // para atualizar um objeto dentro do banco
	public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria) {
		return categoriaRepository.findById(categoria.getId()).map(resposta -> ResponseEntity.status(HttpStatus.CREATED)// vou
																														// tentar
																														// salvar
																														// caso
																														// o
																														// objeto
																														// exista
				.body(categoriaRepository.save(categoria))).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // caso
																															// não
																															// exista,
																															// o
																															// status
																															// é
																															// o
																															// not
																															// found

		// assim como o post, o put só passa o parâmetro no corpo da requisição
	}

	@ResponseStatus(HttpStatus.NO_CONTENT) // se achar o objeto e conseguir deletar, esse é o status de retorno
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) { // método sem retorno, ele só precisa executar uma ação
		Optional<Categoria> categoria = categoriaRepository.findById(id);

		if (categoria.isEmpty()) // isEmpty - usado para verificar se o objeto em questão está vazio ou não.
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		categoriaRepository.deleteById(id);
	}

}
