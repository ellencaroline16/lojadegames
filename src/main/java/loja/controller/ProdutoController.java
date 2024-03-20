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
import loja.model.Produto;
import loja.repository.ProdutoRepository;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@GetMapping
	public Object getAll() {
		return ResponseEntity.ok(((ProdutoRepository) produtoRepository).findAll());
	}

	@GetMapping("/{id}") // capturo um objeto no banco pelo id
	public ResponseEntity<Produto> getById(@PathVariable Long Id) {// @PathVariable é o parâmetro concreto "te entrego a
																	// garrafa p/ vc trazer cheia de água"
		return (ResponseEntity<Produto>) produtoRepository.findById(Id).map(resposta -> ResponseEntity.ok(resposta))// findById
																													// -
																													// método
																													// JPA
																													// |
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	public ResponseEntity<Object> post(@Valid @RequestBody Produto produto) {
		if (produtoRepository.existsById(produto.getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este produto não existe!", null);
	}

	@PutMapping // para atualizar um objeto dentro do banco
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
		return (ResponseEntity<Produto>) produtoRepository.findById(produto.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED)// vou tentar salvar caso o objeto exista
						.body(produtoRepository.save(produto)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);

		if (produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		produtoRepository.deleteById(id);
	}

}
