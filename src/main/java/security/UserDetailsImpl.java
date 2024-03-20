package security; // implementação de detalhes do usuario

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import loja.model.Usuario;

public class UserDetailsImpl implements UserDetails {// biblioteca do spring

	private static final long serialVersionUID = 1L; // versão de serial usado

	private String userName; // nome de usuario
	private String password; // senha
	private List<GrantedAuthority> authorities; // lista para cuidar das autorizações, direto do spring security

	public UserDetailsImpl(Usuario user) { // construtor preenchido, assim como os getters da model usuario
		this.userName = user.getUsuario();
		this.password = user.getSenha();
	}

	public UserDetailsImpl() {
	} // metodos gerados por indicação do spring

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return authorities; // lista de autorizações chamada acima
	}

	@Override
	public String getPassword() {

		return password;// senha chamada acima
	}

	@Override
	public String getUsername() {

		return userName;// nome de usuario chamada acima
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // tudo que era falso foi trocado para true p/ o usuário fazer o login
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
