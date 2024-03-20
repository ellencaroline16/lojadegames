package security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import loja.model.Usuario;
import loja.repository.UsuarioRepository;

@Service // informo ao spring que é uma classe de serviço
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final String Optional = null;
	@Autowired
	private UsuarioRepository usuarioRepository;

	// verifico se esse usuário que está tentando conectar, existe na base de dados
	// ou não
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(userName);

		if (usuario.isPresent()) // caso exista, show, retorno pela classe UserDetails
			return new UserDetailsImpl(usuario.get());
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN); // caso não, retorno como proibido que é o
																		// forbidden
	}

}
