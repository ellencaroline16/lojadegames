package security; // classe que filtra a autenticação para retornar ou não o usuário 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter { // garate que ela será chamada uma vez apenas, a cada
															// requisição

	@Autowired // permite que o Spring resolva e atribua dependências automaticamente de uma
				// classe p/ outra
	private JwtService jwtService;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override // é usada para indicar que um método em uma subclasse pretende sobrescrever um
				// método na superclasse.
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, java.io.IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null; // informações que vem da requisição de login

		// verificações para permitir ou não a requisição
		try {
			if (authHeader != null && authHeader.startsWith("Bearer ")) { // se não seja nulo e comece com a Bearer ,
																			// temos uma ação de tirar 7 caracteres e
																			// seguir para o nome de usuario
				token = authHeader.substring(7);
				username = jwtService.extractUsername(token);
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // se o nome de
																										// usuário não é
																										// nulo e nem o
																										// contexto de
																										// autenticação,
																										// ai carregamos
																										// os detalhes
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (jwtService.validateToken(token, userDetails)) { // se o token é válido e devolver dentro do filtro
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| ResponseStatusException e) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;

			// caso nenhuma das anteriores tenha dado certo, é informado a inconsistência e
			// proibido de acessar
		}
	}
}
